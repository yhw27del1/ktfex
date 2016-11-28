<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<c:set value="<%=new java.util.Date() %>" var="now"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>融资项目还款明细</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
   <%@ include file="/common/import.jsp" %>
   <script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
   <script>
			$(function(){
				$(".table_solid").tableStyleUI(); 
				$("#print_button").click(function(){
					$(this).hide();
					print();
					$(this).show();
				})
			});
		</script>
	<style>
	body{
		padding-right:3px;
	}
		#this_table{
			white-space: nowrap;
		}
		#print_button{
			position:fixed;
			top:0;
			left:0;
		}
	</style>
  </head>
  
  <body>
  	<input type="button" value="打印" id="print_button"/>
  	<h2 style="text-align:center">融资项目还款明细</h2>
  	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" style="line-height:30px;">
  		<img src="/Static/images/logo.png" style="position: absolute;right:10px;top:5px;width:50px;height:50px;"/>
  		<div style="font-size:15px;float:left;">
  			项目编号:<span id="fcode" style="color:#000;"></span> <span>期数:${qs}/<span id="returntimes"></span></span>
  		</div>
  		<div style="float:right;margin-right:30px;">
  			统计日期：<span style="color:#E91313"><fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" /></span>&nbsp;到&nbsp;<span style="color:#E91313"><fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/></span>
  		</div>
  	</div>
<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content" id="this_table">
		  <thead>
		  	<tr class="ui-widget-header ">
		  		<th>名称</th>
		  		<th>投标额</th>
		  		<th>投标日期</th>
		  		<th>应还日期</th>
		  		<th>实还日期</th>
		  		<c:if test="${state==0}">
		  			<th>应还本金</th>
		  			<th>应还利息</th>
		  		</c:if>
		  		<c:if test="${state!=0}">
		  			<th>实还本金</th>
		  			<th>实还利息</th>
		  		</c:if>
		  		<th>罚金</th>
		  		<th>服务费</th>
		  		<th>罚金</th>
		  		<th>担保费</th>
		  		<th>罚金</th>
		  		<th>状态</th>
		  		<th>备注</th>
		  	</tr>
		  	
		  </thead>
		  <tbody class="table_solid">
	  		<c:forEach items="${resultList}" var="item">
	  			<c:set value="${item.financbasecode}" var="financbasecode"/>
	  			<c:set value="${item.returntimes}" var="returntimes"/>
		  		<tr>
		  			<td>${item.investorrealname}</td>
		  			<td>${item.investamount}</td>
		  			<td><fmt:formatDate value="${item.createdate}" pattern="yy/MM/dd"/></td>
		  			<td><fmt:formatDate value="${item.yhdate}" pattern="yy/MM/dd"/></td>
		  			<td><fmt:formatDate value="${item.shdate}" pattern="yy/MM/dd"/></td>
		  			<td>
		  				<fmt:formatNumber value="${item.yhbj}" pattern="#0.00"/>
		  				<fmt:formatNumber value="${item.yhbj+hjbj}" pattern="#0.00" var="hjbj"/>
		  			</td>
		  			<td>
		  				<fmt:formatNumber value="${item.yhlx}" pattern="#0.00"/>
		  				<fmt:formatNumber value="${item.yhlx+hjlx}" pattern="#0.00" var="hjlx"/>
		  			</td>
		  			<td>
		  				
		  				<fmt:formatNumber value="${item.fj}" pattern="#0.00"/>
		  				<fmt:formatNumber value="${item.fj+hjfj}" pattern="#0.00" var="hjfj"/>
		  			</td>
		  			<td>
		  						<c:if test="${state==0}">
		  							<fmt:formatNumber value="${item.yhfee1}" pattern="#0.00" />
		  							<fmt:formatNumber value="${hjfee1+item.yhfee1}" pattern="#0.00" var="hjfee1"/>
		  						</c:if>
		  						<c:if test="${state!=0}">
		  							<fmt:formatNumber value="${item.shfee1}" pattern="#0.00" />
		  							<fmt:formatNumber value="${hjfee1+item.shfee1}" pattern="#0.00" var="hjfee1"/>
		  						</c:if>
		  					
		  			</td>
		  			<td>
		  				<fmt:formatNumber value="${item.fj1}" pattern="#0.00" />
		  				<fmt:formatNumber value="${hjfj1+item.fj1}" pattern="#0.00" var="hjfj1"/>
		  			</td>
		  			<td>
		  						<c:if test="${state==0}">
		  							<fmt:formatNumber value="${item.yhfee2}" pattern="#0.00" />
		  							<fmt:formatNumber value="${hjfee2+item.yhfee2}" pattern="#0.00" var="hjfee2"/>
		  						</c:if>
		  						<c:if test="${state!=0}">
		  							<fmt:formatNumber value="${item.shfee2}" pattern="#0.00" />
		  							<fmt:formatNumber value="${hjfee2+item.shfee2}" pattern="#0.00" var="hjfee2"/>
		  						</c:if>
		  					
		  			</td>
		  			<td>
		  				<fmt:formatNumber value="${item.fj2}" pattern="#0.00" />
		  				<fmt:formatNumber value="${hjfj2+item.fj2}" pattern="#0.00" var="hjfj2"/>
		  			</td>
		  			<td>
		  				<c:choose>
		  					<c:when test="${item.state==0}">
		  						未还款
		  					</c:when>
		  					<c:when test="${item.state==1}">
		  						正常还款
		  					</c:when>
		  					<c:when test="${item.state==2}">
		  						提前还款(${item.overdue_days})
		  					</c:when>
		  					<c:when test="${item.state==3}">
		  						逾期还款(${item.overdue_days})
		  					</c:when>
		  					<c:when test="${item.state==4}">
		  						担保代偿
		  					</c:when>
		  				</c:choose>
		  			</td>
		  			<td>
		  				${item.remark}
		  			</td>
		  		</tr>
	  		</c:forEach>
	  	</tbody>
	  	<script>
	  		$(function(){
	  			$("#fcode").html("${financbasecode}");
	  			$("#returntimes").html("${returntimes}");
	  		})
	  	</script>
	  	<tfoot>
	  		<tr>
	  			<th>小计:</th>
	  			<th></th>
	  			<th></th>
	  			<th></th>
	  			<th></th>	
	  			<th>${hjbj }</th>
	  			<th>${hjlx }</th>
	  			<th>${hjfj }</th>
	  			<th>${hjfee1 }</th>
	  			<th>${hjfj1 }</th>
	  			<th>${hjfee2 }</th>
	  			<th>${hjfj2 }</th>
	  		</tr>
	  		<tr>
	  			<th></th>
	  			<th></th>
	  			<th></th>
	  			<th></th>
	  			<th></th>
	  			<th></th>
	  			<th colspan="7">

	  				<c:if test="${state!=0}">
	  					实还总额:<fmt:formatNumber value="${hjbj+hjlx+hjfj+hjfee1+hjfj1+hjfee2+hjfj2}" pattern="#0.00"/>
	  				</c:if>
	  				<c:if test="${state==0}">
	  					应还总额<fmt:formatNumber value="${hjbj+hjlx+hjfj+hjfee1+hjfj1+hjfee2+hjfj2}" pattern="#0.00"/>
	  				</c:if>
	  			</th>
	  		</tr>
	  	</tfoot>
	  </table>
	  <div style="float:right;margin-right:30px;font-size:13px;">打印时间:<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
	 </div>
  </body>
</html>
