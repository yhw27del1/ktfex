<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript">
function canDate(){
	//主期间，日期可以选择
	$("#startDate").datepicker({
		numberOfMonths: 1,
        dateFormat: "yy-mm-dd"
    });
	$("#endDate").datepicker({
		numberOfMonths: 1,
        dateFormat: "yy-mm-dd"
    });
	$(".subdate").datepicker({
		numberOfMonths: 1,
        dateFormat: "yy-mm-dd"
    });
    $("#ui-datepicker-div").css({'display':'none'});
}
function main_readonly(){
	//主期间，利率数；年利率不可输入
	$("#interval").attr('readonly',true);
   	$("#rate").attr('readonly',true);
   	//主期间，日期不能选择
   	$("#startDate").datepicker('destroy');
   	$("#endDate").datepicker('destroy');
}
function reset(){
	canDate();
	//主期间，利率数；年利率可以输入
	$("#interval").attr('readonly',false).val(1);
   	$("#rate").attr('readonly',false).val(0.2);
   	
   	//下一步按钮可以点击
   	$("#next").attr('disabled',false);
   	
   	$("#startDate").val('');
   	$("#endDate").val('');
   	$("#content").html('');
   	$("#sub").hide();
}
$(function(){
	canDate();
    $("#next").click(function(){
    	var s = $("#interval").val();
    	var r = $("#rate").val();
    	var start = $("#startDate").val();
    	var end = $("#endDate").val();
    	var dstart = new Date(start);
    	var dend = new Date(end);
    	dstart.setDate(dstart.getDate()+1);
    	dend.setDate(dend.getDate()-1);
    	if(!start){
    		alert("请输入起始日期");
    		return false;
    	}
    	if(!end){
    		alert("请输入截止日期");
    		return false;
    	}
    	if(!r){
    		alert("请输入年利率");
    		return false;
    	}
    	if(!s){
    		alert("请输入利率数");
    		return false;
    	}
    	$(this).attr('disabled',true);
    	main_readonly();//控制主期间的值不能修改
    	$("#reset").show();
    	if(s>1){
    		$("#rate").val(0);
    		var content = "";
    		var br = "<br />";
    		for(var i=1;i<=s;i++){
    			if(i==s){
    				br = "";
    			}
    			content +="起始日期&nbsp;<input type='text' readonly='true' id='sub_start_"+i+"' name='main.sub_start_"+i+"' size='8' />&nbsp;截止日期&nbsp;<input class='subdate' type='text' readonly='true' id='sub_end_"+i+"' name='main.sub_end_"+i+"' size='8' />&nbsp;年利率&nbsp;<input type='text' name='main.sub_rate_"+i+"' size='2' />%"+br;
    		}
    		content += "<input type='button' value='下一步' id='next2' />";
    		$("#content").html("").html(content);
    		$(".subdate").datepicker({
    			minDate: dstart,
    			maxDate: dend,
				numberOfMonths: 1,
		        dateFormat: "yy-mm-dd",
		        onSelect: function(selectedDate){
		        	var date_next = new Date(selectedDate);
		        	date_next.setDate(date_next.getDate()+1);
		        	var date_next_next = new Date(date_next);
		        	date_next_next.setDate(date_next_next.getDate()+1);
		        	var str = date_next.getFullYear()+"-"+(date_next.getMonth()+1)+"-"+date_next.getDate();
		        	var n = $(this).next().next().next();
		        	n.attr('readonly',true).val(str);
		        	n.next().datepicker( "option", "minDate", date_next_next);
		        }
		    });
    		$("#next2").click(function(){
    			var $input = $("input[name]","#content");
    			var hasnull = false;//输入框是否有null值
    			$.each($input,function(key,value){
    				if(!$(value).val()){
    					hasnull = true;
    					return false;//一发现有null值，终止循环
    				}
    			});
    			if(hasnull){
    				alert("子期间中所有的值都必须输入");
    			}else{
    				$.each($input,function(key,value){
	    				$(value).attr('readonly',true).datepicker('destroy');
	    			});
    				$(this).attr('disabled',true);
    				$("#form").submit();
    			}
    		});
    		$("#ui-datepicker-div").css({'display':'none'});
    		$("#sub_start_1").val(start);
    		$("#sub_end_"+s).val(end).datepicker('destroy');
    		$("#sub").show();
    	}else{
    		$("#form").submit();
    	}
    });
    $("#reset").click(function(){
    	reset(); 
    	$(this).hide();
    });
});
</script>
<form id="form" action="/back/accrual/accrualAction!calc" method="get" target="_black">
	<fieldset id="main">
	    <legend>会员利息计算主期间</legend>
			起始日期&nbsp;<input type="text" readonly="true" name="main.main_start" value="<fmt:formatDate value='${startDate}' type='date' />" size="8" id="startDate"/>
			截止日期&nbsp;<input type="text" readonly="true" name="main.main_end" value="<fmt:formatDate value='${endDate}' type='date' />" size="8" id="endDate"/>
			年利率&nbsp;<input type="text" name="main.main_rate" value="0.2" size="2" id="rate" />%
			<span style="cursor:hand;" title="主期间使用的年利率标准个数">年利率数</span>&nbsp;<input title="请输入1至3之内的数字" type="text" name="main.interval" value="1" size="2" maxlength="1" onkeyup="this.value=this.value.replace(/[^1-3]/g,'')" id="interval" />
			会员类型&nbsp;<select id="userType" name="userType">
				<option value="all">全部</option>
				<option value="T">投资人</option>
				<option value="R">融资方</option>
				<option value="D">担保方</option>
			</select>
			专户&nbsp;<select id="channel" name="channel">
				<option value="cmb">招行</option>
				<option value="icbc">工行</option>
			</select>
			签约状态&nbsp;<select id="signType" name="signType">
				<option value="all">全部</option>
				<option value="0">未签约</option>
				<option value="1">签约中</option>
				<option value="2">已签约</option>
				<option value="3">已解约</option>
			</select>
			备注&nbsp;<input type="text" value="" name="main.memo" />
			<input type="button" value="下一步" id="next" />
			<input type="button" value="重置" id="reset" style="display: none;" />
	</fieldset>
	<fieldset id="sub" style="display: none;">
	    <legend>会员利息计算子期间</legend>
		<span id="content"></span>
	</fieldset>
</form>
</body>
