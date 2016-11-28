<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<div id="accordion" style="font-size: 10pt;">
		<s:iterator value="leftMenuMap" id="rMap">
			<h3>
				 <a href="#" mid1="<s:property value="#rMap.key.substring(#rMap.key.indexOf('-')+1)" />"><s:property value="#rMap.key.substring(0,#rMap.key.indexOf('-'))" /></a>
			</h3>
			<div>
			   <ul>
					<s:iterator value="#rMap.value" id="menu">
					     <li><a href="#" mid2="${menu.coding }" urlParam="" url="${menu.href}">${menu.name}</a></li> 
					</s:iterator>  
			   </ul>
		   </div>
	     </s:iterator> 
	
</div>