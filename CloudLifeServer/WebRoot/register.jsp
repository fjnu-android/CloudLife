<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  	<head>
  		<title>会员注册页面</title>
  	</head>
  	
  	<body>
  		<center>会员注册信息<br/>
  			<form action="<%=request.getContextPath()%>/activity"
  				method="post">
  				<table>
  					<tr>
  						<td>手机号码:</td>
  						<td><input name="type"/></td>
  					</tr>
  					<tr>
  						<td>密       码:</td>
  						<td><input name="aid"/></td>
  					</tr>
  					<tr align="center">
  						<td colspan="2"><input type="submit" value="提交"/></td>
  					</tr>	
  				</table>	
  			</form>
  		</center>
  	</body>
  	
</html>









