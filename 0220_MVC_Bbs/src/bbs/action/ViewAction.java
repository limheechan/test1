package bbs.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;

public class ViewAction implements Action {

	@Override
	public String execute(HttpServletRequest request, 
			HttpServletResponse response) {
		// 파라미터 값 받기
		String cPage = request.getParameter("cPage");
		String b_idx = request.getParameter("b_idx");
		
		//cPage는 다시 목록화면으로 돌아갈 때 필요하며, b_idx는
		// 표현할 게시물 정보(BbsVO)를 얻기 위해 필요하다.
		BbsVO vo = BbsDAO.getBbs(b_idx);
		
		HttpSession session = request.getSession();
		
		Object obj = session.getAttribute("read_bbs");
		
		if(vo != null){
			boolean chk = false;
			List<BbsVO> list = null;
			
			if(obj == null){
				list = new ArrayList<BbsVO>();
				session.setAttribute("read_bbs", list);
			}else{
				list = (List<BbsVO>)obj;
				
				//vo의 b_idx와 list에 있는 각 BbsVO의 b_idx를 비교
				for(BbsVO r_vo : list){
					if(b_idx.equals(r_vo.getB_idx())){
						chk = true;
						break;
					}
						
				}
			}
			
			if(!chk){
				//일단 현 게시물의 조회수 값을 가져온다.
				int hit = Integer.parseInt(vo.getHit());
				++hit;
				
				vo.setHit(String.valueOf(hit)); 
				
				// 여기까지는 vo가 가지고 있는 hit값을 변경했지만
				// DB에는 변경되지 않았다.
				BbsDAO.hit(b_idx);
				
				//읽은 게시물로 처리하기 위해 list에 vo를 추가
				list.add(vo);
			}
		
			request.setAttribute("vo", vo);
			request.setAttribute("cPage", cPage);//JSP에서 바로 EL로 사용가능
		}
		
		return "/view.jsp";
	}

}
