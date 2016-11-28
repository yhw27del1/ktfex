<%-- 
2012-08-24 aora 修改此页面：更正在IE6、IE8下不能提交查询条件的问题
--%>
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
</style>
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/jquery.form.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<script>
$(function(){
	var options={
			dataType:"json",
			success:result
	}
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
		
		iframe.height($("#mytable").height()+200);
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

	$( "#dialog" ).dialog({
		autoOpen: false,
		show: "blind",
		hide: "explode",
		modal: true,
		resizable:false,
		width:450,
		position: ["right","center"],
		close:function(){
			$("#payment_form").validate().resetForm();
		}
	});
	$( "#dialog_yq" ).dialog({
		autoOpen: false,
		show: "blind",
		hide: "explode",
		modal: true,
		resizable:false,
		width:300,
		position: ["right","center"],
		close:function(){
			$("#payment_form").validate().resetForm();
		}
	});
	
	$("#payment_form").validate({
        rules: {
        	"actually_repayment_date":{required:true}
        },  
        messages: {
            "actually_repayment_date":{required:"请选择实际还款时期"}
        }    
	});   
 
	$("#payment_form").ajaxForm(options); 
	var height = $("#mytable").height()<600?600:$("#mytable").height()+50;
	$(".autoheight").val(height);
	$(".actually_repayment_date_controller").datepicker({
		 	showOn: 'button',
	        buttonImageOnly: false,
	        dateFormat: "yy-mm-dd"
	    });
	     $("#ui-datepicker-div").css({'display':'none'});
});

function payment(id){
	$("#paymentrecord_id").val(id);
	$("#should_debt_label").text($("#h"+id).find(".should_debt_source").html());
	$("#predict_repayment_date_label").text($("#s"+id).find(".predict_repayment_date_source").html());
	$( "#dialog" ).dialog( "open" );
}
function payment_close(){
	$( "#dialog" ).dialog( "close" );
}
function payment_yq_close(){
	$("#extension_period").val("");
	$( "#dialog_yq" ).dialog( "close" );
}
function state(id,s){
	$.post("/back/paymentRecord/paymentRecordAction!state",{"id":id,"state":s},function(data,status){
		window.location.reload();
	});
}
function state_yq(id,s){
	$("#predict_repayment_date_label_yq").text($("#s"+id).find(".predict_repayment_date_source").html());
	$("#extension_period_id").val(id);
	$("#extension_period_state").val(s);
	$( "#dialog_yq" ).dialog( "open" );
}

function payment_submit(){
	$("#payment_form").submit();
}
function payment_yq_submit(){
	var extension_period = $("#extension_period").val();
	var extension_period_id = $("#extension_period_id").val();
	var extension_period_state = $("#extension_period_state").val();
	$.post("/back/paymentRecord/paymentRecordAction!state",{"id":extension_period_id,"state":extension_period_state,"extension_period":extension_period},function(data,status){
		window.location.reload();
	});
}

function result(d,s){
	if(s=="success"){
	    if(d.message=="success"){
	    	window.location.reload();
		}else{
			alert(d.message);
		}
	}else{
		alert("服务器未响应，请稍后重试");
	}
}

