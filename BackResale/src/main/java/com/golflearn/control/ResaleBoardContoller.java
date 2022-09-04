package com.golflearn.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.golflearn.dto.PageBean;
import com.golflearn.dto.ResaleBoardDto;
import com.golflearn.dto.ResaleCommentDto;
import com.golflearn.dto.ResaleLikeDto;
import com.golflearn.dto.ResultBean;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;
import com.golflearn.exception.RemoveException;
import com.golflearn.service.ResaleBoardService;

import net.coobird.thumbnailator.Thumbnailator;

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
	@GetMapping("board/{resaleBoardNo}")
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


	// 파일 저장 경로
	@Value("${spring.servlet.multipart.location}")
	String uploadDirectory;
	
	/**
	 * 게시글 등록
	 * @param imageFiles
	 * @param dto
	 * @param session
	 * @return
	 * @throws AddException 
	 */
	@PostMapping("board/write")
	public ResponseEntity<?> writeBoard (@RequestPart(required = false)List<MultipartFile> imageFiles,
										ResaleBoardDto dto, HttpSession session) throws AddException{
//		String loginedNickname = (String) session.getAttribute("loginNickname");
		// 입력 내용 게시글 저장
		String loginedNickname = "데빌";
	
		dto.setUserNickname(loginedNickname);
		ResaleBoardDto boardDto = service.writeBoard(dto);
	
		Long resaleBoardNo = boardDto.getResaleBoardNo();
		logger.error("글번호는"+boardDto.getResaleBoardNo());
		
		// 파일 저장 폴더
		String saveDirectory = uploadDirectory + "resale\\"+ resaleBoardNo;
		//파일 경로 생성
		if(!new File(saveDirectory).exists()) {
			new File(saveDirectory).mkdirs(); //파일 경로에 폴더 없으면 저장
		}
		
		//이미지 저장
		int savedImgFileCnt = 0; // 서버에 저장된 파일 수
		File thumbnailFile = null;
		if(!imageFiles.isEmpty()) {
			for(MultipartFile imageFile : imageFiles) {
				Long imageFileSize = imageFile.getSize(); // 파일 크기
				if(imageFileSize > 0) { // 파일이 첨부되었을 경우
					
					// 파일 확장자 가지고 오기
					String originFileName = imageFile.getOriginalFilename();
					String fileExtension = originFileName.substring(originFileName.lastIndexOf("."));
					logger.error("파일 확장자는" + fileExtension);
					
					//저장파일 이름 생성
					String savedImageFileName = "image_"+ (savedImgFileCnt+1) + fileExtension;
					//이미지 파일 생성
					File savedImageFile = new File(saveDirectory, savedImageFileName);
					
					try {
						// 파일 저장
						FileCopyUtils.copy(imageFile.getBytes(), savedImageFile);
						
						// 파일 타입 확인
						String contentType = imageFile.getContentType();
						if(contentType.contains("image/*")) {
							System.out.println("파일타입" + imageFile.getContentType());
							return new ResponseEntity<> ("이미지 파일이 아닙니다", HttpStatus.INTERNAL_SERVER_ERROR) ;
						}
						savedImgFileCnt++;
						
						//썸네일 만들기
						String thumbnailName = "s_" + savedImageFileName ;
						thumbnailFile = new File(saveDirectory, thumbnailName);
						FileOutputStream thumbnailOS = new FileOutputStream(thumbnailFile);
						InputStream imageFileIS = imageFile.getInputStream();
						int width = 100;
						int height = 100;
						Thumbnailator.createThumbnail(imageFileIS, thumbnailOS , width, height);
//						
						
						//이미지 썸네일다운로드하기
//						HttpHeaders responseHeaders = new HttpHeaders();
//						responseHeaders.set(HttpHeaders.CONTENT_LENGTH, thumbnailFile.length()+"");
//						responseHeaders.set(HttpHeaders.CONTENT_TYPE, Files.probeContentType(thumbnailFile.toPath()));
//						responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename="+URLEncoder.encode("a", "UTF-8"));
//						logger.info("섬네일파일 다운로드"); 
//						return new ResponseEntity<>(FileCopyUtils.copyToByteArray(thumbnailFile), 
//								responseHeaders, 
//								HttpStatus.OK);
						// 일반 응답가지고 안 됨. 다운로드용 응답헤더를ㄹ\ 설정 해 주어야함
						// content_length 응답 내용의 크기, content_type 응답 형식, content_disposition 다운로드 해야한다
						// 응답 내용 -> 썸네일 방식으로 
						// 썸네일 파일의 내용을 바이트 배열로 만들어서 응답 내용으로 쓰겠다.
						// Json형태로 응답받을 것이 아니라면 ResponseEntity형식으로 첫번째 인자를 string과 같은 것으로 받으면 됨
						
					} catch (IOException e) {
						e.printStackTrace();
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
					}
//						
				}else {
					logger.error("이미지 파일이 없습니다");
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}
		return new ResponseEntity<>("저장 완료",HttpStatus.OK);
		
	}
			

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

	/**
	 * 검색어로 게시글 조회
	 * @param optWord
	 * @param optCp
	 * @return
	 */
	@GetMapping(value = "board/search/{optWord}")
	public ResultBean<PageBean<ResaleBoardDto>> serch(
			@PathVariable Optional<String> optWord,
			@PathVariable Optional<Integer> optCp){
		ResultBean<PageBean<ResaleBoardDto>> rb = new ResultBean<>();

		try {
			PageBean<ResaleBoardDto> pb ; 
			String word = ""; 
			if(optWord.isPresent()) {
				word = optWord.get();
			} else { 
				word = "";
			}

			int currentPage = 1;
			if(optCp.isPresent()) {
				currentPage = optCp.get();
			}else {

			}
			if("".equals(word)) {
				pb = service.boardList(currentPage);
			} else {
				pb = service.searchBoard(word, currentPage);
			} 
			rb.setStatus(1);
			rb.setT(pb);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}

	// (대)댓글 등록 + 댓글 수 증가
	// 댓글 삭제 (대댓글 삭제, 댓글 삭제, 댓글 수 감소)

	/**
	 * (대)댓글 수정
	 * @param resaleCmtNo
	 * @param dto
	 * @param session
	 * @return
	 */
	@PostMapping(value = "comment/{resaleCmtNo}" ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultBean<ResaleCommentDto> modifyComment(@PathVariable Long resaleCmtNo,
			@RequestBody ResaleCommentDto dto,
			HttpSession session){
		//		String loginedNickname = (String)session.getAttribute("loginNickname");
		ResultBean<ResaleCommentDto> rb = new ResultBean<>();
		try {
			if(dto.getResaleCmtContent() == null || dto.getResaleCmtContent().equals("")) {
				rb.setMsg("내용을 반드시 입력해주세요");
			}
			dto.setResaleCmtNo(resaleCmtNo);
			service.modifyComment(dto);
			rb.setStatus(1);
			rb.setMsg("수정 성공");
		}catch(ModifyException e){
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg("수정실패");
		}
		return rb;
	}
	

	/**
	 * 대댓글 삭제(미완성)
	 * 대댓글 작성자 닉네임 세션 닉네임 같은 경우 삭제
	 * @param resaleCmtNo
	 * @param dto
	 * @param session
	 * @return
	 */
	@DeleteMapping(value="comment/{resaleCmtNo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultBean<ResaleCommentDto> deleteRecomment(@PathVariable Long resaleCmtNo, 
			@RequestBody ResaleCommentDto cmtDto, 
			HttpSession session){		
		//		String loginedNickname = (String)session.getAttribute("loginNickname");
		//		String loginedNickname = "데빌";

		ResultBean<ResaleCommentDto> rb = new ResultBean<>();
		try {
			cmtDto.setResaleCmtNo(resaleCmtNo);
			service.deleteRecomment(resaleCmtNo);
			rb.setStatus(1);
			rb.setMsg("대댓글 삭제 성공");
		} catch (RemoveException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg("대댓글 삭제 실패");
		}
		return rb;
	}

	/**
	 * 좋아요 추가 (미완성)
	 * @param likeDto
	 * @param boardDto
	 * @param session
	 * @return
	 */
	@GetMapping(value = "like/add", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultBean<ResaleLikeDto> addLike(ResaleLikeDto likeDto,
			@RequestBody Long resaleBoardNo,
			HttpSession session){

//		String loginedNickname = (String) session.getAttribute("loginNickname");
		String loginedNickname = "데빌";
		ResultBean<ResaleLikeDto> rb = new ResultBean<>();
		try {
			likeDto.setUserNickname(loginedNickname);
			
//			System.out.println(" 글번호 " + boardDto.getResaleBoardNo());
			
			service.addLike(likeDto);
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


