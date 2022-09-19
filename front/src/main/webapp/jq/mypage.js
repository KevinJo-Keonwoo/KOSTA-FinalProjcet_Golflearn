$(function(){

    //1) 레슨이 눌렸을 때 레슨상세페이지로 연결 
    //레슨 내역에서 아무거나 눌러도 연결되게 -> 만약 img같은것에도 
    //일일이 달아줘야하는 경우 설정하기 

    
    //수강상태 설정해주기 
    //수강예정 : 레슨신청일이 존재하나, 레슨일자가 존재하지 않음.
    //수강중 : 레슨일자가 존재함.
    //수강완료 : 레슨일자가 레슨총횟수와 같거나, 레슨만료일>현재날짜이면 수강완료시켜버림 

    //2)수강예정에서 수강취소버튼 눌렀을 시 삭제 --아마 삭제 sql도 구현해야 할 것... 보류 
    //수강중에 수강취소 버튼 눌렀을 시 alert stdt_lsn_status -> 1 
    //종료예정일이 현재날짜보다 지났으면 수강종료
    //LessonStatusUpdateServlet 만들어서 레슨 값 보내주기..? 
    //레슨라인넘버 보내줘야함 

    // let $lsncancelObj = $('input[type=button]');
    // let $lsnstatusObj = $('div.lsnstatus');
    // $lsncancelObj.click(function(){

    // });

/*
    $.ajax({
		url: "http://localhost:1124/back/login",
		success: function (jsonObj) {
		let $tabObj = $("div#content>div#content-right");
		let $tabObjHtml = "";
		if (jsonObj.status == 1) {
			// $('header div#logined').show();

			$tabObjHtml +=
			'<div id="logined"><div id="logout" onclick="logout()">로그아웃</div>';
			$tabObjHtml +=
			'<div id="mypage" onclick="mypage()">마이페이지</div></div>';

		} else {
			// $('header div#normal').show();
			$tabObjHtml +=
			' <div id="normal"><a href="/front/html/login.html">로그인</a>';
			$tabObjHtml +=
			'<a href="/front/html/signuptype.html">회원가입</a></div>';
		}
		$tabObj.html($tabObjHtml);
		
		// return false;
		},
		error: function (jqXHR) {
		alert(jqXHR.status);
		},
	});
    */

   //$(this).val 해서 val값 설정해줄 수 있음 

    //3)레슨내역 주르르르르륵나오게 
    let loginedId = localStorage.getItem("loginedId")
    // let loginedId = "zzeonsh@gmail.com"
    $.ajax({
        url : "http://localhost:1124/back/mypage/student",
        method : 'get',
        data : {userId : loginedId},
        success : function(jsonObj){
            if (jsonObj.status ==1){
                let lsnLineObj = jsonObj.lt;
                // let myStarScore = lsnLineObj.lsn.lsnReview.myStarScore;
                let $lsnLine = $("div.content").first();
                $lsnLine.show();
                $("div.content").not($lsnLine).remove();
                let $lsnLineParent = $lsnLine.parent();
                $(lsnLineObj).each(function(index, lsnLine){
                    let $lsnLineCopy = $lsnLine.clone();

                    $lsnLineCopy.find("img.lsn__image").attr("src", "../lesson_images/" + lsnLine.lsn.lsnNo + ".PNG");
                    $lsnLineCopy.find("div.no").html(lsnLine.lsn.lsnNo);
                    $lsnLineCopy.find("div.title").html(lsnLine.lsn.lsnTitle);
                    $lsnLineCopy.find("div.exp_dt").html(lsnLine.lsnExpDt);
                    $lsnLineCopy.find("div.current__cnt").html(lsnLine.lsnCnt);
                    $lsnLineCopy.find("div.total__cnt").html(lsnLine.lsn.lsnCntSum);

                    // let myStarScore = lsnLine.lsn.lsnReview.myStarScore;
                    // console.log(myStarScore);

                    // let $writeReviewObj = $('input[value=레슨후기작성]');
                    // let $modifyReviewObj = $('input[value=레슨후기수정]');
                    // if (myStarScore = 0) { //작성한 후기가 없으면 수정 버튼숨기기
                    //     $modifyReviewObj.hide();
                    // }else {
                    //     $writeReviewObj.hide();
                    // }
                    // if (myStarScore = 0) { 
                    //     $modifyReviewObj.hide();
                    // }else {
                    //     $writeReviewObj.hide();
                    // }


                    $lsnLineParent.append($lsnLineCopy);
                });
                $("div.content").first().hide();
                
                
                // let myStarScore = lsnLine.lsn.lsnReview.myStarScore;
                // let $writeReviewObj = $('input[value=레슨후기작성]');
                // let $modifyReviewObj = $('input[value=레슨후기수정]');
                // if (myStarScore = 0) { //작성한 후기가 없으면 수정 버튼숨기기
                //     $modifyReviewObj.hide();
                // }else {
                //     $writeReviewObj.hide();
                // }


                /*
                let jsonarr = jsonObj.ll;
                let $lsnObj = $('div.tr');
                $(jsonarr).each(function(i,element){
                    $copyObj = $lsnObj.clone();
                    // $lsnObj.parent.hide();
                    let lsn_line_no = element.lsnLineNo;
                    let lsn_no = element.lsnNo;
                    let lsn_title = element.lsn.lsnTitle;
                    let lsn_exp_dt = element.strLsnExpDt;
                    let stdt_lsn_status = element.stdtLsnStatus;
                    let my_star_score = element.lsnReview.myStarScore;
                    let crnt_lsn_cnt = element.crntLsnCnt;
                    let lsn_cnt_sum = element.lsn.lsnCntSum;
                    
                    if (stdt_lsn_status == 0){
                        stdt_lsn_status = '수강예정';
                    }else if (stdt_lsn_status == 1){
                        stdt_lsn_status = '수강중';
                    }else{
                        stdt_lsn_status = '수강완료';
                    };
                    let lessonLine = '<div class = "no">' + lsn_line_no + '</div>';
                    lessonLine += '<img src = "../lsn_images/' + lsn_no + '_LessonThumbnail.jpg"> '
                    lessonLine += '<div class = "title">' + lsn_title + '</div>'
                    lessonLine += '<div class = "exp_date">레슨유효기간: ' + lsn_exp_dt + '</div>'
                    lessonLine += '<div class = "current_cnt">' + crnt_lsn_cnt + "/" + lsn_cnt_sum + '회</div>'
                    lessonLine += '<input type="button" class = "add_review" value="레슨후기작성">'
                    lessonLine += '<input type="button" class = "modify_review" value="레슨후기수정">'
                    lessonLine += '<input type="button" class = "cancel_lsn" value="수강취소">'

                    $copyObj.find('div.td').html(lessonLine);
                    
                    $('div.table').append($copyObj);

                    let $writeReviewObj = $('input[value=레슨후기작성]');
                    let $modifyReviewObj = $('input[value=레슨후기수정]');
                    if (my_star_score = 0) { //작성한 후기가 없으면 수정 버튼숨기기
                        $modifyReviewObj.hide();
                    }else {
                        $writeReviewObj.hide();
                    }
                    
                });
                */
            }
        },
        error : function(jqXHR){
            alert('오류 : ' + jqXHR.status);
        }
    });

    //레슨상세 가기 -> 실패 
    $('div.td').on('click', 'div.middle>img.lsn__image', function(){
        //레슨번호 찾아오기  //1번완료 후 
        let $lsnNoObj = $(this).siblings("div.no");  //URL종우한테 확인 필요 
        let lsn_no = $lsnNoObj.html(); 
        console.log(lsn_no);
        location.href = "/front/html/viewlesson.html?lsn_no=" + lsn_no;
    });
    //4) 리뷰작성 눌렀을 때 누른 lsn_line_no, status 0 보내주기 
    // let $addReviewObj = $('input.add_review');
    
    let $lsnListObj = $('div.table');
    $lsnListObj.on('click', 'input[value=레슨후기작성]', function(){
        let $lsnLineNoObj = $(this).parent().find('div.no');
        let lsn_line_no = $lsnLineNoObj.html();
        location.href = "../html/review.html?reviewCnt=0&lsn_line_no=" + lsn_line_no;
    });
    
    // <input type="button" class = "add_review" value="레슨후기작성">
    // <input type="button" class = "modify_review" value="레슨후기수정">
    // <input type="button" class = "cancel_lsn" value="수강취소버튼"></input>

    //5) 수정 눌렀을 때 누른 lsn_line_no, status 1 보내주기 
        // location.href = "/front/html/viewproduct.html?prod_no=" + prod_no;
    // let $modifyReviewObj = $('input.modify_review');
    let $lsnListModiObj = $('div.table');
    $lsnListModiObj.on('click', 'input[value=레슨후기수정]', function(){
        let $lsnLineNoObj = $(this).parent().find('div.no');
        let lsn_line_no = $lsnLineNoObj.html();
        location.href = "../html/review.html?reviewCnt=1&lsn_line_no=" + lsn_line_no;
    });
})