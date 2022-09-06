$(function(){
	// 수강신청 버튼 찾기
	let $btPayment =$("button[name=payment]");	
	$btPayment.click(function(){	
		// function iamport() {
			// 주문 페이지에 가맹점 식별코드 이용하여 IMP 객체 초기화
			var IMP = window.IMP // 생략 가능
			//가맹점 식별코드
			IMP.init("imp84404721"); // IMP의 내 식별코드
			
			// 결제 시스템을 실행시키는 함수
			IMP.request_pay({
				pg: "html5_inicis",
				pay_method: "card",
				merchant_uid: "order_no_0001", // 상점에서 관리하는 주문 번호
				name: "주문명:결제테스트",
				amount: 14000,
				buyer_id: "iamport@siot.do",
				buyer_name: "구매자이름",
				buyer_nickname:"구매자 닉네임",
				buyer_tel: "010-1234-5678",
				
				
			},
			function (rsp) { // callback
				if (rsp.success) {
			//[1] 서버단에서 결제정보 조회를 위해 jQuery ajax로 imp_uid 전달하기
			jQuery.ajax({
				url: "/payments/complete", //cross-domain error가 발생하지 않도록 주의해주세요
				type: "POST",
				dataType: "json",
				data: {
					imp_uid: rsp.imp_uid,
					//기타 필요한 데이터가 있으면 추가 전달
				},
			})
			.done(function (data) {
				//[2] 서버에서 REST API로 결제정보확인 및 서비스루틴이 정상적인 경우
				if (everythings_fine) {
					var msg = "결제가 완료되었습니다.";
					msg += "\n고유ID : " + rsp.imp_uid;
					msg += "\n상점 거래ID : " + rsp.merchant_uid;
					msg += "결제 금액 : " + rsp.paid_amount;
					msg += "카드 승인번호 : " + rsp.apply_num;
					
					alert(msg);
				} else {
					//[3] 아직 제대로 결제가 되지 않았습니다.
					//[4] 결제된 금액이 요청한 금액과 달라 결제를 자동취소처리하였습니다.
				}
			});
		} else {
			var msg = "결제에 실패하였습니다.";
			msg += "에러내용 : " + rsp.error_msg;
			
			alert(msg);
		}
	}
	);
	// }
	
	});		
});