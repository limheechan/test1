package bbs.control;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bbs.action.Action;

/**
 * Servlet implementation class MyController
 */
@WebServlet(
		urlPatterns = { "/control" }, 
		initParams = { 
				@WebInitParam(name = "myParam", value = "/WEB-INF/action.properties")
		})
public class MyController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	//action.properties파일에 있는 클래스 경로들이
	//생성되어 저장될 Map구조 선언
	private Map<String, Action> actionMap;
	
    public MyController() {
        super();
        
        actionMap = new HashMap<String, Action>();
    }

	@Override
	public void init() throws ServletException {
		// 현 메서드 첫 요청자에 의해
		//단 한번만 수행하는 곳!
		
		//현재 서블릿이 생성될 때 전달된 초기 파라미터 가져온다.
		String props_path = getInitParameter("myParam");
		// "/WEB-INF/action.properties"
		
		
		//받은 action.properties파일의 경로를 절대경로화 시킨다.
		//서블릿에서는 application을 직접 구해야 한다.
		ServletContext application = getServletContext();
		
		String path = application.getRealPath(props_path);
		
		//절대경로화 시킨 이유는
		// 해당 파일의 내용(클래스 경로들)을 스트림을 이용하여
		// 읽어와서 Properties객체에 담기 위함이다.
		Properties props = new Properties();
		
		//Properties의 load함수를 이용하여 내용들을 읽어온다.
		// 이때 InputStream이 필요하다.
		FileInputStream fis = null;
		try {
			//action.properties파일과 연결되는 스트림
			fis = new FileInputStream(path);
			
			props.load(fis);//action.properties파일의
			//내용들을 읽어서 비었던 Properties객체에
			//키와 값을 쌍으로 저장했다.
			//예)  "hello" ------> "ex4.HelloAction"
		} catch (Exception e) {
			e.printStackTrace();
		}
		//////////////////////////////////////////////////////
		////// 생성할 객체들의 경로가 모두
		////// Properties객체로 저장되었다. 하지만
		////// 현재 컨트롤러 입장에서는
		////// 생성할 객체가 몇개이며, 어떤 객체인지?
		////// 알지 못한다. Properties에 저장된 키들을 모두
		////// 가져와 봐야 알 수 있다.
		//////////////////////////////////////////////////////
		
		Iterator<Object> it = props.keySet().iterator();
		
		//키들을 모두 얻었으니 키에 연결된 클래스 경로를
		//하나씩 얻어내어 객체를 생성한 후 Map구조에 저장!
		
		while(it.hasNext()) {
			//먼저 키를 하나 가져와서 문자열로 변환!
			String key = (String)it.next();
			
			//위에서 받은 키와 연결된 value를 얻어낸다.
			String value = props.getProperty(key);
			// "ex4.HelloAction"
			
			try {
				Object obj = 
					Class.forName(value).newInstance();
				
				actionMap.put(key, (Action)obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}//while의 끝!
				
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//요청시 한글처리
		request.setCharacterEncoding("utf-8");
		
		// type이라는 파라미터 받기
		String type = request.getParameter("type");
		
		// type이 null이면 기본객체를 인식할 수 있도록
		// "hello"를 넣어준다.
		if(type == null)
			type = "list";
		
		//type으로 받은 값이 actionMap의 key로 사용됨!
		Action action = actionMap.get(type);
		
		String viewPath = action.execute(request, response);
		
		if(viewPath != null && viewPath.trim().length() > 0) {
			
			if(viewPath.equals("ans")) {
				response.sendRedirect("control?type=view&b_idx="+request.getAttribute("b_idx")+"&cPage="
						+request.getAttribute("cPage"));
			}else {
				
			
			RequestDispatcher disp = 
				request.getRequestDispatcher(viewPath);
			
			disp.forward(request, response);
			}
		}else if(viewPath == null)
			response.sendRedirect("control");
		//sendRedirect 가 request 를 초기화 시킨다.
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
