<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<%@ page import="java.util.*,com.kmfex.MoneyFormat,com.kmfex.model.FinancingBase" %>
<html>
	<head>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<link rel="stylesheet" href="/Static/css/simpleTabs.css" type="text/css"/>
<script type="text/javascript" src="/Static/js/simpleTabs.js"></script>        
<script type="text/javascript" src="/back/four.jsp"></script>
<script type="text/javascript">  
 
   $(document).ready(function(){
	 //时间控件
	    $("#startDate").datepicker({
	        showOn: 'button',
	        buttonImageOnly: false,
	        //changeMonth: true,
	        //changeYear: true,
	        numberOfMonths: 2,
	        dateFormat: "yy-mm-dd",
	        minDate: +0 
	        //maxDate: "+1M"
	    });
	    
	    $("#endDate").datepicker({
	        showOn: 'button',
	        buttonImageOnly: false,
	        //changeMonth: true,
	        //changeYear: true,
	        numberOfMonths: 2,
	        dateFormat: "yy-mm-dd",
	        minDate: +0 
	    });
       $("#ui-datepicker-div").css({'display':'none'});
			$("#submitBtn").click(function() {
				 window.location.href = "/back/financingBaseAction!list"; 
			}); 
			$("#checksubmitBtn").click(function() {
				if ($("#startDate").val() == "") { 
                    alert("投标开始不能为空!");
                    return false;
                 }
				 if ($("#endDate").val() == "") { 
                    alert("投标截止不能为空!");
                    return false;
                 }
                 if ($("#minStart").val() == ""||$("#minStart").val()<100) { 
                    messageTip("最小起投必须100元以上并且是整十!");
                    return false;        
                 }
                if('1'==$('input:radio[name="financingBase.preInvest"]:checked').val()){//选择了开启    
	                  if($("[name='batchCheck']:checked").length==0){            
	                     alert("开启投标优先时,至少选择一个用户组!");      
	                     return false;      
	                 }   
                  }
                 var checkedValues = new Array();
			     $(':checkbox').each(function(){
					if($(this).is(':checked'))    
					{
						checkedValues.push($(this).val());
					}    
		     	 });
			     	
				var groupIds=checkedValues.join(',');     
				var conTemplateId=$("#conTemplateId").val();            
			    var autoinvest=$('input:radio[name="financingBase.autoinvest"]:checked').val();        
                var preInvest = $('input:radio[name="financingBase.preInvest"]:checked').val();
                var msg;
                if (preInvest== "1") {               
                         msg = "您真的确定要开启投标优先吗？请确认！";     
	                     if (autoinvest== "1") {
	                         msg = "您真的确定要开启'投标优先'和'委托自动投标'吗？请确认！";  
	                     }    
						if (confirm(msg)==true){ 
						     window.location.href = "/back/financingBaseAction!fabu?id=${id}&actionUrl=${actionUrl}&financingBase.startDate="+$("#startDate").val()+"&financingBase.endDate="+$("#endDate").val()+"&financingBase.minStart="+$("#minStart").val()+"&financingBase.preInvest="+preInvest+"&groupIds="+groupIds+"&conTemplateId="+conTemplateId+"&autoinvest="+autoinvest;           
						}else{ 
						   return false; 
						}      
                 }else{  
                 	    if (autoinvest== "1") {
	                         msg = "您真的确定要开启'委托自动投标'吗？请确认！";  
	                         	if (confirm(msg)==true){ 
		               	             window.location.href = "/back/financingBaseAction!fabu?id=${id}&actionUrl=${actionUrl}&financingBase.startDate="+$("#startDate").val()+"&financingBase.endDate="+$("#endDate").val()+"&financingBase.minStart="+$("#minStart").val()+"&groupIds="+groupIds+"&conTemplateId="+conTemplateId+"&autoinvest="+autoinvest;          
		  						}else{ 
								   return false; 
								}
	                     }
		                window.location.href = "/back/financingBaseAction!fabu?id=${id}&actionUrl=${actionUrl}&financingBase.startDate="+$("#startDate").val()+"&financingBase.endDate="+$("#endDate").val()+"&financingBase.minStart="+$("#minStart").val()+"&groupIds="+groupIds+"&conTemplateId="+conTemplateId+"&autoinvest="+autoinvest;          
	                         
					
                }
 
				 
			
			
			});    
		   <c:if test="${financingBase.preInvest=='1'}">  
				 $("#groupsTr").css({'display':''});  	    
		   </c:if>    
		    
		    $("input[name='financingBase.preInvest']").change(function(){         
				  if('0'==$(this).val()){   
				    $("#groupsTr").css({'display':'none'});     
				    $("[name='batchCheck']").removeAttr("checked");   
				  }else{     
				    $("#groupsTr").css({'display':''});    
				  }      
			 });  
	
	
				
			$("#noFabusubmitBtn").click(function() {   
			    if (confirm("确认要驳回到待审核状态吗？")) {
		           window.location.href = "/back/financingBaseAction!fabuBohui?id=${id}"   
		        }  
		        return false;
			}); 		 
			 
		});
