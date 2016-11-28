<%@ page language="java" import="java.util.*" contentType="application/msexcel" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="now" value="<%=new Date()%>" />
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	response.setHeader("Content-Disposition", "inline;filename=" + sdf.format(new Date()) + ".xls");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html>
	<head>
		<title>昆明商企业金融服务有限公司-融资结清明细月报</title>
		<style>
body {
	font-family: "微软雅黑";
}

.center {
	margin: 0 auto;
	width: 80%;
	padding-top: 10px;
	text-align: center;
	position: relative;
	clear: both;
}

table.data,table.data td,table.data th {
	border: 1px solid #000;
	border-collapse: collapse;
}

#print_button {
	position: fixed;
	right: 10px;
	top: 10px;
	z-index: 1000;
}

.nextpage {
	page-break-before: always;
	clear: both;
}

.w80 {
	width: 80px;
}

.w20 {
	width: 20px;
}

.al {
	text-align: left;
}

.w240 {
	width: 240px;
}

.foot {
	float: right;
	margin-right: 30px;
	font-size: 12px;
}

.foot div {
	float: left;
	width: 120px;
}

.logo {
	position: absolute;
	width: 50px;
	height: 50px;
	left: 10px;
}

.title {
	font-size: 20px;
	text-align: center;
	margin-top: 30px;
}

td {
	font-size: 12px;
}

.show_page {
	font-size: 12px;
}
</style>
	</head>
	<body>
		<div class="center">
			<div class="title">
				昆投互联网金融交易-融资结清明细月报
			</div>
			<table style="border:none !important;">
				<tr>
					<td colspan="4">
						会计期间:
						<fmt:formatDate value="${startDate}" pattern="yyyy年MM月dd日" />
						-
						<fmt:formatDate value="${endDate}" pattern="yyyy年MM月dd日" />
					</td>
					<td colspan="6" align="right">
						担保机构：${org.name}&nbsp;&nbsp;机构编码：${org.showCoding}
					</td>
				</tr>
			</table>
				
				
			<table width="100%" style="font-size: 13px;" class="data">
				<thead>
					<tr>
						<th>
							序号
						</th>
						<th>
							结清日期
						</th>
						<th>
							签约日期
						</th>
						<th>
							项目编号
						</th>
						<th>
							融资户名
						</th>
						<th>
							总额
						</th>
						<th>
							${org_code}
						</th>
						<th>
							其它授权中心
						</th>
						<th>
							期限
						</th>
						<th>
							状态
						</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" varStatus="status" var="item">


						<tr>
							<td>
								${status.count}
							</td>
							<td>
								${item.terminal_date}
							</td>
							<td>
								${item.qianyuedate}
							</td>
							<td>
								${item.financecode}
							</td>
							<td>
								${item.financerrealname}
							</td>
							<td>
								<fmt:formatNumber pattern="#,##0.00" value="${item.currenyamount}" />
								<c:set var="currenyamount_sum" value="${currenyamount_sum + item.currenyamount}" />
							</td>
							<td>
								<c:choose>
									<c:when test="${item.mainamount == null}">-</c:when>
									<c:otherwise>
										<fmt:formatNumber pattern="#,##0.00" value="${item.mainamount}" />
										<c:set var="mainamount_sum" value="${mainamount_sum + item.mainamount}" />
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${item.otheramount == null}">-</c:when>
									<c:otherwise>
										<fmt:formatNumber pattern="#,##0.00" value="${item.otheramount}" />
										<c:set var="otheramount_sum" value="${otheramount_sum + item.otheramount}" />
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								${item.term }
							</td>
							<td>
								已结束
							</td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
					<tr>
						<th>
							合计
						</th>
						<th></th>
						<th></th>
						<th></th>
						<th></th>
						<th>
							<fmt:formatNumber pattern="#,##0.00" value="${currenyamount_sum}" />
						</th>
						<th>
							<fmt:formatNumber pattern="#,##0.00" value="${mainamount_sum}" />
						</th>
						<th>
							<fmt:formatNumber pattern="#,##0.00" value="${otheramount_sum}" />
						</th>
						<th></th>
						<th></th>
					</tr>
				</tfoot>
			</table>
		</div>

		<div class="foot">
			<div>
				经办:
			</div>
			<div>
				复核:
			</div>
			<div style="width: 260px;">
				打印时间:
				<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss" />
			</div>
		</div>

	</body>
</html>
