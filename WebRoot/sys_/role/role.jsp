<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<html>
  <head>
    
    <title>角色授权</title> 
    <script type="text/javascript" src="/Static/js/tree/dhtmlXTree/dhtmlxcommon.js"></script>
    <script type="text/javascript" src="/Static/js/tree/dhtmlXTree/dhtmlxtree.js"></script>
	<link rel="stylesheet" type="text/css" href="/Static/js/tree/dhtmlXTree/dhtmlxtree.css">
	<script type="text/javascript" src="/Static/js/autoheight.js"></script>
	<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
	<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
	<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>   
	 
	<script type="text/javascript">
		 $(function() {    
		   $("#setCheckedMenuForm").validate({
		                rules: { 
		                  "role.name":{ required:true} 
		                },  
		                messages: {      
		                  "role.name":{ required:"请输入角色名称"} 
		                }    
		        });   
		})
	
		$(document).ready(function(){
			$("#submitMenu").click(function() {
				$("#setCheckedMenuForm").submit();
			});
			$("#setCheckedMenuForm").submit(function(){
				$("#menuIds").val(tree.getAllCheckedBranches());
			});
			
			<c:if test="${id==0}">
	 	       // setTitle2("新增角色"); //重新设置切换tab的标题
		    </c:if>  
		    <c:if test="${id!=0}"> 
			    //setTitle2("修改角色"); //重新设置切换tab的标题
		    </c:if>  
		});
	</script>
  </head>
  
  <body>
    <form action="/sys_/roleAction!edit" method="post" id="setCheckedMenuForm">  
		    <input type='hidden' class='autoheight' value="auto" /> 
		    <input type="hidden" name="menuIds" id="menuIds"/> 
     	<input type="hidden" name="id" value="${id}"/>   
	       
	        <table border="0">
            <tr><td align="right"><span style="color:red">*</span>角色名称：</td>
                  <td> <input  name="role.name"  type="text"  value="${role.name}"/> </td> 
              </tr>  
            <tr><td align="right">角色描述：</td>
            <td><input  name="role.desc"  type="text"  value="${role.desc }"/>  </td> </tr>
             <tr><td align="right">角色授权 :</td><td><div id="treebox_tree" style="width:500px;border:0px  ">
    	       </div></td> </tr>     
    	</table> 
     	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button class="ui-state-default" id="submitMenu" >保存</button>
    </form>
    <script type="text/javascript">  
		tree = new dhtmlXTreeObject("treebox_tree","500","100%",0);
		tree.setSkin('dhx_skyblue');
		tree.setImagePath('/Static/js/tree/dhtmlXTree/imgs/csh_bluebooks/');
		tree.enableCheckBoxes(true);  
		tree.enableThreeStateCheckboxes(true);  
		tree.loadXML("/sys_/roleAction!getMenuTree?id=${id}");
		setIframeHeight(2000);    
	</script>
	
  </body>
</html>
