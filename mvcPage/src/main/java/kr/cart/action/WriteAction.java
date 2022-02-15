package kr.cart.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.cart.dao.CartDAO;
import kr.cart.vo.CartVO;
import kr.controller.Action;
import kr.item.dao.ItemDAO;
import kr.item.vo.ItemVO;

public class WriteAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		if(user_num == null) { // 로그인 X
			return "redirect:/member/loginForm.do";
		}
		
		//로그인이 되어있는 경우
		request.setCharacterEncoding("utf-8");
		
		CartVO cart = new CartVO();
		cart.setItem_num(Integer.parseInt(request.getParameter("item_num")));
		cart.setOrder_quantity(Integer.parseInt(request.getParameter("order_quantity")));
		cart.setMem_num(user_num);
		
		CartDAO dao = CartDAO.getInstance();
		CartVO cartItem = dao.getCart(cart);
		if(cartItem == null) { // 같은 회원번호, 같은 상품번호로 등록한 정보 미존재
			dao.insertCart(cart);
		}else { // 같은 회원번호, 같은 상품번호로 등록한 정보 존재
			ItemDAO itemDao = ItemDAO.getInstance();
			ItemVO item = itemDao.getItem(cartItem.getItem_num()); // 재고 수량 읽어오기

									// 기존 카트에 저장된 수량				// 새로 입력한 수량
			int order_quantity = cartItem.getOrder_quantity() + cart.getOrder_quantity();
			
			if(item.getQuantity() < order_quantity) {
				//상품 재고 수량보다 장바구니에 많이 담으면 오류
				request.setAttribute("notice_msg", "기존에 주문한 상품입니다. 갯수를 추가하면 재고가 부족합니다.");
				request.setAttribute("notice_url", request.getContextPath()+"/cart/list.do");
				return "/WEB-INF/views/common/alert_singleView.jsp";
			}
			
			// 같은 회원번호, 같은 상품번호로 등록한 정보가 존재할 때 업데이트
			cart.setOrder_quantity(order_quantity);
			dao.updateCartByItem_num(cart);			
		}

		return "redirect:/cart/list.do";
	}
}