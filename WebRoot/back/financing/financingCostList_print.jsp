<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<c:set value="<%=new Date()%>" var="now"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>融资费用打印</title>
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<style>
			body{
				padding-top: 50px;
				margin: 0;
			}
			table,td,th{
				border:1px solid #000;   
    			border-collapse:collapse;
			}
			.table_content tbody td{
				padding-left:5px;
				padding-right:5px;
				white-space:nowrap;overflow:hidden;word-break:break-all;
			}
			#print_do{
				position: absolute;
				top:2px;
				right:5px;
				height:40px;
			}
			#logo{
				position: absolute;
				left:10px;
				top:60px;
				width:40px;
				height:40px;
			}
			.fields{
				position: fixed;
				top:0;
				left;0;
				width:100%;
				height:45px;
				background-color: #777777;
				z-index:10000;
			}
			.fields_table td{
				line-height:40px;
				color:#fff;
				padding:0 3px;
			}
			.fields_table td input{vertical-align: middle;}
			.fields_table td label{cursor: pointer;}
			.hide{display: none;}
</style>
		<script>
			$(function(){
				$("#print_do").click(function(){
					$(".fields").hide();
					$("body").css({"padding-top":"0px"});
					$("#logo").css({"top":"10px"});
					print();
					$(".fields").show();
					$("body").css({"padding-top":"50px"});
					$("#logo").css({"top":"60px"});
				});
				$(".fields_table input").click(function(){
					var _class = $(this).attr("id");
					if($(this).is(":checked")){
						$(".table_content th."+_class+",.table_content td."+_class).show();
					}else{
						$(".table_content th."+_class+",.table_content td."+_class).hide();
					}
				});
			});
		</script>
	</head>
	<body style="font-size:12px;">
		<div class="fields">
			<table class="fields_table">
				<tr>
					<td><input type="checkbox" checked="checked" id="qianyueriqi"/><label for="qianyueriqi">签约日期</label></td>
					<td><input type="checkbox" checked="checked" id="fabaofang"/><label for="fabaofang">担保方</label></td>
					<td><input type="checkbox" checked="checked" id="bianhao"/><label for="bianhao">编号</label></td>
					<td><input type="checkbox" checked="checked" id="rongziren"/><label for="rongziren">融资方</label></td>
					<td><input type="checkbox" checked="checked" id="rongzie"/><label for="rongzie">融资额</label></td>
					<td><input type="checkbox" checked="checked" id="qixian"/><label for="qixian">期限</label></td>
					<td><input type="checkbox" checked="checked" id="youwudanbao"/><label for="youwudanbao">有无担保</label></td>
					<td><input type="checkbox" id="fengxianguanlifei"/><label for="fengxianguanlifei">风险管理费</label></td>
					<td><input type="checkbox" id="jiekuanlvyuebaozhengjin"/><label for="jiekuanlvyuebaozhengjin">还款保证金</label></td>
					<td><input type="checkbox" id="rongzifuwufei"/><label for="rongzifuwufei">融资服务费</label></td>
					<td><input type="checkbox" id="danbaofei"/><label for="danbaofei">担保费</label></td>
					<td><input type="checkbox" id="xiweifei"/><label for="xiweifei">席位费</label></td>
					<!--<td><input type="checkbox" id="xinyongguanlifei"/><label for="xinyongguanlifei">信用管理费</label></td>
					<td><input type="checkbox" id="qitafeiyong"/><label for="qitafeiyong">其他费用</label></td>
					--><td><input type="checkbox" id="rongzijieyu"/><label for="rongzijieyu">融资结余</label></td>
					<td><input type="checkbox" id="beizhu"/><label for="beizhu">备注</label></td>
				</tr>
			</table>
			<input id="print_do" type="button" value="打印页面"/>
		</div>
		
		<div style="margin:0 auto 0 auto;">
			<img src="/Static/images/logo.png" id="logo"/>
			<div style="text-align: center"><h1>昆投互联网金融交易-融资费用列表</h1></div>
			<span>
				会计期间:<fmt:formatDate value="${startDate }" pattern="yyyy/MM/dd"/>-<fmt:formatDate value="${endDate }" pattern="yyyy/MM/dd"/>
			</span> 
		<table class="table_content" width="100%">
			<thead>
				<tr>
				<th nowrap="nowrap" class="qianyueriqi">签约日期</th>
				<th nowrap="nowrap" class="fabaofang">担保方</th>
				<th class="bianhao">编号</th>
				<th nowrap="nowrap" class="rongziren">融资方</th>
				<th nowrap="nowrap" class="rongzie">融资额(￥)</th>
				<th nowrap="nowrap" class="qixian">期限</th>
				<th nowrap="nowrap" class="youwudanbao">有无担保</th> 
                <th nowrap="nowrap" class="hide fengxianguanlifei">风险管理费</th> 
                <th nowrap="nowrap" class="hide fengxianguanlifei">比例</th> 
                <th nowrap="nowrap" class="hide jiekuanlvyuebaozhengjin">还款保证金</th> 
                <th nowrap="nowrap" class="hide rongzifuwufei">融资服务费</th> 
                <th nowrap="nowrap" class="hide rongzifuwufei">比例</th> 
                <th nowrap="nowrap" class="hide danbaofei">担保费</th> 
                <th nowrap="nowrap" class="hide danbaofei">比例</th> 
                <th nowrap="nowrap" class="hide xiweifei">席位费</th> 
				<th nowrap="nowrap" class="hide rongzijieyu">融资结余(￥)</th>  
                <th nowrap="nowrap" class="hide beizhu">备注</th> 
				</tr>
			
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${list}" var="entry">
			<tr> 
			
				<td nowrap="nowrap" class="qianyueriqi"><fmt:formatDate value="${entry.fb_qianyuedate}" pattern="yyyy-MM-dd"/></td> 
				<td nowrap="nowrap" class="fabaofang">${entry.cbo_coding}</td>
				<td nowrap="nowrap" class="bianhao">${entry.fb_code}</td>
				<td nowrap="nowrap" class="rongziren">
				<script>document.write(name("${entry.fm_name}"));</script>
				</td nowrap="nowrap">
				<td class="rongzie">
					<fmt:formatNumber value='${entry.fb_currenyamount}' type="currency" currencySymbol=""/>
				    <c:set value="${z_currenyAmount + entry.fb_currenyamount}" var="z_currenyAmount"/>  
				</td>
				<td nowrap="nowrap" class="qixian">
					<span style="color:#E50000;font-weight:bold;"> 
						<c:if test="${(entry.fb_interestday)!= 0}">按日计息(${entry.fb_interestday}天)</c:if>
						<c:if test="${(entry.fb_interestday)== 0}">${entry.fbt_term}</c:if>
					</span>
				</td>
				<td nowrap="nowrap" class="youwudanbao">
	               <c:if test="${(entry.fb_guarantee)!= null}">
					 有担保 
				   </c:if>
				  <c:if test="${(entry.fb_guarantee)== null}">
					 无担保 
				   </c:if>
				</td> 
				<td class="hide fengxianguanlifei" align="right">
					<c:set var="fxglf" value="${fn:split(entry.fc_fxglf,'|')}"/>
					<c:if test="${fxglf[1]==1}">[按月]</c:if>
					<fmt:formatNumber value="${fxglf[0]}" pattern="#,###,##0.00" />
					<c:set value="${z_fxglf + fxglf[0]}" var="z_fxglf"/>
				</td>
				<td class="hide fengxianguanlifei" align="right">
					<fmt:formatNumber value="${fxglf[2]}" pattern="#,###,##0.00" />%
				</td>
				<td class="hide jiekuanlvyuebaozhengjin" align="right">
					<c:set var="bzj" value="${fn:split(entry.fc_bzj,'|')}"/>
					<fmt:formatNumber value="${bzj[0]}" pattern="#,###,##0.00" />
					<c:set value="${z_bzj + bzj[0]}" var="z_bzj"/>
				</td>
				
				<td class="hide rongzifuwufei" align="right">
					<c:set var="rzfwf" value="${fn:split(entry.fc_rzfwf,'|')}"/>
					<c:if test="${rzfwf[1]==1}">[按月]</c:if>
					<fmt:formatNumber value="${rzfwf[0]}" pattern="#,###,##0.00" />
					<c:set value="${z_rzfwf + rzfwf[0]}" var="z_rzfwf"/>
				</td>
				<td class="hide rongzifuwufei" align="right">
					<fmt:formatNumber value="${rzfwf[2]}" pattern="#,###,##0.00" />%
				</td>
				<td class="hide danbaofei" align="right">
					<c:set var="dbf" value="${fn:split(entry.fc_dbf,'|')}"/>
					<c:if test="${dbf[1]==1}">[按月]</c:if>
					<fmt:formatNumber value="${dbf[0]}" pattern="#,###,##0.00" />
					<c:set value="${z_dbf + dbf[0]}" var="z_dbf"/>
				</td>
				<td class="hide danbaofei" align="right">
					<fmt:formatNumber value="${dbf[2]}" pattern="#,###,##0.00" />%
				</td>
					
					
				<td class="hide xiweifei" align="right">
			    	<fmt:formatNumber value='${entry.fc_xwf }' pattern="#,##0.00"/>
			    	<c:set value="${z_fee7 +entry.fc_xwf}" var="z_fee7"/>  
				</td> 
				
				<!--<td class="hide xinyongguanlifei" align="right">
					<fmt:formatNumber value='${entry.fc_xyglf}' pattern="#,##0.00"/>
					<c:set value="${z_fee10 +entry.fc_xyglf }" var="z_fee10"/>  
				</td> 
				   
				<td class="hide qitafeiyong" align="right">
					<c:set var="fc_other" value="${fn:split(entry.fc_other,'|')}"/>
					<c:if test="${fc_other[1]==1}">[按月]</c:if>
					<fmt:formatNumber value="${fc_other[0]}" pattern="#,###,##0.00" />
					<c:set value="${z_other +fc_other[0]}" var="z_other"/>  
				</td>
				-->
				<td nowrap="nowrap" class="hide rongzijieyu" align="right">
					<fmt:formatNumber value='${entry.fc_realamount}' pattern="#,##0.00"/>
					<c:set value="${z_realAmount +entry.fc_realamount}" var="z_realAmount"/> 
				</td> 
				<td class="hide beizhu">${entry.fc_note}</td>     
			</tr>
		</c:forEach>
		</tbody>
		<tfoot>
				<tr>
				<th class="qianyueriqi">合计</th>
				<th nowrap="nowrap" class="fabaofang">-</th>
				<th nowrap="nowrap" class="bianhao">-</th>
				<th nowrap="nowrap" class="rongziren">-</th>
				<th nowrap="nowrap" class="rongzie" align="right"><fmt:formatNumber value="${z_currenyAmount}" pattern="#,###,##0.00"/></th>
				<th nowrap="nowrap" class="qixian">-</th>
				<th nowrap="nowrap" class="youwudanbao">-</th> 
                <th nowrap="nowrap" class="hide fengxianguanlifei" align="right"><fmt:formatNumber value="${z_fxglf}" pattern="#,###,##0.00"/></th> 
                <th nowrap="nowrap" class="hide fengxianguanlifei">-</th> 
                <th nowrap="nowrap" class="hide jiekuanlvyuebaozhengjin" align="right"><fmt:formatNumber value="${z_bzj}" pattern="#,###,##0.00"/></th> 
                <th nowrap="nowrap" class="hide rongzifuwufei" align="right"><fmt:formatNumber value="${z_rzfwf}" pattern="#,###,##0.00"/></th> 
                <th nowrap="nowrap" class="hide rongzifuwufei">-</th> 
                <th nowrap="nowrap" class="hide danbaofei" align="right"><fmt:formatNumber value="${z_dbf}" pattern="#,###,##0.00"/></th> 
                <th nowrap="nowrap" class="hide danbaofei">-</th> 
                <th nowrap="nowrap" class="hide xiweifei" align="right"><fmt:formatNumber value="${z_fee7}" pattern="#,###,##0.00"/></th> 
                <th nowrap="nowrap" class="hide xinyongguanlifei" align="right"><fmt:formatNumber value="${z_fee10}" pattern="#,###,##0.00"/></th> 
                <th nowrap="nowrap" class="hide qitafeiyong" align="right"><fmt:formatNumber value="${z_other}" pattern="#,###,##0.00"/></th> 
				<th nowrap="nowrap" class="hide rongzijieyu" align="right"><fmt:formatNumber value="${z_realAmount}" pattern="#,###,##0.00"/></th>  
                <th nowrap="nowrap" class="hide beizhu">-</th> 
				</tr>
			
			</tfoot>
			
			 
		</table>
		<div style="float:right;line-height:30px;font-weight: bold">
			<div style="float:left;width:120px;">经办:</div>
			<div style="float:left;width:120px;">复核:</div>
			<div style="float:left;">日期:<fmt:formatDate value="${now}" pattern="yyyy/MM/dd HH:mm:ss"/></div>
		</div>
		</div>
	</body>
</html>
