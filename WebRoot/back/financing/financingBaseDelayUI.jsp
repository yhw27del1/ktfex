<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<html>
	<head>
		<script type="text/javascript" src="/Static/js/autoheight.js"></script>
		<link rel="stylesheet" href="/Static/css/simpleTabs.css"
			type="text/css" />
		<script type="text/javascript" src="/Static/js/simpleTabs.js"></script>
		<script type="text/javascript" src="/Static/js/open.js"></script> 
		<script type="text/javascript" src="/back/four.jsp"></script>
		<script type="text/javascript">   
     
   $(document).ready(function(){		    
		    $("#endDate").datepicker({
		        showOn: 'button',
		        buttonImageOnly: false,
		        //changeMonth: true,
		        //changeYear: true,
		        numberOfMonths: 2,
		        dateFormat: "yy-mm-dd"
		        //minDate: +1 
		    });
		    
            $("#ui-datepicker-div").css({'display':'none'});
 
			
			$("#delayButton").click(function() { 
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
				   
				 
				  
				 if ($("#endDate").val() == "") { 
                    messageTip("投标截止不能为空!");
                    return false;
                 }  
                 
                 if(!confirm("是否确定变更此融资项目的信息(投标截止日期或开启投标优先)？")){
                    return false;
                 }
                  var conTemplateId=$("#conTemplateId").val();    
                  var preInvest = $('input:radio[name="financingBase.preInvest"]:checked').val();      
				  window.location.href = "/back/financingBaseAction!delay?id=${id}&financingBase.endDate="+$("#endDate").val()+"&financingBase.preInvest="+preInvest+"&groupIds="+groupIds+"&conTemplateId="+conTemplateId;       
			}); 
			
			
			//赋值  
			var groupIds=[<s:iterator value="#request.selectGroups" status="statu" id="obj"><s:if test="%{#statu.getIndex() > 0}">,</s:if>'<s:property value="id"/>'</s:iterator>];  
		    if(groupIds.length > 0)         
		    {
		      var temps=document.getElementsByName("batchCheck");
		      for(var i=0;i<temps.length;i++)
		         for(var j=0;j<groupIds.length;j++)
		           if(groupIds[j]==temps[i].value)
		              temps[i].checked=true;
		    }   
		    
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
			    
		});
		
	 function messageTip(message){ 
	        var dlgHelper = new dialogHelper();
	        dlgHelper.set_Title("温馨提示");
	        dlgHelper.set_Msg(message);
	        dlgHelper.set_Height("200");
	        dlgHelper.set_Width("400");
	        dlgHelper.set_Buttons({
	            '关闭': function(ev) { 
	                    $(this).dialog('close'); 
	            } 
	        });      
	        dlgHelper.open();  
      }
</script>
		<title>变更融资项目截止日期的界面</title>
		<style type="text/css">
.s_table td {
	background-color: #e3e3e3
}
</style>
		<style type="text/css">
.hui {
	background-color: #E6E6E6;
}

