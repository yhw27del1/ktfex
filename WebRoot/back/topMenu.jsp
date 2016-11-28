<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication property="principal" var="authentication"/>
<script type="text/javascript">
	setInterval("$('#jnkc').text(new Date().toLocaleString()+' 星期'+'日一二三四五六'.charAt(new Date().getDay()));",1000);
</script>
<div class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" style="font-size:10pt;padding-top:2px;padding-left:2px;">
	<span id="showFlag" state="show" style="cursor: pointer;"><a href="#">隐藏菜单</a></span>&nbsp;
	欢迎您：${authentication.username}!&nbsp;
	<span>换主题</span>
	<select id="themeSwitch">
		<option selected value="excite-bike">
			-- 默认皮肤
		</option>
		<option value="smoothness">
			-- 灰色地带
		</option>
		<option value="south-street">
			-- 绿色梦幻
		</option>
		<option value="start">
			-- 蓝绿相间
		</option>
		<option value="ui-darkness">
			-- 黑色幽默
		</option>
		<option value="black-tie">
			-- 黑白交替
		</option>
	</select>
	&nbsp; 
	<a href="j_spring_security_logout">[退出]</a>&nbsp;
	<span id="jnkc">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;
</div>