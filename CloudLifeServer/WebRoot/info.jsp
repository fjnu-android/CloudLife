<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

  	<head>
  		<title>会员资料填写页面</title>
  	</head>
  	
  	<body>
  		<center>会员资料填写<br/>
  			<form action="<%=request.getContextPath()%>/user/info"
  				method="post">
  				<table>
  					<tr>
  						<td>用户名:</td>
  						<td><input name="name"/></td>
  					</tr>
  					<tr>
  						<td>生日:</td>
  						<td><input name="birthday"/></td>
  					</tr>
  					<tr>
  						<td>城市:</td>
  						<td><input name="city"/></td>
  					</tr>
  					<tr>
  						<td>性别:</td>
  						<td>
  							<select name="type" style="height: 25;" > 
								<option value="f">女性</option> 
								<option value="m">男性</option> 
							</select>
						</td>
					</tr>
  					<tr>
  						<td>身高cm:</td>
  						<td><input name="height"/></td>
  					</tr>
  					<tr>
  						<td>体重kg:</td>
  						<td><input name="weight"/></td>
  					</tr>
  					<tr>
  						<td>个人签名:</td>
  						<td><input name="sign"/></td>
  					</tr>
  					<tr align="center">
  						<td colspan="2"><input type="submit" value="提交"/></td>
  					</tr>	
  				</table>	
  			</form>
  		</center>
  	</body>
  	
</html>
