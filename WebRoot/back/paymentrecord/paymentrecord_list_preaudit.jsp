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
			
			$("#pass").click(function(){
				//alert("系统已停止服务...-_-!!!");
				//return;
				if($(".checkbox_singel:checked").length == 0){
					alert("请至少选一项 ^_^");
					return;
				}
				var postData = "";
				var count = 0;				
				$(".checkbox_singel:checked").each(function(){
					var fid = $(this).attr("fid");
					var qs = $(this).attr("qs");
					if(count !=0 ) postData +=",";
					postData += '{"fid":"'+fid+'","qs":'+qs+'}';
					count++;
				});
				$("body").showLoading();
				$.post("/back/paymentRecord/paymentRecordAction!paymentrecord_do_audit",{"parameters":"{array:["+postData+"]}"},function(data,state){
					var str = "";
					var code = data['code'];
					if(typeof(code) != "undefined"){
						str = data['tip'];
						alert(str);
						$("body").hideLoading();
						return;
					}
					
					
					var list = data['result'];
					$.each(list,function(key,val){
						var item = $("input:checkbox[fid='"+key+"']");
						var code = item.attr("code");
						var qs = item.attr("qs");
						if(val == 1 ){
							item.parents("tr").remove();
							str += "项目编号:"+code+"\n还款期数:"+qs+"\n状态:成功";
						}else if(val==0){
							str += "项目编号:"+code+"\n还款期数:"+qs+"\n状态:失败\n出现未知错误，请稍后重试";
						}else if(val == -1){
							str += "项目编号:"+code+"\n还款期数:"+qs+"\n状态:失败\n融资方帐户余额不足";
						}else if(val == -2){
							str += "项目编号:"+code+"\n还款期数:"+qs+"\n状态:失败\n同步锁异常，请稍后重试";
						}else if(val == -3){
							str += "项目编号:"+code+"\n还款期数:"+qs+"\n状态:失败\n该融资项目已发生变化，请刷新后重试";
						}
					});
					alert(str);
					$("body").hideLoading();
				},'json');
				
				
			});
			
			
			
			$("#unpass").click(function(){
				//alert("系统已停止服务...-_-!!!");
				//return;
				if($(".checkbox_singel:checked").length == 0){
					alert("请至少选一项 ^_^");
					return;
				}
				var postData = "";
				var count = 0;				
				$(".checkbox_singel:checked").each(function(){
					var fid = $(this).attr("fid");
					var qs = $(this).attr("qs");
					if(count !=0 ) postData +=",";
					postData += '{"fid":"'+fid+'","qs":'+qs+'}';
					count++;
				});
				$("body").showLoading();
				$.post("/back/paymentRecord/paymentRecordAction!paymentrecord_do_unaudit",{"parameters":"{array:["+postData+"]}"},function(data,state){
					var list = data['result'];
					$.each(list,function(key,val){
						if(val){
							$("input:checkbox[fid='"+key+"']").parents("tr").remove();;
						}
					});
					$("body").hideLoading();
				},'json');
				
				
			});
			
		});
		
		</script>
	</head>
	<body>
		<table class="table table-striped table-condensed">
			<thead>
				<tr>
					
					<th><input type="checkbox" class="checkbox_all checkbox"/></th>
					<th>签约日期</th>
					<th>应还日期</th>
					<th>项目编号</th>
					<th>融资方名称</th>
					<th>融资方交易帐号</th>
					<th>还款期次</th>
					<th>当期还款额</th>
					<th>交易帐户余额</th>
					<th>融资期限</th>
					<th>单条操作</th>
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
		<a href="javascript:;" id="pass" class="btn btn-danger btn-mini">批量审核通过</a>
		<a href="javascript:;" id="unpass" class="btn btn-mini">批量审核驳回</a>
	</body>
</html>
