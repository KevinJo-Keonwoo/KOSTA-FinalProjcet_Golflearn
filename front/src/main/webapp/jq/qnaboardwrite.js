$(function () {
	let userNickname = localStorage.getItem("loginedNickname"); 
	//--글쓰기 버튼 클릭 START--
	let $btn = $("a[name=question]");
	$btn.click(function () {
		if(userNickname == null) {
			alert("회원만 작성 가능합니다")
			location.href = "./qnaboardlist.html";
		}else {
			let $btObj = $("button[name=submit-btn]");
			$btObj.click(function () {
				let boardTitle = $("input[name=board-title]").val();
				let boardContent = $("input[name=board-content]").val();
				let boardSecret = $("input[name=board-secret]:checked").val();
			if (boardSecret != 1) {
				boardSecret = 0;
			} else {
			}

			$.ajax({
				url: "http://localhost:1127/qna/board/write",
				method: "POST",
				headers: {
				"Content-Type": "application/json",
				},
				data: JSON.stringify({
					"boardTitle": boardTitle,
					"boardContent": boardContent,
					"qnaBoardSecret": boardSecret,
					"userNickname": userNickname
				}),
				success: function (jsonObj) {
					if (jsonObj.status == 1) {
						$('div[name=second-modal-body]').text("글이 정상적으로 등록되었습니다");
						$('button[name=okbtn]').click(function () {
							location.href = "./qnaboardlist.html";
						});
					}
				},
				error: function (jqXHR, textStatus) {
					$('div[name=second-modal-body]').text("글이 정상적으로 등록되지 않았습니다");
					$('button[name=okbtn]').click(function () {
						location.href = "./qnaboardlist.html";
					});
					alert("에러:" + jqXHR.status);
				}
			});
			return false;
			});
		}
	});
});