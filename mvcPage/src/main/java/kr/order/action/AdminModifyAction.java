package kr.order.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;
import kr.order.dao.OrderDAO;
import kr.order.vo.OrderVO;

public class AdminModifyAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		if(user_num == null) {
			return "redirect:/member/loginForm.do";
		}
		
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if (user_auth < 3) { //관리자로 로그인하지 않은 경우
			return "/WEB-INF/views/common/notice.jsp";
		}
		
		//관리자로 로그인된 상태
		request.setCharacterEncoding("utf-8");
		
		OrderVO order = new OrderVO();
		order.setOrder_num(Integer.parseInt(request.getParameter("order_num")));
		order.setPayment(Integer.parseInt(request.getParameter("payment")));
		order.setStatus(Integer.parseInt(request.getParameter("status")));
		order.setReceive_name(request.getParameter("receive_name"));
		order.setReceive_post(request.getParameter("receive_post"));
		order.setReceive_address1(request.getParameter("receive_address1"));
		order.setReceive_address2(request.getParameter("receive_address2"));
		order.setReceive_phone(request.getParameter("receive_phone"));
		order.setReceive_notice(request.getParameter("notice"));
		
		OrderDAO orderdao = OrderDAO.getinstance();
		orderdao.updateOrder(order);
			
		return "redirect:/order/admin_list.do";
	}
}