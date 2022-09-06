package com.golflearn.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golflearn.domain.entity.ResaleBoardEntity;
import com.golflearn.domain.entity.ResaleCommentEntity;
import com.golflearn.domain.entity.ResaleLikeEntity;
import com.golflearn.domain.repository.ResaleBoardRepository;
import com.golflearn.domain.repository.ResaleCommentRepository;
import com.golflearn.domain.repository.ResaleLikeRepository;
import com.golflearn.dto.PageBean;
import com.golflearn.dto.ResaleBoardDto;
import com.golflearn.dto.ResaleCommentDto;
import com.golflearn.dto.ResaleLikeDto;
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

	private static final int CNT_PER_PAGE = 5; // 페이지별 보여줄 목록 수 

	/**
	 * 페이지별 게시글 목록과 페이지 그룹정보를 반환
	 * @param resaleBoardNo
	 * @return
	 * @throws FindException
	 */
	public PageBean<ResaleBoardDto> boardList(int currentPage) throws FindException{ // 반환은 dto로, 주입은 entity
		int endRow = currentPage * CNT_PER_PAGE;
		int startRow = endRow - CNT_PER_PAGE + 1; 

		List<ResaleBoardEntity> rbList = resaleBoardRepo.findByPage(startRow,endRow);
		long totalCnt = resaleBoardRepo.count(); // 총 행수를 얻어오는 메서드
		int cntPerPageGroup = 5;

		ModelMapper modelMapper = new ModelMapper();
		List<ResaleBoardDto> dtoList = 
				rbList.stream().map(ResaleBoardEntity -> modelMapper.map(ResaleBoardEntity, ResaleBoardDto.class))
				.collect(Collectors.toList());
		
		PageBean<ResaleBoardDto> pbDto = new PageBean<>(dtoList, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);

		return pbDto;
	}


	/**
	 * 게시글 상세 보기
	 * 게시글을 보던 중 게시글이 삭제 되면 FindException발생
	 * @param resaleBoardNo
	 * @return
	 * @throws FindException
	 */
	public ResaleBoardDto boardDetail(Long resaleBoardNo) throws FindException{

		// 1. 조회수 증가
		Optional<ResaleBoardEntity> optRb = resaleBoardRepo.findById(resaleBoardNo); //게시판에서 조회수 조회
		if(optRb.isPresent()) {
			ResaleBoardEntity entity = optRb.get();
			entity.setResaleBoardViewCnt(entity.getResaleBoardViewCnt()+1); // 조회수 가지고옴
			resaleBoardRepo.save(entity); // 저장(update)
		}else {
			throw new FindException("게시글이 없습니다.");
		}

		// 2. 게시글 불러오기
		ResaleBoardEntity entity = optRb.get(); // 조회수 증가한 것을 한번 더 가지고 와서 엔티티에 넣어줌
		//dto로 변환
		ModelMapper modelMapper = new ModelMapper();
		ResaleBoardDto dto = modelMapper.map(entity,ResaleBoardDto.class); 
		// 엔티티에 넣어준 것을 dto로 변환하여 ResaleBoardDto 타입의 dto에 넣어줌 (ResaleBoardDto로 반환해야하기때문)

		if(optRb.isPresent()) {
			return dto;
		}else {
			throw new FindException("게시글이 없습니다.");
		}
	}

	/**
	 * 검색어로 게시글 목록 보기
	 * 검색어를 이용한 게시글 검색 목록과 페이지 그룹정보 반환
	 * @param word
	 * @param currentPage
	 * @return
	 * @throws FindException
	 */
	public PageBean<ResaleBoardDto> searchBoard(String word, int currentPage) throws FindException{
		
		int endRow = currentPage * CNT_PER_PAGE;
		int startRow = endRow - CNT_PER_PAGE + 1;
		List<ResaleBoardEntity> entityList = resaleBoardRepo.findByWord(word, startRow, endRow);
		
		int totalCnt = resaleBoardRepo.findCountByWord(word);
		int cntPerPageGroup = 5;
		
		ModelMapper modelMapper = new ModelMapper();
		List<ResaleBoardDto> dtoList = 
				entityList.stream().map(ResaleBoardEntity -> modelMapper.map(ResaleBoardEntity, ResaleBoardDto.class))
				.collect(Collectors.toList());
		
		PageBean<ResaleBoardDto> pb = 
				new PageBean<>(dtoList, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);
		return pb;
	}
	
	/**
	 * 게시글 작성
	 * @param resaleBoard
	 * @throws AddException
	 */
	
	public ResaleBoardDto writeBoard(ResaleBoardDto dto) throws AddException{
		
		ModelMapper modelMapper = new ModelMapper();
		ResaleBoardEntity entity = modelMapper.map(dto, ResaleBoardEntity.class);
		resaleBoardRepo.save(entity);
		 
		//-------- 파일 저장 경로에 폴더명을 게시글번호로 저장하기 위해 ----------
		Optional<ResaleBoardEntity> optRb = resaleBoardRepo.findById(entity.getResaleBoardNo());
		ResaleBoardEntity boardEntity = optRb.get();
		ResaleBoardDto boardDto = modelMapper.map(boardEntity, ResaleBoardDto.class);
		return boardDto;
	}

	/** 
	 * 게시글 수정
	 * @param resaleBoard
	 * @throws ModifyException
	 */
	public void modifyBoard(ResaleBoardDto dto) throws ModifyException{
		Long resaleBoardNo = dto.getResaleBoardNo();
		Optional<ResaleBoardEntity> optRb = resaleBoardRepo.findById(resaleBoardNo);
		if(optRb.isPresent()) { // 게시글이 존재하면
			ResaleBoardEntity entity = optRb.get();
			entity.setResaleBoardContent(dto.getResaleBoardContent()); // resaleBoard.get
			entity.setResaleBoardTitle(dto.getResaleBoardTitle());

			logger.error("변경 내용?" + entity.getResaleBoardContent());
			logger.error("변경 제목?" + entity.getResaleBoardTitle());
			resaleBoardRepo.save(entity);

		}else {
			throw new ModifyException("게시글이 없습니다");
		}
	}

	/**
	 * 게시글 삭제
	 * 댓글, 대댓글, 좋아요 같이 삭제 
	 * @param resaleBoardNo
	 * @throws RemoveException
	 */
	@Transactional
	public void removeBoard(Long resaleBoardNo) throws RemoveException{
		//		Long resaleBoardNo = dto.getResaleBoardNo();
		// 해당 게시글이 있는지 확인
		Optional<ResaleBoardEntity> optRb = resaleBoardRepo.findById(resaleBoardNo);
		if(optRb.isPresent()) { // 게시글 존재하면
			resaleBoardRepo.deleteById(resaleBoardNo);
			//	댓글, 대댓글 삭제
//			resaleBoardRepo.deleteComments(resaleBoardNo);
			//	좋아요 삭제
//			resaleBoardRepo.deleteLike(resaleBoardNo);
			//	원글 삭제
//			resaleBoardRepo.deleteById(resaleBoardNo);
		}else {
			throw new RemoveException("게시글이 없습니다");
		}
	}


	
	/**
	 * 댓글 등록(완성)
	 * 댓글 수도 같이 증가
	 * @param commentDto
	 * @throws AddException
	 */
	@Transactional
	public void writeComment(ResaleCommentDto commentDto) throws AddException{
		
		Long resaleBoardNo = commentDto.getResaleBoard().getResaleBoardNo(); // 원글번호
		ResaleBoardDto boardDto = commentDto.getResaleBoard();
		//1. 댓글 테이블에서 그 글번호에 맞는 글이 있는지 확인
		//2. 있으면 parentNo에 그 글번호를 넣음
		Long resaleCmtParentNo = resaleCommentRepo.findParentCmtNo(resaleBoardNo);
		if(resaleCmtParentNo != null) { // 부모글번호가 있으면
			logger.error("부모댓글번호는 "+ resaleCmtParentNo);
			logger.error("원글번호는 "+ resaleBoardNo);
			
			// 댓글 등록
			ModelMapper modelMapper = new ModelMapper();
			ResaleCommentEntity commentEntity = modelMapper.map(commentDto, ResaleCommentEntity.class);
			commentEntity.setResaleCmtParentNo(resaleCmtParentNo);
			
			ResaleBoardEntity brdEntity = modelMapper.map(boardDto, ResaleBoardEntity.class);
			commentEntity.setResaleBoard(brdEntity);
			resaleCommentRepo.save(commentEntity);
			
		} else { // 부모 글번호가 없으면 0 번
			// 댓글 등록
			ModelMapper modelMapper = new ModelMapper();
			ResaleCommentEntity commentEntity = modelMapper.map(commentDto, ResaleCommentEntity.class);
			ResaleBoardEntity brdEntity = modelMapper.map(boardDto, ResaleBoardEntity.class);
			commentEntity.setResaleBoard(brdEntity);
			resaleCommentRepo.save(commentEntity);
		}
		
		// 댓글 수 증가
		Optional <ResaleBoardEntity> optRb = resaleBoardRepo.findById(resaleBoardNo);
		ResaleBoardEntity boardEntity = optRb.get(); 
		int oldCmtCnt = boardEntity.getResaleBoardCmtCnt();
		boardEntity.setResaleBoardCmtCnt(oldCmtCnt+1);
		resaleBoardRepo.save(boardEntity);
	}
	
	
	/**
	 * (대)댓글 수정
	 * @param dto
	 * @throws ModifyException
	 */
	public void modifyComment(ResaleCommentDto dto) throws ModifyException {
		Long resaleCmtNo = dto.getResaleCmtNo();
		Optional<ResaleCommentEntity> optRc = resaleCommentRepo.findById(resaleCmtNo);
		if(optRc.isPresent()) {
			ResaleCommentEntity entity = optRc.get();
			entity.setResaleCmtContent(dto.getResaleCmtContent());
			//댓글 수정
			resaleCommentRepo.save(entity);
		}else {
			throw new ModifyException("댓글이 없습니다");
		}
	}
	
	
	/**
	 * 댓글 삭제(미완성)
	 * 대댓글 삭제, 댓글 삭제, 댓글 수 감소
	 * @param resaleCmtNo
	 * @throws RemoveException
	 */
	@Transactional
	public void deleteComment(ResaleCommentDto commentDto) throws RemoveException{
		
		// 원글 번호
		Long resaleBoardNo = commentDto.getResaleBoard().getResaleBoardNo();
		
		// 댓글 번호
		Long resaleCmtNo = commentDto.getResaleCmtNo();

		// 부모댓글 번호
		Long resaleCmtParentNo = commentDto.getResaleCmtParentNo();
		
		//원글 조회
		Optional<ResaleBoardEntity> optRb =resaleBoardRepo.findById(resaleBoardNo);
		if(optRb.isPresent()) {
			if(resaleCmtParentNo == 0) { // 부모댓글번호가 0이면
				resaleCommentRepo.deleteReComment(resaleCmtNo); // 대댓글 삭제
				resaleCommentRepo.deleteById(resaleCmtNo); // 댓글 삭제
				
				// 댓글 수 감소
				ResaleBoardEntity boardEntity = optRb.get();
				Integer boardCmtCnt = boardEntity.getResaleBoardCmtCnt();
				System.out.println(boardCmtCnt);
				
				if(boardCmtCnt > 0) {
					Integer oldCmtCnt = boardEntity.getResaleBoardCmtCnt(); //이전 댓글수
					Integer TotalCmtCnt = resaleCommentRepo.findReCommentCnt(resaleCmtParentNo); //대댓글 수
					System.out.println("대댓글 수는" + TotalCmtCnt);
					boardEntity.setResaleBoardCmtCnt(oldCmtCnt- (TotalCmtCnt+1));					
				}else {
					boardEntity.setResaleBoardCmtCnt(0);
				}
				resaleBoardRepo.save(boardEntity);

			}else { // 부모댓글번호가 0이 아니면 대댓글 삭제
				resaleCommentRepo.deleteById(resaleCmtNo); // 대댓글 삭제
				ResaleBoardEntity entity = optRb.get();
				int oldCmtCnt = entity.getResaleBoardCmtCnt();
				if(entity.getResaleBoardCmtCnt()>0) {
					entity.setResaleBoardCmtCnt(oldCmtCnt-1);
				}else {
					entity.setResaleBoardCmtCnt(0);
				}
				resaleBoardRepo.save(entity);
			}
			
		}else {
			throw new RemoveException("글이 없습니다");
		}
		
		
	}
	
