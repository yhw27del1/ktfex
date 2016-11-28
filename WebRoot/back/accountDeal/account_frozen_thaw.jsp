<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/open.js"></script>
<link href="/Static/js/lhgdialog/_doc/common.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<script type="text/javascript">
$(function(){
    $(".table_solid").tableStyleUI();
	$("#reset").click(function(){
		$("#chargeAmount").val("");
		$("#memo").val("");
		$("#bzj").val("");
		$("#codes").val("");
		$("#sure").attr("disabled",false);
		return false;
	});
	$("#sure").click(function(){
		var money = $("#chargeAmount").val();
		var bzj = $("#bzj").val();
		var codes = $("#codes").val();
		if(money&&bzj&&codes){
			$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:"你确定要执行操作吗？",
				ok:function(){
					$("#sure").attr("disabled",true);
					var t = $("#type").val();
					if(t!="冻结"){
						alert("参数错误，请重试！");
						$("#reset").trigger("click");
					}else{
						var u = $("#userName").val();
						var memo = encodeURI($("#memo").val());
						var url = "/back/accountDealAction!account_frozen_thaw_do?time="+new Date().getTime()+"&chargeAmount="+money+"&memo="+memo+"&userName="+u+"&type="+t+"&bzj="+bzj+"&codes="+codes;
						$.getJSON(url,function(data){
							if(data.message=="success"){
								alert("申请成功。");
								window.location.href=window.location.href;
							}else{
								alert(data.message);
								$("#sure").attr("disabled",false);
							}
						});
					}
				},
				cancelVal:'关闭',cancel:true
			});
		}else{
			alert("请输入金额、保证金比例及项目编号");
		}
		return false;
	});
	
	$("input.sqjd").click(function(){
		var $ts = $(this);
		var id = $ts.attr("id");
		var money = $ts.attr("money");
		if(money&&id){
			$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:"你确定要执行操作吗？",
				ok:function(){
					$ts.attr("disabled",true);
					var t = encodeURI("解冻");
					var u = $ts.attr("username");
					var memo = "";
					$.dialog.prompt("申请解冻的原因",function(val){
						memo = encodeURI(val);
						var url = "/back/accountDealAction!account_frozen_thaw_do?time="+new Date().getTime()+"&chargeAmount="+money+"&memo="+memo+"&userName="+u+"&type="+t+"&id="+id;
						$.getJSON(url,function(data){
							if(data.message=="success"){
								alert("申请成功。");
								window.location.href=window.location.href;
							}else{
								alert(data.message);
								$ts.attr("disabled",false);
							}
						});
					});
				},
				cancelVal:'关闭',cancel:true
			});
		}else{
			alert("请输入金额");
		}
		return false;
	});
	
	var b = '${type}';
	if(!b){
		b = "冻结";
	}
    $("option[value='"+b+"']",$("#typeSelect")).attr("selected",true);
    check(b);
    $("#typeSelect").change(function(){
    	var v = $(this).val();
    	check(v);
    });
});
function check(v){
	if(v=='冻结'){
   		$("#dongjie").show();
   		$("#jiedong").hide();
   	}
   	if(v=='解冻'){
   		$("#jiedong").show();
   		$("#dongjie").hide();
   	}
}
</script>
<body>
<form action="">
	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
		类型&nbsp;<select name="type" id="typeSelect"><option value="冻结">冻结</option><option value="解冻">解冻</option></select>
		交易账号&nbsp;<input size="20" type="text" name="userName" value="${userName}" />&nbsp;
		<input type="submit" class="ui-state-default" value="查询" />
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content" id="dongjie">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						会员类型
					</th>
					<th>
						会员名称
					</th>
					<th>
						交易账户
					</th>
					<th>
					            可用余额
					</th>
					<th>
						冻结金额
					</th>
				</tr>
			</thead>
			<tbody class="table_solid" id="jjj">
				<c:if test="${user!=null}">
					<tr>
						<td>
							<c:if test="${user.userType=='T'}">投资人</c:if>
							<c:if test="${user.userType=='R'}">融资方</c:if>
							<c:if test="${user.userType=='D'}">担保方</c:if>
						</td>
						<td>
						    <script>document.write(name("${user.realname}"));</script>
						</td>
						<td>
							${user.username}
						</td>
						<td>
						    <fmt:formatNumber value='${user.userAccount.balance}' type="currency" currencySymbol=""/>
						</td>
						<td>
						    <fmt:formatNumber value='${user.userAccount.frozenAmount}' type="currency" currencySymbol=""/>
						</td>
					</tr>
				</c:if>
				<c:if test="${user==null}">
					<tr>
						<td colspan="6">请输入正确的会员交易账号</td>
					</tr>
				</c:if>
				<c:if test="${user!=null&&type=='冻结'}">
					<tr>
						<td colspan="6">
							金额&nbsp;<input size="10" type="text" onkeyup="this.value=this.value.replace(/[^\d.]/g,'');" name="chargeAmount" id="chargeAmount" />
							保证金比例&nbsp;<input size="10" type="text" onkeyup="this.value=this.value.replace(/[^\d.]/g,'');" name="bzj" id="bzj" />
							项目编号&nbsp;<input size="15" type="text" name="codes" id="codes" />
							<!-- 备注&nbsp;<input size="20" type="text" name="memo" id="memo" /> -->
							备注&nbsp;<select name="memo" id="memo"><option value="协议保证金">协议保证金</option><option value="还款保证金">还款保证金</option></select>
							<input type="hidden" id="type" value="${type}" />
							<input type="hidden" id="userName" value="${userName}" />
							<input type="button" class="ui-state-default" id="sure" value="申请冻结" />
							<input type="button" class="ui-state-default" id="reset" value="重置" />
						</td>
					</tr>
				</c:if>
			</tbody>
		</table>
		
		<table class="ui-widget ui-widget-content" id="jiedong">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						会员类型
					</th>
					<th>
						会员名
					</th>
					<th>
						交易账号
					</th>
					<th>
						交易类型
					</th>
					<th style="text-align: right;">
						金额
					</th>
					<th>
						项目编号
					</th>
					<th>
						保证金比例
					</th>
					<th>
						操作员
					</th>
					<th>
						审核员
					</th>
					<th>
						审核时间
					</th>
					<th>
						备注
					</th>
					<th></th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${list}" var="entry">
					<tr>
						<td>
							<c:if test="${entry.account.user.userType=='T'}">投资人</c:if>
							<c:if test="${entry.account.user.userType=='R'}">融资方</c:if>
							<c:if test="${entry.account.user.userType=='D'}">担保方</c:if>
						</td>
						<td>
						   ${entry.account.user.realname}
						</td>
						<td>
						  ${entry.account.user.username}
						</td>
						<td>
							${entry.type}
						</td>
						<td style="text-align: right;">
							<fmt:formatNumber currencySymbol="" value="${entry.money}" type="currency" />
						</td>
						<td>
							${entry.codes}
						</td>
						<td>
							${entry.bzj}%
						</td>
						<td>
							${entry.user.realname}
						</td>
						<td>
							${entry.checkUser.realname}
						</td>
						<td>
							<fmt:formatDate value="${entry.checkDate}" type="date" />
						</td>
						<td>
							${entry.memo}
						</td>
						<td>
							<input id="${entry.id}" username="${entry.account.user.username}" money="${entry.money}" type="button" class="sqjd ui-state-default" value="申请解冻" />
						</td>
					</tr>
				</c:forEach>
				<c:if test="${entry==null}">
					<tr>
						<td colspan="16">未查询到审核通过的资金冻结记录</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
</form>
</body>