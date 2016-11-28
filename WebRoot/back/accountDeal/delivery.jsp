<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript">
$(function(){
	$(".table_solid").tableStyleUI();
	setIframeHeight(100);
});
function toURL(url){ 
   window.location.href = url; 
}
$(document).ready(function(){ 
	//setTitle2("融资费用"); //重新设置切换tab的标题  
});
function delivery(url){ 
    var dlgHelper = new dialogHelper();
    dlgHelper.set_Title("您要交割此融资项目吗？");
    dlgHelper.set_Msg("执行此操作后，可交割额将进入融资方账户中<br />系统账户的资金相应地减少。");
    dlgHelper.set_Height("220");
    dlgHelper.set_Width("660");
    dlgHelper.set_Buttons({
        '确定': function(ev) {
                //window.location.href = url;
                $(this).dialog('close');
                $.getJSON(url,function(data){
					if(data=='1'){
						alert("交割成功");
					}else{
						alert("交割失败，请稍后再试。");
					}
					window.location.href = "/back/accountDeal/accountDealAction!delivery";
                });
        },
        '取消': function() {
            //这里可以调用其他公共方法。
            $(this).dialog('close');
        }
    });
    dlgHelper.open(); 
}
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script> 
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>

<body class="ui-widget-header">
<form id="form1" action="/back/accountDeal/accountDealAction!delivery" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header "> 
				<th>编号</th>
				<th>融资方</th>
				<th>资金帐号</th>
				<th>融资额(￥)</th>
				<th>担保费(￥)</th>
				<th>风险管理费(按月)(￥)</th>
				<th>融资服务费(￥)</th>
				<th>保证金(￥)</th>
				<th><!-- 融资结余 -->可交割额(￥)</th>  
				<th width="6%">操作</th>
			</tr>
		</thead>
		<tbody  class="table_solid">
		<c:forEach items="${pageView.records}" var="entry">
			<tr> 
				<td><a href="#" onclick="toURL('/back/financingBaseAction!detail?id=${entry.financingBase.id}');return false;"  class="tooltip" title="${entry.financingBase.shortName}">${entry.financingBase.code}</a></td>
				<td>${entry.financier.eName}</td>
				<td>${entry.financier.bankAccount}</td>
				<td><fmt:formatNumber value='${entry.financingBase.currenyAmount}' type="currency" currencySymbol=""/></td>
				<td><fmt:formatNumber value='${entry.dbf}' type="currency" currencySymbol=""/></td>
				<td><fmt:formatNumber value='${entry.fxglf}' type="currency" currencySymbol=""/></td>
				<td><fmt:formatNumber value='${entry.rzfwf}' type="currency" currencySymbol=""/></td>
				<td><fmt:formatNumber value='${entry.bzj}' type="currency" currencySymbol=""/></td>
                <td><span style="color:red;"><fmt:formatNumber value='${entry.realAmount}' type="currency" currencySymbol=""/></span></td> 
				<td><button onclick="delivery('/back/accountDeal/accountDealAction!doDelivery?id=${entry.id}');return false;" class="ui-state-default" >交割</button></td>   
			</tr>
		</c:forEach>
			<tr>
				<td colspan="10">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tbody>
	</table> 
</div>
</form>
</body> 