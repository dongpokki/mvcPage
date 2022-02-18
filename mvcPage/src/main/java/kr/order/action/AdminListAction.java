package kr.order.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;
import kr.order.dao.OrderDAO;
import kr.order.vo.OrderVO;
import kr.util.PagingUtil;

public class AdminListAction implements Action{

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
		
		
		String pageNum = request.getParameter("pageNum");
		if(pageNum == null) {
			pageNum = "1";
		}
		
		String keyfield = request.getParameter("keyfield");
		String keyword = request.getParameter("keyword");
		
		OrderDAO dao = OrderDAO.getinstance();
		int count = dao.getOrderCount(keyfield, keyword);
		
		//페이지 처리
		PagingUtil page = new PagingUtil(Integer.parseInt(pageNum),count,20,10,"list.do");
		
		List<OrderVO> list = null;
		if(count > 0) {
			list = dao.getListOrder(page.getStartCount(), page.getEndCount(), keyfield, keyword);
		}
		
		request.setAttribute("count", count);
		request.setAttribute("list", list);
		request.setAttribute("pagingHtml", page.getPagingHtml());
		
		return "/WEB-INF/views/order/admin_list.jsp";
	}
}