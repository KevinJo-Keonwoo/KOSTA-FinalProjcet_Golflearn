$(function() { 
    //로딩 되자마자 글 목록 불러오기(1 페이지)
    // let url = "http://localhost:1126/backresale/resale/board/list/1";
    showList("http://localhost:1128/noticeboard/notice/list");
    function showList(url){
        $.ajax({
            url:url,
            success:function(jsonObj){
                if(jsonObj.status ==1){
                    let pageBeanObj = jsonObj.t;
                    //게시글 div를 원본으로 함
                    // 게시글 목록을 찾아 복붙 해 넣는 작업
                    let $board= $("div.boardlist__content").first();
                    // 원본 하나 선택 후 나머지 게시글의 div삭제하는 작업
                    $("div.boardlist__content").not($board).remove();
                    let $boardParent = $board.parent();
                    let src = "../notice_images/";
                    $(pageBeanObj.list).each(function (index, board) {
                        let $boardCopy = $board.clone();
                        console.log(board.userNickname); // 출력됨
                        console.log(board.noticeBoardNo);

                        $boardCopy.find("div.board-list__content__no").html(board.noticeBoardNo);
                        $boardCopy.find("div.board-list__content__title").html(board.noticeBoardTitle);
                        $boardCopy.find("div.board-list__content__nickname").html(board.userNickname);
                        $boardCopy.find("div.board-list__content__dt").html(board.noticeBoardDt);
                        $boardCopy.find("div.board-list__content__view-cnt").html(board.noticeBoardViewCnt);
                        $boardCopy.find("div.board-list__content__cmt-cnt").html(board.noticeBoardCmtCnt);
                        $boardCopy.find("div.board-list__content__like-cnt").html(board.noticeBoardLikeCnt);
                        $boardCopy.find("div>img.board-list__content__thumbnail").attr("src", src + board.noticeBoardNo + "_image_" + "pyeonan.png");
                        $boardParent.append($boardCopy);
                    });

                    // 페이지 그룹  이 부분이 잘못되어있음
                    // let $pagegroup = $("div.page-group");
                    // let $pagegroupHtml = "";
                    
                    // let currentPage = pageBeanObj.currentPage;
                    // let startPage = pageBeanObj.startPage;
                    // let endPage = pageBeanObj.endPage;
                    // let totalPage = pageBeanObj.totalPage;
                    // let cntPerPageGroup = pageBeanObj.cntPerPageGroup;
                    // let cntPerPage = 5;

                    // if(endPage > totalPage){
                    //     endPage = totalPage;
                    // }
                    // let next = endPage +1; // 화면에 보여질 마지막 페이지 번호
                    // let prev = startPage -1 ; // 화면에 보여질 첫번째 페이지 번호


                    // if(totalPage<1){
                    //     startPage = endPage;
                    // }
                    // let pages = $("ul.page-group__pages")
                    // pages.empty();
                    // if(startPage > 5){
                    //     pages.append('<li class="pagination--item"></li>')
                    // }
                    // //내용 채워주는 것
                    // if(pageBeanObj.startPage > 5){
                    //     $pagegroupHtml += '<span class="prev"> ◁ </span>';
                    // }
                    // for(let i = pageBeanObj.startPage ; i<=pageBeanObj.endPage ; i ++){
                    //     $pagegroupHtml += "&nbsp;&nbsp;";
                    //     if(pageBeanObj.currentPage == i){ // 현재페이지인 경우
                    //         $pagegroupHtml += '<span class="disabled">'+ i + '</span>'
                    //     }else{
                    //         $pagegroupHtml += '<span>' + i + '</span>';
                    //     }
                    // }
                    // if(pageBeanObj.endPage < pageBeanObj.totalPage){
                    //     $pagegroupHtml += "&nbsp;&nbsp;";
                    //     $pagegroupHtml += '<span class="next"> ▷ </span>';
                    // }

                    //     $pagegroup.html($pagegroupHtml);
                    // } else{
                    //     alert(jsonObj.msg);
                    // }
                }
                },
            error: function(jqXHR){
                alert("에러 : " + jqXHR.status);
            }
        });
    }
    
    // 글쓰기 버튼 클릭 시 글쓰기 페이지로 이동
    let $btWrite = $("button[name=write-button]");
    $btWrite.click(function(){
        location.href = "http://localhost:1128/front/html/noticeboardwrite.html";
    });

    //클릭한 해당 게시물로 이동
    $("div.board-list").on("click", "div.boardlist__content", function(){
        $board = "div.board-list__content__no";
        $boardNo = $(this).children($board).text();
        console.log($boardNo);
        let url = "http://localhost:1128/noticeboard/"
        $.ajax({
            url:url,
            method: "GET",
            success:function(jsonObj){
                if(jsonObj.status ==1){
                    console.log(jsonObj.noticeBoardNo);
                }
            }
        })
        // location.href = "http://localhost:1128/front/html/noticeboard?" + $boardNo;
		// return false;
    });

    // ----- 페이지 그룹의 페이지를 클릭 START -----
    $("div.page-group").on("click","span:not(.disabled)",function(){
        //span 태그들 중에서 class 태그 속성이 disabled 가 아닌 요소들 찾기
        let pageNo = 1;
		if ($(this).hasClass("prev")) {
		    pageNo = parseInt($(this).next().html()) - 1; // 문자 타입을 숫자타입으로 변환해야 NEXT 클릭 시 3페이지 노출
		// parseInt 해 주지 않으면 next 페이지 21로 노출됨
		} else if ($(this).hasClass("next")) {
		    pageNo = parseInt($(this).prev().html()) + 1; //prev()까지는 객체 .html()까지는 객체의 내용
		} else { 
            pageNo = parseInt($(this).html());
		}
		// alert("보려는 페이지번호: " + pageNo);
		let word = $("div.search>div.search__box>div.search__input").val().trim(); // 문자열 공백 제거 -> trim
		let url = "";
		let data = "";
		if (word == "") {
            url = "http://localhost:1128/backresale/notice/board/list/" + pageNo;
		// data = "currentPage=" + pageNo;
		} else {
            url = "http://localhost:1128/backresale/notice/board/search/"+word + pageNo;
            data = "currentPage=" + pageNo + "&word=" + word;
		}
		showList(url, data);
		return false;
    });
    // --페이지 그룹의 페이지를 클릭 END



}); // 맨 위의 funcion