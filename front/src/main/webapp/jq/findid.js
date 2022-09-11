$(function () {
	//이름 입력 객체 찾기
	let $inputName = $("input[name=user_name]");
	//이메일 입력객체 찾기
	let $inputPhone = $("input[name=user_phone]");
	//form 전송 START
	let $form = $("form");

	$form.submit(function (event) {
		let url = "http://localhost:1124/back/user/find/id";
		let inputNameValue, inputPhoneValue;
		
		inputNameValue = $inputName.val();
		inputPhoneValue = $inputPhone.val();
		
		console.log(inputNameValue, inputPhoneValue);
		//let data = {userName : "inputNameValue", userPhone : "inputPhoneValue"};
		//"user_name=" + inputNameValue + "&user_phone=" + inputPhoneValue;
		//JSON.stringify({userName : 'inputNameValue', userPhone : 'inputPhoneValue'});
		//"user_name=" + inputNameValue + "&user_phone=" + inputPhoneValue
		//console.log(data);

		
		$.ajax({
			url: url,
			method: "POST",
			data:{
				userName : inputNameValue,		
				userPhone : inputPhoneValue
				},
			//dataType: "json",
			success: function (data) {
				if (data.status == 1) {
					$('#content.modal-body').text("고객님의 Golflearn 계정 ID는 " + data.t.userId + " 입니다.");
					$('button#btn-secondary').click(function () {
						console.log(data.t.userId);
						location.href = "http://localhost:1123/front/html/login.html";
					});
				}else{
					$('#content.modal-body').text("아이디나 핸드폰번호가 알맞지 않습니다. 아이디와 번호를 정확하게 입력해주세요");
					$('button#btn-secondary').click(function () {
						location.href = "http://localhost:1123/front/html/findid.html";
					});
				}
			},	
			error: function (jqXHR, textStatus, errorThrown) {
				alert(jqXHR.status + ": " + jqXHR.statusText);
		},
	});
	event.preventDefault();
	return false;
	});
});