<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/jquery.form.js"></script>
<style>
.enable_radio{
	font-size:13px;
	padding:0 5px 0 5px;
}
.ui-helper-hidden-accessible{
	display:none;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$(".table_solid").tableStyleUI();
		$( ".enable_radio" ).buttonset();
	});
	function setenable(id,value){
		$.post("/back/tradeTime/kmfexTradeMarketAction!setEnable",{"id":id,"value":value},function(data,status,xml){
			window.location = "/back/tradeTime/kmfexTradeMarketAction!rules";
		});
	}
</script>
<body>
	<input type='hidden' class='autoheight' value="auto" />
	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
		<div style="float: right;">
			<button class="ui-state-default reflash" id="reflash">
				刷新
			</button>
			<button class="ui-state-default"
				onclick="javascript:window.location.href = '/back/tradeTime/kmfexTradeMarketAction!addRules';return false;">
				新增
			</button>
		</div>
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content" border='1px;'>
			<thead>
				<tr class="ui-widget-header ">
					<th>
						标题
					</th>
					<th>
						建立时间
					</th>
					<th>
						启用时间
					</th>
					<th>
						停用日期时间
					</th>
					<th style="width:100px;">
						启用状态
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${rules}" var="iter">
					<tr <c:if test="${iter.enabled}"> style="color:red;font-weight:bold"</c:if>>
						<td>
							${iter.title}
						</td>
						<td>
							<fmt:formatDate value="${iter.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</td>
						<td>
							<fmt:formatDate value="${iter.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</td>
						<td>
							<fmt:formatDate value="${iter.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</td>
						<td>
							<div class="enable_radio">
						        <input type="radio" id="radio_${iter.id}_x" name="radio_${iter.id}" <c:if test="${iter.enabled}">checked="checked"</c:if>/><label for="radio_${iter.id}_x" class="enable_radio"  onclick="setenable('${iter.id}',true)">启用</label>
						        <input type="radio" id="radio_${iter.id}_y" name="radio_${iter.id}" <c:if test="${!iter.enabled}">checked="checked"</c:if> /><label for="radio_${iter.id}_y" class="enable_radio" onclick="setenable('${iter.id}',false)">禁用</label>
						    </div>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>