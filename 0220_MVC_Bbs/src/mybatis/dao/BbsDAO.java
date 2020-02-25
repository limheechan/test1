package mybatis.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import mybatis.service.FactoryService;
import mybatis.vo.BbsVO;
import mybatis.vo.CommVO;

public class BbsDAO {

	//전체게시물의 수를 반환하는 기능 - list.jsp에서 호출
	public static int getTotalCount() {
		SqlSession ss = 
			FactoryService.getFactory().openSession();
		
		int cnt = ss.selectOne("bbs.totalCount");
		ss.close();
		
		return cnt;
	}
	
	//원하는 페이지의 게시물들을  목록화면으로
	// 표현하기 위해 배열로 반환하는 기능 - list.jsp
	public static BbsVO[] getList(int begin, int end) {
		BbsVO[] ar = null;
		
		SqlSession ss =
			FactoryService.getFactory().openSession();
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("begin", begin);
		map.put("end", end);
		
		List<BbsVO> list = ss.selectList("bbs.bbsList", map);
		//각 게시물을 의미하는 객체가 BbsVO이다. 그 안에
		// 댓글들이 들어온 상태다.
		
		//받은 list를 준비된 배열로 변환해야 한다.
		if(list != null) {
			ar = new BbsVO[list.size()];
			list.toArray(ar);
		}
		ss.close();
		
		return ar;
	}
	
	//b_idx값을 인자로 받아서 
	//특정 게시물을 반환하는 기능 - view.jsp
	
	
	//원글 저장기능
	public static boolean add(String title, String writer,
			String content, String fname, String oname, 
			String ip) {
		
		boolean value = false;
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("title", title);
		map.put("writer", writer);
		map.put("content", content);
		map.put("fname", fname);
		map.put("oname", oname);
		map.put("ip", ip);
		
		SqlSession ss = FactoryService.getFactory().openSession();
		int cnt = ss.insert("bbs.add", map);
		
		if(cnt > 0) {
			value = true;
			ss.commit();
		}
		ss.close();
		
		return value;
	}
	
	// b_idx값을 인자로 받아서 bbs.getBbs라는 맵퍼를 호출하는 기능
	// 즉, 보기 기능(view.jsp)에서 호출되는 함수이며 특정 게시물 정보를
	// 반환해야 한다.
	public static BbsVO getBbs(String b_idx) {
		SqlSession ss = 
			FactoryService.getFactory().openSession();
		
		BbsVO vo = ss.selectOne("bbs.getBbs", b_idx);
		ss.close();
		
		return vo;
	}
	
	
	//댓글 저장기능
	public static boolean addAns(CommVO cvo) {
		boolean value = false;
		
		SqlSession ss = 
			FactoryService.getFactory().openSession();
		
		int cnt = ss.insert("bbs.add_ans", cvo);
		if(cnt > 0) {
			ss.commit();
			value = true;
		}else
			ss.rollback();
		
		ss.close();
		
		return value;
	}
	
	
	//원글 수정기능
	public static boolean edit(String b_idx, String title, 
			String writer,String content, String fname,  			
			String oname, String ip, String pwd) {
		
		boolean value = false;
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("b_idx", b_idx);
		map.put("subject", title);
		map.put("content", content);
		map.put("pwd", pwd);
		map.put("ip", ip);
		
		if(fname != null && fname.trim().length() > 0) {
			map.put("fname", fname);
			map.put("oname", oname);
		}
		
		SqlSession ss = 
			FactoryService.getFactory().openSession();
		int cnt = ss.update("bbs.edit", map);
		
		if(cnt > 0) {
			value = true;
			ss.commit();
		}else
			ss.rollback();
		
		ss.close();
		
		return value;
	}
	
	
	//인자로 삭제할 원글의 b_idx와 pw를 받아 처리하는 기능
	public static boolean delBbs(String b_idx, String pw) {
		boolean value = false;
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("no", b_idx);
		map.put("pwd", pw);
		
		SqlSession ss = 
			FactoryService.getFactory().openSession();
		
		int cnt = ss.update("bbs.del",map);
		if(cnt > 0) {
			ss.commit();
			value = true;
		}else
			ss.rollback();
		
		ss.close();
		
		return value;
	}
	
	//인자로 받은 b_idx의 게시물 hit를 증가하는 기능
	public static void hit(String b_idx) {
		SqlSession ss =
			FactoryService.getFactory().openSession();
		
		int cnt = ss.update("bbs.hit", b_idx);
		if(cnt > 0)
			ss.commit();
		else
			ss.rollback();
		
		ss.close();
	}
	
}















