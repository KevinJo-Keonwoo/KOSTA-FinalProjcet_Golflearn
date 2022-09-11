// $(function() { 
    //로딩 되자마자 글 목록 불러오기(1 페이지)
    // let url = "http://localhost:1126/backresale/resale/board/list/1";

    $("div.board-list>div.boardlist__content").on("click",function(){
        console.log(this);
		return false;
    });
    function showList(url){
        $.ajax({
            url:url,
            success:function(jsonObj){
                if(jsonObj.status ==1){
                
                }
                },
            error: function(jqXHR){
                alert("에러 : " + jqXHR.status);
            }
        });
    }
    
    
// });
