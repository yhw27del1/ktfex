<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript">
$(function(){
	$(".table_solid").tableStyleUI();
    $("#batchCheck_pass").click(function(){
    	var k = $("#keyWord").val();
   		$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:"你确定要执行批量审核通过吗？",
			ok:function(){
				$("#batchCheck_pass").attr("disabled",true);
				window.location.href = "/back/accountDeal/accountDealAction!checkLX_pass?time="+new Date().getTime()+"&keyWord="+encodeURI(k);
			},
			cancelVal:'关闭',cancel:true
		});
    });
    
    $("#batchCheck_nopass").click(function(){
    	var k = $("#keyWord").val();
   		$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:"你确定要执行批量审核驳回吗？",
			ok:function(){
				$("#batchCheck_nopass").attr("disabled",true);
				window.location.href = "/back/accountDeal/accountDealAction!checkLX_nopass?time="+new Date().getTime()+"&keyWord="+encodeURI(k);
			},
			cancelVal:'关闭',cancel:true
		});
    });
    
    
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
</script>
<script type="text/javascript">
	function toURL(url,flag) {
		$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:flag,
			ok:function(){
				if(flag=="你确定要将此笔充值审核驳回吗？"){
					$.dialog.prompt("请输入充值驳回原因",function(val){
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
				关键字&nbsp;<input type="text" id="keyWord" name="keyWord" value="${keyWord}" />&nbsp;
				<input type="submit" class="ui-state-default" value="查询" />
				<input type="button" style="display:<c:out value="${menuMap['charge_check_pass']}" />" id="batchCheck_pass" class="ui-state-default" value="批量审核通过" />
				<input type="button" style="display:<c:out value="${menuMap['charge_check_nopass']}" />" id="batchCheck_nopass" class="ui-state-default" value="批量审核驳回" />
				${tip}
	</div>

	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						交易账号
					</th>
					<th>
						会员名称
					</th>
					<th>
						会员类型
					</th>
					<th>
						交易类型
					</th>
					<th>
						金额
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
						   ${accountDeal.type}
						</td>

						<td>
							<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/>
						</td>
						<td>
							${accountDeal.user.realname}
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.createDate}" type="both" />
						</td>
						<td>
							${accountDeal.memo}
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="15">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
				</tr>
			</tbody>
		</table>
	</div>
	</form>
<%@ include file="/common/messageTip.jsp" %>
</body>
