<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript">
function doprint(){
	$("#myToolBar").hide();
	$("#toPrint").hide();
	print();
	$("#myToolBar").show();
	$("#toPrint").show();
}
$(function(){
	$(".table_solid").tableStyleUI();
	$("#startDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
	$("#endDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
    $("#ui-datepicker-div").css({'display':'none'});
    var userTypeVal = $("#userTypeHidden").val();
    if(userTypeVal){
    	//alert(userTypeVal);
    	$("#userTypeSelect option").attr("selected",false);
    	$("#userTypeSelect option[value='"+userTypeVal+"']").attr("selected",true);
    }
    var fxbzStateVal = $("#fxbzStateHidden").val();
    if(fxbzStateVal){
    	//alert(fxbzState);
    	$("#fxbzStateList option").attr("selected",false);
    	$("#fxbzStateList option[value='"+fxbzStateVal+"']").attr("selected",true);  
    }
});
</script>
<script type="text/javascript">
	function toURL(url) {
		window.location.href = url;
	}
	function del(id) {
		window.location.href = "/back/member/memberTypeAction!del?id=" + id;
	}
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="">
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				项目类型&nbsp;
				<input type="hidden" id="userTypeHidden" value="${userType}" />
				<select id="userTypeSelect" name="userType">
					<option value="all">全部</option>
					<option value="F">风险管理费</option>
					<option value="R">融资服务费</option>
					<option value="B">保证金</option>
					<option value="D">担保费</option>
					<!-- <option value="X">信息咨询费</option> -->
				</select> 
				风险保障:  
			  <input type="hidden" id="fxbzStateHidden" value="${fxbzState}" />
				<select id="fxbzStateList" name="fxbzState"> 
				   <option value="">全部</option>  					  
					<option value="10">本息担保</option>
					<option value="12">本金担保</option>					
					<option value="15">无担保</option>  
				</select>
				划款日期 从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;到&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
				<input type="submit" class="ui-state-default" value="查询" />
	</div>
	<div class="dataList ui-widget">
	<table>
		<thead>
			<th>会计日期：<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd" type="date" />至<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd" type="date" /></th>
		</thead>
		<tbody>
		<tr>
			<table class="ui-widget ui-widget-content">

             <!-- 全部开始 -->
          
			<thead>
				<tr class="ui-widget-header ">
					<th>
						签约日期
					</th>
					<th>
						项目编号
					</th>
					<th>
					          融资方
					</th>
					<th>
						交易账号
					</th>
					<th>
						银行账号
					</th>					
					<th>
						
							 <c:if test="${userType=='all'}">
										 项目类型:费用
						         </c:if>
						         <c:if test="${userType=='F'}"> 
								                     风险管理费 
						         </c:if>						         						         					         
						         <c:if test="${userType=='R'}"> 
							                                 融资服务费 
						         </c:if>
						         <c:if test="${userType=='D'}">
										 担保费 
						         </c:if>
						        <c:if test="${userType=='B'}"> 
								                      保证金 
						         </c:if>  
						         <!-- <c:if test="${userType=='X'}"> 
								                      信息咨询费 
						         </c:if> -->   
					</th>
					<!--  <th>
						融资项目期限
					</th>
					<th>
						年利率
					</th>-->
					<th>
						融资额
					</th>
					<th>
					           划款时间
					</th>
					
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${costs}" var="cost">
					<tr>
					    <td>
							<fmt:formatDate value="${cost.financingBase.qianyueDate}" type="date" />
						</td>
						<td>
							${cost.financingBase.code}
						</td>
						<td>
							<script>document.write(name("${cost.financingBase.financier.user.realname}"));</script>
						</td>
						<td>
							${cost.financingBase.financier.user.username}
						</td>
						<td>
							<script>document.write(bankcard("${cost.financingBase.financier.bankAccount}"));</script>
						</td>
						<td> 
							     <c:if test="${userType=='all'}">
							     
							      <c:choose>
					                  <c:when test="${fn:startsWith(cost.financingBase.code, 'X')}">
					                                                             风险管理费:<fmt:formatNumber value='${cost.fee1}' type="currency" currencySymbol=""/>
					                  </c:when>
					                  <c:otherwise>
					                                                             风险管理费:<fmt:formatNumber value='${cost.fxglf}' type="currency" currencySymbol=""/>;<br/>
					                                                             融资服务费:<fmt:formatNumber value='${cost.rzfwf}' type="currency" currencySymbol=""/>;<br/>
						                                                        担保费:<fmt:formatNumber value='${cost.dbf}' type="currency" currencySymbol=""/>;                                                    
				                                                                 保证金:<fmt:formatNumber value='${cost.bzj}' type="currency" currencySymbol=""/> 
					                </c:otherwise>
				                  </c:choose>   
						         </c:if>
						        
						         <c:if test="${userType=='F'}"> 
						           <c:choose>
						           	 <c:when test="${fn:startsWith(cost.financingBase.code, 'X')}">
				 
						              <fmt:formatNumber value='${cost.fee1}' type="currency" currencySymbol=""/>
						            </c:when>
						            <c:otherwise>
						               <fmt:formatNumber value='${cost.fxglf}' type="currency" currencySymbol=""/> 
						            </c:otherwise>
                                    </c:choose>					            								      
						         </c:if> 
						         <c:if test="${userType=='R'}"> 
							         <fmt:formatNumber value='${cost.rzfwf}' type="currency" currencySymbol=""/> 
						         </c:if>
						          <c:if test="${userType=='D'}">
										<fmt:formatNumber value='${cost.dbf}' type="currency" currencySymbol=""/>  
						         </c:if>
						        <c:if test="${userType=='B'}"> 
								     <fmt:formatNumber value='${cost.bzj}' type="currency" currencySymbol=""/> 
						         </c:if>
						         <!--  <c:if test="${userType=='X'}"> 
								    <fmt:formatNumber value='${cost.fee1}' type="currency" currencySymbol=""/>
						         </c:if> -->  
							 
							
						</td>
						<!--  <td>
							${cost.financingBase.businessType.term}个月
						</td>
						<td>
							${cost.financingBase.rate}%
						</td>-->
						<td>
							<fmt:formatNumber value='${cost.financingBase.currenyAmount }' type="currency" currencySymbol=""/>
						</td>
						<td>
						   <fmt:formatDate value="${cost.hkDate}" type="both" />
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td>
						<span style="font-size: 16px;font-weight: bold;">合计</span>
					</td> 
					<td>
						&nbsp;
					</td>
					<td>
						&nbsp;
					</td>
					<td>
						&nbsp;
					</td>
					<td style="text-align: right;">
						<span style="font-size: 16px;font-weight: bold;"><fmt:formatNumber value='${sum_charge}' type="currency" currencySymbol=""/></span>
					</td>
				
					<td>
					   <c:if test="${!empty costs}">
					  
							 <span style="font-size: 14px;font-weight: bold;">  <c:if test="${userType=='all'}">
							                                 风险管理费:<fmt:formatNumber value='${fxglfSum}' type="currency" currencySymbol=""/><br/>
							                                 融资服务费:<fmt:formatNumber value='${rzfwfSum}' type="currency" currencySymbol=""/><br/>
									         担保费:<fmt:formatNumber value='${dbfSum}' type="currency" currencySymbol=""/><br/> 								                     
								                     保证金:<fmt:formatNumber value='${bzjSum}' type="currency" currencySymbol=""/> 
								                     <!--   信息咨询费:<fmt:formatNumber value='${fee1Sum}' type="currency" currencySymbol=""/>-->
						         </c:if>
						         
						         <c:if test="${userType=='F'}"> 
								      <fmt:formatNumber value='${fxglfSum}' type="currency" currencySymbol=""/> 
						         </c:if> 
						         <c:if test="${userType=='R'}"> 
							         <fmt:formatNumber value='${rzfwfSum}' type="currency" currencySymbol=""/> 
						         </c:if>
						         <c:if test="${userType=='D'}">
										<fmt:formatNumber value='${dbfSum}' type="currency" currencySymbol=""/>  
						         </c:if>
						        <c:if test="${userType=='B'}"> 
								     <fmt:formatNumber value='${bzjSum}' type="currency" currencySymbol=""/> 
						         </c:if>
						        <!--   <c:if test="${userType=='X'}"> 
								    <fmt:formatNumber value='${fee1Sum}' type="currency" currencySymbol=""/>
						         </c:if> --> 
						    </c:if>  
						  </span>
						 
					</td>
				     <td>
						&nbsp;
					</td>
					<td>
						&nbsp;
					</td>
					<!--<td>
						&nbsp;
					</td>
					<td>
						&nbsp;
					</td>
				--></tr>
				<tr> 
					<td>
					   &nbsp;
					</td>
					<td>
					   &nbsp;
					</td>
					<td style="text-align: right;">
						<input type="button" value="打印" id="toPrint" onclick="doprint()">
					</td>
					<td>
					</td>
					<td style="text-align: right;">
						报表打印时间：
					</td>
					<td>
						<fmt:formatDate value="${showToday}" type="date" />
					</td>
					<td style="text-align: right;">
						经办员：
					</td>
					<td>
						${user.realname}
					</td>
				</tr>
			</tbody>   
			
		</table>
		</tr>
		</tbody>
	</table>
	</div>
	</form>
</body>
<script>
setIframeHeight(100);
</script>