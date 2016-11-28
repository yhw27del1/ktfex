<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<c:set var="now" value="<%=new java.util.Date()%>" />
<html>
	<head>
		<title>会员利息计算-结果</title>
		<link rel="stylesheet" href="/Static/js/jquery.chromatable-1.3.0/css/style.css" type="text/css" />
		<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
		<script>
		$(function(){
			$("#sum_text").text($("#sum").val());
			$("#lx_text").text($("#lx").val());
			$("#count_text").text($("#count").val());
			
			$("#sum_text_input").val($("#sum").val());
			$("#lx_text_input").val($("#lx").val());
			$("#count_text_input").val($("#count").val());
			
			$("input.lxlx").blur(function(){
				//input修改完利息值后，主区间中的利息汇总要改变
				var lx = 0;
				$.each($("input.lxlx"),function(k,v){
					console.log($(v).val());
					lx  += Number($(v).val());
				});
				$("#lx_text").text(lx.toFixed(2));
				$("#lx_text_input").val(lx.toFixed(2));
			});
		});
		function doprint(){
			$("#toolbar").hide();
			$(".noClassPint").hide();
			$("input.lxlx").addClass("noborder");
			print();  
			$("#toolbar").show();
			$(".noClassPint").show();
			$("input.lxlx").removeClass("noborder");
		}
		function dosubmit(){
			$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:"你确定要预发利息吗？",
				ok:function(){
					$("body").showLoading();
					$("#form").submit();
				},
				cancelVal:'关闭',
				cancel:true
			});
		}
		</script>
<style>
body {
	font-size: 13px;
	padding: 5px;
	margin: 0;
}

.l1 {
	text-align: center;
}

.space {
	padding-left: 50px;
}

.title {
	font-size: 13.5px;
	font-weight: bold
}

.padding {
	padding: 6px;
}

</style>

	</head>
	<body>
		<div style="margin: 50px auto; font-weight: bold;">
			<p style="text-align: center; margin: 20px auto;">
			<div style="float: right">
				<img width="80" height="80" style="position: relative; right: 30px;" src="/Static/images/logo.png">
			</div>
			<span style="text-align: center;"><h2 align="center">
					昆投互联网金融交易
				</h2>
				<h1 align="center">
					会员利息计算-预发利息结果
				</h1>
			</span>
			</p>
			<p style="line-height: 60px;">
			<div>
				${msg}
			</div>
		</div>
	</body>
</html>