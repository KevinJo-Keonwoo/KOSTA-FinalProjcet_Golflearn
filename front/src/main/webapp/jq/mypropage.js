$(function(){
    /* 임시주석
    $.ajax({
		url: "http://localhost:1124/back/login",
		success: function (jsonObj) {
		let $tabObj = $("div#content>div#content-right");
		let $tabObjHtml = "";
		console.log("tetetetete" + jsonObj.type);
		//if (jsonObj.status == 1) {
			console.log("hihiho");
			$tabObjHtml += '<div id="logined"><div id="logout" onclick="logout()">로그아웃</div>';
			$tabObjHtml += '<div id="addlsn"><a id="mypage" href="/front/html/addlesson.html">레슨등록</a></div>';
			$tabObjHtml += '<div id="mypage" onclick="mypage()">마이페이지</div></div>';

		//} else {
			// $('header div#normal').show();
			//$tabObjHtml +=
			//' <div id="normal"><a href="/front/html/login.html">로그인</a>';
			//$tabObjHtml +=
			//'<a href="/front/html/signuptype.html">회원가입</a></div>';
		//}
		$tabObj.html($tabObjHtml);
		
		// return false;
		},
		error: function (jqXHR) {
		alert(jqXHR.status);
		},
	});
    */

    let loginedId = localStorage.getItem("loginedId")
    // let loginedId = "ohpro@gmail.com"
    //1)페이지 로딩되었을 때 프로면 레슨 보여주기...
    $.ajax({
        url : "http://localhost:1124/back/mypage/pro",
        method : 'get',
        data : {userId : loginedId},
        success : function(jsonObj){
            if (jsonObj.status == 1){
                let lsnObj = jsonObj.lt;
                let $lsn = $("div.content").first();
                $lsn.show();
                $("div.content").not($lsn).remove();
                let $lsnParent = $lsn.parent();
                $(lsnObj).each(function(index, lsn){
                    let $lsnCopy = $lsn.clone();

                    $lsnCopy.find("div.lsn__no").html(lsn.lsnNo);
                    $lsnCopy.find("img.lsn__image").attr("src", "../lesson_images/" + lsn.lsnNo + ".PNG");
                    $lsnCopy.find("div.lsn__title").html(lsn.lsnTitle);
                    let lsn_status = lsn.lsnStatus;
                    
                    $resumeObj = $lsnCopy.find("button.lsn__resume");
                    $closeObj = $lsnCopy.find("button.lsn__close");
                    $waitObj = $lsnCopy.find("button.lsn__wait");
                    $rejectObj = $lsnCopy.find("button.lsn__reject");
                    if(lsn_status == 0){ 
                        $closeObj.hide();
                        $waitObj.hide();
                        $rejectObj.hide();
                    } else if (lsn_status == 1){
                        $resumeObj.hide();
                        $waitObj.hide();
                        $rejectObj.hide();
                    } else if (lsn_status == 2){
                        $resumeObj.hide();
                        $closeObj.hide();
                        $rejectObj.hide();
                    } else if (lsn_status == 3){
                        $resumeObj.hide();
                        $closeObj.hide();
                        $waitObj.hide();
                    } 
                    
                    // console.log(lsn_status);
                    // if (lsn_status = 0) {
                    // }else if(lsn_status = 1){
                    // }else {
                    // };
                    // $lsnCopy.find("div.status").html(lsn.lsnStatus);

                    $lsnParent.append($lsnCopy);
                });
                $("div.content").first().hide();

            /*
                $(jsonObj).each(function(i,element){

                    // $copyObj = $lsnObj.clone();
                    // $lsnObj.hide();
                    
                    let lsn_no = element.lsnNo;
                    let lsn_title = element.lsnTitle;
                    let lsn_status = element.lsnStatus;
                    
                    // let lsns = '<div class = "context">';
                    // lsns += '<div class = "no">레슨번호: <span class = "no">' + lsn_no + '</span></div>';
                    // lsns += '<img src = "../lsn_images/' + lsn_no +'.jpg" alt="' + lsn_no + '번째레슨">';
                    // lsns += '<div class = "title">' + lsn_title + '</div>';
                    // // lsns += '<div class = "expdate">종료일자</div>';
                    // // lsns += '<div class = "crnlsncnt">레슨진행횟수</div>';
                    // // lsns += '<div class = "lsncntsum">총레슨횟수</div>';
                    // lsns += '<input type="button" value="수강생관리">';  //미구현
                    // lsns += '<input type="button" value="레슨종료">';  //미구현
                    // lsns += '<input type="button" value="레슨재개">'; //미구현
                    // lsns += '</div>';

                    let lsns = '<div class = "no">' + lsn_no + '</div>'
                    lsns += '<img src = "../lsn_images/' + lsn_no + '_LessonThumbnail.jpg">'
                    lsns += '<div class = "title">' + lsn_title + '</div>'
                    lsns += '<input type="button" class = "stdt_manage" value="수강생관리">'
                    lsns += '<input type="button" class = "lsn_close" value="레슨종료">'
                    lsns += '<input type="button" class = "lsn_resume" value="레슨재개">'

                    $copyObj.find('div.td').html(lsns);

                    $('div.table').append($copyObj);

                    //왜 하나만 사라지지??? 
                    $closeObj = $copyObj.find('input[value=레슨종료]');
                    $resumeObj = $copyObj.find('input[value=레슨재개]');
                    // $resumeObj.hide();
                    // $closeObj.hide();

                    console.log(lsn_status);
                    if (lsn_status = 0) {
                        $closeObj.hide();
                    }else if(lsn_status = 1){
                        $resumeObj.hide();
                    }else {
                        alert("잘못된 상태의 강의입니다")
                    };
                    
                    // $closeObj.click(function(){
                    //     alert("곧 구현 예정입니다 :) 기다려주세요!");
                    // });
                    // $resumeObj.click(function(){
                    //     alert("곧 구현 예정입니다 :) 기다려주세요!");
                    // }); 

                })
            */  
            }
        },
        error : function(jqXHR){
            alert('오류 : ' + jqXHR.status);
        }
    });

    //클릭한 객체의 부모, 클릭한 객체의 자식 찾기 
    $('div.table').on('click', 'input[value=레슨종료]', function(){
        // $(this).parent() -> 나중에 기능 구현할 때 사용 
            alert("곧 구현 예정입니다 :) 기다려주세요!");
    });
    $('div.table').on('click', 'input[value=레슨재개]', function(){
        // $(this).parent() -> 나중에 기능 구현할 때 사용 
            alert("곧 구현 예정입니다 :) 기다려주세요!");
    });
    //2)레슨사진눌렀을때 레슨상세페이지 연결 
    // $lsnNoObj = $('div.no');
    $('div.td').on('click', 'div.lsn>img.lsn__image', function(){
        //레슨번호 찾아오기  //1번완료 후 
        let $lsnNoObj = $(this).siblings("div.lsn__no");  //URL종우한테 확인 필요 
        let lsn_no = $lsnNoObj.html(); 
        console.log(lsn_no);
        location.href = "/front/html/viewlesson.html?lsn_no=" + lsn_no;
    });


    //3)수강생관리 클릭시 수강생관리 페이지 연결  
    $('div.td').on('click', 'button.stdt__manage', function(){
        // let $lsnNoObj = $(this).parent().find('div.no');  //URL종우한테 확인 필요 
        let $lsnNoObj = $(this).siblings("div.lsn__no");  //URL종우한테 확인 필요 
        let lsn_no = $lsnNoObj.html();
        location.href = "/front/html/studentmanage.html?lsn_no=" + lsn_no;
    });    

})