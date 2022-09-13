package com.golflearn.control;

import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.golflearn.domain.entity.PageBean;
import com.golflearn.dto.ResultBean;
import com.golflearn.dto.RoundReviewBoardDto;
import com.golflearn.dto.RoundReviewCommentDto;
import com.golflearn.dto.RoundReviewLikeDto;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;
import com.golflearn.exception.RemoveException;
import com.golflearn.service.RoundReviewBoardService;

@CrossOrigin(origins = "*")
@RestController
//@RequestMapping("roundreview/*")
public class RoundReviewBoardController {
	@Autowired
	private RoundReviewBoardService service;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
//	@GetMapping(value = {"board/list", "board/list/{optOrderType}", "board/list/{optOrderType}/{optCp}"})
//	public ResultBean<PageBean<RoundReviewBoardDto>> list (HttpSession session, @PathVariable Optional<Integer> optOrderType, @PathVariable Optional<Integer> optCp) throws FindException{
//		ResultBean<PageBean<RoundReviewBoardDto>> rb = new ResultBean<PageBean<RoundReviewBoardDto>>();
//		try {
//			int currentPage;
//			if(optCp.isPresent()) {
//				currentPage = optCp.get();
//			} else {
//				currentPage = 1;
//			}
//			int orderType;
//			if(optOrderType.isPresent()) {
//				orderType = optOrderType.get();
//			} else {
//				orderType = 0;
//			}
//			PageBean<RoundReviewBoardDto> pb = service.boardList(currentPage, orderType);
//			rb.setStatus(1);
//			rb.setT(pb);
//		} catch (FindException e) {
//			e.printStackTrace();
//			rb.setStatus(0);
//			rb.setMsg(e.getMessage());
//		}
//		return rb;
//	}
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
	//게시글 검색 임시 주석 
	@GetMapping(value = {"search", "search/{optWord}", "search/{optWord}/{optCp}"})
	public ResultBean<Page<RoundReviewBoardDto>> search(@PathVariable Optional<String> optWord, @PathVariable Optional<Integer> optCp, 
			Pageable pageable){
		ResultBean<Page<RoundReviewBoardDto>> rb = new ResultBean<>();
		Page<RoundReviewBoardDto> pb;
		String word = "";
		logger.error(optWord.toString());
		try {
			if (optWord.isPresent()) {
				word = optWord.get();
			} else {
				word = "";
			}
			int currentPage = 1;
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
	@PostMapping(value = "like/{roundReviewBoardNo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addlike(@PathVariable Long roundReviewBoardNo, @RequestBody RoundReviewLikeDto dto){
		//테스트닉네임
		String loginedNickName = "데빌";
//		Long rr = 1L;
//		roundReviewLike.setRoundReviewLikeNo(rr);
//		dto.setRoundReviewLikeNo(roundReviewBoardNo);
//		roundReviewLike.getRoundReviewBoard().setRoundReviewBoardNo(rr);
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
	

	@PostMapping(value = "board")
	public ResponseEntity<?> writeBoard(@RequestBody RoundReviewBoardDto dto){
		try {
			//테스트 dt
//			java.util.Date utilDate = new java.util.Date();
//			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
//			dto.setRoundReviewBoardDt(sqlDate);
			service.writeBoard(dto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (AddException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping(value = "comment/{roundReviewBoardNo}")
	public ResponseEntity<?> addComment(@PathVariable Long roundReviewBoardNo,@RequestBody RoundReviewCommentDto dto){
		try {
			//테스트 DT
			java.util.Date utilDate = new java.util.Date();
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			dto.setRoundReviewCmtDt(sqlDate);
			service.addComment(roundReviewBoardNo, dto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (AddException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
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
}
