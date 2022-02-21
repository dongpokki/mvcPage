<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript">
	$(function(){
		let photo_path = $('.my-photo').attr('src'); // 파일의 경로(String)을 담을 변수 (처음 화면에 보여지는 이미지 읽기)
		let my_photo; // 파일 객체(DOM)를 담을 공간의 변수
		
		$('#photo_btn').click(function(){
			$('#photo_choice').show(); // 파일선택,전송,취소 버튼 노출
			$(this).hide(); // 수정버튼 감추기
		});
		
		//이미지 미리보기 취소
		$('#photo_reset').click(function(){
			$('.my-photo').attr('src',photo_path); //이미지 미리보기 전 이미지로 되돌리기
			$('#photo').val(''); // 파일선택값 초기화
			$('#photo_choice').hide(); // 파일선택,전송,취소 버튼 숨김
			$('#photo_btn').show(); // 수정 버튼 노출
		
		});
		
		//이미지 선택 및 이미지 미리보기
		$('#photo').change(function(){ // 파일선택이 첨부 or 미첨부가 될경우
			my_photo = this.files[0]; // 첨부한 file은 한개여도 항상 files[] 배열(객체)형으로 관리
			if(!my_photo){ // my_photo의 값이 비어있다면 = 첨부한 파일이 없다면,
				$('.my-photo').attr('src',photo_path); // 기본 이미지가 노출
				return; // 이벤트(함수) 종료
			}
			
			if(my_photo.size > 1024*1024){ // 선택한 파일이 이미지제한 1MB을 넘으면
				alert('1MB까지만 업로드 가능!');
				photo.value = ''; // 파일 선택값 지정을 취소
				return; // 이벤트(함수) 종료
			}
			
			let reader = new FileReader(); // 파일을 읽기위해선 FileReader 객체 생성
			reader.readAsDataURL(my_photo); // 파일을 읽기위해 readAsDataURL 메서드 실행
			
			reader.onload = function(){ // FileReader.readAsDataURL이 호출되어 파일을 다 읽으면
				$('.my-photo').attr('src',reader.result);
			};
		}); // end of change
		
		
		//이미지 전송
		$('#photo_submit').click(function(){
			// 파일 입력 유효성 체크
			if($('#photo').val()==''){
				alert('파일을 선택하세요!');
				$('#photo').focus();
				return;
			}
			
			// 파일 전송 (ajax(json 방식) 으로 페이지 이동없이 비동기 처리)
			let form_data = new FormData(); // ajax 방식 처리 시 FormData 객체 생성
			// FormData 객체 생성후 append 메서드 호출하여 인자값에 키와 value에 파일객체를 넣어준다.
			form_data.append('photo',my_photo); // 파일 선택하여 첨부된 파일(DOM) Element
			$.ajax({
				url : 'updateMyPhoto.do',
				type : 'post', // 파일 업로드 시 반드시 post 방식 사용
				data : form_data, // 선택한 파일이 append 메서드 처리된 FormData 객체를 데이터로 넘겨준다.
				dataType : 'json', //jackson 라이브러리를 사용하기위해 json 방식 
				contentType : false, // 데이터 객체를 문자열로 바꿀지 지정 (true일 경우 일반문자) - 파일을 넘겨주기 때문에 반드시 false
				processData : false, // 해당 타입을 true로 하면 일반 text로 구분  - 파일을 넘겨주기 때문에 반드시 false
				enctype : 'multipart/form-data', //비동기처리든 동기처리든 파일 라이브러리 사용시 enctype의 Multipart/form-data 설정은 필수
				success : function(param){ // ajax 비동기처리가 성공하면 plain 형식의 파일에 데이터를 가져온다.
					if(param.result == 'logout'){
						alert('로그인 후 사용하세요!');
					}else if(param.result == 'success'){
						alert('프로필 사진이 수정되었습니다.');
						photo_path = $('.my-photo').attr('src');
						$('#photo').val('');
						$('#photo_choice').hide();
						$('#photo_btn').show();
					}else{
						alert('파일 전송 오류 발생');	
					}
				},
				error : function(){
					alert('네트워크 오류발생');
				}
			});
		});
		
	});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<h2>회원정보</h2>
	<div class="mypage-div">
		<h3>프로필 사진</h3>
		<ul>
			<li>
				<c:if test="${empty member.photo}">
					<img src="${pageContext.request.contextPath}/images/face.png" width="200" height="200" class="my-photo">
				</c:if>
				<c:if test="${!empty member.photo}">
					<img src="${pageContext.request.contextPath}/upload/${member.photo}" width="200" height="200" class="my-photo">
				</c:if>
			</li>
			<li>
				<div class="align-center">
					<input type="button" value="수정" id="photo_btn">
				</div>
				<div id="photo_choice" style="display:none">
					<input type="file" id="photo" accept="image/gif,image/png,image/jpeg">
					<input type="button" value="전송" id="photo_submit">
					<input type="button" value="취소" id="photo_reset">
				</div>
			</li>
		</ul>
		<h3>회원탈퇴</h3>
		<ul>
			<li>
				<input type="button" value="회원탈퇴" onclick="location.href='deleteUserForm.do';">
			</li>
		</ul>
	</div>
	
	<div class="mypage-div">
		<h3>연락처</h3>
		<ul>
			<li>이름 : ${member.name}</li>
			<li>전화번호 : ${member.phone}</li>
			<li>이메일 : ${member.email}</li>
			<li>우편번호 : ${member.zipcode}</li>
			<li>주소 : ${member.address1} ${member.address2} </li>
			<li>가입일 : ${member.reg_date}</li>
			<c:if test="${!empty member.modify_date}">
			<li>최근 정보 수정일 : ${member.modify_date}</li>
			</c:if>
			<li>
				<input type="button" value="연락처 수정" onclick="location.href='modifyUserForm.do';">
			</li>
		</ul>
		<h3>비밀번호 수정</h3>
		<ul>
			<li>
				<input type="button" value="비밀번호 수정" onclick="location.href='modifyPasswordForm.do';">	
			</li>
		</ul>
		<h3>상품구매 목록</h3>
		<ul>
			<li>
				<c:forEach var="order" items="${orderList}">
					<div>
						<a href="${pageContext.request.contextPath}/order/orderModifyForm.do?order_num=${order.order_num}">
						${order.order_num} ${order.item_name} ${order.reg_date}
						<c:if test="${order.status == 1}">배송대기</c:if>
						<c:if test="${order.status == 2}">배송준비중</c:if>
						<c:if test="${order.status == 3}">배송중</c:if>
						<c:if test="${order.status == 4}">배송완료</c:if>
						<c:if test="${order.status == 5}">주문취소</c:if>
						</a>
					</div>
				</c:forEach>
			</li>
			<li>
				<input type="button" value="상품구매 목록" onclick="location.href='${pageContext.request.contextPath}/order/orderList.do';">	
			</li>
		</ul>
	</div>
	
</div>
</body>
</html>