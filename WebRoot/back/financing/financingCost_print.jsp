
<%@page language="java" import="java.util.*,com.kmfex.model.*,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/common/taglib.jsp"%>
<script type="text/javascript" src="/back/four.jsp"></script>
<%
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String date = dateFormat.format(new Date()); 
%>
<html>
	<head> 
		<link skin="skin" href="/Static/css/themes/flick/jquery-ui-1.8.21.custom.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="/Static/js/jquery.multiselect-1.12/jquery.multiselect.css" />
		<link rel="stylesheet" href="/Static/js/jquery.chromatable-1.3.0/css/style.css" type="text/css" />
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-ui-1.8.17.custom.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery.chromatable-1.3.0/jquery.chromatable.js"></script>
		<script type="text/javascript" src="/Static/js/jquery.multiselect-1.12/jquery.multiselect.mini.js"></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<script>
		
		function doprint(){
			$("#toolbar").hide();
			print();
			$("#toolbar").show();
			
		}
		$(function(){ 
			$("select").multiselect({
				header: false,
				classes:"padding",
				selectedText:"已选择 # 项",
				click: function(event, ui){
	      			var target = ui.value;
	      			if(ui.checked){
	      				$("#"+target).show();
	      			}else{
	      				$("#"+target).hide()
	      			}
   				}
   			}); 
   			
   			
		});
		</script>
		<style>
body {
	font-size: 13px;
	padding: 0 3px;
	margin: 0;
}

.l1 {
	text-align: center;
}

.space {
	padding-left: 50px;
}

.title {
	font-size: 13.5px;
	font-weight: bold
}

