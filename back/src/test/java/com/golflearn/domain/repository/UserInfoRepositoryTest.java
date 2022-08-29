package com.golflearn.domain.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.golflearn.dto.UserInfo;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;

@SpringBootTest
class UserInfoRepositoryTest {
	
	@Autowired
	private UserInfoRepository repository;
	
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
		String userPhone = "010-4465-9015";
		
		String expectedUserPhone = "010-4465-9015";
		
		UserInfo userInfo = repository.selectByUserIdAndPhone(userId, userPhone);
		assertNotNull(userInfo);
		assertEquals(expectedUserPhone, userInfo.getUserPhone());
	}
	
	@Test
	void testupdateByUserPwd() throws ModifyException {
		String userId = "zzeonsh@gmail.com";
		String userPwd = "1234";
		
		String expectedUserPwd = "1234";
		
		UserInfo userInfo = repository.updateByUserPwd(userId, userPwd);
		assertNotNull(userInfo);
		assertEquals(expectedUserPwd, userInfo.getUserPwd());
	}

}
