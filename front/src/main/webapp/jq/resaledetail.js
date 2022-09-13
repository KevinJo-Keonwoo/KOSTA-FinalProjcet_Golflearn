$(function() {

    // 로딩되자마자 글 불러오기
    let queryString = location.search.split("=")[1];
    console.log(queryString);
    
    let src = "../resale_images/" + queryString;
    let boardNo = 0;
    $.ajax({
        url: "http://localhost:1126/backresale/resale/board/" + queryString,
        method : "get",
        success:function(jsonObj){
            console.log(jsonObj.t);
            if(jsonObj.status ==1) {
                let detailObj = jsonObj.t;
                $("div.board__content__title").html("제목 : " + detailObj.resaleBoardTitle);
                $("div.board__content__date").html(detailObj.resaleBoardDt);
                $("div.board__content__view_cnt").html(detailObj.resaleBoardViewCnt);
                $("div.board__content__like_cnt").html(detailObj.resaleBoardLikeCnt);
                $("div>img.board-list__content__thumbnail").attr("src", src+ board.resaleBoardNo +"/s_image_1" +".jpg");
                $("div.board__content").html(jsonObj.t.noticeBoardContent);    
                
                let commentList = jsonObj.t.resaleComment;
                let $commnetParent = $("div.comment-list");

                let $comment = $("div.comment-content").first();
                $("div.comment-content").not($comment).remove();

                $(commentList).each(function (index, comment) {
                console.log(comment);
                let $commentCopy = $comment.clone();
                $commentCopy
                    .find("div.comment-list__nickname")
                    .html(comment.userNickname);
                $commentCopy
                    .find("div.comment-list__content")
                    .html(comment.noticeCmtContent);
                $commentCopy.find("div.comment-list__date").html(comment.noticeCmtDt);
                $commnetParent.append($commentCopy);
                });
                $comment.remove();
                boardNo = resaleNo;
                // $("div.comment-list").html(jsonObj.t.noticeCommentList);
        }

            } // if문    
        }// success

        });
    




});// 첫 function