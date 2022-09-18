$(function () {
    let queryString = location.search.split("=")[1];
    // let queryString = 1;
    console.log(queryString);
    let url = "http://localhost:1129/backmeet/meet/board/" + queryString;

    let loginedNickname = localStorage.getItem("loginedNickname");
    // console.log(loginedNickname);

    //-----summernote 실행 START-----
    $('#summernote').summernote({
        height: 200, // 에디터 높이
        minHeight: null, // 최소 높이
        maxHeight: null, // 최대 높이
        focus: true, // 에디터 로딩후 포커스를 맞출지 여부
        lang: "ko-KR", // 한글 설정
        disableResizeEditor: true, // 크기 조절 기능 삭제
        fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New', 'Helvetica neue', 'Helvetica', 'Impact', 'Lucida Grande', 'Tahoma', 'Times New Roman', 'Verdana', 'Tahoma', 'Courier New', '맑은 고딕', '굴림', '돋움'],
        fontNamesIgnoreCheck: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New', 'Helvetica neue', 'Helvetica', 'Impact', 'Lucida Grande', 'Tahoma', 'Times New Roman', 'Verdana', 'Tahoma', 'Courier New', '맑은 고딕', '굴림', '돋움'],
        toolbar: [
            ['fontname', ['fontname']],
            ['fontsize', ['fontsize']],
            ['style', ['bold', 'italic', 'underline', 'clear']],
            ['color', ['color']],
            ['table', ['table']],
            ['para', ['paragraph']],
            ['insert', ['link']],
            ['view', []]
        ],
        fontSizes: ['8', '9', '10', '11', '12', '14', '16', '18', '20', '22', '24', '28', '30', '36', '50', '72'],
    });
    //-----summernote 실행 END-----

    //-----게시글 내용 불러오기 START-----
    $.ajax({
        url: url,
        method: "GET",
        success: function (jsonObj) {
            if (jsonObj.status == 1) {
                $('input[name=meetBoardTitle').attr('value', jsonObj.t.meetBoardTitle);//제목o
                $("#summernote").summernote("code", jsonObj.t.meetBoardContent);

                //날짝 값 형변환
                meetBoardMeetDt = 20 + jsonObj.t.meetBoardMeetDt.replaceAll('/', '-');
                $("input[name='meetBoardMeetDt']").attr('value', meetBoardMeetDt);//날짜
                $("select[name=meetBoardMaxCnt]").val(jsonObj.t.meetBoardMaxCnt).prop("selected", true);//인원
                $('input[name=meetBoardLocation]').attr('value', jsonObj.t.meetBoardLocation);//장소
                $("select[name='meetCategory']").val(jsonObj.t.meetCategory.meetCtgNo).prop("selected", true);//유형

            } else {
                return false;
            }
        },
    });
    //-----게시글 내용 불러오기 END-----

    //-----게시글 입력 내용 수정하기 START-----
    let $btObj = $("button[name='btn-modify']");
    $btObj.click(function () {//수정버튼 클릭시
        if (!confirm('수정하시겠습니까?')) {
            return false;
        } else {
            let text = $('#summernote').summernote('code');

            let meetBoardTitle = $('input[name=meetBoardTitle').val();
            let meetBoardMeetDt = $('input[name=meetBoardMeetDt]').val();
            let meetBoardMaxCnt = $('select[name=meetBoardMaxCnt]').val();
            let meetBoardLocation = $('input[name=meetBoardLocation]').val()
            let meetBoardContent = text;
            meetBoardMeetDt = meetBoardMeetDt.replaceAll('-', '/'); //날짜값 형태 변환 : 22/09/14
            let meetCtgNo = $('select[name=meetCategory]').val();

            let data = {
                "userNickname": loginedNickname,
                "meetCategory": { "meetCtgNo": meetCtgNo },
                "meetBoardTitle": meetBoardTitle,
                "meetBoardMeetDt": meetBoardMeetDt,
                "meetBoardMaxCnt": meetBoardMaxCnt,
                "meetBoardLocation": meetBoardLocation,
                "meetBoardContent": meetBoardContent
            };
            var jsonData = JSON.stringify(data);
            console.log(jsonData);
            let url = "http://localhost:1129/backmeet/meet/board/" + queryString
            $.ajax({
                url: url,
                method: "put",
                data: jsonData,
                headers: {
                    "content-Type": "application/json",
                },
                success: (jsonObj) => {
                    alert("글이 수정되었습니다");
                    location.href = '../html/meetboarddetail.html?meet_board_no=' + queryString;
                },
                error: function (jqXHR) {//응답실패
                    alert(jqXHR.responseText);
                }
            });
            return false;
        }
    });
    //-----게시글 입력 내용 수정하기 END-----

    //----취소버튼 클릭하가 START----
    let $btCancelObj = $("button[name='btn-cancel']");
    $btCancelObj.click(function () {//취소버튼 클릭시
        if (!confirm('취소하시겠습니까?\n 취소하시면 지금까지 작성하신 내용은 저장되지 않습니다.')) {
            return false;
        } else {
            window.location = '../html/meetboarddetail.html?meet_board_no=' + queryString;
        }
    });
    //----취소버튼 클릭하가 END----

});