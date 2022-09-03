package com.golflearn.control;

import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golflearn.dto.PageBean;
import com.golflearn.dto.ResaleBoardDto;
import com.golflearn.dto.ResaleLikeDto;
import com.golflearn.dto.ResultBean;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;
import com.golflearn.exception.RemoveException;
import com.golflearn.service.ResaleBoardService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("resale/*")
public class ResaleBoardContoller {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ResaleBoardService service;

	@Autowired
	private ServletContext sc;

	/**
	 * 게시글 목록보기
	 * @param optCp
	 * @return
	 */
	@GetMapping(value={"board/list","board/list/{optCp}"})
	public ResultBean<PageBean<ResaleBoardDto>> BoardList(@PathVariable Optional<Integer> optCp){
		// 요청전달데이터 전달 되지 않을 때를 대비하여 사용하는 RESTful의  @PathVariable은 Optional로 설정해줘야함 

		ResultBean<PageBean<ResaleBoardDto>> rb = new ResultBean<>();
		try {
			int currentPage;
			if(optCp.isPresent()) { //currentPage 가 있으면(optional)
				currentPage = optCp.get();
			}else { // 없으면
				currentPage = 1;
			}
			PageBean<ResaleBoardDto> pb = service.boardList(currentPage);
			rb.setStatus(1);
			rb.setT(pb);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}

	/**
	 * 게시글 상세보기
	 * @param resaleBoardNo
	 * @return
	 */
	@GetMapping("{resaleBoardNo}")
	public ResultBean<ResaleBoardDto> viewBoardDetail(@PathVariable Long resaleBoardNo){
		ResultBean<ResaleBoardDto> rb = new ResultBean<>();
		try {
			ResaleBoardDto rbd = service.boardDetail(resaleBoardNo);
			rb.setStatus(1);
			rb.setMsg("상세 목록 불러오기 성공");
			rb.setT(rbd);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}	
		return rb;
	}

	/**
	 * 게시글 수정 
	 * 로그인 닉네임과 게시글 작성자 닉네임과 같으면 수정 가능
	 * @param resaleBoardNo
	 * @param dto
	 * @return
	 */
	@PutMapping(value="{resaleBoardNo}",produces = MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<?> modifyBoard(@PathVariable Long resaleBoardNo,
										 @RequestBody ResaleBoardDto dto,
										 HttpSession session){
		String loginedNickname = (String)session.getAttribute("loginNickname");
		// String loginedNickname = "박공주";
		logger.error(loginedNickname);
		logger.error("작성자 이름은 : " + dto.getUserNickname());
		logger.error(dto.getResaleBoardTitle());
		logger.error(dto.getResaleBoardContent());

		if(loginedNickname == null) { // 로그인된 아이디가 없으면
			return new ResponseEntity<>("로그인하세요", HttpStatus.INTERNAL_SERVER_ERROR);
		}else if(loginedNickname.equals(dto.getUserNickname())) { // 로그인된 아이디와 작성자가 같은 경우 - 제목, 내용 없는 경우				
			if(dto.getResaleBoardTitle() == null || dto.getResaleBoardTitle().equals("")|| 
					dto.getResaleBoardContent() == null || dto.getResaleBoardContent().equals("")) {
				return new ResponseEntity<>("글 내용이나 제목은 반드시 입력하세요",HttpStatus.INTERNAL_SERVER_ERROR);

			}else { // 제목, 내용 있고 로그인된 아이디와 작성자가 같은 경우
				try {
					dto.setResaleBoardNo(resaleBoardNo);
					service.modifyBoard(dto);
					return new ResponseEntity<>(HttpStatus.OK);
				} catch (ModifyException e) {
					e.printStackTrace();
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}else {
			return new ResponseEntity<>("로그인된 아이디와 글 작성자가 다릅니다.",HttpStatus.INTERNAL_SERVER_ERROR); 
		}

	}

	// 게시글 등록 (파일업로드 아직)
	//	@PostMapping("write")
	//	public ResponseEntity<Object> writeBoard (@RequestBody ResaleBoardDto dto){
	//		return null;
	//	}

	/**
	 * 게시글 삭제
	 * @param resaleBoardNo
	 * @param dto
	 * @param session
	 * @return
	 */
	@DeleteMapping(value="board/{resaleBoardNo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultBean<ResaleBoardDto> removeBoard (@PathVariable Long resaleBoardNo, HttpSession session, @RequestBody ResaleBoardDto dto) { //,{
		String loginedNickname = (String) session.getAttribute("loginNickname");
		//테스트용 닉네임
//		String loginedNickname = "박공주";
		
		ResultBean<ResaleBoardDto> rb = new ResultBean<>();
		if(loginedNickname == null) { // 로그인된 아이디가 없으면
			rb.setMsg("로그인 하세요");			
			return rb;
		} else if(loginedNickname.equals(dto.getUserNickname())) { // 로그인 아이디와 글 작성자가 일치하면
			try {
				dto.setResaleBoardNo(resaleBoardNo);
				service.removeBoard(resaleBoardNo);
				rb.setStatus(1);
				rb.setMsg("삭제 성공");
			} catch (RemoveException e) {
				e.printStackTrace();
				rb.setStatus(0);
				rb.setMsg(e.getMessage());
			}
			return rb; 
		}else {
			rb.setMsg("작성자 아이디와 로그인된 아이디가 일치하지 않습니다");
			return rb;
		}
	}
	
	//검색어로 게시글 조회

	// (대)댓글 등록 + 댓글 수 증가
	// (대)댓글 수정
	// 대댓글 삭제
	// 댓글 삭제 (대댓글 삭제, 댓글 삭제, 댓글 수 감소)
	// 좋아요 추가
//	@GetMapping(value = "like/add", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResultBean<ResaleLikeDto> addLike(ResaleLikeDto likeDto,
//											@RequestParam Long resaleBoardNo,
//											HttpSession session){
//		
//		String loginedNickname = (String) session.getAttribute("loginNickname");
//		
//		ResultBean<ResaleLikeDto> rb = new ResultBean<>();
//		try {
//			likeDto.setUserNickname(loginedNickname);
//			System.out.println(" 글번호 " + resaleBoardNo);
//			likeDto.setResaleBoard(
//			service.addLike(likeDto,resaleBoardNo);
//			rb.setStatus(1);
//			rb.setMsg("좋아요 추가 성공");
//		} catch (AddException e) {
//			e.printStackTrace();
//			rb.setStatus(1);
//			rb.setMsg("좋아요 추가 실패");
//		}
//		return rb;
//	}
//	
	@GetMapping(value = "like/add", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultBean<ResaleLikeDto> addLike(ResaleLikeDto likeDto,
											@RequestBody ResaleBoardDto boardDto,
											HttpSession session){
		
		String loginedNickname = (String) session.getAttribute("loginNickname");
		
		ResultBean<ResaleLikeDto> rb = new ResultBean<>();
		try {
			likeDto.setUserNickname(loginedNickname);

//			System.out.println(likeDto.getResaleBoard().getResaleBoardNo());
			System.out.println(" 글번호 " + boardDto.getResaleBoardNo());
			
			service.addLike(likeDto, boardDto);
			rb.setStatus(1);
			rb.setMsg("좋아요 추가 성공");
		} catch (AddException e) {
			e.printStackTrace();
			rb.setStatus(1);
			rb.setMsg("좋아요 추가 실패");
		}
		return rb;
	}

	/**
	 * 좋아요 삭제
	 * @param resaleLikeNo
	 * @param dto
	 * @param session
	 * @return
	 */
	@DeleteMapping(value = "like/{resaleLikeNo}", produces = MediaType.APPLICATION_JSON_VALUE) //Json 형태로 return?!
	public ResultBean<ResaleLikeDto> removeLike(@PathVariable Long resaleLikeNo, 
												@RequestBody ResaleBoardDto dto, 
												HttpSession session){
//		String loginedNickname = (String)session.getAttribute("loginNickname");
		String loginedNickname = "데빌";
		ResultBean<ResaleLikeDto> rb = new ResultBean<>(); // 객체 생성
		
		if(loginedNickname == null) {
			rb.setMsg("로그인하세요");
		}else if(loginedNickname.equals(dto.getUserNickname())) { // 로그인된 닉네임과 좋아요한 닉네임 같으면
			try {
				logger.error("글번호는" + dto.getResaleBoardNo());
				service.removeLike(resaleLikeNo, dto);
				rb.setStatus(1);
				rb.setMsg("좋아요 삭제 성공");
			} catch (RemoveException e) {
				e.printStackTrace();
				rb.setStatus(0);
				rb.setMsg("좋아요 삭제 실패");
			}
		}else {
			rb.setMsg("로그인된 아이디와 좋아요한 아이디가 일치하지 않습니다");
		}
		return rb;
	}
	
}


