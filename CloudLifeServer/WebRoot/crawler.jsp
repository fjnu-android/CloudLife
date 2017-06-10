<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  	<head>
  		<title>云生活后台资源采集页面</title>
  	</head>
  	<body>
  		<center>云生活资源采集信息填写<br/>
  			<form action="<%=request.getContextPath()%>/getDietData"
  				method="post">
  				<table>
  					<tr>
  						<td>菜品名:</td>
  						<td><input name="name" style="width: 327px; "/></td>
  					</tr>
  					<tr>
  						<td>菜品类型:</td>
  						<td><input name="type" style="width: 327px; "/></td>
  					</tr>
  					<tr align="center">
  						<td colspan="2"><input type="submit" value="提交" style="height:40 ;width:100px"/></td>
  					</tr>	
  				</table>	
  			</form>
  		</center>
  	</body>
</html>

