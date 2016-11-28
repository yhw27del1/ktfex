<%@ page language="java" import="java.util.*"   pageEncoding="utf-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript">
$(function(){
	$("#startDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
	$("#endDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
    $("#ui-datepicker-div").css({'display':'none'});
	$(".table_solid").tableStyleUI();
	
	      $("#printButton").click(function(){    	
		    var params = "";
			if(window.location.href.indexOf("?")>0){
			    params = window.location.href.substring(window.location.href.indexOf("?"),window.location.href.length);
			}
			window.open("/back/accountDeal/accountDealAction!cashFlowPrint"+params);
			return false;
		  });  
	      $("#exButton").click(function(){    	
		    var params = "";
			if(window.location.href.indexOf("?")>0){
			    params = window.location.href.substring(window.location.href.indexOf("?"),window.location.href.length);
			}
			window.open("/back/accountDeal/accountDealAction!cashFlowEx"+params+"&excelFlag=1");  
			return false;
		  });  
		  
		  $("#queryButton").click(function(){
		    $("#form1").attr("action","/back/accountDeal/accountDealAction!cashFlow");  
		    $("#form1").submit(); 
		  });
});
function show(url){
	window.showModalDialog(url, null, "dialogWidth:auto;dialogHeight:auto;status:no;help:yes;resizable:no;");
} 
</script>
<style type="text/css">
.s_table td {
	background-color: #e3e3e3     
}
.agreement{cursor:pointer;}
.agreement:HOVER{text-decoration: underline;}
</style>
<body >
	<input type='hidden' class='autoheight' value="auto" />
	<form action="" id='form1'> 
	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" style="height:30px;">
		
		<div style="float:left;margin-left:10px;">
			开始日期&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>" id="startDate" style="width:80px;padding:3px;"/>&nbsp;
			结束日期&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>" id="endDate" style="width:80px;padding:3px;"/>
			<input class="ui-state-default" style="height:28px;cursor:pointer" type="submit" id='queryButton'  value="查询">
			 <c:if test="${!empty pageView.result}">
			&nbsp;&nbsp;
			<!-- 
			<input class="ui-state-default" style="height:28px;cursor:pointer" id='printButton' type="submit" value="打印">
		     -->
		    &nbsp;&nbsp;<input class="ui-state-default" style="height:28px;cursor:pointer" id='exButton' type="submit" value="导出">
		    </c:if>
		</div>
	</div>
	<input type="hidden" name="page" value="1" /> 
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					    <th colspan="7" align="center" valign="middle" style="text-align: center;">
							昆投互联网金融交易-招商银行（投、融资充值、提现）日报表
						</th>
				</tr>
				<tr class="ui-widget-header ">
						 <th style="text-align:left" colspan="4">
						 <c:if test="${!empty startDate } && ${!empty endDate } ">
						 会计期间:${startDate }到${endDate }
						 </c:if>
						 <c:if test="${!empty startDate } && ${empty endDate } ">
						 会计期间:${startDate }至今
						 </c:if>
						 </th> 
					     <th style="text-align:left" colspan="3">开户行;招商银行（一般帐户）   帐号：871903469010608</th> 
				</tr>
				 <tr class="ui-widget-header ">
				    <th style="text-align:center">日期</th>
			  		<th style="text-align:center">投资充值</th> 
				    <th style="text-align:center">融资充值</th>
			  		<th style="text-align:center">投资提现</th> 
			  		<th style="text-align:center">融资提现</th> 
			  		<th style="text-align:center">财务结转</th> 
			  		<th style="text-align:center">交易帐户总余额</th> 
			  	</tr>
			</thead>
			<tbody class="table_solid">
			     <tr>
                            <td>
                                上月累计
                            </td>
                            <td class="xlsMoney"><input type="text"  size="10px" name="lmsumtzcz" value="<fmt:formatNumber groupingUsed="false" value="${lmsumtzcz}" maxIntegerDigits="20" maxFractionDigits="10"/>"/> </td> 
                            <td class="xlsMoney"><input type="text" size="10px"  name="lmsumrzcz" value="<fmt:formatNumber groupingUsed="false"  value="${lmsumrzcz}" maxIntegerDigits="20" maxFractionDigits="10"/>" /> </td> 
                            <td class="xlsMoney"><input type="text" size="10px" name="lmsumtztx" value="<fmt:formatNumber groupingUsed="false"  value="${lmsumtztx}" maxIntegerDigits="20" maxFractionDigits="10"/>" /> </td> 
                            <td class="xlsMoney"><input type="text" size="10px"  name="lmsumrztx" value="<fmt:formatNumber groupingUsed="false"  value="${lmsumrztx}" maxIntegerDigits="20" maxFractionDigits="10"/>" /> </td> 
                            <td class="xlsMoney"><input type="text" size="10px"  name="lmcwzj"value="<fmt:formatNumber groupingUsed="false"  value="${lmcwzj}" maxIntegerDigits="20" maxFractionDigits="10"/>" />  </td> 
                            <td class="xlsMoney"><input type="text" size="10px"  name="lmjyzhye" value="<fmt:formatNumber groupingUsed="false"  value="${lmjyzhye}" maxIntegerDigits="20" maxFractionDigits="10"/>"/> 
                            <c:set value="${sumyu_e + lmjyzhye}" var="sumyu_e" /></td> 
                   </tr>
				<c:forEach items="${pageView.result}" var="entry" varStatus="index">
						<tr>
					        <td>${entry.date_}  </td> 
					        <td>${entry.tzcz} <c:set value="${sumtzcz + entry.tzcz}"
                                        var="sumtzcz" /></td>
					        <td>${entry.rzcz} <c:set value="${sumrzcz + entry.rzcz}"
                                        var="sumrzcz" /></td>
					        <td>${entry.tztx} <c:set value="${sumtztx + entry.tztx}"
                                        var="sumtztx" /></td> 
					        <td>${entry.rztx} <c:set value="${sumrztx + entry.rztx}"
                                        var="sumrztx" /></td>  
					        <td>${entry.cwzj} <c:set value="${sumcwzj + entry.cwzj}"
                                        var="sumcwzj" /></td> 
					        <td> <c:set value="${sumyu_e + entry.yu_e}"
                                        var="sumyu_e" />${sumyu_e }</td>  
					     
						</tr>
					</c:forEach>
					     <tr>
							<td>
								合计
							</td>
							<td> ${sumtzcz}</td> 
							<td> ${sumrzcz}</td> 
							<td>	${sumtztx}</td> 
					        <td> ${sumrztx}</td> 
					        <td> ${sumcwzj }</td> 
					        <td> </td> 
				        </tr>
				        <tr>
                            <td>
                                累计
                            </td>
                            <td> ${sumtzcz+lmsumtzcz}</td> 
                            <td> ${sumrzcz+lmsumrzcz}</td> 
                            <td> ${sumtztx+lmsumtztx}</td> 
                            <td> ${sumrztx+lmsumrztx}</td> 
                            <td> ${sumcwzj+lmcwzj}</td> 
                            <td> </td> 
                        </tr>
				<tr>
				<% SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); %>
					<td colspan="7" align="right" style="text-align: center;">制表：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 复核： &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;    日期：<%=sdf.format(new Date()) %>
						</td>
				</tr>
				<tr>
                    <td colspan="7" align="right" style="text-align: center;color: red">备注：财务转结为系统外数据，系统中无法展示；交易账户总余额需先输入上月余额，再点查询进行查看；
                        </td>
                </tr>
			</tbody>
		</table>
	</div>
	</form>
	
</body>
<script>
setIframeHeight(100);
</script>