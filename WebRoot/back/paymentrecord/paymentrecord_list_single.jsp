<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglib.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
		<style>
		body,th,td{
			font-size:13px !important;
		}
		
		.error {
			float: left;
		}
		.ui-datepicker{
			font-size:12px;
		}
		.table th, .table td{
			vertical-align: bottom !important;
		}
		</style>
		<link rel="stylesheet" href="/Static/js/showloading/css/showLoading.css" type="text/css" />
		<link rel="stylesheet" href="/Static/css/metro-bootstrap.css" type="text/css" />
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="/Static/js/showloading/jquery.showLoading.min.js"></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<script>
		$(function(){
			
			$(".terminal_f").click(function(){
				if(confirm("将此项目的状态置为\"终结\"，确认这样做吗?")){
					$("body").showLoading();
					var id = $(this).attr("element");
					var this_ = $(this);
					$.post("/back/financingBaseAction!terminal",{'id':id},function(data,status){
						this_.parents("tr").remove();
						$("body").hideLoading();
					});
				}
			});
			$(".open_win").click(function(){
				var _id = $(this).attr("_id");
				var _target = $(this).attr("_target");
				var _action = $(this).attr("_action");
				var form = $("#open_form");
				var input = form.children("input:hidden");
				form.attr("action",_action).attr("target",_target);
				input.val(_id);
				
				window.open("about:blank",_target);
				
				form.submit();
				
			});
			
		});
		
		</script>
	</head>
	<body>
		<table class="table table-striped">
			<thead>
				<tr>
					<th>#</th>
					<th>签约日期</th>
					<th>应还日期</th>
					<th>项目编号</th>
					<th>融资方名称</th>
					<th>融资方交易帐号</th>
					<th>融资方银行帐号</th>
					<th>融资总额</th>
					<th>融资期限</th>
					<th>还款时段</th>
					<th>单条操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${records}" var="iter" varStatus="iter_s">
					<tr>
						<td>
							${iter_s.count }
						</td>
						<td>
							<fmt:formatDate value="${iter['qianyuedate']}" pattern="yyyy-MM-dd" />
						</td>
						<td>
							<fmt:formatDate value="${iter['yhdate']}" pattern="yyyy-MM-dd" />
						</td>
						<td>
							${iter['code']}
						</td>
						<td>
							<script>document.write(name("${iter['realname']}"));</script>
						</td>
						<td>
							${iter['username']}
						</td>
						<td>
							${iter.bankaccount}
						</td>
						<td>
							<fmt:formatNumber value="${iter['currenyamount']}" pattern="#,###,##0.00"/>
						</td>
						<td>
							<c:if test="${(iter['interestday']) != 0}">${iter.interestday}天</c:if>
							<c:if test="${(iter['interestday']) == 0}">${iter.term}月</c:if>
						</td>
						<td>
								<c:if test="${iter.hksd == 1}">上午</c:if>
								<c:if test="${iter.hksd == 0}">不限</c:if>
								<c:if test="${iter.hksd == -1}">下午</c:if>
							
						</td>
						<td>
							<a href="javascript:;" _id="${iter['id']}" class="open_win" _target="prjournaling_win" _action="/back/financingBase/financingBaseAction!prjournaling">电子合同汇总</a>|
							<a href="javascript:;" _id="${iter['id']}" class="open_win" _target="list_paymentRecord_win" _action="/back/paymentRecord/paymentRecordAction!list_paymentRecord">还款明细</a>|
							<a href="javascript:;" class="terminal_f" element="${iter['id']}">结束还款</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			
		</table>
		<form id="open_form" method="post"><input type="hidden" name="id"/></form>
	</body>
</html>
