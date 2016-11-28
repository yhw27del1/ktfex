<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript">
$(function(){
	$(".table_solid").tableStyleUI();
	
	var userTypeVal = $("#userTypeHidden").val();
    if(userTypeVal){
    	//alert(userTypeVal);
    	$("option",$("#userTypeSelect")).attr("selected",false);
    	$("option[value='"+userTypeVal+"']",$("#userTypeSelect")).attr("selected",true);
    }
});
</script>
<script type="text/javascript">
	function yhk(url) {
		$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:"你确定此笔提现已划款吗？",
			ok:function(){
				window.location.href = url+"&action=1";
			},
			cancelVal:'关闭',cancel:true
		});
	}
	function zzyc(url) {
		$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:"你确定此笔提现转账异常吗？",
			ok:function(){
				$.dialog.prompt("请输入转账异常原因",function(val){
					window.location.href = url+"&memo="+val+"&action=1";
				});
			},
			cancelVal:'关闭',cancel:true
		});
	}
	function del(id) {
		window.location.href = "/back/member/memberTypeAction!del?id=" + id;
	}
	function doprint(){
		$("#myToolBar").hide();
		$("#toPrint").hide();
		print();
		$("#myToolBar").show();
		$("#toPrint").show();
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
				会员类型&nbsp;
				<input type="hidden" id="userTypeHidden" value="${userType}" />
				<select id="userTypeSelect" name="userType">
					<option value="T">投资人</option>
					<option value="R">融资方</option>
					<option value="all">全部</option>
				</select>
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
						提现金额
					</th>
					<th>
						专户
					</th>
					<th>
						申请日期
					</th>
					<th>
						审核日期
					</th>
					<th>
						审核人姓名
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
							${accountDeal.bank}
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
							<fmt:formatDate value="${accountDeal.createDate}" type="both" />
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.checkDate}" type="both" />
						</td>
						<td>
							${accountDeal.checkUser.realname}
						</td>
						<td>
						<c:if test="${menuMap['waitre_yhk'] == 'inline' }"> 
							<button onclick="yhk('/back/accountDeal/accountDealAction!yhk?id=${accountDeal.id}');return false;" class="ui-state-default"   >已划款</button>
						</c:if>
						<c:if test="${menuMap['waitre_zzyc'] == 'inline' }"> 
							<button onclick="zzyc('/back/accountDeal/accountDealAction!zzyc?id=${accountDeal.id}');return false;" class="ui-state-default" >转账异常</button>
					     </c:if>
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="9">
						<jsp:include page="/common/page.jsp"></jsp:include>&nbsp;共:${bishu}笔&nbsp;合计:<fmt:formatNumber value="${jine}" pattern="#,###,###,##0.00"></fmt:formatNumber>元</td>
						<td><input type="button" value="打印" id="toPrint" onclick="doprint()"></td>
				</tr>
			</tbody>
		</table>
	</div>
	</form>
</body>
<script>
setIframeHeight(100);
</script>