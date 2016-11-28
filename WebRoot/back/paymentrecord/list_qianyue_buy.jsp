<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<style>
	.ui-autocomplete{
		width:120px;
		overflow:hidden;
		padding:0;
		margin:0;
	}
	.ui-autocomplete li {
		width:120px;
		list-style-type: none;
		padding:0;
		margin:0;
	}
	.ui-autocomplete li a:HOVER{
		background-image: none;
	}
	.error{float:left;}
</style>
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/jquery.form.js"></script>
<script>
$(function(){
	var iframe = $("iframe",parent.document);
	$( "#fbcode" ).autocomplete({
		source: "/back/paymentRecord/paymentRecordAction!autocomplete_fbcode",
		minLength: 2,
		scrollHeight: 300
	});
	
	$(".table_solid").tableStyleUI(); 

	$("#startDate").datepicker({
		numberOfMonths: 2,
		minDate:'2013-03-13',
        dateFormat: "yy-mm-dd"
    });
	$("#endDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
    $("#ui-datepicker-div").css({'display':'none'});
    var b = '${bank}';
    $("option[value='"+b+"']",$("#bank")).attr("selected",true);
});
function doprint(){
	$("#myToolBar").hide();
	$("#toPrint").hide();
	$(".state").hide();
	print();
	$("#myToolBar").show();
	$("#toPrint").show();
	$(".state").show();
}
</script>
<body>
<form action="paymentRecord/paymentRecordAction!list_qianyue_buy">
	<input type="hidden" name="page" value="1" />
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
	<div style="position:absolute;left:10px;">
	项目编号&nbsp;<input type="text" id="fbcode" name="fbcode" value="${fbcode}" style="width:120px;"/>&nbsp;&nbsp;
	投标方帐号&nbsp;<input type="text" id="ivcode" name="ivcode" value="${ivcode}" style="width:120px;"/>&nbsp;&nbsp;
	签约行&nbsp;
	<select name="bank" id="bank">
		<option value="华夏银行">华夏银行</option>
	</select>
	&nbsp;&nbsp;
	签约日&nbsp;从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;至&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
	<input type="submit" class="ui-state-default" value="查询" />
	</div>
</div>

<div class="dataList ui-widget">
	<table>
		<thead>
			<tr>
				<th style="text-align: center;font-size:22px;">昆投互联网金融交易-${bank}签约用户债权买入</th>
			</tr>
		</thead>
		<tbody>
			<table class="ui-widget ui-widget-content" id="mytable" style="font-size:12px;">
				<thead>
					<tr class="ui-widget-header ">
						<th>买入日期</th>
						<th>实际金额</th>
						<th>买入成交价</th>
						<th>服务费</th>
						<th>税费</th>
						<th>债权编码</th>
						<th>买入者帐号/姓名/签约行/子账号</th>
						<th>买入者签约日期</th>
						<th>融资方帐号/名称/银行账号</th>
						<th>项目编号</th>
						<th>融资期限</th>
					</tr>
				</thead>
				<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="iter">
					<tr>
						<td><fmt:formatDate value="${iter.createDate}" type="both"/></td>
						<td><fmt:formatNumber value="${iter.realAmount}" pattern="0.00"/>元</td>
						<td><fmt:formatNumber value="${iter.buyingPrice}" pattern="0.00"/>元</td>
						<td><fmt:formatNumber value="${iter.zqfwf}" pattern="0.00"/>元</td>
						<td><fmt:formatNumber value="${iter.zqsf}" pattern="0.00"/>元</td>
						<td>${iter.zhaiQuanCode}</td>
						<td>
							${iter.buyer.username}/${iter.buyer.realname}/${iter.investRecord.investor.banklib.caption}/${iter.buyer.accountNo}
						</td>
						<td>
							<fmt:formatDate value="${iter.buyer.signDate}" type="both"/>
						</td>
						<td>${iter.investRecord.financingBase.financier.user.username}/
						<c:if test="${iter.investRecord.financingBase.financier.category=='1'}">
							${iter.investRecord.financingBase.financier.pName}
						</c:if>
						<c:if test="${iter.investRecord.financingBase.financier.category!='1'}">
							${iter.investRecord.financingBase.financier.eName}
						</c:if>/${iter.investRecord.financingBase.financier.bankAccount}
						</td>
						<td>${iter.investRecord.financingBase.code}</td>
						<td>
					<c:if test="${(iter.investRecord.financingBase.interestDay)!= 0}">按日计息(${iter.investRecord.financingBase.interestDay}天)</c:if>
			        <c:if test="${(iter.investRecord.financingBase.interestDay)== 0}">${iter.investRecord.financingBase.businessType.term}个月 </c:if>    
					 
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td>合计</td>
					<td>
						<fmt:formatNumber value="${sum}" pattern="0.00"/>元<br />
						${count}笔
					</td>
					<td colspan="10" style="text-align: right;">
					</td>
				</tr>
				<tr>
					<td colspan="15" style="text-align: right;">
						<input type="button" value="打印" id="toPrint" onclick="doprint()">&nbsp;&nbsp;报表打印时间：<fmt:formatDate value="${showToday}" type="both" />&nbsp;&nbsp;经办员：${user.realname}
					</td>
				</tr>
				</tbody>
				<tfoot class="state">
					<tr>
						<td colspan="18">
							<jsp:include page="/common/page.jsp"></jsp:include>华夏银行接口自2013年3月13日启用</td>
					</tr>
				</tfoot>
			</table>
		</tbody>
	</table>
</div>
</form>
</body>


