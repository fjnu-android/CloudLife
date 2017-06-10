<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  	<head>
  		<title>会员动态发表页面</title>
  	</head>
  	
  	<body>
  		<center>会员动态发表<br/>
  			 <form method="post" enctype="multipart/form-data" action="<%=request.getContextPath()%>/user/social">

  				<table>
  					<tr>
  						<td>文本内容:</td>
  						<td><input name="text"/></td>
  					</tr>
  					<tr>
  						<td>选择图片</td>
  						<td><input type="file" name="file"> </td>
  					</tr>
  					<tr align="center">
  						<td colspan="2"><input type="submit" value="提交"/></td>
  					</tr>	
  				</table>	
  			</form>
  		</center>
  	</body>
  	
</html>

