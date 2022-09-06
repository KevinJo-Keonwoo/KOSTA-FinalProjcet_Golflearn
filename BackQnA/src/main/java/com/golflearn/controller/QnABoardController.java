package com.golflearn.controller;

import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public ResultBean<PageBean<QnABoardDto>> list(@PathVariable  Optional<Integer> optCp){
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
	@GetMapping(value= {"searchlist/{optWord}/{optCp}", "searchlist/{optWord}"})
	public ResultBean<PageBean<QnABoardDto>> searchList(@PathVariable  Optional<Integer> optCp
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
	public ResultBean<QnABoardDto> viewBoard(@PathVariable Long boardNo, HttpSession session) {
		ResultBean<QnABoardDto> rb = new ResultBean<>();
		QnABoardDto qd = new QnABoardDto();
		String loginedNickname = (String)session.getAttribute("loginNickname");
		String userType = (String)session.getAttribute("userType");
		try {
			qd = qnaService.viewBoard(boardNo);
			if(!(qd.getQnaBoardSecret() == 1)) {
				rb.setStatus(1);
				rb.setT(qd);				
				return rb;
			}else {
				if(!qd.getUserNickname().equals(loginedNickname) || !userType.equals(2)) {
					rb.setStatus(0);
					rb.setMsg("비밀글은 작성자만 열람할 수 있습니다.");
				}
			}
		} catch (FindException e) {
			e.printStackTrace();
		}
		return rb;
	}

	//글 작성하기
	@PostMapping(value = "write")
	public ResultBean<QnABoardDto> writeboard(HttpSession session, @RequestBody QnABoardDto qnaBoard ) {
		ResultBean<QnABoardDto> rb = new ResultBean<>();
		String loginNickname = (String)session.getAttribute("loginNickname");
		//		String loginNickname = "데빌";
		try {
			if(loginNickname == null) {
				rb.setStatus(0);
				rb.setMsg("로그인을 해주세요");
			}
			qnaBoard.setUserNickname(loginNickname);
			//					QnABoardDto.builder()
			//					.boardNo(qnaBoard.getBoardNo())
			//					.boardTitle(qnaBoard.getBoardTitle())
			//					.boardContent(qnaBoard.getBoardContent())
			//					.qnaBoardSecret(qnaBoard.getQnaBoardSecret())
			//					.userNickname(loginNickname)
			//					.build();
			qnaService.writeBoard(qnaBoard);
			rb.setStatus(1);
		} catch (AddException e) {
			e.printStackTrace();
		}
		return rb;
	}


	//댓글(답변) 달기(관리자만 가능)
	@PostMapping(value= "comment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultBean<QnACommentDto> writeComment(HttpSession session, @RequestBody QnACommentDto qnaComment) {
		ResultBean<QnACommentDto> rb = new ResultBean<>();
		//			int userType = session.getAttribute("userType");
		//			String loginNickname = (String)session.getAttribute("loginNickname");

		int userType = 2;
		String loginNickname = "kosta";
		try {
			if (userType != 2) {
				rb.setStatus(0);
				rb.setMsg("관리자만 작성이 가능합니다"); 
			}else {
				Long cmtNo = qnaComment.getCommentNo();
				qnaComment.setCommentNo(cmtNo);
				qnaComment.setUserNickname(loginNickname);
				qnaService.writeComment(qnaComment);
				rb.setStatus(1);
				rb.setMsg("글이 정상적으로 등록 되었습니다");
			}
		}catch(AddException e) {
			e.printStackTrace();
		}
		return rb;
	}

	//	글 수정하기(댓글이 달렸으면 수정하지 못하게, 글 수정할 때 제목 본문 나옴)
	@PutMapping(value = "{boardNo}")
	public ResultBean<QnABoardDto> modifyBoard(HttpSession session, @RequestBody QnABoardDto qnaBoard) {
		ResultBean<QnABoardDto> rb = new ResultBean<>();
		//		String loginNickname = (String)session.getAttribute("loginNickname");	
		String loginNickname = "데빌";
		Long boardNo = qnaBoard.getBoardNo();	
		System.out.println(qnaBoard.getBoardNo());
		try {	
			if(!loginNickname.equals(qnaBoard.getUserNickname())) {
				System.out.println(qnaBoard.getUserNickname());
				rb.setStatus(0);
				rb.setMsg("작성자만 수정이 가능합니다");

			}else {
				qnaBoard.setBoardNo(boardNo);
				qnaService.modifyBoard(qnaBoard);
				rb.setStatus(1);
				rb.setMsg("글이 정상적으로 수정되었습니다");
			}
		} catch (ModifyException e) {
			e.printStackTrace();
		}
		return rb;
	}
	//글 삭제하기(답변까지 삭제)
	@DeleteMapping("{boardNo}")
	public ResultBean<QnABoardDto> deleteBoard (HttpSession session, @PathVariable Long boardNo, @RequestBody QnABoardDto qnaBoard) throws RemoveException{
		ResultBean<QnABoardDto> rb = new ResultBean<>();
//		String loginNickname = (String)session.getAttribute("loginNickname");
//		String userType = (String)session.getAttribute("userType");
		//테스트용
		String loginNickname = "데빌";
		String userType = "2";
//		boardNo = qnaBoard.getBoardNo();
		try {
//			qnaBoard.setBoardNo(boardNo);
			qnaService.deleteBoard(boardNo);
			if (!loginNickname.equals(qnaBoard.getUserNickname()) || !userType.equals("2") ) {
				System.out.println(qnaBoard.getUserNickname());
				rb.setStatus(0);
				rb.setMsg("삭제는 작성자나 관리자만 가능합니다");
			}else {
				rb.setStatus(1);
				rb.setMsg("글이 삭제 되었습니다");
			}
			return rb;
		}catch (RemoveException e) {
			e.printStackTrace();
			return rb;
		}
	}
	
	//댓글 삭제하기(관리자만 가능)
	@DeleteMapping("delete/{commentNo}")
	public ResultBean<QnACommentDto> deleteComment(HttpSession session, @PathVariable Long commentNo, @RequestBody QnACommentDto qnaComment) {
		ResultBean<QnACommentDto> rb = new ResultBean<>();
//		String loginNickname = (String)session.getAttribute("loginNickname");
//		String userType = (String)session.getAttribute("userType");
		//테스트용
		String loginNickname = "kosta";
		String userType = "1";
		try {
			qnaService.deleteComment(commentNo);
			if (loginNickname.equals(qnaComment.getUserNickname()) && userType.equals("2") ) {
				rb.setStatus(1);
				rb.setMsg("글이 삭제 되었습니다");
				System.out.println(qnaComment.getUserNickname());
			}else {
				rb.setStatus(0);
				rb.setMsg("삭제는 작성자나 관리자만 가능합니다");
			}
			return rb;
		}catch(RemoveException e) {
			e.printStackTrace();
			return rb;
		}
	}
}
