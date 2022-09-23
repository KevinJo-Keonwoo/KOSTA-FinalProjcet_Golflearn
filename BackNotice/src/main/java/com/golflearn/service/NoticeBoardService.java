package com.golflearn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.golflearn.domain.entity.NoticeBoardEntity;
import com.golflearn.domain.entity.NoticeCommentEntity;
import com.golflearn.domain.entity.NoticeLikeEntity;
import com.golflearn.domain.respository.NoticeBoardRepository;
import com.golflearn.domain.respository.NoticeCommentRepository;
import com.golflearn.domain.respository.NoticeLikeRepository;
import com.golflearn.dto.NoticeBoardDto;
import com.golflearn.dto.NoticeCommentDto;
import com.golflearn.dto.NoticeLikeDto;
import com.golflearn.dto.PageBean;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;
import com.golflearn.exception.RemoveException;

import lombok.Singular;

@Service
public class NoticeBoardService {
	@Autowired
	private NoticeBoardRepository boardRepository;

	@Autowired
	private NoticeCommentRepository commentRepository;

	@Autowired
	private NoticeLikeRepository likeRepository;

	private static final int CNT_PER_PAGE = 5;
	
	@Transactional
	public PageBean<NoticeBoardDto> boardList(int currentPage) throws FindException {

		int endRow = currentPage * CNT_PER_PAGE;
		int startRow = endRow - CNT_PER_PAGE + 1; 
		List<NoticeBoardEntity> list = boardRepository.findByPage(startRow,endRow);
		
//		List<NoticeBoardDto> boarDto = list.getContent().stream().map(boardEntity ->
//		sourceToDestination(boardEntity, new ResaleBoardDto()))
//		.collect(Collectors.toList());
		long totalCnt = boardRepository.count(); // 총 행수를 얻어오는 메서드
		int cntPerPageGroup = 5;

		PageBean<NoticeBoardDto> pb = new PageBean(list, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);
		return pb;
	}
	
	@Transactional
	public NoticeBoardDto viewNoticeBoard(long noticeBoardNo) throws FindException {
		Optional<NoticeBoardEntity> optB = boardRepository.findById(noticeBoardNo);
		NoticeBoardDto dto = new NoticeBoardDto();
		NoticeBoardEntity en = new NoticeBoardEntity();

		if(optB.isPresent()) {
			en = optB.get();
			en = NoticeBoardEntity.builder()
					.noticeBoardTitle(en.getNoticeBoardTitle())
					.noticeBoardCmtCnt(en.getNoticeBoardCmtCnt())
					.noticeBoardContent(en.getNoticeBoardContent())
					.noticeBoardDt(en.getNoticeBoardDt())
					.noticeCommentList(en.getNoticeCommentList())
					.noticeLikeList(en.getNoticeLikeList())
					.userNickname(en.getUserNickname())
					.noticeBoardNo(en.getNoticeBoardNo())
					.noticeBoardLikeCnt(en.getNoticeBoardLikeCnt())
					.noticeBoardViewCnt(en.getNoticeBoardViewCnt()+1).build();

			boardRepository.save(en);
		}else {
			throw new FindException("게시글이 없습니다");
		}

//		Optional<NoticeBoardEntity> optB1 = boardRepository.findById(noticeBoardNo);
		NoticeBoardEntity optB1 = optB.get();
//		if(optB1.isPresent()) {

//			List<NoticeCommentDto> cDto = new ArrayList<>();
//			for(NoticeCommentEntity nCe : optB.get().getNoticeCommentList()){
//				NoticeCommentDto cmtDto = NoticeCommentDto.builder()
//						.noticeCmtContent(nCe.getNoticeCmtContent())
//						.noticeCmtDt(nCe.getNoticeCmtDt())
//						.noticeCmtNo(nCe.getNoticeCmtNo())
//						.noticeCmtParentNo(nCe.getNoticeCmtParentNo())
//						.userNickname(nCe.getUserNickname())
//						.build();
//				cDto.add(cmtDto);
//			}
//			
//			List<NoticeLikeDto> lDto = new ArrayList<>();
//			for(NoticeLikeEntity nLe : optB.get().getNoticeLikeList()){
//				NoticeLikeDto likeDto = NoticeLikeDto.builder()
//						.noticeLikeNo(nLe.getNoticeLikeNo())
//						.userNickname(nLe.getUserNickname())
//						.build();
//				lDto.add(likeDto);
//			}
			ModelMapper modelMapper = new ModelMapper();
			NoticeBoardDto dto1 = modelMapper.map(optB1,NoticeBoardDto.class); 
			
//			NoticeBoardEntity nBe = optB1.get();
//			dto = NoticeBoardDto.builder()
//					.noticeBoardNo(nBe.getNoticeBoardNo())
//					.noticeBoardTitle(nBe.getNoticeBoardTitle())
//					.noticeBoardContent(nBe.getNoticeBoardContent())
//					.noticeBoardDt(nBe.getNoticeBoardDt())
//					.noticeBoardCmtCnt(nBe.getNoticeBoardCmtCnt())
//					.noticeBoardLikeCnt(nBe.getNoticeBoardLikeCnt())
//					.noticeBoardViewCnt(nBe.getNoticeBoardViewCnt())
//					.userNickname(nBe.getUserNickname())
//					.noticeCommentList(nBe.getNoticeCommentList())
//					.noticeLikeList(nBe.getNoticeLikeList())
//					.build();

			return dto1;
//		}else {
//			throw new FindException("게시글이 없습니다");
//		}
	}


