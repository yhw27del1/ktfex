<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>清算与对账</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
	<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
	<script type="text/javascript" src="/Static/js/showloading/jquery.showLoading.min.js"></script>
	<style type="text/css">
		.red{color:red;}
		.green{color:green;}
	</style>
	<style>
		#logo{
			position: absolute;
			right:10px;
			top:5px;
			width:40px;
			height:40px;
		}
	</style>
	<script type="text/javascript">
		function doprint(){
			$("#myToolBar").hide();
			$("#toPrint").hide();
			$("#qingsuanduizhang").hide();
			$(".state").hide();
			print();
			$("#myToolBar").show();
			$("#toPrint").show();
			$("#qingsuanduizhang").show();
			$(".state").show();
		}
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
			$("#checkDate").datepicker({
				numberOfMonths: 2,
		        dateFormat: "yy-mm-dd"
		    });
		    $("#ui-datepicker-div").css({'display':'none'});
		    var b = '${state}';
    		$("option[value='"+b+"']",$("#state")).attr("selected",true);
		});
		function toURL(url,text){
			var d = $("#dataForm").serialize()+"&time="+new Date().getTime();
			$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:"你确定要执行华夏银行"+text+"操作吗？",
				ok:function(){
					$("body").showLoading();
					$.ajax({
				   		type:"post",
				   		url:url,
				   		data:d,
				   		dataType:"json",
				   		success:function(data){
				   			alert(data.tip);
				   			$("body").hideLoading();
				   			location.reload();
				   		},
				   		error:function(data){
				   			alert(data.tip);
				   			$("body").hideLoading();
				   			location.reload();
				   		}
			   		});
				},
				cancelVal:'关闭',cancel:true
			});
		}
	</script>
  </head>
<body>
<form id="dataForm">
<table border="0" id="qingsuanduizhang">
	<tr>
		<td align="right">
			<span style="color: red">*</span>清算日期：
		</td>
		<td>
			<input type="text" name="checkDate" value="<fmt:formatDate value='${checkDate}' type='date' />" id="checkDate"/>
		</td>
		<td align="right">
			<button onclick="toURL('/back/hxbank/hxbankAction!liquidation','清算');return false;" class="ui-state-default" >清算</button>
			<button onclick="toURL('/back/hxbank/hxbankAction!reconciliation','对账');return false;" class="ui-state-default" >对账</button>
		</td>
	</tr>
</table>
</form>
<br />
<form action="">
<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				处理日期&nbsp;从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;到&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
				状态&nbsp;<select name="state" id="state"><option value="success">成功</option><option value="failure">失败</option><option value="all">全部</option></select>
				<input type="submit" class="ui-state-default" value="查询" />
	</div>
	<div class="dataList ui-widget">
		<table>
		<thead>
			<tr>
			<th style="text-align: center;font-size:22px;">昆投互联网金融交易-${bank}清算与对账明细表<img src="/Static/images/logo.png" id="logo"/></th>
			</tr>
			<th>
			会计日期：
			<c:if test="${startDate==endDate}">
				<fmt:formatDate value="${startDate}" pattern="yyyy年MM月dd日" type="date" />
			</c:if>
			<c:if test="${startDate!=endDate}">
				<fmt:formatDate value="${startDate}" pattern="yyyy年MM月dd日" type="date" />&nbsp;至&nbsp;<fmt:formatDate value="${endDate}" pattern="yyyy年MM月dd日" type="date" />
			</c:if>
			</th>
		</thead>
		<tbody>
		<tr>
			<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						发生日期
					</th>
					<th>
						交易名称<br/>交易码
					</th>
					<th>
						处理日期
					</th>
					<th>
						处理者
					</th>
					<th>
						状态
					</th>
					<th>
						批次号
					</th>
					<th>
						备注
					</th>
					<th></th>
				</tr>
			</thead>
			<tbody class="table_solid">
			<c:forEach items="${pageView.records}" var="entry">
					<tr>
						<td>
							<fmt:formatDate value="${entry.checkDate}" type="date" />
						</td>
						<td>
							${entry.name}<br/>${entry.trnxCode}
						</td>
						<td>
							<fmt:formatDate value="${entry.createDate}" type="date" />
						</td>
						<td>
							${entry.operator.realname}
						</td>
						<td>
							<c:if test="${entry.success}"><span class="green">成功</span></c:if>
							<c:if test="${!entry.success}"><span class="red">失败</span></c:if>
						</td>
						<td>
							${entry.batchNo}
						</td>
						<td>
							<a href="javascript:void(0);" class="tooltip" title="${entry.message}">
								<c:choose>
									<c:when test="${fn:length(entry.message) > 10}">
										<c:out value="${fn:substring(entry.message,0,10)}..." />
									</c:when>
									<c:otherwise>
										<c:out value="${entry.message}" />
									</c:otherwise>
								</c:choose>
							</a>
							${entry.dealerOperNo}
						</td>
						<td>
							<a href='/back/hxbank/hxbankAction!bank_liquidation_reconciliation_detail?id=${entry.id}' target="_blank">${entry.name}明细</a>
						</td>
					</tr>
			</c:forEach>
			<tr>
				<td colspan="12">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
			<tr>
					<td colspan="15" style="text-align: right;">
						<input type="button" value="打印" id="toPrint" onclick="doprint()">&nbsp;&nbsp;报表打印时间：<fmt:formatDate value="${showToday}" type="date" />&nbsp;&nbsp;经办员：${user.realname}
					</td>
				</tr>
			</tbody>
		</table>
		</tr>
		</tbody>
	</table>
	</div>
	</form>
</body>
</html>
