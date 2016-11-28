<%-- 
2012-08-24 aora 修改此页面：更正在IE6、IE8下不能提交查询条件的问题
--%>
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
<script type="text/javascript" src="/back/four.jsp"></script>
<script>
$(function(){
	var iframe = $("iframe",parent.document);
	$( "#fbcode" ).autocomplete({
		source: "/back/paymentRecord/paymentRecordAction!autocomplete_fbcode",
		minLength: 2,
		scrollHeight: 300
	});
	$(".sh").css({'cursor':'pointer'}).click(function(){
		var id = $(this).attr("id");
		if($("#h"+id).is(":hidden")){
			$(this).attr("src","/Static/js/tree/tabletree/images/minus.gif");
			$("#h"+id).show();
		}else{
			$(this).attr("src","/Static/js/tree/tabletree/images/plus.gif");
			$("#h"+id).hide();
		}
		
		iframe.height($("#mytable").height()+50);
	});
	
	
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
	
	

	
	$(".autoheight").val($("#mytable").height()+50);
});



</script>
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="/back/paymentRecord/paymentRecordAction!list_die">
	<input type="hidden" name="page" value="1" />
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
	<div style="position:absolute;right:10px;"><button class="ui-state-default reflash" >刷新</button></div>
	<div style="position:absolute;left:10px;">
	项目编号&nbsp;<input type="text" id="fbcode" name="fbcode" value="${fbcode}" style="width:120px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
	投标方帐号&nbsp;<input type="text" id="ivcode" name="ivcode" value="${ivcode}" style="width:120px;"/>&nbsp;&nbsp;
	实际还款日&nbsp;从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;至&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
	<input type="submit" class="ui-state-default" value="查询" />
    <c:if test="${!empty pageView.records}">
       &nbsp;&nbsp;&nbsp;&nbsp; <a style="color:red;"  href="/back/paymentRecord/paymentRecordAction!list_die?fbcode=${fbcode}&ivcode=${ivcode}&startDate=<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" />&endDate=<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd" />&excelFlag=1" title="结果导出EXCEL"><img src="/Static/images/excel.gif"></a>
    </c:if>		
	</div>
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content" id="mytable" style="font-size:12px;">
		<thead>
			<tr class="ui-widget-header ">
				<th></th>
				<th>项目编号</th>
				<th>融资方帐号</th>
				<th>融资方银行帐号</th>
				<th>融资方名称</th>
				<th>投资方帐号/姓名</th>
				<th>投标金额</th>
				<th>到期时间</th>
				<th>实际还款日</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${pageView.records}" var="iter">
			<tr>
				<td><img src="/Static/js/tree/tabletree/images/plus.gif" class="sh" id="${iter.id}"/></td>
				<td>${iter.investRecord.financingBase.code}</td>
				<td>${iter.investRecord.financingBase.financier.user.username}</td>
				<td><script>document.write(bankcard("${iter.investRecord.financingBase.financier.bankAccount}"));</script></td>
				<td>
				<c:if test="${iter.investRecord.financingBase.financier.category=='1'}">
					<script>document.write(name("${iter.investRecord.financingBase.financier.pName}"));</script>
				</c:if>
				<c:if test="${iter.investRecord.financingBase.financier.category!='1'}">
					<script>document.write(name("${iter.investRecord.financingBase.financier.eName}"));</script>
				</c:if>
				</td>
				<td>
					${iter.investRecord.investor.user.username}/
					<c:if test="${iter.investRecord.investor.category=='0'}">
						<script>document.write(name("${iter.investRecord.investor.eName}"));</script>
					</c:if>
					<c:if test="${iter.investRecord.investor.category!='0'}">
						<script>document.write(name("${iter.investRecord.investor.pName}"));</script>
					</c:if>
				</td>
				<td>${iter.investRecord.investAmount}</td>
				<td><fmt:formatDate value="${iter.predict_repayment_date}" pattern="yyyy-MM-dd"/></td>
				<td><fmt:formatDate value="${iter.actually_repayment_date}" pattern="yyyy-MM-dd"/></td>
			</tr>
			<tr id="h${iter.id}" style="display:none">
				<td style="background-color:#fff"></td>
				<td colspan="8" style="padding:0;margin:0;background-color:#fff" valign="top">
					<table cellpadding="0" cellspacing="0" style="margin-top:-1px;margin-left:-1px;width:auto;" >
						<tr>
							<td width="80">融资利率(年)</td><td width="150">${iter.investRecord.financingBase.rate}%</td>
							<td width="80">融资期限</td><td width="200">
											<c:if test="${(iter.investRecord.financingBase.interestDay)!= 0}">按日计息(${iter.investRecord.financingBase.interestDay}天)</c:if>
			                                <c:if test="${(iter.investRecord.financingBase.interestDay)== 0}">${iter.investRecord.financingBase.businessType.term}个月 </c:if>    						
							 </td>
						</tr>
						<tr>
							<td width="80">还款方式</td><td width="150">${iter.investRecord.financingBase.businessType.returnPattern}</td>
							<td width="80">还款次数</td><td width="200">${iter.investRecord.financingBase.businessType.returnTimes}次，当前为第${iter.succession}次</td>
						</tr>
						<tr>
							<td width="80">投标合同</td><td width="200"><a href="/back/investBaseAction!agreement_ui?financingBaseId=${iter.investRecord.financingBase.id}" target="_blank">${iter.investRecord.contract.contract_numbers }</a></td>
							<!--  <td width="80">投标合同</td><td width="200"><a href="/back/investBaseAction!agreement_ui?invest_record_id=${iter.investRecord.id}" target="_blank">${iter.investRecord.contract.contract_numbers }</a></td>-->
							<td width="80">投标时间</td><td width="200">${iter.investRecord.createDate}</td>
						</tr>
						<tr>
							<td width="80">应还总额</td><td colspan="3" class="should_debt_source"><fmt:formatNumber value="${iter.xybj+iter.xylx}" pattern="0.00"/>元(其中本金:<fmt:formatNumber value="${iter.xybj}" pattern="0.00"/>元，利息:<fmt:formatNumber value="${iter.xylx}" pattern="0.00"/>元)</td>
						</tr>
						<tr>
							<td width="80">实际还款额</td><td colspan="3" class="should_debt_source"><fmt:formatNumber value="${iter.shbj+iter.shlx}" pattern="0.00"/>元</td>
						</tr>
					</table>
				</td>
			</tr>
		</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="8">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tfoot>
	</table>
</div>
</form>
</body>


