package com.golflearn.controller;

import java.util.Optional;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golflearn.dto.PageBean;
import com.golflearn.dto.QnABoardDto;
import com.golflearn.dto.ResultBean;
import com.golflearn.exception.FindException;
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
	
	
	//	@GetMapping(value= {"no"})
	//	public ResultBean<QnABoardDto> viewBoard(@PathVariable Long boardNo) {
	//		ResultBean<QnABoardDto> rb = new ResultBean<>();
	//		try {
	//			QnABoardDto qd = qnaService.viewBoard(boardNo);
	//			rb.setStatus(1);
	//			rb.setT(qd);
	//		} catch (FindException e) {
	//			rb.setStatus(0);
	//			rb.setMsg(e.getMessage());
	//			e.printStackTrace();
	//		}
	//		
	//		
	//		return rb;
	//	}
}
