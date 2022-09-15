package com.golflearn.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.golflearn.dto.ResultBean;
import com.golflearn.dto.RoundReviewBoardDto;
import com.golflearn.dto.RoundReviewCommentDto;
import com.golflearn.dto.RoundReviewLikeDto;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;
import com.golflearn.exception.RemoveException;
import com.golflearn.service.RoundReviewBoardService;

import net.coobird.thumbnailator.Thumbnailator;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
//@RequestMapping("roundreview/*")
public class RoundReviewBoardController {
	@Autowired
	private RoundReviewBoardService service;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	//파일 저장 경로
	@Value("${spring.servlet.multipart.location}")
	String uploadDirectory;

	@GetMapping(value = {"board/list", "board/list/{optOrderType}", "board/list/{optOrderType}/{optCp}"})
	public ResultBean<Page<RoundReviewBoardDto>> list (HttpSession session, @PathVariable Optional<Integer> optOrderType, @PathVariable Optional<Integer> optCp, 
					@PageableDefault(page = 0, size = 5, sort = "roundReviewBoardNo", direction = Direction.DESC) Pageable pageable) throws FindException{
		ResultBean<Page<RoundReviewBoardDto>> rb = new ResultBean<Page<RoundReviewBoardDto>>();
		try {
			int currentPage;
			if(optCp.isPresent()) {
				currentPage = optCp.get();
			} else {
				currentPage = 0;
			}

			int orderType;
			if(optOrderType.isPresent()) {
				orderType = optOrderType.get();
			} else {
				orderType = 0;
			}
			//프론트에서 orderType를 보내야됨 
			String orderCriteria = "";
			if(orderType == 0) {
				orderCriteria = "roundReviewBoardNo";
			} else if (orderType == 1) {
				orderCriteria = "roundReviewBoardViewCnt";
			} else {
				orderCriteria = "roundReviewBoardLikeCnt";
			}
			pageable = PageRequest.of(currentPage, 5, Sort.by(Sort.Direction.DESC, orderCriteria));
			Page<RoundReviewBoardDto> dto = service.boardList(currentPage, orderType, pageable);
			rb.setStatus(1);
			rb.setT(dto);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}
	//게시글 검색  
	@GetMapping(value = {"search", "search/{optWord}", "search/{optWord}/{optCp}"})
	public ResultBean<Page<RoundReviewBoardDto>> search(@PathVariable Optional<String> optWord, @PathVariable Optional<Integer> optCp, 
			Pageable pageable){
		ResultBean<Page<RoundReviewBoardDto>> rb = new ResultBean<>();
		Page<RoundReviewBoardDto> pb;
		String word = "";
		try {
			if (optWord.isPresent()) {
				word = optWord.get();
			} else {
				word = "";
			}
			int currentPage = 0;
			if(optCp.isPresent()) {
				currentPage = optCp.get();
			}
			if("".equals(word)) {
				int orderType = 0; //나중에 아마 빼도 될듯?
				String orderCriteria = "roundReviewBoardNo";
				pageable = PageRequest.of(currentPage, 5, Sort.by(Sort.Direction.DESC, orderCriteria));
				pb = service.boardList(currentPage, orderType, pageable);
			}else {
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
	//게시물 상세보기 
	@GetMapping(value = "board/{roundReviewBoardNo}")
	public ResultBean<RoundReviewBoardDto> viewBoard(@PathVariable Long roundReviewBoardNo){
		ResultBean<RoundReviewBoardDto> rb = new ResultBean<>();
		try {
			RoundReviewBoardDto dto = service.viewBoard(roundReviewBoardNo);
			rb.setStatus(1);
			rb.setT(dto);
		}catch(FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}
	//게시물 수정하기
	@PutMapping(value = "board/{roundReviewBoardNo}", produces = MediaType.APPLICATION_JSON_VALUE) //세션 유저아이디 잡아오기
	public ResponseEntity<Object> modifyBoard(@PathVariable Long roundReviewBoardNo, @RequestBody RoundReviewBoardDto roundReviewBoard){
		try {
			if(roundReviewBoard.getRoundReviewBoardContent() == null || roundReviewBoard.getRoundReviewBoardContent().equals("") ||
					roundReviewBoard.getRoundReviewBoardTitle() == null || roundReviewBoard.getRoundReviewBoardTitle().equals("")){
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			roundReviewBoard.setRoundReviewBoardNo(roundReviewBoardNo); //해주는 이유는? 
			service.modifyBoard(roundReviewBoard);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (ModifyException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);			
		}
	}
	//cascade할 수 있을지 
	//게시물 삭제
	@Transactional
	@DeleteMapping(value = "board/{roundReviewBoardNo}")
	public ResponseEntity<String> removeBoard(@PathVariable Long roundReviewBoardNo){
		try {
			service.removeBoard(roundReviewBoardNo);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (RemoveException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
    
	
	//게시물 작성하기
	@PostMapping(value = "board", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> writeBoard(@RequestPart(required = false)List<MultipartFile> imageFiles, RoundReviewBoardDto dto){
		RoundReviewBoardDto boardDto = new RoundReviewBoardDto();
		logger.error(dto.getRoundReviewBoardContent());
		logger.error(dto.getRoundReviewBoardTitle());
		logger.error(dto.getUserNickname());
		try {
			//테스트 dt
//			java.util.Date utilDate = new java.util.Date();
//			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
//			dto.setRoundReviewBoardDt(sqlDate);
//			dto.setUserNickname("데빌");
			logger.error(dto.getRoundReviewBoardContent());
			logger.error(dto.getRoundReviewBoardTitle());
			logger.error(dto.getUserNickname());
			boardDto = service.writeBoard(dto);
//			return new ResponseEntity<>(HttpStatus.OK);
		} catch (AddException e) {
			e.printStackTrace();
//			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		Long roundReviewBoardNo = boardDto.getRoundReviewBoardNo();
		
		//파일 저장 폴더
		//spirng.servlet.multipart.location resale_images\\roundReviewBoardNo
		String saveDirectory = uploadDirectory + "roundReview_images\\" + roundReviewBoardNo;
		//파일 경로 생성
		//파일 경로 에 폴더 없으면 폴더 생성 
		if(!new File(saveDirectory).exists()) {
				new File(saveDirectory).mkdir();
		}
		
		//이미지 저장
		int savedImgFileCnt = 0; //서버에 저장된 파일 수
		File thumbnailFile = null;
		if(!imageFiles.isEmpty()) {
			for(MultipartFile imageFile : imageFiles) {
				Long imageFileSize = imageFile.getSize();
				if(imageFileSize > 0) { //파일이 첨부되었을 경우 
					String originFileName = imageFile.getOriginalFilename(); //파일 확장자 가져오기
					logger.error("파일이름: " + originFileName);
					//.뒤의 파일확장자만 자르기
					String fileExtension = originFileName.substring(originFileName.lastIndexOf("."));
					logger.error("파일확장자: " + fileExtension);
					
					//저장파일생성
					String savedImageFileName = "image_" + (savedImgFileCnt+1) + fileExtension;
					//이미지 파일 생성
					File savedImageFile = new File(saveDirectory, savedImageFileName);
					
					try {
						//파일 저장
						FileCopyUtils.copy(imageFile.getBytes(), savedImageFile);
						
						//파일 타입 확인
						String contentType = imageFile.getContentType();
						if(contentType.contains("image/*")) {
							System.out.println("파일타입" + imageFile.getContentType());
							return new ResponseEntity<> ("이미지 파일이 아닙니다", HttpStatus.INTERNAL_SERVER_ERROR);
						}
						savedImgFileCnt++;
						
						//썸네일 만들기
						String thumbnailName = "s_" + savedImageFileName;
						thumbnailFile = new File(saveDirectory, thumbnailName);
						FileOutputStream thumbnailOS = new FileOutputStream(thumbnailFile);
						InputStream imageFileIS = imageFile.getInputStream();
						int width = 100;
						int height = 100;
						Thumbnailator.createThumbnail(imageFileIS, thumbnailOS, width, height);
						
					} catch(IOException e ) {
						e.printStackTrace();
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}else {
					logger.error("이미지 파일이 없습니다");
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
				
			}
		}
		return new ResponseEntity<>("저장 완료", HttpStatus.OK);
	}
	//댓글 작성하기
	@PostMapping(value = "comment/{roundReviewBoardNo}")
	public ResponseEntity<?> addComment(@PathVariable Long roundReviewBoardNo,@RequestBody RoundReviewCommentDto dto){
		try {
			//테스트 DT
//			java.util.Date utilDate = new java.util.Date();
//			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
//			dto.setRoundReviewCmtDt(sqlDate);
			logger.error("첫번째 매개변수" + roundReviewBoardNo);
			logger.error("두번째 배개변수" + dto.getRoundReviewBoard().getRoundReviewBoardNo());
			logger.error("날짜" + dto.getRoundReviewCmtDt());
			
			service.addComment(roundReviewBoardNo, dto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (AddException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//댓글 수정하기
	@PutMapping(value = "comment/{roundReviewCmtNo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> modifyComment(@PathVariable Long roundReviewCmtNo, @RequestBody RoundReviewCommentDto dto){
		try {
			if(dto.getRoundReviewCmtContent() == null || dto.getRoundReviewCmtContent().equals("")){
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			service.modifyComment(dto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (ModifyException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);			
		}
	}
	//댓글 삭제하기
	@Transactional
	@DeleteMapping(value = "comment/{roundReviewBoardNo}/{roundReviewCmtNo}")
	public ResponseEntity<String> removeComment(@PathVariable Long roundReviewBoardNo, @PathVariable Long roundReviewCmtNo){
		try {
			service.removeComment(roundReviewBoardNo, roundReviewCmtNo);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (RemoveException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//대댓글 수정하기
	@PutMapping(value = "recomment/{roundReviewCmtNo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> modifyRecomment(@PathVariable Long roundReviewCmtNo, @RequestBody RoundReviewCommentDto dto){
		try {
			if(dto.getRoundReviewCmtContent() == null || dto.getRoundReviewCmtContent().equals("")){
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			service.modifyRecomment(dto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (ModifyException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);			
		}
	}
	//대댓글 삭제하기
	@DeleteMapping(value = "recomment/{roundReviewBoardNo}/{roundReviewCmtNo}")
	public ResponseEntity<String> removeRecomment(@PathVariable Long roundReviewBoardNo, @PathVariable Long roundReviewCmtNo){
		try {
			service.removeRecomment(roundReviewBoardNo, roundReviewCmtNo);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (RemoveException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//좋아요 추가하기
	@PostMapping(value = "like/{roundReviewBoardNo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addlike(@PathVariable Long roundReviewBoardNo, @RequestBody RoundReviewLikeDto dto){
		//테스트닉네임
		String loginedNickName = "데빌";
//			Long rr = 1L;
//			roundReviewLike.setRoundReviewLikeNo(rr);
//			dto.setRoundReviewLikeNo(roundReviewBoardNo);
//			roundReviewLike.getRoundReviewBoard().setRoundReviewBoardNo(rr);
		dto.setUserNickname(loginedNickName);
		logger.error(roundReviewBoardNo.toString());
		try {
			service.addLike(roundReviewBoardNo, dto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (AddException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//좋아요 삭제하기
	@Transactional
	@DeleteMapping(value = "like/{roundReviewBoardNo}")
	public ResponseEntity<?> removeLike(@PathVariable Long roundReviewBoardNo, String userNickname){
		try {
			//테스트
			String nickname = userNickname;
			service.removeLike(roundReviewBoardNo, nickname);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (RemoveException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
}
