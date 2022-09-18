$(function(){  
    $(document).ready(function() {  // 페이지 로딩이 끝나면
        // let url = "http://localhost:1125/board/";

        $('#summernote').summernote({ // summernote 실행
            placeholder: '게시글을 입력해 주세요(최대 1300자까지 쓸 수 있습니다)',
            height: 500, // 에디터 높이
            minHeight: null, // 최소 높이
            maxHeight: null, // 최대 높이
            focus: true, // 에디터 로딩후 포커스를 맞출지 여부
            lang: "ko-KR", // 한글 설정
            disableResizeEditor: true, // 크기 조절 기능 삭제
            fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New', 'Helvetica neue', 'Helvetica', 'Impact', 'Lucida Grande', 'Tahoma', 'Times New Roman', 'Verdana', 'Tahoma', 'Courier New', '맑은 고딕', '굴림', '돋움'],
			fontNamesIgnoreCheck: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New', 'Helvetica neue', 'Helvetica', 'Impact', 'Lucida Grande', 'Tahoma', 'Times New Roman', 'Verdana', 'Tahoma', 'Courier New',  '맑은 고딕', '굴림', '돋움'],
            toolbar: [
                ['fontname', ['fontname']],
                ['fontsize', ['fontsize']],
                ['style', ['bold', 'italic', 'underline', 'clear']],
                ['color', ['color']],
                ['table', ['table']],
                ['para', ['paragraph']],
                ['insert', ['link', 'picture']],
                ['view', []]
            ],
            fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72'],
            // callbacks: {
            //     onImageUpload: function(files, editor, welEditable) { 
            //         //이미지 업로드 시 동작하는 함수 (onImageUpload)
            //         //파일 다중 업로드를 위한 반복문
            //         RealTimeImageUpdate(files[0]);                   
            //         for(let i = files.length -1 ; i >= 0 ; i--) {
            //             sendFile(files[i],this);
            //         }
            //     }
            // } // calbacks
            
        }); //summernote에 
    });// document.ready 
    //카카오맵
    //지도 검색 start ------------------------------------------------
    let latitude = 37.33892677498593;
    let longitude = 127.10997003393963;
    let keyword = "";
    let container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
    let options = { //지도를 생성할 때 필요한 기본 옵션
        center: new kakao.maps.LatLng(latitude, longitude), //지도의 중심좌표.
        level: 3 //지도의 레벨(확대, 축소 정도)
    };
    let map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
    let infowindow = new kakao.maps.InfoWindow({zIndex:1}); //마커 클릭시 장소명 표출 -> 인포윈도우
    let ps = new kakao.maps.services.Places(); //장소검색 객체 생성
    
    let markerLat = ""
    let markerLng = ""
    //키워드 검색 완료시 호출되는 콜백함수
    function placesSearchCB (data, status, pagination){
        if (status == kakao.maps.services.Status.OK) {
            //검색된 장소 위치를 기준으로 지도 범위 재설정 -> LatLngBounds객체에 좌표를 추가 
            let bounds = new kakao.maps.LatLngBounds();
            for (var i = 0; i<data.length; i++){
                displayMarker(data[i])
                bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
            }
            //검색된 장소 위치를 기준으로 지도 범위 재설정
            map.setBounds(bounds);
        }
    }
    //지도에 마커 표시
    function displayMarker(place){
        //마커를 생성하고 지도에 표시
        let marker = new kakao.maps.Marker({
            map: map,
            position: new kakao.maps.LatLng(place.y, place.x)
        });
        
        // 마커에 클릭이벤트 등록
        kakao.maps.event.addListener(marker, 'click', function(){
            //마커를 클릭하면 장소명이 인포윈도위에 노출 
            infowindow.setContent('<div style="padding:5px; font-size:12px;">' + place.place_name + '</div>');
            infowindow.open(map, marker);
            markerLat = place.y;
            markerLng = place.x;
            console.log(place.y);
            console.log(place.x);
        });
    }
    $("div.write__map>button.search__button").on('click', function(){
        keyword = $("div.write__map>input.search").val();
        console.log(keyword);
        ps.keywordSearch(keyword, placesSearchCB); //키워드로 장소 검색


        return false;
    });
    
    

    //지도 검색 end ------------------------------------------------




    // 드래그 드롭
    // $(".sortable").sortable();
    //이미지 등록
    let files;
    $("#imgFiles").change(function(e){
      //div 내용 비워주기
        $('#preview').empty();
        
        files = e.target.files;
        let arr = Array.prototype.slice.call(files);
        // console.log(files);
        // console.log(arr);
        preview(arr);

        function preview(arr){
            arr.forEach(function(f){
                //div에 이미지 추가
                let img = '<li class="ui-state-default">';
                //str += '<span>'+fileName+'</span><br>';
                
                //이미지 파일 미리보기
                if(f.type.match('image.*')){
                    //파일을 읽기 위한 FileReader객체 생성
                    let reader = new FileReader(); 
                    reader.onload = function (e) { 
                        //파일 읽어들이기를 성공했을때 호출되는 이벤트 핸들러
                        img += '<img src="'+ e.target.result+'" title="'+ f.name +'" width=300 height=300>';
                        // img += '<span class="delBtn" onclick="delImg(this)"> X </span>';
                        img += "</li>";
                        $(img).appendTo("#preview");
                    } 
                    reader.readAsDataURL(f);
                }
            })
        }
    });
    
    //이미지 삭제
    // function delImg(_this){
    //     $(_this).parent('li').remove()
    // }

    // 세션의 닉네임
    // let loginedNickname = localStorage.getItem("loginedNickname");
    let loginedNickname = "케빈";
    // 게시글 번호
    let queryString = location.search.split("=")[1];
    let roundReviewBoardNo = queryString;
    console.log(queryString);
    /*
    // 수정 시 내용 가져오도록
    $titleObj = $("input.write__title-box");
    // $contentObj = $("div.note-editing-area");
    
    $.ajax({
        url: "http://localhost:1125/backroundreview/board/" + roundReviewBoardNo,
        method: "get",
        success: function(jsonObj){
            let detailObj = jsonObj.t; 
            let content = detailObj.roundReviewBoardContent ;
            let title = detailObj.roundReviewBoardTitle ;
            console.log("title" + title);
            $("input.write__title-box").val(title);
            $("#summernote").summernote("code", content); // 기존 내용 불러넣기
        }
    });
    */
    // ----- 글 등록 START -----
    // 등록 버튼 객체 찾기
    let $btSubmit = $("div.footer>button.submit");
    // 버튼 클릭
    $btSubmit.click(function(){
        //summernote 사용법 익히기 
        let text = $('#summernote').summernote('code');
        // let text = $("input#summernote").val();
        let $formObj =$("form.write");
        console.log("formObj[0] =" + $formObj[0]);
        let formData = new FormData($formObj[0]);
        // let formData = $formObj.serialize();
        console.log("formData: " + formData);
        console.log("content: " + text);
        let title = $("input.write__title-box").val();
        console.log("title: " + title);

        // let kakaoLat = markerLat;
        // let kakaoLng = markerLng;
        // let kakaoLat = $("div.latitude").html(markerLat);
        // let kakaoLng = $("div.longitude").html(markerLng);
        

        let obj = {};
        // obj = formData.set("userNickname", loginedNickname);
        formData.append("userNickname", loginedNickname);
        formData.append("roundReviewBoardLatitude", markerLat);
        formData.append("roundReviewBoardLongitude", markerLng);
        // formData.set("userNickname", loginedNickname);

        formData.forEach(function(value,key){
        });
        
        let obj2 = formData.get("imageFiles");
        if(obj2.size <= 0){
            alert("이미지 첨부는 필수입니다");
        }else if(text == "" || text == " " || text == "  "){
            alert("내용은 필수입력값 입니다.");
        }else if(title == "" || title == " " || title == "  "){
            alert("제목은 필수입력값 입니다.");
        }else{
            $.ajax({
                url: "http://localhost:1125/backroundreview/board",
                method: "post",
                processData: false, //파일업로드용 설정
                contentType: false, //파일업로드용 설정
                data: formData, //파일업로드용 설정
                cache: false, //이미지 다운로드용 설정
                success:
                    alert("글 등록 성공"),
                error: function (jqXHR) {
                    //응답실패
                    alert("에러:" + jqXHR.status);
                }
            });
        }
        return false;
    }); // 글 저장 버튼 클릭

    // 내용 trim해줄때 
    // $("span.board__content-content").html(jsonObj.t.meetBoardContent.replace(
    //     /&amp;/g, "&").replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&quot;/g,'"').replace(/&#40;/g,'(').replace(/&#41;/g,')').replace(/&#35;/g,'#'));
}); //function