	public PageBean<NoticeBoardDto> searchBoard(String word, int currentPage) throws FindException{

		int endRow = currentPage * CNT_PER_PAGE;
		int startRow = endRow - CNT_PER_PAGE + 1;
		List<NoticeBoardEntity> entityList = boardRepository.findByWord(word, startRow, endRow);

		int totalCnt = boardRepository.findCountByWord(word);
		int cntPerPageGroup = 5;
		
		//리스트로반환해야함
		
		ModelMapper modelMapper = new ModelMapper();
		List<NoticeBoardDto> dtoList = 
				entityList.stream().map(NoticeBoardEntity -> modelMapper.map(NoticeBoardEntity, NoticeBoardDto.class))
				.collect(Collectors.toList());
		
		PageBean<NoticeBoardDto> pb = 
				new PageBean<>(dtoList, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);
		return pb;
	}


	/**
	 * 게시글 추가
	 * @param noticeBoardNo
	 * @return 
	 * @throws FindException
	 */
	public NoticeBoardDto writeBoard(NoticeBoardEntity noticeBoard) throws AddException {
		boardRepository.save(noticeBoard);

		Optional<NoticeBoardEntity> optNb = boardRepository.findById(noticeBoard.getNoticeBoardNo());

		if(optNb.isPresent()) {
			NoticeBoardEntity boardEntity = optNb.get();
			NoticeBoardDto nbDto = boardEntity.toDto();

			return nbDto;
		}else {
			throw new AddException("게시글이 없습니다");
		}
	}

	/**
	 * 게시글 수정하기
	 * @param board
	 * @throws ModifyException
	 */
	public void modifyBoard(NoticeBoardEntity noticeBoard) throws ModifyException {
		Optional<NoticeBoardEntity> optB = boardRepository.findById(noticeBoard.getNoticeBoardNo()); //boardNo가 PK이기 때문에 findById의 인자 BoardNo인 것
		if(!optB.isPresent()) {
			throw new ModifyException("글이 없습니다.");
		}else { // Content만 변경하고자 한다.
			NoticeBoardEntity b = optB.get();

			b = NoticeBoardEntity.builder()
					.userNickname(noticeBoard.getUserNickname())
					.noticeBoardNo(b.getNoticeBoardNo())
					.noticeBoardDt(b.getNoticeBoardDt())
					.noticeBoardCmtCnt(b.getNoticeBoardCmtCnt())
					.noticeBoardLikeCnt(b.getNoticeBoardLikeCnt())
					.noticeBoardViewCnt(b.getNoticeBoardViewCnt())
					.noticeBoardTitle(noticeBoard.getNoticeBoardTitle())
					.noticeBoardContent(noticeBoard.getNoticeBoardContent())
					.build();

			boardRepository.save(b);
		}
	}


	@Transactional // 현재 removeBoard 메서드에서 한개 이상의 DML구문이 실행되게끔 코드가 구현되어져 있기 때문에 한 개의 트랜잭션에서 관리가 되어져야 하는 것들이다.
	public void removeBoard(Long boardNo) throws RemoveException {
		Optional<NoticeBoardEntity> optB = boardRepository.findById(boardNo);
		if(!optB.isPresent()) {
			throw new RemoveException("글이 없습니다.");
		} else {

			boardRepository.deleteLike(boardNo);
			boardRepository.deleteReply(boardNo);
			boardRepository.findById(boardNo);
			boardRepository.deleteById(boardNo);
		}
	}

	/**
	 * 댓글쓰기
	 * @param noticeCommentDto
	 * @throws AddException
	 */
	public void replyBoard(NoticeCommentEntity noticeComment) throws AddException{
//		if(noticeComment.getNoticeCmtParentNo() == 0L) {
//			throw new AddException("답글쓰기의 부모글번호가 없습니다");
//		}
		//		noticeCommentDto = NoticeCommentDto.builder()
		//								.noticeCmtNo(noticeCommentDto.getNoticeCmtNo())
		//								.noticeCmtDt(noticeCommentDto.getNoticeCmtDt())
		//								.noticeCmtContent(noticeCommentDto.getNoticeCmtContent())
		//								.noticeCmtParentNo(noticeCommentDto.getNoticeCmtParentNo())
		//								.noticeBoard(noticeCommentDto.getNoticeBoard())
		//								.build();
		System.out.println(noticeComment.getNoticeCmtContent());
		commentRepository.save(noticeComment);
	}

