<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<style>
.ui-autocomplete {
	width: 120px;
	overflow: hidden;
	padding: 0;
	margin: 0;
}

.ui-autocomplete li {
	width: 120px;
	list-style-type: none;
	padding: 0;
	margin: 0;
}

.ui-autocomplete li a:HOVER {
	background-image: none;
}

.error {
	float: left;
}
</style>
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script>
<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css" />
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
<script>
/*var payment_record;
var payment_id;
$(function(){
	var options={
			dataType:"json",
			success:result,
			timeout:2000
	}
	
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
	$(".actually_repayment_date_controller").datepicker({
		 	showOn: 'button',
	        buttonImageOnly: false,
	        dateFormat: "yy-mm-dd"
	    });
	     $("#ui-datepicker-div").css({'display':'none'});
	     
	
});*/

/*function payment(obj,id){

	payment_record = obj;
	payment_id = id;
	$("#paymentrecord_id").val(id);
	if($(payment_record).attr("fxbz")=="2"){
		$("tr.xyd").show();
	}else{
		$("tr.xyd").hide();
	}
	var str_ = $("#should_debt_source"+id).text();
	$("#should_debt_label").text(str_);
	$("#paid_debt").val(str_.substr(0,str_.indexOf("元")));//实还本息
	$("#predict_repayment_date_label").text($("#yhksj"+id).html());
	$( "#dialog" ).dialog( "open" );
	return false;
}*/

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
	    location.href = location.href;
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
<body>
	<input type='hidden' class='autoheight' value="auto" />
	<form action="">
		<input type="hidden" name="page" value="1" />
		<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
			<div style="position: absolute; right: 10px;">
				<button class="ui-state-default reflash">
					刷新
				</button>
			</div>
			<div style="position: absolute; left: 10px;">
				项目编号&nbsp;
				<input type="text" id="fbcode" name="fbcode" value="${fbcode}" style="width: 120px;" />
				&nbsp; 应还日期&nbsp;从
				<input type="text" name="startDate" value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"  />" id="startDate" />
				&nbsp;到&nbsp;
				<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd" />" id="endDate" />
				&nbsp;
				<button class="ui-state-default">
					查询
				</button>
				<c:if test="${!empty pageView.records}">
					        &nbsp;&nbsp;&nbsp;&nbsp; <a style="color: red;"
						href="/back/paymentRecord/paymentRecordAction!list_wait_date?startDate=<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" />&endDate=<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd" />&excelFlag=1&fbcode=${fbcode}"
						title="结果导出EXCEL"><img src="/Static/images/excel.gif">
					</a>
				</c:if>
			</div>
		</div>

		<div class="dataList ui-widget">
			<table class="ui-widget ui-widget-content" id="mytable" style="font-size: 12px;">
				<thead>
					<tr class="ui-widget-header ">
						<th></th>
						<th>
							项目编号
						</th>
						<th>
							融资方帐号
						</th>
						<th>
							融资方银行帐号
						</th>
						<th>
							融资方
						</th>
						<th>
							投标方帐号
						</th>
						<th>
							投标方
						</th>
						<th style="text-align: right;">
							投标金额
						</th>
						<th>
							应还总额
						</th>
						<th>
							应还日期
						</th>
						<!--  <th></th>-->
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:forEach items="${pageView.records}" var="iter">
						<tr>
							<td>
								<img src="/Static/js/tree/tabletree/images/plus.gif" class="sh" id="${iter.id}" />
							</td>
							<td>
								${iter.investRecord.financingBase.code}
							</td>
							<td>
								${iter.investRecord.financingBase.financier.user.username}
							</td>
							<td>
								<script>document.write(bankcard("${iter.investRecord.financingBase.financier.bankAccount}"));</script>
							</td>
							<td>
								<c:if test="${iter.investRecord.financingBase.financier.category=='1'}">
									<script>document.write(name("${iter.investRecord.financingBase.financier.pName}"));</script>
								</c:if>
								<c:if test="${iter.investRecord.financingBase.financier.category!='1'}">
									<script>document.write(name("${iter.investRecord.financingBase.financier.eName}"));</script>
								</c:if>
							</td>
							<td>
								${iter.investRecord.investor.user.username}
							</td>
							<td>
								<c:if test="${iter.investRecord.investor.category=='0'}">
									<script>document.write(name("${iter.investRecord.investor.eName}"));</script>
				   				</c:if>
								<c:if test="${iter.investRecord.investor.category!='0'}">
									<script>document.write(name("${iter.investRecord.investor.pName}"));</script>
				   				</c:if>
							</td>
							<td style="text-align: right;">
								<fmt:formatNumber value="${iter.investRecord.investAmount}" pattern="0.00" />
							</td>
							<td id="should_debt_source${iter.id}">
								<fmt:formatNumber value="${iter.xybj+iter.xylx}" pattern="0.00" />
								元(本金:
								<fmt:formatNumber value="${iter.xybj}" pattern="0.00" />
								元，利息:
								<fmt:formatNumber value="${iter.xylx}" pattern="0.00" />
								元)
							</td>
							<td id="yhksj${iter.id}">
								<fmt:formatDate value="${iter.predict_repayment_date}" pattern="yyyy-MM-dd" />
							</td>
							<!--  <td><c:if test="${iter.state==0}"><button fxbz="${iter.investRecord.financingBase.fxbzState}" class="ui-state-default" style="display:<c:out value="${menuMap['payhandler']}" />" onclick="payment(this,'${iter.id}');return false;">还款</button></c:if></td>-->
						</tr>
						<tr id="h${iter.id}" style="display: none">
							<td style="background-color: #fff"></td>
							<td colspan="9" style="padding: 0; margin: 0; background-color: #fff" valign="top">
								<table cellpadding="0" cellspacing="0" style="margin-top: -1px; margin-left: -1px; width: auto;">
									<tr>
										<td width="80">
											年利率
										</td>
										<td width="150">
											${iter.investRecord.financingBase.rate}%
										</td>
										<td width="80">
											融资期限
										</td>
										<td width="200">
											<c:if test="${(iter.investRecord.financingBase.interestDay)!= 0}">按日计息(${iter.investRecord.financingBase.interestDay}天)</c:if>
											<c:if test="${(iter.investRecord.financingBase.interestDay)== 0}">${iter.investRecord.financingBase.businessType.term}个月 </c:if>

										</td>
									</tr>
									<tr>
										<td width="80">
											还款方式
										</td>
										<td width="150">
											${iter.investRecord.financingBase.businessType.returnPattern}
										</td>
										<td width="80">
											还款期次
										</td>
										<td width="200">
											${iter.investRecord.financingBase.businessType.returnTimes}期，当前为第${iter.succession}期次
										</td>
									</tr>
									<tr>
										<td width="80">
											投标合同
										</td>
										<td width="200">
											<a href="/back/investBaseAction!agreement_ui2?invest_record_id=${iter.investRecord.id}" target="_blank">${iter.investRecord.contract.contract_numbers }</a>
										</td>
										<td width="80">
											投标时间
										</td>
										<td width="200">
											<fmt:formatDate value="${iter.investRecord.createDate}" pattern="yyyy-MM-dd" />
										</td>
									</tr>
									<tr>
										<td width="80">
											应还总额
										</td>
										<td colspan="3" class="should_debt_source">
											<fmt:formatNumber value="${iter.xybj+iter.xylx}" pattern="0.00" />
											元(其中本金:
											<fmt:formatNumber value="${iter.xybj}" pattern="0.00" />
											元，利息:
											<fmt:formatNumber value="${iter.xylx}" pattern="0.00" />
											元)
										</td>
									</tr>
								</table>
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

