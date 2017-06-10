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
  		<center>会员密码修改<br/>
  			<form action="<%=request.getContextPath()%>/user/password"
  				method="post">
  				<table>
  					<tr>
  						<td>原先密码:</td>
  						<td><input name="prePass"/></td>
  					</tr>
  					<tr>
  						<td>新的密码:</td>
  						<td><input name="newPass"/></td>
  					</tr>
  					<tr align="center">
  						<td colspan="2"><input type="submit" value="提交"/></td>
  					</tr>	
  				</table>	
  			</form>
  		</center>
  	</body>
  	
</html>