</script>
		<title>融资项目发布</title>  
		<style type="text/css">

			.s_table td{background-color:#e3e3e3}
		</style>
									<style type="text/css">  
			.hui{
			  background-color:#E6E6E6;
			} 
			.baiSe{
			  background-color:#FFF;
			} 
			</style>
	</head>

	<body class="ui-widget-header" style="color: black"> 
			<input type='hidden' class='autoheight' value="auto" />  
	 			<table style="width:70%" class="s_table"> 
			    <tr  > 
					<td style="padding-left:30px;"  style="color:black">
						${financingBase.shortName } 
							&nbsp;&nbsp;&nbsp;进度:<fmt:formatNumber value='${(financingBase.currenyAmount/financingBase.maxAmount)*100}'  type="currency" currencySymbol=""/>%&nbsp;&nbsp;&nbsp;&nbsp;
							<c:if test="${financingBase.state=='5'}"><button class="ui-state-default" onclick='javascript:window.open("/back/financingBaseAction!receipts?id=${financingBase.id}")'>融资单</button></c:if>
										
						<c:if test="${(financingBase.state=='0')||(financingBase.state=='1')}">
							<button class="ui-state-default" id="checksubmitBtn"   >  
								确认发布
							</button> &nbsp;&nbsp; 
							<button class="ui-state-default" id="noFabusubmitBtn"   >
								驳回
							</button>  
				 		</c:if> 
				 		 			 		
					</td>
				</tr>
				</table>
			  <table  style="width:70%;font-size:10p;" class="s_table">  
				<tr>
				   <td align="right" class="ui-widget-header ui-corner-all" style="width:18%">项目编号：</td> 
				   <td> 
					    ${financingBase.code}
                    </td>
                     <td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">融资方：</td> 
				    <td >
						<c:if test="${(financingBase.financier)!= null}">
							 <script>document.write(name("${financingBase.financier.eName}"));</script>
						</c:if>	 
					    <c:if test="${(financingBase.financier)== null}">
								  暂无
					    </c:if>
					 </td>
                   
				</tr>				
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
			                    <c:if test="${(financingBase.interestDay)== 0}">${financingBase.businessType.term}月</c:if> 						</c:if>	 
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
				    <%
				    		FinancingBase p = (FinancingBase)request.getAttribute("financingBase"); //.maxAmount
				    		String maxAmount = p.getMaxAmount().toString();
				    	 %>						
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">总融资额：</td><td ><fmt:formatNumber value='${financingBase.maxAmount }' type="currency" currencySymbol=""/>元</td>
				</tr>
				<tr> 
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">已融资额：</td> <td ><fmt:formatNumber value='${financingBase.currenyAmount}' type="currency" currencySymbol=""/>元</td>
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">可融资额：</td><td ><fmt:formatNumber value='${financingBase.curCanInvest }' type="currency" currencySymbol=""/>元</td>
 				</tr>
				<tr> 
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">投标开始：</td> 
					<td >
						<input type="text" value="<fmt:formatDate value="<%=new Date()%>" pattern="yyyy-MM-dd"/>"  id="startDate" readonly="readonly"/>
						<span style="color:red;">*</span>  
						<!-- <fmt:formatDate value="${financingBase.startDate}" pattern="yyyy-MM-dd"/> -->
					</td> 
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">投标截止：</td> 
					<td >
						<input type="text" value="<fmt:formatDate value="${financingBase.endDate }" pattern="yyyy-MM-dd"/>"  id="endDate" readonly="readonly"/> 
						<span style="color:red;">*</span>
						<!-- <fmt:formatDate value="${financingBase.endDate }" pattern="yyyy-MM-dd"/> -->
					</td>
				</tr>
				<tr>
				<td  align="right"  class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" >年利率：</td>
					<td>${financingBase.rate }%
				</td>
				
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					最低起投额：
				</td>
				<td>
				       <input type="text" value="${financingBase.minStart }" id="minStart"/>元 	
				</td> 
			</tr>
		   <tr>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					借款合同：
				</td>
				<td colspan="3"> 
						 <s:select name="conTemplateId"  id="conTemplateId"  list="cTemplates"   listKey="id" listValue="description" />    
				</td>   
			</tr>
		    <tr>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					委托自动投标：
				</td>
				<td colspan="3">
					 <!--<s:radio list="#{'0':'对外手工投标','1':'委托自动投标'}" name="financingBase.autoinvest"  id='autoinvest'/>-->
					 <s:radio list="#{'0':'对外手工投标'}" name="financingBase.autoinvest"  id='autoinvest'/>
				</td>   
			</tr>			
		    <tr>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					开启投标优先：
				</td>
				<td colspan="3">
					<div class="ui-state-highlight ui-corner-all">
					<c:if test="${financingBase.preInvest=='1'}">开启优先投标(<s:iterator value="#request.selectGroups" status="statu" id="obj"><s:if test="#statu.last">${obj.name}</s:if><s:else>${obj.name},</s:else></s:iterator>)
					</c:if>
					<c:if test="${financingBase.preInvest=='0'}">关闭优先投标</c:if> </div>	
				</td>   
			</tr> 
                <tr>
                   <td align="right" class="ui-widget-header ui-corner-all" >担保方：</td> <td >
					   ${financingBase.createBy.org.parentCoding}-${financingBase.createBy.org.name}  					    
				    </td>
				    <td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">行业：</td> 
					<td >
					   <div class="ui-state-highlight ui-corner-all"><c:if test="${(financingBase.hyType)!= null}"> 
						    ${financingBase.hyType} 
						 </c:if>	 
					    <c:if test="${(financingBase.hyType)== null}"> 
								  暂无 
					    </c:if></div>
					</td> 
					
				</tr>  	
				 <tr>
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" >投标人数：</td> <td >${financingBase.haveInvestNum}</td>
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" >状态：</td><td >
			  	            <c:if test="${financingBase.state=='0'}"><span style="color:#4169E1;">待审核</span></c:if>
					        <c:if test="${financingBase.state=='1'}"><span style="color:green;">待发布</span></c:if>
					         <c:if test="${financingBase.state=='1.5'}"><span style="color:green;">待挂单</span></c:if>
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
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">用途：</td>
					<td >${financingBase.yongtu}</td>
				</tr> 
				<!-- 
							<tr>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					贷款分类：
				</td>
				<td colspan="3">
					 <c:if test="${(financingBase.dkType)!= null}">
					   	 ${financingBase.dkTypeShow}	
			         </c:if>
					 <c:if test="${(financingBase.dkType)== null}">
								  暂无
					    </c:if>
				</td>   
			</tr>
			 -->
			</table>
						<div class="tab" style="height:100%;">
			<dl>
				<dt>
					<a>风险评级</a><a>保障措施</a><a>项目详情</a><c:if test="${financingBase.fxbzState=='1'}"><a>担保情况</a></c:if>
				</dt>
				<dd  style="padding-top:30px;"> 
					<ul style="list-style-type:none;">
					  <li style="list-style-type:none;">
						<table border="0" align="left">
								<tr>
									<td align="right">
										产品评级：
									</td>
									<td>
										${financingBase.czzsShow}
									</td>
									<td align="left">
										${financingBase.czzsNote}
									</td>
								</tr>
								<tr>
									<td align="right" width="25%">
										融资方评级：
									</td>
									<td width="25%">
										${financingBase.qyzsShow}
									</td>
									<td align="left">
										${financingBase.qyzsNote}
									</td>
								</tr> 
								<tr>
									<td align="right">
										担保机构评级：
									</td>
									<td>
										${financingBase.dbzsShow}
									</td>
									<td align="left">
										${financingBase.dbzsNote}
									</td>
								</tr>
								<tr>
									<td align="right">
										项目评级：
									</td>
									<td>
										${financingBase.zhzs}(${financingBase.zhzsLevel})
									</td>
									<td align="left">
										${financingBase.zhzsNote}
									</td>
								</tr>
							</table>
						</li>
					</ul> 	 
					<ul style="list-style-type:none;">
					  <li style="list-style-type:none;">
						<table border="0" align="left">  
							  <tr> 
							  <td align="left" colspan="3">    
								     ${financingBase.purpose} &nbsp;&nbsp;&nbsp;
								</td>
							</tr>  
						</table> 
						</li>
					</ul> 
					<ul style="list-style-type:none;">
					  <li style="list-style-type:none;">
						<table border="0"> 
							<tr> 
								<td>
								     ${financingBase.note}&nbsp;&nbsp;&nbsp; 
								</td>
							</tr>  
						</table> 
						</li>
					</ul> 
					<!--<c:if test="${financingBase.fxbzState=='1'}">
					<ul style="list-style-type:none;">
					    <li style="list-style-type:none;">
						  <table border="0"> 
							<tr> 
								<td>
							
							
		<table cellpadding="0" cellspacing="1" bgcolor="#339933" style="background-color:#000">
	<tr>
		<td align="center" class="hui">
			<span class="title">担保公司名称：</span>
		</td>
		<td colspan="2" class="baiSe"><c:if test="${(memberGuarantee.id)!= null}">
				<c:if test="${memberGuarantee.memberBase.category==\"1\"}">
							    ${memberGuarantee.memberBase.pName}
							</c:if>
				<c:if test="${memberGuarantee.memberBase.category==\"0\"}">
							    ${memberGuarantee.memberBase.eName}
							</c:if>
			</c:if>
			<c:if test="${(memberGuarantee.id)== null}">
								  暂无
			</c:if></td>
	</tr>
	<tr>
		<td align="center" class="hui">
			<span class="title">交易中心推荐指数：</span>
		</td>
		<td colspan="2" class="baiSe"> 
		      <div id="ajaxStar"></div>  
		</td>
	</tr> 
	<tr class="hui">
		<td colspan="3" align="center">
			详细
		</td>
	</tr>
	<tr class="hui">
		<td colspan="3" align="center">
			详${memberGuarantee.note}   
		</td>
	</tr>
	 
</table>					
							
							
							
							
							
								</td>
							</tr> 
						  </table> 
						</li>
					</ul> 
					</c:if>
					<ul style="list-style-type:none;">
					    <li style="list-style-type:none;"> 
									<div  id="responseFiles"> 
							        <c:forEach items="${files}" var="entry">
									     <a href="/Static/userfiles/${entry.id}" target="_blank" style="color: black"  <c:if test="${(entry.ext=='jpg')||(entry.ext=='JPG')||(entry.ext=='gif')||(entry.ext=='GIF')||(entry.ext=='jpeg')||(entry.ext=='JPEG')||(entry.ext=='bmp')||(entry.ext=='png')}"> class="tooltipImg2" </c:if>  >${entry.fileName}</a><br/>
									</c:forEach> 
									</div> 
						</li>
					</ul>  
				--></dd>
			</dl>
		</div>   
		<!-- 
			<table style="width:100%;" class="s_table">
				 
				<tr>
					<td   colspan="4" style="padding-left:200px;width:100%;" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
						
						<c:if test="${(financingBase.state=='0')||(financingBase.state=='1')}">
							<button class="ui-state-default" id="checksubmitBtn"   >
								挂单通过
							</button>  
				 		</c:if> 
					</td>
				</tr>  
			</table> -->
			 <script type="text/javascript">
		      setIframeHeight(400);
		     </script>
	</body>
</html>
