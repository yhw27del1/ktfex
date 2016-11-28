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
	function toURL(url,flag) {
		$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:flag,
			ok:function(){
				if(flag=="你确定要将此笔提现申请审核驳回吗？"){
					$.dialog.prompt("请输入提现驳回原因",function(val){
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
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				关键字&nbsp;<input type="text" name="keyWord" value="${keyWord}" />
				<input type="submit" class="ui-state-default" value="查询" />
	</div>

	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						交易账户
					</th>
					<th>
						用户名
					</th>
					<th>
						会员类型
					</th>
					<th>
					    资金账号
					</th>
					<th>
						类型
					</th>
					<th>
						提现金额
					</th>
					<th>
						申请日期
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
						<!-- <td>
							${accountDeal.account.accountId}
						</td> -->
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
							<fmt:formatDate value="${accountDeal.createDate}" type="both" />
						</td>
						<td>
						      <c:if test="${accountDeal.checkFlag=='2.5'}">待审核</c:if>
						     <span style='color:red;'> <c:if test="${accountDeal.checkFlag=='2.4'}">驳回</c:if></span>
							${accountDeal.memo}
						</td>
						<td>
						 <c:if test="${accountDeal.checkFlag=='2.5'}">  
							<button  onclick="toURL('/back/accountDeal/accountDealAction!bzjCashCheckToPass?id=${accountDeal.id}','你确定要将此笔提现申请审核通过吗？');return false;" class="ui-state-default" >审核通过</button>
							<button  onclick="toURL('/back/accountDeal/accountDealAction!bzjCashCheckToNoPass?id=${accountDeal.id}','你确定要将此笔提现申请审核驳回吗？');return false;" class="ui-state-default" >审核驳回</button>
                         </c:if>
                        </td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="9">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
				</tr>
			</tbody>
		</table>
	</div>
	</form>
</body>
<script>
setIframeHeight(100);
</script>