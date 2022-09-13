$(function () {
    //로딩 되자마자 글 목록 불러오기(1 페이지)
    // let url = "http://localhost:1126/backresale/resale/board/list/1";
    let queryString = location.search.split("=")[1];
    console.log(queryString);
    let url = "http://localhost:1128/noticeboard/notice/" + queryString;
    let src = "../notice_images/";
    let boardNo = 0;
    $.ajax({
        url: url,
        method: "GET",
        success: function (jsonObj) {
        console.log("----" + jsonObj.t.noticeBoardLikeCnt);
        if (jsonObj.status == 1) {
            let noticeNo = jsonObj.t.noticeBoardNo;
            $("div.board__content__title").html(
            "제목 : " + jsonObj.t.noticeBoardTitle
            );
            $("div.board__content__date").html(jsonObj.t.noticeBoardDt);
            $("div.board__content__view_cnt").html(jsonObj.t.noticeBoardViewCnt);
            $("div.board__content__like_cnt").html(jsonObj.t.noticeBoardLikeCnt);
            $("div.board__content__thumbnail>img").attr(
            "src",
            src + noticeNo + "_image_" + "pyeonan.png"
            );
            $("div.board__content").html(jsonObj.t.noticeBoardContent);

            let commentList = jsonObj.t.noticeCommentList;
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
            boardNo = noticeNo;
            // $("div.comment-list").html(jsonObj.t.noticeCommentList);
        }
        },
    });

    $("div.board-like").on("click", function () {
        let url = "http://localhost:1128/noticeboard/notice/like/add";
        console.log(boardNo);
        $.ajax({
            url: url,
            method: "post",
            contentType: "application/json",
            data: JSON.stringify({"noticeBoardNo": boardNo}), // userNickname 받아와야함
            success: function (jsonObj) {
                console.log("좋아요 성공");
                location.reload();
            },
        });
    });
});
