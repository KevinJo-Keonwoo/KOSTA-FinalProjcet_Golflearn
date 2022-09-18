package com.golflearn.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.golflearn.domain.entity.PageBean;
import com.golflearn.domain.entity.RoundReviewBoardEntity;
import com.golflearn.domain.entity.RoundReviewCommentEntity;
import com.golflearn.domain.entity.RoundReviewLikeEntity;
import com.golflearn.domain.repository.RoundReviewBoardRepository;
import com.golflearn.domain.repository.RoundReviewCommentRepository;
import com.golflearn.domain.repository.RoundReviewLikeRepository;
import com.golflearn.dto.RoundReviewBoardDto;
import com.golflearn.dto.RoundReviewCommentDto;
import com.golflearn.dto.RoundReviewLikeDto;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;
import com.golflearn.exception.RemoveException;

@Service
public class RoundReviewBoardService {
	private static final int CNT_PER_PAGE = 5;
	@Autowired
//	private RoundReviewBoardRepository repo;
	private RoundReviewBoardRepository boardRepo;
	
	@Autowired
	private RoundReviewCommentRepository commentRepo;
	
	@Autowired
	private RoundReviewLikeRepository likeRepo;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
//	/**
//	 * 정렬 기준에 따라 게시글의 목록을 보여줌 
//	 * @param currentPage 현재 페이지 
//	 * @param orderType 프론트에서 받아옴
//	 * @return
//	 * @throws FindException
//	 */
//	public PageBean<RoundReviewBoardDto> boardList(int currentPage, int orderType) throws FindException{
//		int endRow = currentPage * CNT_PER_PAGE;
//		int startRow = endRow - CNT_PER_PAGE + 1;
//		long totalCnt = boardRepo.count();
//		int cntPerPageGroup = 5;
//		List<RoundReviewBoardEntity> list = null;
//		//기본(최신순)이면 0 , 조회수순 1 , 좋아요순 2
//		if (orderType == 1) {
//			list = boardRepo.findListByViewCnt(startRow, endRow);
//		}else if (orderType == 2) {
//			list = boardRepo.findListByLike(startRow, endRow);
//		}else {
//			list = boardRepo.findListByRecent(startRow, endRow);
//		}
//		ModelMapper modelMapper = new ModelMapper();
//		List<RoundReviewBoardDto> dtoList = list.stream()
//				.map(RoundReviewBoardEntity -> modelMapper
//						.map(RoundReviewBoardEntity, RoundReviewBoardDto.class))
//				.collect(Collectors.toList());
////		모델매퍼 안썼을 때 
////		com.golflearn.dto.RoundReviewBoardDto dto = new com.golflearn.dto.RoundReviewBoardDto();
////		List<RoundReviewBoardDto> dtoList = new ArrayList<>();
////		for(RoundReviewBoardEntity b: list) {
////			RoundReviewBoardDto dto = new RoundReviewBoardDto(
////					b.getRoundReviewBoardNo(),b.getRoundReviewBoardTitle(),
////					b.getRoundReviewBoardContent(), b.getUserNickname(), 
////					b.getRoundReviewBoardDt(), b.getRoundReviewBoardViewCnt(), 
////					b.getRoundReviewBoardLikeCnt(), b.getRoundReviewBoardCmtCnt(), 
////					b.getRoundReviewBoardLatitude(), b.getRoundReviewBoardLongitude());
////			dtoList.add(dto);
////		}
//		PageBean<RoundReviewBoardDto> pb = new PageBean<>(dtoList, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);
//		return pb;
//	}
	public Page<RoundReviewBoardDto> boardList(int currentPage, int orderType, Pageable pageable) throws FindException{
//		int endRow = currentPage * CNT_PER_PAGE;
//		int startRow = endRow - CNT_PER_PAGE + 1;
//		long totalCnt = boardRepo.count();
//		int cntPerPageGroup = 5;
//		Page<RoundReviewBoardEntity> list = null;
		//기본(최신순)이면 0 , 조회수순 1 , 좋아요순 2
		//---
		Page<RoundReviewBoardEntity> entity = boardRepo.findAll(pageable);
		ModelMapper modelMapper = new ModelMapper();
		Page<RoundReviewBoardDto> dto = modelMapper.map(entity, new TypeToken<Page<RoundReviewBoardDto>>(){}.getType());
		
		//---
//		if (orderType == 1) {
//			list = boardRepo.findListByViewCnt(startRow, endRow, pageable);
//		}
//		}else if (orderType == 2) {
//			list = boardRepo.findListByLike(startRow, endRow pageable);
//		}else {
//			list = boardRepo.findListByRecent(startRow, endRow pageable);
//		}
//		ModelMapper modelMapper = new ModelMapper();
//		List<RoundReviewBoardDto> dtoList = list.stream()
//				.map(RoundReviewBoardEntity -> modelMapper
//						.map(RoundReviewBoardEntity, RoundReviewBoardDto.class))
//				.collect(Collectors.toList());
//		PageBean<RoundReviewBoardDto> pb = new PageBean<>(dtoList, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);
		return dto;
	}
	//검색하기    제목 or 내용 or 닉네임
		public Page<RoundReviewBoardDto> searchBoard(String word, int currentPage) throws FindException{
			int endRow = currentPage * CNT_PER_PAGE * 1;
			int startRow = endRow - CNT_PER_PAGE + 1;
			long totalCnt = boardRepo.count();
			int cntPerPageGroup = 5;
			
			Pageable pageable = PageRequest.of(currentPage, cntPerPageGroup);
//			Page<RoundReviewBoardEntity> entity = boardRepo.findByWord(word, startRow, CNT_PER_PAGE, pageable);
			Page<RoundReviewBoardEntity> entity = boardRepo.findByWord2(word, pageable);
			logger.error(entity.toString());
			ModelMapper modelMapper = new ModelMapper();
			Page<RoundReviewBoardDto> dto = modelMapper.map(entity, new TypeToken<Page<RoundReviewBoardDto>>(){}.getType());
//			ModelMapper modelMapper = new ModelMapper();
//			List<RoundReviewBoardDto> dtoList = entityList.stream()
//					.map(RoundReviewBoardEntity -> modelMapper
//							.map(RoundReviewBoardEntity, RoundReviewBoardDto.class))
//					.collect(Collectors.toList());
//			Page<RoundReviewBoardDto> pb = new PageBean<>(dtoList, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);
			return dto;
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
		Optional<RoundReviewBoardEntity> optB = boardRepo.findById(roundReviewBoardNo);
//		RoundReviewBoardEntity boardEntity = repo.findDetail(roundReviewBoardNo);
//		Optional<RoundReviewBoardEntity> optB = Optional.of(boardEntity);
//		Optional<RoundReviewBoardEntity> boardEntity = boardRepo.findById(roundReviewBoardNo);
//		Optional<List<RoundReviewCommentEntity>> commentEntity = commentRepo.findAllById(roundReviewCommentNo);
		//조회수 증가
		if(optB.isPresent()) {
			RoundReviewBoardEntity entity = optB.get();
			entity.setRoundReviewBoardViewCnt(entity.getRoundReviewBoardViewCnt()+1);
			boardRepo.save(entity);
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
		Optional<RoundReviewBoardEntity> optB = boardRepo.findById(boardNo);
		if(!optB.isPresent()) {
			throw new ModifyException("글이 없습니다");
		}else {
			RoundReviewBoardEntity entity = optB.get();
			//제목, 내용, 지도 수정가능하게
			entity.setRoundReviewBoardTitle(dto.getRoundReviewBoardTitle());
			entity.setRoundReviewBoardContent(dto.getRoundReviewBoardContent());
			entity.setRoundReviewBoardLatitude(dto.getRoundReviewBoardLatitude());
			entity.setRoundReviewBoardLongitude(dto.getRoundReviewBoardLongitude());
			boardRepo.save(entity);
		}
	}
	//원글 + (댓글 + 대댓글) + 좋아요 삭제
	/**
	 * 원글번호를 입력하면 해당하는 댓글, 대댓글, 좋아요 모두 삭제 
	 * @param roundReviewBoardNo
	 * @throws RemoveException
	 */
	public void removeBoard (Long roundReviewBoardNo) throws RemoveException {
		Optional<RoundReviewBoardEntity> optB = boardRepo.findById(roundReviewBoardNo);
		if(!optB.isPresent()) {
			throw new RemoveException("삭제할 글이 없습니다");
		}else {
//			likeRepo.deleteLike(roundReviewBoardNo);
//			likeRepo.deleteById(roundReviewBoardNo);
//			commentRepo.deleteById(roundReviewBoardNo);
			boardRepo.deleteById(roundReviewBoardNo);
		}
	}
	//댓글 + 대댓글 삭제 + 댓글 수 감소(대댓글 수 카운트해야함) ?? 
	//deletebyid가 @id어노테이션 된것만 가능한지 아니면 findbyName으로 작성해줘야 하는지 
	public void removeComment (Long roundReviewBoardNo, Long roundReviewCmtNo) throws RemoveException {
		Optional<RoundReviewBoardEntity> optB = boardRepo.findById(roundReviewBoardNo);
		RoundReviewBoardEntity boardEntity = optB.get();
		boardEntity.setRoundReviewBoardCmtCnt(boardEntity.getRoundReviewBoardCmtCnt()-1);
		boardRepo.save(boardEntity);
		
		//리스트로 받아오지 않나..? 
		Optional<RoundReviewCommentEntity> optC = commentRepo.findById(roundReviewBoardNo);
		if(!optC.isPresent()) {
			throw new RemoveException("삭제할 댓글이 없습니다");
		}else {
			commentRepo.deleteById(roundReviewBoardNo);
		}
	}
	//대댓글 삭제 (댓글 수 감소) 
	public void removeRecomment (Long roundReviewBoardNo, Long roundReviewCmtNo) throws RemoveException {
		Optional<RoundReviewBoardEntity> optB = boardRepo.findById(roundReviewBoardNo);
		RoundReviewBoardEntity boardEntity = optB.get();
		boardEntity.setRoundReviewBoardCmtCnt(boardEntity.getRoundReviewBoardCmtCnt()-1);
		boardRepo.save(boardEntity);
		
		Optional<RoundReviewCommentEntity> optC = commentRepo.findById(roundReviewCmtNo);
		if(!optC.isPresent()) {
			throw new RemoveException("삭제할 대댓글이 없습니다");
		}else {
			commentRepo.deleteById(roundReviewCmtNo);
		}
	}
	//좋아요 추가 + 좋아요 수 증가 / 좋아요 취소 좋아요 수 감소 
	//유저 아이디로 구분한 것이 존재하는지 컨트롤러에서 
//	public void addLike (RoundReviewLikeDto dto) throws AddException {
//		Optional<RoundReviewBoardEntity> optB = boardRepo.findById(dto.getRoundReviewBoard().getRoundReviewBoardNo());
//		if (optB.isPresent()) {
//			//좋아요 수 증가
//			RoundReviewBoardEntity boardEntity = optB.get();
//			boardEntity.setRoundReviewBoardLikeCnt(boardEntity.getRoundReviewBoardLikeCnt()+1);
//			boardRepo.save(boardEntity);
//			
//			//좋아요 추가 
//			logger.error(dto.getUserNickname());
//	//		logger.error(dto.getRoundReviewBoard().getRoundReviewBoardNo().toString());
////			logger.error(roundReviewBoardNo.toString());
//			ModelMapper modelMapper = new ModelMapper();
//			RoundReviewLikeEntity likeEntity = modelMapper.map(dto, RoundReviewLikeEntity.class);
//			likeRepo.save(likeEntity);
//		} else {
//			throw new AddException("게시글이 존재하지 않습니다");
//		}
//	}
	public void addLike (Long roundReviewBoardNo, RoundReviewLikeDto dto) throws AddException {
		Optional<RoundReviewBoardEntity> optB = boardRepo.findById(roundReviewBoardNo);
		if (optB.isPresent()) {
			//좋아요 수 증가
			RoundReviewBoardEntity boardEntity = optB.get();
			boardEntity.setRoundReviewBoardLikeCnt(boardEntity.getRoundReviewBoardLikeCnt()+1);
			boardRepo.save(boardEntity);
			
			//좋아요 추가 
			ModelMapper modelMapper = new ModelMapper();
			RoundReviewLikeEntity likeEntity = modelMapper.map(dto, RoundReviewLikeEntity.class);
			likeRepo.save(likeEntity);
		} else {
			throw new AddException("게시글이 존재하지 않습니다");
		}
	}
	public void removeLike (Long roundReviewBoardNo, String userNickname) throws RemoveException{
		Optional<RoundReviewBoardEntity> optB = boardRepo.findById(roundReviewBoardNo);
		RoundReviewBoardEntity boardEntity = optB.get();
		boardEntity.setRoundReviewBoardLikeCnt(boardEntity.getRoundReviewBoardLikeCnt()-1);
		boardRepo.save(boardEntity);
		
		likeRepo.deleteLike(roundReviewBoardNo, userNickname); //좋아요 취소 
	}
	
	//게시글 작성
	/**
	 * 게시글 작성하기 
	 * @param dto
	 * @throws AddException
	 */
	public RoundReviewBoardDto writeBoard(RoundReviewBoardDto dto) throws AddException {
		ModelMapper modelMapper = new ModelMapper();
		RoundReviewBoardEntity entity = modelMapper.map(dto, RoundReviewBoardEntity.class);
		boardRepo.save(entity);
		
		Optional<RoundReviewBoardEntity> optRb = boardRepo.findById(entity.getRoundReviewBoardNo());
		RoundReviewBoardEntity boardEntity = optRb.get();
		RoundReviewBoardDto boardDto = modelMapper.map(boardEntity, RoundReviewBoardDto.class);
		return boardDto;
	}
	/**
	 * 댓글 등록 + 댓글 수 증가 
	 * @param dto
	 * @throws AddException
	 */
	public void addComment(Long roundReviewBoardNo, RoundReviewCommentDto dto) throws AddException{
		logger.error("날짜" + dto.getRoundReviewCmtDt());
		Optional<RoundReviewBoardEntity> optB = boardRepo.findById(roundReviewBoardNo);
		RoundReviewBoardEntity boardEntity = optB.get();
		boardEntity.setRoundReviewBoardCmtCnt(boardEntity.getRoundReviewBoardCmtCnt()+1);
		boardRepo.save(boardEntity);
		
		ModelMapper modelMapper = new ModelMapper();
		RoundReviewCommentEntity commentEntity = modelMapper.map(dto, RoundReviewCommentEntity.class);
		logger.error("엔터티날짜" + commentEntity.getRoundReviewCmtDt());
		commentRepo.save(commentEntity);
	}
	//댓글 수정
	/**
	 * 게시글에 달린 댓글 내용 수정하기
	 * @param dto
	 * @throws ModifyException
	 */
	public void modifyComment(RoundReviewCommentDto dto) throws ModifyException{
		ModelMapper modelMapper = new ModelMapper();
		RoundReviewCommentEntity entity = modelMapper.map(dto, RoundReviewCommentEntity.class);
		Optional<RoundReviewCommentEntity> optC = commentRepo.findById(dto.getRoundReviewBoard().getRoundReviewBoardNo());
		if(!optC.isPresent()) {
			throw new ModifyException("댓글이 없습니다");
		}else {
			RoundReviewCommentEntity commentEntity = optC.get();
			commentEntity.setRoundReviewCmtContent(entity.getRoundReviewCmtContent());
			commentRepo.save(commentEntity);
		}
	}
	//대댓글 수정
	public void modifyRecomment(RoundReviewCommentDto dto) throws ModifyException{
		ModelMapper modelMapper = new ModelMapper();
		RoundReviewCommentEntity entity = modelMapper.map(dto, RoundReviewCommentEntity.class);
		Optional<RoundReviewCommentEntity> optC = commentRepo.findById(dto.getRoundReviewCmtNo());
		if(!optC.isPresent()) {
			throw new ModifyException("대댓글이 없습니다");
		}else {
			RoundReviewCommentEntity commentEntity = optC.get();
			commentEntity.setRoundReviewCmtContent(entity.getRoundReviewCmtContent());
			commentRepo.save(commentEntity);
		}
	}	
}
