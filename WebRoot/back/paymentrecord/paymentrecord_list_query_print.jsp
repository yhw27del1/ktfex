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
		form{
			margin:0 !important;
		}
		</style>
		<link rel="stylesheet" href="/Static/css/metro-bootstrap.css" type="text/css" />
		<link rel="stylesheet" href="/Static/js/showloading/css/showLoading.css" type="text/css" />
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="/Static/js/showloading/jquery.showLoading.min.js"></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<script>
		$(function(){
			
			$(".checkbox_all").click(function(){
				if($(this).is(":checked")){
					$(".checkbox_singel").attr("checked",true);
				}else{
					$(".checkbox_singel").removeAttr("checked");
				}
			});
			$(".checkbox_singel").click(function(){
				if($(this).is(":checked") && $(".checkbox_singel").not(":checked").length ==0 ){
					$(".checkbox_all").attr("checked",true);
				}else{
					$(".checkbox_all").removeAttr("checked");
				}
			});
			
			$(".print-button").click(function(){
				if($(".checkbox_singel:checked").length == 0){
					alert("请至少选一项 ^_^");
					return;
				}
				var postData = "";
				var count = 0;
				var form = $("form."+$(this).attr("id"));
				var target = form.attr("target");				
				$(".checkbox_singel:checked").each(function(){
					var fid = $(this).attr("fid");
					var qs = $(this).attr("qs");
					if(count !=0 ) postData +=",";
					postData += '{"fid":"'+fid+'","qs":'+qs+'}';
					count++;
				});
				
				$("input:hidden[name='list']").val("{array:["+postData+"]}");
				
				window.open("about:blank",target);
				form.submit();
				
			});
			
			
			
			
			
		});
		
		</script>
	</head>
	<body>
		
		
		<table class="table table-striped">
			<thead>
				<tr>
					<th><input type="checkbox" class="checkbox_all checkbox"/></th>
					<th>签约日期</th>
					<th>应还日期</th>
					<th>项目编号</th>
					<th>融资方名称</th>
					<th>交易帐号</th>
					<th>还款期次</th>
					<th>当期还款额</th>
					<th>交易帐户余额</th>
					<th>融资期限</th>
					<th>合同汇总</th>
					<th>批处理号</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${records}" var="iter">
					<c:forEach items="${iter.value}" var="list" varStatus="list_s">
					<tr>
						<td><input type="checkbox" class="checkbox_singel checkbox" code="${list.financbasecode}" fid="${list.financbaseid}" qs="${list.qs}"/></td>
						<td>
							<fmt:formatDate value="${list.qianyuedate}" pattern="yyyy-MM-dd" />
						</td>
						<td>
							<fmt:formatDate value="${list.daoqidate}" pattern="yyyy-MM-dd" />
						</td>
						<td>
							${list.financbasecode}
						</td>
						<td>
							<script>document.write(name("${list.frealname}"));</script>
						</td>
						<td>
							${list.financiername}
						</td>
						<td>
							${list.returntimes}-${list.qs}
						</td>
						<td>
							<fmt:formatNumber value="${list.yhje}" pattern="#,##0.00"/>
						</td>
						<td>
							<fmt:formatNumber value="${list.fbalance}" pattern="#,##0.00"/>
						</td>
						<td>
							<c:if test="${(list.interestday)!= 0}">${list.interestday}[天]</c:if>
							<c:if test="${(list.interestday)== 0}">${list.term}[月]</c:if>
						</td>
						<td>
							<a href="/back/financingBase/financingBaseAction!prjournaling?id=${list.financbaseid}" target="_blank">电子合同汇总</a>
						</td>
						<c:if test="${list_s.first}"><td rowspan="${fn:length(iter.value)}">${iter.key}</td></c:if>
					</tr>
					</c:forEach>
				</c:forEach>
			</tbody>
			
		</table>
		<a href="javascript:;" id="print_list" class="print-button btn btn-info btn-mini">打印还款列表</a>
		<a href="javascript:;" id="print_details" class="print-button btn btn-danger btn-mini">打印还款明细</a>
		
		<form class="print_list" action="/back/paymentRecord/paymentRecordAction!paymentrecord_list_do_print" target="paymentrecord_list_do_print" method="post">
			<input type="hidden" name="list"/>
		</form>
		<form class="print_details" action="/back/paymentRecord/paymentRecordAction!paymentrecord_details_do_print" target="paymentrecord_details_do_print" method="post">
			<input type="hidden" name="list"/>
		</form>
	</body>
</html>
