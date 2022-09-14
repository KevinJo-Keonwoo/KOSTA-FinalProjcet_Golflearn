$(function () {
    // 로딩되자마자 글 불러오기
    let queryString = location.search.split("=")[1].split("%")[0];
    console.log("쿼리스트링값 " + queryString);

    let src = "../resale_images/";
    let resaleBoardNo = 0;
    let userNickname = "";
    let likeNickname = "";
    let likeNo = 0;
    let likedNickname="";
    $.ajax({
        url: "http://localhost:1126/backresale/resale/board/" + queryString,
        method: "get",
        success: function (jsonObj) {
            console.log("좋아요 수 : " + jsonObj.t.resaleBoardLikeCnt);
            console.log("게시판 번호 : " + jsonObj.t.resaleBoardNo);
            let detailObj = jsonObj.t;
            resaleBoardNo = jsonObj.t.resaleBoardNo;
            userNickname = jsonObj.t.userNickname;
            
            if (jsonObj.status == 1) {
            // function zeroFill(number, lenght){ //number : 숫자 lenght:자릿수
            //     let output = number.toString(); //숫자를 문자로 변환합니다.
            //     while(output.length < lenght ){ //숫자의 길이가 전체 자리수보다 작으면
            //         output = '0'+output; //숫자앞에 '0'을 붙여줍니다.
            //     return output;
            //     }
            // }
            // for(var i=1; i<=5; i++){
            // $('div.board__content__images').append('<img src="image/IMG_' + zeroFill(i, 2) + '.JPG">');
            // }

                $("div.board__content__images>img").attr(
                "src",
                src + detailObj.resaleBoardNo + "/image_1.jpg"
                );
                $("div.board__content__images>img").attr(
                "src",
                src + detailObj.resaleBoardNo + "/image_2.jpg"
                );
                // $("div.board__content__images>img").attr("src", src+ detailObj.resaleBoardNo +"/image_3.jpg");
                $("div.board__content__title").html(
                "제목 : " + detailObj.resaleBoardTitle
                );
                $("div.board__content__date").html(
                "작성일 : " + detailObj.resaleBoardDt
                );
                $("div.board__content__view_cnt").html(
                "조회수 : " + detailObj.resaleBoardViewCnt
                );
                $("div.board__content__view_cnt").html(
                "댓글수 : " + detailObj.resaleBoardCmtCnt
                );
                $("div.board__content__like_cnt").html(
                "좋아요수 : " + detailObj.resaleBoardLikeCnt
                );
                $("div.board__content").html(detailObj.resaleBoardContent);
                //-------------------------------------

                let commentObj = jsonObj.t.resaleComment;
                // let
                console.log(commentObj);
                let $commnetParent = $("div.comment-list");
                // 원본 하나 선택 후 나머지 댓글의 div 삭제
                let $comment = $("div.comment-content").first();
                $("div.comment-content").not($comment).remove();

                $(commentObj).each(function (index, comment) {
                let $commentCopy = $comment.clone();
                // console.log(comment);
                $commentCopy
                    .find("div.comment-list__nickname")
                    .html("닉네임 - " + comment.userNickname);
                $commentCopy
                    .find("div.comment-list__content")
                    .html("댓글 - " + comment.resaleCmtContent);
                $commentCopy
                    .find("div.comment-list__date")
                    .html("작성일 - " + comment.resaleCmtDt);
                $commentCopy
                    .find("div.comment-list__parentno")
                    .html("부모댓글번호 - " + comment.resaleCmtParentNo);

                $commnetParent.append($commentCopy);
                }); // each

                $comment.remove();
                let likeObj = jsonObj.t.resaleLike;
                // console.log(likeObj);

                $.each(likeObj, function (i, like) {
                    console.log(like);
                    likeNickname = like.userNickname;
                    console.log("좋아요 한 사람들 " + likeNickname);
                    if (likeNickname == localStorage.getItem("loginedNickname")) {
                        //localStorage.getItem("loginedNickname")
                        // 세션 아이디와 좋아요 한 닉네임이 같으면
                        likeNo = like.resaleLikeNo;
                        likedNickname = likeNickname;
                        console.log(likedNickname);
                        console.log(likeNo);
                    } // each 의 if문
                });
        } // if문

        }, // success
        error: function (jsonObj) {
        alert(jsonObj.msg);
        }, // error
    }); // ajax

    // 좋아요 클릭
    $("div.board-like").on("click", function () {
        console.log("보드 넘버는" + resaleBoardNo);
        // 좋아요 여부
        console.log("좋아요한 닉넴" + likedNickname);
        console.log("좋아요 번호 " + likeNo);
        if (likedNickname == localStorage.getItem("loginedNickname")) { // localStorage.getItem("loginedNickname")
        // 세션 아이디와 좋아요 한 닉네임이 같으면
        // 좋아요 삭제
            let obj = { resaleLikeNo: likeNo,
                userNickname : likedNickname,
                resaleBoard : {resaleBoardNo : resaleBoardNo}
        };
        $.ajax({
            url:"http://localhost:1126/backresale/resale/like/" + likeNo,
            method:"delete",
            contentType: "application/json",
            data:JSON.stringify(obj),
            success: function(jsonObj){
                if(jsonObj.status == 1){
                    alert(jsonObj.msg);
                }
            },
            error: function (jqXHR) {
                alert(jqXHR.status + ":" + "좋아요 삭제 실패");
            }
        });
        } else {
            // 세션 아이디와 좋아요한 닉네임이 같지 않으면
            let obj = { resaleBoardNo: resaleBoardNo };
            //좋아요 클릭
            $.ajax({
                url: "http://localhost:1126/backresale/resale/like/add",
                method: "post",
                contentType: "application/json",
                data: JSON.stringify(obj),
                success: function (jsonObj) {
                    if ((jsonObj.status = 1)) {
                    alert("좋아요 추가 성공");
                    }
                },
                error: function (jqXHR) {
                    alert(jqXHR.status + ":" + "좋아요 추가 실패");
                }//error
            }); // ajax
        } //else 끝
    }); //클릭 끝

    // 댓글 작성
    // 댓글 삭제

    // 게시글 수정(미완)
    $("button.bt__modify").on("click", function () {
        // alert("수정");
        let boardNo = resaleBoardNo;
        // console.log("게시글번호는" + boardNo);
        alert("게시글번호는" + boardNo);
        let nickName = userNickname;
        // console.log("작성자 닉네임은" + nickName);
        alert("작성자 닉네임은" + nickName);
        location.href = "../html/resaleboardwrite.html?resaleBoardNo=" + boardNo;
    });

    // 게시글 삭제
    $("button.bt__delete").on("click", function () {
        // alert("삭제");
        let boardNo = resaleBoardNo;
        let nickname = userNickname;
        console.log("삭제할 글번호 : " + boardNo);
        console.log("닉네임은 : " + nickname);

        let obj = { resaleBoardNo: boardNo, userNickname: nickname };
        // $.ajax({
        // url: "http://localhost:1126/backresale/resale/board/" + boardNo,
        // method: "delete",
        // contentType: "application/json",
        // data: JSON.stringify(obj),
        // success: function () {
        //     alert("게시글 삭제 성공");
        //     location.href("../html/resaleborardlist.html");
        // },
        // error: function (jqXHR) {
        //     alert("error : " + jqXHR.status + " : " + "게시글 삭제 실패");
        // },
        // });
    });
}); // 첫 function
