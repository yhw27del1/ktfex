<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<html>
	<head>
	    <script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
	    <script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
	    <link rel="stylesheet" href="/Static/css/page/page-skin1.css" type="text/css"/> 
	    <link rel="stylesheet" href="/Static/css/simpleTabs.css" type="text/css"/>
	    <script type="text/javascript" src="/Static/js/simpleTabs.js"></script>   
	    <link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>
	    <script type="text/javascript" src="/Static/js/jquery.uploadify-v2.1.0/swfobject.js"></script>
        <script type="text/javascript" src="/Static/js/jquery.uploadify-v2.1.0/jquery.uploadify.v2.1.0.min.js"></script>
        <script type="text/javascript" src="/Static/js/open.js"></script>
        <script type="text/javascript" src="/Static/js/kindeditor/kindeditor-min.js"></script>
		<script type="text/javascript" src="/Static/js/autoheight.js"></script>
	    <script type="text/javascript" src="/Static/js/star/rater/js/jquery.raty.min.js"></script>  

<script type="text/javascript">
    var ratyPath='/Static/js/star/rater/img';  
 	$(function() {  
       //加星级   
	  $("#demo1").raty({hintList:[arrRater[4],arrRater[3],arrRater[2],arrRater[1],arrRater[0]],path:ratyPath,readOnly:true,number:5,start:${financingBase.qyzs}}); 
	  $("#demo2").raty({hintList:[arrRater[4],arrRater[3],arrRater[2],arrRater[1],arrRater[0]],path:ratyPath,readOnly:true,number:5,start:${financingBase.fddbzs}}); 
	  $("#demo3").raty({hintList:[arrRater[4],arrRater[3],arrRater[2],arrRater[1],arrRater[0]],path:ratyPath,readOnly:true,number:5,start:${financingBase.czzs}}); 
	  $("#demo4").raty({hintList:[arrRater[4],arrRater[3],arrRater[2],arrRater[1],arrRater[0]],path:ratyPath,readOnly:true,number:5,start:${financingBase.dbzs}}); 
	  $("#demo5").raty({hintList:[arrRater[4],arrRater[3],arrRater[2],arrRater[1],arrRater[0]],path:ratyPath,readOnly:true,number:5,start:${financingBase.zhzs}});   
	  $("#demo1").css({'width':'130px'}); 
	  $("#demo2").css({'width':'130px'});  
	  $("#demo3").css({'width':'130px'});  
	  $("#demo4").css({'width':'130px'});  
	  $("#demo5").css({'width':'130px'}); 
	  	  $("#ajaxStar").raty({hintList:[arrRater[4],arrRater[3],arrRater[2],arrRater[1],arrRater[0]],path:ratyPath,readOnly:true,number:5,start:${memberGuarantee.tjzs}});   
	  $("#ajaxStar").css({'width':'130px'});  
	  $("#invest").submit(function(){
			var $money = $("#money");
			var money = $money.val();
			if(money!=""){
				money = parseInt(money);
			}
			var balance = parseInt($("#balance").text());
			var min = parseInt($("#min").text());
			var max = parseInt($("#max").text());
			if(money==""||money==NaN){
				alert("请输入投标金额");
				$money.focus();
				return false;
			}else{
				if(money<min){
					alert("当前融资项目的最小融资额为："+min+"元");
					$money.focus();
					return false;
				}
				if(money>max){
					alert("当前融资项目的最大融资额为："+max+"元");
					$money.focus();
					return false;
				}
				if(money>balance){
					alert("您的账户余额为："+balance+"元,不足以投标："+money+"元");
					$money.focus();
					return false;
				}
			}
	   	});
		$("#submitBtn").click(function() {
			 window.location.href = "/back/financingBaseAction!investList"; 
		}); 
	});   
