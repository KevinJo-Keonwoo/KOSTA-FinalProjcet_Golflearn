package com.golflearn.control;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.golflearn.dto.Message;
import com.golflearn.dto.ProInfo;
import com.golflearn.dto.ResultBean;
import com.golflearn.dto.UserInfo;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;
import com.golflearn.service.SmsService;
import com.golflearn.service.UserInfoService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins="*") // 누구든 ajax로 요청할 수 있음 (다른 포트번호도O)
@RestController // @Controller + @ResponseBody
@RequestMapping("/user/*") // 각 메서드 앞에 붙여도됨
@RequiredArgsConstructor
public class UserInfoController {
	private Logger logger = Logger.getLogger(getClass());
	
	private final SmsService smsService;

	@Autowired // 빈 객체 주입받음
	private UserInfoService service;

	@Autowired 
	private ServletContext sc;


	// 프로 회원가입 (파일 업로드를 곁들인..)
	// 파일 업로드 시 formData 필요 > PathVariable 사용 불가
	// 파일 업로드 시 요청전달데이터 꼭 필요 > RestfulAPI 사용 불가
	// 파일 업로드 가능 방법 
	// 1. @RequestPart MultipartFile 타입 사용
	// 2. ServletRequest or MultipartHttpServletRequest
	@Value("${spring.servlet.multipart.location}")
	String uploadDirectory;
	@PostMapping("signuppro")
	public ResponseEntity<?> signuppro (
			@RequestPart(required = false) List<MultipartFile> certifFiles, 
			@RequestPart(required = false) MultipartFile profileImg,
			@Valid UserInfo userInfo, @Valid ProInfo proInfo, Errors error,
			HttpSession session) {

		logger.info("요청전달데이터 userNickname=" + userInfo.getUserNickname());
		logger.info("요청전달데이터 userId=" + userInfo.getUserId());
		logger.info("요청전달데이터 userName=" + userInfo.getUserName());
		logger.info("letterFiles.size()" + certifFiles.size());
		logger.info("imageFile.getsize()" + profileImg.getSize());
		logger.info("업로드한 프로필 사진명" + profileImg.getOriginalFilename());		

		//가입 입력 내용 DB에 저장
		try {
			service.signuppro(userInfo, proInfo);

		}catch (AddException e) {
			e.printStackTrace();
			return new ResponseEntity<> (HttpStatus.INTERNAL_SERVER_ERROR);
		}

		String userNickname = userInfo.getUserNickname(); // 저장될 폴더의 이름으로 사용

		// 회원가입 파일 저장 폴더 ★★★
		String uploadPath = uploadDirectory + "user_images\\" + userNickname;

		// 파일 경로 생성 (닉네임으로 폴더 생성)
		if(!new File(uploadPath).exists()) {
			logger.info("업로드 실제 경로 생성");
			new File(uploadPath).mkdirs(); // 닉네임으로 폴더 생성
			// mkdirs () : 해당 디렉토리에 상위 폴더가 없으면 폴더부터 생성 해 줌 
		}

		// Certif파일 저장
		int savedCertifFileCnt = 0;
		if(certifFiles !=null) {
			for(MultipartFile certifFile : certifFiles) {
				long certifFileSize = certifFile.getSize();
				if(certifFileSize > 0) { // 첨부 되었을 경우
					String paramName = certifFile.getName(); //파라미터로 받아올 값 
					// 파라미터 값이 user_profile이거나 user_certf인 경우
					//					if(paramName.equals("user_profile") || paramName.equals("pro_certf")) {
					// 파일 확장자 가져오기
					String originFileName = certifFile.getOriginalFilename(); // 첨부된 오리지널 파일의 이름 가지고 옴
					String fileExtension = originFileName.substring(originFileName.lastIndexOf(".")); // .으로 나누어 뒤의 확장자 가지고 옴
					logger.info("파일 확장자는 " + fileExtension);					

					//						if(paramName.equals("user_profile")) {
					// 저장 파일 이름 설정
					String CertifFileName = "Certification_" + savedCertifFileCnt +"_"+ fileExtension; //+ UUID.randomUUID() 
					File savedCertifFile = new File(uploadPath, CertifFileName); // 경로, 저장될 파일 이름
					// 부모 파일의 경로와, 그 하위의 파일명을 각각 매개변수로 지정하여 
					// 해당 경로를 조합하여 그 위치에 대한 File 객체를 생성

					//왜 이 과정이 필요한가?
					try {
						FileCopyUtils.copy(certifFile.getBytes(), savedCertifFile);
						logger.info("자격증 파일 저장 경로" + savedCertifFile.getAbsolutePath());// 파일 저장 경로 확인
					} catch (IOException e) {
						e.printStackTrace();
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
					}
					savedCertifFileCnt++;
				}
				logger.info("저장된 자격증 파일 개수" + savedCertifFileCnt);
			}
			//				}
		}
		// 프로필 파일 저장
		long profileImgSize = profileImg.getSize();
		if(profileImgSize > 0) {
			String paramName = profileImg.getName(); //파라미터로 받아올 값 
			// 파라미터 값이 user_profile이거나 user_certf인 경우

			//				if(paramName.equals("user_profile") || paramName.equals("pro_certf")) {

			// 파일 확장자 가져오기
			String originFileName = profileImg.getOriginalFilename(); // 첨부된 오리지널 파일의 이름 가지고 옴
			String fileExtension = originFileName.substring(originFileName.lastIndexOf(".")); // .으로 나누어 뒤의 확장자 가지고 옴
			logger.info("파일 확장자는 " + fileExtension);					

			//					if(paramName.equals("user_profile")) {

			// 저장 파일 이름 설정
			String profileImgName = "Profile" +fileExtension; //+ UUID.randomUUID() 
			File savedCertifFile = new File(uploadPath, profileImgName); // 경로, 저장될 파일 이름
			// 부모 파일의 경로와, 그 하위의 파일명을 각각 매개변수로 지정하여 
			// 해당 경로를 조합하여 그 위치에 대한 File 객체를 생성

			//왜 이 과정이 필요한가?
			try {
				FileCopyUtils.copy(profileImg.getBytes(), savedCertifFile);
				logger.info("프로필 저장 경로는" + savedCertifFile.getAbsolutePath());// 파일 저장 경로 확인
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		//				}
		//			} 
		//		}
		logger.error("이미지 파일 저장 완료");
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 수강생 회원가입
	//	@Value("${spring.servlet.multiple.location}")
	//	String uploadDirectory;
	@Transactional
	@PostMapping("signupstdt")
	public ResultBean<?> signupstdt (
			@RequestPart(required = false) MultipartFile profileImg,
			@Valid UserInfo userInfo, 
			Errors error) {

		ResultBean<?> rb = new ResultBean<>();

		logger.info("요청전달데이터 userNickname=" + userInfo.getUserNickname());
		logger.info("요청전달데이터 userId=" + userInfo.getUserId());
		logger.info("요청전달데이터 userName=" + userInfo.getUserName());
		logger.info("imageFile.getsize()" + profileImg.getSize());
		logger.info("업로드한 프로필 사진명" + profileImg.getOriginalFilename());		
		if(error.hasErrors()) {
			//			return new ResponseEntity<String> (error.getAllErrors().get(0).getDefaultMessage(), HttpStatus.CHECKPOINT);			
			rb.setStatus(0);
			rb.setMsg("유효성 검사 실패");
			return rb;			 
		}

		//가입 입력 내용 DB에 저장
		try {
			service.signupStdt(userInfo);
		}catch (AddException e) {
			e.printStackTrace();
			//			return new ResponseEntity<> (HttpStatus.INTERNAL_SERVER_ERROR);
			rb.setStatus(0);
			return rb;
		}

		String userNickname = userInfo.getUserNickname(); // 저장될 폴더의 이름으로 사용

		// 회원가입 파일 저장 폴더 ★★★
		// 회원가입 파일 저장 폴더 ★★★
		String uploadPath = uploadDirectory + "user_images\\" + userNickname;

		// 파일 경로 생성 (닉네임으로 폴더 생성)
		if(!new File(uploadPath).exists()) {
			logger.info("업로드 실제 경로 생성");
			new File(uploadPath + userNickname).mkdirs(); // 닉네임으로 폴더 생성
			// mkdirs () : 해당 디렉토리에 상위 폴더가 없으면 폴더부터 생성 해 줌 
		}
		// 프로필 파일 저장
		long profileImgSize = profileImg.getSize();
		if(profileImgSize > 0) {
			String paramName = profileImg.getName(); //파라미터로 받아올 값 
			// 파라미터 값이 user_profile이거나 user_certf인 경우

			//			if(paramName.equals("user_profile") || paramName.equals("pro_certf")) {

			// 파일 확장자 가져오기
			String originFileName = profileImg.getOriginalFilename(); // 첨부된 오리지널 파일의 이름 가지고 옴
			String fileExtension = originFileName.substring(originFileName.lastIndexOf(".")); // .으로 나누어 뒤의 확장자 가지고 옴
			logger.info("파일 확장자는 " + fileExtension);					

			//				if(paramName.equals("user_profile")) {

			// 저장 파일 이름 설정
			String profileImgName = "Profile" +fileExtension; //+ UUID.randomUUID() 
			File savedCertifFile = new File(uploadPath, profileImgName); // 경로, 저장될 파일 이름
			// 부모 파일의 경로와, 그 하위의 파일명을 각각 매개변수로 지정하여 
			// 해당 경로를 조합하여 그 위치에 대한 File 객체를 생성

			//왜 이 과정이 필요한가?
			try {
				FileCopyUtils.copy(profileImg.getBytes(), savedCertifFile);
				logger.info("프로필 저장 경로는" + savedCertifFile.getAbsolutePath());// 파일 저장 경로 확인
			} catch (IOException e) {
				e.printStackTrace();
				//						rb.setStatus(0);
				//						return rb;
			}
		}
		//			}
		//		} 
		//		return new ResponseEntity<>(HttpStatus.OK);
		rb.setStatus(1);
		return rb;
	}


	// 로그인
	@PostMapping(value="login")
	private ResultBean<UserInfo> login(HttpSession session, @RequestParam String userId, @RequestParam String userPwd, String userNickname, String userType) {

		ResultBean<UserInfo> rb = new ResultBean<>();

		rb.setStatus(0);
		rb.setMsg("로그아웃 상태");
		session.removeAttribute("loginInfo");
		session.removeAttribute("loginNickname");
		session.removeAttribute("userType");

		try {
			service.login(userId, userPwd);
			rb.setStatus(1);
			rb.setMsg("로그인 성공");
			session.setAttribute("loginInfo", userId);
			session.setAttribute("loginNickname", userNickname);
			session.setAttribute("userType", userType);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setMsg("로그인 실패. 아이디 비밀번호를 확인 해 주세요");
		}
		return rb;
	}

	// 로그인상태
	@GetMapping(value = "loginstatus")
	public ResultBean<UserInfo> loginstatus (HttpSession session) {

		String loginedId = (String)session.getAttribute("loginInfo");
		ResultBean<UserInfo> rb = new ResultBean<>();

		if(loginedId == null) {
			rb.setStatus(0);
			rb.setMsg("로그아웃 상태");
		}else {
			rb.setStatus(1);
			rb.setMsg("로그인 상태");
		}
		return rb;

	}

	// 로그아웃
	@GetMapping(value = "logout")
	private String logout(HttpSession session) {
		session.removeAttribute("loginInfo");
		session.removeAttribute("loginNickname");
		session.removeAttribute("userType");
		String result = ("로그아웃 되었습니다");
		System.out.println(result);

		return null;
	}

	// 아이디 중복확인
	@GetMapping(value = "iddupchk")
	public ResultBean<UserInfo> iddupchk (@RequestParam String userId) {

		ResultBean<UserInfo> rb = new ResultBean<>();
		rb.setStatus(1);
		rb.setMsg("사용 가능한 아이디입니다");

		try {
			service.iddupchk(userId);
			rb.setStatus(0);
			rb.setMsg("이미 사용중인 아이디입니다");
		} catch (FindException e) {
			e.printStackTrace();
		}
		return rb;
	}

	// 닉네임 중복확인
	@GetMapping(value="nicknamedupchk")
	public ResultBean<UserInfo> nicknamedupchk(@RequestParam String userNickname) {

		ResultBean<UserInfo> rb = new ResultBean<>();
		rb.setStatus(1);
		rb.setMsg("사용 가능한 닉네임입니다");

		try {
			service.nicknamedupchk(userNickname);
			rb.setStatus(0);
			rb.setMsg("이미 사용중인 닉네임입니다");
		} catch (FindException e) {
			e.printStackTrace();
		}
		return rb;
	}
	
	//아이디 찾기
	@PostMapping(value="find/id", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultBean <UserInfo> selectByUserNameAndPhone(@RequestParam String userName, @RequestParam String userPhone) throws FindException {
		ResultBean<UserInfo> rb = new ResultBean<>();
		UserInfo userInfo = new UserInfo();
		try {
			userInfo = service.selectByUserNameAndPhone(userName, userPhone);
			rb.setStatus(1);
			rb.setT(userInfo);
		}catch(FindException e) {
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}	

	//비밀번호 변경 인증 문자 발송
	@PostMapping(value="find/pwd", produces = MediaType.APPLICATION_JSON_VALUE)
	//Requestparam으로 userId와 userPhone값을 받아옴
	public ResultBean <UserInfo> selectByUserIdAndPhone(HttpSession session, @RequestParam String userId, @RequestParam String userPhone) throws FindException, JsonProcessingException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException {
		ResultBean<UserInfo> rb = new ResultBean<>();
		UserInfo userInfo = new UserInfo();
		try {
			//받아올 값을 담아줄 UserInfo 타입의 userInfo 객체를 생성한다
			//userId와 userPhone를 매개변수를 가진 UserInfoService에 있는 selectByIdAndPhone 메서드를 호출한다.(서비스호출)
			userInfo = service.selectByUserIdAndPhone(userId, userPhone);
			//Message타입의 객체 생성해줌 => 조회해온 폰번호와 발생시킨 난수 set 시켜주기 위해
			Message message = new Message();
			
			//받아온 전화번호 값에서 "-" 제거해줘야함
			String str = userInfo.getUserPhone();
			String userNum = str.replaceAll("-","");
			System.out.println(userNum);
			message.setTo(userNum);
			
			//난수 발생
			Random rnd = new Random();
			String randomKey = "";
			for (int i = 0; i < 5; i++) {
				//0~9 사이의 숫자들로 난수 발생시킴 => 5번 반복 & 숫자를 문자열로 바꿈
				String ran = Integer.toString(rnd.nextInt(10));
				//randomKey에 발생시킨 난수들이 계속 붙음
				randomKey += ran;
			}
			System.out.println(randomKey);
			
			message.setContent("GolfLearn 인증번호["+randomKey+"]를 입력해주세요." );
			//smsService에 있는 sendSms 메서드 호출(message를 매개변수로 함-> set한 정보 넘어감)
			smsService.sendSms(message);
			
			rb.setStatus(1);
			
			//세션에 userId와 randomKey 저장
			session.setAttribute("userId", userId);
			session.setAttribute("authenticationKey", randomKey);
		}catch(FindException e) {
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}	
	@PostMapping(value="change/pwd", produces = MediaType.APPLICATION_JSON_VALUE) 
	public ResultBean <?> updateByUserPwd(HttpSession session, @RequestParam String authenticationUser, @RequestParam String newPwd, @RequestParam String chkNewPwd) throws FindException {
		ResultBean<?> rb = new ResultBean<>();
		Message message = new Message();
		//세션에 저장되어있는 Id와 인증번호 가져옴
		String userId = (String)session.getAttribute("userId");
		String authenticationKey = (String)session.getAttribute("authenticationKey");
//		//userInfo의 userId에 가져온 세션값 저장
//		UserInfo userInfo = new UserInfo();
//		userInfo.setUserId((String)session.getAttribute("userId"));
		if (!authenticationUser.equals(authenticationKey)) {
			rb.setStatus(0);
			rb.setMsg("인증번호가 일치하지 않습니다.");
			System.out.println(authenticationKey);
			System.out.println(authenticationUser);
		}else {
			if(newPwd.equals(chkNewPwd)) {
				try {
					service.updateByUserPwd(userId, newPwd);
					rb.setStatus(1);
					rb.setMsg("비밀번호가 변경되었습니다");
				} catch (ModifyException e) {
					e.printStackTrace();
				}
			}else {
				rb.setStatus(0);
				rb.setMsg("비밀번호가 일치하지 않습니다");
			}
		}
		return rb;
	}

}