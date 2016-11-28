<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ include file="/common/taglib.jsp"%>
		<style type="text/css">  
			.hui{
			  background-color:#E6E6E6;
			} 
			.baiSe{
			  background-color:#FFF;
			} 
			</style>
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
			“担保额度”是指银行批准的担保公司可担保贷款的总额，额度的大小直接反映了担保公司的担保实力。系统将担保公司在各银行金融机构所获批准的额度总计，分为1-6级：
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

