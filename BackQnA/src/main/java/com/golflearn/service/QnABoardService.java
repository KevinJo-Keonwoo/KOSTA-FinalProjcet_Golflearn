package com.golflearn.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golflearn.domain.entity.QnABoardEntity;
import com.golflearn.domain.repository.QnABoardRepository;
import com.golflearn.domain.repository.QnACommentRepository;
import com.golflearn.dto.PageBean;
import com.golflearn.dto.QnABoardDto;
import com.golflearn.exception.FindException;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class QnABoardService {
	private static final int CNT_PER_PAGE = 5; //페이지별 보여줄 목록수
	@Autowired
	private QnABoardRepository boardRepo;
	private QnACommentRepository commentRepo;
	/**
	 * 페이지별 게시글 목록과 페이지 그룹 정보를 반환한다.
	 * @param currentPage 검색할 페이지
	 * @return
	 * @throws FindException
	 */
	public PageBean<QnABoardDto> boardList(int currentPage) throws FindException {
		int cntPerPageGroup = 5;
		int endRow = currentPage * CNT_PER_PAGE;
		int startRow = endRow - CNT_PER_PAGE + 1;
		Long totalCnt = boardRepo.count();

		List<QnABoardEntity> list = boardRepo.findByPage(startRow, endRow);

		//가져온 Entity ->Dto로 바꿔야함(db에 있는걸 가져옴) 
		List <QnABoardDto> dtolist = list.stream()
				.map(t -> QnABoardDto.builder()
						.boardNo(t.getBoardNo())
						.boardTitle(t.getBoardTitle())
						.userNickname(t.getUserNickname())
						.qnaBoardDt(t.getQnaBoardDt())
						.qnaBoardSecret(t.getQnaBoardSecret())
						.build())
				.collect(Collectors.toList());
		PageBean<QnABoardDto> pb = new PageBean<>(dtolist, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);
		return pb;
	}

	/**
	 * 페이지별 공개글인 게시글 목록과 페이지 그룹 정보를 반환.
	 * @param currentPage 검색할 페이지
	 * @return
	 * @throws FindException
	 */
	public PageBean<QnABoardDto> openBoardList(int currentPage) throws FindException {
		int cntPerPageGroup = 5;
		int endRow = currentPage * CNT_PER_PAGE;
		int startRow = endRow - CNT_PER_PAGE + 1;
		Long totalCnt = boardRepo.count();

		List<QnABoardEntity> list = boardRepo.findByOpenPost(startRow, endRow);

		//가져온 Entity ->Dto로 바꿔야함(db에 있는걸 가져옴) 
		List <QnABoardDto> dtolist = list.stream()
				.map(t -> QnABoardDto.builder()
						.boardNo(t.getBoardNo())
						.boardTitle(t.getBoardTitle())
						.userNickname(t.getUserNickname())
						.qnaBoardDt(t.getQnaBoardDt())
						.qnaBoardSecret(t.getQnaBoardSecret())
						.build())
				.collect(Collectors.toList());
		PageBean<QnABoardDto> pb = new PageBean<>(dtolist, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);
		return pb;
	}

	/**
	 * 닉네임으로 게시글을 조회한다.
	 * @param userNickname 회원 닉네임
	 * @param currentPage  검색할 페이지
	 * @return
	 * @throws FindException
	 */
	public PageBean<QnABoardDto> searchWriter(String userNickname, int currentPage) throws FindException {
		int cntPerPageGroup = 5;
		int endRow = currentPage * CNT_PER_PAGE;
		int startRow = endRow - CNT_PER_PAGE + 1;
		Long totalCnt = boardRepo.count();
		List<QnABoardEntity> list = boardRepo.findByNickname(userNickname, startRow, endRow);
		//가져온 Entity ->Dto로 바꿔야함(db에 있는걸 가져옴) 
		List <QnABoardDto> dtolist = list.stream()
				.map(t -> QnABoardDto.builder()
						.boardNo(t.getBoardNo())
						.boardTitle(t.getBoardTitle())
						.userNickname(t.getUserNickname())
						.qnaBoardDt(t.getQnaBoardDt())
						.qnaBoardSecret(t.getQnaBoardSecret())
						.build())
				.collect(Collectors.toList());
		PageBean<QnABoardDto> pb = new PageBean<>(dtolist, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);
		return pb;
	}
	
	/**
	 * 답변 대기인 글들만 목록으로 불러온다. 
	 * @param currentPage
	 * @return
	 * @throws FindException
	 */
	public PageBean<QnABoardDto> AnswerStatusList(int currentPage) throws FindException {
		int cntPerPageGroup = 5;
		int endRow = currentPage * CNT_PER_PAGE;
		int startRow = endRow - CNT_PER_PAGE + 1;
		Long totalCnt = boardRepo.count();

		List<QnABoardEntity> list = boardRepo.findByAnswerStatus(startRow, endRow);

		//가져온 Entity ->Dto로 바꿔야함(db에 있는걸 가져옴) 
		List <QnABoardDto> dtolist = list.stream()
				.map(t -> QnABoardDto.builder()
						.boardNo(t.getBoardNo())
						.boardTitle(t.getBoardTitle())
						.userNickname(t.getUserNickname())
						.qnaBoardDt(t.getQnaBoardDt())
						.qnaBoardSecret(t.getQnaBoardSecret())
						.build())
				.collect(Collectors.toList());
		PageBean<QnABoardDto> pb = new PageBean<>(dtolist, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);
		return pb;
	}
	
	
	
	//	public QnABoardDto viewBoard(Long boardNo) throws FindException {
	//		Optional<QnABoardEntity> boardEntity = boardRepo.findById(boardNo);
	//		if(boardEntity.isPresent()) {
	//			QnABoardEntity board = boardEntity.get();
	//			//		System.out.println(board);
	//			QnABoardDto boardDto = QnABoardDto.builder()
	//							.boardNo(board.getBoardNo())
	//							.boardTitle(board.getBoardTitle())
	//							.boardContent(board.getBoardContent())
	//							.userNickname(board.getUserNickname())
	//							.qnaBoardDt(board.getQnaBoardDt())
	//							.build();
	//			return boardDto;
	//		}else {
	//			throw new FindException("게시글이 없습니다");
	//		}
	//	}
}
//		List<QnABoardEntity> board = boardRepo.findDetail(boardNo);
//		
//		
//		List<QnABoardDto> viewboard = board.stream() 
//						.map(t -> QnABoardDto.builder()
//						.boardNo(t.getBoardNo())
//						.boardTitle(t.getBoardTitle())
//						.userNickname(t.getUserNickname())
//						.qnaBoardDt(t.getQnaBoardDt())
//						.comment(t.getComment())
//						.build())
//						.collect(Collectors.toList());
//		
//		ResultBean<QnABoardDto> rb = new ResultBean<>(viewboard);
//		
//		return rb;
//	}


