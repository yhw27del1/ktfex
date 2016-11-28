<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<html>
	<head>
		<title></title>
		<link rel="stylesheet" href="/Static/js/showloading/css/showLoading.css" type="text/css" />
		<link rel="stylesheet" href="/Static/css/metro-bootstrap.css" type="text/css" />
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="/Static/js/showloading/jquery.showLoading.min.js"></script>
		<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js?self=true&skin=discuz"></script>
		<style>
			
			body {font-size:13px;padding:0;margin:0;background-color: #fff;}
			.i1{width:100px;}
			.i2{
				width:110px;
			}
			.i3{width:70px;}
			li{
				list-style-type: none;
				line-height:40px;
				float:left;margin-right:20px;
			}
			ul{
				margin:0;
				padding:0;
				clear: both;
			}
			.succession{
				width:80px;
			}
			.days{
				width:30px;
			}
			.controls_b a,.same_value_text,.select_button{
				text-decoration: none;
				font-size:10px;
				-webkit-text-size-adjust:none;
				
			}
			
			
			#zhinajinbili,#returntimes{
				width:25px;
				text-align: right;
			}
			table th{
				padding-left:3px;
				padding-right:0px;
			}
			
			#toolbar{
				position: fixed;
				top:5px;
				right:5px;
			}
			#close{
				font-size:15px;
				font-weight: bold;
			}
			.transfer_history{
				color:#1984E8;
			}
			input[disabled],input[readonly],input[disabled]:hover,input[readonly]:hover{
				border:none !important;
				-webkit-box-shadow:none;
				outline: none !important;
				cursor: default;
			}
			#succession_info{font-size:14px;}
			#data_table{font-size:13px;}
			#data_table th, #data_table td {padding:4px;}
		</style>
		<script type="text/javascript">
		
			
			Array.max=function(array){
			    return Math.max.apply(Math,array);
			}
			Array.min=function(array){
			    return Math.min.apply(Math,array);
			}
			Date.prototype.format =function(format){
				var o = {
					"M+" : this.getMonth()+1, //month
					"d+" : this.getDate(), //day
					"h+" : this.getHours(), //hour
					"m+" : this.getMinutes(), //minute
					"s+" : this.getSeconds(), //second
					"q+" : Math.floor((this.getMonth()+3)/3), //quarter
					"S" : this.getMilliseconds() //millisecond
				}
				if(/(y+)/.test(format)){
					format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4- RegExp.$1.length));
				}
				for(var k in o)if(new RegExp("("+ k +")").test(format)){
					format = format.replace(RegExp.$1, RegExp.$1.length==1? o[k] :("00"+ o[k]).substr((""+ o[k]).length));
				}
				return format;
			}
			
			
			
			$(function(){
				$("[readonly='readonly']").focus(function(){
					$(this).blur();
				})
			
				$.ajaxSetup({
					async:true
				});  
				$(".benjin,.lixi").change(function(){
					var benxiheji_input = $(this).parents("tr").find("input[name='benxiheji']");
					var lixi_input = $(this).parents("tr").find("input[name='lixi']");
					var benjin_input = $(this).parents("tr").find("input[name='benjin']");
					var benjin = Number(benjin_input.val());
					var lixi = Number(lixi_input.val());
					benxiheji_input.val((benjin+lixi).toFixed(2));
					benxiheji_input.trigger("change");
				});
				$("#data_table tbody .statistics").change(function(){
					var this_index = $(this).parents("tr").find(".statistics").index(this);
					$(this).val(Number($(this).val()).toFixed(2));
					var sum = 0;
					$(this).parents("tbody").children("tr[class!='disabled']").each(function(){
						var obj = $($(this).find(".statistics").get(this_index));
						sum += Number(obj.val());
					});
					var target = $($(this).parents("table").children("tfoot").find(".statistics").get(this_index));
					target.val(sum.toFixed(2));
					target.trigger("change");
				}).click(function(){
					$(this).select();
				});
				$("#data_table tfoot .statistics").change(function(){
					var value = 0;
					$("tfoot .statistics").each(function(){
						if(!$(this).hasClass("notsum")){
							value += Number($(this).val());
						}
					});
					$(".sum_all").val(value.toFixed(2));
				});
				$("#data_table tbody .statistics").trigger("change");
				$("#help_succession").click(function(){
					window.open("/back/paymentrecord/help_succession.html");
				});
				$("input[readonly]").css({'border':'none','background-color':'transparent'});
				$("#yqts").change(function(){
					if(!isNaN($(this).val())){
						if(Number($(this).val()) > 0){
							$("#tqts").val(0).attr("disabled",true);
							var num = Number($(this).val());
							var zhinajinbili = $("#zhinajinbili").val();
							$('input[name="fajin"],input[name="rongzifuwufei_fajin"],input[name="danbaofei_fajin"],input[name="fengxianguanlifei_fajin"]').each(function(){
								var td = $(this).parent("td").prev();
								var input = td.find("input");
								$(this).val((input.val()*zhinajinbili/100*num).toFixed(2));
								$(this).trigger("change");
							});
						}else{
							$("#tqts").val(0).attr("disabled",false);
						}
					}
				});
				$("#tqts").change(function(){
					if(!isNaN($(this).val())){
						if(Number($(this).val()) > 0){
							$("#yqts").val(0).attr("disabled",true);
						}else{
							$("#yqts").val(0).attr("disabled",false);
						}
					}
				});
				
				$('#checkall').click(function(){
					if($(this).is(":checked")){
						$("input[class='tr_checkbox']").attr("checked","true");
					}else{
						$("input[class='tr_checkbox']").removeAttr("checked");
					}
					tr_checkbox_change();
				});
				$('#invert').click(function(){
					$("input[class='tr_checkbox']").each(function(){
						$(this).is(":checked")?$(this).removeAttr("checked"):$(this).attr("checked","true");
					});
					if($("input[class='tr_checkbox']").not(":checked").length > 0 ){
						$('#checkall').removeAttr("checked");
					}else{
						$('#checkall').attr("checked","true");
					}
					tr_checkbox_change();
				});
				$("input[class='tr_checkbox']").click(function(){
					if($("input[class='tr_checkbox']").not(":checked").length > 0 ){
						$('#checkall').removeAttr("checked");
					}else{
						$('#checkall').attr("checked","true");
					}
					tr_checkbox_change();
				});
				
				//同值按钮，在标题的右侧，点击后将该列的所有输入框的值指定为第一行的输入框的值
				$(".controls_b").append('<a href="javascript:;" class="same_value">[同值]</a>');
				$(".same_value").click(function(){
					var index = $(this).parent().index();
					var tds = [];
					$(this).parents("table").children("tbody").children('tr[class!="disabled"]').each(function(){
						tds.push($(this).find('td:eq('+index+')'));
					});
					for(var x = 0; x < tds.length; x++){
						tds[x].find("input").val(Number(tds[0].find("input").val()).toFixed(2));
					}
					
					$("#data_table tbody .statistics").trigger("change");
				});
				$(".same_value_text").click(function(){
					var index = $(this).parent().index();
					var tds = [];
					$(this).parents("table").children("tbody").children('tr[class!="disabled"]').each(function(){
						tds.push($(this).find('td:eq('+index+')'));
					});
					for(var x = 0; x < tds.length; x++){
						tds[x].find("input").val(tds[0].find("input").val());
					}
				});
				
				
				$("#data_table tbody .statistics[readonly!='readonly']").keypress(function(e) {  
					if (e.which == 13){
						var tr = $(this).parents("tr");
						var tr_inputs = tr.find(".statistics[readonly!='readonly']");
						var index = tr_inputs.index($(this));
						var next_tr = tr.next("[class!='disabled']");
						var input = next_tr.find(".statistics[readonly!='readonly']").get(index);
						if(next_tr.length == 0 || input.length == 0 && index < tr_inputs.length - 1 ){
							tr = $(this).parents("tbody").find("tr[class!='disabled']:first");
							index++;
							input = tr.find(".statistics[readonly!='readonly']").get(index);
							if(input != null ){
								input.focus();
								input.select();
							}
						}else{
							input.focus();
							input.select();
						}
						return false;// 取消默认的提交行为  
					}  
				});  
				
				/*
				$("#isDBDC").click(function(){
					if($(this).is(":checked")){
						$(".i3").each(function(){
							$(this).attr("old_val",$(this).val()).val("0.00").trigger("change");
						});
					} else {
						$(".i3").each(function(){
							if(typeof($(this).attr("old_val")) !='undefined'){
								$(this).val($(this).attr("old_val")).trigger("change");
							}
						});
					}
				});
				*/
				
				$("#submit").click(function(){
					
					var s_text = $("#succession_input").val();
					var succession = parser(s_text);
					if( succession.length == 0 ){
						$.dialog.alert('还款期数不正确，请按规则填写!');
						$("#succession_input").focus().select();
						return;
					}
					for(var x in succession){
						if(isNaN(succession[x]) || succession[x]<=0 || Array.max(succession)> $("#returntimes").val()){
							$.dialog.alert('还款期数不正确，请按规则填写!');
							$("body").hideLoading();
							$("#succession_input").focus().select();
							return;
						}
					}
					var yqts = Number($("#yqts").val());
					var tqts = Number($("#tqts").val());
					if(isNaN(yqts) && isNaN(tqts)){
						$.dialog.alert('逾期或提前天数不正确，请按规则填写!');
						return;
					}
					var days = (!isNaN(tqts) && Number(tqts)>0 ) ? -tqts : yqts;
					/*
						判断融资方帐户余额
					*/
					var all_money = succession.length * $(".sum_all").val();
					if(Number($("#userAccount_balance").val())< all_money ){
						$.dialog.alert("融资方帐户余额不足！此次还款为"+succession.length+"期，共需要"+succession.length+"*"+$(".sum_all").val()+"="+all_money.toFixed(2)+"元");
						return;
					}
					
					var isDBDC = $("#isDBDC").is(":checked");
					
					if(confirm("即将对该融资项目的第"+succession+"期进行还款，确认继续")){
						var c = 0;
						var json_str = '{';
						json_str += '"financingbaseid":"${id}"';
						json_str += ',"days":'+days;
						json_str += ',"succession":"['+succession+']"';
						json_str += ',"dbdc":"'+isDBDC+'"';
						json_str += ',"records":[';
						$("#data_table tbody tr[class!='disabled']").each(function(){
							var this_ = $(this);
							if( c != 0) json_str += ",";
							json_str += '{';
							var x = 0;
							this_.find(".submit").each(function(){
								if(x != 0) json_str += ",";
								x++;
								json_str += '"'+$(this).attr("name")+'":"'+$(this).val()+'"';
							});
							json_str += '}';
							c++;
						});
						json_str += ']}';
						
						
						var dialog = $.dialog({
							id:'tip',
							title:false,
							fixed: true,
							lock: true,
						    content: '<b>正在提交数据，还款过程需要时间，系统建议您等会儿~。<br/>如果长时间无响应，肯定是卡住了，请猛击刷新按钮。</b>',
						    icon: 'loading.gif'
						}); 
						
						
						$.post("/back/paymentRecord/paymentRecordAction!die",{"parameters":json_str},function(data, status){
							dialog.close();
							if(data['code']=='error'){
								$.dialog({
									id:'tip',
									title:"失败",
									fixed: true,
									lock: true,
								    content: '错误提示'+data['tip'],
								    icon: 'error.gif',
								    max: false,
								    min: false
								});
							}else if(data['code']=='success'){
								$.dialog({
									id:'tip',
									title: "成功",
									fixed: true,
									lock: true,
								    content: data['tip'],
								    icon: 'success.gif',
								    max: false,
								    min: false
								});
							}
							if (typeof(data['balance']) != "undefined") {
								$("#userAccount_balance").val(Number(data['balance']).toFixed(2));
							}
							
						},'json');
					}
					
					
					
					
					
					
				});
				//此函数负责提取还款期数
				function parser(str){
					var result = [];
					if(str.length > 0)
					if( str.indexOf("~") == -1 && str.indexOf(",") == -1 ){
						if(!isNaN(str)){
							result.push(parseInt(str));
						}
					}else{
						if( str.indexOf(",") != -1 ){
							var temp = str.split(",");
							for(var t in temp){
								if(temp[t].indexOf("~") != -1){
									var temp_ = temp[t].split("~");
									for(var t_ = temp_[0]; t_ <= temp_[1]; t_++){
										result.push(t_);
									}
								}else{
									result.push(temp[t]);
								}
							}
						}else{
							var temp = str.split("~");
							for(var t = temp[0]; t <= temp[1]; t++){
								result.push(t);
							}
						}
					}
					return result
				}
				
				function tr_checkbox_change(){
					$("input[class='tr_checkbox']").each(function(){
						if($(this).is(":checked")){
							$(this).parents("tr").removeClass("disabled").find("input:text").removeAttr("disabled");
						}else{
							$(this).parents("tr").addClass("disabled").find("input:text").attr("disabled","disabled");
						}
					});
					$("#data_table tbody .statistics").trigger("change");
				}
				
				$("#close").click(function(){
					window.parent.close_children_window();
				});
				
				$(".transfer_history").click(function(){
					window.open("/back/zhaiquan/contractAction!listByInvestRecordId?investRecordId="+$(this).next().val());
				});
				
				$("#succession_input").change(function(){
					if(isNaN($(this).val())) return;
					var succession = $(this).val();
					$.post("/back/paymentRecord/paymentRecordAction!get_record_info",{"fid":"${id}","succession":$(this).val()},function(data,state){
						var str = '';
						if(data['num'] >= 0 ){
							str += '第 '+succession+' 期已还'+(data['num']+1)+'次';
						}else{
							str += '第 '+succession+' 期未进行过还款';
						}
						str += '&nbsp;&nbsp;';
						if(data['yhdate'] != -1){
							var dateobj = data['yhdate'];
							var date = new Date(dateobj['time']);
							str += "应还日期为："+date.format("yyyy-MM-dd");
						}else{
							str += '未找到该期应还日期';
						}
						$("#succession_info").html(str);
						
						$.each(data['rows'],function(index,row){
							var id = row.ID;
							var xybj = row.XYBJ;
							var xylx = row.XYLX;
							var tr = $('[name="investrecord_id"][value="'+id+'"]').parents("tr");
							tr.find("input[name='benjin']").val(xybj);
							tr.find("input[name='lixi']").val(xylx);
						});
						
						$("#data_table tbody .statistics").trigger("change");
					},"json");
				});
				
			});
		</script>
	</head>
	<body>
		<div id="toolbar"><a href="javascript:;" id="close">关闭</a></div>
		<div id="co">
		<ul>
			<li>项目编号:${financingbase["code"]}</li>
			<li>还款总次数:<input readonly="readonly" value="${financingbase["times"]}" id="returntimes"/></li>
			<li>滞纳金比例:<input readonly="readonly" value="${fn:replace(financingbase["zhinajinbili"],'%','')}" id="zhinajinbili"/>%</li>
			<li><label for="succession_input" style="display:inline">期数</label><input type="text" class="succession" id="succession_input"/>[<a href="javascript:;" id="help_succession">?</a>]</li>
			<li><span id="succession_info"></span></li>
		</ul>
		<table class="table table-bordered table-striped" id="data_table">
			<thead>
				<tr>
					<th><input type="checkbox" id="checkall" checked="checked"/></th>
					<th>申购方</th>
					<th>申购额</th>
					<th class="controls_b">本金</th>
					<th class="controls_b">利息</th>
					<th>本息合计</th>
					<th class="controls_b">罚金</th>
					<th>公共备注<a href="javascript:;" class="same_value_text">[同值]</a></th>
					<c:if test="${financingbase._rzfwf}"><th class="controls_b">服务费</th></c:if>
					<c:if test="${financingbase._rzfwf}"><th class="controls_b">罚金</th></c:if>
					<c:if test="${financingbase._dbf}"><th class="controls_b">担保费</th></c:if>
					<c:if test="${financingbase._dbf}"><th class="controls_b">罚金</th></c:if>
					<c:if test="${financingbase._fxglf}"><th class="controls_b">风险管理费</th></c:if>
					<c:if test="${financingbase._fxglf}"><th class="controls_b">罚金</th></c:if>
					<th>私有备注<a href="javascript:;" class="same_value_text">[同值]</a></th>
					
					
					
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${paymentrecordlist}" var="p">
					<tr>
						<td><input type="checkbox" class="tr_checkbox" checked="checked"/></td>
						<td>
							<c:choose>
								<c:when test="${p['transferflag']=='2'}">
									<a href="javascript:;" class="transfer_history">${p['holderrealname'] }</a>
								</c:when>
								<c:otherwise>
									${p['holderrealname'] }
								</c:otherwise>
							</c:choose>
							<input type="hidden" name="investrecord_id" class="submit" value="${p['investrecord_id']}"/>
						</td>
						<td><fmt:formatNumber  value="${p['investamount']}" pattern="#,##0.00"/></td>
						<td><input type="text" name="benjin" class="benjin i1 statistics submit" value="${p['yhbj']}"/></td>
						<td><input type="text" name="lixi" class="lixi i3 statistics submit" value="${p['yhlx']}"/></td>
						<td><input type="text" name="benxiheji" class="i1 statistics benxiheji" readonly="readonly"/></td>
						<td><input type="text" name="fajin" class="i3 statistics submit"/></td>
						<td><input type="text" name="remark" class="i2 remark submit"/></td>
						<c:if test="${financingbase._rzfwf}"><td><input type="text" name="rongzifuwufei" class="i3 statistics submit" value="${p['fwf']}"/></td></c:if>
						<c:if test="${financingbase._rzfwf}"><td><input type="text" name="rongzifuwufei_fajin" class="i3 statistics submit"/></td></c:if>
						<c:if test="${financingbase._dbf}"><td><input type="text" name="danbaofei" class="i3 statistics submit" value="${p['dbf']}"/></td></c:if>
						<c:if test="${financingbase._dbf}"><td><input type="text" name="danbaofei_fajin" class="i3 statistics submit"/></td></c:if>
						<c:if test="${financingbase._fxglf}"><td><input type="text" name="fengxianguanlifei" class="i3 statistics submit" value="${p['fxglf']}"/></td></c:if>
						<c:if test="${financingbase._fxglf}"><td><input type="text" name="fengxianguanlifei_fajin" class="i3 statistics submit"/></td></c:if>
						
						<td><input type="text" name="remark2" class="i2 remark submit"/></td>
						
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<th><a href="javascript:;" class="select_button" id="invert">反选</a></th>
					<th colspan="2">合计</th>
					<th><input type="text" class="i2 statistics notsum" readonly="readonly"/></th>
					<th><input type="text" class="i3 statistics notsum" readonly="readonly"/></th>
					<th><input type="text" class="i2 statistics" readonly="readonly"/></th>
					<th><input type="text" class="i3 statistics" readonly="readonly"/></th>
					
					<th>${_rzfwf}</th>
					
					<c:if test="${financingbase._rzfwf}"><th><input type="text" class="i3 statistics" readonly="readonly"/></th></c:if>
					<c:if test="${financingbase._rzfwf}"><th><input type="text" class="i3 statistics" readonly="readonly"/></th></c:if>
					<c:if test="${financingbase._dbf}"><th><input type="text" class="i3 statistics" readonly="readonly"/></th></c:if>
					<c:if test="${financingbase._dbf}"><th><input type="text" class="i3 statistics" readonly="readonly"/></th></c:if>
					<c:if test="${financingbase._fxglf}"><th><input type="text" class="i3 statistics" readonly="readonly"/></th></c:if>
					<c:if test="${financingbase._fxglf}"><th><input type="text" class="i3 statistics" readonly="readonly"/></th></c:if>
					
					<th><input type="text" class="i2 sum_all" readonly="readonly"/></th>
					
				</tr>
			</tfoot>
		</table>
		<div style="margin-top:5px;">
			<div style="float:left">
				<label for="userAccount_balance" style="display:inline">融资方帐户余额:</label>
				<input type="text" readonly="readonly" value="<fmt:formatNumber value="${financingbase['balance_']}" pattern="0.00"/>" id="userAccount_balance"/>
			</div>
			<div style="float:right">
				
				<label for="tqts" style="display:inline">提前天数</label><input type="text" value="0" id="tqts" class="days" onpaste="return false" style="ime-mode:disabled" onkeypress="return event.keyCode>=48&&event.keyCode<=57||event.keyCode==46" />
				<span style="padding:0 5px 0 5px;"></span>
				<label for="yqts" style="display:inline">逾期天数</label><input type="text" class="days" id="yqts" value="0" onkeypress="return event.keyCode>=48&&event.keyCode<=57||event.keyCode==46"/>
				<span style="padding:0 5px 0 5px;"></span>
				<input type="checkbox" id="isDBDC"/><label for="isDBDC" style="display:inline">担保代偿?</label><span style="padding:0 5px 0 5px;"></span>
				
				<span class="btn btn-primary" id="submit">提交</span>
			</div>
		</div>
		</div>
	</body>
</html>