.baiSe {
	background-color: #FFF;
}
</style>
	</head>

	<body class="ui-widget-header" style="color: black">
		<input type='hidden' class='autoheight' value="auto" />
		<table style="width: 70%" class="s_table">
			<tr>
				<td style="padding-left: 30px;" style="color:black">
					${financingBase.shortName } &nbsp;&nbsp;&nbsp;进度:
					<fmt:formatNumber
						value='${(financingBase.currenyAmount/financingBase.maxAmount)*100}'
						type="currency" currencySymbol="" />
					%&nbsp;&nbsp;&nbsp;&nbsp;
					<c:if test="${financingBase.state=='5'}">
						<button class="ui-state-default"
							onclick='javascript:window.open("/back/financingBaseAction!receipts?id=${financingBase.id}")'>
							融资单
						</button>
					</c:if>
					<c:if
						test="${financingBase.state=='2' || financingBase.state=='3'}">
						<button class="ui-state-default" id="delayButton">
							确认变更
						</button>&nbsp;&nbsp; 
				 		</c:if>
					&nbsp;&nbsp;
				</td>
			</tr>
		</table>
		<table style="width: 70%; font-size: 10p;" class="s_table">
			<tr>
				<td align="right" class="ui-widget-header ui-corner-all"
					style="width: 18%">
					项目编号：
				</td>
				<td>
					${financingBase.code}
				</td>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					融资方：
				</td>
				<td>
					<c:if test="${(financingBase.financier)!= null}">
						<script>document.write(name("${financingBase.financier.eName}"));</script>
					</c:if>
					<c:if test="${(financingBase.financier)== null}">
								  暂无
					    </c:if>
				</td>
			</tr>
			<tr>
				<td align="right" class="ui-widget-header ui-corner-all"
					style="width: 18%">
					融资方行业：
				</td>
				<td>
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
				<td align="right" class="ui-widget-header ui-corner-all"
					style="width: 21%">
					融资方公司性质：
				</td>
				<td>
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
			</tr>
			<tr>
				<td align="right" class="ui-widget-header ui-corner-all"
					style="width: 18%">
					还款方式：
				</td>
				<td>
					<c:if test="${(financingBase.businessType)!= null}">
							${financingBase.businessType.returnPattern}
						</c:if>
					<c:if test="${(financingBase.businessType)== null}">
								  暂无
					    </c:if>
				</td>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					融资期限：
				</td>
				<td>
					<c:if test="${(financingBase.businessType)!= null}">
					   	  		<c:if test="${(financingBase.interestDay)!= 0}">按日计息(${financingBase.interestDay}天)</c:if>
			                    <c:if test="${(financingBase.interestDay)== 0}">${financingBase.businessType.term}月</c:if> 						</c:if>
					<c:if test="${(financingBase.businessType)== null}">
								  暂无
					    </c:if>
				</td>
			</tr>
			<tr>
				<td align="right" class="ui-widget-header ui-corner-all"
					style="width: 18%">
					融资方地区：
				</td>
				<td>
					<c:if test="${(financingBase.financier)!= null}"> 
						    ${financingBase.financier.provinceName}${financingBase.financier.cityName}  
						</c:if>
					<c:if test="${(financingBase.financier)== null}">
								  暂无
					    </c:if>
				</td>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					总融资额：
				</td>
				<td>
					<fmt:formatNumber value='${financingBase.maxAmount }'
						type="currency" currencySymbol="" />
					元
				</td>
			</tr>
			<tr>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					已融资额：
				</td>
				<td>
					<fmt:formatNumber value='${financingBase.currenyAmount}'
						type="currency" currencySymbol="" />
					元
				</td>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					可融资额：
				</td>
				<td>
					<fmt:formatNumber value='${financingBase.curCanInvest }'
						type="currency" currencySymbol="" />
					元
				</td>
			</tr>
			<tr>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					投标开始：
				</td>
				<td>
					<input type="text"
						value="<fmt:formatDate value="${financingBase.startDate}" pattern="yyyy-MM-dd"/>"
						id="startDate" readonly="readonly" />
				</td>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					投标截止：
				</td>
				<td>
					<input type="text"
						value="<fmt:formatDate value="${financingBase.endDate }" pattern="yyyy-MM-dd"/>"
						id="endDate" readonly="readonly" />
				</td>
			</tr>
		    <tr>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					委托自动投标：
				</td>
				<td colspan="3"> 
					<c:if test="${financingBase.autoinvest!='1'}">未开启</c:if>
					<c:if test="${financingBase.autoinvest=='1'}">开启</c:if>
				</td>   
			</tr>
			<tr>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					开启投标优先：
				</td>
				<td colspan="3">
					 <s:radio list="#{'1':'开启优先投标','0':'关闭优先投标'}" name="financingBase.preInvest" />
				</td>   
			</tr>
			
			<tr id="groupsTr" style='display:none;'>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					选择用户组：  
				</td>
				<td colspan="3">
					  <s:iterator value="#request.groups" status="statu" id="obj">  
					       <input type="checkbox" value='${obj.id }' name="batchCheck"/> <label>${obj.name}</label> 
					  </s:iterator> 
					  <input type="hidden" name="groupIds" id="groupIds" /> 
				</td>   
			</tr>
			<tr>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					合同模板：
				</td>
				<td colspan="3">
				    <s:select name="conTemplateId" id="conTemplateId" list="cTemplates"   listKey="id" listValue="description" /> 
				</td>   
			</tr>
			<tr>
				<td align="right" class="ui-widget-header ui-corner-all">
					担保方：
				</td>
				<td>
	                   <c:if test="${financingBase.createBy.org.parentCoding=='440300'}">   
							  资产监管
						</c:if> 
                    	<c:if test="${financingBase.createBy.org.parentCoding!='440300'}">   
							<c:if test="${(financingBase.guarantee)!= null}">
								<a href="#" class="tooltip"
									title="${financingBase.guarantee.eName}">
									${financingBase.guarantee.eName}</a>
							</c:if>
							<c:if test="${(financingBase.guarantee)== null}">
										  暂无
							    </c:if>
						</c:if>       
				

				</td>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					年利率：
				</td>
				<td>
					${financingBase.rate }%
				</td>
			</tr>
			<tr>
				<td align="right" class="ui-widget-header ui-corner-all"
					style="width: 18%">
					行业：
				</td>
				<td>
					<c:if test="${(financingBase.hyType)!= null}">
							${financingBase.hyTypeShow}
						</c:if>
					<c:if test="${(financingBase.hyType)== null}">
								  暂无
					    </c:if>  
				</td>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					企业类型：
				</td>
				<td>
					 <c:if test="${(financingBase.qyType)!= null}">
					   	 ${financingBase.qyTypeShow}	
			         </c:if>
					 <c:if test="${(financingBase.qyType)== null}">
								  暂无
					    </c:if>
				</td> 
			</tr>
			<tr>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					投标人数：
				</td>
				<td>
					${financingBase.haveInvestNum}
				</td>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					状态：
				</td>
				<td>
					<c:if test="${financingBase.state=='0'}">
						<span style="color: #4169E1;">待审核</span>
					</c:if>
					<c:if test="${financingBase.state=='1'}">
						<span style="color: green;">待挂单</span>
					</c:if>
					<c:if test="${financingBase.state=='2'}">
						<span style="color: red;">投标中 </span>
					</c:if>
					<c:if test="${financingBase.state=='3'}">
						<span style="color: #4169E1;">部分投标</span>
					</c:if>
					<c:if test="${financingBase.state=='4'}">
						<span style="color: green;">已满标</span>
					</c:if>
					<c:if test="${financingBase.state=='5'}">
						<span style="color: red;">已确认 </span>
					</c:if>
					<c:if test="${financingBase.state=='6'}">
						<span style="color: green;">已确认</span>
					</c:if>
					<c:if test="${financingBase.state=='7'}">
						<span style="color: red;">已签约 </span>
					</c:if>
			</tr>
			<tr>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					风险保障：
				</td>
				<td>
					 ${financingBase.fxbzStateName}
				</td>
				<td align="right"
					class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
					用途：
				</td>
				<td>
					${financingBase.yongtu}
				</td>
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
		<div class="tab" style="height: 100%;">
			<dl>
				<dt>
					<a>风险评级</a><a>保障措施</a><a>项目详情</a>
					<c:if test="${financingBase.fxbzState=='1'}">
						<a>担保情况</a>
					</c:if>
					<a>资料清单</a>
				</dt>
				<dd style="padding-top: 30px;">
					<ul style="list-style-type: none;">
						<li style="list-style-type: none;">
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
										项目总评分：
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
					<ul style="list-style-type: none;">
						<li style="list-style-type: none;">
							<table border="0" align="left">
								<tr>
									<td align="left" colspan="3">
										${financingBase.purpose} &nbsp;&nbsp;&nbsp;
									</td>
								</tr>
							</table>
						</li>
					</ul>
					<ul style="list-style-type: none;">
						<li style="list-style-type: none;">
							<table border="0">
								<tr>
									<td>
										${financingBase.note}&nbsp;&nbsp;&nbsp;
									</td>
								</tr>
							</table>
						</li>
					</ul>
					<c:if test="${financingBase.fxbzState=='1'}">
						<ul style="list-style-type: none;">
							<li style="list-style-type: none;">
								<table border="0">
									<tr>
										<td>
											<table cellpadding="0" cellspacing="1" bgcolor="#339933"
												style="background-color: #000">
												<tr>
													<td align="center" class="hui">
														<span class="title">担保公司名称：</span>
													</td>
													<td colspan="2" class="baiSe">
														<c:if test="${(memberGuarantee.id)!= null}">
															<c:if
																test="${memberGuarantee.memberBase.category==\"1\"}">
							    ${memberGuarantee.memberBase.pName}
							</c:if>
															<c:if
																test="${memberGuarantee.memberBase.category==\"0\"}">
							    ${memberGuarantee.memberBase.eName}
							</c:if>
														</c:if>
														<c:if test="${(memberGuarantee.id)== null}">
								  暂无
			</c:if>
													</td>
												</tr>
												<tr>
													<td align="center" class="hui">
														<span class="title">交易中心推荐指数：</span>
													</td>
													<td colspan="2" class="baiSe">
														<div id="ajaxStar"></div>
													</td>
												</tr>
												<tr>
													<td align="center" class="hui">
														<span class="title">担保范围及方式：</span>
													</td>
													<td colspan="2" class="baiSe">
														${memberGuarantee.type}
													</td>
												</tr>
												<tr class="hui">
													<td colspan="3" align="center">
														担保公司担保实力资讯
													</td>
												</tr>
												<tr class="hui">
													<td align="center">
														指标名称
													</td>
													<td align="center" style="width: 100px;">
														指标值
													</td>
													<td align="center">
														备注
													</td>
												</tr>
												<tr>
													<td align="center" class="hui" style="width: 190px;">
														<span>注册资金：</span>
													</td>
													<td align="center" class="baiSe">
														${memberGuarantee.createMoney}
													</td>
													<td class="hui" align="center">
														&nbsp;
													</td>
												</tr>
												<tr>
													<td align="center" class="hui">
														<span>成立年限：</span>
													</td>
													<td align="center" class="baiSe">
														<c:if test="${(memberGuarantee.createYear)!= null}">
			    ${memberGuarantee.createYear}年
			</c:if>
													</td>
													<td class="hui">
														“成立年限”，对一家担保公司的担保信誉有较大影响。一般来说，成立时间越长的担保公司其业务模式及操作流程越成熟，风险管控经验越丰富，对借款资金的安全保障程度就越高；反之亦然。
													</td>
												</tr>
												<tr>
													<td align="center" class="hui">
														<span>员工人数：</span>
													</td>
													<td align="center" class="baiSe">
														<c:if test="${(memberGuarantee.empNumber)!= null}">
			    ${memberGuarantee.empNumber}人
			</c:if>

													</td>
													<td class="hui">
														“员工人数”，是衡量担保公司业务规模的重要指标。通常员工人数越多，担保公司业务规模越大，开展业务能力也就越强，对借款项目的风险控制能力也就越强；反之亦然。
													</td>
												</tr>
												<tr>
													<td align="center" class="hui">
														<span>有担保行业工作经历<br>职员人数比率：</span>
													</td>
													<td align="center" class="baiSe">
														<c:if test="${(memberGuarantee.jingyanNumber)!= null}">
			     ${memberGuarantee.jingyanNumber}%
			</c:if>
													</td>
													<td class="hui">
														该指标反映了公司职员担保行业从业经验的总体水平，指标越大说明该公司职员在担保行业的经验丰富程度越高；反之亦然。
													</td>
												</tr>
												<tr>
													<td align="center" class="hui">
														合作银行金融机构：
													</td>
													<td align="center" class="baiSe">
														${memberGuarantee.bank}
													</td>
													<td class="hui">
														该项指标中，银行金融机构数量越多，说明银行界对该担保公司的认可程度越高，该担保公司业务可开展范围越广；银行规模越大，侧面反映了该担保公司在同类公司中竞争实力越强。
													</td>
												</tr>
												<tr>
													<td align="center" class="hui">
														<span>担保额度级别：</span>
													</td>
													<td align="center" class="baiSe">
														${memberGuarantee.dbedType}
													</td>
													<td class="hui" align="center">
														“担保额度”是指银行批准的担保公司可担保贷款的总额，额度的大小直接反映了担保公司的担保实力。中心将担保公司在各银行金融机构所获批准的额度总计，分为1-6级：
														第一等级担保额度范围为50亿以上； 第二等级担保额度范围为40亿至50亿； 第三等级担保额度范围为30亿至40亿；
														第四等级担保额度范围为20亿至30亿； 第五等级担保额度范围为10亿至20亿； 第六等级担保额度范围为10亿以下。
													</td>
												</tr>
												<tr>
													<td align="center" class="hui">
														<span>担保余额集中度级别：</span>
													</td>
													<td align="center" class="baiSe">
														${memberGuarantee.jzdType}
													</td>
													<td class="hui" align="center">
														“担保余额”是指担保公司目前在银行担保的贷款总额，前三笔最大担保余额除以担保余额总额得到担保余额集中度指标，该指标反映了担保公司业务集中程度，指标值越大，担保风险越高。中心按1-5级就该指标作出评价：
														第一等级集中度小于20%； 第二等级集中度为20%-40%； 第三等级集中度为40%-60%；
														第四等级集中度为60%-80%； 第五等级集中度大于80%。
													</td>
												</tr>
												<tr>
													<td align="center" class="hui">
														<span>累计代偿率：</span>
													</td>
													<td align="center" class="baiSe">
														<c:if test="${(memberGuarantee.daichanRate)!= null}">
			      ${memberGuarantee.daichanRate}%
			</c:if>
													</td>
													<td class="hui" align="center">
														“累计代偿率”是指担保公司自营业以来，因被担保人违约而造成代为偿还借款的合同笔数占总担保合同笔数的比率，该比率越小说明该担保公司风险控制能力越强；反之亦然。
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</li>
						</ul>
					</c:if>
					<ul style="list-style-type: none;">
						<li style="list-style-type: none;">
							<div id="responseFiles">
								<c:forEach items="${files}" var="entry">
									<a href="/Static/userfiles/${entry.id}" target="_blank"
										style="color: black"
										<c:if test="${(entry.ext=='jpg')||(entry.ext=='JPG')||(entry.ext=='gif')||(entry.ext=='GIF')||(entry.ext=='jpeg')||(entry.ext=='JPEG')||(entry.ext=='bmp')||(entry.ext=='png')}"> class="tooltipImg2" </c:if>>${entry.fileName}</a>
									<br />
								</c:forEach>
							</div>
						</li>
					</ul>
				</dd>
			</dl>
		</div>
		<script type="text/javascript">
		      setIframeHeight(400);
		     </script>
	</body>
</html>
