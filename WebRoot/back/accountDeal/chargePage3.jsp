<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/open.js"></script>
<link href="/Static/js/lhgdialog/_doc/common.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript">
$(function(){
	$("#value").focus();
	
	$("#qqqqq").keydown(function(event){
		if(event.keyCode == 13){
			$("#chager_list tr").remove();
			$("#query").trigger("click");
		}
	});
	
	$("#query").click(function(){
		$("#chargeAmount").val("");
		var value = $("#value").val();
		var type = $("#search_type").val();
		if(value == ""){
			$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:'请输入会员名称或银行卡号或会员交易账户',cancelVal:'关闭',cancel:true,close:function(){$("#value").focus();}});
			return false;
		}else{
			$.getJSON("/back/accountDeal/accountDealAction!query?time=" + new Date().getTime()+"&value="+value+"&type="+type+"&channel=2", function(data){
				$("#chager_list tr").remove();
				if(data.flag==false){
					alert("缴费失败，请重新查询。");
				}else{
					$.each(data,function(key,record){
						if('投资人'==record.tip){
							$("<tr><td>"+record.username+"</td><td>"+record.tip+"</td><td>"+record.name+"</td><td>"+record.account+"</td><td>"+record.sign+"</td><td>"+record.balance+"</td><td>金额:<input type='text' cannext class='money' id='chargeAmount"+record.username+"'/>&nbsp;元<br />回单:<input type='text' cannext id='huidan"+record.username+"' /><br />备注:<input type='text' cannext id='memo"+record.username+"' /><br />渠道:<select id='qudao"+record.username+"'><option value='2'>工商银行</option></select>&nbsp;<button type='1' huidan='huidan"+record.username+"' memo='memo"+record.username+"' amount='chargeAmount"+record.username+"' target='"+record.username+"' class='ui-state-default mybutton charge' style='cursor:pointer'>投资人充值</button></td></tr>").appendTo($("#chager_list"));
						}else{
							$("<tr><td>"+record.username+"</td><td>"+record.tip+"</td><td>"+record.name+"</td><td>"+record.account+"</td><td>"+record.sign+"</td><td>"+record.balance+"</td><td>金额:<input type='text' cannext class='money' id='chargeAmount"+record.username+"'/>&nbsp;元<br />回单:<input type='text' cannext id='huidan"+record.username+"' /><br />备注:<input type='text' cannext id='memo"+record.username+"' /><br />渠道:<select id='qudao"+record.username+"'><option value='2'>工商银行</option></select><br />类型:<select id='type"+record.username+"'><option value='21'>还款充值</option><option value='22'>履约充值</option></select>&nbsp;<button type='type"+record.username+"' huidan='huidan"+record.username+"' memo='memo"+record.username+"' amount='chargeAmount"+record.username+"' target='"+record.username+"' class='ui-state-default mybutton charge' style='cursor:pointer'>融资方充值</button></td></tr>").appendTo($("#chager_list"));
						}
					});
					$(".money:first").focus();
					init();
				}
	        });
		}
	});
	
});

function init(){
	//输入金额进行格式化，会计习惯
	$(".money").keyup(function(event){
		var old = $(this).val();
		var value=old.replace(/\,|\s/g,'');
		value = value.replace(/[^(\d|\.)]/g,'');
		value = value.replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g, '$1,');
		$(this).val(value);
	});

	//回车进行表单切换
	var $inp = $("input[cannext]");
	var size = 0;
	if($inp){
		size = $inp.size();
	}
	if(size>0){
		$inp.bind('keydown', function (e) {
			var key = e.which;
			if (key == 13) {
				e.preventDefault();
				var pre = $inp.index(this);
				var nxt = 0;
				if(pre==(size-1)){
					nxt = pre;
				}else{
					nxt = pre+1;
				}
				$inp[nxt].focus();
			}
		});
	}
	
	$(".charge").click(function(){
		var id = $(this).attr("target");
		var amount_input = $("#"+$(this).attr("amount"));
		var amount = amount_input.val();
		amount = amount.replace(/\,|\s/g,'');
		var memo_input = $("#"+$(this).attr("memo"));
		var memo = memo_input.val();
		var huidan_input = $("#"+$(this).attr("huidan"));
		var huidan = huidan_input.val();//回单号可输可不输，输入后台会验证，不输入后台则不验证
		var type_select = $("#"+$(this).attr("type"));
		var type = '1';
		if(type_select.val()){
			type = type_select.val();
		}
		var qudao_select = $("#qudao"+id);
		var qudao = '0';
		if(qudao_select.val()){
			qudao = qudao_select.val();
		}
		if(id==""){
			 $.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:'数据错误，请刷新重试。',cancelVal:'关闭',cancel:true});
		}else if(amount==""){
	        $.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:'请输入充值金额，再进行充值操作。',cancelVal:'关闭',cancel:true,close:function(){amount_input.focus();}});
			return false;
		}else{
			$.dialog({title:'充值确认',lock:true,min:false,max:false,width:'400px',content:'您确定要为此会员充值吗？',
				ok:function(){
					$.getJSON("/back/accountDeal/accountDealAction!charge?time=" + new Date().getTime()+"&id="+id+"&chargeAmount="+amount+"&memo="+encodeURI(memo)+"&type="+type+"&huidan="+encodeURI(huidan)+"&qudao="+qudao , function(data){
						if(data.flag==false){
							if(data.tip=="")
					        	alert("充值失败，请稍后再试！");
					        else
					        	alert(data.tip);
							return false;
						}else{
					        alert("您为会员交易账号:"+data.id+"充值"+data.chargeAmount+"元成功");
							$("#value").val("").focus();
							$("#chager_list tr").remove();
						}
			        });
				},
				cancelVal:'取消',cancel:true,
				close:function(){amount_input.focus();}
			});
		}
	});
}
</script>
<body>
	<table style="padding:0 0;" id="qqqqq">
		<tbody>
			<tr>
				<td style="width:25%;text-align:right;">
					<select name="search_type" id="search_type">
						<option value="name">会员名称</option>
						<option value="bankaccount">银行卡号</option>
						<option value="username">会员交易账号</option>
					</select>
				</td>
				<td style="width:25%;">
					<input size="20" type="text" name="value" id="value"/>&nbsp;
				</td>
				<td style="width:50%;">
					<button id="query" class="ui-state-default mybutton" style="width:40pt;">查询</button>&nbsp;<span style="color:red;">工商银行专户：2502110419024503160</span>
				</td>
			</tr>
		</tbody>
	</table>
	<div class="dataList ui-widget" style="border:0;">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						交易账户
					</th>
					<th>
						会员类型
					</th>
					<th>
						会员名称
					</th>
					<th>
						银行卡号
					</th>
					<th>
						说明
					</th>
					<th>
						当前余额
					</th>
					<th>
						充值入口
					</th>
				</tr>
			</thead>
			<tbody id="chager_list">
				
			</tbody>
		</table>
	</div>
</body>