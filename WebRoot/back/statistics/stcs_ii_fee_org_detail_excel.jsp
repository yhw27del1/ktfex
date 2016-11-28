<%@ page language="java" import="java.util.*" contentType="application/msexcel" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>

<%
  response.setHeader("Content-Disposition", "inline;filename=transactionfees.xls");
	Date now = new Date();
	
%>
<c:set value="<%=now %>" var="now"/>
<html>
	<head>
		<title>昆明商企业金融服务有限公司-交易手续费详单打印</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
			
			.nextpage{
				page-break-before: always;
				clear: both;
			}  
			.w80{
				width:80px;
			}
			.w20{
				width:20px;
			}
			
			.al{
				text-align: left;
			}
			.w240{
				width:240px;
			}
			.foot{
				float:right;
				margin-right:30px;
				font-size:12px;
			}
			.foot div{
				float:left;
				width:120px;
			}
			.logo{
				position: absolute;
				width:50px;
				height:50px;
				left:10px;
			}
			.title{
				font-size:20px;
				text-align: center;
				margin-top:30px;
			}
			td{
			font-size:12px;
			}
			.show_page{
				font-size:12px;
			}
		</style>
		<style>
		   .xlsMoney{mso-number-format:"0\.0";}
		   .xlsDate{mso-number-format:"yyyy\/mm\/dd hh\:mm\:ss";}
		   .xlsDate1{mso-number-format:"yyyy-mm-dd";}
		   .xlsDate2{mso-number-format:"yyyy年mm月";}
		   .xlsText{mso-number-format:"\@";} 
		</style>
	</head>
	<body>
		
		<table>
			<thead>
				<tr>
					<th colspan="5">昆明商金融交易服务有限公司</th>
				</tr>
				<tr>
					<th colspan="5">交易手续费明细&nbsp;&nbsp;&nbsp;统计区间<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>-<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/></th>
				</tr>
				<tr>
					<th>投资人帐号</th>
					<th>融资项目号</th>
					<th>还款日期</th>
					<th>收益金额</th>
					<th>产生交易手续费</th>
				</tr>
			</thead>
			<tbody>
			
			<c:forEach var="item" items="${list}" >
				<tr>
					<td>${item.username }</td>
					<td>${item.code }</td>
					<td><fmt:formatDate value="${item.shdate }" pattern="yyyy-MM-dd"/></td>
					<td align="right"><fmt:formatNumber value="${item.sy }" pattern="#,##0.00"/><c:set value="${item.sy+sy_sum}" var="sy_sum"/></td>
					<td align="right"><fmt:formatNumber value="${item.ii_fee }" pattern="#,##0.00"/><c:set value="${item.ii_fee+ii_fee_sum}" var="ii_fee_sum"/></td>
				</tr>
			</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<th><fmt:formatDate value="${now}" pattern="yyyy-MM-dd"/></th>
					<th></th>
					<th></th>
					<th align="right"><fmt:formatNumber value="${sy_sum }" pattern="#,##0.00"/></th>
					<th align="right"><fmt:formatNumber value="${ii_fee_sum }" pattern="#,##0.00"/></th>
				</tr>
			</tfoot>
		</table>
	</body>
</html>