	//	public void deleteComment(NoticeCommentEntity noticeComment) throws RemoveException {
	//		//게시글이 존재하는지 파악
	//		Optional<NoticeBoardEntity> optB = boardRepository.findById(noticeComment.getNoticeBoard().getNoticeBoardNo());
	//		if(!optB.isPresent()) {
	//			throw new RemoveException("글이 없습니다.");
	//		} else {
	//			boardRepository.deleteById(noticeComment.getNoticeCmtNo());
	//		}
	//	}

	public void removeComment(long commentNo) throws RemoveException {
		Optional<NoticeCommentEntity> optB = commentRepository.findById(commentNo);
		if(!optB.isPresent()) {
			throw new RemoveException("댓글이 없습니다.");
		} else {
			commentRepository.deleteById(commentNo);
		}
	}

	public void modifyComment(NoticeCommentEntity noticeComment) throws ModifyException {
		Optional<NoticeCommentEntity> optB = commentRepository.findById(noticeComment.getNoticeCmtNo());
		if(!optB.isPresent()) {
			throw new ModifyException("댓글이 없습니다.");
		}else { // Content만 변경하고자 한다.
			NoticeCommentEntity cmt = optB.get();

			cmt = NoticeCommentEntity.builder()
					.noticeCmtParentNo(cmt.getNoticeCmtParentNo())
					.noticeCmtNo(cmt.getNoticeCmtNo())
					.noticeCmtDt(cmt.getNoticeCmtDt())
					.noticeCmtContent(noticeComment.getNoticeCmtContent())
					.userNickname(cmt.getUserNickname())
					.noticeBoard(cmt.getNoticeBoard())
					.build();

			commentRepository.save(cmt);
		}
	}

	/** 
	 * 좋아요 추가(완성)
	 * 좋아요 수 같이 증가
	 * @param resaleLike
	 */
	@Transactional
	public void addLike(NoticeLikeDto likeDto) throws AddException{
		Long noticeBoardNo = likeDto.getNoticeBoardDto().getNoticeBoardNo();
		
		
				System.out.println("--------"+ noticeBoardNo);
		Long noticeLikeNo = likeDto.getNoticeLikeNo();


		Optional<NoticeBoardEntity> optB = boardRepository.findById(noticeBoardNo); // 확인 / resaleBoard 객체 or resaleBoardNo?
		if(optB.isPresent()) {
			NoticeBoardEntity entity = optB.get();
			ModelMapper modelMapper = new ModelMapper();
			NoticeLikeEntity likeEntity = modelMapper.map(likeDto, NoticeLikeEntity.class);	
			
			likeEntity.setNoticeBoard(entity);
			likeRepository.save(likeEntity); // 좋아요 추가
			Long oldLikeCnt = optB.get().getNoticeBoardLikeCnt();
			entity.setNoticeBoardLikeCnt(oldLikeCnt+1);
			
			//--------------
			
			// 좋아요 추가
//			NoticeLikeEntity likeEntity = likeDto.toEntity();	
//			System.out.println("-----"+likeEntity.getNoticeBoard().getNoticeBoardNo());
//			likeEntity = NoticeLikeEntity.builder()
//					.noticeBoard(entity)
//					.noticeLikeNo(likeEntity.getNoticeLikeNo())
//					.userNickname(likeEntity.getUserNickname())
//					.build();
//
//			likeRepository.save(likeEntity); // 좋아요 추가
//			System.out.println("-0-0-00-0-" + entity.getNoticeBoardLikeCnt());
//			entity = NoticeBoardEntity.builder()
//					.noticeBoardNo(entity.getNoticeBoardNo())
//					.userNickname(entity.getUserNickname())
//					.noticeBoardContent(entity.getNoticeBoardContent())
//					.noticeBoardCmtCnt(entity.getNoticeBoardCmtCnt())
//					.noticeBoardDt(entity.getNoticeBoardDt())
//					.noticeBoardTitle(entity.getNoticeBoardTitle())
//					.noticeBoardViewCnt(entity.getNoticeBoardViewCnt())
//					.noticeBoardLikeCnt(entity.getNoticeBoardLikeCnt()+1)
//					.build();

			boardRepository.save(entity); // 좋아요 수 증가
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
	public void removeLike(NoticeLikeDto likeDto) throws RemoveException{
		Long resaleBoardNo = likeDto.getNoticeBoardDto().getNoticeBoardNo();
		//		logger.error("글번호는"+resaleBoardNo);

		Optional<NoticeBoardEntity> optRb = boardRepository.findById(resaleBoardNo); // 좋아요 수 불러오기
		if(optRb.isPresent()) { // 글 존재 시
			NoticeBoardEntity entity = optRb.get();
			Long oldLikeCnt = entity.getNoticeBoardLikeCnt();
			if(oldLikeCnt > 0) { // 좋아요 수가 0보다 크면
				likeRepository.deleteById(likeDto.getNoticeLikeNo()); // 좋아요 삭제
				entity.setNoticeBoardLikeCnt(oldLikeCnt-1); // 좋아요 수 감소
			}
		} else {
			throw new RemoveException("게시글이 없습니다");
		}
	}

}
