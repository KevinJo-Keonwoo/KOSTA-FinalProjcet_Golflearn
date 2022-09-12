$(function() {
    let url = "http://localhost:1128/noticeboard/notice/" + $boardNo
        $.ajax({
            url:url,
            method: "GET",
            success:function(jsonObj){
                if(jsonObj.status ==1){
                    console.log(jsonObj.t.noticeBoardNo);
                    let noticeNo = jsonObj.t.noticeBoardNo;
                    
                }
            }
        })
})