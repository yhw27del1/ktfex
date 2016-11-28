<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript">
$(function(){
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
    $(".print_a").click(function(){
		var id = $(this).attr("id");
		var va = window.showModalDialog("/back/accountDeal/accountDealAction!print_voucher_dn?ids="+id,"handlerrequestforcash_print","dialogWidth=900px;dialogHeight=600px;");
		return false;
	});
});
</script>
<script type="text/javascript">
	function toURL(url,flag) {
		$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:flag,
			ok:function(){
				if(flag=="你确定要将此笔转账审核驳回吗？"){
					$.dialog.prompt("请输入转账驳回原因",function(val){
						window.location.href = url+"&memo="+val;
					});
				}else{
					window.location.href = url;
				}
			},
			cancelVal:'关闭',cancel:true
		});
	}
	function del(id) {
		window.location.href = "/back/member/memberTypeAction!del?id=" + id;
	}
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="">
	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
			日期&nbsp;从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;到&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
			<input type="submit" class="ui-state-default" value="查询" />
	</div>

	<div class="dataList ui-widget">
			<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						转出账户
					</th>
					<th>
						转入账户
					</th>
					<th>
						类型
					</th>
					<th>
						转出金额
					</th>
					<th>
						操作者
					</th>
					<th>
						日期
					</th>
					<th>
						备注
					</th>
					<th>
						操作
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${deals}" var="accountDeal">
					<tr>
						<td>
							<script>document.write(name("${accountDeal.account.user.realname}"));</script>/${accountDeal.account.user.username}/<script>document.write(bankcard("${accountDeal.bankAccount}"));</script>
						</td>
						<td>
						    <script>document.write(name("${accountDeal.accountDeal.account.user.realname}"));</script>/${accountDeal.accountDeal.account.user.username}/<script>document.write(bankcard("${accountDeal.accountDeal.bankAccount}"));</script>
						</td>
						<td>
							${accountDeal.type}
						</td>
						<td>
						 	<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/>
						</td>
						<td>
							${accountDeal.user.realname}
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.createDate}" type="date" />
						</td>
						<td>
							${accountDeal.memo} 
						</td>
						<td>
							<button style="display:<c:out value="${menuMap['charge_check_pass']}" />" onclick="toURL('/back/accountDeal/accountDealAction!nbzzCheckToPass?id=${accountDeal.id}','你确定要将此笔转账审核通过吗？');return false;" class="ui-state-default" >审核通过</button>
							<button style="display:<c:out value="${menuMap['charge_check_nopass']}" />" onclick="toURL('/back/accountDeal/accountDealAction!nbzzCheckToNoPass?id=${accountDeal.id}','你确定要将此笔转账审核驳回吗？');return false;" class="ui-state-default" >审核驳回</button>
							<c:if test="${menuMap['print_recharge_voucher']=='inline'}"><button id="${accountDeal.id}" class="ui-state-default print_a" >打印凭证</button></c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	</form>
</body>
