<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<style>
	.tip_h1_pass{
		display: inline;
		color:#409C28;
	}
	.tip_h1_reject{
		display: inline;
		color:#B22222;
	}
</style>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js?skin=discuz"></script>
<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/easyui-lang-zh_CN.js"/></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<script type="text/javascript">
$(function(){
	$(".table_solid").tableStyleUI();
	$(".makesure").click(function(){
		var _this = $(this);
		var aid = $(this).attr("aid");
		$.dialog({title:'正在进行确认操作',lock:true,min:false,max:false,width:'400px',content:"当前执行的操作<<h1 class='tip_h1_pass'>通过</h1>>",
			ok:function(){
				$.dialog.tips("拼命加载中...",600,"loading.gif");
				$.post("/back/accountDealAction!cash_risk_makesure",{"id":aid},function(data,status){
					var code = data['code'];
					var str = data['str'];
					if(code==1){
						$.dialog.tips("<h1>操作成功!</h1>",5,"success.gif");
						_this.parents("tr").remove();
					}else{
						$.dialog.tips("<h1>操作失败!</h1><span>"+str+"</span>",5,"error.gif");
					}
				},'json');
			},
			cancelVal:'关闭',
			cancel:true
		});
	});
	
	$(".reject").click(function(){
		var _this = $(this);
		var aid = $(this).attr("aid");
		
		$.dialog({title:'正在进行驳回操作',lock:true,min:false,max:false,width:'400px',content:"当前执行的操作<<h1 class='tip_h1_reject'>驳回</h1>>",
			ok:function(){
				$.dialog.prompt("请输入提现驳回原因",function(val){
					$.dialog.tips("拼命加载中...",600,"loading.gif");
					$.post("/back/accountDealAction!cash_risk_reject",{"id":aid,"remark":val},function(data,status){
						var code = data['code'];
						var str = data['str'];
						if(code==1){
							$.dialog.tips("<h1>操作成功!</h1>",5,"success.gif");
							_this.parents("tr").remove();
						}else{
							$.dialog.tips("<h1>操作失败!</h1><span>"+str+"</span>",5,"error.gif");
						}
					},'json');
				});
			},
			cancelVal:'关闭',
			cancel:true
		});
	});
});
	
	
</script>
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="">
	<input type="hidden" name="page" value="1" />

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
						类型
					</th>
					<th>
						融资项目
					</th>
					<th>
						担保机构
					</th>
					<th>
						提现金额
					</th>
					<th>
						帐户余额
					</th>
					<th>
						申请日期
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
							${accountDeal.type}
						</td>
						<td>
						    ${accountDeal.financing.code}
						</td>
						<td>
						    ${accountDeal.financing.createBy.org.showCoding}/${accountDeal.financing.createBy.org.name}
						</td>
						<td>
							<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/>
						</td>
						<td>
							<fmt:formatNumber value='${accountDeal.account.balance}' pattern="#,###,##0.00"/>
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.createDate}" type="both" />
						</td>
						
						<td>
							<a href="javascript:;" class="makesure" aid="${accountDeal.id}">确认放款</a>
							<a href="javascript:;" class="reject" aid="${accountDeal.id}">申请驳回</a>
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