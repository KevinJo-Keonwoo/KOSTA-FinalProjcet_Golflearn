package com.golflearn.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golflearn.domain.entity.ResaleBoardEntity;
import com.golflearn.domain.entity.ResaleLikeEntity;
import com.golflearn.domain.repository.ResaleBoardRepository;
import com.golflearn.domain.repository.ResaleCommentRepository;
import com.golflearn.domain.repository.ResaleLikeRepository;
import com.golflearn.dto.ResaleBoardDto;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;
import com.golflearn.exception.RemoveException;


@Service
public class ResaleBoardService {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ResaleBoardRepository resaleBoardRepo;

	@Autowired
	ResaleCommentRepository resaleCommentRepo;
	
	@Autowired
	ResaleLikeRepository resaleLikeRepo;
	
	/**
	 * 페이지별 게시글 목록과 페이지 그룹정보를 반환
	 * @param resaleBoardNo
	 * @return
	 * @throws FindException
	 */
//	public PageBean<ResaleBoardDto> boardList(int currentPage) throws FindException{ // 반환은 dto로, 주입은 entity
//		ModelMapper modelMapper = new ModelMapper();
//		ResaleBoardEntity resaleBoardEntity = resaleBoardRepo.findByPage(resaleBoardNo); 
//		ResaleBoardDto resaleBoardDto = modelMapper.map(resaleBoardEntity, ResaleBoardDto.class);		
//		return null;
//	}
	
	/**
	 * 게시글 상세 보기
	 * 게시글을 보던 중 게시글이 삭제 되면 FindException발생
	 * @param resaleBoardNo
	 * @return
	 * @throws FindException
	 */
	public ResaleBoardDto boardDetail(Long resaleBoardNo) throws FindException{
		// 게시글 불러옴
		ResaleBoardEntity resaleBoardEntity = resaleBoardRepo.findDetail(resaleBoardNo);
		
		if(resaleBoardEntity != null) { // 게시글이 있으면
//			resaleBoardRepo.findById(resaleBoardNo); //게시판에서 조회수 조회
			int oldViewCnt = resaleBoardEntity.getResaleBoardViewCnt(); // 조회수 가지고옴
			resaleBoardEntity.setResaleBoardViewCnt(oldViewCnt+1); // 조회수 1 증가
			resaleBoardRepo.save(resaleBoardEntity); // 저장(update)
		}else {
			throw new FindException("게시글이 없습니다.");
		}
		
		// entity -> dto로 변환
		ModelMapper modelMapper = new ModelMapper();
		ResaleBoardDto resaleBoardDto = modelMapper.map(resaleBoardEntity, ResaleBoardDto.class);
		
		if(resaleBoardDto != null) {
			return resaleBoardDto;
		}else {
			throw new FindException("게시글이 없습니다");
		}
	}
	
	/**
	 * 게시글 작성
	 * @param resaleBoard
	 * @throws AddException
	 */
	public void writeBoard(ResaleBoardEntity resaleBoard) throws AddException{
		ResaleBoardEntity resaleBoardEntity = resaleBoardRepo.save(resaleBoard);
		
		ModelMapper modelMapper = new ModelMapper();
		ResaleBoardDto resaleBoardDto = modelMapper.map(resaleBoardEntity, ResaleBoardDto.class);
	}
	
	/** 매핑 어떻게..? ★★★
	 * 게시글 수정
	 * @param resaleBoard
	 * @throws ModifyException
	 */
	public void modifyBoard(ResaleBoardEntity resaleBoard) throws ModifyException{
		Optional<ResaleBoardEntity> optRb = resaleBoardRepo.findById(resaleBoard.getResaleBoardNo());
		if(optRb.isPresent()) { // 게시글이 존재하면
			ResaleBoardEntity rb = optRb.get();
			rb.setResaleBoardContent(resaleBoard.getResaleBoardContent()); // resaleBoard.get
			rb.setResaleBoardTitle(resaleBoard.getResaleBoardTitle());
			
			logger.error("변경 내용?" + resaleBoard.getResaleBoardContent());
			logger.error("변경 제목?" + resaleBoard.getResaleBoardTitle());
			resaleBoardRepo.save(rb);
			
			ModelMapper modelMapper = new ModelMapper();
			ResaleBoardDto resaleBoardDto  = modelMapper.map(optRb, ResaleBoardDto.class);
		}
	}
	/** 매핑..?
	 * 게시글 삭제
	 * 댓글, 대댓글, 좋아요 같이 삭제 - boardRepo에 있는데 각각 commentRepo와 likeRepo로 옮겨야하는 것이 아닌가?
	 * @param resaleBoardNo
	 * @throws RemoveException
	 */
	public void removeBoard(Long resaleBoardNo) throws RemoveException{
		// 해당 게시글이 있는지 확인
		Optional<ResaleBoardEntity> optRb = resaleBoardRepo.findById(resaleBoardNo);
		if(optRb.isPresent()) { // 게시글 존재하면
			//댓글, 대댓글 삭제
			resaleBoardRepo.deleteComments(resaleBoardNo);
			//좋아요 삭제
			resaleBoardRepo.deleteLike(resaleBoardNo);
			//원글 삭제
			resaleBoardRepo.deleteById(resaleBoardNo);
		}else {
			throw new RemoveException("게시글이 없습니다");
		}
	}
	
	//게시글 검색
//	public PageBean
	
	// (대)댓글 등록 + 댓글 수 증가
	// (대)댓글 수정
	// 대댓글 삭제
	// 댓글 삭제 (대댓글 삭제, 댓글 삭제, 댓글 수 감소)
	
	/** 
	 * 좋아요 추가
	 * 좋아요 수 같이 증가
	 * @param resaleLike
	 */
	public void addLike(ResaleLikeEntity resaleLike, ResaleBoardEntity resaleBoard) throws AddException{
		Optional<ResaleBoardEntity> optRb = resaleBoardRepo.findById(resaleBoard.getResaleBoardNo()); // 확인 / resaleBoard 객체 or resaleBoardNo?
		if(optRb.isPresent()) {
			resaleLikeRepo.save(resaleLike); // 좋아요 추가
			int oldLikeCnt = optRb.get().getResaleBoardLikeCnt();
			resaleBoard.setResaleBoardLikeCnt(oldLikeCnt+1);
			resaleBoardRepo.save(resaleBoard); // 좋아요 수 증가
		}else {
			throw new AddException("게시글이 없습니다");
		}
	}
	/**
	 * 좋아요 취소
	 * 좋아요 수가 0 이상인 경우 같이 감소 (0인 경우 감소시키지 않음)
	 * @param resaleLikeNo
	 * @param resaleBoard
	 * @throws RemoveException
	 */
	public void removeLike(Long resaleLikeNo, ResaleBoardEntity resaleBoard) throws RemoveException{
		Optional<ResaleBoardEntity> optRb = resaleBoardRepo.findById(resaleBoard.getResaleBoardNo()); // 확인 / resaleBoard 객체 or resaleBoardNo?
		if(optRb.isPresent()) {
			if(optRb.get().getResaleBoardLikeCnt() > 0) { // 좋아요 수가 0보다 크면
				resaleLikeRepo.deleteById(resaleLikeNo); // 좋아요 삭제
				int oldLikeCnt = optRb.get().getResaleBoardLikeCnt();
				resaleBoard.setResaleBoardLikeCnt(oldLikeCnt-1);
			}
		}else {
			throw new RemoveException("게시글이 없습니다");
		}
	}
	
}
