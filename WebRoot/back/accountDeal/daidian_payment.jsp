<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/open.js"></script>
<link href="/Static/js/lhgdialog/_doc/common.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript">
$(function(){
	$("#account_type").change(function(){
		zyzh_query($(this).val());
	});
	$("#value").focus();
	zyzh_query($("#account_type").val());
	$("#query").click(function(){
		$("#chargeAmount").val("");
		var value = $("#value").val();
		var type = $("#search_type").val();
		if(value == ""){
			$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:'请输入会员名称或银行卡号或会员交易账户',cancelVal:'关闭',cancel:true,close:function(){$("#value").focus();}});
			return false;
		}else{
			var account = $("#account_type").val();
			if(account=="请选择专用账户"){
				$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:'请选择专用账户',cancelVal:'关闭',cancel:true,close:function(){$("#value").focus();}});
				return false;
			}else{
				if(value==$('#zyzh_query').attr('username')||value==$('#zyzh_query').attr('realname')){
					$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:'两个账户一致，不能代垫还款',cancelVal:'关闭',cancel:true,close:function(){$("#value").focus();}});
					return false;
				}else{
					$.getJSON("/back/accountDeal/accountDealAction!query?time=" + new Date().getTime()+"&value="+value+"&type="+type, function(data){
						$("#chager_list tr").remove();
						if(data.flag==false){
							alert("缴费失败，请重新查询。");
						}else{
							$.each(data,function(key,record){
								$("<tr><td>"+record.username+"</td><td>"+record.name+"</td><td>"+record.account+"</td><td>"+record.balance+"</td><td><input type='text' size='20' id='chargeAmount"+record.username+"'/>&nbsp;元&nbsp;备注：<input type='text' id='memo"+record.username+"' /><button memo='memo"+record.username+"' amount='chargeAmount"+record.username+"' target='"+record.username+"' class='ui-state-default mybutton charge' style='cursor:pointer'>&nbsp;代垫&nbsp;</button></td></tr>").appendTo($("#chager_list"));
							});
							$("#chargeAmount").focus();
							init();
						}
		        	});
				}
			}
		}
	});
});

function init(){
	$(".charge").click(function(){
		var id = $(this).attr("target");
		var amount_input = $("#"+$(this).attr("amount"));
		var amount = amount_input.val();
		var memo_input = $("#"+$(this).attr("memo"));
		var memo = memo_input.val();
		var zyzh = $("#from_zyzh").val();
		if(id==""){
			 $.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:'数据错误，请刷新重试。',cancelVal:'关闭',cancel:true});
		}else if(amount==""){
	        $.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:'请输入代垫金额，再进行操作。',cancelVal:'关闭',cancel:true,close:function(){amount_input.focus();}});
			return false;
		}else{
			$.dialog({title:'充值确认',lock:true,min:false,max:false,width:'400px',content:'您确定要从专用账户中代垫金额给此会员吗？',
				ok:function(){
					$.getJSON("/back/accountDeal/accountDealAction!daidian_payment?time=" + new Date().getTime()+"&id="+id+"&chargeAmount="+amount+"&memo="+encodeURI(memo)+"&value="+zyzh , function(data){
						if(data.flag==false){
							if(data.tip=="")
					        alert("操作失败，请稍后再试！");
					        else
					        alert(data.tip);
							return false;
						}else{
					        alert("从专用账户为会员交易账号:"+data.id+"代垫"+data.chargeAmount+"元成功");
							amount_input.val("");
						}
			        });
				},
				cancelVal:'取消',cancel:true,
				close:function(){amount_input.focus();}
			});
		}
	});
}

function zyzh_query(username){
	$.getJSON("/back/accountDeal/accountDealAction!zyzh_query?time=" + new Date().getTime()+"&value="+username, function(data){
		$("#zyzh_query").html("");
		if(data.flag==false){
			$("#zyzh_query").html(data.tip);
		}else{
			$("#zyzh_query").attr("username",data.username);
			$("#zyzh_query").attr("realname",data.name);
			$("#zyzh_query").html("专用账户："+data.username+"，名称："+data.name+"，账户余额："+data.balance+" 元<input id='from_zyzh' type='hidden' value='"+data.username+"' />");
		}
	});
}
</script>
<body>
	<table style="padding:0 0;width:100%;">
		<tbody>
			<tr>
				<td style="width:25%;text-align:right;">
					<select name="account_type" id="account_type">
						<option value="">请选择专用账户</option>
						<option selected value="53010100269">兴易贷-孔祥超</option>
					</select>
				</td>
				<td colspan="2" style="width:75%;" id="zyzh_query">
					
				</td>
			</tr>
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
					<button id="query" class="ui-state-default mybutton" style="width:40pt;">查询</button>
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
						会员名称
					</th>
					<th>
					            银行卡号
					</th>
					<th>
						当前余额
					</th>
					<th>
						代垫金额
					</th>
				</tr>
			</thead>
			<tbody id="chager_list">
				
			</tbody>
		</table>
	</div>
</body>