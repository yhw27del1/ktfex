<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<html>
	<head>
<script type="text/javascript" src="/Static/js/autoheight.js"></script> 
<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
		<script type="text/javascript">   
		
		$(function(){
			$(".table_solid").tableStyleUI();
				$("#form1").validate({
			        rules: { 
			          "day":{ required:true} 
			        },  
			        messages: {      
			          "day":{ required:"请选择还款日"} 
			        }    
				});  
				$('.contract').click(function(){
					window.showModalDialog("/back/investBaseAction!agreement_ui2?invest_record_id="+$(this).attr("id"), null, "dialogWidth:1000px;dialogHeight:auto;status:no;help:yes;resizable:no;");
					$("input.ui-state-default").trigger('click');
				});
			})
		   $(document).ready(function(){ 
		            $("#ui-datepicker-div").css({'display':'none'});
					$("#submitBtn").click(function() {
						 window.location.href = "/back/financingBaseAction!list"; 
					});  
					$("#qianyuesubmitBtn").click(function() {
					 	 if ($("#startDate").val() == "") { 
		                    messageTip("投标开始不能为空!");
		                    return false;
		                 }
						 if ($("#endDate").val() == "") { 
		                    messageTip("投标截止不能为空!");
		                    return false;
		                 }
						 window.location.href = "/back/financingBaseAction!qianyue?id=${id}"; 
					}); 
					 
					//setTitle2("融资项目签约"); //重新设置切换tab的标题
				});
				 function sub_(){
					 $('#form1').submit();
				}
		</script>
		<title>融资项目签约</title>  
		<style type="text/css"> 
			.s_table td{background-color:#e3e3e3}
		</style>
	</head>

	<body class="ui-widget-header" style="color: black"> 
	<form action='/back/financingBaseAction!qianyue' id="form1">
     <input type='hidden' class='autoheight' value="auto" /> 
     <input type="hidden" name="id" value="${id}">  
	 			<table style="width:70%" class="s_table"> 
			    <tr  > 
					<td style="padding-left:30px;"  style="color:black">
						${financingBase.shortName } 
							&nbsp;&nbsp;&nbsp;进度:<fmt:formatNumber value='${(financingBase.currenyAmount/financingBase.maxAmount)*100}'    type="currency" currencySymbol=""/>%&nbsp;&nbsp;&nbsp;&nbsp;
 					  
					</td>
				</tr>
				</table>
			  <table  style="width:70%;font-size:10p;" class="s_table">  
				<tr>
				   <td align="right" class="ui-widget-header ui-corner-all" style="width:18%">项目编号：</td> <td> 
					    ${financingBase.code}
                    </td>
                     <td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">融资方：</td> <td >
						<c:if test="${(financingBase.financier)!= null}">
							 <a href="#"   class="tooltip" title="${financingBase.financier.eName}"> ${financingBase.financier.eName}</a>
						</c:if>	 
					    <c:if test="${(financingBase.financier)== null}">
								  暂无
					    </c:if></td>
				</tr>
				<!--  <tr>
				   <td align="right" class="ui-widget-header ui-corner-all" style="width:18%">融资方行业：</td> <td>
				        <c:if test="${(financingBase.financier)!= null}">
							        <c:if test="${(financingBase.financier.industry)!= null}">
							                 ${financingBase.financier.industry.name}
									</c:if>	 
								    <c:if test="${(financingBase.financier.industry)== null}">
											  暂无
								    </c:if> 
						</c:if>	 
					    <c:if test="${(financingBase.financier)== null}">
								  暂无
					    </c:if>
                    </td>
                    <td align="right" class="ui-widget-header ui-corner-all" style="width:21%">融资方公司性质：</td> <td >
				        <c:if test="${(financingBase.financier)!= null}">
							        <c:if test="${(financingBase.financier.companyProperty)!= null}">
							                 ${financingBase.financier.companyProperty.name}
									</c:if>	 
								    <c:if test="${(financingBase.financier.companyProperty)== null}">
											  暂无
								    </c:if> 
						</c:if>	 
					    <c:if test="${(financingBase.financier)== null}">
								  暂无
					    </c:if>
				    </td> 
				</tr>-->
				<tr>
				   <td align="right" class="ui-widget-header ui-corner-all" style="width:18%">还款方式：</td> <td>
						<c:if test="${(financingBase.businessType)!= null}">
							${financingBase.businessType.returnPattern}
						</c:if>	 
					    <c:if test="${(financingBase.businessType)== null}">
								  暂无
					    </c:if>
                    </td>
                   <td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all"> 融资期限：</td> <td >
					   <c:if test="${(financingBase.businessType)!= null}">
					   	  		<c:if test="${(financingBase.interestDay)!= 0}">按日计息(${financingBase.interestDay}天)</c:if>
			                    <c:if test="${(financingBase.interestDay)== 0}">${financingBase.businessType.term}月</c:if> 
 						</c:if>	 
					    <c:if test="${(financingBase.businessType)== null}">
								  暂无
					    </c:if>
					</td> 

				</tr>
				<tr>
                    <td align="right" class="ui-widget-header ui-corner-all" style="width:18%">融资方地区：</td> <td >
				        <c:if test="${(financingBase.financier)!= null}"> 
						    ${financingBase.financier.provinceName}${financingBase.financier.cityName}  
						</c:if>	 
					    <c:if test="${(financingBase.financier)== null}">
								  暂无
					    </c:if>
				    </td> 
				    					
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">总融资额：</td><td ><fmt:formatNumber value='${financingBase.maxAmount }'  type="currency" currencySymbol=""/>元</td>
				</tr>
				<tr> 
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">已融资额：</td> <td ><fmt:formatNumber value='${financingBase.currenyAmount}'  type="currency" currencySymbol=""/>元</td>
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">可融资额：</td><td ><fmt:formatNumber value='${financingBase.curCanInvest }'  type="currency" currencySymbol=""/>元</td>
 				</tr>
				<tr> 
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">投标开始：</td> 
					<td >
					 <fmt:formatDate value="${financingBase.startDate}" pattern="yyyy-MM-dd"/> 
 					</td> 
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">投标截止：</td> 
					<td >  
					    <fmt:formatDate value="${financingBase.endDate }" pattern="yyyy-MM-dd"/> 
 					</td>
				</tr>
                <tr>
                    <td align="right" class="ui-widget-header ui-corner-all" >担保方：</td> <td >
				        <c:if test="${(financingBase.guarantee)!= null}">
							   <a href="#"   class="tooltip" title="${financingBase.guarantee.eName}"> ${financingBase.guarantee.eName}</a>
						</c:if>	 
					    <c:if test="${(financingBase.guarantee)== null}">
								  暂无
					    </c:if>
				    </td> 
					<td  align="right"  class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" >年利率：</td>
					<td>${financingBase.rate }%</td>
				</tr>  	
				 <tr>
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" >投标人数：</td> <td >${financingBase.haveInvestNum}</td>
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" >状态：</td><td >	
			  	            <c:if test="${financingBase.state=='0'}"><span style="color:#4169E1;">待审核</span></c:if>
					        <c:if test="${financingBase.state=='1'}"><span style="color:green;">待挂单</span></c:if>
							<c:if test="${financingBase.state=='2'}"><span style="color:red;">投标中 </span></c:if>
							<c:if test="${financingBase.state=='3'}"><span style="color:#4169E1;">部分投标</span></c:if>
							<c:if test="${financingBase.state=='4'}"><span style="color:green;">已满标</span></c:if>
	 					 <c:if test="${financingBase.state=='5'}"><span style="color:red;">已确认 </span></c:if>	
				     	<c:if test="${financingBase.state=='6'}"><span style="color:green;">已确认</span></c:if>
				     	<c:if test="${financingBase.state=='7'}"><span style="color:red;">已签约 </span></c:if>
				</tr>
				<tr>
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">风险保障：</td>
					<td > 
					  ${financingBase.fxbzStateName}
					</td>
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">&nbsp;&nbsp;&nbsp;</td>
					<td ></td>
				</tr> 
				 <tr>
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all"><span style="color: red">(*必填)</span>还款日：</td>
					<td colspan="3">${showMessage} 
					    <select name="day">
					       <option value="">选择还款日</option>
					       <option value="1">1日</option>
					       <option value="2">2日</option>
					       <option value="3">3日</option>
					       <option value="4">4日</option>
					       <option value="5">5日</option>
					       <option value="6">6日</option>
					       <option value="7">7日</option>
					       <option value="8">8日</option>
					       <option value="9">9日</option>
					       <option value="10">10日</option>
					       <option value="11">11日</option>
					       <option value="12">12日</option>
					       <option value="13">13日</option>
					       <option value="14">14日</option>
					       <option value="15">15日</option>
					       <option value="16">16日</option>
					       <option value="17">17日</option>
					       <option value="18">18日</option>
					       <option value="19">19日</option>
					       <option value="20">20日</option> 
					    </select>
					</td> 
				</tr> 
			</table>
			<br />
			<table class="ui-widget ui-widget-content" width="100%">
				<thead>
					<tr class="ui-widget-header ">
						
						<th>
							投标人
						</th>
						<th>
							投标金额(￥)
						</th>
						<th>
							投标日期
						</th>
						<th>
							投标合同编号及状态
						</th>
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:forEach items="${investRecords}" var="entry">
						<tr>
							<td>
								${entry.investor.pName}
							</td>
							<td>
								<fmt:formatNumber value='${entry.investAmount}'  type="currency" currencySymbol="" />
							</td>
							<td>
								<fmt:formatDate value="${entry.createDate}" pattern="yyyy-MM-dd" />
							</td>
							<td>
								<a class="contract" href="javascript:void(0);" id="${entry.id}">${entry.contract.contract_numbers}</a>/<c:if test="${entry.contract.investor_make_sure==null}">未确认</c:if><c:if test="${entry.contract.investor_make_sure!=null}">已确认</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			本金总额：<fmt:formatNumber value='${principal_allah}'  type="currency" currencySymbol=""/>元，利息总额：<fmt:formatNumber value='${interest_allah}'  type="currency" currencySymbol=""/>
			<c:if test="${financingBase.businessType.returnTimes>1}">
			<br />按月等额本息还款(本金)：<fmt:formatNumber value='${principal_allah_monthly}'  type="currency" currencySymbol=""/>元，
				     按月等额本息还款(利息)：<fmt:formatNumber value='${interest_allah_monthly}'  type="currency" currencySymbol=""/>元，
				     每月应还金额：<fmt:formatNumber value='${repayment_amount_monthly_allah}'  type="currency" currencySymbol=""/>元
			</c:if>
			<br /><br />
			<table style="width:100%;" class="s_table">  
				<tr>
					<td colspan="4" style="padding-left: 200px; width: 100%;" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
						<c:choose>
							<c:when test="${financingBase.state=='6' && creditor}">
								<button class="ui-state-default" onclick="sub_();return false;">
									签约
								</button>
							</c:when>
							<c:otherwise>
								融资方本人方可进行签约
							</c:otherwise>
						</c:choose>
					</td>
				</tr>  
			</table> 
			</form>
	</body>
</html>
