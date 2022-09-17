$(function() {
	let $inputAuthenticationUser = $("input[name=authenticationUser]");
	let $inputNewPwd = $("input[name=user_newpwd]");
	let $inputChkNewPwd = $("input[name=user_chknewpwd]");

	let $form = $('form');
	$form.submit(function (event) {
		let userId = localStorage.getItem("userId"); 
		if(userId == null || userId == ""){
			$('#content.modal-body').html("회원정보가 없습니다");
			$("button.btn-secondary").click(function () {
				location.href = "http://localhost:1123/front/html/changepwd.html";
				localStorage.removeItem("userId");
				localStorage.removeItem("authenticationKey");
			});
			//$inputNewPwd.focus();
			return false;
		}

		if ($inputNewPwd.val() != $inputChkNewPwd.val()) {
			$('#content.modal-body').html("비밀번호가 일치하지 않습니다. 비밀번호를 확인해주세요.");
			$("button.btn-secondary").click(function () {
				localStorage.removeItem("userId");
				localStorage.removeItem("authenticationKey");
				location.href = "http://localhost:1123/front/html/changepwd.html";
			});
			$inputNewPwd.focus();
			return false;
		}

		let url = "http://localhost:1124/back/user/change/pwd";
		let inputAuthenticationUser, inputNewPwd, inputChkNewPwd;
		
		inputAuthenticationUser = $inputAuthenticationUser.val();//입력된인증코드값
		inputNewPwd = $inputNewPwd.val();
		//inputChkNewPwd = $inputChkNewPwd.val();
		console.log(inputAuthenticationUser,inputNewPwd,inputChkNewPwd);
		
		let authenticationKey = localStorage.getItem("authenticationKey");

		if(authenticationKey != inputAuthenticationUser){
			$('#content.modal-body').html("인증코드가 일치하지 않습니다. 인증코드를 정확하게 입력해주세요.");
			$('button#btn-secondary').click(function () {
				localStorage.removeItem("userId");
				localStorage.removeItem("authenticationKey");
				location.href = "http://localhost:1123/front/html/changepwd.html";
			});
		}

		$.ajax({
			url: url,
			method: "POST",
			data: {
				userId : userId,
				newPwd : inputNewPwd,
			},
			success: function(data) {
				if(data.status == 1){
					$('#content.modal-body').html("고객님의 계정 비밀번호가 변경되었습니다.");
					$('button#btn-secondary').click(function () {
						localStorage.removeItem("userId");
						localStorage.removeItem("authenticationKey");
						location.href = "http://localhost:1123/front/html/login.html";
					});		//let data = "authenticationUser=" + inputAuthUser + "&user_newpwd=" + inputNewPwd + "&user_chknewpwd=" + inputChkNewPwd;
				}else{
					$('#content.modal-body').html("비밀번호 변경에 실패했습니다");
					
					$('button#btn-secondary').click(function () {
						localStorage.removeItem("userId");
						localStorage.removeItem("authenticationKey");
						location.href = "http://localhost:1123/front/html/changepwd.html";				
					});
					
				}
			},

			error: function (jqXHR,textStatus,errorThrown) {
				$('#content.modal-body').html("오류:" + jqXHR.status);
				$('button#btn-secondary').click(function () {
	//			location.href = "http://localhost:1123/front/html/changepwd.html";
				});
			}
		});
		//event.preventDefault();
		return false;
	});
});