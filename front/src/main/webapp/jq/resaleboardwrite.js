$(function(){

    $(document).ready(function() {  // 페이지 로딩이 끝나면
        let url = "http://localhost:1124/board/write";
        //초기화
        // $("#summernote").summernote("code", "${board_data.BOARD_CONTENT}");
        $('#summernote').summernote({ // summernote 실행
            height: 300, // 에디터 높이
            minHeight: null, // 최소 높이
            maxHeight: null, // 최대 높이
            focus: true, // 에디터 로딩후 포커스를 맞출지 여부
            lang: "ko-KR", // 한글 설정
            disableResizeEditor: true, // 크기 조절 기능 삭제
            placeholder: '게시글을 입력해 주세요(최대 1000자까지 쓸 수 있습니다)',
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
                onImageUpload: function(files, editor, welEditable) { 
                    //이미지 업로드 시 동작하는 함수 (onImageUpload)
                    //파일 다중 업로드를 위한 반복문
                    for(let i = 0; i<files.length ; i++) {
                        uploadImgFiles(files[i], this);
                    }
                }
            // } // calbacks
        }); //summernote에 
    });// document.ready 
    let content = $("#summernote").summernote("code");
    console.log(content);
    // formData.append("resaleBoardTitle", $resaleBoardTitle.val());
    // ----- 글 등록 START -----
    //등록 버튼 객체 찾기
    let $btSubmitBoard = $("div.submit-board>button.submit-board__button");
    // // 등록 버튼 클릭
    $($btSubmitBoard).click(function(uploadImgFiles){
        let $formObj = $("form.write");
        let formData = new FormData($formObj[0]);
        let obj= {};
        formData.append("file",file);
        formData.append("key" , JSON.stringify(obj));
        // {}에 담아두기 위함 json형태로 변환하여

        formData.forEach(function(value,key){
            console.log(key + ":" + value);
        }); // 각각 key와 value로 만듦
        // function uploadImgFiles(file, editor, welEditable) {  
        let obj2 = formData.get("write-summernote");
            $.ajax({
                url: 'http://localhost:1126/backresale/resale/board/write',
                type: "POST",
                data: data,
                enctype: 'multipart/form-data',
                cache: false,
                contentType : false,
                    processData : false,
                    // success : function(data) {
                    //     let json = JSON.parse(data);
                    //     $(editor).summernote('editor.insertImage',data.url);
                    //         jsonArray.push(json["url"]);
                    //         jsonFn(jsonArray);
                    //     },
                    //     error : function(e) {
                    //         console.log(e);
                    //     }
                success : function(url) {
                    editor.insertImage(welEditable,url);
                    alert("성공");
                },
                error : function(jqXHR){
                    alert(jqXHR.status +"실패");
                }
            }); // ajax
                // }
                // function jsonFn(jsonArray){
            // console.log(jsonArray);
        // }
    });


    

    // let $val = $('textarea[name="summernote"]').val($('#summernote').summernote('code'));
    // let $val = $('textarea[name="summernote"]').val();
    // console.log($val);

    // // 제목 불러오기
    // let $resaleBoardTitle = $(input.write - title);

    // let formData = new FormData();

    // console.log($resaleBoardTitle);

    // var summernoteContent = $("#summernote").summernote("code"); //썸머노트(설명)
    // console.log("summernoteContent : " + summernoteContent);

    // $(".note-group-image-url").remove();    //이미지 추가할 때 Image URL 등록 input 삭제 ( 나는 필요없음 )
    
    // /* 초기 셋팅 ( etc -> 게시글 수정 or Default font family ) */
    //     $('#summernote').summernote('code', "<?php echo $positing_text ?>");
    //     $('.note-current-fontname').css('font-family','Apple SD Gothic Neo').text('Apple SD Gothic Neo');
    //     $('.note-editable').css('font-family','Apple SD Gothic Neo');


    //     $("#submit-btn").click(function(){
    //         var text = $('#summernote').summernote('code');

    //     });

    //     /*
    //      - 이미지 추가 func
    //      - ajax && formData realtime img multi upload
    //     */
    //     function RealTimeImageUpdate(files, editor) {
    //         var formData = new FormData();
    //         var fileArr = Array.prototype.slice.call(files);
    //         fileArr.forEach(function(f){
    //             if(f.type.match("image/jpg") || f.type.match("image/jpeg" || f.type.match("image/jpeg"))){
    //                 alert("JPG, JPEG, PNG 확장자만 업로드 가능합니다.");
    //                 return;
    //             }
    //         });
    //         for(var xx=0;xx<files.length;xx++){
    //             formData.append("file[]", files[xx]);
    //         }

    //         $.ajax({
    //             url : "./이미지 받을 백엔드 파일",
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
    //                 for(x=0;x<data.length;x++){
    //                     var img = $("<img>").attr({src: data[x], width: "100%"});   // Default 100% ( 서비스가 앱이어서 이미지 크기를 100% 설정 - But 수정 가능 )
    //                     console.log(img);
    //                     $(editor).summernote('pasteHTML', "<img src='"+data[x]+"' style='width:100%;' />");
    //                 }
    //             }
    //         });
    //     }
    // });

    // ----- 글 등록 START -----
    //등록 버튼 객체 찾기
    // let $btSubmitBoard = $("div.submit-board>button.submit-board__button");
    // // 등록 버튼 클릭
    // $($btSubmitBoard).click(function(){


    //     // 자료를 보내 줄 폼 객체 찾기
    //     let $formObj = $("form.write");
    //     let formData = new FormData($formObj[0]);

    //     let obj= {};
    //     formData.append("key" , JSON.stringify(obj));
    //     // {}에 담아두기 위함 json형태로 변환하여

    //     formData.forEach(function(value,key){
    //         console.log(key + ":" + value);
    //     }); // 각각 key와 value로 만듦
    
    //     let obj2 = formData.get("write-summernote");
    //     $.ajax({
    //         url:"http://localhost:1126/backresale/resale/board/write",
    //         data:formData,
    //         type:"post",
    //         processData: false,
    //         contentType:false,
    //         cache: false, //이미지 다운로드용 설정
    //         xhrFields: {
	// 		    //이미지 다운로드용 설정
    //             responseType: "blob",
    //         },
    //         success: function(responseData){
    //             let url = URL.createObjectURL(responseData);
    //             $img.attr("src", url);
    //             alert("등록 완료");
    //         },
    //         error: function(jqXHR){
    //             alert(jqXHR.status + "글 등록 실패");
    //         }
            
    //     });// ajax
    //     return false;
    // });// 버튼 클릭
    // // ----- 글 등록 END -----

}); // 맨 위 function