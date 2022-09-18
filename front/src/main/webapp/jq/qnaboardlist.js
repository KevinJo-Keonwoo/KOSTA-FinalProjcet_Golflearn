$(function () {
	let loginedNickname = localStorage.getItem("loginedNickname"); 
	let loginedUserType = localStorage.getItem("loginedUserType");

	function showList(url, data) {
		$('div.detail').hide();
		$.ajax({
		url: url,
		method: "get",
		data: data,
		success: function (jsonObj) {
			if (jsonObj.status == 1) {
			let pageBeanObj = jsonObj.t;
			console.log(jsonObj.t);
			//게시글 div를 원본으로 한다. 복제본만든다
			let $board = $("div.qnaboard").first();
			//나머지 게시글 div는 삭제한다
			$("div.qnaboard").not($board).remove();
			let $boardParent = $board.parent();
			$(pageBeanObj.list).each(function (index, board) {
				let $boardCopy = $board.clone(); //복제본
				$boardCopy.find("div.board_no").html(board.boardNo).hide();
				$boardCopy.find("div.board_title").html(board.boardTitle);
				$boardCopy.find("div.board_dt").html(board.qnaBoardDt);
				$boardCopy.find("div.board_nickname").html(board.userNickname);
				$boardCopy.find("div.board_secret").html(board.qnaBoardSecret).hide();
				$boardCopy.find("div.board_title").addClass("down");
				$boardParent.append($boardCopy);
				if (board.qnaBoardSecret > 0) {
					if(board.userNickname == loginedNickname || loginedUserType == 2) {
						$boardCopy.find("div.board_title").html(board.boardTitle);
					}else {
						$boardCopy.find("div.board_title").html("비밀글 입니다");
					}
				} else {
					$boardCopy.find("div.board_title").html(board.boardTitle);
				}
			});
			$("div.qnaboard").first().remove();

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
		let word = $("div.searchInput>input[name=word]").val().trim();
		let url = "";
		let data = "";
		if (word == "") {
			url = "http://localhost:1127/qna/board/list/" + pageNo;
			data = "currentPage=" + pageNo;
			} else {
			url = "http://localhost:1127/qna/board/search/" + word + "/" + pageNo;
			data = "word=" + word + "&currentPage=" + pageNo;
			}
			showList(url, data);
			return false;
		});			
	//---페이지 그룹의 페이지를 클릭 END---
	//---검색 클릭 START---
	$("div.searchInput>button[name=qna-searchBtn]").click(function () {
		let word = $("div.searchInput>input[name=word]").val().trim();
		let url = "http://localhost:1127/qna/board/search/" + word;
		let data = "word=" + word;
		if(word == "") {
			alert("검색어를 입력해주세요");
		}
		showList(url, data);
		return false;
	});
	//---검색 클릭 END---
	//--비밀글 제외 보기 클릭 START---
	$("button[name=publicboard]").click(function () {
		let pageNo = $("div.pagegroup").val();
		let url = "http://localhost:1127/qna/board/openlist/" + pageNo;
		let data = "currentPage=" + pageNo;
		if (pageNo == 1) {
		url = "http://localhost:1127/qna/board/openlist";
		} else {
		url = "http://localhost:1127/qna/board/openlist/" + pageNo;
		data = "currentPage=" + pageNo;
		}
		showList(url, data);
		$("button[name=publicboard]").off("click").on("click", function() {
			showList("http://localhost:1127/qna/board/list");
		});
		return false;
	});
	//--비밀글 제외 보기 클릭 END---

	//---제목 클릭 START---
	$("div.boardlist").on("click", "div.qnaboard>div.cell>div.summary>div.board_title", function () {		
		if ($(this).hasClass("down")) {
			let boardNo = $(this).siblings("div.board_no").html();
			let $detail = $(this).parents("div.cell").find("div.detail");
			let $boardContent = $detail.find("input.board_content");
			let $qnaCmtContent = $detail.find("input.comment_content");
			let $qnaCmtdt = $detail.find("div.comment_dt");
			let $commentUserNickname = $detail.find("div.comment_user_nickname");
			let $writeCmt = $detail.find("div.write-comment");
			let $modify = $detail.find("div.modifyNremove button[name=board-modify]");
			let $remove = $detail.find("div.modifyNremove button[name=board-remove]");
			let $modifySub = $detail.find("div.modifyNremove button[name=board-modify-submit]");
			let $cmtInsert =$detail.find("div.insertNremove button[name=comment-insert]");
			let $cmtRemove = $detail.find("div.insertNremove button[name=comment-remove]");
		$.ajax({
			url: "http://localhost:1127/qna/board/view/" + boardNo,
			method: "get",
			success: function (jsonObj) {
				if (jsonObj.status == 1) {
						let board = jsonObj.t;
						if(loginedUserType == 2) {
							if(board.comment != null) {
								$boardContent.val(board.boardContent);
								$qnaCmtContent.val(board.comment.qnaCmtContent).html(board.comment.qnaCmtContent);
								$qnaCmtdt.val(board.comment.qnaCmtDt).html(board.comment.qnaCmtDt);
								$commentUserNickname.val(board.comment.userNickname).html(board.comment.userNickname);
								$detail.show();
								$modify.hide();
								$modifySub.hide();
								$cmtInsert.hide();
								$cmtRemove.show();
							}else {
								$boardContent.val(board.boardContent);
								$qnaCmtContent.hide();
								$detail.show();
								$modify.hide();
								$modifySub.hide();
								$writeCmt.show();
								$cmtInsert.show();
								$cmtRemove.hide();
							}
						}else {
							if(board.qnaBoardSecret != 0) {
								if (loginedNickname != board.userNickname) {
									alert("비밀글은 작성자와 관리자만 볼 수 있습니다");
								}else if(loginedNickname == board.userNickname) {
									if(board.comment == null) {
										$boardContent.val(board.boardContent).html(board.boardContent);
										$detail.show();
										$remove.show();
										$modify.show();
										$modifySub.hide();
										$cmtInsert.hide();
										$cmtRemove.hide();
									}else if (board.comment != null) {
										$boardContent.val(board.boardContent).html(board.boardContent);
										$boardContent.attr("readonly", "readonly");
										$qnaCmtContent.val(board.qnaCmtContent).html(board.qna);
										$qnaCmtdt.val(board.qnaCmtdtContent).html(board.qnaCmtdt);
										$commentUserNickname.val(board.commentUserNickname).html(board.commentUserNickname);
										$detail.show();
										$remove.show();
										$modify.hide();
										$modifySub.hide();
										$cmtInsert.hide();
										$cmtRemove.hide();
									}
								}
							}else if(board.qnaBoardSecret == 0) {
								if (loginedNickname != board.userNickname) {
									if(board.comment == null) {
										$boardContent.val(board.boardContent);
										$detail.show();
										$remove.hide();
										$modify.hide();
										$modifySub.hide();
										$cmtInsert.hide();
										$cmtRemove.hide();
									}else if (board.comment != null) {
										$boardContent.val(board.boardContent).html(board.boardContent);
										$qnaCmtContent.val(board.comment.qnaCmtContent).html(board.comment.qnaCmtContent);
										$qnaCmtdt.val(board.comment.qnaCmtDt).html(board.comment.qnaCmtDt);
										$commentUserNickname.val(board.comment.userNickname).html(board.comment.userNickname);
										$detail.show();
										$remove.hide();
										$modify.hide();
										$modifySub.hide();
										$cmtInsert.hide();
										$cmtRemove.hide();
									}
								}else if(loginedNickname == board.userNickname){
									if(board.comment == null) {
										$boardContent.val(board.boardContent);
										$detail.show();
										$remove.show();
										$modify.show();
										$modifySub.hide();
										$cmtInsert.hide();
										$cmtRemove.hide();
									}else if (board.comment != null) {
										$boardContent.val(board.boardContent).html(board.boardContent);
										$qnaCmtContent.val(boardcomment.qnaCmtContent).html(boardcomment.qnaCmtContent);
										$qnaCmtdt.val(board.comment.qnaCmtDt).html(board.comment.qnaCmtDt);
										$commentUserNickname.val(board.comment.userNickname).html(board.comment.userNickname);
										$detail.show();
										$remove.show();
										$modify.hide();
										$modifySub.hide();
										$cmtInsert.hide();
										$cmtRemove.hide();
									}
								}
							}
						}
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
	});

	//---수정버튼 클릭 START---
	$("div.boardlist").on( "click", "div.qnaboard>div.cell>div.detail>div.modifyNremove button.modify", function () {
			let $userNickname = $(this).parents("div.cell").find("div.board_nickname").html();
			let $boardTitle = $(this).parents("div.cell").find("div.board_title");
			let $detail = $(this).parents("div.cell").find("div.detail");
			let $boardContent = $detail.find("input.board_content");
			let $modify = $detail.find("div.modifyNremove button[name=board-modify]");
			let $remove = $detail.find("div.modifyNremove button[name=board-remove]");
			let $modifySub = $detail.find("div.modifyNremove button[name=board-modify-submit]")
			$boardTitle.removeClass("up");
			if(loginedNickname == $userNickname) {
				$boardTitle.attr("contenteditable","true");
				$boardContent.removeAttr("readonly");
				$boardTitle.css("outline", "auto");
				$boardContent.css("outline", "auto");
				$modify.hide();
				$remove.show();
				$modifySub.show();
			}
			$("div.boardlist").on( "click", "div.qnaboard>div.cell>div.detail>div.modifyNremove button.modifysubmit",	function () {
				$boardTitle.addClass("up");
				let boardNo = $(this).parents("div.cell").find("div.board_no").html();
				let boardTitle = $(this).parents("div.cell").find("div.board_title").html();
				let boardContent = $(this).parents("div.cell").find("input.board_content").val();
				let boardSecret = $(this).parents("div.cell").find("div.board_secret").html();
					$.ajax({
					url: "http://localhost:1127/qna/board/" + boardNo,
					method: "PUT",
					timeout: 0,
					headers: {
					"Content-Type": "application/json",
					},
					data: JSON.stringify({
						boardNo: boardNo,
						boardTitle: boardTitle,
						boardContent: boardContent,
						userNickname: loginedNickname,
						qnaBoardSecret : boardSecret
					}),
					success: function (jsonObj) {
						$("div.modal-body-alert").text("글이 수정되었습니다");
						$("button[name=close]").click(function () {
							location.href = "./qnaboardlist.html";
						});
					},
					error: function (jqXHR, textStatus) {
					console.log(boardContent);
					alert("수정 에러:" + jqXHR.status + ", jqXHR.responseText:" + jqXHR.responseText);
					},
				});
				return false;
				}
			);
		});

	//---수정버튼 클릭 END---

	//---글 삭제버튼 클릭 START 댓글이 있는글은 삭제할 수 없음---
	$("div.boardlist").on("click","div.qnaboard>div.cell>div.detail>div.modifyNremove button[name=board-remove]",function () {
			let boardNo = parseInt($(this).parents("div.cell").find("div.board_no").html());
				$.ajax({
				url: "http://localhost:1127/qna/board/delete/" + boardNo,
				method: "DELETE",
				data: JSON.stringify({
					boardNo: boardNo,
				}),
				success: function () {
					$("div.modal-body-alert").text("글이 삭제 되었습니다");
					$("button[name=close]").click(function () {
						location.href = "./qnaboardlist.html";
					});
				},
				error: function (jqXHR, textStatus) {
				alert("삭제 에러:" + jqXHR.status + ", jqXHR.responseText:" + jqXHR.responseText);
				}
			});
			return false;
		});
	//---삭제버튼 클릭 END---

	// 답글저장
	$("div.boardlist").on(
		"click",
		"div.qnaboard>div.cell>div.detail>div.insertNremove>button[name=comment-insert]",
		function () {
		let boardNo = parseInt($(this).parents("div.cell").find("div.board_no").html());
		let qnaCmtContent = $(this).parents("div.cell").find("textarea[name=input_comment]").val();
		let userNickname = localStorage.getItem("loginedNickname");
		$.ajax({
			url: "http://localhost:1127/qna/board/comment/" + boardNo,
			method: "POST",
			timeout: 0,
			headers: {
				"Content-Type": "application/json",
				},
			data: JSON.stringify({
				"commentNo": boardNo,
				"qnaCmtContent": qnaCmtContent,
				"userNickname" : userNickname,
			}),
			success: function () {
				$("div.modal-body-alert").text("답변이 등록되었습니다");
				$('button[name=close]').click(function () {
					location.href = "./qnaboardlist.html";
				});
			},
			error: function (jqXHR, textStatus) {
				$("div.modal-body-alert").text("등록에 실패했습니다.다시 시도해주세요");
				alert("답글 에러:" +jqXHR.status +", jqXHR.responseText:" +jqXHR.responseText);
			},
		});
		return false;
		//---답글저장버튼 클릭 END---
		}
	);
	
	//답글삭제
	$("div.boardlist").on("click","div.qnaboard>div.cell>div.detail>div.insertNremove button[name=comment-remove]",function () {
		let commentNo = parseInt($(this).parents("div.cell").find("div.board_no").html());
			$.ajax({
			url: "http://localhost:1127/qna/board/comment/delete/" + commentNo,
			method: "DELETE",
			data: JSON.stringify({
				"commentNo": commentNo,
			}),
			success: function () {
				$("div.modal-body-alert").text("답변이 삭제되었습니다");
				$('button[name=close]').click(function () {
					location.href = "./qnaboardlist.html";
				});
			},
			error: function (jqXHR, textStatus) {
			alert("삭제 에러:" + jqXHR.status + ", jqXHR.responseText:" + jqXHR.responseText);
			}
		});
		return false;
	});
});