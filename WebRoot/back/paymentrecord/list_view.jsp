<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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
	#startDate,#endDate,#fbcode{
		width:80px;
	}
</style>
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<link rel="stylesheet" href="/Static/js/jquery.multiselect-1.12/jquery.multiselect.css" type="text/css"/>
<script type="text/javascript" src="/Static/js/jquery.multiselect-1.12/jquery.multiselect.mini.js"></script>
<link rel="stylesheet" href="/Static/js/jquery-ui-multiselect-widget/jquery.multiselect.filter.css" type="text/css"/>
<script type="text/javascript" src="/Static/js/jquery-ui-multiselect-widget/jquery.multiselect.filter.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<script>
$(function(){
    $(".table_solid").tableStyleUI(); 
	$( "#fbcode" ).autocomplete({
		source: "/back/paymentRecord/paymentRecordAction!autocomplete_fbcode",
		minLength: 2,
		scrollHeight: 300
	});
	$("#startDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
	$("#endDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
    $("#ui-datepicker-div").css({'display':'none'});
	$(".print_finance_voucher").click(function(){
		var id = $(this).attr("id");
		window.showModalDialog("/back/financingBase/financingBaseAction!print_finance_voucher?id="+id,"print_finance_voucher","dialogWidth=900px;dialogHeight=600px;");
	});
	$("#orglist").multiselect().multiselectfilter();
});

</script>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<body>
 
<form action="back/paymentRecord/paymentRecordAction!list_view">
    <input type='hidden' class='autoheight' value="auto" />
	<input type="hidden" name="page" value="1" />
	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
		<div style="position:absolute;right:10px;">
			
				<a href="javascript:;" onclick="window.open('/back/financingBase/financingBaseAction!print_transactions_report?queryCode=${fbcode}&startDate=<fmt:formatDate value='${startDate}'  pattern="yyyy-MM-dd"/>&endDate=<fmt:formatDate value='${endDate}'  pattern="yyyy-MM-dd"/>&org_code2=<s:property value="#request.org_code2"/>&org_code=${org_code}')">打印</a>
				<a href="javascript:;" onclick="window.open('/back/financingBase/financingBaseAction!export_transactions_report?queryCode=${fbcode}&startDate=<fmt:formatDate value='${startDate}'  pattern="yyyy-MM-dd"/>&endDate=<fmt:formatDate value='${endDate}'  pattern="yyyy-MM-dd"/>&org_code2=<s:property value="#request.org_code2"/>&org_code=${org_code}')">导出</a>
		</div>
		<div style="position:absolute;left:10px;">
			项目编号&nbsp;<input type="text" id="fbcode" name="fbcode" value="${fbcode}" style="width:80px;"/>&nbsp;&nbsp;
			保荐机构代码<input name="org_code" value="${org_code}" style="width:80px;"/>&nbsp;&nbsp;
			授权机构
			
			<select multiple="multiple" id="orglist" name="org_code2">
				<c:forEach items="${orgs}" var="orglist">
					<option value="${orglist.showCoding}"
						<c:forEach items="${org_code2}" var="orgselected">
							<c:if test="${orglist.showCoding == orgselected}"> selected="selected"</c:if>
						</c:forEach>
						>${orglist.showCoding}
					</option>
				</c:forEach>
			</select>
			签约日期&nbsp;从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}'  pattern="yyyy-MM-dd"/>" id="startDate"/>&nbsp;至&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd" />" id="endDate"/>
			<input type="submit" value="查询" class="ui-state-default">
		</div>
	</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content"><!--   id="mytable" style="font-size:12px;">-->
		<thead>
			<tr class="ui-widget-header ">
				<th>序号</th>
				<th>项目编号</th>
				<th>项目简称</th>
				<th>担保方</th>
				<th>融资方</th>
				<th>融资方交易帐号</th>
				<th>融资方银行帐号</th>
				<th>融资总额</th>
				<th>融资期限</th>
				<th>签约日期</th>
				<th>电子合同汇总</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:if test="${fn:length(pageView.records)==0}"><tr><td colspan="11">没有数据</td></tr></c:if>
		<c:forEach items="${pageView.records}" var="iter" varStatus="iter_s">
			<tr id="s${iter.id}">
				<td>${iter_s.count}</td>
				<td>${iter.code}</td>
				
				<td><a title="${iter.shortName}">${fn:substring(iter.shortName,0,6)}</a></td>
				<td>${iter.createBy.org.shortName}<br/>${iter.createBy.org.showCoding}</td>
				<td>
				<c:if test="${iter.financier.category=='1'}">
					<script>document.write(name("${iter.financier.pName}"));</script>
				</c:if>
				<c:if test="${iter.financier.category!='1'}">
					<script>document.write(name("${iter.financier.eName}"));</script>
				</c:if>
				</td>
				<td>${iter.financier.user.username}</td>
				<td><script>document.write(bankcard("${iter.financier.bankAccount}"));</script></td>
				<td><fmt:formatNumber value="${iter.currenyAmount}" pattern="#,###,##0.00"/></td>
				<td>
				    <c:if test="${(iter.interestDay)!= 0}">${iter.interestDay}天</c:if>
			        <c:if test="${(iter.interestDay)== 0}">${iter.businessType.term}月</c:if> 				
				
				</td>
				<td><fmt:formatDate value="${iter.qianyueDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
					<a href="/back/financingBase/financingBaseAction!prjournaling?id=${iter.id}" target="_blank">电子合同汇总</a><br/>
					<c:if test="${menuMap['print_finance_voucher']=='inline'}"><a href="javascript:;" class="print_finance_voucher" id="${iter.id}">打印融资借据</a></c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="11">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tfoot>
	</table>
</div>
</form>
</body>
