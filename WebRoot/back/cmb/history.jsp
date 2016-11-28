<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>签约及解约历史</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
	<script type="text/javascript" src="/back/four.jsp"></script>
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
			var d = $("#dataForm").serialize()+"&time="+new Date().getTime();
		   	$.ajax({
		   		type:"post",
		   		url:url,
		   		data:d,
		   		dataType:"json",
		   		success:function(data){
		   		console.log(data);
		   			alert(data.msg);
		   			location.reload();
		   		},
		   		error:function(data){
		   		console.log(data);
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
				日期&nbsp;从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;到&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
				关键字&nbsp;<input type="text" name="keyWord" value="${keyWord}" />
				<input type="submit" class="ui-state-default" value="查询" />
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th></th>
					<th>
						交易账号
					</th>
					<th>
						姓名
					</th>
					<th>
						子账号
					</th>
					<th>
						业务名称
					</th>
					<th>
						签约行
					</th>
					<th>
						预签日期
					</th>
					<th>
						签约日期
					</th>
					<th>
						解约日期
					</th>
					<th>
						备注
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
			<c:forEach items="${data}" var="entry" varStatus="jj">
					<tr>
						<td>
							${jj.count}
						</td>
						<td>
							${entry.username}
						</td>
						<td>
							<script>document.write(name("${entry.realname}"));</script>
						</td>
						<td>
							${entry.accountno}
						</td>
						<td>
							${entry.name}
						</td>
						<td>
							<c:if test="${entry.signbank==1}">
						   	华夏
						   	<c:if test="${entry.signtype==1}">本行</c:if>
						   	<c:if test="${entry.signtype==2}">他行</c:if>
						   </c:if>
						   <c:if test="${entry.signbank==2}">
							招商
							<c:if test="${entry.signtype==1}">本行</c:if>
						   	<c:if test="${entry.signtype==2}">他行</c:if>
						   </c:if>
						</td>
						<td>
							${entry.syndate_market}
						</td>
						<td>
							${entry.signdate}
						</td>
						<td>
							${entry.surrenderdate}
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