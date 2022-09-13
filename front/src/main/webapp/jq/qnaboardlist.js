$(function () {
	//let loginedId = "hwangcy@gmail.com"; //테스트용
	
		//function showList(pageNo){
		function showList(url, data) {
			$.ajax({
			// url: '/backboard/boardlist',
			// data: 'currentPage=' + pageNo,
				url: url,
				method: "get",
				data: data,
				success: function (jsonObj) {
					if (jsonObj.status == 1) {
						let pageBeanObj = jsonObj.t;
	
					//게시글 div를 원본으로 한다. 복제본만든다
						let $board = $("div.qnaboard").first();
					//나머지 게시글 div는 삭제한다
						$("div.qnaboard").not($board).remove();
						let $boardParent = $board.parent();
						$(pageBeanObj.list).each(function (index, board) {
							let $boardCopy = $board.clone(); //복제본
							$boardCopy.find("div.board_no").html(board.boardNo);
							$boardCopy.find("div.board_title").html(board.boardTitle);
							$boardCopy.find("div.board_dt").html(board.qnaBoardDt);
							$boardCopy.find("div.board_nickname").html(board.userNickname);
							$boardCopy.find("div.board_secret").html(board.qnaBoardSecret).hide();
							$boardCopy.find("div.arrow").addClass("down");
							$boardParent.append($boardCopy);
							if(board.qnaBoardSecret > 0) {
								$boardCopy.find("div.board_title").html("비밀글 입니다");
							}else{
								$boardCopy.find("div.board_title").html(board.boardTitle);
							}
							$boardParent.append($boardCopy);
						});

						let $pagegroup = $("div.pagegroup");
						let $pagegroupHtml = "";
						if (pageBeanObj.startPage > 1) {
							$pagegroupHtml += '<span class="prev">PREV</span>';
						}
						for (let i = pageBeanObj.startPage; i <= pageBeanObj.endPage; i++) {
							$pagegroupHtml += "&nbsp;&nbsp;";
							if (pageBeanObj.currentPage == i) {
							//현재페이지인 경우 <span>태그 안만듦
							// $pagegroupHtml += i;
							$pagegroupHtml += '<span class="disabled">' + i + "</span>";
							} else {
							$pagegroupHtml += "<span>" + i + "</span>";
							}
						}
						if (pageBeanObj.endPage < pageBeanObj.totalPage) {
							$pagegroupHtml += "&nbsp;&nbsp;";
							$pagegroupHtml += '<span class="next">NEXT</span>';
						}
			
						$pagegroup.html($pagegroupHtml);
						} else {
						alert(jsonObj.msg);
						}
					},
					error: function (jqXHR) {
						alert("에러:" + jqXHR.status);
					},
					});
				}	
	//---페이지 로드되자 마자 게시글1페이지 검색 START---
	showList("http://localhost:1127/qna/board/list");
	//showList("qna/board/list");
	//showList("/qna/board/list", "currentPage=1");
	//---페이지 로드되자 마자 게시글1페이지 검색 END---

	//---페이지 그룹의 페이지를 클릭 START---
	$("div.pagegroup").on("click", "span:not(.disabled)", function () {
		let pageNo = 1;
        if ($(this).hasClass("prev")) {
			pageNo = parseInt($(this).next().html()) - 1;
        } else if ($(this).hasClass("next")) {
			pageNo = parseInt($(this).prev().html()) + 1;
        } else {
			pageNo = parseInt($(this).html());
        }
		let word = $("div.search>div.searchInput>input[name=word]").val().trim();
		let url = "";
		let data = "";
		if (word == "") {
			url = "http://localhost:1127/qna/board/list/" + pageNo;
			data = "currentPage=" + pageNo;
		}else {
			url = "http://localhost:1127/qna/board/search/" + word +'/'+ pageNo ;
			data = "word=" + word + "&currentPage=" + pageNo;
		}
		showList(url, data);
		return false;
		// }
	});
	//---페이지 그룹의 페이지를 클릭 END---
	//---검색 클릭 START---
	$("div.search>div.searchInput>button[name=qna-searchBtn]").click(function () {
		let word = $("div.search>div.searchInput>input[name=word]").val().trim();
		let url = "http://localhost:1127/qna/board/search/" + word;
		let data = "word=" + word;
		showList(url, data);
		return false;
		});
	//---검색 클릭 END---
	//--비밀글 제외 보기 클릭 START---
	$("button[name=publicboard]").click(function () {
		let pageNo = $("div.pagegroup").val();
		let url =  "http://localhost:1127/qna/board/openlist/"+ pageNo;
		let data = "currentPage=" + pageNo;
		if(pageNo == 1) {
			url = "http://localhost:1127/qna/board/openlist"
		}else {
			url = "http://localhost:1127/qna/board/openlist/" + pageNo ;
			data = "currentPage=" + pageNo;
		}
		showList(url, data);
		return false;
		});
	//--비밀글 제외 보기 클릭 END---


	//---제목 클릭 START---
	$("div.boardlist").on(
		"click", 
		"div.qnaboard>div.cell>div.summary>div.arrow", 
		function () {
			//loginedNickname = "땡초";
			//logineduserType="2";
			if ($(this).hasClass("down")) {
			let boardNo =  $(this).siblings("div.board_no").html();
            let $detail = $(this).parents("div.cell").find("div.detail");
			let $userNickname = $(this).siblings("div.board_nickname");
            let $boardContent = $detail.find("input.board_content");
            let $modifyNremove = $detail.find("div.modifyNremove");
			let $qnaCmtContent = $detail.find("input.comment_content");
			let $qnaCmtdt = $detail.find("div.comment-detail>div.comment_dt");
			let $commentUserNickname = $detail.find("div.comment_user_nickname");
			let $writeComment = $(this).parents("div.detail").find("div.write-comment");
			let $inputComment = $writeComment.find("div.textarea[name = input_comment]");
			let $insertNremove = $writeComment.find("div.insertNremove");
			$.ajax({
				// url: "/backboard/viewboard",
				url:'http://localhost:1127/qna/board/view/'+boardNo,
				method: 'get',
				//data: boardNo,
				success: function (jsonObj) {
					if (jsonObj.status == 1) {
						let board = jsonObj.t;
						//console.log(jsonObj.t);
						console.log(jsonObj.t.comment);
					if (loginedNickname == board.userNickname) {
							$boardContent.removeAttr("readonly");
							$boardContent.css("outline", "auto");
					} else {
						$boardContent.attr("readonly", "readonly");
						$boardContent.css("outline", "none");
						$modifyNremove.hide();
					}
					if(logineduserType == "2" ) {
						$writeComment.show();
						$inputComment.show();
						$insertNremove.show();
					}else {
						$inputComment.hide();
						$insertNremove.hide()
						$writeComment.hide();
					}
					$boardContent.val(board.boardContent);
					$qnaCmtContent.val(board.comment.qnaCmtContent);
					$qnaCmtdt.val(board.comment.qnaCmtDt).html(board.comment.qnaCmtDt);
					$userNickname.val(board.userNickname);
					$commentUserNickname.val(board.comment.userNickname).html(board.comment.userNickname);
					$detail.show();
					$modifyNremove.show();
					} else {
					alert(jsonObj.msg);
					}
				},
				error: function (jqXHR) {
					alert("에러:" + jqXHR.status);
				},
            });
            $(this).addClass("up");
            $(this).removeClass("down");
        } else if ($(this).hasClass("up")) {
            $(this).addClass("down");
            $(this).removeClass("up");
            $(this).parents("div.cell").find("div.detail").hide();
        }
	}
    );
//---arrow화살표클릭 END---

	//---수정버튼 클릭 START---
	$("div.boardlist").on(
		"click",
		"div.board div.modifyNremove>button.modify",
		function () {
		let boardNo = $(this).parents("div.cell").find("div.board_no").html();
		let boardContent = $(this)
			.parents("div.cell")
			.find("input.board_content")
			.val();
		$.ajax({
			url: "http://localhost:9997/backboardboot/board/" + boardNo,
			method: "PUT",
			timeout: 0,
			headers: {
			"Content-Type": "application/json",
			},
			data: JSON.stringify({
			boardNo: boardNo,
			boardContent: boardContent,
			}),
			success: function (jsonObj) {
			alert("수정성공");
			},
			error: function (jqXHR, textStatus) {
			alert(
				"수정 에러:" +
				jqXHR.status +
				", jqXHR.responseText:" +
				jqXHR.responseText
			);
			},
		});
		return false;
		}
	);
	//---수정버튼 클릭 END---

	//---삭제버튼 클릭 START---
	$('div.boardlist').on('click', 'div.board div.modifyNremove>button.remove', function () {
		alert("in 삭제");
		let boardNo = $(this).parents('div.cell').find('div.board_no').html()
		$.ajax({
		"url": "/backboard/board/" + boardNo,
		"method": "DELETE",
		success: function () {
			location.href = "./boardlist.html";
		}, error: function (jqXHR, textStatus) {
			alert("삭제 에러:" + jqXHR.status + ", jqXHR.responseText:" + jqXHR.responseText);
		}
		});
		return false;
	});
	//---삭제버튼 클릭 END---

	// 답글저장
	$('div.boardlist').on('click', 'div.board div.reply button', function () {
		let boardParentNo = $(this).parents('div.cell').find('div.board_no').html()
		
		let boardTitle = $(this).siblings('input[name=board_title]').val()
		let boardContent = $(this).siblings('textarea[name=board_content]').val()

		$.ajax({
			"url": "http://localhost:9997/backboardboot/board/reply/" + boardParentNo,
			"method": "POST",
			"timeout": 0,
			"headers": {
				"Content-Type": "application/json"
			},
			"data": JSON.stringify({
				"boardTitle": boardTitle,
				"boardContent": boardContent
			}),
			success: function () {
				location.href = "./boardlist.html";
			}, error: function (jqXHR, textStatus) {
				alert("답글 에러:" + jqXHR.status + ", jqXHR.responseText:" + jqXHR.responseText);
			}
		});
		return false;
		//---답글저장버튼 클릭 END---
	});
});