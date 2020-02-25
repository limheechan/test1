package bbs.action;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class SaveImageAction implements Action {

	@Override
	public String execute(HttpServletRequest request, 
			HttpServletResponse response) {
		// 이미지를 저장할 위치를 절대경로화 시킨다.
		ServletContext application = request.getServletContext();
		String path = application.getRealPath("/editor_img");
		
		try {
			MultipartRequest mr = 
				new MultipartRequest(request, path, 1024*1024*5,
						"utf-8", new DefaultFileRenamePolicy());
			
			String fname = null;
			File f = mr.getFile("upload");
			if(f != null)
				fname = f.getName();
			
			//jsp파일에서 json표기법으로 지금 업로드된 파일의
			//경로를 출력해야 한다.
			//그래서 파일명과 경로를 request에 저장한 후
			//jsp로 forward되어 표현해야 함!
			request.setAttribute("fname", fname);
			request.setAttribute("c_path", request.getContextPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "/saveImage.jsp";
	}

}