</script>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="back/paymentRecord/paymentRecordAction!list_3">
	<input type="hidden" name="page" value="1" />
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
	<div style="position:absolute;right:10px;"><button class="ui-state-default reflash" >刷新</button></div>
	<div style="position:absolute;left:10px;">
	项目编号&nbsp;<input type="text" id="fbcode" name="fbcode" value="${fbcode}" style="width:120px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
	投标方帐号&nbsp;<input type="text" id="ivcode" name="ivcode" value="${ivcode}" style="width:120px;"/>&nbsp;&nbsp;
	实际还款日&nbsp;从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;至&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
	<input type="submit" value="查询" class="ui-state-default">
    <c:if test="${!empty pageView.records}">
       &nbsp;&nbsp;&nbsp;&nbsp; <a style="color:red;"  href="/back/paymentRecord/paymentRecordAction!list_3?fbcode=${fbcode}&ivcode=${ivcode}&startDate=<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" />&endDate=<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd" />&excelFlag=1"  title="结果导出EXCEL"><img src="/Static/images/excel.gif"></a>
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
			<tr id="s${iter.id}">
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
				<td class="predict_repayment_date_source">
					<fmt:formatDate value="${iter.predict_repayment_date}" pattern="yyyy-MM-dd"/>
				</td>
				<td >
					<fmt:formatDate value="${iter.actually_repayment_date}" pattern="yyyy-MM-dd"/>
				</td>
			</tr>
			<tr id="h${iter.id}" style="display:none">
				<td style="background-color:#fff"></td>
				<td colspan="10" style="padding:0;margin:0;background-color:#fff" valign="top">
					<table cellpadding="0" cellspacing="0" style="margin-top:-1px;margin-left:-1px;width:auto;font-size:12px;" >
						<tr>
							<td width="80">已融资额</td><td width="150">${iter.investRecord.financingBase.currenyAmount}</td>
							<td width="80">项目简称</td><td width="200">${iter.investRecord.financingBase.shortName}</td>
						</tr>
						<tr>
							<td width="80">融资利率(年)</td><td width="150">${iter.investRecord.financingBase.rate}%</td>
							<td width="80">融资期限</td><td width="200">
							<c:if test="${(iter.investRecord.financingBase.interestDay)!= 0}">按日计息(${iter.investRecord.financingBase.interestDay}天)</c:if>
			                <c:if test="${(iter.investRecord.financingBase.interestDay)== 0}">${iter.investRecord.financingBase.businessType.term}个月 </c:if>    						

						</tr>
						<tr>
							<td width="80">还款方式</td><td width="150">${iter.investRecord.financingBase.businessType.returnPattern}</td>
							<td width="80">还款次数</td><td width="200">${iter.investRecord.financingBase.businessType.returnTimes}次，当前为第${iter.succession}次</td>
						</tr>
						<tr>
							<td width="80">投标合同</td><td width="200"><a href="/back/investBaseAction!agreement_ui?invest_record_id=${iter.investRecord.id}" target="_blank">${iter.investRecord.contract.contract_numbers }</a></td>
							<td width="80">投标时间</td><td width="200">${iter.investRecord.createDate}</td>
						</tr>
						<tr>
							<td width="80">应还总额</td><td colspan="3" class="should_debt_source"><fmt:formatNumber value="${iter.xybj+iter.xylx}" pattern="0.00"/>元(其中本金:<fmt:formatNumber value="${iter.xybj}" pattern="0.00"/>元，利息:<fmt:formatNumber value="${iter.xylx}" pattern="0.00"/>元)</td>
						</tr>
						<tr>
							<td width="80">实还总额</td><td colspan="3" class="should_debt_source"><fmt:formatNumber value="${iter.paid_debt+iter.penal}" pattern="0.00"/>元(实还本息<fmt:formatNumber value="${iter.paid_debt}" pattern="0.00"/> + 罚金<fmt:formatNumber value="${iter.penal}" pattern="0.00"/>)</td>
						</tr>
					</table>
				</td>
			</tr>
		</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="10">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tfoot>
	</table>
</div>
</form>
</body>
<div id="dialog" title="确认还款" style="display:none;">
	<form action="/back/paymentRecord/paymentRecordAction!die" id="payment_form">
		<input type="hidden" name="id" id="paymentrecord_id"/>
		<table style="font-size:13px;">
			<tr><td width="100">应还款额</td><td><label id="should_debt_label"></label></td></tr>
			<!-- 
			<tr><td>实际还款金额</td><td><input type="text" name="paid_debt" id="paid_debt" style="width:260px;padding:3px;"/></td></tr>
			<tr><td>确认还款金额</td><td><input type="text" name="re_paid_debt" id="re_paid_debt" style="width:260px;padding:3px;"/></td></tr>
			 -->
			<tr><td>应还款日期</td><td><label id="predict_repayment_date_label"></label></td></tr>
			<tr><td>实际还款日期</td><td><input onselect="return false" readonly="readonly" type="text" name="actually_repayment_date" class="actually_repayment_date_controller" style="width:260px;padding:3px;"/></td></tr>
			<tr><td valign="top">备注</td><td><textarea name="remark" id="remark" style="width:260px;padding:3px;height:150px;"></textarea></td></tr>
			<tr><td></td><td align="right"><button class="ui-state-default" style="width:100px;height:30px;" onclick="payment_submit();return false;">确认</button>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="payment_close()">取消</a></td></tr>
		</table>
	</form>
</div>
<div id="dialog_yq" title="延期还款" style="display:none">
	<input type="hidden" id="extension_period_id"/>
	<input type="hidden" id="extension_period_state"/>
	<table style="font-size:13px;">
	
		<tr><td>应还款日期</td><td><label id="predict_repayment_date_label_yq"></td></tr>
		<tr><td>延期日期</td><td><input onselect="return false" readonly="readonly" type="text" name="extension_period" id="extension_period" class="actually_repayment_date_controller" style="width:160px;padding:3px;"/></td></tr>
		<tr><td></td><td align="right"><button class="ui-state-default" style="width:100px;height:30px;" onclick="payment_yq_submit();return false;">确认</button>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="payment_yq_close()">取消</a></td></tr>
	</table>
</div>
