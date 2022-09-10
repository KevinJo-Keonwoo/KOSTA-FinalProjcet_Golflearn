package com.golflearn.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golflearn.domain.entity.QnABoardEntity;
import com.golflearn.domain.entity.QnACommentEntity;
import com.golflearn.domain.repository.QnABoardRepository;
import com.golflearn.domain.repository.QnACommentRepository;
import com.golflearn.dto.PageBean;
import com.golflearn.dto.QnABoardDto;
import com.golflearn.dto.QnACommentDto;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;
import com.golflearn.exception.RemoveException;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class QnABoardService {
	private static final int CNT_PER_PAGE = 5; //페이지별 보여줄 목록수
	@Autowired
	private QnABoardRepository boardRepo;

	//	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
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
	 * @param currentPage 현재 페이지
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

	/**
	 * 게시글 상세보기
	 * @param boardNo 게시글 번호
	 * @return
	 * @throws FindException
	 */
	public QnABoardDto viewBoard(Long boardNo) throws FindException {
		Optional<QnABoardEntity> boardEntity = boardRepo.findById(boardNo);
		if(boardEntity.isPresent()) {
			QnACommentEntity qc =  boardEntity.get().getComment();
			QnACommentDto qd = QnACommentDto.builder()
					.commentNo(qc.getCommentNo())
					.qnaCmtContent(qc.getQnaCmtContent())
					.qnaCmtDt(qc.getQnaCmtDt())
					.userNickname(qc.getUserNickname())
					.build();
			
			QnABoardEntity board = boardEntity.get(); 
			QnABoardDto boardDto = QnABoardDto.builder()
					.boardNo(board.getBoardNo())
					.boardTitle(board.getBoardTitle())
					.boardContent(board.getBoardContent())
					.userNickname(board.getUserNickname())
					.qnaBoardDt(board.getQnaBoardDt())
					.qnaBoardSecret(board.getQnaBoardSecret())
					.comment(qd)
					.build();
			return boardDto;
		}else {
			throw new FindException("게시글이 없습니다");
		}
	}

	/**
	 * 글을 작성한다
	 * @param qnaBoard 
	 * @throws AddException
	 */
	public void writeBoard(QnABoardDto qnaBoard) throws AddException {
		//사용자가 쓴 게시글을 Dto -> entity로 변환 시켜서 저장
		QnABoardEntity be = qnaBoard.toEntity();
		boardRepo.save(be);
	}

	/**
	 * 답변을 작성한다.
	 * @param qnaComment
	 * @throws AddException
	 */
	public void writeComment(QnACommentDto qnaComment)throws AddException {		
		QnACommentEntity ce  = qnaComment.toEntity();
		Optional<QnABoardEntity> optB = boardRepo.findById(qnaComment.getCommentNo());
		System.out.println("commentNo=" + qnaComment.getCommentNo());
		if (optB.isPresent()) {
			QnABoardEntity b = optB.get();			
			ce.setBoard(b);
			System.out.println("before saves");
			commentRepo.save(ce);
		}else {
			throw new AddException("잘못된 요청입니다.");
		}
	}

	/**
	 * 게시글을 수정한다
	 * @param boardNo
	 */
	public void modifyBoard(QnABoardDto qnaBoard) throws ModifyException {
		Optional<QnABoardEntity> optB = boardRepo.findById(qnaBoard.getBoardNo());
		if (optB.isPresent()) {
			//db에서 받아온값(optB.get)을 QnABoardEntity 타입의 qe에 담아줌 
			QnABoardEntity qe = optB.get();
			if(qe.getComment() == null && qnaBoard.getUserNickname().equals(qe.getUserNickname())) {
				qe = QnABoardEntity.builder()
						.boardNo(qe.getBoardNo())
						.boardContent(qnaBoard.getBoardContent())
						.boardTitle(qnaBoard.getBoardTitle())
						.userNickname(qe.getUserNickname())
						.qnaBoardDt(qe.getQnaBoardDt())
						.qnaBoardSecret(qnaBoard.getQnaBoardSecret())
						.build();
				boardRepo.save(qe);
			}else {
				throw new ModifyException("답변이 달린 글은 수정할 수 없습니다");
			}
		}
		//			QnABoardDto board = QnABoardDto.builder()
		//					.boardNo(qe.getBoardNo())
		//					.boardTitle(qnaBoard.getBoardTitle())
		//					.boardContent(qnaBoard.getBoardContent())
		//					.userNickname(qe.getUserNickname())
		//					.qnaBoardDt(qe.getQnaBoardDt())
		//					.qnaBoardSecret(qnaBoard.getQnaBoardSecret())
		//					.build();
		//			board.toEntity();
	}

	/**
	 * 게시글을 삭제한다
	 * @param boardNo
	 */
	//	@Transactional
	public void deleteBoard(Long boardNo) throws RemoveException {
		//DB에 findbyid로 boardNo가 있는지 찾아서 entity타입의 qd에 담는다
		Optional<QnABoardEntity> optB = boardRepo.findById(boardNo);
		if (optB.isPresent()) {
			QnABoardEntity qe = optB.get();
			System.out.println(qe);
			if(qe.getComment() != null) { //댓글이 있으면
				QnACommentEntity ce = qe.getComment();
				System.out.println(ce);
				ce.setBoard(qe);
				commentRepo.delete(ce);
			}
			
//			optB.get();
			boardRepo.deleteById(boardNo);
//			qe = QnABoardEntity .builder()
//					.boardNo(qe.getBoardNo())
//					.boardContent(qe.getBoardContent())
//					.boardTitle(qe.getBoardContent())
//					.qnaBoardDt(qe.getQnaBoardDt())
//					.userNickname(qe.getUserNickname())
//					.qnaBoardSecret(qe.getQnaBoardSecret())
//					.comment(qe.getComment())
//					.build();
		}else {
			throw new RemoveException("글이 없습니다");
		}
	}
	
	/**
	 * 답변을 삭제한다(관리자만 가능)
	 * @param commentNo
	 * @throws RemoveException 
	 */
	public void deleteComment(Long commentNo) throws RemoveException {
		Optional<QnACommentEntity> optB = commentRepo.findById(commentNo);
		if (optB.isPresent()) {
			commentRepo.deleteById(commentNo);
		}else {
			throw new RemoveException("답변을 삭제할 수 없습니다");
		}
	}

}




