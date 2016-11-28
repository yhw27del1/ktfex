<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglib.jsp"%>
<c:set var="now" value="<%=new Date() %>"/>
<html>
	<head>
		<title>打印</title>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
		<style>
		body,th,td{
			font-size:13px !important;
		}
		.table-border{
			width:100%;
			border-collapse: 0;
			border-spacing: 0;
			border-top: 1px solid #000;
			border-left: 1px solid #000;
		}
		.table-border td,.table-border th{
			border-bottom: 1px solid #000;
			border-right: 1px solid #000;
		}
		#print_button{
			position: fixed;
			top:10px;
			right:10px;
		}
		.title{
			text-align: center;
		}
		.PageNext
        {
            page-break-after: always;
        }
        .right{float:right;}
		</style>
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<script>
		$(function(){
			
			$("#print_button").click(function(){
				$(this).hide();
				print();
				$(this).show();
			});
			
			
			
			
			
		});
		
		</script>
	</head>
	<body>
		<input type="button" id="print_button" value="打印"/>
		<h3 class="title">昆投互联网金融交易-结算部正常还款批量明细表</h3>
		<div><label>日期</label></div>
		<c:set var="count" value="1"/>
		<c:forEach items="${records}" var="iter" varStatus="state">
			<c:set var="count" value="${count+1}"/>
			<c:if test="${state.count%20 == 1 || state.first}">
				<c:set var="count" value="1"/>
				<table class="table-border">
					<thead>
						<tr>
							<th>序号</th>
							<th>签约日期</th>
							<th>到期日期</th>
							<th>项目编号</th>
							<th>融资方名称</th>
							<th>融资方交易帐号</th>
							<th>还款期次</th>
							<th>当期还款额</th>
							<th>交易帐户余额</th>
							<th>状态</th>
							<th>操作员</th>
							<th>复核员</th>
						</tr>
					</thead>
					<tbody>
			</c:if>
					
						<tr>
							<td>${state.count }</td>
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
								<fmt:formatNumber value="${iter.yhje}" pattern="#,##0.00" />
								<c:set var="yhje_all" value="${iter.yhje+yhje_all}"/>
							</td>
							<td>
								<fmt:formatNumber value="${iter.fbalance}" pattern="#,##0.00" />
								<c:set var="fbalance_all" value="${iter.fbalance+fbalance_all}"/>
							</td>
							<td>
								<c:choose>
									<c:when test="${iter.approve==2}">
										已还
									</c:when>
									<c:otherwise>未还</c:otherwise>
								</c:choose>
							</td>
							<td>${iter.operator }</td>
							<td>${iter.auditor }</td>
							
						</tr>
			<c:if test="${count==20 || state.last}">
					</tbody>
				<c:if test="${state.last}">
					<tfoot>
						<tr>
							<th>合计</th>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th><fmt:formatNumber value="${yhje_all }" pattern="#,##0.00" /></th>
							<th><fmt:formatNumber value="${fbalance_all }" pattern="#,##0.00" /></th>
							<th></th>
							<th></th>
							<th></th>
						</tr>
						
					</tfoot>
				</c:if>
				</table>
				<c:if test="${!state.last}"><div class="PageNext"></div></c:if>
			</c:if>
			<span class="right"><c:if test="${state.last}">打印时间:<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss"/></c:if></span>
			
			 
		</c:forEach>
		
	</body>
</html>
