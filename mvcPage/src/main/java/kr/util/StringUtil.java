package kr.util;

public class StringUtil {
	//HTML 태그를 허용하면서 줄바꿈
	public static String useBrHtml(String str) {
		if(str == null) { // 입력한 문자열이 없다면 작업못함(NullPointerException 발생)
			return null; // null 반환하고 종료
		}
		
		// 첫번째, 문자열에 \r\n이 있으면 <br>로 바꿔주기
		// 두번째, 문자열에 \r이 있으면 <br>로 바꿔주기
		// 세번째, 문자열에 \n이 있으면 <br>로 바꿔주기
		return str.replaceAll("\r\n", "<br>").replaceAll("\r","<br>").replaceAll("\n", "<br>");
	}
	
	//HTML 태그를 허용하지 않으면서 줄바꿈
	public static String useBrNoHtml(String str) {
		if(str == null) {
			return null;
		}
		
		return str.replaceAll("<", "&lt;")
				  .replaceAll(">", "&gt;")
				  .replaceAll("\r\n", "<br>")
				  .replaceAll("\r", "<br>")
				  .replaceAll("\n", "<br>");
	}
	
	//HTML 태그를 허용하지 않음
	public static String useNoHtml(String str) {
		if(str == null) return null;
		
		return str.replaceAll("<", "&lt;")
				  .replaceAll(">", "&gt;");
	}

	//특정 문자열 이후에 ...으로 처리
	public static String shortWords(int length, String content) {
		if(content == null) return null; // 입력한 문자열 값이 없다면 null값 반환하고 바로 종료
		
		if(content.length() > length) { // 입력한 문자열(content)의 길이가 입력제한길이(length)보다 큰 경우에만 ...처리를 할거임
			return content.substring(0,length) + " ...";
		}
		
		// 입력한 문자열(cotent)의 길이가 입력 제한길이(length)보다 작은경우는 ...처리 없이 데이터 원본 그대로 반환
		return content;
	}
	
}