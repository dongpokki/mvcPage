package kr.member.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;

import com.oreilly.servlet.MultipartRequest;

import kr.controller.Action;
import kr.member.dao.MemberDAO;
import kr.member.vo.MemberVO;
import kr.util.FileUtil;

public class UpdateMyPhotoAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		Map<String,String> mapAjax = new HashMap<String, String>();
		
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		if(user_num == null) { //로그인 되지 않은 경우
			mapAjax.put("result", "logout");
		}else { // 로그인 된 경우
			MemberDAO dao = MemberDAO.getInstance();
			MemberVO db_member = dao.getMember(user_num); // 현재 등록되어 있는 회원 레코드 내 photo 정보 읽기
			
			//전송된 파일 업로드 처리
			MultipartRequest multi = FileUtil.createFile(request);
			//서버에 저장된 파일명 반환
			String photo = multi.getFilesystemName("photo");
			
			//프로필 수정        파일명  회원번호(mem_num)
			dao.updateMyPhoto(photo,user_num);
			
			//세션에 저장된 프로필 사진 정보 갱신
			session.setAttribute("user_photo", photo);
			
			//이전 프로필 이미지 삭제
			FileUtil.removeFile(request, db_member.getPhoto());
			
			mapAjax.put("result", "success");
		}
		
		//JSON 데이터로 변환
		ObjectMapper mapper = new ObjectMapper();
		String ajaxData= mapper.writeValueAsString(mapAjax);
		
		request.setAttribute("ajaxData", ajaxData);
		
		// jsp 경로 반환
		return "/WEB-INF/views/common/ajax_view.jsp";
	}
}