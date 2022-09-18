$(function () {
    // let loginedId = 'id1'; //테스트용 ,back요청X

    function showList(url, data) {//게시글&페이지그룹에서 페이지클릭으로&검색버튼클릭으로 목록얻기
        $.ajax({
            url: url,
            method: 'get',
            data: data,
            success: function (jsonObj) {
                if (jsonObj.status == 1) {
                    let pageBeanObj = jsonObj.t;
                    //게시글 div를 원본으로 한다. 복제본만든다
                    let $lesson = $("div.lessonlist__content").first();
                    //나머지 게시글 div는 삭제한다
                    $("div.lessonlist__content").not($lesson).remove();
                    let $lessonParent = $lesson.parent();

                    $(pageBeanObj.list).each(function (index, lesson) {
                        let $lessonCopy = $lesson.clone();

                        $lessonCopy.find("div.lesson-list__content__no").html(lesson.lsnNo);
                        $lessonCopy.find("div.lesson-list__content__user-id").html(lesson.userInfo.userId);
                        $lessonCopy.find("div.lesson-list__content__user-name").html(lesson.userInfo.userName);
                        $lessonCopy.find("div.lesson-list__content__title").html(lesson.lsnTitle);
                        $lessonCopy.find("div.lesson-list__content__dt").html(lesson.lsnReqDt);

                        //모집유형은 숫자값(0,1)므로 한글로 변경하여 넣기 
                        if (lesson.meetBoardStatus == 2) {//승인대기중인 경우-2
                            $lessonCopy.find("div.lesson-list__content__status").html("승인대기중");
                        } else if (lesson.meetBoardStatus == 3) {//반려인 경우 -3
                            $lessonCopy.find("div.lesson-list__content__status").html("반려");
                        } else {//승인완료인 경우 - 0 또는 1
                            $lessonCopy.find("div.lesson-list__content__status").html("승인완료");
                        }

                        $lessonParent.append($lessonCopy);

                    });

                    //---------페이징처리 START---------
                    let $pagegroup = $('div.pagegroup')
                    let $pagegroupHtml = '';
                    if (pageBeanObj.startPage > 1) {
                        $pagegroupHtml += '<span class="prev">PREV</span>';
                    }
                    for (let i = pageBeanObj.startPage; i <= pageBeanObj.endPage; i++) {
                        $pagegroupHtml += '&nbsp;&nbsp;';
                        if (pageBeanObj.currentPage == i) { //현재페이지인 경우 <span>태그 안만듦
                            // $pagegroupHtml += i;
                            $pagegroupHtml += '<span class="disabled">' + i + '</span>';//css에서 not함수를 사용하여 구분
                        } else {
                            $pagegroupHtml += '<span>' + i + '</span>';
                        }
                    }
                    if (pageBeanObj.endPage < pageBeanObj.totalPage) {
                        $pagegroupHtml += '&nbsp;&nbsp;';
                        $pagegroupHtml += '<span class="next">NEXT</span>';
                    }

                    $pagegroup.html($pagegroupHtml);
                } else {
                    alert(jsonObj.msg);
                }
            },
            error: function (jqXHR) {
                alert("에러:" + jqXHR.responseText);
            }
        });
        return false;
    }
    //---------페이징처리 END---------

    //---페이지 로드되자 마자 승인대기상태의 레슨리스트의 1페이지 검색 START---
    showList('http://localhost:1124/back/admin/approval/1');
    //---페이지 로드되자 마자 승인대기상태의 레슨리스트의 1페이지 검색 END---

    //---페이지 그룹의 페이지를 클릭 START---
    $('main>section>div.pagegroup').on('click', 'span:not(.disabled)', function () {//아래 if조건 대신 not 선택자 사용 
        let pageNo = 1;
        if ($(this).hasClass('prev')) {
            pageNo = parseInt($(this).next().html()) - 1;
        } else if ($(this).hasClass('next')) {
            pageNo = parseInt($(this).prev().html()) + 1;
        } else {
            pageNo = parseInt($(this).html());
        }
        // alert("보려는 페이지번호: " + pageNo);
        let url = '';
        let data = '';

        //------레슨상태 필터 값 유지 START---------
        let lsnStatus = 2;
        lsnStaus = $("input[name='search__filter-status']:checked").val();
        //레슨상태 필터를 선택하지 않은 경우 승인대기(2)가 default
        console.log(lsnStatus);
        $("ul.search__filter-list").each(function (index, element) {
            let $inputObj = $(element).find('input');
            if ($inputObj.css("background-color") == 'rgb(255, 0, 0)') {
                console.log(index, "color", $inputObj.css("background-color"));
                lsnStatus = index - 1;//반려3, 승인 0, 승인대기2
                console.log(lsnStatus);
            }
        });
        //-----승인상태별 필터 값 유지 END---------
            url = 'http://localhost:1124/back/admin/approval/' + lsnStatus + '/'+ pageNo;
            data = 'lsnStatus=' + lsnStatus + '&currentPage=' + pageNo;
        console.log(url);
        showList(url, data);
    });
    //---페이지 그룹의 페이지를 클릭 END---

    // //----승인상태별 필터 클릭 START----
    $("input[type='checkbox']").click(function () {
        var status = $("input[name='search__filter-status']:checked").val();
        console.log(status);
        let url = '';
        let data = '';
        if (status == "2") {//전체를 클릭했을 때
            url = 'http://localhost:1129/backmeet/meet/board/list';
            data = "";
        } else {
            url = 'http://localhost:1129/backmeet/meet/board/filter/' + status;
            data = "meetBoardStatus" + status;
        }

        $("input[type='checkbox']").css("background-color", "yellow"); //기본
        $(this).css("background-color", "red"); //체크된 경우

        console.log(url);
        showList(url, data);
    });
    // //----승인상태별 필터 클릭 END----

});

