<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html>
	<head>
		<title>昆明商企业金融服务有限公司-融资成交明细月报</title>
		<style>
			body {
				font-family: "微软雅黑";
			}
			table,td,th {
				border: 1px solid #000;
				border-collapse: collapse;
				padding:0 5px 0 5px;
			}
			tbody tr:hover{
				background-color:#d7d7d7;
			}

		</style>
	</head>
	<body>
		<table>
			<thead>
				<tr>
					<th>债权编码</th>
					<th>转让协议</th>
					<th>持有人</th>
					<th>持有截至日期</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list}" var="item">
				<tr>
					<td>${item.zhaiQuanCode}</td>
					<td><a href="/back/zhaiquan/contractAction!view?id=${item.id}" target="_blank">${item.xieyiCode}</a></td>
					<td>${item.seller.username}/${item.seller.realname}</td>
					<td>${item.fbrq}</td>
					
				</tr>
				</c:forEach>
			</tbody>
		</table>
	</body>
</html>
