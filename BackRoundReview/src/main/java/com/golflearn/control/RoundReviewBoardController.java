package com.golflearn.control;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golflearn.domain.entity.PageBean;
import com.golflearn.dto.ResultBean;
import com.golflearn.dto.RoundReviewBoardDto;
import com.golflearn.exception.FindException;
import com.golflearn.service.RoundReviewBoardService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("roundreviewboard/*")
public class RoundReviewBoardController {
	@Autowired
	private RoundReviewBoardService service;
	
	@GetMapping(value = "list")
	public ResultBean<PageBean<RoundReviewBoardDto>> list (HttpSession session, int currentPage, int orderType) throws FindException{
		ResultBean<PageBean<RoundReviewBoardDto>> rb = new ResultBean<PageBean<RoundReviewBoardDto>>();
		try {
			PageBean<RoundReviewBoardDto> pb = service.boardList(currentPage, orderType);
			rb.setT(pb);
		} catch (FindException e) {
			e.printStackTrace();
		}
		return rb;
	}
}
