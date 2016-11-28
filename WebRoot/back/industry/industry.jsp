<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<html>
  <head>
    
    <title>行业</title>  
	<script type="text/javascript" src="/Static/js/autoheight.js"></script>
	<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
	<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
	<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>   
	 
	<script type="text/javascript">
		 $(function() {    
		   $("#setCheckedMenuForm").validate({
		                rules: { 
		                  "industry.name":{ required:true} 
		                },  
		                messages: {      
		                  "industry.name":{ required:"请输入行业名称"} 
		                }    
		        });   
		})
	
		$(document).ready(function(){
			$("#submitMenu").click(function() {
				$("#setCheckedMenuForm").submit();
			}); 
			
			<c:if test="${id==0}">
	 	       // setTitle2("新增行业"); //重新设置切换tab的标题
		    </c:if>  
		    <c:if test="${id!=0}"> 
			    //setTitle2("修改行业"); //重新设置切换tab的标题
		    </c:if>    
		});
	</script>
  </head>
  
  <body>
    <form action="/back/industry/industryAction!edit" method="post" id="setCheckedMenuForm">  
		    <input type='hidden' class='autoheight' value="auto" />  
     	<input type="hidden" name="id" value="${id}"/>   
	       
	        <table border="0">
            <tr><td align="right"><span style="color:red">*</span>行业名称：</td>
                  <td> <input  name="industry.name"  type="text"  value="${industry.name}"/> </td> 
              </tr>  
            <tr><td align="right">行业描述：</td>
            <td><input  name="industry.note"  type="text"  value="${industry.note }"/>  </td> </tr>
  
    	</table> 
     	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button class="ui-state-default" id="submitMenu" >保存</button>
    </form>
 
	
  </body>
</html>
