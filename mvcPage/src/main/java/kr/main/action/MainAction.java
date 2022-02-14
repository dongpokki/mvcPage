package kr.main.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.controller.Action;
import kr.item.dao.ItemDAO;
import kr.item.vo.ItemVO;

public class MainAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 상품 정보 읽기
		ItemDAO dao = ItemDAO.getInstance();
		
		// status가 2(표시)인 상품만 리스트로 가져오기
		List<ItemVO> itemList = dao.getListItem(1,8,null,null,1);
		
		request.setAttribute("itemList", itemList);
		
		// jsp 경로 반환
		return "/WEB-INF/views/main/main.jsp";
	}
}