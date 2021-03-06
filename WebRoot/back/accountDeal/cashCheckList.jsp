<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript">
$(function(){
	$(".table_solid").tableStyleUI();
	$(".print_a").click(function(){
		var id = $(this).attr("id");
		var va = window.showModalDialog("/back/accountDeal/accountDealAction!print_voucher?ids="+id,"handlerrequestforcash_print","dialogWidth=900px;dialogHeight=600px;");
		return false;
	});
	
	$("#print_all").click(function(){
		var str = "";
		var x = 0;
		$(".print_a").each(function(){
			if(x!=0) str += "&";
			str += "ids="+$(this).attr("id");
			x++;
		})
		var va = window.showModalDialog("/back/accountDeal/accountDealAction!print_voucher?"+str,"handlerrequestforcash_print","dialogWidth=900px;dialogHeight=600px;");
	});
});
	function checkPass(url) {
		$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:"你确定要将此笔提现申请审核通过吗？",
			ok:function(){
				window.location.href = url+"&action=1";
			},
			cancelVal:'关闭',cancel:true
		});
	}
	
	function checkNoPass(url) {
		$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:"你确定要将此笔提现申请审核驳回吗？",
			ok:function(){
				$.dialog.prompt("请输入提现驳回原因",function(val){
					window.location.href = url+"&memo="+val+"&action=1";
				});
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
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				<input type="hidden" name="action" value="0" />
				会员类型:
				<s:select list="#{'T':'投资人','R':'融资方'}" listKey="key" listValue="value"  headerKey="" headerValue="全部" value="#request.userType" name="userType"></s:select>
				关键字&nbsp;<input type="text" name="keyWord" value="${keyWord}" />
				<input type="submit" class="ui-state-default" value="查询" />
				招商银行专户
	</div>

	<div class="dataList ui-widget">
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
						会员类型
					</th>
					<th>
					    银行账号
					</th>
					<th>
						交易类型
					</th>
					<th>
						提现金额
					</th>
					<th>
						专户
					</th>
					<th>
						结余
					</th>
					<th>
                           融资项目
                    </th>
					<th>
						申请日期
					</th>
					<th>
						风控确认
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
				<c:forEach items="${pageView.records}" var="accountDeal">
					<tr>
						<td>
							${accountDeal.account.user.username}
						</td>
						<td>
						    <script>document.write(name("${accountDeal.account.user.realname}"));</script>
						</td>
						<td>
						    <c:if test="${accountDeal.account.user.userType=='T'}">投资人</c:if>
						    <c:if test="${accountDeal.account.user.userType=='R'}">融资方</c:if>
						    <c:if test="${accountDeal.account.user.userType=='D'}">担保方</c:if>
						</td>
						<td>
						    <script>document.write(bankcard("${accountDeal.bankAccount}"));</script>
						</td>
						<td>
							${accountDeal.type}
						</td>
						<td>
							<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/>
						</td>
						<td>
							<c:if test="${accountDeal.channel==0}">无</c:if>
							<c:if test="${accountDeal.channel==1}">招商银行</c:if>
							<c:if test="${accountDeal.channel==2}">工商银行</c:if>
						</td>
						<td>
							<fmt:formatNumber value='${accountDeal.nextMoney}' pattern="#,###,##0.00"/>
						</td>
						<td>
                            ${accountDeal.financing.code}
                        </td>
						<td>
							<fmt:formatDate value="${accountDeal.createDate}" type="both" />
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.fkqrdate}" type="both" />
						</td>
						<td>
							${accountDeal.memo}
						</td>
						<td>
							<button style="display:<c:out value="${menuMap['charge_check_pass']}" />" onclick="checkPass('/back/accountDeal/accountDealAction!cashCheckToPass?id=${accountDeal.id}');return false;" class="ui-state-default" >审核通过</button>
							<button style="display:<c:out value="${menuMap['charge_check_nopass']}" />" onclick="checkNoPass('/back/accountDeal/accountDealAction!cashCheckToNoPass?id=${accountDeal.id}');return false;" class="ui-state-default" >审核驳回</button>
							<c:if test="${menuMap['print_withdraw_voucher']=='inline'}"><button id="${accountDeal.id}" class="ui-state-default print_a" >打印凭证</button></c:if>
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="13">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
				</tr>
			</tbody>
		</table>
	</div>
	<%@ include file="/common/messageTip.jsp" %>
	</form>
</body>
<script>
setIframeHeight(100);
</script>