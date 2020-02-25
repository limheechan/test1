package bbs.util;

public class Paging {

	int nowPage = 1; //현재 페이지값  <---- 파라미터 cPage
	int numPerPage = 7;// 한 페이지당 보여질 게시물 수
	
	//페이징 기법에 필요한 변수들
	int totalRecord = 0; //총 게시물 수
	
	int pagePerBlock = 3; //페이지 묶음(한 블럭당 보여질 페이지 수)
	int totalPage = 0; // 전체 페이지 수
	
	int begin, end, startPage, endPage;
	
	
	public Paging() {}	//기본생성자

	public Paging(int numPerPage, int pagePerBlock) {
		this.numPerPage = numPerPage;
		this.pagePerBlock = pagePerBlock;
	}

	public int getNowPage() {
		return nowPage;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
		
		//현재 페이지값이 총 페이지 값보다 크면 
		//현재 페이지 값을 총 페이지 값으로 대체한다.
		if(nowPage > totalPage)
			nowPage = totalPage;
		
		//각 페이지 별 게시물을 선별하기 위한 범위(begin, end)를 지정
		// 현재페이지 1 : begin:1, end:10
		// 현재페이지 2 : begin:11, end:20
		// 현재페이지 3 : begin:21, end:30
		// 현재페이지 4 : begin:31, end:40
		begin = (nowPage-1)*numPerPage+1;
		end = begin+numPerPage-1;
		
		//현재 페이지 값에 의해 블럭의
		//시작페이지 값을 구한다.
		startPage = 
			(nowPage-1)/pagePerBlock*pagePerBlock+1;
		
		//블럭의 마지막 페이지 값 구하기
		endPage = startPage+pagePerBlock-1;
		
		//현재 총 게시물이 12개라고 가정하자! 그러면
		// 총 페이지 수는 2가 된다. 이렇게 되면
		// endPage가 총 페이지 수보다 더 커져 있다.
		//그래서 endPage의 값을 총 페이지의 값보다 크면 안되므로
		// 같게 해야 한다.
		if(endPage > totalPage)
			endPage = totalPage;
	}

	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
		
		//총 게시물에 의해서 총 페이지 수가 결정된다.
		totalPage = (int)Math.ceil((double)totalRecord/numPerPage);
	}

	public int getPagePerBlock() {
		return pagePerBlock;
	}

	public void setPagePerBlock(int pagePerBlock) {
		this.pagePerBlock = pagePerBlock;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
}
