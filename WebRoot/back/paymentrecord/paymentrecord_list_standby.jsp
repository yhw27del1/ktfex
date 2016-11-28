<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglib.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
		<style>
		body,th,td{
			font-size:13px !important;
			font-family: '微软雅黑';
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
		.alert-success{
			width:370px;
			height:70px;
			position: fixed;
			top:0;
			right:0;
			display: none;
		}
		.alert-success label{display: inline;margin-right: 5px;line-height: 25px;}
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
				if($(this).is(":checked")){
					var date = new Date();
					var date_str = "";
					date_str += date.getFullYear() 
					+""
					+ ((date.getMonth()+1)>9?(date.getMonth()+1):"0"+(date.getMonth()+1) ) 
					+""
					+ (date.getDate() > 9 ? date.getDate() : "0" + date.getDate())
					+""
					+ (date.getHours() > 9 ? date.getHours() : "0" + date.getHours())
					+""
					+ (date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes())
					+""
					+ (date.getSeconds() > 9 ? date.getSeconds() : "0" + date.getSeconds());
					$(this).attr("date",date_str);
					if($(".checkbox_singel").not(":checked").length ==0){
						$(".checkbox_all").attr("checked",true);
					}
				}else{
					$(".checkbox_all").removeAttr("checked");
					$(this).removeAttr("date");
				}
			});
			
			$("#prepare").click(function(){			
				if($(".checkbox_singel:checked").length == 0){
					alert("请至少选一项 ^_^");
					return;
				}
				var postData = "";
				var count = 0;				
				$(".checkbox_singel:checked").each(function(){
					var fid = $(this).attr("fid");
					var qs = $(this).attr("qs");
					var date = $(this).attr("date");
					if(count !=0 ) postData +=",";
					postData += '{"fid":"'+fid+'","qs":'+qs+',"date":"'+date+'"}';
					count++;
				});
				$("body").showLoading();
				$.post("/back/paymentRecord/paymentRecordAction!paymentrecord_do_standby",{"parameters":"{array:["+postData+"]}"},function(data,state){
					var list = data['result'];
					var batch_no = data['batch_no'];
					$.each(list,function(key,val){
						if(val){
							$("input:checkbox[fid='"+key+"']").parents("tr").remove();
						}
					});
					$(".batch-no-label").text(batch_no);
					$(".alert-success").show();
					$("body").hideLoading();
				},'json');
				
				
			});
			$(".close").click(function(){
				$(".alert-success").hide();
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
					<th>融资方交易号</th>
					<th>还款期次</th>
					<th>当期还款额</th>
					<th>交易帐户余额</th>
					<th>融资期限</th>
					<th>单条操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${records}" var="iter">
					<tr>
						<td><input type="checkbox" class="checkbox_singel checkbox" fid="${iter.financbaseid}" qs="${iter.qs}"/></td>
						<td>
							<fmt:formatDate value="${iter.qianyuedate}" pattern="yyyy-MM-dd" />
						</td>
						<td>
							<fmt:formatDate value="${iter.daoqidate}" pattern="yyyy-MM-dd" />
						</td>
						<td>
							${iter.financbasecode}
						</td>
						<td>
							<script>document.write(name("${iter.frealname}"));</script>
						</td>
						<td>
							${iter.financiername}
						</td>
						<td>
							${iter.returntimes}-${iter.qs}
						</td>
						<td>
							<fmt:formatNumber value="${iter.yhje}" pattern="#,##0.00"/>
						</td>
						<td>
							<fmt:formatNumber value="${iter.fbalance}" pattern="#,##0.00"/>
						</td>
						<td>
							<c:if test="${(iter.interestday)!= 0}">${iter.interestday}[天]</c:if>
							<c:if test="${(iter.interestday)== 0}">${iter.term}[月]</c:if>
						</td>
						<td>
							<a href="/back/financingBase/financingBaseAction!prjournaling?id=${iter.financbaseid}" target="_blank">电子合同汇总</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			
		</table>
		<a href="javascript:;" id="prepare" class="btn btn-danger btn-mini">批量预还款</a>
		<div class="alert alert-success">
		  <button class="close" data-dismiss="alert">×</button>
		  <h4><b>操作成功!</b></h4>
		  <label>本次操作批处理号为:</label><label class="batch-no-label"></label>
		</div>
	</body>
</html>
