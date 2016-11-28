<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>招商申请密钥</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
	<style type="text/css">
		.red{color:red;}
		.green{color:green;}
	</style>
	<script type="text/javascript">
		$(function(){
			$(".table_solid").tableStyleUI();
			$("#startDate").datepicker({
				numberOfMonths: 2,
		        dateFormat: "yy-mm-dd"
		    });
			$("#endDate").datepicker({
				numberOfMonths: 2,
		        dateFormat: "yy-mm-dd"
		    });
		    $("#ui-datepicker-div").css({'display':'none'});
		});
		function toURL(url){
			var d = "time="+new Date().getTime();
		   	$.ajax({
		   		type:"post",
		   		url:url,
		   		data:d,
		   		dataType:"json",
		   		success:function(data){
		   			alert(data.msg);
		   			location.reload();
		   		},
		   		error:function(data){
		   			alert(data.msg);
		   			location.reload();
		   		}
		   	});
		}
	</script>
  </head>
<body>
<form action="">
<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				申请日期&nbsp;从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;到&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
				<input type="submit" class="ui-state-default" value="查询" />
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						日期
					</th>
					<th>
						市场流水
					</th>
					<th>
						交易名称<br/>交易码
					</th>
					<th>
						状态
					</th>
					<th>
						操作者
					</th>
					<th>
						备注&nbsp;
						<button onclick="toURL('/back/cmb/cmbAction!request6600_do');return false;" class="ui-state-default" >招行申请密钥</button>
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
			<c:forEach items="${data}" var="entry">
					<tr>
						<td>
							${entry.createdate}
						</td>
						<td>
							${entry.coserial}
						</td>
						<td>
							${entry.name}<br/>${entry.txcode}
						</td>
						<td>
							<c:if test="${entry.success==1}"><span class="green">成功</span></c:if>
							<c:if test="${entry.success==0}"><span class="red">失败</span></c:if>
						</td>
						<td>
							${entry.username}/${entry.realname}
						</td>
						<td>
							${entry.memo}
						</td>
					</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	</form>
</body>
</html>