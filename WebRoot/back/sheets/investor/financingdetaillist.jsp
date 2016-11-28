<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>投资人详细投资情况</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
   <%@ include file="/common/import.jsp" %>
   <script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
   <script type="text/javascript" src="/back/four.jsp"></script>
   <script>
			$(function(){
				$(".table_solid").tableStyleUI(); 
				
			});
		</script>
  </head>
  <body>
  	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
  		<form>
	  		<a href="javascript:void(0)" onclick="window.location.href='/sheets/investor!financingList?startDate=<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" />&endDate=<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>&searchfinancingcode=${searchfinancingcode}&type=${type}'" style="text-decoration: none;font-weight: bold"><<返回</a>
	  		<input type="hidden" value="${searchfinancingcode}" name="searchfinancingcode"/>
	  		<input type="hidden" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" />" name="startDate"/>
	  		<input type="hidden" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>" name="endDate"/>
	  		<input type="hidden" value="${financingcode }" name="financingcode"/>
	  		&nbsp;&nbsp;&nbsp;&nbsp;会员帐号<input type="text" name="username" value="${username }"/><button class="ui-state-default" id="seachButton">查找</button>
	  		<div style="float:right;margin-right:30px;">
	  			投标日期：<span style="color:#E91313"><fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" /></span>&nbsp;到&nbsp;<span style="color:#E91313"><fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/></span>
	  		</div>
  		</form>
  	</div>
<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		  <thead>
		  	<tr class="ui-widget-header ">
		  		<th>会员帐号</th>
		  		<th>会员名称</th>
		  		<th>投标额</th>
		  		<th>投标日期</th>
		  		<th>签约日期</th>
		  		<th>融资项目名</th>
		  		<th>项目编号</th>
		  		
		  		<th>状态</th>
		  	</tr>
		  </thead>
		  <tbody class="table_solid">
	  		<c:forEach items="${resultList}" var="item">
		  		<tr>
		  			<td>${item.investorname}</td>
		  			<td>
		  				<script>document.write(name("${item.investorrealname}"));</script>
		  			</td>
		  			<td>${item.investamount}</td>
		  			<td>${item.createdate}</td>
		  			<td>${item.qianyuedate}</td>
		  			<td>${item.fshortname}</td>
		  			<td>${item.financbasecode}</td>
		  			<td>
		  				<c:if test="${item.state=='1'}">待签约</c:if>
		  				<c:if test="${item.state=='2'}">已签约</c:if>
		  				<c:if test="${item.state=='3'}">已撤单</c:if>
		  			</td>
		  		</tr>
	  		</c:forEach>
	  	</tbody>
	  </table>
	 </div>
  </body>
</html>
