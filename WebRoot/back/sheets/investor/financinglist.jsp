<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>投资人列表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
   <%@ include file="/common/import.jsp" %>
   <script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
   <script>
			$(function(){
				$("#type").val(${type }); 
				
				$("#seachButton").click(function() {
					$("#form1").submit();
				}); 
				$("#startDate").datepicker({
					numberOfMonths: 2,
			        dateFormat: "yy-mm-dd"
			    });
				$("#endDate").datepicker({
					numberOfMonths: 2,
			        dateFormat: "yy-mm-dd"
			    });
			    $("#ui-datepicker-div").css("display","none");
			    $( "#radio" ).buttonset();
			    $("#radio1").click(function(){
			    	var startDate = $("#startDate").val();
			    	var endDate = $("#endDate").val();
			    	var type = $("#type").val();
			    	//alert(type);
			    	window.location.href="/sheets/investor!investList?startDate="+startDate+"&endDate="+endDate+"&type="+type;
			    });
			   
			     $(".table_solid").tableStyleUI(); 
			});
		</script>
		<style>
			.ui-helper-hidden-accessible{
			display:none;
		}
		</style>
  </head>
  <body>
  	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
  	<form action="" id="form1">
  		<div id="radio" style="float:left">
			<input type="radio" id="radio1" name="watchtype" /><label for="radio1">按投资人</label>
			<input type="radio" id="radio2" name="watchtype" checked="checked" /><label for="radio2" >按融资项目</label>
		</div>
		<div style="float:left;margin-left:20px;">
		<script type="text/javascript">
			$("#tpye").value = "1";
		</script>
  		<select type="select" name="type" id="type" value="1" >
  				<option value="1" >签约日期：</option>
   				<option value="0" >投标日期：</option>
   				
   			</select> <label for="startDate"></label><input type="text" id="startDate" name="startDate" style="width:80px;" id="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>"/>&nbsp;到&nbsp;<input type="text" name="endDate" style="width:80px;" id="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>"/>
  		项目编号 <input type="text" name=searchfinancingcode value="${searchfinancingcode}"/>
  		<button class="ui-state-default" id="seachButton">查找</button>
  		</div>
  	</form>
  	</div>
<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		  <thead>
		  	<tr class="ui-widget-header ">
		  		<th>项目编号</th>
		  		<th>融资项目名称</th>
		  		<th>融资期限</th>
		  		<th>还款方式</th>
		  		<th>当前融资额</th>
		  		<th>投标额</th>
		  		<th>投标人次</th>
		  		<th></th>
		  	</tr>
		  </thead>
		  <tbody class="table_solid">
	  		<c:forEach items="${resultList}" var="item">
		  		<tr>
		  			<td>${item.financbasecode}</td>
		  			<td>${item.fshortname}</td>
		  			<td>
		  			<c:choose>
                        <c:when test="${item.interestday=='0'}">${item.businesstype}个月</c:when>
                        <c:otherwise>${item.interestday}天</c:otherwise>
                    </c:choose>
		  			</td>
		  			<td>${item.returnpattern}</td>
		  			<td>${item.currenyamount}</td>
		  			<td>${item.money}</td>
		  			<c:set value="${allmoney + item.money}" var="allmoney"/>
		  			<td>${item.count}</td>
		  			<c:set value="${allcount + item.count}" var="allcount"/>
		  			<td><a href="javascript:void(0)" onclick="window.location.href='/sheets/investor!financingDetailList?startDate=<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>&endDate=<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>&financingcode=${item.financbasecode}&searchfinancingcode=${searchfinancingcode}&type=${type}'">查看明细</a></td>
		  		</tr>
	  		</c:forEach>
	  		<tr>
	  			<td>总计</td>
	  			<td></td>
	  			<td></td>
	  			<td></td>
	  			<td></td>
	  			<td>${allmoney }</td>
	  			<td>${allcount }</td>
	  			<td></td>
	  		</tr>
	  	</tbody>
  </body>
</html>
