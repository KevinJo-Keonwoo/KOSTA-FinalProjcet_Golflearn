$(function() { 
    //로딩 되자마자 글 목록 불러오기(1 페이지)
    // let url = "http://localhost:1126/backresale/resale/board/list/1";
    function showList(url){
        $.ajax({
            url:url,
            method: "get",
            success:function(jsonObj){
                if(jsonObj.status ==1){
                    let pageContentObj = jsonObj.t.content; // content -> pageable사용 시 담기는 데이터들
                    // console.log(pageBeanObj.t.content);
                    //게시글 div를 원본으로 함
                    // 게시글 목록을 찾아 복붙 해 넣는 작업
                    let $board= $("div.boardlist__content").first();
                    // 원본 하나 선택 후 나머지 게시글의 div삭제하는 작업
                    $("div.boardlist__content").not($board).remove();
                    let $boardParent = $board.parent();
                    let src = "../resale_images/";
                    $(pageContentObj).each(function (index, board) {
                        let $boardCopy = $board.clone();
                        // console.log(board.userNickname); // 출력됨
                        // console.log(board.resaleBoardNo);

                        $boardCopy.find("div.board-list__board__no").html(board.resaleBoardNo);
                        $boardCopy.find("div>img.board-list__content__thumbnail").attr("src", src+ board.resaleBoardNo +"/s_image_1" +".jpg");
                        $boardCopy.find("div.board-list__content__title").html(board.resaleBoardTitle);
                        $boardCopy.find("div.board-list__content__nickname").html(board.userNickname);
                        $boardCopy.find("div.board-list__content__dt").html(board.resaleBoardDt);
                        $boardCopy.find("div.board-list__content__view-cnt").html(board.resaleBoardViewCnt);
                        $boardCopy.find("div.board-list__content__cmt-cnt").html(board.resaleBoardCmtCnt);
                        $boardCopy.find("div.board-list__content__like-cnt").html(board.resaleBoardLikeCnt);
                        $boardParent.append($boardCopy);
                    });
                    
                        $("img.board-list__content__thumbnail").first().remove();
                        // $("img.board-list__content__thumbnail").first().text("사진");

                        // 페이지 그룹
                        let $pagegroup = $("div.page-group");
                        let $pagegroupHtml = "";

                        let pageableObj = jsonObj.t;
                        let cntPerPageGroup = 5;
                        let currentPage = pageableObj.number +1;
                        
                        let endPage =Math.ceil(currentPage/cntPerPageGroup)*cntPerPageGroup;
                        let startPage = endPage - cntPerPageGroup + 1
                        let totalPage = pageableObj.totalPages;

                        console.log("현재페이지"+ currentPage);
                        console.log(startPage);
                        console.log("마지막페이지" + endPage);
                        console.log(totalPage);

                        // let nextPage = endPage +1; // 화면에 보여질 마지막 페이지 번호
                        // let prevPage = startPage -1; // 화면에 보여질 첫번째 페이지 번호
                        
                        if(totalPage< 1){
                            startPage = endPage;
                        }

                        //내용 채워주기
                        if(startPage > 5){
                            $pagegroupHtml += '<span class="prev"> ◁ </span>'
                            console.log("시작페이지 : " + startPage);
                        // } else if(startPage < 0){
                            //     startPage = 1; 
                        }
                        if (totalPage < endPage) {
                            endPage = totalPage;
                        }

                        // 시작페이지가 끝 페이지보다 작거나 같을 때 
                        for(let i = startPage ; i <= endPage ; i++){
                            $pagegroupHtml += "&nbsp;&nbsp;";
                            if(currentPage == i) { // 현재페이지인 경우 <span> 태그 만들지 않음
                                $pagegroupHtml += '<span class="disabled">' + i + '</span>';
                            } else {
                                $pagegroupHtml += "<span>" + i + "</span>";
                            }
                        } // for문 종료
                        // 맨 마지막                            
                        if(endPage < totalPage){ // 마지막 페이지가 총 페이지 수 보다 작으면 NEXT
                            $pagegroupHtml += "&nbsp;&nbsp;";
                            $pagegroupHtml += '<span class="next"> ▷ </span>';
                        }
                        $pagegroup.html($pagegroupHtml);
                        
                    } else{
                        alert(jsonObj.msg);
                    }
                }, // success 종료
                error: function(jqXHR){
                    alert("에러 : " + jqXHR.status);
                }
            });
            return false;
        }

        showList("http://localhost:1126/backresale/resale/board/list");

    // ----- 페이지 그룹의 페이지를 클릭 START -----
    $("div.page-group").on("click","span:not(.disabled)",function(){
            //span 태그들 중에서 class 태그 속성이 disabled 가 아닌 요소들 찾기
            let pageNo = 1;
            if($(this).hasClass("prev")) {
                pageNo = parseInt($(this).next().html()) -1; // 문자 타입을 숫자타입으로 변환해야 NEXT 클릭 시 3페이지 노출
            // parseInt 해 주지 않으면 next 페이지 21로 노출됨
            } else if ($(this).hasClass("next")) {
                pageNo = parseInt($(this).prev().html()) + 1; //prev()까지는 객체 .html()까지는 객체의 내용
            } else { 
                pageNo = parseInt($(this).html());
            }
            // alert("보려는 페이지번호: " + pageNo);
            let word = $("div.search>div.search__box>input.search__input").val().trim(); // 문자열 공백 제거 -> trim
            console.log("페이지 번호" + pageNo);
            console.log(word);
            let url = "";
            let data = "";
            if (word == "") {
                url = "http://localhost:1126/backresale/resale/board/list/" + pageNo;
            // data = "currentPage=" + pageNo;
            } else {
                url = "http://localhost:1126/backresale/resale/board/search/"+word +"/"+pageNo;
                data = "currentPage=" + pageNo + "&word=" + word;
            }
            showList(url, data);
            return false;
        });
        // --페이지 그룹의 페이지를 클릭 END

    // 검색창 클릭
    let $btSearch = $("div.search__button > button.search__button");
    $btSearch.click(function(){
        let word = $("div.search>div.search__box>input.search__input").val().trim();
        let url = "http://localhost:1126/backresale/resale/board/search";
        let data = "currentPage=1 & word=" + word;
        // let data = "currentPage=" + pageNo + "&word=" + word;
        showList(url, data);
        return false;
    });

    // 글쓰기 버튼 클릭 시 글쓰기 페이지로 이동
    // ----- 글쓰기 버튼 클릭 START -----
    let $btWrite = $("button[name=write-button]");
    $btWrite.click(function(){
        location.href = "http://localhost:1123/front/html/resaleboardwrite.html";
    });
    // ----- 글쓰기 버튼 클릭 END -----

        //클릭한 해당 게시물로 이동
    $("div.board-list").on("click", "div.boardlist__content", function(){
        $board = $("div.board-list__content__no");
        $boardNo = $(this).children($board).text();
        console.log("게시글번호는"+$boardNo);

        alert($boardNo);
        location.href = '../html/resaledetail.html?resale_board_no=' + $boardNo;
    });
}); // 맨 위의 funcion