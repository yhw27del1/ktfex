<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%
	Date now = new Date();
%>
<html>
	<head>
		<title></title>
		<%@ include file="/common/taglib.jsp"%>
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/metro/easyui.css">
    	<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/icon.css">
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/easyui-lang-zh_CN.js"/></script>
		<script type="text/javascript" src="/Static/js/jquery.form.js"></script>
		<style>
			<!--
				body{
				 overflow:auto !important;padding;0;margin:0;
				}
			-->
		</style>
 	</head>
	<body>
	    <div id="tb" style="padding:5px;height:auto">
	        <div>
	        <form action="/back/accountDeal/accountDealAction!xyd_batch" method="post"  enctype="multipart/form-data" id="chargeform">
	            <input id="upload" type="file" name="upload" size="25" class="combo" title="请选择批量划拨excel文件" />
	            <input id="daoru" value="批量划拨" type="button" />
	        </form>
	            <span id="showResult" style="color:red;"></span>
	        </div>
	    </div>
	    <div id="win"></div>
		<%@ include file="/common/messageTip.jsp" %>
	</body> 
</html>



<script>
	$(function(){
		var height_ = document.body.clientHeight;
		var options={dataType:"json",success:result};
        $("#chargeform").ajaxForm(options);
        
		$("#daoru").click(function(){
			var upload = document.getElementById("upload").value;
		    if(upload==""){
		         $.messager.alert('文件选择','请选择批量划拨excel文件');
		         return false;
		    }else{
		    	$("#daoru").attr("disabled",true);
        		$("#daoru").attr("mmmmmmm","mmmmmmm");
        		$("#showResult").html("正在导入，请稍后。");
		    	$("#chargeform").submit();
		    }
		});
	});
	function result(d,s){
    	$("#daoru").attr("disabled",false);
    	$("#daoru").attr("nnnnnnn","nnnnnnn");
		if(s=="success"){
		    if(d.message=="success"){
		    	$("#chargeform").resetForm();
				$("#showResult").html("导入成功"+d.tip);
			}else{
				$("#chargeform").resetForm();
				$("#showResult").html(d.message);
			}
		}else{
			$("#chargeform").resetForm();
			$("#showResult").html("服务器未响应，请稍后重试");
		}
	}
</script>