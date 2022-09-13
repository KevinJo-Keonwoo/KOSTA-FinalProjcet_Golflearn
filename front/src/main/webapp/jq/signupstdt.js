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
			} //error
		}); //ajax
		return false; //
	}); // 중복확인 클릭

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
		});// ajax
		return false;
	});	// 닉네임 중복확인 클릭
	
	
	
	// 유효성 검사
	
	// 가입버튼 입력 객체 찾기
	let $btSignup = $("button[name=signup]");
	
	$btSignup.click(function () {
		// function checkz(){
			// 입력 객체 찾기
			let $inputPwd = $("input[name=userPwd]");
			let $inputPwdChk = $("input[name=user_pwd_chk]");
			let $inputName = $("input[name=userName]");
			let $inputPhone = $("input[name=userPhone]");
			// 유효성 검사 
			// ----- 필수 작성 검사 START -----
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
			// ----- 필수 작성 검사 END -----
		
			// ----- 형식 검사 START -----
			// let idCheck = RegExp(/^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$/);
			// let pwdCheck = RegExp(/^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{4,20}$/);
			// let nameCheck = RegExp(/^[A-Za-z가-힣]{2,7}$/);
			// let nicknameCheck = RegExp(/^[A-Za-z가-힣0-9]{2,7}$/);		
			// let phoneCheck = RegExp(/^[0-9]{3}-[0-9]{4}-[0-9]{4}$/);
		
			// if(!idCheck.test($inputId.val())){
			// 	alert("이메일 형식에 맞추어 입력 해 주세요");
			// 	$inputId.focus();
			// 	return false;
			// }else if(!pwdCheck.test($inputPwd.val())){
			// 	alert("비밀번호는 4~10자 영문 대소문자, 숫자, 특수문자를 사용하세요");
			// 	$inputPwd.focus();
			// 	return false;
			// }else if(!nameCheck.test($inputName.val())){
			// 	alert("이름은 영문, 한글 형식입니다");
			// 	$inputName.focus();
			// 	return false;			
			// }else if(!nicknameCheck.test($inputNickname.val())){
			// 	alert("닉네임은 영문 대소문자, 숫자, 한글 사용이 가능합니다");
			// 	$inputNickname.focus();
			// 	return false;			
			// }else if (!phoneCheck.test($inputPhone.val())) {
			// 	alert("휴대폰번호 형식에 맞추어 입력 해 주세요. - 포함하여 작성 해 주세요.");
			// 	$inputPhone.focus();
			// 	return false;
			// }
			//----- 형식 검사 END -----
	
			// ----- 비밀번호 중복확인 START -----
			// let $inputPwd = $("input[name=user_pwd]");
			// let $inputPwdChk = $("input[name=user_pwd_chk]");
			// console.log($inputPwd.val());
			// console.log($inputPwdChk.val());
			if ($inputPwd.val() != $inputPwdChk.val()) {
				alert("비밀번호가 일치하지 않습니다");
				$inputPwd.focus();
				return false;
			}
			// ----- 비밀번호 중복확인 END -----
		// return false;	
		// }
		// 데이터를 한번에 보낼 form 객체 생성
		let $formObj = $("form.signupstdt");
		let formData = new FormData($formObj[0]);
		// console.log("form데이터는 " + formData);
		// console.log
		let obj = {};
		formData.append("key", JSON.stringify(obj));
		formData.forEach(function (value, key) {
			console.log(key + ":" + value);
		});
		console.log(JSON.stringify(obj));
		// ("---------------------------------");
		let obj2 = formData.get("profileImg");
		console.log(obj2);
		$.ajax({
			url: "http://localhost:1124/back/user/signupstdt",
			type: "post", //
			processData: false, // processData false로 항상 설정 해 주어야 함
			contentType: false,
			data: formData,
			success: function() {
				alert("가입 성공");
				location.replace("http://localhost:1123/front/html/login.html");
			},
			error: function (jqXHR) {
				alert(jqXHR.status + ":  가입실패");
			},
		});
		return false;
	}); // signup 버튼 클릭
}); // 맨 위 function
