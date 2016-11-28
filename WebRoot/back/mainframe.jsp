<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="/Static/css/newtemplate/common.css" type="text/css" />
<title>管理导航区域</title>
<jsp:include page="/common/import.jsp"></jsp:include>
</head>
<script  type="text/javascript">
function Submit_onclick(){
    if(window.top.document.getElementById('myFrame').cols == "196,*") {
        window.top.document.getElementById('myFrame').cols = "0,*";
        document.getElementById("ImgArrow").src="/Static/images/newtemplate/right.png";
        document.getElementById("ImgArrow").alt="打开左侧导航栏";
    } else {
        window.top.document.getElementById('myFrame').cols = "196,*";
        document.getElementById("ImgArrow").src="/Static/images/newtemplate/left.png";
        document.getElementById("ImgArrow").alt="隐藏左侧导航栏";
    }
}

function MyLoad() {
    if(window.parent.location.href.indexOf("MainUrl")>0) {
        window.top.midFrame.document.getElementById("ImgArrow").src="/Static/images/newtemplate/right.png";
    }
}
</script>
<body>
<!--  <div id="nav">
</div>-->

<div id="sub_info">
 <div style="position: relative;float:left;width: 24px;height: 24px;">
<a href="javascript:Submit_onclick()"><img src="/Static/images/newtemplate/left.png" alt="隐藏左侧导航栏" id="ImgArrow"  style="width:16px;height:24px;"/></a>
</div>
	
<div id="show_text" style="height:24px;overflow:hidden;width:900px;float:left;margin-left:15px;">
    <s:action name="announcementAction!show" executeResult="true" namespace="/back"/>
</div>

</div>
</body>
</html>
