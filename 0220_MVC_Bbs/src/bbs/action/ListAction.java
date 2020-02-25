package bbs.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bbs.util.Paging;
import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;

public class ListAction implements Action {

	@Override
	public String execute(HttpServletRequest request, 
			HttpServletResponse response) {
		
		// 페이징 처리를 위한 작업
		Paging page = new Paging();
		
		//전체페이지 수를 구하기 위해 먼저
		//총 게시물의 수를 구해서 전체페이지 값을 지정한다.
		//int cnt = BbsDAO.getTotalCount();
		//page.setTotalRecord(cnt);
		page.setTotalRecord(BbsDAO.getTotalCount());
		//위의 총 게시물의 수가 정해지면서 자동적으로
		// 총 페이지 수가 변경된다.
		
		//파라미터로 현재 페이지 값이 있는지 받아본다.
		String cPage = request.getParameter("cPage");
		
		if(cPage != null){
			int p = Integer.parseInt(cPage);
			page.setNowPage(p); //이때!!!!!
			// 게시물을 추출할 범위(begin, end) 그리고
			//시작페이지와 마지막페이지 값이 모두 구해진다.
			
		}else //cPage라는 파라미터가 없이 호출되었을 때
			page.setNowPage(page.getNowPage());// setNowPage()가
		//호출되지 않으면 begin, end, startPage, endPage값이
		//구해지지 않아서 목록을 얻을 수 없다.
		
		//화면에 표현할 게시물들을 검출해 온다.
		BbsVO[] ar = BbsDAO.getList(
			page.getBegin(), page.getEnd());
		
		// 뷰 페이지(list.JSP)에서 표현할 자원들을
		//request에 저장한다.
		request.setAttribute("ar", ar);
		request.setAttribute("page", page);
		
		return "/list.jsp";
	}

}






