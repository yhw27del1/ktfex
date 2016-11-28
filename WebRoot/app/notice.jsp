<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1"/>
<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
<link rel="stylesheet" type="text/css" href="/app/app_default.css"/>
</head>
<body>
  <h1 ></h1>
  <p  style="padding-left:10%;padding-right:10%">${announcement.content}</p>
  <p  style="padding-top:10px;padding-right:20px;" ><fmt:formatDate value="${announcement.addtime}" pattern="yyyy年MM月dd日"/></p>
</body>
</html>
