$(function () {
    //로딩 되자마자 글 목록 불러오기(1 페이지)
    // let url = "http://localhost:1126/backresale/resale/board/list/1";
    let queryString = location.search.split("=")[1];
    console.log(queryString);
    let url = "http://localhost:1128/noticeboard/notice/" + queryString;
    let src = "../notice_images/";
    let boardNo = 0;
    let userNickname = "";
    $.ajax({
        url: url,
        method: "GET",
        success: function (jsonObj) {
        console.log("----" + jsonObj.t.noticeBoardLikeCnt);
        if (jsonObj.status == 1) {
            userNickname = jsonObj.t.userNickname;
            console.log("user="+ userNickname);
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
            let $commentParent = $("div.comment-list");

            let $comment = $("div.comment-content").first();
            $("div.comment-content").not($comment).remove();

            $(commentList).each(function (index, comment) {
            console.log(comment);
            let $commentCopy = $comment.clone();
            $commentCopy
                .find("div.comment-list__cmtNo")
                .html(comment.noticeCmtNo);
            $commentCopy
                .find("div.comment-list__nickname")
                .html(comment.userNickname);
            $commentCopy
                .find("div.comment-list__content")
                .html(comment.noticeCmtContent);
            $commentCopy.find("div.comment-list__date").html(comment.noticeCmtDt);

            let cmt = "<div class='comment-list__content__modify'><input type='text'><button name='cmtModify'>수정</button><button name='cmtDelete'>댓글삭제</button></div>";

            // $commentCopy.append("< class='comment-list__content__modify'>");
            // $commentCopy.append("<input type='text'>");
            // $commentCopy.append("<button name='cmtModify'>수정</button>");
            // $commentCopy.append("<button name='cmtDelete'>댓글삭제</button>");
            $commentCopy.append(cmt);

            $commentParent.append($commentCopy);
            });
            $comment.remove();
            boardNo = noticeNo;
            // $("div.comment-list").html(jsonObj.t.noticeCommentList);
        }}
        // error 작성바람
    });
    // return false;

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
            // error 작성바람
        });
        return false;
    });

    //댓글 작성
    $("div.comment-write>button").on("click", function () {
        let url = "http://localhost:1128/noticeboard/notice/comment/write";

        let cmt = $('div.comment-write>input').val();
		
        let data = {
            'noticeCmtContent':cmt,
            'noticeBoardDto':{"noticeBoardNo":boardNo},
            'noticeCmtParentNo':0
        }

        $.ajax({
            url: url,
            method: "post",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data), // userNickname 받아와야함
            success: function (jsonObj) {
                console.log(data);
                location.reload();
            },
            // error 작성바람
            
        });
        return false;
    });

    //댓글 수정
    // $("button.cmtModify").on("click",
    $("div.comment-list").on("click", 
    "div.comment-content>div.comment-list__content__modify> button[name='cmtModify']", 
    function(){
        let cmtNo = $(this).parent().parent().find("div.comment-list__cmtNo").html();

        let url = "http://localhost:1128/noticeboard/notice/comment/"+cmtNo;
        console.log(url);
        let cmtContent = $(this).parent().find("input").val();
        let data = {
            "noticeCmtNo": cmtNo,
            "noticeCmtContent": cmtContent,
            "userNickname":"id1",
            "noticeBoardDto" : {"noticeBoardNo" : boardNo}
        };

        $.ajax({
            url: url,
            method: "PUT",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data),
            success: function (jsonObj) {
                location.reload();
            },
            error: function (jsonObj) {
            }
        });
        return false;
    });

    // 댓글삭제
    // list 안에 content여러개 생성됨, 생성이후에 each 로 클릭한 댓글번호를 가져와야 함.
    $("div.comment-list").on("click", 
    "div.comment-content>div.comment-list__content__modify> button[name='cmtDelete']", 
    function(){
        let cmtNo = $(this).parent().parent().find("div.comment-list__cmtNo").html();
        let url = "http://localhost:1128/noticeboard/notice/comment/" + cmtNo;

        $.ajax({
            url: url,
            method: "DELETE",
            success: function(jsonObj) {
                location.reload();
            },
            error: function(jqXHR) {

            }
        })
    });
    
});
