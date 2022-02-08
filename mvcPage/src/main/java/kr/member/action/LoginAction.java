package kr.member.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;
import kr.member.dao.MemberDAO;
import kr.member.vo.MemberVO;

public class LoginAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 로그인 입력폼에서 전달받은 데이터 인코딩
		request.setCharacterEncoding("utf-8");
		
		String id = request.getParameter("id");
		String passwd = request.getParameter("passwd");
		
		MemberDAO dao = MemberDAO.getInstance();
		MemberVO member = dao.checkMember(id);
		
		boolean check = false;
		
		if(member != null) { // 입력한 아이디로 조회한 결과가 있다면 (아이디 존재)
			//비밀번호 일치 여부 체크
			check = member.isCheckedPassword(passwd);
			
			//로그인 실패시 auth 체크용
			request.setAttribute("auth", member.getAuth());
			
		}
		
		if(check) { // 인증 성공
			//로그인 처리
			HttpSession session = request.getSession();
			session.setAttribute("user_num", member.getMem_num());
			session.setAttribute("user_id", member.getId());
			session.setAttribute("user_auth", member.getAuth());
			session.setAttribute("user_photo", member.getPhoto());
			
			//인증 성공시 호출
			//리다이렉트 방식으로 /main/main.do 호출 (로그인 완료된 UI가 나오는 메인페이지로 바로 보내도 로그인 완료가 되었다는걸 알 수 있음)
			return "redirect:/main/main.do"; // 메인서블릿에 redirect: 문자열 자르고 /main/main.do 경로로 리다이렉트 하는 기능이 구현되어있음.  
		}
		
		//인증 실패시 호출
		// jsp 경로 반환
		return "/WEB-INF/views/member/login.jsp";
	}
}