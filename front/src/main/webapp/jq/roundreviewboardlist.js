$(function(){
    //1. 리스트 불러와서 보여주기
    function showList(url, data){
        $.ajax({
            url: url,
            method: "get",
            success: function(jsonObj){
                if(jsonObj.status == 1){ //rb의 status가 1일때
                    let pageableContentObj = jsonObj.t.content;
                    //board-list                comment
                    //board-list__content       comment-list
                    let $board = $("div.board-list").first();
                    $board.show();
                    
                    $("div.board-list").not($board).remove();
                    let $boardParent = $board.parent();
                    //한줄씩 넣기 //나중에 span을 div로 바꾸고 테이블형식 넣기 
                    $(pageableContentObj).each(function(index, board){
                        let $boardCopy = $board.clone();
                        // 나중에 "" + 다 빼기
                        $boardCopy.find("div.board-list__content__no").html(board.roundReviewBoardNo)
                        $boardCopy.find("img.board-list__content__image").attr("src", "../roundreview_images/" + board.roundReviewBoardNo + "/image_1.png");
                        $boardCopy.find("div.board-list__content__title").html(board.roundReviewBoardTitle)
                        $boardCopy.find("div.board-list__content__cmt-cnt").html(board.roundReviewBoardCmtCnt)
                        $boardCopy.find("div.board-list__content__nickname").html(board.userNickname)
                        $boardCopy.find("div.board-list__content__dt").html(board.roundReviewBoardDt)
                        $boardCopy.find("div.board-list__content__view-cnt").html(board.roundReviewBoardViewCnt)
                        $boardCopy.find("div.board-list__content__like-cnt").html(board.roundReviewBoardLikeCnt)

                        $boardParent.append($boardCopy);
                    });
                    //맨앞에 뜨던 그지같은 이미지 삭제 
                    // $("img.board-list__content__image").first().hide();
                    $("div.board-list").first().hide();

                    let $pageGroup = $("div.page-group");
                    let $pageGroupHtml = "";

                    let pageableObj = jsonObj.t;
                    let currentPage = pageableObj.number + 1;  
                    let totalPage = pageableObj.totalPages
                    let size = pageableObj.size  
                    let endPageNo = Math.ceil(currentPage/size)*size; 
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
                        $pageGroupHtml += "&nbsp;&nbsp&nbsp&nbsp;";
                        // $pageGroupHtml += "<span>" + i + "</span>"; 
                        if (currentPage == i){
                            $pageGroupHtml += '<span class="disabled">' + i + "</span>";
                        } else {   
                            $pageGroupHtml += "<span>" + i + "</span>"; 
                        }
                    }
                    //back에서 보내준 endPage 값이 totalPage값보다 작으면 화살표나오게  
                    if (endPageNo < totalPage) {
                        $pageGroupHtml += "&nbsp;&nbsp&nbsp&nbsp;";
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
        // log.console(data);
        // let orderType = data.split("=");
        let orderType = 0;
        $("ul.order>li").each(function(index, element){
            let $aObj = $(element).find('a');
            // if($aObj.css("background-color") == 'rgb(255, 0, 0)'){
            if($aObj.css("color") == 'rgb(255, 0, 0)'){
                // switch($(element).html()){
                //     case '최신순':break;
                //     case '조회순':break;
                //     case '좋아요순':break
                // }
                // switch(index){
                    //     case 0:break;
                    //     case 1:break;
                    //     case 2:break
                    // }
                orderType = index;
                return false;
            }
        });
        
        //$("ul.order>li").is("background-color", "yellow"); //기본
        
        let pageNo = 0;
        
        //pageNo = $(this).html();
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
        if(word == "") {
            url = "http://localhost:1125/backroundreview/board/list/" + orderType + "/" + pageNo;
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
        let url = "http://localhost:1125/backroundreview/search/" + word;
        // let data = "currentPage=1&word=" + word;
        let data = "";
        showList(url, data);
        return false;
    });

    //3. 최신순 정렬하기
    $("ul.order>li.order__recent>a").click(function(){
        //현재의 pageNo를 어떻게 잡아올것인지? 잡아와서 optCp로 만들고 orderType를 작성해야함 
        let orderType = 0;
        let url = "http://localhost:1125/backroundreview/board/list/" + orderType
        let data = "";
        
        $("ul.order>li>a").css("color", "#64DB99"); //기본
        $(this).css("color", "red"); //클릭된경우
        
        showList(url,data);
    });
    //4. 조회순 정렬하기
    $("ul.order>li.order__view-cnt>a").click(function(){ 
        let orderType = 1;
        let url = "http://localhost:1125/backroundreview/board/list/" + orderType
        // let data = "orderType=" + orderType;;
        let data = "";
        $("ul.order>li>a").css("color", "#64DB99"); //기본
        $(this).css("color", "red"); //클릭된경우
        showList(url,data);
    });
    //5. 좋아요순 정렬하기
    $("ul.order>li.order__like-cnt>a").click(function(){ 
        let orderType = 2;
        let url = "http://localhost:1125/backroundreview/board/list/" + orderType
        let data = "";
        
        $("ul.order>li>a").css("color", "#64DB99"); //기본
        
        $(this).css("color", "red"); //클릭된경우
        showList(url,data);
    });
    //6. 글쓰기로 이동하기 -> 보내줄 데이터 없음 (닉네임? )
    $("header button.write").click(function(){
        $(location).attr('href', '../html/roundreviewboardwrite.html');
        // location.href = "http://localhost:1123/front/html/roundreviewboardwrite.html";
    })

    //7 수정하기

    //8. 제목이나 사진 눌렀을때 해당 게시글로 이동하기
    //undefined나오는것 해결하기 + 여기서 누른 번호 보내주기 roundreviewboardno
    $("div.board").on('click', "img.board-list__content__image, div.board-list__content__title", function(){
        let $roundReviewBoardNoObj = $(this).parent().find('div.board-list__content__no');
        let round_review_board_no = $roundReviewBoardNoObj.html();
        console.log(round_review_board_no);
        location.href = '../html/roundreviewdetail.html?round_review_board_no=' + round_review_board_no;
        // location.href = '../html/roundreviewdetail.html?round_review_board_no=' + 1;
        // location.href = '../html/roundreviewdetail.html?round_review_board_no=1';
        // location.href = "http://localhost:1125/front/board/" + round_review_board_no;
        // $(location).attr('href', '../html/roundreview/board/' + round_review_board_no + '.html');
        
        // let url = "http://localhost:1128/noticeboard/"
        // $.ajax({
        //     url:url,
        //     method: "GET",
        //     success:function(jsonObj){
        //         if(jsonObj.status ==1){
        //             console.log(jsonObj.noticeBoardNo);
        //         }
        //     }
        // })
        
    })
    
    //라운드리뷰 클릭하면 재로딩
    $("h2").on('click', function(){
        location.href = '';
    })
})