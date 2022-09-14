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
          let $board = $('li.board-container').first();
          //나머지 게시글 div는 삭제한다
          $('li.board-container').not($board).remove();

          let $boardParent = $board.parent();
          $(pageBeanObj.list).each(function (index, board) {
            let $boardCopy = $board.clone();//복제본
            $boardCopy.find("span.board-no").html(board.meetBoardNo);
            $boardCopy.find("span.board-ctg").html(board.meetCategory.meetCtgTitle);
            $boardCopy.find("span.board-nickname").html(board.userNickname);
            $boardCopy.find("h3.board-title").html(board.meetBoardTitle);
            $boardCopy.find("span.board-dt").html(board.meetBoardDt);
            $boardCopy.find("span.board-meet_dt").html(board.meetBoardMeetDt);
            $boardCopy.find("span.board-cur_cnt").html(board.meetBaordCurCnt);
            $boardCopy.find("span.board-max_cnt").html(board.meetBaordCurCnt);
            $boardCopy.find("span.board-location").html(board.meetBoardLocation);
            //내용은 앞에서 30자만 보이기
            let meetBoardContent = board.meetBoardContent.substr(0, 30) + "...";
            $boardCopy.find("p.board__body-content").html(meetBoardContent);

            //모집유형은 숫자값(0,1)므로 한글로 변경하여 넣기 
            if (board.meetBoardStatus == 0) {
              $boardCopy.find("span.board-status").html("모집중");
            } else {
              $boardCopy.find("span.board-status").html("모집완료");
            }
            $boardParent.append($boardCopy);
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
        alert("에러:" + jqXHR.status);
      }
    });
  }
   //---------페이징처리 END---------

  //---페이지 로드되자 마자 게시글1페이지 검색 START---
  showList('http://localhost:1129/backmeet/board/list');
  //---페이지 로드되자 마자 게시글1페이지 검색 END---

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
    let word = $('input[name=word]').val().trim();
    let url = '';
    let data = '';

    //------모집상태별 필터 값 유지 START---------
    var status = $("input[name='search__filter-status']:checked").val();
    $("ul.search__filter-list>li").each(function (index, element) {
      let $inputObj = $(element).find('input');
      if ($inputObj.css("background-color") == 'rgb(255, 0, 0)') {
        console.log(index, "color", $inputObj.css("background-color"));
        status = index - 1;
        return false;
      }
    });
    //-----모집상태별 필터 값 유지 END---------

    if (!status && word == '') {//모집상태 필터와 검색어가 없으면
      url = 'http://localhost:1129/backmeet/board/list/' + pageNo;
      data = 'currentPage=' + pageNo;
    } else if (word.length > 0) {//검색어가 있으면
      url = 'http://localhost:1129/backmeet/board/search/' + word + '/' + pageNo;
      data = 'word=' + word + '&currentPage=' + pageNo;
    } else {//모집상태 필터를 체크하면
      console.log(status);
      url = 'http://localhost:1129/backmeet/board/filter/' + status + '/' + pageNo;
      data = 'meetBoardStatus=' + status + '&currentPage=' + pageNo;
    }
    console.log(url);
    showList(url, data);
    return false;
  });
  //---페이지 그룹의 페이지를 클릭 END---

  //----모집상태별 필터 클릭 START----
  $("input[type='checkbox']").click(function () {
    var status = $("input[name='search__filter-status']:checked").val();
    console.log(status);
    let url = '';
    let data = '';
    if (status == "2") {//전체를 클릭했을 때
      url = 'http://localhost:1129/backmeet/board/list';
      data = "";
    } else {
      url = 'http://localhost:1129/backmeet/board/filter/' + status;
      data = "meetBoardStatus" + status;
    }

    $("input[type='checkbox']").css("background-color", "yellow"); //기본
    $(this).css("background-color", "red"); //체크된 경우
    console.log(url);
    showList(url, data);
  });
  //----모집상태별 필터 클릭 END----

  //---검색 클릭 START---
  $('button[name=board-search]').click(function () {
    let word = $('input[name=word]').val().trim();
    let url = 'http://localhost:1129/backmeet/board/search/' + word;
    let data = 'word=' + word;
    showList(url, data);
    return false;
  });
  //---검색 클릭 END---

  //----글쓰기 버튼 클릭 START----
  $('button[name=write-button]').click(function () {
    $(location).attr('href', '../html/meetboardwrite.html');
  })
  //----글쓰기 버튼 클릭 END----

  //----게시글 클릭 START-----
  // $("a").on('click', function () {
  //   let $meetBoardNoObj = $(this).prev().find('meetBoardNo');
  //   let meet_board_no = $meetBoardNoObj.html();
  //   console.log(meet_board_no);
  //   // location.href = "/front/html/meetboarddetail" + meet_board_no;
  // })
  //----게시글 클릭 END-----

});

