package com.golflearn.domain;

import java.util.List;

import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonLine;
import com.golflearn.dto.UserInfo;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;

public interface AdminRepository {
	/**
	 * 레슨상태별 레슨 목록을 페이징처리하여 최신순으로 보여준다.
	 * @param lsnStatus 레슨상태
	 * @param startRow 시작행
	 * @param endRow 끝행
	 * @return 
	 * @throws FindException
	 */
	List <Lesson> selectByLsnStatus(int lsnStatus, int currentPage, int cntPerPage) throws FindException;
	
	/**
	 * 선택된 레슨을 승인 또는 반려한다 : 승인-1, 반려-3 lsnRjtReason 입력
	 * @param lsnNo 레슨번호
	 * @param lsnRjtReason 반려사유
	 * @throws ModifyException
	 */
	void modifyLsnStatus(Lesson lesson) throws ModifyException;
	
	/**
	 * 반려사유를 불러온다
	 * @param lsnNo 레슨번호
	 * @return 반려사유
	 * @throws FindException
	 */
	public String selectLsnRjtReason(int lsnNo) throws FindException;
	
	/**
	 * 특정 승인상태인 레슨 행 수를 불러온다
	 * @param lsnStatus 승인상태
	 * @return 
	 * @throws FindException
	 */
	int selectCntByLsnStatus(int lsnStatus) throws FindException;
	
	/**
	 * 모든 유저들의 정보를 검색해서 리스트로 반환한다
	 * @param startRow
	 * @param endRow
	 * @return
	 * @throws FindException
	 */
	public List<UserInfo> selectByUserPage(int currentPage, int cntPerPage)throws FindException;
	/**
	 * 페이징 처리
	 * 전체 회원의 행 수를 검색한다
	 * @return
	 * @throws FindException
	 */
	int selectCountAllUser() throws FindException;
	
	/**
	 * 유저들의 타입을 검색해서 리스트로 반환한다
	 * @param userType
	 * @return
	 * @throws FindException
	 */
	public List<UserInfo> selectByUserType(int currentPage, int cntPerPage, int userType) throws FindException;
	/**
	 * 유저 타입별 행 수를 검색한다
	 * @param userType
	 * @return
	 * @throws FindException
	 */
	int selectCountUserType(int userType) throws FindException;
	
	
	/**
	 * 유저의 상세정보를 검색해서 반환한다
	 * @param userId
	 * @return
	 * @throws FindException
	 */
	public UserInfo selectUserDetail(String userId) throws FindException;
	
	/**
	 * 수강생의 레슨정보를 검색해서 반환한다
	 * @param userId
	 * @return
	 * @throws FindException
	 */
	public List<LessonLine> selectByStdtLesson (String userId) throws FindException;
	/**
	 * 수강생의 레슨정보의 행 수를 검색한다
	 * @param userId
	 * @return
	 * @throws FindException
	 */
	int selectCountStdtLesson(String userId) throws FindException;
	
	
	/**
	 * 프로의 레슨 정보를 검색해서 반환한다
	 * @param userId
	 * @return
	 * @throws FindException
	 */
	public List<Lesson> selectByProLesson (int currentPage, int cntPerPage, String userId) throws FindException;
	/**
	 * 프로의 레슨 정보의 행 수를 검색한다.
	 * @param userId
	 * @return
	 * @throws FindException
	 */
	int selectCountProLesson(String userId) throws FindException;
	
	
	/**
	 * 유저 레슨 정보를 검색해서 반환한다
	 * @param startRow
	 * @param EndRow
	 * @param userType
	 * @return
	 * @throws FindException
	 */
	public List<LessonLine> selectUserLesson (int currentPage, int cntPerPage, int userType, String userId) throws FindException;
	
	
	/**
	 * 검색어로 회원을 검색해서 반환한다
	 * @param startRow
	 * @param endRow
	 * @param word
	 * @return
	 * @throws FindException
	 */
	public List<UserInfo> selectByUserSearching(int currentPage, int cntPerPage, String word) throws FindException;
	
	int selectCountSearchUser(String word) throws FindException;
}