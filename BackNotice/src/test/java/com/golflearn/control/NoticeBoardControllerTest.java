package com.golflearn.control;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
class NoticeBoardControllerTest {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc; // 컨트롤러 테스트용 모의객체
	
	@BeforeEach // junit4에서는 @Before을 쓰지만 junit5에서는 @BeforeEach를 쓴다.
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	@Test
	public void testBoardList() throws Exception {
ResultActions resultActions = mockMvc.perform(get("/notice/list/").accept(org.springframework.http.MediaType.APPLICATION_JSON));
		
		resultActions.andExpect(MockMvcResultMatchers.status().isOk()); //응답상태코드200번 성공 예상
		resultActions.andExpect(MockMvcResultMatchers.jsonPath("status", is(1)));
	}
	
	@Test
	public void testViewBoard() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/notice/2").accept(org.springframework.http.MediaType.APPLICATION_JSON));
		
		resultActions.andExpect(MockMvcResultMatchers.status().isOk()); //응답상태코드200번 성공 예상
		resultActions.andExpect(MockMvcResultMatchers.jsonPath("status", is(1)));
		
	}
	
	@Test
	public void testWriteBoard() throws Exception {
		ResultActions resultActions = mockMvc.perform(post("/notice/writeboard").accept(org.springframework.http.MediaType.APPLICATION_JSON));
		
		resultActions.andExpect(MockMvcResultMatchers.status().isOk()); //응답상태코드200번 성공 예상
		resultActions.andExpect(MockMvcResultMatchers.jsonPath("status", is(1)));
	}
	
}
