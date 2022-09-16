$(function(){
	//전달받은 queryString에서 레슨넘버 얻기
	// queryString = "http://localhost:1123/front/html/viewlesson.html?lsn_no=1"형태로 올 것임
	let queryString = location.search.split("=")[1]; //새 코드
	let loginedId = localStorage.getItem("loginedId");
	console.log("레슨번호 : " + queryString, "로그인아이디 :" + loginedId);

	let url = "http://localhost:1124/back/lesson/" + queryString;
	$.ajax({
		url: url,
		method: 'get',
		success: function (jsonObj) {
			//레슨정보 불러오기
            let lesson = jsonObj.t;

			let loc_no = lesson.locNo; //지역코드(ex:11011에 해당하는 주소지역 다 문자로 표현)
			let lsn_title = lesson.lsnTitle; //레슨명
			let lsn_star_score = lesson.lsnStarScore; //레슨별점
			let lsn_review_cnt = lesson.lsnStarPplCnt; //리뷰갯수
			let user_name = lesson.userInfo.userName; //프로명
			let pro_star_score = lesson.proStarScore; //프로별점
			//레슨상세정보(레슨소개, 프로소개) 서블릿에서 가져오기
			let lsn_intro = lesson.lsnIntro;//레슨소개
			let pro_intro = lesson.userInfo.proInfo.proCareer;//프로소개

			$('div.viewlesson>img').attr('src', '../lsn_images/' + queryString + '_LessonThumbnail.jpg').attr('alt', lsn_title);
			$('div.viewlesson ul>div>li>span.loc_no').html(loc_no);	//지역은 api로 넘어갈때 수정
			$('div.viewlesson ul>div>li>span.lsn_title').html(lsn_title);
			$('div.viewlesson ul>div>li>span.lsn_star_score').html(lsn_star_score);
			$('div.viewlesson ul>div>li>span.lsn_review_cnt').html(lsn_review_cnt);
			$('div.viewlesson ul>div>li>span.user_name').html(user_name);
			$('div.viewlesson ul>div>li>span.pro_star_score').html(pro_star_score);
			$('div.lsn_intro').html(lsn_intro);
			$('div.pro_intro').html(pro_intro);

			//-----------레슨후기 list START-------------
			let lessonLines = lesson.lsnLines;
            let $lsnObj = $('div.reviewlist');
            $(lessonLines).each(function(i, element){
				let reviewNickname = element.lsnReview.stdtNickname;
                if(reviewNickname != null){//레슨리뷰가 있으면 append
				$copyObj = $lsnObj.clone();
				let reviewDt = element.lsnReview.reviewDt;
                let review = element.lsnReview.review;

                let lessonReview = '<ul>';
                lessonReview += '<li><div>작성자아이디: <span class = "reviewId">' + reviewNickname + '</span></div></li>'
                lessonReview += '<li><div>작성날짜: <span class = "reviewDt">' + reviewDt + '</span></div></li>'
                lessonReview += '<li><div>리뷰: <span class = "review">' + review + '</span></div></li>'
                lessonReview += '</ul>'
                
                $copyObj.find('div.reviewdetail').html(lessonReview);
                
                $('div.lsn').append($copyObj);
				}
			});	
		},
		error: function (jsonObj) {
			alert(jsonObj.msg);
		}
	});

	//------------수강신청 버튼 클릭 START-------------
	let $btPaymentObj = $("button[name='payment']");
    $btPaymentObj.click(function () {//취소버튼 클릭시
        if (confirm("수강신청하시겠습니까?\n '확인'을 누르시면 결제 페이지로 이동합니다.")) {
            return false;
        } else {
            window.location = '';	//결제창으로 넘어가기
        }
    });

	//------------레슨상세정보 네비바 클릭 START-------------
	$(document).ready(function(){
		$('#lsnIntroLink').click(function(){
			var offset = $('div.lsn_intro').offset(); //선택한 태그의 위치를 반환                
			//animate()메서드를 이용해서 선택한 태그의 스크롤 위치를 지정해서 0.4초 동안 부드럽게 해당 위치로 이동함
			$('html').animate({scrollTop : offset.top}, 400);		
		});	
	});
	$(document).ready(function(){
		$('#proIntroLink').click(function(){
			var offset = $('div.pro_intro').offset(); //선택한 태그의 위치를 반환                
			//animate()메서드를 이용해서 선택한 태그의 스크롤 위치를 지정해서 0.4초 동안 부드럽게 해당 위치로 이동함
			$('html').animate({scrollTop : offset.top}, 400);		
		});	
	});
	$(document).ready(function(){
		$('#reviewLink').click(function(){
			var offset = $('div.lsn').offset(); //선택한 태그의 위치를 반환                
			//animate()메서드를 이용해서 선택한 태그의 스크롤 위치를 지정해서 0.4초 동안 부드럽게 해당 위치로 이동함
			$('html').animate({scrollTop : offset.top}, 400);		
		});	
	});

	//-----------상단이동버튼 클릭 START-------------
	$(window).scroll(function(){
		if ($(this).scrollTop() > 300){
			$('.btn_gotop').show();
		} else{
			$('.btn_gotop').hide();
		}
	});
	$('.btn_gotop').click(function(){
		$('html, body').animate({scrollTop:0},400);
		return false;
	});
	//-----------상단이동버튼 클릭 END-------------

});