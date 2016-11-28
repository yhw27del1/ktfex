<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<link href="/Static/js/tree/tabletree/styles.css" rel="stylesheet" type="text/css" /> 
<script type="text/javascript" src="/Static/js/tree/tabletree/jquery.treetable.js"></script> 
<script type="text/javascript">
function toURL(url){  
   window.location.href = url;  
}
function del(id){ 
        var dlgHelper = new dialogHelper();
        dlgHelper.set_Title("确认要删除现有公司性质吗？");
        dlgHelper.set_Msg("执行这个操作，此公司性质将被删除，你确认要这么做吗？");
        dlgHelper.set_Height("180");
        dlgHelper.set_Width("650");
        dlgHelper.set_Buttons({
            '确定': function(ev) {
                window.location.href = "/back/companyProperty/companyPropertyAction!del?id="+id;
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
		$("#seachButton").click(function() {
				$("#form1").submit();
		}); 
});
$(document).ready(function(){  
		$.treetable.defaults={
			id_col:0,//ID td列 {从0开始}
			parent_col:3,//父ID td列
			handle_col:1,//加上操作展开操作的 td列
			open_img:"/Static/js/tree/tabletree/images/minus.gif",
			close_img:"/Static/js/tree/tabletree/images/plus.gif"
		};
		   
		$("#tb").treetable();
		
		//应用样式
		$("#tb tr:even td").addClass("alt");
		$("#tb tr").find("td:eq(1)").addClass("spec");
		$("#tb tr:even").find("td:eq(1)").removeClass().addClass("specalt");
		
		
		//隐藏数据列,只用于运算，显示出来没有意义
		$("#tb tr").find("td:eq(0)").hide();
		$("#tb tr").find("td:eq(3)").hide();
		$("#mytable tr:eq(0)").find("th:eq(0)").hide();
		$("#mytable tr:eq(0)").find("th:eq(3)").hide(); 
		
		

	}); 
</script>
<body> 
<form id="form1" action="/back/companyProperty/companyPropertyAction!list" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
<!-- 
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
  	<button class="ui-state-default"  onclick="toURL('/back/companyProperty/companyPropertyAction!ui');return false;"  style="display:<c:out value="${menuMap['companyProperty_add']}" />">新增</button> 
	关键字&nbsp;<input type="text" name="keyWord" value="${keyWord}"/>
	<button class="ui-state-default"  id="seachButton">搜索</button>
</div> -->

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content"  id="mytable" >
		<thead>
			<tr class="ui-widget-header "> 
				<th>id</th>
				<th>名称</th>
				<th>所属父类</th> 
				<th>parentId</th>
				<th>编码</th>   
				<th width="18%">操作</th>
			</tr>
		</thead>
		<tbody  id="tb">
		<c:forEach items="${list}" var="entity">
			<tr> 
 			    <td>${entity.id}</td>
				<td>${entity.name}</td>      
				<td><c:if test="${entity.parent!= null}">${entity.parent.name}</c:if></td> 
				<td><c:if test="${entity.parent!= null}">${entity.parent.id}</c:if></td>
				<td>${entity.showCoding}</td>  
				<td><button    class="ui-state-default"  onclick="toURL('/back/companyProperty/companyPropertyAction!ui?id=${entity.id}');return false;"  style="display:<c:out value="${menuMap['companyProperty_edit']}" />">修改</button>&nbsp;<!-- <button  class="ui-state-default"  onclick="del('${entry.id}');return false;"  style="display:<c:out value="${menuMap['companyProperty_del']}" />">删除</button> --></td>
			</tr>
		</c:forEach>
		</tbody> 
	</table> 
</div> 
</form>
</body>
