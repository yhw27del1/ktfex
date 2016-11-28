<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<html>
	<head>
		<title>哪些人</title>
	</head>
	<script type="text/javascript">
		$(function(){
		    var height_ = document.body.clientHeight ;
		    $("#tt").datagrid({
		        height: height_
		    });
		    $("#search").click(function(){
		    	var id = "${id}";
		        var source_url = '/back/autoInvestAction!draws_data';
		        var url_ = source_url+"?id="+id;
		        $("#tt").datagrid({
		            url: url_
		        });
		    });
		    
		    $(window).resize(function(){
		        $("#tt").datagrid('resize');
		    });
		});
	</script>
	<body style="padding:0;margin:0;">
		<span id="info" style="font-size: 16px;"></span><br />
		<table id="tt" class="easyui-datagrid"
            data-options="rownumbers:true,
            singleSelect:true,
            url:'/back/autoInvestAction!draws_data?id=${id}',
            method:'get',
            onLoadSuccess:loadSuccess,
            showFooter: true
            ">
			<thead>
				<tr>
					<th data-options="field:'username',width:100">交易帐号</th>
					<th data-options="field:'balance',width:150">可用余额</th>
				    <th data-options="field:'param8',width:100">可用余额不低于</th>   
				    <th data-options="field:'param9',width:100">单笔投标最大金额</th> 
					<th data-options="field:'min',width:120">最小投标</th>
					<th data-options="field:'max',width:120">最大投标</th>
					<th data-options="field:'prePrice',width:120">中奖额</th>
					<th data-options="field:'levelScore',width:120">优先级</th>
				</tr>
			</thead>
		</table>
		<table>
			<tr>
				<td width="126"></td>
				<td width="150"></td>
				<td width="150"></td>
				<td width="150"></td>
				<td width="150"></td>
				<td width="120"></td>
				<td width="120"></td>
				<td width="120"></td>
				<td width="150"><c:if test="${menuMap['-3'] == 'inline' }"><a href="#" onclick="autoInvests('${id}');"  class="easyui-linkbutton" iconCls="icon-save">成交</a></c:if></td>
			</tr>
		</table>
	</body>
</html>
