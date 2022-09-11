package com.golflearn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golflearn.domain.AdminRepository;
import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonLine;
import com.golflearn.dto.PageBean;
import com.golflearn.dto.UserInfo;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;

@Service(value = "adminService")
public class AdminService {
	@Autowired
	private AdminRepository repository;
	
	private static final int CNT_PER_PAGE = 5; //페이지별 보여줄 목록수

	/**
	 * 승인상태별 레슨목록을 페이징처리하여 불러온다
	 * @param lsnStatus 승인상태 
	 * @param currentPage 검색할 페이지
	 * @return
	 * @throws FindException
	 */
	public PageBean<Lesson> searchLessonByStatus(int lsnStatus, int currentPage) throws FindException{
		List<Lesson> list = repository.selectByLsnStatus(lsnStatus, currentPage, CNT_PER_PAGE);
		int totalCnt = repository.selectCntByLsnStatus(lsnStatus);//해당하는 상태의 레슨 수 불러오기
		int cntPerPageGroup = 5;
		PageBean<Lesson> pb = new PageBean<>(list, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);
		return pb;
	}
	/**
	 * 승인 또는 반려하기
	 * @param lesson 레슨
	 * @throws ModifyException
	 */
	public void modifyLsnStatus(Lesson lesson) throws ModifyException{
		repository.modifyLsnStatus(lesson);
	}
	/**
	 * 반려사유 보기
	 * @param lsnNo 레슨번호
	 * @return
	 * @throws FindException
	 */
	public String selectLsnRjtReason(int lsnNo) throws FindException{
		String lsnRjtReason = repository.selectLsnRjtReason(lsnNo);
		return lsnRjtReason;
	}	
	
	/**
	 * 페이지별 유저 목록과 페이지 그룹 정보를 반환
	 * @param currentPage 검색할 페이지
	 * @return
	 * @throws FindException
	 */
	public PageBean<UserInfo> userInfoList(int currentPage) throws FindException{

		List<UserInfo> userList = repository.selectByUserPage(currentPage, CNT_PER_PAGE);
		int totalCnt = repository.selectCountAllUser();

		int cntPerPageGroup = 5;

		PageBean<UserInfo> pu = new PageBean<>(userList, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);

		return pu;
	}
	/**
	 * 페이지별 유저 타입을 검색해 타입별 유저 목록과 페이지 그룹 정보를 반환
	 * @param currentPage
	 * @param userType
	 * @return
	 * @throws FindException
	 */
	public PageBean<UserInfo> userInfoByType(int currentPage, int userType) throws FindException {

		List<UserInfo> userTypeList = repository.selectByUserType(currentPage, CNT_PER_PAGE, userType);

		int totalCnt = repository.selectCountUserType(userType);
		int cntPerPageGroup = 5;

		PageBean<UserInfo> pu = new PageBean<>(userTypeList, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);

		return pu;

	}
	
	/**
	 * 유저 상세정보를 검색해 반환
	 * @param userId
	 * @return
	 * @throws FindException
	 */
	public UserInfo userInfoDetail(String userId) throws FindException {
		UserInfo userDetail = repository.selectUserDetail(userId);
		return userDetail;
	}

	/**
	 * 페이지별 유저 레슨 정보를 반환
	 * @param currentPage
	 * @param userType
	 * @param userId
	 * @return
	 * @throws FindException
	 */
	public PageBean<LessonLine> userLesson(int currentPage, int userType, String userId) throws FindException {
	
		int cntPerPageGroup = 5;
		int totalCnt = 0;
				
		if(userType == 0) {
			totalCnt = repository.selectCountStdtLesson(userId);
		}else if(userType == 1) {
			totalCnt = repository.selectCountProLesson(userId); 
		}
		List<LessonLine> userLessonList = repository.selectUserLesson(currentPage, currentPage, userType, userId);
		PageBean<LessonLine> pu = new PageBean<>(userLessonList, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);
		
		return pu;
	}

	/**
	 * 페이지별 검색어를 이용해 유저 정보와 페이지 그룹 정보를 반환
	 * @param word
	 * @param currentPage
	 * @return
	 * @throws FindException
	 */
	public PageBean<UserInfo> searchUser(String word, int currentPage) throws FindException {
		System.out.println(word);
		List<UserInfo> userList = repository.selectByUserSearching(currentPage, currentPage, word);
		
		int totalCnt = repository.selectCountSearchUser(word);

		int cntPerPageGroup = 5;
		PageBean<UserInfo> pu = new PageBean<>(userList, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);

		return pu;
	}
}
