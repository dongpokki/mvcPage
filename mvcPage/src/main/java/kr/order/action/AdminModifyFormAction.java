package kr.order.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;
import kr.order.dao.OrderDAO;
import kr.order.vo.OrderDetailVO;
import kr.order.vo.OrderVO;

public class AdminModifyFormAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		if(user_num == null) { // 로그인한 회원이 아니라면
			return "redirect:/member/loginForm.do";
		}
		
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		
		if(user_auth < 3) { // 로그인한 회원이 관리자가 아니라면
			return "/WEB-INF/views/common/notice.jsp";
		}
		
		
		int order_num = Integer.parseInt(request.getParameter("order_num"));
		
		OrderDAO dao = OrderDAO.getinstance();
		//주문 정보
		OrderVO order = dao.getOrder(order_num);
		//개별 상품 목록
		List<OrderDetailVO> detailList = dao.getListOrderDetail(order_num);
		
		request.setAttribute("order", order);
		request.setAttribute("detailList", detailList);	
		
		return "/WEB-INF/views/order/admin_modifyForm.jsp";
	}
}