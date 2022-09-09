$(function(){

    $(document).ready(function() {
        var url = "http://localhost:1124/board/write";
        $('#summernote').summernote({
            height: 300, // 에디터 높이
            minHeight: null, // 최소 높이
            maxHeight: null, // 최대 높이
            focus: true, // 에디터 로딩후 포커스를 맞출지 여부
            lang: "ko-KR", // 한글 설정
            placeholder: "최대 1000자까지 쓸 수 있습니다", //placeholder 설정
        }); //summernote에 
    });// document.ready 

    // ----- 글 등록 START -----
    //등록 버튼 객체 찾기
    let $btSubmitBoard = $("div.submit-board>button.submit-board__button");
    // 등록 버튼 클릭
    $($btSubmitBoard).click(function(){


        // 자료를 보내 줄 폼 객체 찾기
        let $formObj = $("form.write");
        let formData = new FormData($formObj[0]);

        let obj= {};
        formData.append("key" , JSON.stringify(obj));
        // {}에 담아두기 위함 json형태로 변환하여

        formData.forEach(function(value,key){
            console.log(key + ":" + value);
        }); // 각각 key와 value로 만듦
    
        let obj2 = formData.get("write-summernote");
        $.ajax({
            url:"http://localhost:1126/backresale/resale/board/write",
            data:formData,
            type:"post",
            processData: false,
            contentType:false,
            cache: false, //이미지 다운로드용 설정
            xhrFields: {
			    //이미지 다운로드용 설정
                responseType: "blob",
            },
            success: function(responseData){
                let url = URL.createObjectURL(responseData);
                $img.attr("src", url);
                alert("등록 완료");
            },
            error: function(jqXHR){
                alert(jqXHR.status + "글 등록 실패");
            }
            
        });// ajax
        return false;
    });// 버튼 클릭
    // ----- 글 등록 END -----

}); // 맨 위 function