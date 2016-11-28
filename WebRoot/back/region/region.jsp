<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>
<script>
$(function(){
	$("#form1").validate({
        rules: { 
          "region.areaname_l":{ required:true},
          "region.areaname_s":{ required:true}
        },  
        messages: {      
          "region.areaname_l":{ required:"请输入地点名称(长)"},
          "region.areaname_s":{ required:"请输入地点名称(短)"}
        }    
	});   
})
		
</script>
<form action='/back/regionAction!edit' id="form1">
<input type="hidden" value="${region_id}" name='region_id'/>
<input type="hidden" value="${region_parent_id}" name='region_parent_id'/>
	<table>
		<tr><td>地点名称(长):</td><td><input type="text" name="region.areaname_l" value="${region.areaname_l}" /></td></tr>
		<tr><td>地点名称(短):</td><td><input type="text" name="region.areaname_s" value="${region.areaname_s}" /></td></tr>
		<tr><td>地点编码:</td><td><input type="text" name="region.areacode" value="${region.areacode}" /></td></tr>
		<tr><td colspan='2' align='left'><button  class='ui-state-default'>提交</button></td></tr>
	</table>
</form>