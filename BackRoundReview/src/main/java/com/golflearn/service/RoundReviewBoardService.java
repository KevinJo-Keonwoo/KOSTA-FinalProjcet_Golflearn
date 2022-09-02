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
import com.golflearn.dto.RoundReviewLikeDto;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;
import com.golflearn.exception.RemoveException;

@Service
public class RoundReviewBoardService {
	private static final int CNT_PER_PAGE = 5;
	@Autowired
	private RoundReviewBoardRepository repo;
//	private RoundReviewBoardRepository boardRepo;
//	private RoundReviewCommentRepository commentRepo;
//	private RoundReviewLikeRepository likeRepo;
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
		Optional<RoundReviewBoardEntity> optB = repo.findById(roundReviewBoardNo);
//		RoundReviewBoardEntity boardEntity = repo.findDetail(roundReviewBoardNo);
//		Optional<RoundReviewBoardEntity> optB = Optional.of(boardEntity);
//		Optional<RoundReviewBoardEntity> boardEntity = boardRepo.findById(roundReviewBoardNo);
//		Optional<List<RoundReviewCommentEntity>> commentEntity = commentRepo.findAllById(roundReviewCommentNo);
		//조회수 증가
		if(optB.isPresent()) {
			RoundReviewBoardEntity entity = optB.get();
			entity.setRoundReviewBoardViewCnt(entity.getRoundReviewBoardViewCnt()+1);
			repo.save(entity);
		}else {
			throw new FindException("게시글이 없습니다");
		}
		
		RoundReviewBoardEntity entity = optB.get();
		ModelMapper modelMapper = new ModelMapper();
		RoundReviewBoardDto dto = modelMapper.map(entity, RoundReviewBoardDto.class);
		
		//게시글내용반환
		Optional<RoundReviewBoardDto> optB1 = Optional.of(dto);
		
		if (optB1.isPresent()) {
			RoundReviewBoardDto b1 = optB1.get();
			return b1;
		} else {
			throw new FindException("게시글이 없습니다");
		}
	}
	
	//게시글 수정
	/**
	 * 기존 게시글 내용을 가져온후 제목, 내용, 지도 수정된 값을 다시 update 해줌
	 * @param dto
	 * @throws ModifyException
	 */
	public void modifyBoard(RoundReviewBoardDto dto) throws ModifyException {
		Long boardNo = dto.getRoundReviewBoardNo();
		Optional<RoundReviewBoardEntity> optB = repo.findById(boardNo);
		if(!optB.isPresent()) {
			throw new ModifyException("글이 없습니다");
		}else {
			RoundReviewBoardEntity entity = optB.get();
			//제목, 내용, 지도 수정가능하게
			entity.setRoundReviewBoardTitle(dto.getRoundReviewBoardTitle());
			entity.setRoundReviewBoardContent(dto.getRoundReviewBoardContent());
			entity.setRoundReviewBoardLatitude(dto.getRoundReviewBoardLatitude());
			entity.setRoundReviewBoardLongitude(dto.getRoundReviewBoardLongitude());
			repo.save(entity);
		}
	}
	//원글 + (댓글 + 대댓글) + 좋아요 삭제
	/**
	 * 원글번호를 입력하면 해당하는 댓글, 대댓글, 좋아요 모두 삭제 
	 * @param roundReviewBoardNo
	 * @throws RemoveException
	 */
	public void removeBoard (Long roundReviewBoardNo) throws RemoveException {
		Optional<RoundReviewBoardEntity> optB = repo.findById(roundReviewBoardNo);
		if(!optB.isPresent()) {
			throw new RemoveException("삭제할 글이 없습니다");
		}else {
			repo.deleteLike(roundReviewBoardNo);
			repo.deleteComments(roundReviewBoardNo);
			repo.deleteById(roundReviewBoardNo);
		}
		
	}
	//댓글 + 대댓글 삭제
	public void removeComment (Long roundReviewBoardNo) throws RemoveException {
		Optional<RoundReviewBoardEntity> optB = repo.findById(roundReviewBoardNo);
		if(!optB.isPresent()) {
			throw new RemoveException("삭제할 댓글이 없습니다");
		}else {
			repo.deleteComments(roundReviewBoardNo);
		}
	}
	//대댓글 삭제
	public void removeRecomment (Long roundReviewCmtNo) throws RemoveException {
		Optional<RoundReviewBoardEntity> optB = repo.findById(roundReviewCmtNo);
		if(!optB.isPresent()) {
			throw new RemoveException("삭제할 대댓글이 없습니다");
		}else {
			repo.deleteRecomment(roundReviewCmtNo);
		}
	}
	//좋아요 추가 + 좋아요 수 증가 / 좋아요 취소 좋아요 수 감소 
	//유저 아이디로 구분한 것이 존재하는지 컨트롤러에서 
	public void addLike (Long roundReviewBoardNo, RoundReviewLikeDto roundReviewLike) throws RemoveException {
		Optional<RoundReviewBoardEntity> optB = repo.findById(roundReviewBoardNo);
		
		RoundReviewBoardEntity boardEntity = optB.get();
		boardEntity.setRoundReviewBoardLikeCnt(boardEntity.getRoundReviewBoardLikeCnt()+1);
		repo.save(boardEntity);
		
		ModelMapper modelMapper = new ModelMapper();
		RoundReviewBoardEntity likeEntity = modelMapper.map(roundReviewLike, RoundReviewBoardEntity.class);
		repo.save(likeEntity);
	}
	
	public void removeLike (Long roundReviewBoardNo) {
		Optional<RoundReviewBoardEntity> optB = repo.findById(roundReviewBoardNo);
		
		RoundReviewBoardEntity boardEntity = optB.get();
		boardEntity.setRoundReviewBoardLikeCnt(boardEntity.getRoundReviewBoardLikeCnt()-1);
		repo.save(boardEntity);
		
		repo.deleteLike(roundReviewBoardNo); //좋아요 취소 
	}
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
	
	//게시글 작성
	
	
	
}
