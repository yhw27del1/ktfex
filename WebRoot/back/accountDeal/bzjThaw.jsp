<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/open.js"></script>
<link href="/Static/js/lhgdialog/_doc/common.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript">
$(function(){
	$("#value").focus();
	
	$("#query").click(function(){
		$("#chargeAmount").val("");
		var value = $("#value").val();
		var type = $("#search_type").val();
		if(value == ""){
			$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:'请输入会员名称或银行卡号或会员交易账户',cancelVal:'关闭',cancel:true,close:function(){$("#value").focus();}});
			return false;
		}else{
			$.getJSON("/back/accountDeal/accountDealAction!query_bzj?time=" + new Date().getTime()+"&value="+value+"&type="+type, function(data){
				$("#chager_list tr").remove();
				if(data.flag==false){
					alert("查询失败，请重新查询。");
				}else{
					$.each(data,function(key,record){
					//alert(key)
					$("<tr><td>"+record.username+"</td><td>"+record.name+"</td><td>"+record.account+"</td><td>"+record.balance+"</td><td>"+record.frozenAmount+"</td><td><input type='text' size='20' onkeyup='this.value=this.value.replace(/[^\d]/g,'');' id='chargeAmount"+record.username+"'/>&nbsp;元&nbsp;<button amount='chargeAmount"+record.username+"' target='"+record.username+"' class='ui-state-default mybutton charge' style='cursor:pointer'>&nbsp;解冻&nbsp;</button></td></tr>").appendTo($("#chager_list"));
				});
				$("#chargeAmount").focus();
				init();
				}
	        });
		}
	});
	
});

function init(){
	$(".charge").click(function(){
		var id = $(this).attr("target");
		var amount_input = $("#"+$(this).attr("amount"));
		var amount = amount_input.val();
		if(id==""){
			 $.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:'数据错误，请刷新重试。',cancelVal:'关闭',cancel:true});
		}else if(amount==""){
	        $.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:'请输入解冻金额，再进行解冻操作。',cancelVal:'关闭',cancel:true,close:function(){amount_input.focus();}});
			return false;
		}else{
			$.dialog({title:'解冻确认',lock:true,min:false,max:false,width:'400px',content:'您确定要为此会员解冻保证金金额吗？',
				ok:function(){
					$.getJSON("/back/accountDeal/accountDealAction!bzjThaw?time=" + new Date().getTime()+"&id="+id+"&chargeAmount="+amount , function(data){
						if(data.flag==false){
							if(data.tip=="")
					        alert("解冻失败，请稍后再试！");
					        else
					        alert(data.tip);
							return false;
						}else{
					        alert("您为会员交易账号:"+data.id+"解冻"+data.chargeAmount+"元成功");
					        $("#query").trigger("click");
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
</script>
<body>
	<table style="padding:0 0;">
		<tbody>
			<tr>
				<td style="width:25%;text-align:right;">
				融资会员保证金解冻
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
						当前冻结金额
					</th>
					<th>
						保证金解冻入口
					</th>
				</tr>
			</thead>
			<tbody id="chager_list">
				
			</tbody>
		</table>
	</div>
</body>