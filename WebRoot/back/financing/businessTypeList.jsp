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
  <div style="float: right; margin-right: 20px;">
    <button class="ui-state-default" onclick="toURL('/back/financing/businessTypeAction!ui');return false;" >新建</button>
  </div>		
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header "> 
				<th>类型名称</th>
				<th>类型标识</th>
				<th>期限(月数)</th>
				<th>还款方式</th>
				<th>风险管理费(无担保)</th>
				<th>还款次数</th>
				<th width="15%">操作</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${pageView.records}" var="businessType">
			<tr> 
				<td>${businessType.name}</td>
				<td>${businessType.code}</td>
				<td>${businessType.term}</td> 
				<td>${businessType.returnPattern}</td>
				<td>${businessType.fxglf}%</td>
				<td>${businessType.returnTimes}</td>
				<td><button onclick="toURL('/back/financing/businessTypeAction!ui?id=${businessType.id}'); return false;" class="ui-state-default" style="display:<c:out value="${menuMap['user_Edit']}" />">修改</button><span style="clear:both" ></span></td>
			</tr>
		</c:forEach>
		</tbody><tbody>  
			<tr>
				<td colspan="8">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tbody>
	</table> 
</div>
</form>
</body> 