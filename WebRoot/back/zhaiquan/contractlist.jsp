<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<script type="text/javascript">
$(function(){
	$("#startDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
	$("#endDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
    $("#ui-datepicker-div").css({'display':'none'});
	$(".table_solid").tableStyleUI();
});
function show(url){
	window.showModalDialog(url, null, "dialogWidth:800px;dialogHeight:auto;status:no;help:yes;resizable:no;");
}
</script>
<style type="text/css">
.s_table td {
	background-color: #e3e3e3     
}
.agreement{cursor:pointer;}
.agreement:HOVER{text-decoration: underline;}
</style>
<body class="ui-widget-header" style="font-size: 8pt; color: black">
	<input type='hidden' class='autoheight' value="auto" />
	<form action="/back/zhaiquan/contractAction"> 
	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" style="height:30px;">
		
		<div style="float:left">
			
			<div style="background-color:#fff;height:24px;float:left;border:1px solid #d3d3d3">
<s:select list="#{'a':'出/受让人关键字','b':'债权代码','c':'主合同编码'}" name="searchtype" cssStyle="padding:3px"></s:select>
				<input type="text" value="${keyword}" name="keyword" style="padding:3px 0 3px 0;margin:0;border:none;" >
			</div>
		</div>
		<div style="float:left;margin-left:10px;">
			开始日期&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>" id="startDate" style="width:80px;padding:3px;"/>&nbsp;
			结束日期&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>" id="endDate" style="width:80px;padding:3px;"/>
			<input class="ui-state-default" style="height:28px;cursor:pointer" type="submit" value="查询">
		</div>
	</div>
	<input type="hidden" name="page" value="1" /> 
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
						<th>
							转让协议编号
						</th>
						<!--  <th>
							借款合同编号
						</th>-->
						<th>
							出让方
						</th>
						<th>
							受让方
						</th>
						<th>
							转让价格
						</th>
						
						<th>
							债权实际值
						</th>
						<th>
							债权到期日期
						</th>
						
						<th>
							转让完成时间
						</th>
						<!-- <th>
							受让日期
						</th> -->
						<th>
							交易详情
						</th>
					</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="entry">
						<tr>
							<td>
								<a href="javascript:void(0)" onclick="show('/back/zhaiquan/contractAction!view?id=${entry.id}')">${entry.xieyiCode}</a>
							</td>
							<!-- <td>
								<a href="javascript:void(0)" onclick="show('/back/zhaiquan/contractAction!agreement?code=${entry.contract_numbers}')">${entry.contract_numbers}</a>
							</td> -->
							<td>
								<script>document.write(name("${entry.seller.realname}"));</script>(${entry.seller.username})
							</td>
							<td>
								<script>document.write(name("${entry.buyer.realname}"));</script>(${entry.buyer.username})
							</td>
							<td>
								<fmt:formatNumber value='${entry.price}' type="currency" currencySymbol="" />
							</td>
							
							<td>
								<fmt:formatNumber value='${entry.syje}' type="currency" currencySymbol=""/>
							</td>
							<td>
								<fmt:formatDate value='${entry.endDate}' pattern="yyyy-MM-dd" />
							</td>
							<td>
								<!--<fmt:formatDate value='${entry.buyerDate}' pattern="yyyy-MM-dd 24hh:mm:ss" />-->
								${entry.buyerDate}  
							</td>
							<!--  <td>
								<fmt:formatDate value='${entry.sellerDate}' pattern="yyyy-MM-dd hh:mm:ss" />
							</td>-->
							<td>
								<a href="javascript:void(0)" onclick="show('/back/zhaiquan/zhaiQuanInvestAction!detail?zhaiQuanId=${entry.investRecord.id}&contract_id=${entry.id}')">查看详情</a>
							</td>
						</tr>
					</c:forEach>
				<tr>
					<td colspan="9">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
				</tr>
			</tbody>
		</table>
	</div>
	</form>
</body>
