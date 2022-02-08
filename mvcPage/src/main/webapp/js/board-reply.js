$(function(){
	let currentPage;
	let count;
	let rowCount;
	
	// 댓글 목록
	function selectData(page){
	
	}
	
	// 페이지 처리 이벤트 연결(다음 댓글 보기 버튼 클릭 시 데이터 추가)
	
	// 댓글 등록
	$('#re_form').submit(function(event){
		if($('#re_content').val().trim() == ''){
			alert('내용을 입력하세요');
			$('#re_content').val('').focus();
			return false;
		}
		
		//form 이하의 태그에 입력한 데이터를 모두 읽어옴
		//						serrialize : 입력한 모든 데이터 한번에 가져오는 메서드
		let form_data = $(this).serialize();
		
		//데이터 전송
		$.ajax({
			url:'writeReply.do',
			type:'post',
			data:form_data,
			dataType:'json',
			cache:false,
			timeout:30000,
			success:function(param){
				if(param.result == 'logout'){
					alert('로그인 해야 작성할 수 있습니다.');
				}else if(param.result == 'success'){
					//폼 초기화
					initForm();
					//댓글 작성이 성공하면 새로 입력한 댓글을 포함해서 첫번째 페이지의 게시글을 다시 호출함
					selectData(1);
				}else{
					alert('등록 시 오류 발생');
				}
			},
			error:function(){
				alert('네트워크 오류 발생')
			}
		});
		
		// 기본 이벤트 제거
		event.preventDefault();

	});
	
	// 댓글 작성 폼 초기화
	function initForm(){
		$('textarea').val('');
		$('#re_first .letter-count').text('300/300');
	}
	
	// textarea에 내용 입력시 글자수 체크
	$(document).on('keyup','textarea',function(){
		
		//입력한 글자수를 구함
		let inputLength = $(this).val().length;
		
		if(inputLength > 300){ // 300자를 넘어선 경우
			$(this).val($(this).val().substring(0,300));
		}else{ //300자 이하인 경우
			let remain = 300 - inputLength;
			remain += '/300';
			
			if($(this).attr('id') == 're_content'){
				//등록폼 글자수
				$('#re_first .letter-count').text(remain);
			}else{
				//수정폼 글자수
				$('#mre_first .letter-count').text(remain);				
			}
		}
		
	});
	
	// 댓글 수정 버튼 클릭시 수정폼 노출
	
	// 수정폼에서 취소 버튼 클릭 시 수정폼 초기화
	
	// 댓글 수정 폼 초기화
	
	// 댓글 수정
	
	// 댓글 삭제
	
	
	// 초기 데이터(목록) 호출
	selectData(1);
});