$(function(){
    //1. 리스트 불러와서 보여주기
    function showList(url){
        $.ajax({
            url: url,
            method: "get",
            success: function(jsonObj){
                if(jsonObj.status == 1){ //rb의 status가 1일때
                    let pageableContentObj = jsonObj.t.content;

                    let $board = $("div.board-list").first();
                    $("div.board-list").not($board).remove();

                    let $boardParent = $board.parent();
                    //한줄씩 넣기 //나중에 span을 div로 바꾸고 테이블형식 넣기 
                    $(pageableContentObj).each(function(index, board){
                        let $boardCopy = $board.clone();
                        // 나중에 "" + 다 빼기
                        $boardCopy.find("div.board-list__content__no").html("글번호 : " + board.roundReviewBoardNo)
                        $boardCopy.find("img.board-list__content__image").attr("src", "../roundreview_images/" + board.roundReviewBoardNo + "_RoundReviewThumbnail.jpg");
                        $boardCopy.find("div.board-list__content__title").html("제목 : " + board.roundReviewBoardTitle)
                        $boardCopy.find("div.board-list__content__cmt-cnt").html("댓글수 : " + board.roundReviewBoardCmtCnt)
                        $boardCopy.find("div.board-list__content__nickname").html("닉네임 : " + board.userNickname)
                        $boardCopy.find("div.board-list__content__dt").html("작성일자 : " + board.roundReviewBoardDt)
                        $boardCopy.find("div.board-list__content__view-cnt").html("조회수 : " + board.roundReviewBoardViewCnt)
                        $boardCopy.find("div.board-list__content__like-cnt").html("좋아요수 : " + board.roundReviewBoardLikeCnt)

                        $boardParent.append($boardCopy);
                    });
                    //맨앞에 뜨던 그지같은 이미지 삭제 
                    $("img.board-list__content__image").first().remove();

                    let $pageGroup = $("div.page-group");
                    let $pageGroupHtml = "";

                    let pageableObj = jsonObj.t;
                    let currentPage = pageableObj.number + 1;  // 0 + 1
                    let totalPage = pageableObj.totalPages
                    let size = pageableObj.size  //5
                    let endPageNo = Math.ceil(currentPage/size)*size; // 5
                    let startPageNo = endPageNo - size + 1;
                    if (totalPage < endPageNo) {
                        endPageNo = totalPage;
                    }

                    if (startPageNo > 1) {
                        $pageGroupHtml += '<span class="prev">◁</span>';
                    }

                    //startPage부터 endPage까지 숫자 넣어주기 
                    //현재페이지면 disable클래스 줘서 css다르게 넣어주기 (링크안되게?)
                    for (let i = startPageNo; i<= endPageNo; i++){
                        $pageGroupHtml += "&nbsp;&nbsp;";
                        // $pageGroupHtml += "<span>" + i + "</span>"; 
                        if (currentPage == i){
                            $pageGroupHtml += '<span class="disabled">' + i + "</span>";
                        } else {   
                            $pageGroupHtml += "<span>" + i + "</span>"; 
                        }
                    }
                    //back에서 보내준 endPage 값이 totalPage값보다 작으면 화살표나오게  
                    if (endPageNo < totalPage) {
                        $pageGroupHtml += "&nbsp;&nbsp;";
                        $pageGroupHtml += '<span class="next">▷</span>';
                    }
                    //pageGroupHtml에 받아놨던 정보를 pageGroup selector에 넣어주기 
                    $pageGroup.html($pageGroupHtml);
                }else {
                    alert(jsonObj.msg); //rb에 set된 메시지 
                }
            },
            error: function(jqXHR){
                alert("에러:" + jqXHR.status);
            }
        });
    }
    showList('http://localhost:1125/backroundreview/board/list');
    //1-2. 페이지그룹 페이지 클릭
    //span 태그들 중에서 disabled가 아닌 요소 찾기 
    
    $("div.page-group").on("click", "span:not(.disabled)", function() {
        let orderType = 0;
        let pageNo = 0;
        
        //?? 이해안됨
        if($(this).hasClass("prev")){
            pageNo = parseInt($(this).next().html()) - 2;
        } else if ($(this).hasClass("next")){
            pageNo = parseInt($(this).prev().html()) ;
        } else {
            pageNo = parseInt($(this).html()) -1;
        }
        //trim() 양끝의 공백을 제거 
        let word = $("div.search>input[name=search-box]").val().trim();
        let url = "";
        let data = "";

        console.log(orderType);
        console.log(pageNo);

        if(word == "") {
            url = "http://localhost:1125/backroundreview/board/list/" + orderType + "/" + pageNo;
            console.log(url);
        } else {
            //검색어가 있는 경우 검색어를 path에 넣어주고 back 에 보낼 data를 만들기 
            url = "http://localhost:1125/backroundreview/search/" + word + "/" + pageNo;
            data = "currentPage=" + pageNo + "&word=" + word;
        } 
        showList(url, data);
        return false;
    });

    //2. 검색하기
    //이미지 클릭으로 바꾸기 
    $("div.search>button.searchBtn").click(function(){
        let word = $("div.search>input[name=search-box]").val().trim();
        console.log(word);
        let url = "http://localhost:1125/backroundreview/search/";
        let data = "currentPage=1&word=" + word;
        // let data = "";
        showList(url, data);
        return false;
    });

    //3. 최신순 정렬하기
    $("ul.order>li.order__recent>a").click(function(){
        //현재의 pageNo를 어떻게 잡아올것인지? 잡아와서 optCp로 만들고 orderType를 작성해야함 
        let orderType = 0;
        let url = "http://localhost:1125/backroundreview/board/list/" + orderType
        let data = "";
        showList(url,data);
    });
    //4. 조회순 정렬하기
    $("ul.order>li.order__view-cnt>a").click(function(){ 
        let orderType = 1;
        let url = "http://localhost:1125/backroundreview/board/list/" + orderType
        let data = "";
        showList(url,data);
    });
    //5. 좋아요순 정렬하기
    $("ul.order>li.order__like-cnt>a").click(function(){ 
        let orderType = 2;
        let url = "http://localhost:1125/backroundreview/board/list/" + orderType
        let data = "";
        showList(url,data);
    });
    //6. 글쓰기로 이동하기 -> 보내줄 데이터 없음 (닉네임? )
    $("header>span.write").click(function(){
        $(location).attr('href', '../html/roundreviewwrite.html');
    })

    //7. 제목이나 사진 눌렀을때 해당 게시글로 이동하기
    $("div.board-list__content__thumbnail, div.board-list__content__title").on('click', function(){
        let $roundReviewBoardNoObj = $(this).prev().find('roundReviewBoardNo');
        let round_review_board_no = $roundReviewBoardNoObj.html();
        location.href = "/front/html/roundreview/board/" + round_review_board_no;
    })
})