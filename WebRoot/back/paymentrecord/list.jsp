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
<script>
var payment_record;
var payment_id;
$(function(){
	var options={
			dataType:"json",
			success:result,
			timeout:2000
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

	$( "#dialog" ).dialog({
		autoOpen: false,
		show: "blind",
		hide: "explode",
		modal: true,
		resizable:false,
		width:450,
		position: ["top","center"],
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

function payment(obj,id){

	payment_record = obj;
	payment_id = id;
	$("#paymentrecord_id").val(id);
	
	var str_ = $("#div"+id).find(".should_debt_source").html();
	$("#should_debt_label").text(str_);
	$("#paid_debt").val(str_.substr(0,str_.indexOf("元")));//实还本息
	$("#predict_repayment_date_label").text($("#yhksj"+id).html());
	$( "#dialog" ).dialog( "open" );
	return false;
}

//批处理
function batching(obj){
	if($(obj).attr("checked")){
		var ids = $("#div"+payment_id).parent().children(".ids").val();
		$("#paymentrecord_id").val(ids);
	}else{
		$("#paymentrecord_id").val(payment_id);
	}
	
}



function payment_close(){
	$( "#dialog" ).dialog( "close" );
}
function payment_yq_close(){
	$("#extension_period").val("");
	$( "#dialog_yq" ).dialog( "close" );
}
function state(obj,id,s){
	$.post("/back/paymentRecord/paymentRecordAction!state",{"id":id,"state":s},function(data,status){
		$(obj).parent().children("button").not(":first").hide();
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
	return false;
}
function payment_yq_submit(){
	var extension_period = $("#extension_period").val();
	var extension_period_id = $("#extension_period_id").val();
	var extension_period_state = $("#extension_period_state").val();
	$.post("/back/paymentRecord/paymentRecordAction!state?id="+extension_period_id+"&state="+extension_period_state+"&extension_period="+extension_period,{},function(data,status){
		$( "#dialog_yq" ).dialog( "close" );
	});
}

function result(d,s){
	if(s=="success"){
	    if(d.message=="success"){
	    	alert("操作成功");
	  		$( "#dialog" ).dialog( "close" );
	    	$(payment_record).parent().children("botton").remove();
	    	$(payment_record).parent().html("已还款");
	    	
		}else{
			alert(d.message);
		}
	}else{
		alert("服务器未响应，请稍后重试");
	}
}




function date_mouseover(obj){
	$(obj).css({'border':'1px solid black','border-bottom':'none','background-color':'#F3F3F3'}).siblings().css({'border':'none','background-color':''});
	var contents = $(obj).parent().parent().children("div");
	var target = $(contents.get($(obj).index()));
	contents.hide();
	target.show();
}

function showorhidden(obj,str){
	if($(str).height()==30){
		$(obj).attr('src','/Static/js/tree/tabletree/images/minus.gif');
		$(str).css({'height':'auto'});
	}else{
		$(str).css({'height':'30px'});
		$(obj).attr('src','/Static/js/tree/tabletree/images/plus.gif');
	}
}
</script>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="">
	<input type="hidden" name="page" value="1" />
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
	<div style="position:absolute;right:10px;"><button class="ui-state-default reflash" >刷新</button></div>
	<div style="position:absolute;left:10px;">
	项目编号&nbsp;<input type="text" id="fbcode" name="fbcode" value="${fbcode}" style="width:120px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
	投标方帐号&nbsp;<input type="text" id="ivcode" name="ivcode" value="${ivcode}" style="width:120px;"/>&nbsp;&nbsp;
	<button class="ui-state-default">搜索</button>
	</div>
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content" id="mytable" style="font-size:12px;">
		<thead>
			<tr class="ui-widget-header ">
				<th></th>
				<th>项目编号</th>
				<th>项目简称</th>
				<th>融资方名称</th>
				<th>融资方交易帐号</th>
				<th>融资方银行帐号</th>
				<th>融资期限</th>
				<th></th>
				
				
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${pageView.records}" var="iter">
			<c:if test="${ iter.paymentrecords != null }">
				<tr id="s${iter.id}">
					<td><img src="/Static/js/tree/tabletree/images/plus.gif" class="sh" id="${iter.id}"/></td>
					<td>${iter.code}</td>
					
					<td>${iter.shortName}</td>
					<td>
					<c:if test="${iter.financier.category=='1'}">
						${iter.financier.pName}
					</c:if>
					<c:if test="${iter.financier.category!='1'}">
						${iter.financier.eName}
					</c:if>
					</td>
					<td>${iter.financier.user.username}</td>
					<td>${iter.financier.bankAccount}</td>
					<td>
				    <c:if test="${(iter.interestDay)!= 0}">${iter.interestDay}天</c:if>
			        <c:if test="${(iter.interestDay)== 0}">${iter.businessType.term}月</c:if>  
					<td><a href="/back/financingBase/financingBaseAction!prjournaling?id=${iter.id}" target="_blank">电子合同汇总</a></td>
					
					
					
					
				</tr>
				<tr id="h${iter.id}" style="display:none">
					<td style="background-color:#fff"></td>
					<td colspan="10" style="padding:0;margin:0;background-color:#fff" valign="top">
						<ul style="padding:0;margin:5px 0 0 0;width:100%;list-style-type: none;height:30px;border-bottom:1px solid black;">
							<c:forEach items="${iter.paymentrecords}" var="iter_2" varStatus="sta">
								<li style="float:left;margin-left:10px;cursor:pointer;padding-left:5px;padding-right:5px;line-height:30px;${sta.index==0?"border:1px solid black;border-bottom:none;background-color:#F3F3F3":""}" onclick="date_mouseover(this)"><fmt:formatDate value="${iter_2.key}" pattern="yyyy/MM/dd" /></li>
							</c:forEach>
						</ul>
						<c:forEach items="${iter.paymentrecords}" var="iter_2" varStatus="sta">
						<c:set var="ids" value=""/>
							<div style="${sta.index==0?"":"display:none"};background-color:#F3F3F3;">
								<c:forEach items="${iter_2.value}" var="iter_3" varStatus="inner">
									<c:if test="${inner.index!=0}">
										<c:set var="ids" value="${ids}," scope="page"/>
									</c:if>
									<c:set var="ids" value="${ids}${iter_3.id}"/>
									<div style="height:30px;overflow-y:hidden" id="div${iter_3.id}">
										<table cellpadding="0" cellspacing="0" style="margin-top:-1px;margin-left:-1px;width:auto;font-size:12px;" >
											<tr>
												<td width="30" height="30"><img src="/Static/js/tree/tabletree/images/plus.gif" onclick="showorhidden(this,'#div${iter_3.id}')" style="cursor:pointer"/></td>
												<td width="40" style="width:60px">投标方：</td>
												<td width="40" style="width:80px">
													<c:if test="${iter_3.investRecord.investor.category=='1'}">
														${iter_3.investRecord.investor.pName}
													</c:if>
													<c:if test="${iter_3.investRecord.investor.category!='1'}">
														${iter_3.investRecord.investor.eName}
													</c:if>
												</td>
												<td width="50">投标额：</td><td width="100"><fmt:formatNumber value="${iter_3.investRecord.investAmount}" type="currency"/></td>
												
												<td width="80">应还总额</td><td class="should_debt_source" width="300"><fmt:formatNumber value="${iter_3.xybj+iter_3.xylx}" pattern="0.00"/>元(其中本金:<fmt:formatNumber value="${iter_3.xybj}" pattern="0.00"/>元，利息:<fmt:formatNumber value="${iter_3.xylx}" pattern="0.00"/>元)</td>
												<td width="150">
													<c:if test="${iter_3.state==1 || iter_3.state==3 }">已还款<c:if test="${ iter_3.state==3 }"></c:if></c:if>
													<c:if test="${iter_3.state==0}"><button class="ui-state-default" style="display:<c:out value="${menuMap['payhandler']}" />" onclick="payment(this,'${iter_3.id}');return false;">还款</button></c:if>
												</td>
											</tr>
											<tr>
												<td></td>
												<td >投标时间</td><td><fmt:formatDate value="${iter_3.investRecord.createDate}" pattern="yyyy-MM-dd"/></td>
												<td >还款次数</td><td>${iter_3.investRecord.financingBase.businessType.returnTimes}次，当前为第${iter_3.succession}次</td>
												<td >罚金</td><td colspan="2">${iter_3.penal}</td>
												
											</tr>
											<tr>
												<td></td>
												<td>应还时间</td><td id="yhksj${iter_3.id}"><fmt:formatDate value="${iter_3.predict_repayment_date}" pattern="yyyy-MM-dd"/></td>
												<td>实还时间</td>
												<td>
													<c:if test="${iter_3.state == 0}">
														<c:set var="yhksj" value="${iter_3.predict_repayment_date}" scope="page"/>
														<%
															Date now = new Date();
															Date yhksj = (Date)pageContext.getAttribute("yhksj");
															long timer = now.getTime()-yhksj.getTime();
															if(timer > 0 ){
																long dates = timer/1000/60/60/24;
																out.println("已逾期<span style='color:#C82828'>"+dates+"</span>天");
															}else{
																out.println("未到期");
															}
														%>
													</c:if>
													
													<c:if test="${iter_3.state == 1 || iter_3.state == 3}">
														已还款 <fmt:formatDate value="${ iter_3.actually_repayment_date}" pattern="yyyy-MM-dd"/>
													</c:if>
												</td>
												<td >投标合同</td><td colspan="2"><a href="/back/investBaseAction!agreement_ui2?invest_record_id=${iter_3.investRecord.id}" target="_blank">${iter_3.investRecord.contract.contract_numbers }</a></td>
											</tr>
											<c:if test="${fn:startsWith(iter_3.investRecord.financingBase.code, 'X')}"> 
												<tr>
													<td></td>
													<td colspan="2">融资服务费:<fmt:formatNumber value="${iter_3.fee_1}" type="currency"/></td>
													<td colspan="3">担保费:<fmt:formatNumber value="${iter_3.fee_2}" type="currency"/></td>
												</tr>
											</c:if>
										</table>
									</div>
								</c:forEach>
								<input type="hidden" class="ids" value="<c:out value="${ids }"/>"/>
							</div>
						</c:forEach>
					</td>
				</tr>
			</c:if>
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
		<input type="hidden" name="ids" id="paymentrecord_id"/>
		<table style="font-size:13px;">
			<tr><td width="100">应还本息</td><td><label id="should_debt_label"></label></td></tr>
			
			<tr><td>实还本息</td><td><input type="text" name="paid_debt" id="paid_debt" style="width:260px;padding:3px;"/></td></tr>
			<tr><td>罚金金额</td><td><input type="text" name="penal" style="width:260px;padding:3px;"/></td></tr>
			<!-- 
			<tr><td>确认还款金额</td><td><input type="text" name="re_paid_debt" id="re_paid_debt" style="width:260px;padding:3px;"/></td></tr>
			 -->
			<tr><td>应还款日期</td><td><label id="predict_repayment_date_label"></label></td></tr>
			<tr><td valign="top">备注</td><td><textarea name="remark" id="remark" style="width:260px;padding:3px;height:150px;"></textarea></td></tr>
			<tr><td><input type="checkbox" onclick="batching(this)"/>批处理</td><td align="right"><button class="ui-state-default" style="width:100px;height:30px;" onclick="return payment_submit();">确认</button>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="payment_close()">取消</a></td></tr>
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
