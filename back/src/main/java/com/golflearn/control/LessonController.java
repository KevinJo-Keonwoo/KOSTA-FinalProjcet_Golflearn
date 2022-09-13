package com.golflearn.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonLine;
import com.golflearn.dto.ResultBean;
import com.golflearn.dto.UserInfo;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.service.LessonService;

import net.coobird.thumbnailator.Thumbnailator;

@CrossOrigin(origins = "*")//모든포트에서 접속가능 + 메서드마다 각각 설정도 가능
@RestController
@RequestMapping("lesson/*") //합의 필요
public class LessonController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private LessonService service;

	@Autowired
	private ServletContext sc;//파일path설정 시 필요
	
	@GetMapping("{lsnNo}")
	public ResultBean<Lesson> viewLessonDetail(@PathVariable int lsnNo) {
		ResultBean<Lesson> rb = new ResultBean<>();
		try {
			Lesson l = service.viewLessonDetail(lsnNo);
			rb.setStatus(1); //성공시 satus : 1
			rb.setT(l); //lesson객체 담기
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0); //성공 실패시 satus : 0
			rb.setMsg(e.getMessage());
		}
		return rb;
	}
  
  @GetMapping(value= {""})
	public ResultBean<Lesson> list(@PathVariable Optional<Integer> optCp) { //로그인 유무와 상관없이 다 볼수 있기때문에 httpSession 필요없음
		ResultBean<Lesson> rb = new ResultBean<>();
		try {
			List<Lesson> lessons = service.viewMain();
			rb.setStatus(1);
			rb.setLt(lessons);
			return rb;
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(-1);
			rb.setMsg(e.getMessage());
			return rb;
		}
	}

	@GetMapping(value = {"lesson", "lesson/{optCp}"}) // 프로의 레슨내역에서 레슨번호에 대한 히스토리
	public ResultBean<LessonLine> viewHistory(@PathVariable int optCp, HttpSession session) {
		ResultBean<LessonLine> rb = new ResultBean<>();
		// 로그인 여부를 받아와야한다 HttpSession?
		String loginedId = (String)session.getAttribute("loginInfo");
		if(loginedId == null) {
			rb.setStatus(0);
			rb.setMsg("로그인하세요");
			return rb;
		}else {
			try {
				List<LessonLine> lsnHistories = service.viewLessonHistory(optCp);
				rb.setStatus(1);
				rb.setLt(lsnHistories);
				return rb;
			} catch (FindException e) {				
				e.printStackTrace();
				rb.setStatus(-1);
				rb.setMsg(e.getMessage());
				return rb;
			}
		}
	}
	
	//restcontroller가 아님
	@Value("${spring.servlet.multipart.location}")
	String saveDirectory;//파일경로생성
