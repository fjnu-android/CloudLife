<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  	<head>
  		<title>会员登录页面</title>
  	</head>
  	
  	<body>
  		<center>会员登录信息<br/>
  			<form action="<%=request.getContextPath()%>/user/login"
  				method="post">
  				<table>
  					<tr>
  						<td>手机号码:</td>
  						<td><input name="phone"/></td>
  					</tr>
  					<tr>
  						<td>密        码:</td>
  						<td><input name="password"/></td>
  					</tr>
  					<tr align="center">
  						<td colspan="2"><input type="submit" value="提交"/></td>
  					</tr>	
  				</table>	
  			</form>
  		</center>
  	</body>
  	
</html>

