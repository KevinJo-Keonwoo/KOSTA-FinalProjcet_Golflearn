	// $(function() {
	//     $.ajax({
	// 		url: "http://localhost:1124/back/login",
	// 		success: function (jsonObj) {
	// 		console.log("usertype =" + jsonObj.type);
	// 		$contentObj = $("div#content>div#content-right>div#logined");
	// 		let $tabObjHtml = "";

	// 		if (jsonObj.type == 0) {
	// 			// $(location).attr("href", "mypage.html");
	// 		} else {
	// 			$tabObjHtml = '<a id="mypage" href="/front/html/addlesson.html">레슨등록</a>';
	// 			// $(location).attr("href", "mypropage.html");
	// 		}
	// 		let $copyObj = $contentObj.clone();
	//         $copyObj.html($tabObjHtml);

	// 		$contentObj.append($copyObj);
	// 		$contentObj.hide();
	// 		},
	// 		error: function (jqXHR) {
	// 		alert("오류 : " + jqXHR.status);
	// 		},
	// 	});
	// })

	function mypage() {
	let loginedId = localStorage.getItem("loginedId");
	$.ajax({
		url: "http://localhost:1124/back/mypage/student"+"?userId="+loginedId,
		//   dataType: "json",
		success: function (jsonObj) {
		// console.log("usertype =" + jsonObj.type);
		$(location).attr("href", "mypage.html");
		},
		error: function (jqXHR) {
		alert("오류 : " + jqXHR.status);
		},
	});
	}

	function mypropage() {
	// let type = localStorage.getItem("loginedType");
	let loginedId = localStorage.getItem("loginedId");

	$.ajax({
		url: "http://localhost:1124/back/mypage/pro"+"?userId="+loginedId,
		//   dataType: "json",
		success: function (jsonObj) {
		//   console.log("usertype =" + jsonObj.type);
		$(location).attr("href", "mypropage.html");
		},
		error: function (jqXHR) {
		alert("오류 : " + jqXHR.status);
		},
	});
	}

	function logout() {
	$.ajax({
		url: "http://localhost:1124/back/user/logout",
		// url: "../back/user/logout",
		success: function () {
		alert("로그아웃 되었습니다.");
		localStorage.removeItem("loginedNickname");
		localStorage.removeItem("loginedUserType");
		localStorage.removeItem("loginedId");

		location.href = "../html/main.html";
		},
		error: function (jqXHR) {
		alert("error: " + jqXHR.status);
		},
	});
	return false;
}