//	String saveDirectory = sc.getInitParameter("filePath");
	@PostMapping("request")//합의필요
	public ResponseEntity<?> reuqestLesson(// ResponseEntity 응답성공실패
			@RequestPart(required = false) List<MultipartFile> lessonFiles// MultipartFile타입을 사용하려면 @RequestPart필요
			, @RequestPart(required = false) MultipartFile imageFile, Lesson lesson,
			HttpSession session) {
		
		try {//입력한 레슨승인요청 정보 DB에 저장
			
			//로그인된 아이디가 프로인지 여기서 설정? 프론트에서 레슨등록버튼 자체가 안보이니까 상관X?
			
//			 String loginedId = (String)session.getAttribute("loginInfo");
			
			//---로그인 회원 샘플데이터--------------------
			String loginedId = "ohpro@gmail.com";
			//----------------------------------------
			UserInfo userinfo = new UserInfo();
			userinfo.setUserId(loginedId);
			lesson.setUserInfo(userinfo);//has-a관계로 묶인 dto, 어떻게 저장시키지 
			
			service.addLesson(lesson);//레슨승인요청서비스 호출
			
		} catch (AddException e1) {
			e1.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		String path = "lesson\\";
		
		String lessonPath = saveDirectory + path;
				
		if (!new File(lessonPath).exists()) {
			logger.info("업로드 실제경로생성");
			new File(lessonPath).mkdirs();
		}

		long wroteLsnNo = lesson.getLsnNo();// 저장된 글의 레슨번호

		// lessonFiles 저장
		int savedlessonFileCnt = 0;// 서버에 저장된 파일수
		if (lessonFiles != null) {
			for (MultipartFile lessonFile : lessonFiles) {
				long letterFileSize = lessonFile .getSize();
				if (letterFileSize > 0) {// file크기가 0보다 클 경우(=파일이 잘 업로드된 경우)에만 저장
					String lessonOriginFileName = lessonFile .getOriginalFilename(); // 파일원본이름얻기
					// 레슨 파일들 저장하기
					logger.info("파일이름: " + lessonOriginFileName + " 파일크기: " + lessonFile.getSize());
					// 저장할 파일이름을 지정한다 ex) 레슨번호_file_XXXX_원본이름
					String lessonfileName = wroteLsnNo + "_file_" + UUID.randomUUID() + "_" + lessonOriginFileName;
					File savevdLessonFile = new File(lessonPath, lessonfileName);// 파일생성
					try {
						// FileCopyUtils: springframework.util.FileCopyUtils패키지 내 클래스
						FileCopyUtils.copy(lessonFile.getBytes(), savevdLessonFile);
						logger.info("레슨승인요청 파일저장:" + savevdLessonFile.getAbsolutePath());

					} catch (IOException e) {
						e.printStackTrace();
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
					}
					savedlessonFileCnt++;
				} 
			}
		} 
		logger.info("저장된 파일개수: " + savedlessonFileCnt);

		File thumbnailFile = null;
		
		long imageFileSize = imageFile.getSize();//getSize() => 0 if empty
		
//		int imageFileCnt = 0;// 서버에 저장된 이미지파일수
		
		if (imageFileSize > 0) {
			// 이미지파일 저장하기
			String imageOrignFileName = imageFile.getOriginalFilename(); // 이미지파일원본이름얻기
			logger.info("이미지 파일이름:" + imageOrignFileName + ", 파일크기: " + imageFile.getSize());

			// 저장할 파일이름을 지정한다 ex) 레슨번호_image_XXXX_원본이름
			String imageFileName = wroteLsnNo + "_image_" + UUID.randomUUID() + "_" + imageOrignFileName;
			// 이미지파일생성
			File savedImageFile = new File(lessonPath, imageFileName);

			try {// copy메서드를 이용하여 파일 저장
				FileCopyUtils.copy(imageFile.getBytes(), savedImageFile);
				logger.info("이미지 파일저장:" + savedImageFile.getAbsolutePath());

				// 파일형식 확인
				String contentType = imageFile.getContentType();
				if (!contentType.contains("image/")) { // 이미지파일형식이 아닌 경우
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}

				// 이미지파일인 경우 섬네일파일을 만듦
				String thumbnailName = "s_" + imageFileName; // 섬네일 파일명은 s_글번호_XXXX_원본이름
				thumbnailFile = new File(lessonPath, thumbnailName);
				FileOutputStream thumbnailOS;
				thumbnailOS = new FileOutputStream(thumbnailFile);
				InputStream imageFileIS = imageFile.getInputStream();
				int width = 100;
				int height = 100;
				Thumbnailator.createThumbnail(imageFileIS, thumbnailOS, width, height);
				logger.info("섬네일파일 저장:" + thumbnailFile.getAbsolutePath() + ", 섬네일파일 크기:" + thumbnailFile.length());

				// 이미지 썸네일다운로드하기
				HttpHeaders responseHeaders = new HttpHeaders();
				responseHeaders.set(HttpHeaders.CONTENT_LENGTH, thumbnailFile.length() + "");
				responseHeaders.set(HttpHeaders.CONTENT_TYPE, Files.probeContentType(thumbnailFile.toPath()));
				responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION,
						"inline; filename=" + URLEncoder.encode("a", "UTF-8"));
				logger.info("섬네일파일 다운로드");

				// 응답헤더, 응답내용까지 한번에 응답
				return new ResponseEntity<>(FileCopyUtils.copyToByteArray(thumbnailFile), responseHeaders,
						HttpStatus.OK);

			} catch (IOException e2) {
				e2.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} // end if(imageFileSize > 0)
		else {
			logger.error("이미지파일이 없습니다");
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
}
