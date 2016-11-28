<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/back/four.jsp"></script>
<script type="text/javascript">
$(function(){
	$(".table_solid").tableStyleUI();
	$(".print_a").click(function(){
		var id = $(this).attr("id");
		var va = window.showModalDialog("/back/accountDeal/accountDealAction!print_voucher?ids="+id,"handlerrequestforcash_print","dialogWidth=900px;dialogHeight=600px;");
		return false;
	});
	var b = '${bank}';
    $("option[value='"+b+"']",$("#bank")).attr("selected",true);
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
				if(flag=="你确定要将此笔出金审核驳回吗？"){
					$.dialog.prompt("请输入出金驳回原因",function(val){
						var d = "&time="+new Date().getTime()+"&memo="+val;
						$.ajax({
					   		type:"post",
					   		url:url,
					   		data:d,
					   		dataType:"json",
					   		success:function(data){
					   			alert(data.msg);
					   			location.reload();
					   		},
					   		error:function(data){
					   			alert(data.msg);
					   			location.reload();
					   		}
					   	});
					});
				}else{
					$("body").showLoading();
					var d = "&time="+new Date().getTime();
					$.ajax({
				   		type:"post",
				   		url:url,
				   		data:d,
				   		dataType:"json",
				   		success:function(data){
				   			alert(data.msg);
				   			$("body").hideLoading();
				   			location.reload();
				   		},
				   		error:function(data){
				   			alert(data.msg);
				   			location.reload();
				   		}
				   	});
				}
			},
			cancelVal:'关闭',cancel:true
		});
	}
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="/Static/js/showloading/jquery.showLoading.min.js"></script>
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="">
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				签约行&nbsp;
				<select name="bank" id="bank">
					<option value="-1">全部</option>
					<option value="1">华夏</option>
					<option value="2">招商</option>
				</select>
				会员类型:
				<s:select list="#{'T':'投资人','R':'融资方'}" listKey="key" listValue="value"  headerKey="" headerValue="全部" value="#request.userType" name="userType"></s:select>
				关键字&nbsp;<input type="text" name="keyWord" value="${keyWord}" />
				<input type="submit" class="ui-state-default" value="查询" />
	</div>

	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						会员名
					</th>
					<th>
						交易账号
					</th>
					<th>
						子账号
					</th>
					<th>
						签约行
					</th>
					<th>
						签约类型
					</th>
					<th>
						交易类型
					</th>
					<th style="text-align: right;">
						出金额
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
						<td>
						    <script>document.write(name("${accountDeal.account.user.realname}"));</script>
						</td>
						<td>
							${accountDeal.account.user.username}
						</td>
						<td>
						   ${accountDeal.account.user.accountNo}
						</td>
						<td>
						   <c:if test="${accountDeal.account.user.signBank==1}">华夏</c:if>
						   <c:if test="${accountDeal.account.user.signBank==2}">招商</c:if>
						</td>
						<td>
						   <c:if test="${accountDeal.account.user.signType==1}">本行</c:if>
						   <c:if test="${accountDeal.account.user.signType==2}">他行</c:if>
						</td>
						<td>
							${accountDeal.type}
						</td>
						<td style="text-align: right;">
							<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/>
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.createDate}" type="both" />
						</td>
						<td>
							<button style="display:<c:out value="${menuMap['hx_out_pass']}" />" onclick="toURL('/back/accountDeal/accountDealAction!outGoldenToPass?id=${accountDeal.id}','你确定要将此笔出金审核通过吗？');return false;" class="ui-state-default" >审核通过</button>
							<button style="display:<c:out value="${menuMap['hx_out_nopass']}" />" onclick="toURL('/back/accountDeal/accountDealAction!outGoldenToNoPass?id=${accountDeal.id}','你确定要将此笔出金审核驳回吗？');return false;" class="ui-state-default" >审核驳回</button>
							<c:if test="${menuMap['print_withdraw_voucher']=='inline'}"><button id="${accountDeal.id}" class="ui-state-default print_a" >打印凭证</button></c:if>
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
