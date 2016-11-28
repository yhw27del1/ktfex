<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<html>
	<head>
		<title>哪些人</title>
	</head>
	<body style="padding:0;margin:0;">
		<span style="font-size: 16px;">委托人数:${n}</span><br />
		<span style="font-size: 16px;">委托金额:${w}</span><br />
		<span style="font-size: 16px;">可用余额汇总:${sum_balance}</span><br />
		<span style="font-size: 16px;">可用余额(整千)汇总:${sum_balance_zq}</span><br />
		<!-- -<span style="font-size: 16px;">风险评级：${fxpj}</span><br />
		<span style="font-size: 16px;">还款方式：${hkfs}</span><br />
		<span style="font-size: 16px;">担保方式：${dbfs}</span><br />
		<span style="font-size: 16px;">期限及年利率：${qx}，${nll}%</span> -->
		<table id="tt" class="easyui-datagrid" data-options="remoteSort:false,singleSelect:true,rownumbers:true">
			<thead>
				<tr>
					<th data-options="field:'username',width:80">交易帐号</th>
					<th data-options="field:'balance',width:100">可用余额</th>
				    <th data-options="field:'param8',width:100">可用余额不低于</th>   
				    <th data-options="field:'param9',width:100">单笔投标最大金额</th> 
					<th data-options="field:'min',width:90">最小投标</th>
					<th data-options="field:'max',width:90">最大投标</th>
					<th data-options="field:'param1',width:70"><span id="param1" style="color: green;text-decoration: underline;">风险评级</span></th>
					<th data-options="field:'param2',width:70"><span id="param2" style="color: green;text-decoration: underline;">还款方式</span></th>
					<th data-options="field:'param5',width:70"><span id="param5" style="color: green;text-decoration: underline;">担保方式</span></th>
					
					<th data-options="field:'dayMin',width:50">最小天</th>
					<th data-options="field:'dayMax',width:50">最大天</th>
					<th data-options="field:'param7',width:80">按天年利率</th>
					
					<th data-options="field:'monthMin',width:50">最小月</th>
					<th data-options="field:'monthMax',width:50">最大月</th>
					<th data-options="field:'param6',width:80">按月年利率</th>
					<th data-options="field:'levelScore',width:50">优先级</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list}" var="item">
					<tr>
						<td>${item.username}</td>
						<td>${item.balance}</td>
						<td>${item.param8}</td>
						<td>${item.param9}</td> 
						<td>${item.min}</td>
						<td>${item.max}</td>
						<td>${item.param1}</td>
						<td>${item.param2}</td>
						<td>${item.param5}</td>
						
						<td>${item.dayMin}</td>
						<td>${item.dayMax}</td>
						<td>${item.param7}</td>
						
						<td>${item.monthMin}</td>
						<td>${item.monthMax}</td>
						<td>${item.param6}</td>
						<td>${item.levelScore}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</body>
</html>
