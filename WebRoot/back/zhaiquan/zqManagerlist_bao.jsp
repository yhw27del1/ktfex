<%-- 
2012-08-24 aora 修改此页面：更正在IE6、IE8下不能提交查询条件的问题
--%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>

<style>
	.ui-autocomplete{
		width:120px;
		overflow:hidden;
		padding:0;
		margin:0;
	}
	.ui-autocomplete li {
		width:120px;
		list-style-type: none;
		padding:0;
		margin:0;
	}
	.ui-autocomplete li a:HOVER{
		background-image: none;
	}
	.error{float:left;}
</style>
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<script>
$(function(){
	$(".table_solid").tableStyleUI();
	$(".sh").css({'cursor':'pointer'}).click(function(){
		var id = $(this).attr("id");
		if($("#h"+id).is(":hidden")){
			$(this).attr("src","/Static/js/tree/tabletree/images/minus.gif");
			$("#h"+id).show();
		}else{
			$(this).attr("src","/Static/js/tree/tabletree/images/plus.gif");
			$("#h"+id).hide();
		}
	});
});
function toURL(url,flag) {
		$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:flag,
			ok:function(){
				window.location.href = url;
			},
			cancelVal:'关闭',cancel:true
		});
	}
</script>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="">
	<input type="hidden" name="page" value="1" />
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
	<div style="position:absolute;right:10px;"><button class="ui-state-default reflash" >刷新</button></div>
	<div style="position:absolute;left:10px;">
	项目编号&nbsp;<input type="text" id="querykeyWord" name="querykeyWord" value="${querykeyWord}" style="width:120px;"/>&nbsp;
	<input type="submit" class="ui-state-default" value="查询">
	</div>
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content" id="mytable" style="font-size:12px;">
		<thead>
			<tr class="ui-widget-header ">
				<th></th>
				<th>签约日期</th>
				<th>到期日期</th>
				<th>项目编号</th>
				<th>项目简称</th>
				<th>融资方名称</th>
				<th>融资方交易帐号</th>
				<th>融资方银行帐号</th>
				<th style="text-align: right;">融资期限</th>
				<th>状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${pageView.records}" var="iter">
			<tr id="s${iter.id}">
				<td><img src="/Static/js/tree/tabletree/images/plus.gif" class="sh" id="${iter.id}"/></td>
				<td><fmt:formatDate value="${iter.qianyueDate}" pattern="yyyy-MM-dd"/></td>
				<td><fmt:formatDate value="${iter.daoqiDate}" pattern="yyyy-MM-dd"/></td>
				<td>${iter.code}</td>
				<td>${iter.shortName}</td>
				<td>
				<c:if test="${iter.financier.category=='1'}">
					<script>document.write(name("${iter.financier.pName}"));</script>
				</c:if>
				<c:if test="${iter.financier.category!='1'}">
					<script>document.write(name("${iter.financier.eName}"));</script>
				</c:if>
				</td>
				<td>${iter.financier.user.username}</td>
				<td><script>document.write(bankcard("${iter.financier.bankAccount}"));</script></td>
				<td style="text-align: right;">
				<c:if test="${(iter.interestDay)!= 0}">按日计息(${iter.interestDay}天)</c:if>
			    <c:if test="${(iter.interestDay)== 0}">${iter.businessType.term}个月 </c:if>    
				</td>
				<td>
					${iter.enableZq?"允许转让":"禁止转让"}
				</td>
				<td>
					<c:if test="${iter.enableZq}">
					<button onclick="toURL('/back/zhaiquan/zhaiQuanInvestAction!disableZq?querykeyWord=${querykeyWord}&id=${iter.id}','你确定要设定为禁止转让吗？');return false;" class='ui-state-default'>禁止转让</button>
					</c:if>
					<c:if test="${!iter.enableZq}">
					<button onclick="toURL('/back/zhaiquan/zhaiQuanInvestAction!enableZq?querykeyWord=${querykeyWord}&id=${iter.id}','你确定要设定为允许转让吗？');return false;" class='ui-state-default'>允许转让</button>
					</c:if>
				</td>
			</tr>
			<tr id="h${iter.id}" style="display:none">
				<td style="background-color:#fff"></td>
				<td colspan="12" style="padding:0;margin:0;background-color:#fff" valign="top">
					<table cellpadding="0" cellspacing="0" style="margin-top:-1px;margin-left:-1px;width:auto;" >
						<thead>
						<tr>
							<th>
								债权代码
							</th>
							 <th>
								持有人编号
							</th>   
							 <th>
								持有人姓名
							</th>
							<th>
							           债权期末值
							</th>
							<th>
							          债权成本价
							</th>  
							<th>
								挂牌状态
							</th> 
							<th>
								债权状态
							</th> 
						</tr>
						</thead>
						<tbody>
						<c:forEach items="${iter.investrecords}" var="entry">
						<tr>
							<td>
								${entry.zhaiQuanCode}
							</td>
							 <td>
								${entry.investor.user.username}
							</td>   
							 <td>
								${entry.investor.pName}
							</td>
							<td>
							    <fmt:formatNumber value='${entry.qmz}' type="currency" currencySymbol="" />
							</td>
							<td>
							    <fmt:formatNumber value='${entry.cbj}' type="currency" currencySymbol="" />
							</th>  
							<td>
								<c:if test="${entry.zqzrState=='0'}"><span style="color:#4169E1;">待挂牌</span></c:if>
								<c:if test="${entry.zqzrState=='1'}"><span style="color:green;">挂牌</span></c:if>
								<c:if test="${entry.zqzrState=='2'}"><span style="color:red;">停牌 </span></c:if>
								<c:if test="${entry.zqzrState=='3'}"><span style="color:#4169E1;">挂牌</span></c:if>
								<c:if test="${entry.zqzrState=='4'}"><span style="color:green;">摘牌</span></c:if> 
							</td> 
							<td>
								<c:if test="${entry.sellingState=='0'}"><span style="color:#4169E1;">持有</span></c:if>
								<c:if test="${entry.sellingState=='1'}"><span style="color:green;">出让中</span></c:if>
								<c:if test="${entry.sellingState=='2'}"><span style="color:#4169E1;">求购中 </span></c:if>
								<c:if test="${entry.sellingState=='3'}"><span style="color:red;">成功</span></c:if>
							</td> 
						</tr>
						</c:forEach>
						</tbody>
					</table>
				</td>
			</tr>
		</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="12">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tfoot>
	</table>
</div>
</form>
</body>
