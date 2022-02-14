package kr.item.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;

import kr.controller.Action;
import kr.item.dao.ItemDAO;
import kr.item.vo.ItemVO;
import kr.util.FileUtil;

public class AdminModifyAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		if(user_num == null) {
			return "redirect:/member/loginForm.do";
		}
		
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if(user_auth < 3) {
			return "/WEB-INF/views/common/notice.jsp";
		}
		
		// 관리자로 로그인한 경우 아래 작업 수행
		MultipartRequest multi = FileUtil.createFile(request);
		
		int item_num = Integer.parseInt(multi.getParameter("item_num"));
		String photo1 = multi.getFilesystemName("photo1");
		String photo2 = multi.getFilesystemName("photo2");
		
		ItemDAO dao = ItemDAO.getInstance();
		ItemVO db_item = dao.getItem(item_num);
		
		//전송된 정보를 자바빈(VO)에 저장
		ItemVO item = new ItemVO();
		item.setItem_num(item_num);
		item.setName(multi.getParameter("name"));
		item.setPrice(Integer.parseInt(multi.getParameter("price")));
		item.setQuantity(Integer.parseInt(multi.getParameter("quantity")));
		item.setDetail(multi.getParameter("detail"));
		item.setPhoto1(photo1);
		item.setPhoto2(photo2);
		item.setStatus(Integer.parseInt(multi.getParameter("status")));
		
		//상품 수정
		dao.updateItem(item);

		//전송된 새로 수정한 이미지가 있는 경우, 이전 이미지 삭제
		if(photo1 != null) {
			FileUtil.removeFile(request, db_item.getPhoto1());
		}
		if(photo2 != null) {
			FileUtil.removeFile(request, db_item.getPhoto2());
		}
		
		return "redirect:/item/list.do";
	}
}