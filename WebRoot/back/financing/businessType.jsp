<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<html>
  <head>
    
    <title>业务类型</title>
    <script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="/Static/js/tree/dhtmlXTree/dhtmlxcommon.js"></script>
    <script type="text/javascript" src="/Static/js/tree/dhtmlXTree/dhtmlxtree.js"></script>
    <script type="text/javascript" src="/Static/js/autoheight.js"></script> 
	<link rel="stylesheet" type="text/css" href="/Static/js/tree/dhtmlXTree/dhtmlxtree.css">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<script type="text/javascript">
		$(document).ready(function(){		    
		    $("#submitBtn").click(function() {
		        var name = $("#name").val();
		         if ("" == name) {
			        alert("业务类型不能为空！");
			         return false;
		         }
				$("#setBusinessTypeForm").submit();
			});
		});
		
	</script>
  </head>
  
  <body>
    <form action="/back/financing/businessTypeAction!edit" method="post" id="setBusinessTypeForm">
    <input type='hidden' class='autoheight' value="auto" /> 
                       类型名称：<input  name="businessType.name"  id="name" type="text"  value="${businessType.name}"/><br/>
	         类型标识：<input  name="businessType.code"  id="name" type="text"  value="${businessType.code }"/><br/>
	         期限(月)：<input  name="businessType.term"  id="name" type="text"  value="${businessType.term }"/><br/>
	         风险管理费：<input  name="businessType.fxglf"  id="name" type="text"  value="${businessType.fxglf }"/>%<br/>
	        还款方式：<input  name="businessType.returnPattern"  id="name" type="text"  value="${businessType.returnPattern }"/><br/> 
	        还款次数：<input  name="businessType.returnTimes"  id="returnTimes" type="text"  value="${businessType.returnTimes }"/><br/>    
     	<input type="hidden" name="id" value="${id}"/>  
     	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button class="ui-state-default" id="submitBtn">保存</button>
    </form>   
  </body>
</html>
