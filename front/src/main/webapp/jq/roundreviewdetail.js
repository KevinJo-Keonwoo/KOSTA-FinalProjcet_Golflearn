$(function(){
    // html/roundreviewdetail.html?round_review_board_no=' + round_review_board_no
    let user_nickname = localStorage.getItem("userNickName");
    let test = location.search;
    console.log(test);
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
                
                // $("div.board__images").attr("src", "../roundreview_images/" + roundReview.roundReviewBoardNo + "/image_1.PNG");
                $("div.board__images").attr("src", "https://a.slack-edge.com/production-standard-emoji-assets/14.0/google-medium/1f9e1.png");
                $("div.board__content").html(roundReview.roundReviewBoardContent);
                // $("div.board__map").html(roundReview.roundReviewBoardMap);
            
                let cmtList = roundReview.roundReviewCommentList;

                let $comment = $("div.comment").first();
                $comment.show();
                    
                $("div.comment").not($comment).remove();
                let $commentParent = $comment.parent(); 
                $(cmtList).each(function(index, comment){
                    let $commentCopy = $comment.clone();
                    $commentCopy.find("div.comment-list__nickname").html(comment.userNickname);
                    $commentCopy.find("div.comment-list>div.comment-list__content").html(comment.roundReviewCmtContent);
                    $commentCopy.find("div.comment-list>div.comment-list__date").html(comment.roundReviewCmtDt);
                    $commentParent.append($commentCopy);
                })
                $("div.comment").first().hide();
            }else {

            }
        },
        error : function(jqXHR){
            alert("에러:" + jqXHR.status);
        }
    })
})