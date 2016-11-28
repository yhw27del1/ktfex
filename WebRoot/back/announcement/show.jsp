<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script>

function opendialog(id){
	//$("#"+id).dialog({
	//	modal: true,
	//	resizable:false
	//});
}
</script>
         
		<MARQUEE scrollAmount="5" direction="left" onmousemove="this.stop()" onmouseout="this.start()" style="background-image: none;">
 		<s:iterator value="notices" var="notice">
			<a href="/sys_/notice/noticeAc!detail?id=${notice.id}"  target="_blank" style="font-size:12px;text-decoration:none" title='个人通知'><img src="/Static/images/newtemplate/hi.gif" style='height:10px;'/>&nbsp;<span>${notice.title}</span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</s:iterator>
		<s:iterator value="announcements" var="ann">
			<a href="/back/announcement/announcementAction!detail?announcement_id=${ann.id}" onclick="opendialog('${ann.id}')" target="_blank" title='公告' style="font-size:12px;text-decoration:none"><span>${ann.title}</span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</s:iterator>
		</MARQUEE>

