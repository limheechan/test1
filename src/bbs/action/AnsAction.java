package bbs.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mybatis.dao.BbsDAO;
import mybatis.vo.CommVO;

public class AnsAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
		
		String b_idx = request.getParameter("b_idx");
		String writer =request.getParameter("writer");
		String content =request.getParameter("content");
		String pwd = request.getParameter("pwd");
		String cPage = request.getParameter("cPage");
		System.out.println("b_idx"+b_idx+"writer"+writer+"content"+content+"pwd"+pwd+"c"+cPage);
		CommVO cvo = new CommVO();
		
		cvo.setB_idx(b_idx);
		cvo.setContent(content);
		cvo.setPwd(pwd);
		cvo.setWriter(writer);
		
		boolean cc = 	BbsDAO.addAns(cvo);
		request.setAttribute("cPage", cPage);
		request.setAttribute("b_idx", b_idx);
		request.setAttribute("cvo", cvo);
		//return "control?type=view&b_idx="+b_idx+"&cPage="+cPage;
		return "ans";
	}

}
