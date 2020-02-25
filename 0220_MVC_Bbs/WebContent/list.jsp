<%@page import="bbs.util.Paging"%>
<%@page import="mybatis.vo.BbsVO"%>
<%@page import="mybatis.dao.BbsDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	#bbs table {
	    width:580px;
	    margin-left:10px;
	    border:1px solid black;
	    border-collapse:collapse;
	    font-size:14px;
	    
	}
	
	#bbs table caption {
	    font-size:20px;
	    font-weight:bold;
	    margin-bottom:10px;
	}
	
	#bbs table th,#bbs table td {
	    text-align:center;
	    border:1px solid black;
	    padding:4px 10px;
	}
	
	.no {width:15%}
	.subject {width:30%}
	.writer {width:20%}
	.reg {width:20%}
	.hit {width:15%}
	.title{background:lightsteelblue}
	
	.odd {background:silver}
	
	/* paging */
	
	table tfoot ol.paging {
	    list-style:none;
	}
	
	table tfoot ol.paging li {
	    float:left;
	    margin-right:8px;
	}
	
	table tfoot ol.paging li a {
	    display:block;
	    padding:3px 7px;
	    border:1px solid #00B3DC;
	    color:#2f313e;
	    font-weight:bold;
	}
	
	table tfoot ol.paging li a:hover {
	    background:#00B3DC;
	    color:white;
	    font-weight:bold;
	}
	
	.disable {
	    padding:3px 7px;
	    border:1px solid silver;
	    color:silver;
	}
	
	.now {
	   padding:3px 7px;
	    border:1px solid #ff4aa5;
	    background:#ff4aa5;
	    color:white;
	    font-weight:bold;
	}
	
	.empty{
		height: 60px;
	}
		
</style>
</head>
<body>
	<div id="bbs">
		<table summary="게시판 목록">
			<caption>게시판 목록</caption>
			<thead>
				<tr class="title">
					<th class="no">번호</th>
					<th class="subject">제목</th>
					<th class="writer">글쓴이</th>
					<th class="reg">날짜</th>
					<th class="hit">조회수</th>
				</tr>
			</thead>
			
			<tfoot>
                      <tr>
                          <td colspan="4">
                              <ol class="paging">
<%
	//페이징을 위해 request에 저장된 page객체를 얻어낸다.
	Object obj = request.getAttribute("page");
	Paging pvo = null;
	if(obj != null){
		pvo = (Paging)obj;
		//startPage의 값은 항상 1,4,7,...형식이다.
		//그러다보니 이전으로 가는 기능은
		// startPage가 pagePerBlock보다 작을 때는 
		//비활성화가 되어야 한다.
		if(pvo.getStartPage() < pvo.getPagePerBlock()){
%>
	<li class="disable">&lt;</li>
<%		
		}else{ //활성화
%>
	<li><a href="control?type=list&cPage=<%=pvo.getNowPage()-pvo.getPagePerBlock()%>">&lt;</a></li>
<%		
		}
	
		for(int i=pvo.getStartPage(); i<=pvo.getEndPage(); i++){
		
			if(pvo.getNowPage() == i){
%>
	<li class="now"><%=i %></li>
<%			
			}else{
%>
	<li><a href="control?type=list&cPage=<%=i%>"><%=i %></a></li>
<%		
			}//if문의 끝
		}//for문의 끝
	
	//다음 기능을 활성화 비활성화 시켜야 한다.
	//endPage가 totalPage보다 작을 경우에만 활성화!
		if(pvo.getEndPage() < pvo.getTotalPage()){
%>
	<li><a href="control?type=list&cPage=<%=pvo.getNowPage()+pvo.getPagePerBlock()%>">&gt;</a></li>	
<%		
		}else{
%>
	<li class="disable">&gt;</li>
<%		
		}
	}
%>             
	
                              </ol>
                          </td>
						  <td>
							<input type="button" value="글쓰기"
			onclick="javascript:location.href='control?type=write'"/>
						  </td>
                      </tr>
                  </tfoot>
			<tbody>
<%
	// 게시물들 begin과 end에 맞도록 가져온다.
	BbsVO[] ar = null;
	Object ar_obj = request.getAttribute("ar");
	
	if(ar_obj != null){
	
		ar = (BbsVO[])ar_obj;
		
		
		int i = 0; //앞에 번호를 만들기 위해 필요한 변수
		
		for(BbsVO vo : ar){
			int num = pvo.getTotalRecord()-
				((pvo.getNowPage()-1)*pvo.getNumPerPage()+i);	
%>			
		<tr>
			<td><%=num %></td>
			<td style="text-align: left">
				<a href="control?type=view&cPage=<%=pvo.getNowPage()%>&b_idx=<%=vo.getB_idx()%>">
					<%=vo.getSubject() %>
					<%
						if(vo.getC_list().size() > 0){
					%>
						(<%=vo.getC_list().size() %>)
					<%		
						}
					%>	
				</a>
				
				
			</td>
			<td><%=vo.getWriter() %></td>
			<td>
			<%
				if(vo.getWrite_date() != null)
					out.println(vo.getWrite_date().substring(0,10));
			%>	
			</td>
			<td><%=vo.getHit() %></td>
		</tr>
<%
			++i;
		}//for의 끝
		
	}else{
%>
		<tr>
			<td colspan="5" class="empty">
				등록된 게시물이 없습니다.
			</td>
		</tr>
<%		
	}
%>
			</tbody>
		</table>
		
	</div>
</body>
</html>
    