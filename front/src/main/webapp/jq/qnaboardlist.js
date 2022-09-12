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
				alert(data.msg);
				console.log(jsonObj.msg);
				}
			},
			error: function (jqXHR) {
			alert("에러:" + jqXHR.status);
			console.log($board());
			},
		});
	}
//---페이지 로드되자 마자 게시글1페이지 검색 START---
showList("http://localhost:1127/qna/board/list");
//showList("qna/board/list");
showList("/qna/board/list", "currentPage=1");
//---페이지 로드되자 마자 게시글1페이지 검색 END---

//---페이지 그룹의 페이지를 클릭 START---
$("div.pagegroup").on("click", "span:not(.disabled)", function () {
let pageNo = 1;
// if (!$(this).hasClass('disabled')) {
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
		url = "/qna/board/list/" + pageNo;
		data = "currentPage=" + pageNo;
	} else {
		url = "/qna/board/list/search/" + word + pageNo;
		data = "currentPage=" + pageNo + "&word=" + word;
	}
	showList(url, data);
	return false;
// }
});
//---페이지 그룹의 페이지를 클릭 END---
});
