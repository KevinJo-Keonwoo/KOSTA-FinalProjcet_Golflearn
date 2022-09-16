$(function(){
    //1. 상세내용 보여주기
    // html/roundreviewdetail.html?round_review_board_no=' + round_review_board_no
    let user_nickname = localStorage.getItem("userNickName");
    let currentPage = location.search;
    console.log(currentPage);
    let board_no = location.search.substring(1).split('=')[1];
    console.log(board_no);
    let url = 'http://localhost:1125/backroundreview/board/' + board_no;
    let data = "";
    $.ajax({
        url : url,
        method : "get",
        data : data,
        success : function(jsonObj){
            if (jsonObj.status == 1){
                let roundReview = jsonObj.t;
                $("div.board__no").html(roundReview.roundReviewBoardNo)
                $("div.board__title").html(roundReview.roundReviewBoardTitle);
                //likelist의 usernickname이 현재 살아잇는 nickname하고 같으면 좋아요 true
                //아니면 false(색안입히기)

                $("div.user__nickname").html(roundReview.userNickname);
                $("div.board__dt").html(roundReview.roundReviewBoardDt);
                $("div.board__view-cnt").html(roundReview.roundReviewBoardViewCnt);
                
                $("img.board__images1").attr("src", "../roundreview_images/" + roundReview.roundReviewBoardNo + "/image_1.PNG");
                $("img.board__images2").attr("src", "../roundreview_images/" + roundReview.roundReviewBoardNo + "/image_2.PNG");
                $("img.board__images3").attr("src", "../roundreview_images/" + roundReview.roundReviewBoardNo + "/image_3.PNG");
                // $("img.board__images").attr("src", "https://a.slack-edge.com/production-standard-emoji-assets/14.0/google-medium/1f9e1.png");
                $("div.board__content").html(roundReview.roundReviewBoardContent);
                // $("div.board__map").html(roundReview.roundReviewBoardMap);
            
                let cmtList = roundReview.roundReviewCommentList;

                let $comment = $("div.comment").first();
                $comment.show();
                    
                $("div.comment").not($comment).remove();
                let $commentParent = $comment.parent(); 
                $(cmtList).each(function(index, comment){
                    let $commentCopy = $comment.clone();
                    $commentCopy.find("div.comment-list__no").html(comment.roundReviewCmtNo);
                    $commentCopy.find("div.comment-list__nickname").html(comment.userNickname);
                    $commentCopy.find("div.comment-list>div.comment-list__content").html(comment.roundReviewCmtContent);
                    $commentCopy.find("div.comment-list>div.comment-list__date").html(comment.roundReviewCmtDt);
                    $commentParent.append($commentCopy);
                })
                $("div.comment").first().hide();
                
                //카카오맵
                let latitude = roundReview.roundReviewBoardLatitude;
                let longitude = roundReview.roundReviewBoardLongitude;
                let mapContainer = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
                let mapOption = { //지도를 생성할 때 필요한 기본 옵션
                    center: new kakao.maps.LatLng(latitude, longitude), //지도의 중심좌표.
                    level: 3 //지도의 레벨(확대, 축소 정도)
                };
                let map = new kakao.maps.Map(mapContainer, mapOption); //지도 생성 및 객체 리턴
                //마커 위치
                var markerPosition  = new kakao.maps.LatLng(latitude, longitude); 
                //마커 생성
                var marker = new kakao.maps.Marker({
                    position: markerPosition,
                    text : "장소"
                });
                marker.setMap(map);

                let infowindow = new kakao.maps.InfoWindow({zIndex:1});
                kakao.maps.event.addListener(marker, 'click', function(mouseEvent){
                    let msg = "약속장소입니다"
                    //마커를 클릭하면 장소명이 인포윈도위에 노출 
                    infowindow.setContent('<div style="padding:5px; font-size:12px;">' + msg + '</div>');
                    infowindow.open(map, marker);
                })

                //주소얻기 
                // let geocoder = new kakao.maps.services.Geocoder();
                
                // searchAddrFromCoords(map.getCenter(), displayCenterInfo);
                // kakao.maps.event.addListener(map, 'click', function(mouseEvent){
                //     searchDetailAddrFromCoords(mouseEvent.latLng, function(result, status) {
                //         if (status === kakao.maps.services.Status.OK) {
                //             var detailAddr = !!result[0].road_address ? '<div>도로명주소 : ' + result[0].road_address.address_name + '</div>' : '';
                //             detailAddr += '<div>지번 주소 : ' + result[0].address.address_name + '</div>';
                            
                //             var content = '<div class="bAddr">' +
                //                             '<span class="title">법정동 주소정보</span>' + 
                //                             detailAddr + 
                //                         '</div>';
                
                //             // 마커를 클릭한 위치에 표시합니다 
                //             marker.setPosition(mouseEvent.latLng);
                //             marker.setMap(map);
                
                //             // 인포윈도우에 클릭한 위치에 대한 법정동 상세 주소정보를 표시합니다
                //             infowindow.setContent(content);
                //             infowindow.open(map, marker);

                //         }
                //     });
                // });
                // kakao.maps.event.addListener(map, 'idle', function() {
                //     searchAddrFromCoords(map.getCenter(), displayCenterInfo);
                // });
                // function searchAddrFromCoords(coords, callback) {
                //     // 좌표로 행정동 주소 정보를 요청합니다
                //     geocoder.coord2RegionCode(coords.getLng(), coords.getLat(), callback);         
                // }
                // function searchDetailAddrFromCoords(coords, callback) {
                //     // 좌표로 법정동 상세 주소 정보를 요청합니다
                //     geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
                // }
                
                // // 지도 좌측상단에 지도 중심좌표에 대한 주소정보를 표출하는 함수입니다
                // function displayCenterInfo(result, status) {
                //     if (status === kakao.maps.services.Status.OK) {
                //         var infoDiv = document.getElementById('centerAddr');
                
                //         for(var i = 0; i < result.length; i++) {
                //             // 행정동의 region_type 값은 'H' 이므로
                //             if (result[i].region_type === 'H') {
                //                 infoDiv.innerHTML = result[i].address_name;
                //                 break;
                //             }
                //         }
                //     }    
                // }
            }else {

            }
        },
        error : function(jqXHR){
            alert("에러:" + jqXHR.status);
        }
    })

    //2. 댓글 작성하기
    $("div.comment-box").on("click", "button.comment-box__send", function(){
        let roundReviewBoardNo = board_no;
        let roundReviewCmtContent = $(this).siblings("input[name=comment-box__write]").val();
        let roundReviewCmtParentNo = 0;

        console.log(roundReviewCmtContent);
        // let userNickname = localStorage.getItem("loginedNickname");
        // 테스트용
        let userNickname = "데빌"; 
        $.ajax({
            url : "http://localhost:1125/backroundreview/comment/" + board_no,
            method : "post",
            timeout : 0,
            headers: {
                "Content-Type": "application/json"
            },
            /*
                "roundReviewBoardNo" : "roundReviewBoardNo",
                "roundReviewCmtContent" : "roundReviewCmtContent",
                "roundReviewCmtParentNo" : "roundReviewCmtParentNo",
                "userNickname" : "userNickname",
            */
            //보내줘야할것. boardno, content, parentno, nickname
            data : JSON.stringify({
                roundReviewCmtContent : roundReviewCmtContent,
                roundReviewCmtParentNo : roundReviewCmtParentNo,
                userNickname : userNickname,
                roundReviewBoard : {
                    roundReviewBoardNo : roundReviewBoardNo
                }
            }),
            success : function(){
                location.href = currentPage;
            },
            error: function(jqXHR){
                alert("에러:" + jqXHR.status);
            }
        })
    })
    //3. 댓글 수정하기
    let $test = $("article");
    let $test2 = $test.parents();
    $test.on('click', "div.comment>div.comment-list>button.comment-list__modify", function(){
        let roundReviewCmtNo = $(this).siblings("div.comment-list__no").html();
        console.log(roundReviewCmtNo);
        $.ajax({
            url : "http://localhost:1125/backroundreview/comment/" + roundReviewCmtNo,
            method : 'put',
            timeout : 0,
            header : {
                "Content-Type": "application/json"
            },
            data : JSON.stringify({}),
            success : function(){
                alert("수정성공")
            },
            error : function (jqXHR) {
                alert(
                    "수정 에러: " +
                    jqXHR.status + 
                    ", jqXHR.responseText:" +
                    jqXHR.responseTest
                );
            }

        })
    })


    //4. 댓글 삭제하기 

    //5. 대댓글삭제하기?? 
    
    //6. 좋아요 누르기/해제하기
    
    //7. 이전 버튼 눌렀을 때 이전으로 넘어가기
    $("div.footer").on('click', "button.previous", function(){
        location.href = './roundreviewboardlist.html';
    })
    
    








})