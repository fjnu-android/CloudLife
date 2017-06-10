<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<jsp:useBean id="publicData" class="com.cloudlife.resource.PublicData" scope="request"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  	<head>
  		<title>云生活后台资源采集页面</title>
  	</head>
  	<body>
  		<br/>
  		<center>
  		当前已采集菜品总数为: <jsp:getProperty property="hasDeal" name="publicData"/>
  		待处理数为: <jsp:getProperty property="needDeal" name="publicData"/>
  		</center>
  		<br/><br/>
  		<center>云生活资源采集信息填写<br/>
  			<form action="<%=request.getContextPath()%>/getgroup"
  				method="post">
  				<table>
  					<tr>
  						<td>菜品地址:</td>
  						<td><input name="url" style="width: 327px; "/></td>
  					</tr>
  					<tr>
  						<td>页数:</td>
  						<td><input name="page" style="width: 327px; "/></td>
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
  			<br>程序运行出错日志<br><br>
  			<textarea name="log_error" cols="50" rows="10">
  				<jsp:getProperty property="logError" name="publicData"/>
  			</textarea>
  		</center>
  	</body>
</html>

