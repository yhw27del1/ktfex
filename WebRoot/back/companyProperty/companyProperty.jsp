<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<html>
  <head>
    
    <title>公司性质</title>  
	<script type="text/javascript" src="/Static/js/autoheight.js"></script>
	<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
	<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
	<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>   
	 
	<script type="text/javascript">
		 $(function() {    
		   $("#setCheckedMenuForm").validate({
		                rules: { 
		                  "companyProperty.name":{ required:true} 
		                },  
		                messages: {      
		                  "companyProperty.name":{ required:"请输入名称"} 
		                }    
		        });   
		})
	
		$(document).ready(function(){
			$("#submitMenu").click(function() {
				$("#setCheckedMenuForm").submit();
			}); 
			<c:if test="${id==0}">
	 	      //  setTitle2("新增公司性质"); //重新设置切换tab的标题
		    </c:if>  
		    <c:if test="${id!=0}"> 
			   // setTitle2("修改公司性质"); //重新设置切换tab的标题
		    </c:if>  
		});
	</script>
  </head>
  
  <body>
    <form action="/back/companyProperty/companyPropertyAction!edit" method="post" id="setCheckedMenuForm">  
		    <input type='hidden' class='autoheight' value="auto" />  
     	<input type="hidden" name="id" value="${id}"/>   
	        <table border="0">
            <tr><td align="right"><span style="color:red">*</span>名称：</td>
                  <td> <input  name="companyProperty.name"  type="text"  value="${companyProperty.name}"/> </td> 
              </tr>  
            <tr><td align="right">描述：</td>
            <td><input  name="companyProperty.note"  type="text"  value="${companyProperty.note }"/>  </td> </tr>
  
    	</table> 
     	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button class="ui-state-default" id="submitMenu" >保存</button>
    </form>
 
	
  </body>
</html>
