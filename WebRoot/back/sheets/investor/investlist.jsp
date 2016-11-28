<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>会员投资统计</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <%@ include file="/common/import.jsp" %>
   <script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
   <script type="text/javascript" src="/back/four.jsp"></script>
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
			    $("#radio2").click(function(){
			    	var startDate = $("#startDate").val();
			    	var endDate = $("#endDate").val();
			    	var type = $("#type").val();
			    	//alert(type);
			    	window.location.href="/sheets/investor!financingList?startDate="+startDate+"&endDate="+endDate+"&type="+type;
			    });
			    
			    $("#mingxi").click(function(){
			    	var startDate = $("#startDate").val();
                    var stDate = new Date(Date.parse(startDate));
                    var endDate = $("#endDate").val();
                    var edDate = new Date(Date.parse(endDate));
                    var type = $("#type").val();
                    if(edDate < stDate){
                        alert("查询失败！开始日期不能小于结束日期！");
                        return false;
                    }
                    stDate.setMonth(stDate.getMonth()+1);
                    if(edDate <= stDate){
                    	alert(2);
                    	//window.location.href="/sheets/investor!investorDetailList?startDate="+startDate+"&endDate="+endDate+"&type="+type+"";
                    	self.location="/sheets/investor!investorDetailList?startDate="+startDate+"&endDate="+endDate+"&type="+type+""; 
                    }else{
                    	alert("查询失败!明细查询一次不能超过1个月！");
                    }
                    //alert(type);
                    
                    //sheets/investor!investorDetailList?startDate=<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>&endDate=<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>&username=${searchusername }&type=${type}&financingcode="
			    });
			    
			    $("#excelImg").click(function(){
			    	var startDate = $("#startDate").val();
                    var endDate = $("#endDate").val();
                    var type = $("#type").val();
                    var searchusername= $("#searchusername").val();
                    var searchjinbanren = $("#searchjinbanren").val();
			    	window.location.href="/sheets/investor!investListEx?startDate="+startDate+"&endDate="+endDate+"&type="+type+"&searchusername="+searchusername+"&searchjinbanren="+searchjinbanren;
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
			<input type="radio" id="radio1" name="watchtype" checked="checked" /><label for="radio1">按投资人</label>
			<input type="radio" id="radio2" name="watchtype"/><label for="radio2" >按融资项目</label>
		</div>
		<div style="float:left;margin-left:20px;">
  			<!-- 未动态设置Select的默认值 -->

  			<select type="select" name="type" id="type" value = "1">
   				<option value="1" >签约日期：</option>
   				<option value="0" >投标日期：</option>
   				
   			</select> <label for="startDate"></label>
   			<input type="text" id="startDate" name="startDate" style="width:80px;" id="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>"/>&nbsp;到&nbsp;<input type="text" name="endDate" style="width:80px;" id="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>"/>
  		会员帐号 <input type="text" name="searchusername" id="searchusername" style="width:90px;" value="${searchusername}">
  		会员介绍人 <input type="text" name="searchjinbanren" id="searchjinbanren" style="width:90px;" value="${searchjinbanren}">
  		<button class="ui-state-default" id="seachButton">查找</button>
  		<input type = "hidden" id = "excel" name = "excel" value="0" />
  		<input type = "hidden" id = "detail" name = "detail" value="0" />
  		<a style="color:red;" id = "excelImg" name="excelImg" href="javascript:;" title="结果导出EXCEL"><img src="/Static/images/excel.gif"></a>
  		&nbsp;&nbsp;
  		 
  		 
  		 <!--
  		<input type="button" class="ui-state-default" id="mingxi" value="查看明细" />
       
                 
                &nbsp;&nbsp;   
       <a href="/sheets/investor!investDetailListImport?startDate=<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>&endDate=<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>&searchusername=${searchusername }" >下载明细到excel
                </a>
                 -->
  		</div> 
  	</form>
  	</div>
  	<div class="dataList ui-widget">
	  <table  class="ui-widget ui-widget-content">
		  <thead >
		  	<tr class="ui-widget-header ">
		  		<th>会员帐号</th>
		  		<th>会员名称</th>
		  		<th>所属机构</th>
		  		<th>总投标额</th>
		  		<th>投标次数</th>
		  		<th>会员介绍人</th>
		  		<th></th>
		  	</tr>
		  </thead>
		  <tbody class="table_solid">
	  		<c:forEach items="${resultList}" var="item">
		  		<tr>
		  			<td>${item.investorname}</td>
		  			<td>
		  				<script>document.write(name("${item.investorrealname}"));</script>
		  			</td>
		  			<td>${item.investor_orgno}</td>
		  			<td>${item.money}</td>
		  			<c:set value="${allmoney + item.money}" var="allmoney"/>
		  			<td>${item.count}</td>
		  			<c:set value="${allcount + item.count}" var="allcount"/>
		  			<td>${item.jingbanren}</td>
		  			<td><a href="javascript:void(0)" onclick="window.location.href='/sheets/investor!investorDetailList?startDate=<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>&endDate=<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>&username=${item.investorname}&searchusername=${searchusername}&type=${type}'">查看明细</a></td>
		  		</tr>
	  		</c:forEach>
	  		<tr>
	  			<td>总计</td>
	  			<td></td>
	  			<td></td>
	  			<td>${allmoney }</td>
	  			<td>${allcount}</td>
	  			<td></td>
	  		</tr>
	  	</tbody>
	  </table>
	 </div>
  </body>
</html>
