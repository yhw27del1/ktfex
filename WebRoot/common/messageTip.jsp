<%@ page language="java" pageEncoding="UTF-8"%>  
<link href="/Static/js/jgrowl/jquery.jgrowl.css" rel="stylesheet" type="text/css"/> 
<script src="/Static/js/jgrowl/jquery.jgrowl_minimized.js" type="text/javascript"></script> 
<script type="text/javascript">  
   function showMessageTip()
   {
	   if("${session.MESSAGE}" !=""){ 
	       $.jGrowl("${session.MESSAGE}", { position:'top-right',header:'操作提示：' });
	     } 
    }
    showMessageTip(); 
</script>
 <% session.removeAttribute("MESSAGE");  %>   
 