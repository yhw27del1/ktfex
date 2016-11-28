<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/common/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Insert title here</title>
<link rel="stylesheet" href="/Static/css/newtemplate/common.css" type="text/css" />
<jsp:include page="/common/import.jsp"></jsp:include>
</head>
<script language="JavaScript">
function Submit_onclick(){
    if(window.top.document.getElementById('myFrame').cols == "199,7,*") {
        window.top.document.getElementById('myFrame').cols = "0,7,*";
        document.getElementById("ImgArrow").src="/Static/images/newtemplate/show.png";
        document.getElementById("ImgArrow").alt="打开左侧导航栏";
    } else {
        window.top.document.getElementById('myFrame').cols = "199,7,*";
        document.getElementById("ImgArrow").src="/Static/images/newtemplate/hold.png";
        document.getElementById("ImgArrow").alt="隐藏左侧导航栏";
    }
}

function MyLoad() {
    if(window.parent.location.href.indexOf("MainUrl")>0) {
        alert(1);
        window.top.midFrame.document.getElementById("ImgArrow").src="/Static/images/newtemplate/show.png";
    }
}
</script>
<body onload="MyLoad()">
    <div id="left_content">
        <div id="user_info">
            
        </div>
    </div>
</body>
</html>