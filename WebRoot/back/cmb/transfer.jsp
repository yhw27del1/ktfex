<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>招行转账类对账</title>
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
				对账日期&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>
				<input type="submit" class="ui-state-default" value="查询" />
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						
					</th>
					<th>
						交易批次号
					</th>
					<!-- 
					<th>
						银行编号
					</th>
					<th>
						合作方编号
					</th>
					 -->
					<th>
						交易日期
					</th>
					<th>
						交易时间
					</th>
					<!-- 
					<th>
						银行流水
					</th>
					<th>
						合作方流水
					</th>
					 -->
					<th>
						银行账号
					</th>
					<th>
						交易账号
					</th>
					<th>
						姓名
					</th>
					<th>
						交易发起方
					</th>
					<th>
						<!-- 转账方向 -->
						类型
					</th>
					<!-- 
					<th>
						货币代码
					</th>
					<th>
						钞汇标志
					</th>
					 -->
					<th>
						入金金额
					</th>
					<th>
						出金金额
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
			<c:if test="${data!=null}">
				<c:set var="in_amount_bank" value="0" /><!-- 银行方入金总额 -->
				<c:set var="in_count_bank" value="0" /><!-- 银行方入金总笔数 -->
				<c:set var="out_amount_bank" value="0" /><!-- 银行方出金总额 -->
				<c:set var="out_count_bank" value="0" /><!-- 银行方出金总笔数 -->
				
				<c:set var="in_amount_m" value="0" /><!-- 交易所方入金总额 -->
				<c:set var="in_count_m" value="0" /><!-- 交易所方入金总笔数 -->
				<c:set var="out_amount_m" value="0" /><!-- 交易所方出金总额 -->
				<c:set var="out_count_m" value="0" /><!-- 交易所方出金总笔数 -->
				
				<c:set var="in_amount" value="0" /><!-- 入金总额 -->
				<c:set var="in_count" value="0" /><!-- 入金总笔数 -->
				<c:set var="out_amount" value="0" /><!-- 出金总额 -->
				<c:set var="out_count" value="0" /><!-- 出金总笔数 -->
				<c:forEach items="${data}" var="entry" varStatus="jj">
						<tr>
							<td>
								${jj.count}
							</td>
							<td>
								${entry.BATCHNO}
							</td>
							<!-- 
							<td>
								${entry.BANKID}
							</td>
							<td>
								${entry.DEALID}
							</td>
							 -->
							<td>
								<c:out value="${fn:substring(entry.TXDATE,0,4)}-" /><c:out value="${fn:substring(entry.TXDATE,4,6)}-" /><c:out value="${fn:substring(entry.TXDATE,6,8)}" />
							</td>
							<td>
								<c:out value="${fn:substring(entry.TXTIME,0,2)}:" /><c:out value="${fn:substring(entry.TXTIME,2,4)}:" /><c:out value="${fn:substring(entry.TXTIME,4,6)}" />
							</td>
							<!-- 
							<td>
								${entry.BKSERIAL}
							</td>
							<td>
								${entry.COSERIAL}
							</td>
							 -->
							<td>
								<script>document.write(bankcard("${entry.BANKACC}"));</script>
							</td>
							<td>
								${entry.FUNDACC}
							</td>
							<td>
								<script>document.write(name("${entry.CUSTNAME}"));</script>
							</td>
							<td>
								<c:if test="${entry.TXOPT=='0'}">
									交易所
									<c:if test="${entry.TXDIR=='1'}">
										<c:set var="in_amount_m" value="${in_amount_m+entry.AMOUNT_}" />
										<c:set var="in_count_m" value="${in_count_m+1}" />
									</c:if>
									<c:if test="${entry.TXDIR=='2'}">
										<c:set var="out_amount_m" value="${out_amount_m+entry.AMOUNT_}" />
										<c:set var="out_count_m" value="${out_count_m+1}" />
									</c:if>
								</c:if>
								<c:if test="${entry.TXOPT=='1'}">
									银行
									<c:if test="${entry.TXDIR=='1'}">
										<c:set var="in_amount_bank" value="${in_amount_bank+entry.AMOUNT_}" />
										<c:set var="in_count_bank" value="${in_count_bank+1}" />
									</c:if>
									<c:if test="${entry.TXDIR=='2'}">
										<c:set var="out_amount_bank" value="${out_amount_bank+entry.AMOUNT_}" />
										<c:set var="out_count_bank" value="${out_count_bank+1}" />
									</c:if>
								</c:if>
							</td>
							<td>
								<c:if test="${entry.TXDIR=='1'}">
									<span style="color: red;">入金</span>
									<c:set var="in_amount" value="${in_amount+entry.AMOUNT_}" />
									<c:set var="in_count" value="${in_count+1}" />
								</c:if>
								<c:if test="${entry.TXDIR=='2'}">
									<span style="color: green;">出金</span>
									<c:set var="out_amount" value="${out_amount+entry.AMOUNT_}" />
									<c:set var="out_count" value="${out_count+1}" />
								</c:if>
							</td>
							<!-- 
							<td>
								${entry.CURCODE}
							</td>
							<td>
								${entry.CURFLAG}
							</td>
							 -->
							<td style="text-align: right;">
								<c:if test="${entry.TXDIR=='1'}">
									<span style="color: red;"><fmt:formatNumber value="${entry.AMOUNT_}" pattern="#,###,###,##0.00"/></span>
								</c:if>
							</td>
							<td style="text-align: right;">
								<c:if test="${entry.TXDIR=='2'}">
									<span style="color: green;"><fmt:formatNumber value="${entry.AMOUNT_}" pattern="#,###,###,##0.00"/></span>
								</c:if>
							</td>
						</tr>
				</c:forEach>
				<tr style="font-weight: bold;">
					<td colspan="9" style="text-align: right;">交易所小计：</td>
					<td style="text-align: right;">
						${in_count_m}笔<br />
						<fmt:formatNumber value="${in_amount_m}" pattern="#,###,###,##0.00"/>元
					</td>
					<td style="text-align: right;">
						${out_count_m}笔<br />
						<fmt:formatNumber value="${out_amount_m}" pattern="#,###,###,##0.00"/>元
					</td>
				</tr>
				<tr style="font-weight: bold;">
					<td colspan="9" style="text-align: right;">银行小计：</td>
					<td style="text-align: right;">
						${in_count_bank}笔<br />
						<fmt:formatNumber value="${in_amount_bank}" pattern="#,###,###,##0.00"/>元
					</td>
					<td style="text-align: right;">
						${out_count_bank}笔<br />
						<fmt:formatNumber value="${out_amount_bank}" pattern="#,###,###,##0.00"/>元
					</td>
				</tr>
				<tr style="font-size: 16px;font-weight: bold;">
					<td colspan="9" style="text-align: right;">合计：</td>
					<td style="text-align: right;">
						${in_count}笔<br />
						<fmt:formatNumber value="${in_amount}" pattern="#,###,###,##0.00"/>元
					</td>
					<td style="text-align: right;">
						${out_count}笔<br />
						<fmt:formatNumber value="${out_amount}" pattern="#,###,###,##0.00"/>元
					</td>
				</tr>
			</c:if>
			<c:if test="${data==null}">
				<tr>
					<td></td>
					<td colspan="10">${msg}</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	</div>
	</form>
</body>
</html>