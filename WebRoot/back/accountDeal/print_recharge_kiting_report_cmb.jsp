<%@ page language="java" import="java.util.*,com.kmfex.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>

<html>
	<head>
		<title>昆明商企业金融服务有限公司-业务充值、提现日结单</title>
		<style>
			body{
				font-family: "微软雅黑";
			}
			.center{
				margin:0 auto;
				width:80%;
				padding-top:10px;
				text-align: center;
				position: relative;
				clear: both;
				
			}
			table,td,th{
				border:1px solid #000;   
    			border-collapse:collapse;
			}
			#print_button{
				position: fixed;
				right:10px;
				top:10px;
				z-index:1000;
			}
			.table_foot{
				float:right;
				margin-right:80px;
			}
			.show_date{
			
				border:none;
				outline: none;
				width:20px;
				font-size:15px;
			}
		</style>
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<script>
			$(function(){
				$(window).keypress(function(e){
					if(e.keyCode==13){
						var str = "";
						$(".show_date").each(function(){
							str+=$(this).val()+"-";
						});
						str = str.substr(0,str.length-1);
						$("#form_date").val(str);
						$("#submit_form").submit();
					}
				});
			});
		</script>
	</head>
	<body>
		<input type="button" value="打印" id="print_button"/>
		<div class="center">
		<h3>昆明商企业金融服务有限公司-业务充值、提现日结单</h3>
		
		<form action="" method="post" id="submit_form">
			<input type="hidden" name="date" id="form_date"/>
			<span style="float:left;">
				日期：
					<input type="text" value="<fmt:formatDate value="${date}" pattern="yyyy"/>" class="show_date" style="width:35px;"/>年
					<input type="text" value="<fmt:formatDate value="${date}" pattern="MM"/>" class="show_date"/>月
					<input type="text" value="<fmt:formatDate value="${date}" pattern="dd"/>" class="show_date"/>日
			</span>
		</form>
		
		<table width="100%">
			<thead>
				<tr style="background:#eeeeee">
					<th rowspan="2">授权中心</th> 
					<th rowspan="2">业务内容</th>
					<th rowspan="2">笔数</th>
					<th colspan="1">充值金额</th>
					<th rowspan="2">笔数</th>
					<th colspan="1">提现金额</th>
				</tr>
				<tr style="background:#eeeeee">
					<th>招行</th>
					<th>招行</th>
				</tr>
			</thead>
			<tbody style="text-align: center;">
				<c:set value="0" var="cz_bs_hj"/>
				<c:set value="0" var="cz_je_hj"/>
				<c:set value="0" var="tx_bs_hj"/>
				<c:set value="0" var="tx_je_hj"/>
				
				<c:forEach items="${list}" var="item">
					<c:set value="0" var="cz_bs_xj"/>
					<c:set value="0" var="cz_je_xj"/>
					<c:set value="0" var="tx_bs_xj"/>
					<c:set value="0" var="tx_je_xj"/>
					<c:forEach items="${item.value}" var="item_i" varStatus="state">
						<tr>
							<c:if test="${state.index == 0}">
								<td rowspan="${fn:length(item.value)}">${item.key}</td>
							</c:if>
							<td>${item_i.key}</td>
							
							
							<c:if test="${fn:contains(item_i.key,'提现')}">
								<td></td>
								<td></td>
								<td>${item_i.value.count}</td>
								<td>
									<fmt:formatNumber value="${item_i.value.cmb == null ? 0 : item_i.value.cmb}" pattern="#,##0.00"/>
								</td>
								<c:set value="${tx_bs_xj+item_i.value.count}" var="tx_bs_xj"/>
								<c:set value="${tx_je_xj+item_i.value.cmb}" var="tx_je_xj"/>
							</c:if>
							
							<c:if test="${fn:contains(item_i.key,'提现') == false}">
								<td>${item_i.value.count}</td>
								<td>
									<fmt:formatNumber value="${item_i.value.cmb == null ? 0 : item_i.value.cmb}" pattern="#,##0.00"/>
								</td>
								<td></td>
								<td></td>
								
								<c:set value="${cz_bs_xj+item_i.value.count}" var="cz_bs_xj"/>
								<c:set value="${cz_je_xj+item_i.value.cmb}" var="cz_je_xj"/>
								
							</c:if>
							
						</tr>
					</c:forEach>
					<tr style="background:#eeeeee">
						<td colspan="2">小计</td>
						<td>${cz_bs_xj}</td>
						<td><fmt:formatNumber value="${cz_je_xj}" pattern="#,##0.00"/></td>
						<td>${tx_bs_xj}</td>
						<td><fmt:formatNumber value="${tx_je_xj}" pattern="#,##0.00"/></td>
						<c:set value="${cz_bs_xj + cz_bs_hj}" var="cz_bs_hj"/>
						<c:set value="${cz_je_xj + cz_je_hj}" var="cz_je_hj"/>
						<c:set value="${tx_bs_xj + tx_bs_hj}" var="tx_bs_hj"/>
						<c:set value="${tx_je_xj + tx_je_hj}" var="tx_je_hj"/>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr style="background:#eeeeee">
					<th colspan="2">合计</th>
					<th>${cz_bs_hj}</th>
					<th><fmt:formatNumber value="${cz_je_hj }" pattern="#,##0.00"/></th>
					<th>${tx_bs_hj }</th>
					<th><fmt:formatNumber value="${tx_je_hj }" pattern="#,##0.00"/></th>
				</tr>
			</tfoot>
		</table>
		<span class="table_foot">制表：</span>
		<span class="table_foot">记帐：</span>
		<span class="table_foot">复核：</span>
		</div>
	</body>
</html>
<script type="text/javascript">
<!--
	var obj = document.getElementById("print_button");
	if (window.addEventListener) {
		obj.addEventListener('click', print_action, false);
	} else if (window.attachEvent) {
		obj.attachEvent('onclick', print_action);
	}
	function print_action(){
		obj.style.display="none";
		print();
		obj.style.display="";
	}
	
//-->
</script>
