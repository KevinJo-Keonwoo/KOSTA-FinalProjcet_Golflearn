$(function(){  
    $(document).ready(function() {  // 페이지 로딩이 끝나면
        let url = "http://localhost:1124/board/write";

        $('#summernote').summernote({ // summernote 실행
            placeholder: '게시글을 입력해 주세요(최대 1000자까지 쓸 수 있습니다)',
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

    // function sendFile(file, editor){
    //     let data = new FormData();
    //     data.append("imageFiles", file);
    //     $.ajax({
    //         data : data,
    //         type : "post",
    //         url:"http://localhost:1126/backresale/resale/board/write",
    //         cache : false,
    //         contentType: false,
    //         enctype:'multipart/form-data',
    //         processData : false,
    //         success : function(data){
    //             $(editor).summernote('editor.insertImage',data.url);
    //             $('#imageBoard > ul').append('<li><img src="'+url+'"/></li>');
    //         }
    //     });
    // }

    // $(".note-group-image-url").remove();    //이미지 추가할 때 Image URL 등록 input 삭제 
    
    // // ----- 글 등록 START -----
    // //등록 버튼 객체 찾기
    
    // let $btSubmitBoard = $("div.submit-board>button.submit-board__button");
    
//--이미지첨부파일 변경될때  미리보기 START--
	// $("input[name=imageFiles]").change(function () {
	$("input.imageFiles1").change(function () {
        let file = this.files[0];
		$("div.image>img.preview1").attr("src", URL.createObjectURL(file));
	});
	$("input.imageFiles2").change(function () {
        let file = this.files[0];
		$("div.image>img.preview2").attr("src", URL.createObjectURL(file));
	});
	$("input.imageFiles3").change(function () {
        let file = this.files[0];
		$("div.image>img.preview3").attr("src", URL.createObjectURL(file));
	});
	//--이미지첨부파일 변경될때  미리보기 END--

    // function readURL(obj){
    //     let reader = new FileReader();
    //     if(!obj.files.length){
    //         return;
    //     }
    //     reader.readAsDataURL(obj.files[0]);
    //     reader.onload = function(e){
    //         let img = $('<img / >');
    //         $(img).attr("src", e.target.result);
    //         $("div.image").append(img);
    //     }
    // }


    // // ----- 글 등록 START -----
    // //등록 버튼 객체 찾기
    let $btSubmitBoard = $("div.submit-board>button.submit-board__button");
    // // 버튼 클릭
    // 버튼 클릭
    $btSubmitBoard.click(function(){
        let text = $('#summernote').summernote('code');
        let $formObj =$("form.write");
        let formData = new FormData($formObj[0]);
        
        let obj = {};
        formData.forEach(function(value,key){
        });
        let obj2 = formData.get("img");
        console.log(obj2);

        $.ajax({
        url: "http://localhost:1126/backresale/resale/board/write",
		method: "post",
		processData: false, //파일업로드용 설정
		contentType: false, //파일업로드용 설정
		data: formData, //파일업로드용 설정
		cache: false, //이미지 다운로드용 설정
		xhrFields: {
			//이미지 다운로드용 설정
			responseType: "blob",
		},
		success: function (responseData) {
			let $img = $("div.image>img.downloadview");
			let url = URL.createObjectURL(responseData);
			$img.attr("src", url);
		},
		error: function (jqXHR, textStatus) {
			//응답실패
			alert("에러:" + jqXHR.status);
		},
		});
		return false;
        });
    
    });


    // var summernoteContent = $("#summernote").summernote("code"); //썸머노트(설명)
    // console.log("summernoteContent : " + summernoteContent);

    

    // /* 초기 셋팅 ( etc -> 게시글 수정 or Default font family ) */
    //     $('#summernote').summernote('code', "<?php echo $positing_text ?>");
    //     $('.note-current-fontname').css('font-family','Apple SD Gothic Neo').text('Apple SD Gothic Neo');
    //     $('.note-editable').css('font-family','Apple SD Gothic Neo');

     $(".note-group-image-url").remove();    //이미지 추가할 때 Image URL 등록 input 삭제 ( 나는 필요없음 )
        /*
        - 이미지 추가 func
        - ajax && formData realtime img multi upload
        */
    //     function RealTimeImageUpdate(files, editor) {
    //         var formData = new FormData();
    //         var fileArr = Array.prototype.slice.call(files);
    //         fileArr.forEach(function(f){
    //             if(f.type.match("image/jpg") || f.type.match("image/jpeg" || f.type.match("image/jpeg"))){
    //                 alert("JPG, JPEG, PNG 확장자만 업로드 가능합니다.");
    //                 return;
    //             }
    //             for(var xx=0;xx<files.length;xx++){
    //                 formData.append("file[]", files[xx]);
    //             }
        
    //             $.ajax({
    //             url : "http://localhost:1126/backresale/resale/board/write",
    //             data: formData,
    //             cache: false,
    //             contentType: false,
    //             processData: false,
    //             enctype	: 'multipart/form-data',
    //             type: 'POST',
    //             success : function(result) {    
    //                 //항상 업로드된 파일의 url이 있어야 한다.
    //                 if(result === -1){
    //                     alert('이미지 파일이 아닙니다.');
    //                     return;
    //                 }
    //                 var data = JSON.parse(result);
    //             }
    //             });
    //         }
    // }
// });

    // });
    // });


// }); // 맨 위 function