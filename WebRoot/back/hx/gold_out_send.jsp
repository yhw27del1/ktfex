<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>出金送审结果发送</title>
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
			var d = param+"&time="+new Date().getTime();
			alert(d);
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
	</script>
  </head>
<body>
<!-- 
<form id="dataForm">
<table border="0">
	<tr>
		<td align="right">
			<span style="color: red">*</span>用户名：
		</td>
		<td>
			<input name="param.merAccountNo" maxlength="20" type="text"/>
		</td>
		<td align="right">
			<span style="color: red">*</span>子账号：
		</td>
		<td>
			<input name="param.accountNo" maxlength="6" type="text"/>
		</td>
		<td align="right">
			<span style="color: red">*</span>金额：
		</td>
		<td>
			<input name="param.amt" type="text"/>
		</td>
	</tr>
	<tr>
		<td align="right">
			<span style="color: red">*</span>审核结果：
		</td>
		<td>
			<select name="param.result">
				<option value="1">通过</option>
				<option value="0">拒绝</option>
			</select>
		</td>
		<td>
			<button onclick="toURL('/back/hxbank/hxbankAction!outGoldAuditResultSend');return false;" class="ui-state-default" >出金审核结果发送</button>
		</td>
		<td align="right" colspan="3">
			&nbsp;
		</td>
	</tr>
</table>
</form>
<br />
 -->
<form action="">
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				出金申请日期&nbsp;从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;到&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
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
						金额
					</th>
					<th>
						市场流水
					</th>
					<th>
						银行流水
					</th>
					<th>
						交易名称<br/>交易码
					</th>
					<th>
						用户名
					</th>
					<th>
						子账号
					</th>
					<th>
						审核状态
					</th>
					<th>
						操作者
					</th>
					<th>
						备注
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
			<c:forEach items="${pageView.records}" var="entry">
					<tr>
						<td>
							<fmt:formatDate value="${entry.createDate}" type="date" />
						</td>
						<td>
							${entry.amt}
						</td>
						<td>
							${entry.merchantTrnxNo}
						</td>
						<td>
							${entry.bankTxSerNo}
						</td>
						<td>
							${entry.name}<br/>${entry.trnxCode}
						</td>
						<td>
							${entry.merAccountNo}
						</td>
						<td>
							${entry.accountNo}
						</td>
						<td>
							<c:if test="${entry.result=='2'}">
								<span class="red">待审核</span><br />
								<button  onclick="toURL('/back/hxbank/hxbankAction!outGoldAuditResultSend','id=${entry.id}&result=1');return false;" class="ui-state-default" >通过</button>
								<button onclick="toURL('/back/hxbank/hxbankAction!outGoldAuditResultSend','id=${entry.id}&result=0');return false;" class="ui-state-default" >拒绝</button>
							</c:if>
							<c:if test="${entry.result=='1'}"><span class="green">通过</span></c:if>
							<c:if test="${entry.result=='0'}"><span class="green">拒绝</span></c:if>
						</td>
						<td>
							${entry.operator.realname}
						</td>
						<td>
							${entry.message}
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
