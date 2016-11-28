<%@ taglib prefix="s" uri="/struts-tags"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="member_state_not_audited" value="<%=com.kmfex.model.MemberBase.STATE_NOT_AUDIT %>" />

