<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/open.js"></script>
<link href="/Static/js/lhgdialog/_doc/common.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript">
$(function(){
	$("#reset").click(function(){
		$("#chargeAmount").val("");
		$("#memo").val("");
		$("#sure").attr("disabled",false);
		return false;
	});
	$("#sure").click(function(){
		var money = $("#chargeAmount").val();
		if(money){
			$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:"你确定要执行转账操作吗？",
				ok:function(){
					var in_ = $("#in_member_username").val();
					var out_ = $("#out_member_username").val();
					if(in_==out_){
						alert("两个账户一致，不能内转");
					}else{
						$("#sure").attr("disabled",true);
						var memo = encodeURI($("#memo").val());
						var url = "/back/accountDeal/accountDealAction!internalTransfer_do?time="+new Date().getTime()+"&chargeAmount="+money+"&memo="+memo+"&out_member_username="+out_+"&in_member_username="+in_;
						$.getJSON(url,function(data){
							if(data.message=="success"){
								alert("转账成功。");
								window.location.href=window.location.href;
							}else{
								alert(data.message);
							}
						});
					}
				},
				cancelVal:'关闭',cancel:true
			});
		}else{
			alert("请输入转账金额");
		}
		return false;
	});
});
</script>
<body>
<form action="">
	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
		转出会员交易账号&nbsp;<input size="20" type="text" name="out_member_username" value="${out_member_username}" />&nbsp;
		转入会员交易账号&nbsp;<input size="20" type="text" name="in_member_username" value="${in_member_username}" />&nbsp;
		<input type="submit" class="ui-state-default" value="查询" />
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						转账说明
					</th>
					<th>
						交易账户
					</th>
					<th>
						用户名
					</th>
					<th>
					            银行账号
					</th>
					<th>
					            可用余额
					</th>
					<th>
						冻结金额
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${in_out}" var="entry">
					<tr>
						<td>
							${entry.tip}					
						</td>
						<td>
							${entry.username}
						</td>
						<td>
						    ${entry.name}
						</td>
						<td>
						    ${entry.bank}
						</td>
						<td>
						    <fmt:formatNumber value='${entry.balance}' type="currency" currencySymbol=""/>
						</td>
						<td>
						    <fmt:formatNumber value='${entry.frozenAmount}' type="currency" currencySymbol=""/>
						</td>
					</tr>
				</c:forEach>
				<c:if test="${in_out== null || fn:length(in_out) == 0}">
					<tr>
						<td colspan="6">请输入正确的转出会员交易账号及转入会员交易账号</td>
					</tr>
				</c:if>
				<c:if test="${fn:length(in_out) > 0}">
					<tr>
						<td colspan="6">
							转账金额&nbsp;<input size="10" type="text" onkeyup="this.value=this.value.replace(/[^\d.]/g,'');" name="chargeAmount" id="chargeAmount" />
							备注&nbsp;<input size="20" type="text" name="memo" id="memo" />
							<input type="hidden" id="out_member_username" value="${out_member_username}" />
							<input type="hidden" id="in_member_username" value="${in_member_username}" />
							<input type="button" class="ui-state-default" id="sure" value="确认" />
							<input type="button" class="ui-state-default" id="reset" value="重置" />
						</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
</form>
</body>