<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript">
function toURL(url){  
   window.location.href = url;  
}
function del(id){ 
        var dlgHelper = new dialogHelper();
        dlgHelper.set_Title("确认要删除现有行业吗？");
        dlgHelper.set_Msg("执行这个操作，此行业将被删除，你确认要这么做吗？");
        dlgHelper.set_Height("180");
        dlgHelper.set_Width("650");
        dlgHelper.set_Buttons({
            '确定': function(ev) {
                window.location.href = "/back/industry/industryAction!del?id="+id;
                $(this).dialog('close');
            },
            '取消': function() {
                //这里可以调用其他公共方法。
                $(this).dialog('close');
            }
        });
        dlgHelper.open(); 
  
}
$(document).ready(function(){    
        $(".table_solid").tableStyleUI();       
		//setTitle2("行业管理"); //重新设置切换tab的标题 
		$("#seachButton").click(function() {
				$("#form1").submit();
		}); 
});
</script>
<body> 
<form id="form1" action="/back/industry/industryAction!list" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
  	<button class="ui-state-default"  onclick="toURL('/back/industry/industryAction!ui');return false;"  style="display:<c:out value="${menuMap['industry_add']}" />">新增</button> 
	关键字&nbsp;<input type="text" name="keyWord" value="${keyWord}"/>
	<button class="ui-state-default"  id="seachButton">搜索</button>
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header "> 
				<th>名称</th>
				<th>描述</th> 
				<th width="18%">操作</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${pageView.records}" var="entry">
			<tr> 
				<td>${entry.name}</td>
				<td>${entry.note}</td> 
				<td><button    class="ui-state-default"  onclick="toURL('/back/industry/industryAction!ui?id=${entry.id}');return false;"  style="display:<c:out value="${menuMap['industry_edit']}" />">修改</button>&nbsp;<button  class="ui-state-default"  onclick="del('${entry.id}');return false;"  style="display:<c:out value="${menuMap['industry_del']}" />">删除</button></td>
			</tr>
		</c:forEach>
		</tbody>
		<tbody>
			  <tr>
				 <td colspan="8"><jsp:include page="/common/page.jsp"></jsp:include></td>
			   </tr> 
		</tbody>
	</table> 
</div> 
</form>
</body>
