$(function () {
	//아이디 입력 객체 찾기
	let $inputId = $("input[name=userId]");

	// ----- 아이디 중복확인 버튼 클릭 START -----
	let $btIddupchk = $("button[name=iddupchk]");
	$btIddupchk.click(function () {
		if($inputId.val()==''){
			alert('아이디를 입력하세요');
			return false;
		}
		$.ajax({
			url: "http://localhost:1124/back/user/iddupchk",
			type: "get",
			data: {userId: $inputId.val()},
			success: function (jsonObj) {
				if (jsonObj.status == 1) {
					alert(jsonObj.msg);
				} else {
					alert(jsonObj.msg);
				}
			},
			error: function (jqXHR) {
				alert(jqXHR.status + ":" + jqXHR.statusText);
			}
		});
		return false;
	});
  	// ----- 아이디 중복확인 버튼 클릭 END -----

	// 닉네임 입력 객체
	let $inputNickname = $("input[name=userNickname]");
	// ----- 닉네임 중복확인 버튼 클릭 START -----
	let $btNicknamedupchk = $("button[name=nicknamedupchk]");

	$btNicknamedupchk.click(function () {
		if($inputNickname.val()==''){
			alert('닉네임을 입력하세요');
			return false;
		}
		$.ajax({
			url: "http://localhost:1124/back/user/nicknamedupchk",
			type:"get",
			data:{userNickname: $inputNickname.val()},
			success: function(jsonObj){
				if (jsonObj.status == 1) {
					alert(jsonObj.msg);
				} else {
					alert(jsonObj.msg);
				}
			},
			error:function (jqXHR) {
				alert(jqXHR.status + ":" + jqXHR.statusText);
			}
		});
		return false;
	});	
	
  // 데이터를 한번에 보낼 form 객체 생성

	// 입력 객체 찾기
	let $inputPwd = $("input[name=userPwd]");
	let $inputPwdChk = $("input[name=user_pwd_chk]");
	let $inputName = $("input[name=userName]");
	let $inputPhone = $("input[name=userPhone]");
	let $inputCareer = $("input[name=proCareer]");

  	// 가입버튼 입력 객체 찾기
	let $btSignup = $("button[name=signup]");
	
	// 가입하기 버튼 클릭 이벤트 발생
	$btSignup.click(function () {
		if($inputId.val()==''){
			alert('아이디를 입력하세요');
			return false;
		}
		else if($inputNickname.val()==''){
			alert('닉네임을 입력하세요');
			return false;
		}else if($inputPwd.val() == ''){
			alert('비밀번호를 입력하세요');
			$inputPwd.focus();
			return false;
		}else if($inputPwdChk.val() == ''){
			alert('비밀번호 확인을 입력하세요')
			$inputPwdChk.focus();
			return false;
		}else if ($inputName.val() == '') {
			alert('이름을 입력하세요');
			$inputName.focus();
			return false;
		}else if ($inputPhone.val() == '') {
			alert('전화번호를 입력하세요');
			$inputPhone.focus();
			return false;
		}

		// let idCheck = RegExp(/^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$/);
		// let pwdCheck = RegExp(/^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{4,20}$/);
		// let nameCheck = RegExp(/^[A-Za-z가-힣]{2,7}$/);
		// let nicknameCheck = RegExp(/^[A-Za-z가-힣0-9]{2,7}$/);
		// let phoneCheck = RegExp(/^[0-9]{3}-[0-9]{4}-[0-9]{4}$/);
		

		// ----- 비밀번호 확인 START -----
		// let $inputPwd = $("input[name=user_pwd]");
		// let $inputPwdChk = $("input[name=user_pwd_chk]");
		if ($inputPwd.val() != $inputPwdChk.val()) {
			alert("비밀번호가 일치하지 않습니다");
			$inputPwd.focus();
			return false;
		}
    	// ----- 비밀번호 확인 END -----

		// 데이터 전송을 위한 폼 객체 불러옴
		let $formObj = $("form.signuppro");
		// 불러온 폼 객체를 FormData parameter에 담아줌
		//[0]은 첫번째 form을 의미함
		let formData = new FormData($formObj[0]);
		// formObj[1][0] - 두번째 form의 첫번째 요소를 의미
		// console.log(formData);
		
		let obj = {};
		formData.append("key", JSON.stringify(obj));
		formData.forEach(function (value, key) {
			console.log(key + ":" + value);
		});
		
		console.log(JSON.stringify(obj));
		// ("---------------------------------");
		let obj2 = formData.get("profileImg");
		let obj3 = formData.get("certifFiles");
		
		$.ajax({
			url: "http://localhost:1124/back/user/signuppro",
			type: "post",
			processData: false,
			contentType: false,
			data: formData,
			success: function() {
				alert("가입 성공"); // jsonObj.msg로 작성 시 오류
				location.replace="http://localhost:1123/front/html/login.html";
			},
			error: function (jqXHR) {
				alert(jqXHR.status + ":  가입실패");
				console.log(formData.userId);
				console.log(formData.userNickname);
			},
		});
		return false;
	});
});
// processData false로 항상 설정 해 주어야 함
// 일반적으로 서버에 전달되는 데이터는 query string 형태
// data 파라미터로 전달된 데이터를 jQuery 내부적으로 query string 형태로 만드는 데, 파일 전송의 경우 이를 하지 않아야하므로 processData를 false로 설정
//contentType 은 default 값이 "application/x-www-form-urlencoded; charset=UTF-8" 인데, "multipart/form-data" 로 전송이 되게 false 로 넣는 것