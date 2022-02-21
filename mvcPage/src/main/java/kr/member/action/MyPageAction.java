package kr.member.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;
import kr.member.dao.MemberDAO;
import kr.member.vo.MemberVO;
import kr.order.dao.OrderDAO;
import kr.order.vo.OrderVO;

public class MyPageAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//회원 전용 페이지니까 세션 조회 필수
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		if(user_num == null) { //로그인이 되지 않은 경우
			return "redirect:/member/loginForm.do";
		}
		
		// 로그인이 된 경우
		MemberDAO dao = MemberDAO.getInstance();
		MemberVO member = dao.getMember(user_num);
		
		//구매상품정보
		OrderDAO orderdao = OrderDAO.getinstance();
		List<OrderVO> orderList = orderdao.getListOrderByMem_num(1,5,null,null,user_num);
		
		request.setAttribute("member", member);
		request.setAttribute("orderList", orderList);
		
		// jsp 경로 반환
		return "/WEB-INF/views/member/myPage.jsp";
	}

}
