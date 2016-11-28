<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
	function toURL(url) {
		window.location.href = url;
	}
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript">
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
});
$(function() {
		$("#jsbl").focus();
		$("#sfjs").click(function() {
			var jsbl = $("#jsbl").val();
			if (jsbl == "") {
				alert("请输入结算比例");
				$("#jsbl").focus();
				return false;
			} else {
				//alert("结算比例为："+jsbl+"%");
				//alert("三方账户余额为："+$('#sfbalance').text());
				//alert("中心账户所得金额为："+$('#sfbalance').text()*(jsbl/100));
				$.getJSON("/back/accountDeal/accountDealAction!thirdAccount_js_do?time="+ new Date().getTime()+ "&jsbl="+ jsbl,function(data){
					if(data.message=='success'){
						alert("三方结算成功");
					}else{
						alert(data.message);
					}
					toURL(window.location.href);
				});
			}
		});
	});
</script>
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				兴易贷账户余额：<span id="sfbalance"><fmt:formatNumber value='${account.balance}' type="currency" currencySymbol=""/></span>元&nbsp;
	</div>
	<table>
		<tbody>
			<tr id="chargeInput">
				<td style="width: 50%;">
					<ul>
						<li>三方账户结算是指第三方账户(如兴易贷账户)与中心账户之间的金额分配</li>
						<li>将第三方账户余额安照一定的结算比例进行计算后划入中心账户，即中心账户所得金额</li>
						<li>中心账户所得金额  = 第三方账户金额 * 结算比例</li>
						<li style="list-style-type: none;">
							&nbsp;
						</li>
						<li style="list-style-type: none;">
							结算比例：
							<input type="text" name="jsbl" size="20" onkeyup="this.value=this.value.replace(/[^\d.]/g,'');" id="jsbl" />
							<!-- <button id="sfjs" class="ui-state-default mybutton">三方结算</button> -->
							<button id="sfjs" class="ui-state-default" style="display:<c:out value="${menuMap['sfjs']}" />" >三方结算</button>
						</li>
					</ul>
				</td>
			</tr>
		</tbody>
	</table>
</body>
