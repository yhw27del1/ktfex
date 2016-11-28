<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>

<html>
	<head>
		<title>${financingBase.shortName}-融资方合同</title>
		<style>
			body{padding:0;margin:0;text-align:center;font-size:13px;overflow-x:hidden}
			table td{font-size:13px;}
			.agreement{color:blue}
			.agreement:HOVER{text-decoration: underline;cursor:pointer}
		</style>
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<script>
		$(function(){
			$("#make_sure").click(function(){
				$.post("/back/financingBaseAction!agreement?id=${financingBase.id}",{},function(){
					window.location.reload();
				});
			});
			$('.agreement').click(function(){
				window.showModalDialog("/back/investBaseAction!agreement_ui?invest_record_id="+$(this).attr("id"), null, "dialogWidth:720px;dialogHeight:auto;status:no;help:yes;resizable:no;");
				
			});
		});
	 var ratyPath='/Static/js/star/rater/img';  
 	$(function() {   
       //加星级    
	  	$("#ajaxStar").raty({hintList:[arrRater[4],arrRater[3],arrRater[2],arrRater[1],arrRater[0]],path:ratyPath,readOnly:true,number:5,start:${memberGuarantee.tjzs}});   
	    $("#ajaxStar").css({'width':'130px'});  
	});   
		</script>
          <style type="text/css">  
			.hui{
			  background-color:#E6E6E6;
			} 
			.baiSe{
			  background-color:#FFF;
			} 
			</style>
	</head>
	<body>
		<!-- <div style="position:absolute;top:0px;right:30px;"><input type="button" value="打印" onclick="print()"/></div> -->
		<div style="width:650px;overflow-x:hidden;margin-left:auto;margin-right:auto;margin-top:30xp;font-size:13px;">
			<div style="text-align:center;font-size:16px;">${financingBase.shortName}-融资方合同<br/></div>
			<table width="100%" cellpadding="1" cellspacing="0">
				<tr>
					<td colspan="4" height="60" valign="bottom" style="border-bottom:2px solid black;font-size:15px;">融资项目信息</td>
				</tr>
				<tr>
					<td width="100">项目编号：</td><td width="200">${financingBase.code}</td>
					<td width="100">项目简称：</td><td width="200">${financingBase.shortName}</td>
				</tr>
				<tr>
					<td>总融资额：</td><td width="200">${financingBase.maxAmount}</td>
					<td>已融资额：</td><td width="200">${financingBase.currenyAmount}</td>
				</tr>
				<tr>
					<td>剩余融资额：</td><td width="200">${financingBase.curCanInvest}</td>
					<td>年利率：</td><td width="200">${financingBase.rate/100}</td>
				</tr>
				<tr>
					<td>投标开始日期：</td><td width="200"><fmt:formatDate value="${financingBase.startDate}" pattern="yyyy-MM-dd"/></td>
					<td>投标截止日期：</td><td width="200"><fmt:formatDate value="${financingBase.endDate}" pattern="yyyy-MM-dd"/></td>
				</tr>
				<tr>
					<td>已投标人数：</td><td colspan="3">${financingBase.haveInvestNum}</td>
				</tr>
				<tr>
					<td valign="top">保障措施：</td><td colspan="3">${financingBase.purpose}</td>
				</tr>
				<tr>
					<td valign="top">担保情况：</td><td colspan="3">
					
			<!-- 担保情况开始 -->
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
		<td align="center" style="width:100px;">
			指标值
		</td>
		<td align="center">
			备注
		</td>
	</tr>
	<tr>
		<td align="center" class="hui" style="width:190px;">
			<span>注册资金：</span>
		</td>
		<td align="center" class="baiSe">
			${memberGuarantee.createMoney}万元
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
			${memberGuarantee.createYear}年
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
			${memberGuarantee.empNumber}人
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
			${memberGuarantee.jingyanNumber}%
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
			第一等级集中度小于20%； 第二等级集中度为20%-40%； 第三等级集中度为40%-60%； 第四等级集中度为60%-80%；
			第五等级集中度大于80%。
		</td>
	</tr>
	<tr>
		<td align="center" class="hui">
			<span>累计代偿率：</span>
		</td>
		<td align="center" class="baiSe">
			${memberGuarantee.daichanRate}%
		</td>
		<td class="hui" align="center">
			“累计代偿率”是指担保公司自营业以来，因被担保人违约而造成代为偿还借款的合同笔数占总担保合同笔数的比率，该比率越小说明该担保公司风险控制能力越强；反之亦然。
		</td>
	</tr>
