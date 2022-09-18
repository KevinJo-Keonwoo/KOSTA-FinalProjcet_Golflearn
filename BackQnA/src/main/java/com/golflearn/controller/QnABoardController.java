package com.golflearn.controller;

import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.golflearn.dto.PageBean;
import com.golflearn.dto.QnABoardDto;
import com.golflearn.dto.QnACommentDto;
import com.golflearn.dto.ResultBean;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;
import com.golflearn.exception.RemoveException;
import com.golflearn.service.QnABoardService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("board/*")
@Slf4j
public class QnABoardController {
	//	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private QnABoardService qnaService;

	@Autowired
	private ServletContext sc;

	//전체 리스트 불러오기
	@GetMapping(value= {"list", "list/{optCp}"})
	public ResultBean<PageBean<QnABoardDto>> list(@PathVariable Optional<Integer> optCp){
		ResultBean<PageBean<QnABoardDto>> rb = new ResultBean<>();
		try {
			int currentPage;
			if(optCp.isPresent()) { 
				currentPage = optCp.get();
			}else { 
				currentPage = 1;
			}
			PageBean<QnABoardDto> pb = qnaService.boardList(currentPage);
			rb.setStatus(1);
			rb.setT(pb);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}

	//공개글만 목록 불러오기
	@GetMapping(value= {"openlist", "openlist/{optCp}"})
	public ResultBean<PageBean<QnABoardDto>> openBoardList(@PathVariable  Optional<Integer> optCp){
		ResultBean<PageBean<QnABoardDto>> rb = new ResultBean<>();
		try {
			int currentPage;
			if(optCp.isPresent()) { 
				currentPage = optCp.get();
			}else { 
				currentPage = 1;
			}
			PageBean<QnABoardDto> pb = qnaService.openBoardList(currentPage);
			rb.setStatus(1);
			rb.setT(pb);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}

	//userNickname으로 검색하기(for관리자)
	@GetMapping(value= {"search", "search/{optWord}/{optCp}", "search/{optWord}"})
	public ResultBean<PageBean<QnABoardDto>> searchList(@PathVariable Optional<Integer> optCp
			,											@PathVariable Optional<String> optWord){
		ResultBean<PageBean<QnABoardDto>> rb = new ResultBean<>();
		try {
			PageBean<QnABoardDto> pb;
			String userNickname;
			if(optWord.isPresent()) { 
				userNickname = optWord.get();
			}else {
				userNickname = "";
			}
			int currentPage = 1;

			if(optCp.isPresent()) {
				currentPage = optCp.get();
			}
			if("".equals(userNickname)) {
				pb = qnaService.boardList(currentPage);
			}else {
				pb = qnaService.searchWriter(userNickname, currentPage);
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

	//답변대기중인 글들만 조회하기
	@GetMapping(value= {"answerlist", "answerlist/{optCp}"})
	public ResultBean<PageBean<QnABoardDto>> answerlist(@PathVariable Optional<Integer> optCp){
		ResultBean<PageBean<QnABoardDto>> rb = new ResultBean<>();
		try {
			int currentPage;
			if(optCp.isPresent()) { 
				currentPage = optCp.get();
			}else { 
				currentPage = 1;
			}
			PageBean<QnABoardDto> pb = qnaService.AnswerStatusList(currentPage);			
			rb.setStatus(1);
			rb.setT(pb);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}

	//글 상세보기
	@GetMapping(value= "view/{boardNo}")
	public ResultBean<QnABoardDto> viewBoard(@PathVariable Long boardNo) {
		ResultBean<QnABoardDto> rb = new ResultBean<>();
		QnABoardDto qd = new QnABoardDto();
		try {
			qd = qnaService.viewBoard(boardNo);
				rb.setStatus(1);
				rb.setT(qd);				
				return rb;
		} catch (FindException e) {
			e.printStackTrace();
		}
		return rb;
	}

	//글 작성하기
	@PostMapping(value = "write")
	public ResultBean<QnABoardDto> writeboard( @RequestBody QnABoardDto qnaBoard ) {
		ResultBean<QnABoardDto> rb = new ResultBean<>();
		try {
			qnaService.writeBoard(qnaBoard);
			rb.setStatus(1);
		} catch (AddException e) {
			e.printStackTrace();
		}
		return rb;
	}


	//댓글(답변) 달기(관리자만 가능)
	@PostMapping(value= "comment/{boardNo}")
	public ResultBean<QnACommentDto> writeComment(@PathVariable Long boardNo, @RequestBody QnACommentDto qnaComment) {
		ResultBean<QnACommentDto> rb = new ResultBean<>();
		try {
				Long cmtNo = qnaComment.getCommentNo();
				qnaComment.setCommentNo(cmtNo);
//				String nickname = qnaComment.getUserNickname();
//				qnaComment.setUserNickname(nickname);
				qnaService.writeComment(qnaComment);
				rb.setStatus(1);
				rb.setMsg("글이 정상적으로 등록 되었습니다");
		}catch(AddException e) {
			e.printStackTrace();
		}
		return rb;
	}

	//	글 수정하기(댓글이 달렸으면 수정하지 못하게, 글 수정할 때 제목 본문 나옴)
	@PutMapping(value = "{boardNo}")
	public ResultBean<QnABoardDto> modifyBoard(@PathVariable Long boardNo, @RequestBody QnABoardDto qnaBoard) {
		ResultBean<QnABoardDto> rb = new ResultBean<>();
		try {
				qnaBoard.setBoardNo(boardNo);
				qnaService.modifyBoard(qnaBoard);
				rb.setStatus(1);
				rb.setMsg("글이 정상적으로 수정되었습니다");
		} catch (ModifyException e) {
			e.printStackTrace();
		}
		return rb;
	}
	//글 삭제하기(답변까지 삭제)
	@DeleteMapping("delete/{boardNo}")
	public ResultBean<QnABoardDto> deleteBoard (@PathVariable Long boardNo) throws RemoveException{
		ResultBean<QnABoardDto> rb = new ResultBean<>();
		try {
			qnaService.deleteBoard(boardNo);
			return rb;
		}catch (RemoveException e) {
			e.printStackTrace();
			return rb;
		}
	}
	
	//댓글 삭제하기(관리자만 가능)
	@DeleteMapping("comment/delete/{commentNo}")
	public ResultBean<QnACommentDto> deleteComment(@PathVariable Long commentNo) {
		ResultBean<QnACommentDto> rb = new ResultBean<>();
		try {
			qnaService.deleteComment(commentNo);
			return rb;
		}catch(RemoveException e) {
			e.printStackTrace();
			return rb;
		}
	}
}
