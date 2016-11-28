<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <link rel="shortcut icon" type="image/ico" href="/images/favicon.ico" />
    <title>昆明商企业金融交易系统登录</title>
   <link href="/Static/css/styles.css" type="text/css" media="screen" rel="stylesheet" />

  </head> 
  
  <body> 
 
          <% 
                 response.setHeader("refresh", "3;URL=/sys_/userAction!loginPage");//这里的3,是你要确定的时间秒URL是要跳转的地址
                %>
			 
		<font color="red" >
		        您无权限访问此资源,3秒后将跳转到登录页面<br> 如果没有跳转,请按   
		      
				         <a href="/sys_/userAction!loginPage" style="color:red;font-size:14px;">这里</a>!
 
		</font>           
  </body>
</html>
