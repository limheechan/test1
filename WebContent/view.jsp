<%@page import="java.util.ArrayList"%>
<%@page import="mybatis.vo.CommVO"%>
<%@page import="java.util.List"%>
<%@page import="mybatis.dao.BbsDAO"%>
<%@page import="mybatis.vo.BbsVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" href="css/jquery-ui.min.css"/>
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
	
	#bbs table th {
	    text-align:center;
	    border:1px solid black;
	    padding:4px 10px;
	}
	
	#bbs table td {
	    text-align:left;
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
	
	
	#del_win{
		display: none;
	}
		
</style>

</head>
<body>
<%
	Object obj = request.getAttribute("vo");

	if(obj != null){
		BbsVO vo = (BbsVO)obj;
%>
	<div id="bbs">
	<form method="post" >
		<table summary="게시판 글쓰기">
			<caption>게시판 글쓰기</caption>
			<tbody>
				<tr>
					<th>제목:</th>
					<td><%=vo.getSubject() %></td>
					<th>조회수:</th>
					<td><%=vo.getHit() %></td>
				</tr>

				<tr>
					<th>첨부파일:</th>
					<td colspan="3">
					<%
						if(vo.getFile_name() != null &&
						vo.getFile_name().length() > 4){
					%>
					<a href="javascript: fDown('<%=vo.getFile_name()%>')">
						<%=vo.getFile_name() %>
						(<%=vo.getOri_name() %>)
					</a>
					<%
						}
					%>
					</td>
				</tr>
				
				<tr>
					<th>이름:</th>
					<td colspan="3"><%=vo.getWriter() %></td>
				</tr>
				<tr>
					<th>내용:</th>
					<td colspan="3"><pre><%=vo.getContent() %></pre></td>
				</tr>
				
				<tr>
					<td colspan="4">
						<input type="button" value="수정"
							onclick="edit()"/>
						<input type="button" value="삭제" 
							id="del_btn"/>
						<input type="button" value="목록"
							onclick="goList()"/>
					</td>
				</tr>
			</tbody>
		</table>
		
	</form>
	
	
	<form method="post" action="control?type=ans" name="ansfrm">
		이름:<input type="text" name="writer"/><br/>
		내용:<textarea rows="4" cols="55" name="content"></textarea><br/>
		비밀번호:<input type="password" name="pwd"/><br/>
		
		
		<input type="hidden" name="b_idx" value="${param.b_idx }">
		<input type="hidden" name="cPage" value="${cPage }"/>
		<input type="button" value="저장하기"
		onclick="addans(this.form)" id="ans_btn"/> 
	</form>
	
	댓글들<hr/>
<%
		List<CommVO> c_list = vo.getC_list();

		for(CommVO cvo:c_list){
%>	
		<div>
			이름: <%=cvo.getWriter() %> &nbsp;&nbsp;
			날짜: <%=cvo.getWrite_date() %><br/>
			내용: <%=cvo.getContent() %>
		</div>
		<hr/>
<%
		}//for의 끝
	}else{
		//페이지 강제 이동!
		response.sendRedirect("control");
	}
%>
	
	</div>
	
	<form action="control" name="frm" method="post">		
		<input type="hidden" name="type"/>
		<input type="hidden" name="f_name"/>
		<input type="hidden" name="b_idx" value="${param.b_idx }">
		<input type="hidden" id="cPage" name="cPage" value="${cPage }"/>
	</form>
	
	<div id="del_win">
		<form>
			<input type="hidden" name="b_idx" id="b_idx"
				value="${param.b_idx }"/>
			<label for="pw">비밀번호:</label>
			<input type="password" id="pw" name="pw"/>
			<br/>
			<button type="button" id="delete_bt">삭제</button>
			<button type="button" id="close_bt">닫기</button>
		</form>
	</div>
	
	<script src="js/jquery-3.4.1.min.js"></script>
	<script src="js/jquery-ui.min.js"></script>
	<script>
	
		$(function(){
			
			$("#del_btn").bind("click",function(){
				$("#del_win").css("display","block");
				$("#del_win").dialog();
			});
			
			$("#close_bt").bind("click",function(){
				
				$("#del_win").dialog("close");
			});
			
			$("#delete_bt").bind("click",function(){
				var b_idx = $("#b_idx").val();
				var pw = $("#pw").val();
				var cPage = $("#cPage").val();
				
				var param = "type=del&b_idx="+encodeURIComponent(b_idx)+
					"&pw="+encodeURIComponent(pw);
				
				$.ajax({
					url: "control",
					type: "post",
					data: param,
					dataType: "json"
				}).done(function(data){
					
					if(data.value == "true")
						location.href = "control?type=list&cPage=${cPage }";
					else{						
						alert("비밀번호가 다릅니다.");
					}
				}).fail(function(err){
					
				});
				
			});
			/* $("#ans_btn").click(function(){
				document.ansfrm.submit();
			}); */
			
		});
		function addans(frm) {
			frm.submit();
		}
	
		function fDown(fname){
			//인자로 넘어온 파일명을
			//현재 문서에 frm이라는 이름의 form객체에
			// 이름이 f_name인 요소의 값으로 지정한 후
			//form을 서버로 보낸다.
			document.frm.type.value = "down";
			document.frm.f_name.value = fname;
			document.frm.submit();
		}
		
		function goList(){
			document.frm.type.value = "list";
			document.frm.submit();
		}
		
		function edit(){
			document.frm.type.value = "edit";
			document.frm.submit();
		}
		
		
		
	</script>
</body>
</html>












