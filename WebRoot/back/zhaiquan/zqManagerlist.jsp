<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
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
	
	$("#queryForm1").click(function(){
		$("#form1").submit();  
	});
	$('.agreement').click(function(){
		window.showModalDialog("/back/investBaseAction!agreement_ui2?invest_record_id="+$(this).attr("id"), null, "dialogWidth:1000px;dialogHeight:auto;status:no;help:yes;resizable:no;");
		$("input.ui-state-default").trigger('click');
	});
});

function oper(investRecordId,opeState,title){ 
        var dlgHelper = new dialogHelper();
        dlgHelper.set_Title("提示信息");
        dlgHelper.set_Msg("您要"+title+"吗?");
        dlgHelper.set_Height("180");
        dlgHelper.set_Width("650");
        dlgHelper.set_Buttons({
            '确定': function(ev) {
                    //window.location.href = url;
                    //$(this).dialog('close'); 
                    $("#form1").attr("action","/back/zhaiquan/zhaiQuanInvestAction!upZqzrState");
                    $("#investRecordId").val(investRecordId);
                    $("#opeState").val(opeState);
                    $("#form1").submit();  
                    
            },
            '取消': function() {
                //这里可以调用其他公共方法。
                $(this).dialog('close');
            }
        });
        dlgHelper.open(); 

}
		function show(url){
	    window.showModalDialog(url, null, "dialogWidth:800px;dialogHeight:auto;status:no;help:yes;resizable:no;");
    }
</script>
<style type="text/css">
.s_table td {
	background-color: #e3e3e3
}
.agreement{cursor:pointer;}
.agreement:HOVER{text-decoration: underline;}
</style>
<body class="ui-widget-header" style="font-size: 8pt; color: black">
	<input type='hidden' class='autoheight' value="auto" />
	<form action="/back/zhaiquan/zhaiQuanInvestAction!list" id="form1"> 
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
		查询条件:&nbsp;关键字<input type="text" value="${querykeyWord}" name="querykeyWord">&nbsp;
				日期段：&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>" id="startDate"/>到<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>" id="endDate"/>
                        状态:<s:select name="zqzrState" list="paiList" listKey="string1" listValue="string2" headerKey="" headerValue="全部"/>
				<input class="ui-state-default" type="submit" value="查询" >
	</div>
	<input type="hidden" name="investRecordId" id="investRecordId"/> 
	<input type="hidden" name="opeState" id="opeState"/> 
	<input type="hidden" name="page" value="1" />
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						债权代码
					</th>
					 <th>
						持有人编号
					</th>   
					 <th>
						持有人姓名
					</th>
					<th>
					           债权期末值
					</th>
					<th>
					          债权成本价
					</th>  
					<!--  <th>
						当前参考收益率(￥)
					</th> -->
					<!-- <th>
						本金(￥)
					</th>
					<th>
						利息(￥)
					</th>
					<th>
						转让标的
					</th> -->  
					<th>
						下一还款日
					</th> 
					<th>
						状态
					</th> 
					<th>
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="entry">
					<tr>
						 <td>
							  <a href="javascript:void(0)" onclick="show('/back/zhaiquan/zhaiQuanInvestAction!detail?zhaiQuanId=${entry.id}')">${entry.zhaiQuanCode}</a>
 						</td>
						<td>
							${entry.investor.user.username}
						</td>  
						<td>
							<script>document.write(name("${entry.investor.pName}"));</script>
						</td> 
						<!--  <td>
							<fmt:formatNumber value='${entry.investAmount}' type="currency" currencySymbol=""/> 
						</td> --> 
						<td>						
						     <fmt:formatNumber value='${entry.qmz}' type="currency" currencySymbol="" />
						</td>
						<td>
						     <fmt:formatNumber value='${entry.cbj}' type="currency" currencySymbol="" />
						</td>
						<!--  <td>
							<fmt:formatNumber value='${entry.bjye}' type="currency" currencySymbol="" /> 
						</td>  
						<td>
							<fmt:formatNumber value='${entry.lxye}' type="currency" currencySymbol="" /> 
						</td>  
						 <td>
							<fmt:formatNumber value='${entry.qmz}' type="currency" currencySymbol="" /> 
						</td>-->  
						<td> 
								 <fmt:formatDate value='${entry.xyhkr}' pattern="yyyy-MM-dd"/>
						</td>  
						<td> 
								<c:if test="${entry.zqzrState=='0'}"><span style="color:#4169E1;">待挂牌</span></c:if>
								<c:if test="${entry.zqzrState=='1'}"><span style="color:green;">挂牌</span></c:if>
								<c:if test="${entry.zqzrState=='2'}"><span style="color:red;">停牌 </span></c:if>
								<c:if test="${entry.zqzrState=='3'}"><span style="color:#4169E1;">挂牌</span></c:if>
								<c:if test="${entry.zqzrState=='4'}"><span style="color:green;">摘牌</span></c:if> 
						</td> 

							<td> 
								<c:if test="${entry.zqzrState=='0'}">
								   
								</c:if>
								<c:if test="${entry.zqzrState=='1'}">
								     <button onclick="oper('${entry.id}','2','停牌');return false;" class="ui-state-default" >停牌</button>&nbsp;&nbsp;&nbsp;
								     <button onclick="oper('${entry.id}','4','摘牌');return false;" class="ui-state-default" >摘牌</button>
								</c:if>
								<c:if test="${entry.zqzrState=='2'}">
								     <button onclick="oper('${entry.id}','3','复牌');return false;" class="ui-state-default" >复牌</button>&nbsp;&nbsp;&nbsp;
								     <button onclick="oper('${entry.id}','4','摘牌');return false;" class="ui-state-default" >摘牌</button>
								</c:if>
								<c:if test="${entry.zqzrState=='3'}">
									  <button onclick="oper('${entry.id}','1','重新挂牌');return false;" class="ui-state-default" title="重新挂牌">挂牌</button>&nbsp;&nbsp;&nbsp;
									  <button onclick="oper('${entry.id}','2','停牌');return false;" class="ui-state-default" >停牌</button>&nbsp;&nbsp;&nbsp;
								      <button onclick="oper('${entry.id}','4','摘牌');return false;" class="ui-state-default" >摘牌</button>
                                </c:if>
								<c:if test="${entry.zqzrState=='4'}">
								
                               </c:if> 
						</td> 					
						
					</tr>
				</c:forEach>
				<tr>
					<td colspan="9">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
				</tr>
			</tbody>
		</table>
	</div>
	</form>
	<%@ include file="/common/messageTip.jsp" %>
</body>