</table>
	  		
	  		<!-- 担保情况结束 -->
					
					</td>
				</tr>
				<tr>
					<td valign="top">项目详情：</td><td colspan="3">${financingBase.note}</td>
				</tr>
				
				<tr>
					<td colspan="4" height="60" valign="bottom" style="border-bottom:2px solid black;font-size:15px;">业务类型</td>
				</tr>
				<tr>
					<td width="100">类型名称：</td><td width="200">${financingBase.businessType.name}</td>
					<td width="100">期限(月)：</td><td width="200">
				<c:if test="${(financingBase.interestDay)!= 0}">按日计息(${financingBase.interestDay}天)</c:if>
			    <c:if test="${(financingBase.interestDay)== 0}">${financingBase.businessType.term}个月 </c:if>  
				</tr>
				<tr>
					<td width="100">还款方式：</td><td width="200">${financingBase.businessType.returnPattern}</td>
					<td width="100">还款次数：</td><td width="200">${financingBase.businessType.returnTimes}</td>
				</tr>
				<tr>
					<td colspan="4" height="60" valign="bottom" style="border-bottom:2px solid black;font-size:15px;">融资方信息</td>
				</tr>
				<tr>
					<td width="100">融资方类别：</td><td width="200">${financingBase.financier.category=="0"?"企业":"个人"}</td>
					<td width="100">企业名称：</td><td width="200">${financingBase.financier.eName}</td>
				</tr>
				<tr>
					<td width="100">企业地址：</td><td width="200">${financingBase.financier.eAddress}</td>
					<td width="100">邮政编码：</td><td width="200">${financingBase.financier.ePostcode}</td>
				</tr>
				<tr>
					<td width="100">企业性质：</td><td width="200">${financingBase.financier.companyProperty.name}</td>
					<td width="100">所属行业：</td><td width="200">${financingBase.financier.industry.name}</td>
				</tr>
				<tr>
					<td width="100">固定电话：</td><td width="200">${financingBase.financier.ePhone}</td>
					<td width="100">经营状况：</td><td width="200">${financingBase.financier.eState}</td>
				</tr>
				<tr>
					<td colspan="4" height="60" valign="bottom" style="border-bottom:2px solid black;font-size:15px;">担保方信息&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-size:11px;color:#A30707">担保方式：${financingBase.fxbzState=="0"?"风险补偿":"担保代偿"}</span></td>
				</tr>
				<s:if test="financingBase.fxbzState=='1'">
				<tr>
					<td width="100">融资方类别：</td><td width="200">${financingBase.guarantee.category=="0"?"企业":"个人"}</td>
					<td width="100">企业名称：</td><td width="200">${financingBase.guarantee.eName}</td>
				</tr>
				<tr>
					<td width="100">企业地址：</td><td width="200">${financingBase.guarantee.eAddress}</td>
					<td width="100">邮政编码：</td><td width="200">${financingBase.guarantee.ePostcode}</td>
				</tr>
				<tr>
					<td width="100">企业性质：</td><td width="200">${financingBase.guarantee.companyProperty.name}</td>
					<td width="100">所属行业：</td><td width="200">${financingBase.guarantee.industry.name}</td>
				</tr>
				<tr>
					<td width="100">固定电话：</td><td width="200">${financingBase.guarantee.ePhone}</td>
					<td width="100">经营状况：</td><td width="200">${financingBase.guarantee.eState}</td>
				</tr>
				</s:if>
				
				
				<tr>
					<td colspan="4" height="60" valign="bottom" style="border-bottom:2px solid black;font-size:15px;">投资人投标信息</td>
				</tr>
				<%boolean makesure = true; %>
				<c:forEach items="${investRecords}" var="iter">
				<tr><td colspan="4" height="20">&nbsp;</td></tr>
				<tr>
					<td width="100">投资人帐号：</td><td width="200">${iter.investor.user.username}</td>
					<td width="100">投资人姓名：</td><td width="200">
						<c:if test="${iter.investor.category=='0'}">
							${iter.investor.eName}
						</c:if>
						<c:if test="${iter.investor.category=='1'}">
							${iter.investor.pName}
						</c:if>
					</td>
				</tr>
				<tr>
					<td width="100">投标额：</td><td width="200">${iter.investAmount}</td>
					<td width="100">投标时间：</td><td width="200">${iter.createDate}</td>
				</tr>
				<tr>
					<td width="100">合同号：</td><td width="200"><a class="agreement" id="${iter.id}">${iter.contract.contract_numbers}</a></td>
					<td>合同状态：</td>
					<td>
						<c:if test="${iter.financier_make_sure==null}"><%makesure = false; %>融资方未确认</c:if>
						<c:if test="${iter.financier_make_sure!=null}">融资方已确认&nbsp;&nbsp;确认时间:<fmt:formatDate value="${financingBase.finishtime}" pattern="yyyy-MM-dd"/></c:if>
						<br/>
						<c:if test="${iter.investor_make_sure==null}">投资方未确认</c:if>
						<c:if test="${iter.investor_make_sure!=null}">投资方已确认&nbsp;&nbsp;确认时间:<fmt:formatDate value="${financingBase.finishtime}" pattern="yyyy-MM-dd"/></c:if>
					</td>
				</tr>
				<tr><td colspan="4" height="20">&nbsp;</td></tr>
				
				</c:forEach>
				<tr>
					<td colspan="4" height="60">
						<%if(!makesure) {%><input type="button" value="确认合同" id="make_sure"/><%} %>
					</td>
				</tr>
				<tr>
					<td colspan="4" height="160"></td>
				</tr>
			</table>
		</div>
	</body>
</html>
