<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>日切</title>
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
				numberOfMonths: 1,
				maxDate: '+0d',//最大可选日期，0d表示只能选择到今天
		        dateFormat: "yy-mm-dd"
		    });
		    $("#ui-datepicker-div").css({'display':'none'});
		    var b = '${userType}';
    		$("option[value='"+b+"']",$("#userTypeSelect")).attr("selected",true);
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
		   			alert(data.tip);
		   			location.reload();
		   		},
		   		error:function(data){
		   			alert(data.tip);
		   			location.reload();
		   		}
		   	});
		}
	</script>
  </head>
<body>
<!--  <form id="dataForm">
<table border="0">
	<tr>
		<td align="right">
			<span style="color: red">*</span>日切日期：
		</td>
		<td>
			<input type="text" name="checkDate" readonly="readonly" value="<fmt:formatDate value='${checkDate}' type='date' />" id="checkDate"/>
		</td>
		<td align="right">
			<button onclick="toURL('/back/hxbank/dayCutAction!cut');return false;" class="ui-state-default" >日切</button>
		</td>
	</tr>
</table>
</form>
<br />-->
<form action="">
<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				日期&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;
				会员类型&nbsp;<select id="userTypeSelect" name="userType">
					<option value="all">全部</option>
					<option value="T">投资人</option>
					<option value="R">融资方</option>
					<option value="D">担保方</option>
				</select>
				机构&nbsp;<input type="text" name="orgInfo" placeholder="机构名称/机构编号" value="${orgInfo}" />
				会员&nbsp;<input type="text" name="keyWord" placeholder="会员名称/会员编号" value="${keyWord}" />
				<input type="submit" class="ui-state-default" value="查询" />
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th rowspan="2">
						首次日切
					</th>
					<th rowspan="2">
						最近日切
					</th>
					<th rowspan="2">
						会员名称
					</th>
					<th rowspan="2">
						会员编号
					</th>
					<th rowspan="2">
						会员类型
					</th>
					<th rowspan="2">
						所属机构
					</th>
					<th colspan="2" style="text-align:center;">
						<fmt:formatDate value='${startDate}' type='date' pattern="yyyy年MM月dd日" />
					</th>
				</tr>
				<tr class="ui-widget-header ">
					<th style="text-align: right;">
						可用余额
					</th>
					<th style="text-align: right;">
						冻结余额
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
			<c:forEach items="${pageView.result}" var="entry" varStatus="sta">
					<tr>
						<td>
							${entry.createdate}
						</td>
						<td>
							${entry.modifydate}
						</td>
						<td>
							<script>document.write(name("${entry.realname}"));</script>
						</td>
						<td>
							${entry.username}
						</td>
						<td>
							<c:if test="${entry.usertype=='T'}">投资方</c:if>
							<c:if test="${entry.usertype=='R'}">融资方</c:if>
						</td>
						<td>
							${entry.orgname}&nbsp;${entry.orgcode}
						</td>
						<td style="text-align: right;">
							${entry.balance}
						</td>
						<td style="text-align: right;">
							${entry.frozen}
						</td>
					</tr>
			</c:forEach>
			<tr style="font-size: 16px;font-weight: bold;">
				<td colspan="5" style="text-align: right;">合计</td>
				<td style="text-align: right;">${count}笔</td>
				<td style="text-align: right;">${balance_sum}元</td>
				<td style="text-align: right;">${frozen_sum}元</td>
			</tr>
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