</script>
		<title>投标</title>  
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
			   
	<div style="margin-top: 5px; padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
		<p>
			您的交易账户余额为：<span id="balance">${account.balance}</span>元(${account.daxie})<br />
			您的最小投标额为：<span id='min'>${invest.minMoney}</span>元(${invest.minMoney_daxie})<br />
			您的最大投标额为：<span id='max'>${invest.maxMoney}</span>元(${invest.maxMoney_daxie})
			<c:if test="${invest.hasMoney>0}">
				,您已经投标了:<span>${invest.hasMoney}</span>元(${invest.hasMoney_daxie})
			</c:if>
			<br />
			此融资项目已融资：${financingBase.currenyAmount}元(${financingBase.currenyAmount_daxie})<br />
			此融资项目的可融资额为：${financingBase.curCanInvest}元(${financingBase.curCanInvest_daxie})<br />
			已有${financingBase.haveInvestNum}人投标了此融资项目<br />
			<c:if test='${invest.canInvest==true}'>
				<form id='invest' action='/back/investBaseAction!agreement_ui'>
					<input name="financingBaseId" value="${invest.financingId}" type="hidden"/>
					投标金额：<input id='money' name="investMoney" onkeyup="this.value=this.value.replace(/[^\d]/g,'');" type="text"/>
					<input type="submit" value="投标"/><input type="button" id="submitBtn" value="返回" />
				</form>
			</c:if>
			<c:if test='${invest.canInvest==false}'>
				<span style='color:red;'>${invest.msg}</span>
				<button class="ui-state-default" id="submitBtn">返回</button>
			</c:if>
		</p>
	</div>
			   
	 			<table style="width:70%" class="s_table"> 
			    <tr  > 
					<td style="padding-left:30px;"  style="color:black">
						${financingBase.shortName } 
							&nbsp;&nbsp;&nbsp;进度:<fmt:formatNumber value='${(financingBase.currenyAmount/financingBase.maxAmount)*100}'    type="currency" currencySymbol=""/>%&nbsp;&nbsp;&nbsp;&nbsp;
							<c:if test="${financingBase.state=='5'}">
								<button class="ui-state-default" onclick='javascript:window.open("/back/financingBaseAction!receipts?id=${financingBase.id}")'>融资单</button>
								<button class="ui-state-default" onclick='javascript:window.open("/back/financingBaseAction!agreement_ui?id=${financingBase.id}")'>融资合同</button>
							</c:if>
						 <c:if test="${!empty directUrl}">
							 <button class="ui-state-default" id="submitBtn" onclick="window.location.href ='${directUrl}'">返回
					         </button>    
						</c:if>	 
							
					 	 
					</td>
				</tr>
				</table>
			  <table  style="width:70%;font-size:10p;" class="s_table">  
				<tr>
				   <td align="right" class="ui-widget-header ui-corner-all" style="width:18%">项目编号：</td> <td> 
					    ${financingBase.code}
                    </td>
                     <td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">融资方：</td> <td >
						******
					</td>
				</tr>
				<tr>
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
							  ${financingBase.businessType.term}月<c:if test="${(financingBase.businessType.interestDay)!= 0}">(按日计息:${financingBase.businessType.interestDay}天)</c:if>
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
					<td ><fmt:formatDate value="${financingBase.startDate}" pattern="yyyy-MM-dd"/></td> 
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">投标截止：</td> 
					<td > <fmt:formatDate value="${financingBase.endDate }" pattern="yyyy-MM-dd"/></td>
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
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">投标人数：</td>
					<td >${financingBase.haveInvestNum}</td>
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">状态：</td><td >	
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
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">风险保障</td>
					<td >
						 ${financingBase.fxbzStateName}
					</td>
					<td align="right" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">用途：</td>
					<td >${financingBase.yongtu}</td>
				</tr> 
			</table>
			

			<div class="tab" style="height:400px;">
			<dl>
				<dt>
					<a>风险评级</a><a>保障措施</a><a>项目详情</a>	
					<c:if test="${financingBase.fxbzState=='1'}">
						<a>担保情况</a>
					</c:if><a>资料清单</a>
				</dt>
				<dd  style="padding-top:30px;"> 
					<ul style="list-style-type:none;">
					  <li style="list-style-type:none;">
						<table border="0" align="left"> 
						      <tr>
								<td align="right" width="25%">企业基本信用指数：
								</td>
								<td width="25%">  <div id="demo1"></div> 
								</td>
								 <td  align="left">
								     ${financingBase.qyzsNote} 
								</td>
							</tr>  	
							 <tr>
								<td align="right">法人代表信用指数：
								</td>
								<td>
								    	    <div id="demo2"></div>  
								</td>
								 <td  align="left">
								     ${financingBase.fddbzsNote} 
								</td>
							</tr>  	
							<tr>
								<td align="right">履约偿债能力指数：
								</td>
								<td>
										    <div id="demo3"></div>   
								</td>
								<td  align="left">
								     ${financingBase.czzsNote}
								</td>
							</tr>  	
							<tr>
								<td align="right">担保情况指数：
								</td>
								<td>
									        <div id="demo4"></div>  
								</td>
								 <td  align="left">
								     ${financingBase.dbzsNote} 
								</td>
							</tr>  	
							<tr>
								<td align="right">综合推荐指数：
								</td>
								<td>
										   <div id="demo5"></div>  
								</td>
								<td  align="left">
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
					<ul style="list-style-type:none;">
					    <li style="list-style-type:none;"> 
									<div  id="responseFiles"> 
							        <c:forEach items="${files}" var="entry">
									     <a href="/Static/userfiles/${entry.id}" target="_blank" style="color: black"  <c:if test="${(entry.ext=='jpg')||(entry.ext=='JPG')||(entry.ext=='gif')||(entry.ext=='GIF')||(entry.ext=='jpeg')||(entry.ext=='JPEG')||(entry.ext=='bmp')||(entry.ext=='png')}"> class="tooltipImg2" </c:if>  >${entry.fileName}</a><br/>
									</c:forEach> 
									</div> 
						</li>
					</ul>  
				</dd>
			</dl>
		</div>     
	</body>
</html>
