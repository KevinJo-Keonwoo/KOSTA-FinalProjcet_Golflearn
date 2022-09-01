package com.golflearn.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.golflearn.domain.UserInfoRepository;
import com.golflearn.dto.ProInfo;
import com.golflearn.dto.UserInfo;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;

@SpringBootTest
class UserInfoRepositoryTest {
	
	@Autowired
	private UserInfoRepository repository;
	
	@Test
	public void testInsertStdt() throws AddException{
		java.util.Date utilDate = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		UserInfo user = new UserInfo();
		user.setUserId("mr.gan@naver.com");
		user.setUserName("가나다라");
		user.setUserNickname("마바사");
		user.setUserPwd("1234");
		user.setUserPhone("010-2111-2222");
		user.setUserJoinDt(sqlDate);
		repository.insertStdt(user);		
	}
	
	
	@Test
	public void testInsertPro() throws AddException{
		java.util.Date utilDate = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		UserInfo user = new UserInfo();
		ProInfo pro = new ProInfo();
		user.setUserId("pro.han@naver.com");
		user.setUserName("한겨레");
		user.setUserNickname("프로한");
		user.setUserPwd("12345");
		user.setUserPhone("010-2222-4444");
		user.setUserJoinDt(sqlDate);
		pro.setUserId("pro.han@naver.com");
		pro.setProCareer("20년차 핵매운맛 프로입니다.");
		repository.insertPro(user, pro);

	}
	
	@Test
	public void testSelectByUserId() throws FindException{
		String userId = "hanmr@nate.com";
		String expectedUserName = "한미래";
		UserInfo ui = repository.selectByUserId(userId);
				
//		assertEquals(userId, ui.getUserId());
		assertEquals(expectedUserName, ui.getUserName());
		
	}
	
	@Test
	public void testSelectByUserNickname() throws FindException {
		String userNickname ="땡초";
		UserInfo ui = repository.selectByUserNickName(userNickname);
		assertEquals(userNickname, ui.getUserNickname());
	}
	
	
	@Test
	public void testSelectByUserIdAndPwd() throws FindException {
		String userNicakName = "프로한";
		String expectedUserId = "pro.han@naver.com";
		String expectedUserPwd = "12345";
		UserInfo ui = repository.selectByUserIdAndPwd(expectedUserId, expectedUserPwd);
		
		assertEquals(expectedUserId, ui.getUserId());
		assertEquals(expectedUserPwd, ui.getUserPwd());
	}
	
	@Test
	void testSelectByUserNameAndPhone() throws FindException {
		String userName = "전승현";
		String userPhone = "010-4465-9015";
		
		String expectedUserId = "zzeonsh@gmail.com";
		UserInfo userInfo = repository.selectByUserNameAndPhone(userName, userPhone);
		assertNotNull(userInfo);
		assertEquals(expectedUserId, userInfo.getUserId() );
	}
	
	@Test
	void testselectByUserIdAndPhone() throws FindException {
		String userId = "zzeonsh@gmail.com";
		String userPhone = "010-7186-5611";
		
		UserInfo userInfo = repository.selectByUserIdAndPhone(userId, userPhone);
		
		String expectedUserPhone = "010-7186-5611";
		assertEquals(expectedUserPhone, userInfo.getUserPhone());
	}
	
	@Test
	void testupdateByUserPwd() throws ModifyException {
		UserInfo userInfo = new UserInfo();
		String userId = "zzeonsh@gmail.com";
		String userPwd = "1234";
		
		userInfo.setUserPwd(userPwd);
		
		repository.updateByUserPwd(userId, userPwd);
		
		String expectedUserPwd = "1234";

		assertEquals(expectedUserPwd, userInfo.getUserPwd());
	}
}
