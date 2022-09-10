package com.golflearn.domain;

import java.util.List;

import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonLine;
import com.golflearn.dto.UserInfo;
import com.golflearn.exception.FindException;

public interface AdminRepository {
	/*
	 * 1)유저정보 전체 목록 불러오기
	 * 2)유저정보 목록 usertype별 목록 불러오기  => 프로정보 낼 물어보기
	 * 3)유저정보 상세보기
	 * 4)레슨정보(수강생, 프로별로 나누기)
	 * 5)회원 검색하기
	 */
	
	
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