 <%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 

<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %> 
<link href="/Static/js/tree/tabletree/styles.css" rel="stylesheet" type="text/css" /> 
<script type="text/javascript" src="/Static/js/tree/tabletree/jquery.treetable.js"></script>


 
<script type="text/javascript">   
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
	
			//回传值
		var api = frameElement.api, W = api.opener;
		
		api.button({
		    id:'valueOk',
			name:'确定',
			callback:ok
		});
		
		function ok()
		{ 
		   if($("input[name='xuanze']:checked").val() == undefined) { 
			    alert("请选择！"); 
			    return false;
		    }    
		    W.document.getElementById('companyPropertyId').value = $("input[name='xuanze']:checked").val();
		    W.document.getElementById('companyPropertyName').value = $("input[name='xuanze']:checked").attr("title");
		 }; 
</script>  
<body>
 
<form id="form1" action="/back/companyProperty/companyPropertyAction!listTree" method="post">  
  <div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content"  id="mytable" >  
		<thead>
			<tr class="ui-widget-header ">  
				<th>id</th>
				<th>名称</th>
				<th>所属父类</th> 
				<th>parentId</th>
				<th>编码</th>   
			</tr>
		</thead>
		<tbody id="tb" >
		<c:forEach items="${listTree}" var="entity"> 
			<tr> 
 			    <td>${entity.id}</td>
				<td><c:if test="${entity.leaf}"><input type='radio' name='xuanze' value='${entity.id}'  title='${entity.name}'/></c:if>${entity.name}</td>      
				<td><c:if test="${entity.parent!= null}">${entity.parent.name}</c:if></td> 
				<td><c:if test="${entity.parent!= null}">${entity.parent.id}</c:if></td>
				<td>${entity.showCoding}</td>  
 			</tr>
		</c:forEach> 
		</tbody>
	</table> 
</div>
</form>
</body>