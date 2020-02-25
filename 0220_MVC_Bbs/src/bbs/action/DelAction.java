package bbs.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mybatis.dao.BbsDAO;

public class DelAction implements Action {

	@Override
	public String execute(HttpServletRequest request, 
			HttpServletResponse response) {
		// 기본키와 비밀번호를 파라미터로 받는다.
		String b_idx = request.getParameter("b_idx");
		String pw = request.getParameter("pw");
		
		boolean chk = BbsDAO.delBbs(b_idx, pw);
		request.setAttribute("value", chk);
		
		return "/del_bbs.jsp";
	}

}