//	/**
//	 * 대댓글 삭제(미완)
//	 * @param resaleCmtNo
//	 */
//	@Transactional
//	public void deleteRecomment(ResaleCommentDto cmtDto) throws RemoveException{
//		
//		Long resaleBoardNo = cmtDto.getResaleBoardDto().getResaleBoardNo();
//		System.out.println("원글번호" + resaleBoardNo);
//		
//		Long resaleCmtNo = cmtDto.getResaleCmtNo();
//		System.out.println("댓글번호" + resaleCmtNo);
//		
//		Optional<ResaleBoardEntity> optRb = resaleBoardRepo.findById(resaleBoardNo);
//		if(optRb.isPresent()) {
//			// 대댓글 삭제
//			resaleCommentRepo.deleteById(resaleCmtNo);
//			// 댓글 수 감소
//			ResaleBoardEntity entity = optRb.get();
//			int oldCmtCnt = entity.getResaleBoardCmtCnt();
//			entity.setResaleBoardCmtCnt(oldCmtCnt-1);
//			resaleBoardRepo.save(entity);
//		} else {
//			throw new RemoveException("게시글이 없습니다");
//		}
//	}

	/** 
	 * 좋아요 추가(완성)
	 * 좋아요 수 같이 증가
	 * @param resaleLike
	 */
	public void addLike(ResaleLikeDto likeDto) throws AddException{
		Long resaleBoardNo = likeDto.getResaleBoard().getResaleBoardNo();
		System.out.println(resaleBoardNo);
		Long resaleLikeNo = likeDto.getResaleLikeNo();
		
		
		Optional<ResaleBoardEntity> optRb = resaleBoardRepo.findById(resaleBoardNo); // 확인 / resaleBoard 객체 or resaleBoardNo?
		if(optRb.isPresent()) {
			ResaleBoardEntity entity = optRb.get();
			
			// 좋아요 추가
			ModelMapper modelMapper = new ModelMapper();
			ResaleLikeEntity likeEntity = modelMapper.map(likeDto, ResaleLikeEntity.class);	
			
			likeEntity.setResaleBoard(entity);
			resaleLikeRepo.save(likeEntity); // 좋아요 추가
			int oldLikeCnt = optRb.get().getResaleBoardLikeCnt();
			entity.setResaleBoardLikeCnt(oldLikeCnt+1);
			resaleBoardRepo.save(entity); // 좋아요 수 증가
		}else {
			throw new AddException("게시글이 없습니다");
		}
	}
	
	/**
	 * 좋아요 취소(완성)
	 * 좋아요 수가 0 이상인 경우 같이 감소 (0인 경우 감소시키지 않음)
	 * @param resaleLikeNo
	 * @param resaleBoard
	 * @throws RemoveException
	 */
	@Transactional
	public void removeLike(ResaleLikeDto likeDto) throws RemoveException{
		Long resaleBoardNo = likeDto.getResaleBoard().getResaleBoardNo();
		logger.error("글번호는"+resaleBoardNo);

		Optional<ResaleBoardEntity> optRb = resaleBoardRepo.findById(resaleBoardNo); // 좋아요 수 불러오기
		if(optRb.isPresent()) { // 글 존재 시
			ResaleBoardEntity entity = optRb.get();
			int oldLikeCnt = entity.getResaleBoardLikeCnt();
			if(oldLikeCnt > 0) { // 좋아요 수가 0보다 크면
				resaleLikeRepo.deleteById(likeDto.getResaleLikeNo()); // 좋아요 삭제
				entity.setResaleBoardLikeCnt(oldLikeCnt-1); // 좋아요 수 감소
			} 
		} else {
			throw new RemoveException("게시글이 없습니다");
		}
	}
}


