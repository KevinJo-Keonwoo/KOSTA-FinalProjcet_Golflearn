package com.golflearn.control;

import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("roundreview/*")
public class RoundReviewBoardController {
	@Autowired
	private RoundReviewBoardService service;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@GetMapping(value = {"board/list", "board/list/{optCp}", "board/list/{optCp}/{optOrderType}"})
	public ResultBean<PageBean<RoundReviewBoardDto>> list (HttpSession session, @PathVariable Optional<Integer> optCp, @PathVariable Optional<Integer> optOrderType) throws FindException{
		ResultBean<PageBean<RoundReviewBoardDto>> rb = new ResultBean<PageBean<RoundReviewBoardDto>>();
		try {
			int currentPage;
			if(optCp.isPresent()) {
				currentPage = optCp.get();
			} else {
				currentPage = 1;
			}
			int orderType;
			if(optOrderType.isPresent()) {
				orderType = optOrderType.get();
			} else {
				orderType = 0;
			}
			PageBean<RoundReviewBoardDto> pb = service.boardList(currentPage, orderType);
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
	public ResponseEntity<?> addLike(@PathVariable Long roundReviewBoardNo, @RequestBody RoundReviewLikeDto roundReviewLike){
		
		
		//테스트닉네임
		String loginedNickName = "데빌";
		Long rr = 1L;
		roundReviewLike.setRoundReviewLikeNo(rr);
//		dto.setRoundReviewLikeNo(roundReviewBoardNo);
		roundReviewLike.setUserNickname(loginedNickName);
		roundReviewLike.getRoundReviewBoard().setRoundReviewBoardNo(rr);
		logger.error(roundReviewLike.getUserNickname());
		logger.error(roundReviewLike.getRoundReviewLikeNo().toString());
		logger.error(roundReviewLike.getRoundReviewBoard().getRoundReviewBoardNo().toString());
		try {
			service.addLike(rr, roundReviewLike);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (AddException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//게시글 검색
	@GetMapping(value = {"search", "search/{optWord}", "search/{optWord}/{optCp}"})
	public ResultBean<PageBean<RoundReviewBoardDto>> search(Optional<Integer> optCp, Optional<String> optWord){
		ResultBean<PageBean<RoundReviewBoardDto>> rb = new ResultBean<>();
		PageBean<RoundReviewBoardDto> pb;
		String word;
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
					pb = service.boardList(currentPage, 0);
			}
			pb = service.searchBoard(word, currentPage);
			rb.setStatus(1);
			rb.setT(pb);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}
	@PostMapping(value = "board")
	public ResponseEntity<?> writeBoard(@RequestBody RoundReviewBoardDto dto){
		try {
			service.writeBoard(dto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (AddException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping(value = "comment")
	public ResponseEntity<?> addComment(@PathVariable Long roundReviewBoardNo,@RequestBody RoundReviewCommentDto dto){
		try {
			service.addComment(roundReviewBoardNo, dto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (AddException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PutMapping(value = "comment")
	public ResponseEntity<?> modifyComment(@RequestBody RoundReviewCommentDto dto){
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
	@PutMapping(value = "recomment")
	public ResponseEntity<?> modifyRecomment(@RequestBody RoundReviewCommentDto dto){
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
