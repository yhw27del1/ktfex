<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>开市与休市</title>
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
		function toURL(url,param){
			var d = "time="+new Date().getTime()+"&name="+encodeURI(param);
			var gogo = false;
			gogo = confirm("你确定要"+param+"吗？");
			if(gogo==true){
				$.ajax({
		   		type:"post",
		   		url:url,
		   		data:d,
		   		dataType:"json",
		   		success:function(data){
		   			alert(data.tip);
		   			location.reload();
		   		},
		   		error:function(data){
		   			alert(data.tip);
		   			location.reload();
		   		}
		   	});
			}
		}
	</script>
  </head>
<body>
<form action="">
<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				日期&nbsp;从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;到&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
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
						交易名称
					</th>
					<th>
						状态
					</th>
					<th>
						操作者
					</th>
					<th>
						备注&nbsp;
						<button onclick="toURL('/back/kmfexTradeMarket/kmfexTradeMarketAction!openOrClose','开市');return false;" class="ui-state-default" >开市</button>
						<button onclick="toURL('/back/kmfexTradeMarket/kmfexTradeMarketAction!openOrClose','休市');return false;" class="ui-state-default" >休市</button>
						&nbsp;
						<button style="display:<c:out value="${menuMap['open_night']}" />" onclick="toURL('/back/kmfexTradeMarket/kmfexTradeMarketAction!openOrClose_night','开夜市');return false;" class="ui-state-default" >开夜市</button>
						<button style="display:<c:out value="${menuMap['close_night']}" />" onclick="toURL('/back/kmfexTradeMarket/kmfexTradeMarketAction!openOrClose_night','休夜市');return false;" class="ui-state-default" >休夜市</button>
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
			<c:forEach items="${pageView.records}" var="entry">
					<tr>
						<td>
							<fmt:formatDate value="${entry.createDate}" type="both" />
						</td>
						<td>
							${entry.name}
						</td>
						<td>
							<c:if test="${entry.success}"><span class="green">成功</span></c:if>
							<c:if test="${!entry.success}"><span class="red">失败</span></c:if>
						</td>
						<td>
							${entry.operator.realname}
						</td>
						<td>
							${entry.memo}
						</td>
					</tr>
			</c:forEach>
			<tr>
				<td colspan="12">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
			</tbody>
		</table>
	</div>
	</form>
</body>
</html>
