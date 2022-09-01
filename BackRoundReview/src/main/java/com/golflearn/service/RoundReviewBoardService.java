package com.golflearn.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.golflearn.domain.entity.PageBean;
import com.golflearn.domain.entity.RoundReviewBoard;
import com.golflearn.domain.repository.RoundReviewBoardRepository;
import com.golflearn.exception.FindException;

@Service
public class RoundReviewBoardService {
	private static final int CNT_PER_PAGE = 5;
	
	private RoundReviewBoardRepository repo;
	/**
	 * 정렬 기준에 따라 게시글의 목록을 보여줌 
	 * @param currentPage 현재 페이지 
	 * @param orderType 프론트에서 받아옴
	 * @return
	 * @throws FindException
	 */
	public PageBean<RoundReviewBoard> boardList(int currentPage, int orderType) throws FindException{
		int endRow = currentPage * CNT_PER_PAGE;
		int startRow = endRow - CNT_PER_PAGE + 1;
		Long totalCnt = repo.count();
		int cntPerPageGroup = 5;
		List<RoundReviewBoard> list = null;
		//기본(최신순)이면 0 , 조회수순 1 , 좋아요순 2
		if (orderType == 1) {
			list = repo.findListByViewCnt(startRow, endRow);
		}else if (orderType == 2) {
			list = repo.findListByLike(startRow, endRow);
		}else {
			list = repo.findListByRecent(startRow, endRow);
		}
		PageBean<RoundReviewBoard> pb = new PageBean<>(list, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);
		return pb;
	} 
}
