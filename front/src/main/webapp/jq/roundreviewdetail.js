$(function(){
    //1. 상세내용 보여주기
    // html/roundreviewdetail.html?round_review_board_no=' + round_review_board_no
    let user_nickname = localStorage.getItem("userNickName");
    let currentPage = location.search;
    console.log(currentPage);
    let board_no = location.search.substring(1).split('=')[1];
    console.log(board_no);
    let url = 'http://localhost:1125/backroundreview/board/' + board_no;
    let data = "";
    $.ajax({
        url : url,
        method : "get",
        data : data,
        success : function(jsonObj){
            if (jsonObj.status == 1){
                let roundReview = jsonObj.t;
                $("div.board__no").html(roundReview.roundReviewBoardNo)
                $("div.board__title").html(roundReview.roundReviewBoardTitle);
                //likelist의 usernickname이 현재 살아잇는 nickname하고 같으면 좋아요 true
                //아니면 false(색안입히기)

                $("div.user__nickname").html(roundReview.userNickname);
                $("div.board__dt").html(roundReview.roundReviewBoardDt);
                $("div.board__view-cnt").html(roundReview.roundReviewBoardViewCnt);
                
                $("img.board__images1").attr("src", "../roundreview_images/" + roundReview.roundReviewBoardNo + "/image_1.PNG");
                $("img.board__images2").attr("src", "../roundreview_images/" + roundReview.roundReviewBoardNo + "/image_2.PNG");
                $("img.board__images3").attr("src", "../roundreview_images/" + roundReview.roundReviewBoardNo + "/image_3.PNG");
                // $("img.board__images").attr("src", "https://a.slack-edge.com/production-standard-emoji-assets/14.0/google-medium/1f9e1.png");
                $("div.board__content").html(roundReview.roundReviewBoardContent);
                // $("div.board__map").html(roundReview.roundReviewBoardMap);
            
                let cmtList = roundReview.roundReviewCommentList;

                let $comment = $("div.comment").first();
                $comment.show();
                    
                $("div.comment").not($comment).remove();
                let $commentParent = $comment.parent(); 
                $(cmtList).each(function(index, comment){
                    let $commentCopy = $comment.clone();
                    $commentCopy.find("div.comment-list__no").html(comment.roundReviewCmtNo);
                    $commentCopy.find("div.comment-list__nickname").html(comment.userNickname);
                    $commentCopy.find("div.comment-list>div.comment-list__content").html(comment.roundReviewCmtContent);
                    $commentCopy.find("div.comment-list>div.comment-list__date").html(comment.roundReviewCmtDt);
                    $commentParent.append($commentCopy);
                })
                $("div.comment").first().hide();
                
                //카카오맵
                let latitude = roundReview.roundReviewBoardLatitude;
                let longitude = roundReview.roundReviewBoardLongitude;
                let container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
                let options = { //지도를 생성할 때 필요한 기본 옵션
                center: new kakao.maps.LatLng(latitude, longitude), //지도의 중심좌표.
                level: 3 //지도의 레벨(확대, 축소 정도)
                };
                let map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
            }else {

            }
        },
        error : function(jqXHR){
            alert("에러:" + jqXHR.status);
        }
    })

    //2. 댓글 작성하기
    $("div.comment-box").on("click", "button.comment-box__send", function(){
        let roundReviewBoardNo = board_no;
        let roundReviewCmtContent = $(this).siblings("input[name=comment-box__write]").val();
        let roundReviewCmtParentNo = 0;

        console.log(roundReviewCmtContent);
        // let userNickname = localStorage.getItem("loginedNickname");
        // 테스트용
        let userNickname = "데빌"; 
        $.ajax({
            url : "http://localhost:1125/backroundreview/comment/" + board_no,
            method : "post",
            timeout : 0,
            headers: {
                "Content-Type": "application/json"
            },
            /*
                "roundReviewBoardNo" : "roundReviewBoardNo",
                "roundReviewCmtContent" : "roundReviewCmtContent",
                "roundReviewCmtParentNo" : "roundReviewCmtParentNo",
                "userNickname" : "userNickname",
            */
            //보내줘야할것. boardno, content, parentno, nickname
            data : JSON.stringify({
                roundReviewCmtContent : roundReviewCmtContent,
                roundReviewCmtParentNo : roundReviewCmtParentNo,
                userNickname : userNickname,
                roundReviewBoard : {
                    roundReviewBoardNo : roundReviewBoardNo
                }
            }),
            success : function(){
                location.href = currentPage;
            },
            error: function(jqXHR){
                alert("에러:" + jqXHR.status);
            }
        })
    })
    //3. 댓글 수정하기
    
    $("div.comment div.comment-list").on('click', "button.comment-list__modify", function(){
        let roundReviewCmtNo = $(this).siblings("div.comment-list__no").html();
        console.log(roundReviewCmtNo);
        $.ajax({
            url : "http://localhost:1125/backroundreview/comment/" + roundReviewCmtNo,
            method : 'put',
            timeout : 0,
            header : {
                "Content-Type": "application/json"
            },
            data : JSON.stringify({}),
            success : function(){
                alert("수정성공")
            },
            error : function (jqXHR) {
                alert(
                    "수정 에러: " +
                    jqXHR.status + 
                    ", jqXHR.responseText:" +
                    jqXHR.responseTest
                );
            }

        })
    })


    //4. 댓글 삭제하기 

    //5. 대댓글삭제하기?? 
    
    //6. 좋아요 누르기/해제하기
    
    //7. 이전 버튼 눌렀을 때 이전으로 넘어가기
    $("div.footer").on('click', "button.previous", function(){
        location.href = './roundreviewboardlist.html';
    })
    
    








})