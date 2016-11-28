<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>融资项目列表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<style>
		.ui-button-text{
			line-height:30px;
			padding:10px 3px 5px 3px;
		}
		.ui-helper-hidden-accessible{
			display:none;
		}
	</style>
   <%@ include file="/common/import.jsp" %>
   <script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
   <script type="text/javascript" src="/back/four.jsp"></script>
   <script>
			$(function(){
				
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
			    $("#radio2").click(function(){
			    	var startDate = $("#startDate").val();
			    	var endDate = $("#endDate").val();
			    	var selectby = $("#selectby").val();
			    	window.location.href="/sheets/finance!financeList?startDate="+startDate+"&endDate="+endDate+"&selectby="+selectby;
			    });
			   
			     $(".table_solid").tableStyleUI(); 
			});
		</script>
  </head>
  <body>
  	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" style="height:30px;margin-bottom:10px;">
  	<form action="" id="form1">
  		<div id="radio" style="float:left">
			<input type="radio" id="radio2" name="watchtype" /><label for="radio2" >按融资项目</label>
			<input type="radio" id="radio1" name="watchtype" checked="checked"/><label for="radio1">按融资方</label>
		</div>
		<div style="float:left;margin-left:20px;font-size:15px;">
		按<s:select list="#{'签约':'签约','挂单通过':'挂单','撤单':'撤单'}" name="selectby" id="selectby" value="#request.selectby" cssStyle="padding:3px"></s:select>时间
  		<input type="text" id="startDate" name="startDate" style="width:80px;padding:3px;" id="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>"/>
  		&nbsp;到&nbsp;
  		<input type="text" name="endDate" style="width:80px;padding:3px;" id="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>"/>
  		融资方会员号 <input type="text" name=searchusername value="${searchusername}" style="padding:3px;"/>
  		<button class="ui-state-default" id="seachButton">查找</button>
  		</div>
  	</form>
  	</div>
<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		  <thead>
		  	<tr class="ui-widget-header ">
		  		<th width="10"></th>
		  		<th>融资方会员</th>
		  		<th>融资方姓名</th>
		  		<th>融资项目数量</th>
		  		<th>总融资额</th>
		  		<th></th>
		  	</tr>
		  </thead>
		  <tbody class="table_solid">
	  		<c:forEach items="${resultList}" var="item">
		  		<tr>
		  			<c:set value="${allcount + 1}" var="allcount"/>
		  			<td>${allcount }</td>
		  			<td>${item.financerusername}</td>
		  			<td>
		  				<script>document.write(name("${item.financerrealname}"));</script>
		  			</td>
		  			<td>${item.fcount}</td>
		  			<td>${item.currenyamount}</td>
		  			<c:set value="${allmoney + item.currenyamount}" var="allmoney"/>
		  			<td><a href="javascript:void(0)" onclick="window.location.href='/sheets/finance!financelistdetail?startDate=<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>&endDate=<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>&username=${item.financerusername}&searchusername=${searchusername}&selectby=${selectby }'">查看明细</a></td>
		  		</tr>
	  		</c:forEach>
	  		<tr>
	  			<td colspan="2"><h3 style="margin:5px 5px 5px 0;padding:0">总计</h3></td>
	  			<td></td>
	  			<td></td>
	  			<td><h3 style="margin:5px 5px 5px 0;padding:0">${allmoney }</h3></td>
	  			<td></td>
	  		</tr>
	  	</tbody>
  </body>
</html>
