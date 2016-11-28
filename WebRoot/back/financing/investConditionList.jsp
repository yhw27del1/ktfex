<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript">   
function toURL(url){ 
   window.location.href = url; 
}
$(document).ready(function(){
    $(".table_solid").tableStyleUI();  
});
//function del(id){ 
//   window.location.href = "/member/memberAction!del?id="+id; 
//}
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script> 
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<body><input type='hidden' class='autoheight' value="auto" /> 
<form action="">
	<input type="hidden" name="page" value="1" />
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
  <div style="float: left;">
            关键字&nbsp;<input type="text" name="keyWord" value="${keyWord}"/>
	<button class="ui-state-default">查找</button>
  </div>
  <div style="float: right; margin-right: 20px;"><button onclick="toURL('/back/financing/investConditionAction!ui');return false;" class="ui-state-default" >新增</button></div>	
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header "> 
			    <th>会员类型</th>
			    <th>会员级别</th>
				<th>单笔最低投标额</th>
				<th>最高投标额百分比(%)</th>
				<th width="15%">操作</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${pageView.records}" var="iter">
			<tr> 
			    <td>${iter.memberLevel.memberType.name}</td>
			    <td>${iter.memberLevel.levelname}</td>
				<td>${iter.lowestMoney}</td>
				<td>${iter.highPercent}</td>
				<td><button onclick="toURL('/back/financing/investConditionAction!ui?investcondition_id=${iter.id}'); return false;" class="ui-state-default" >修改</button><span style="clear:both" ></span></td>
			</tr>
		</c:forEach>
		</tbody><tbody>
			<tr>
				<td colspan="5">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tbody>
	</table> 
</div>
</form>
</body> 