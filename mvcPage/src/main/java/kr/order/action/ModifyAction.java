package kr.order.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;
import kr.order.dao.OrderDAO;
import kr.order.vo.OrderVO;

public class ModifyAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		if(user_num == null) { // 로그인 X
			return "redirect:/member/loginForm.do";
		}
		
		//전송된 데이터 인코딩 처리
		request.setCharacterEncoding("utf-8");
		
		int order_num = Integer.parseInt(request.getParameter("order_num"));
		String status = request.getParameter("status");
		if(status == null) { // 주문상태가 빈값으로 올 경우ㅜ
			status = "1"; // 배송 대기 
		}
		
		//주문 수정 전 배송 상태를 최종정으로 체크
		OrderDAO dao = OrderDAO.getinstance();
		OrderVO db_order = dao.getOrder(order_num);
		
		if(db_order.getStatus() > 1) { // 배송대기중(1)이었던 상품의 상태가 배송준비중 이상으로 상품의 주문 정보가 변경되어 사용자가 주문 정보를 변경할 수 없음
			request.setAttribute("notice_msg", "배송 상태가 변경되어 주문자가 주문 정보 변경 불가");
			request.setAttribute("notice_url", request.getContextPath()+"/order/orderList.do");
			
			return "/WEB-INF/views/common/alert_singleView.jsp";
		}
		
		OrderVO order = new OrderVO();
		order.setOrder_num(order_num);
		order.setPayment(Integer.parseInt(request.getParameter("payment")));
		order.setStatus(Integer.parseInt(status));
		order.setReceive_name(request.getParameter("receive_name"));
		order.setReceive_post(request.getParameter("receive_post"));
		order.setReceive_address1(request.getParameter("receive_address1"));
		order.setReceive_address2(request.getParameter("receive_address2"));
		order.setReceive_phone(request.getParameter("receive_phone"));
		order.setReceive_notice(request.getParameter("notice"));
		
		//주문 정보 수정
		dao.updateOrder(order);
		
		return "redirect:/order/orderList.do";
	}
}