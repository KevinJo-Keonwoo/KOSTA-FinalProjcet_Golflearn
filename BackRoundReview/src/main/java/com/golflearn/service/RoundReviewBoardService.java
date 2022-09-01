package com.golflearn.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golflearn.domain.entity.PageBean;
import com.golflearn.domain.entity.RoundReviewBoardEntity;
import com.golflearn.domain.repository.RoundReviewBoardRepository;
import com.golflearn.dto.RoundReviewBoardDto;
import com.golflearn.exception.FindException;

@Service
public class RoundReviewBoardService {
	private static final int CNT_PER_PAGE = 5;
	@Autowired
	private RoundReviewBoardRepository repo;
	/**
	 * 정렬 기준에 따라 게시글의 목록을 보여줌 
	 * @param currentPage 현재 페이지 
	 * @param orderType 프론트에서 받아옴
	 * @return
	 * @throws FindException
	 */
	public PageBean<RoundReviewBoardDto> boardList(int currentPage, int orderType) throws FindException{
		int endRow = currentPage * CNT_PER_PAGE;
		int startRow = endRow - CNT_PER_PAGE + 1;
		long totalCnt = repo.count();
		int cntPerPageGroup = 5;
		List<RoundReviewBoardEntity> list = null;
		//기본(최신순)이면 0 , 조회수순 1 , 좋아요순 2
		if (orderType == 1) {
			list = repo.findListByViewCnt(startRow, endRow);
		}else if (orderType == 2) {
			list = repo.findListByLike(startRow, endRow);
		}else {
			list = repo.findListByRecent(startRow, endRow);
		}
		ModelMapper modelMapper = new ModelMapper();
		List<RoundReviewBoardDto> dtoList = list.stream()
				.map(RoundReviewBoardEntity -> modelMapper
						.map(RoundReviewBoardEntity, RoundReviewBoardDto.class))
				.collect(Collectors.toList());
//		모델매퍼 안썼을 때 
//		com.golflearn.dto.RoundReviewBoardDto dto = new com.golflearn.dto.RoundReviewBoardDto();
//		List<RoundReviewBoardDto> dtoList = new ArrayList<>();
//		for(RoundReviewBoardEntity b: list) {
//			RoundReviewBoardDto dto = new RoundReviewBoardDto(
//					b.getRoundReviewBoardNo(),b.getRoundReviewBoardTitle(),
//					b.getRoundReviewBoardContent(), b.getUserNickname(), 
//					b.getRoundReviewBoardDt(), b.getRoundReviewBoardViewCnt(), 
//					b.getRoundReviewBoardLikeCnt(), b.getRoundReviewBoardCmtCnt(), 
//					b.getRoundReviewBoardLatitude(), b.getRoundReviewBoardLongitude());
//			dtoList.add(dto);
//		}
		PageBean<RoundReviewBoardDto> pb = new PageBean<>(dtoList, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);
		return pb;
	}
	/**
	 * 라운딩리뷰 게시글의 상세내용을 보여줌 
	 * 기존 게시글 목록을 보던 중 게시자가 게시글을 삭제한 경우
	 * '게시글이 없습니다'를 던져주기 
	 * @param roundReviewBoardNo 라운딩리뷰 게시글번호
	 * @return
	 * @throws FindException 
	 */
	public RoundReviewBoardDto viewBoard(Long roundReviewBoardNo) throws FindException {
		RoundReviewBoardEntity entity = repo.findDetail(roundReviewBoardNo);
		Optional<RoundReviewBoardEntity> optB = Optional.of(entity);
		if(optB.isPresent()) {
			RoundReviewBoardEntity b = optB.get();
			b.setRoundReviewBoardViewCnt(b.getRoundReviewBoardViewCnt()+1);
			repo.save(b);
		}else {
			throw new FindException("게시글이 없습니다");
		}
		
		ModelMapper modelMapper = new ModelMapper();
		RoundReviewBoardDto dto = modelMapper.map(entity, RoundReviewBoardDto.class);
		
		Optional<RoundReviewBoardDto> optB1 = Optional.of(dto);
		if (optB1.isPresent()) {
			RoundReviewBoardDto b1 = optB1.get();
			return b1;
		} else {
			throw new FindException("게시글이 없습니다");
		}
	}
	
	// 조회수 증가
	// 게시글 수정
	
	//원글 + 댓글 + 대댓글 + 좋아요 삭제
	public void removeBoard (Long roundReviewBoardNo) {
		
	}
	//댓글 + 대댓글 삭제
	
	//대댓글 삭제
	
	//좋아요 삭제
	
	//게시글 검색
	
	//댓글 등록 + 댓글 수 증가
	
	//댓글 수정
	
	//대댓글 수정
	
	//댓글 삭제 시 댓글 수 감소
	//대댓글 삭제 시 댓글 수 감소
	
	//좋아요 추가
	//좋아요 수 증가
	
	//좋아요 취소
	//좋아요 수 감소 
	
	
	
	
}
