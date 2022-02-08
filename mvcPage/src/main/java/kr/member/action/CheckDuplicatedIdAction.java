package kr.member.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
// jackson 라이브러리(.jar)를 lib 폴더에 넣어줘야 import 가능

import kr.controller.Action;
import kr.member.dao.MemberDAO;
import kr.member.vo.MemberVO;

public class CheckDuplicatedIdAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//전송된 데이터 인코딩 처리 (회원가입 페이지에서 입력한 id)
		request.setCharacterEncoding("utf-8");
		
		//전송된 데이터 반환
		String id = request.getParameter("id");
		
		MemberDAO dao = MemberDAO.getInstance();
		MemberVO member = dao.checkMember(id);
		
		// jackson 라이브러리 활용을 위한 Map 생성 ( Map,HashMap 모두 가능 )
		Map<String,String> mapAjax = new HashMap<String,String>();
		if(member == null) { // 아이디 미중복
			mapAjax.put("result", "idNotFound");
		}else { // 아이디 중복
			mapAjax.put("result", "idDuplicated");
		}
		
		/*
		  JSON형식으로 변환하기를 원하는 문자열을 HashMap에 key와 value의 쌍으로 저장한 후,
		  ObjectMapper의 writeValueAsString 메서드에 Map 객체를 전달해서 일반 문자열 데이터를 JSON 형식의 데이터로 변환 후 반환
		*/
		
		ObjectMapper mapper = new ObjectMapper();
		String ajaxData = mapper.writeValueAsString(mapAjax);
		
		request.setAttribute("ajaxData", ajaxData);
		
		// jsp 경로 반환
		return "/WEB-INF/views/common/ajax_view.jsp";
	}
}