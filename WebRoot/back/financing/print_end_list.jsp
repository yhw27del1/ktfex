<%@ page language="java" import="java.util.*,com.kmfex.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<c:set var="now" value="<%=new Date() %>"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html>
	<head>
		<title>昆明商企业金融服务有限公司-融资结清明细月报</title>
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
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
	</head>
	<body>
		<input type="button" value="打印" id="print_button"/>
		
			<c:set value="1" var="page"/>
			<c:set value="0" var="count"/>
			
			<%
				ArrayList<LinkedHashMap<String, Object>> list = (ArrayList<LinkedHashMap<String, Object>>)request.getAttribute("list");
				int list_size = list.size();
				int _pagecount = 0;
				while(++_pagecount * 25 < list_size);
			%>
			<c:set value="<%=_pagecount %>" var="pagecount"/>
			<c:forEach items="${list}" varStatus="status" var="item">
				
					<c:choose>
						<c:when test="${page ==1 && count == 0}">
							<div class="center">
							<img src="/Static/images/logo.png" class="logo"/>
							<div class="title">昆投互联网金融交易-融资结清明细月报</div>
							<div style="float:left; font-size:13px;">
								会计期间:<fmt:formatDate value="${startDate}" pattern="yyyy年MM月dd日"/>-<fmt:formatDate value="${endDate}" pattern="yyyy年MM月dd日"/>
							</div>
							<div style="float:right;font-size:13px;">担保机构：${org.name}&nbsp;&nbsp;&nbsp;&nbsp;机构编码：${org.showCoding}</div>
							<table width="100%" style="font-size:13px;">
								<thead>
									<tr>
										<th>序号</th><th>结清日期</th><th>签约日期</th><th>项目编号</th><th>融资户名</th><th>总额</th><th>${org_code}</th><th>其它授权中心</th><th>期限</th><th>状态</th>
									</tr>
								</thead>	
								<tbody>
						</c:when>
						<c:when test="${page != 1 && count == 0}">
							<div class="nextpage center">
								<img src="/Static/images/logo.png" class="logo"/>
								<div class="title">昆投互联网金融交易-融资结清明细月报</div>
								<div style="float:left;font-size:13px;">
									会计期间:<fmt:formatDate value="${startDate}" pattern="yyyy年MM月dd日"/>-<fmt:formatDate value="${endDate}" pattern="yyyy年MM月dd日"/>
								
								</div>
								<div style="float:right;font-size:13px;">担保机构：${org.name}&nbsp;&nbsp;&nbsp;&nbsp;机构编码：${org.showCoding}</div>
							<table width="100%" style="font-size:13px;">
								<thead>
									<tr>
										<th>序号</th><th>结清日期</th><th>签约日期</th><th>项目编号</th><th>融资户名</th><th>总额</th><th>${org_code}</th><th>其它</th><th>期限</th><th>状态</th>
									</tr>
								</thead>	
								<tbody>
						</c:when>
					</c:choose>
						
					
							
							<tr>
								<td>${status.count}</td>
								<td><fmt:formatDate value="${item.terminal_date}" pattern="yyyy-MM-dd"/></td>
								<td>${item.qianyuedate}</td>
								<td>${item.financecode}</td>
								<td>${item.financerrealname}</td>
								<td>
									<fmt:formatNumber pattern="#,##0.00" value="${item.currenyamount}"/>
									<c:set var="currenyamount_sum" value="${currenyamount_sum + item.currenyamount}"/>
								</td>
								<td>
									<c:choose>
										<c:when test="${item.mainamount == null}">-</c:when>
										<c:otherwise>
											<fmt:formatNumber pattern="#,##0.00" value="${item.mainamount}"/>
											<c:set var="mainamount_sum" value="${mainamount_sum + item.mainamount}"/>
										</c:otherwise>
									</c:choose>
								</td>
								<td>
									<c:choose>
										<c:when test="${item.otheramount == null}">-</c:when>
										<c:otherwise>
											<fmt:formatNumber pattern="#,##0.00" value="${item.otheramount}"/>
											<c:set var="otheramount_sum" value="${otheramount_sum + item.otheramount}"/>
										</c:otherwise>
									</c:choose>
								</td>
								<td>
									<c:if test="${item.interestday == 0 }">
										${item.term }[月]
									</c:if>
									<c:if test="${item.interestday != 0 }">
										${item.interestday }[天]
									</c:if>
								</td>
								<td>已结束</td>
							</tr>
							
					<c:set value="${count+1}" var="count"/>
					
					<c:if test="${count == 25 || status.last}">
						<c:set value="0" var="count"/>
						<c:set value="${page+1}" var="page"/>
					</c:if>
					
					
					
					<c:if test="${count == 0}">
						</tbody>
						</table>
						<div class="show_page">第${page-1}页 &nbsp;&nbsp;&nbsp;&nbsp; 共${pagecount}页</div>
						</div>
					</c:if>
					
					
					<c:if test="${status.last}">
						<script>
							$(function(){
								var tfoot = $("<tfoot></tfoot>");
								var tr = $("<tr></tr>");
								tr.append("<th>合计</th>");
								tr.append("<th></th>");
								tr.append("<th></th>");
								tr.append("<th></th>");
								tr.append("<th></th>");
								tr.append("<th><fmt:formatNumber pattern="#,##0.00" value="${currenyamount_sum}"/></th>");
								tr.append("<th><fmt:formatNumber pattern="#,##0.00" value="${mainamount_sum}"/></th>");
								tr.append("<th><fmt:formatNumber pattern="#,##0.00" value="${otheramount_sum}"/></th>");
								tr.append("<th></th>");
								tr.append("<th></th>");
								tfoot.append(tr);
								$("table:last").append(tfoot);
							});
						</script>
						<div class="foot">
							<div>经办:</div>
							<div>复核:</div>
							<div style="width:260px;">打印时间:<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
						</div>
					</c:if>	
					
					
				
			</c:forEach>
			
		
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