.padding {
	padding: 6px;
}
</style>

	</head>
	<body>
		<div style="position: fixed; top: 0px; left: 0px; width: 100%; background-color: #F2F2F2; border-bottom: 1px solid #DCDCDC;" id="toolbar">
			<input type="button" value="打印" onclick="doprint()" style="padding: 6px;" /> 
		</div>
		<div style="margin: 40px auto; font-weight: bold;">
			<p style="text-align: center; margin: 20px auto;">
			<div style="float: left; margin-left: -20px;">
				<img width="80" height="80" style="margin: 2px; margin-left: 20px;" src="/Static/images/logo.png">
			</div>
			<span style="text-align: center;">
				<h2 align="center">昆投互联网金融交易</h2>
				<h1 align="center">融资项目费用(${cost.financingBase.createBy.org.shortName}&nbsp;${cost.financingBase.createBy.org.showCoding})</h1>
			</span>
			</p>
			<p style="line-height: 60px;">
			<div style="float: right; margin: 5px;">
				成交日期：<fmt:formatDate value="${cost.financingBase.qianyueDate }" pattern="yyyy-MM-dd"/></div>
			<br />
			</p>
			<div>
				<span class="title">融资项目：</span><span class="value">${cost.financingBase.code}</span>
				<span class="space"></span>
				<span class="title">贷款期限：</span>
				<span class="value">
				<c:if test="${(cost.financingBase.interestDay)!= 0}">按日计息(${cost.financingBase.interestDay}天)</c:if>
				<c:if test="${(cost.financingBase.interestDay)== 0}">${cost.financingBase.businessType.term}个月</c:if> 
				[<fmt:formatDate value="${cost.financingBase.qianyueDate}" pattern="yyyy-MM-dd" />至<fmt:formatDate value="${cost.financingBase.daoqiDate}" pattern="yyyy-MM-dd" />]</span>
				<span class="space"></span>
				<span class="title">还款方式：</span><span class="value">${cost.financingBase.businessType.returnPattern} </span> 
								<span class="space"></span> 
				<span class="title">年利率：</span><span class="value">${cost.financingBase.rate}% </span>
			</div>
			<table style="font-size: 13px; font-weight: bold">
				<thead>
					<tr>
						<th>
							项目简称
						</th>
						<th>
							融资方户名
						</th>
						<th>
							帐号（交易）
						</th>
						<th>
							帐号（银行）
						</th>
						<th>
							融资总额
						</th> 
						<th>
							融资结余
						</th> 
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							${cost.financingBase.shortName }
						</td>
						<td>
							<c:choose>
								<c:when test="${cost.financingBase.financier.eName == null }">
									<script>document.write(name("${cost.financingBase.financier.pName}"));</script>
								</c:when>
								<c:otherwise>
									<script>document.write(name("${cost.financingBase.financier.eName}"));</script>
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							${cost.financingBase.financier.user.username}
						</td>
						<td>
							<script>document.write(bankcard("${cost.financingBase.financier.bankAccount}"));</script>
						</td>
						<td>
							<fmt:formatNumber value="${cost.financingBase.currenyAmount}" pattern="#0.00" />
						</td> 
						<td>
							<fmt:formatNumber value="${cost.realAmount}" pattern="#0.00" />
						</td> 
						
					</tr>
				</tbody>
			</table>
			<div> 
			    <c:choose>
						<c:when test="${fn:startsWith(cost.financingBase.code, 'X')}">
					          <c:if test="${cost.fee1>0}">   
					              风险管理费:${cost.fee1_tariff==0?"":"[按月]"}[${cost.fee1_bl}%]:<fmt:formatNumber value='${cost.fee1}' pattern="#,###,##0.00" />
					          </c:if>
					          
					          <c:if test="${cost.fee2>0}">    
					             <span class="space"></span>融资服务费:${cost.fee2_tariff==0?"":"[按月]"}[${cost.fee2_bl}%]:<fmt:formatNumber value='${cost.fee2}' pattern="#,##0.00" /><br />
					          </c:if>
					          <c:if test="${cost.bzj>0}">  
					            <span class="space"></span> 还款保证金:<fmt:formatNumber value='${cost.bzj}' pattern="#,##0.00"/> 
					          </c:if>
					          <c:if test="${cost.fee3>0}">  
					             担保费:${cost.fee3_tariff==0?"":"[按月]"}[${cost.fee3_bl}%]:<fmt:formatNumber value='${cost.fee3}' pattern="#,##0.00" />
						      </c:if>
						</c:when>
						<c:otherwise>
                     		<c:if test="${cost.fxglf>0}">  
                     		  风险管理费:${cost.fxglf_tariff==0?"":"[按月]"}[${cost.fxglf_bl}%]:<fmt:formatNumber value='${cost.fxglf}' pattern="#,##0.00" />
                     		</c:if>                     		
                         	<c:if test="${cost.rzfwf>0}">  
                         	  <span class="space"></span> 融资服务费:${cost.rzfwf_tariff==0?"":"[按月]"}[${cost.rzfwf_bl}%]:<fmt:formatNumber value='${cost.rzfwf}' pattern="#,##0.00" /><br /> 
	            			</c:if>
	            			<c:if test="${cost.bzj>0}">  
                     		  <span class="space"></span>还款保证金:<fmt:formatNumber value='${cost.bzj}' pattern="#,##0.00"/>
                         	</c:if>
	            			<c:if test="${cost.dbf>0}">  
	            			   担保费:${cost.dbf_tariff==0?"":"[按月]"}[${cost.dbf_bl}%]:<fmt:formatNumber value='${cost.dbf}' pattern="#,##0.00" />
						    </c:if>
						</c:otherwise> 
					</c:choose> 
					
					<c:if test="${cost.fee7>0}">  
					    <span class="space"></span>席位费:<fmt:formatNumber value='${cost.fee7}' pattern="#,##0.00"/>
					</c:if>
					
				   <br/>
				 备注:<span style='color:red;'>${cost.note}</span>  
			</div>

				
				<div style="float:right">
					<div style="float:left;width:200px;">介绍人:</div>
					<div style="float:left;width:200px;">复核人:</div>
					<div style="float:left;">打印时间:<%=date%></div>
				</div>
		</div>
	</body>
</html>
