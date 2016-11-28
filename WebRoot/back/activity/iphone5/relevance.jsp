<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<html>
	<head>
		<%@ include file="/common/import.jsp"%>
		<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
		<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script>
		<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css" />
		<script>
		$(function(){
        	var cache = {};
        	$( "#auto_un" ).autocomplete({
            	minLength: 6,
            	source: function( request, response ) {
                	var term = request.term;
                	if ( term in cache ) {
                    	response( cache[ term ] );
                    	return;
                	}
                	$.getJSON( "/back/activity/iphone5!autoc_un", request, function( data, status, xhr ) {
                    	cache[term] = data;
                    	response(data);
                	});
            	}
        	});
        	$(".end_table").datepicker({
				changeYear: true,
		        dateFormat: "yy-mm-dd"
		    });
		    $("#save_button").button().click(function(){
		    	 $("#form1").submit();
		    });
		    
		    $("#form1").validate({
		        rules: { 
		          "username":{ required:true},
		          "relation.from":{ required:true},
		          "relation.end":{ required:true}
		        },  
		        messages: {
		          "username":{ required:"请输入用户交易帐号"},
		          "relation.from":{ required:"请选择签约日期"},
		          "relation.end":{ required:"请选择结束日期"}
		        }    
			}); 
		});
		
		
				
		</script>
		<style>
		input,select {
			padding: 3px;
			width: 200px;
			outline: none;
			border: 1px solid #DDD;
		}
		
		input:hover,input:focus,select:hover,select:focus {
			border-color: #7DBDE2;
			box-shadow: 0 0 5px #7DBDE2;
		}
		
		#ui-datepicker-div {
			display: none;
			font-size: 13px;
			border-radius: 4px;
		}
		.ui-autocomplete{
			font-size:13px !important;
			float:left;
			list-style: none;
		}
		.ui-autocomplete li{
			cursor: default;
		}
		#save_button{
			font-size:13px;
			cursor:pointer;
		}
		.ui-datepicker .ui-datepicker-title select.ui-datepicker-year{float:left !important;}
		</style>

	</head>
	<body>
		<input type='hidden' class='autoheight' value="auto" />
		
		
		<div>
			<form action="/back/activity/iphone5!relevance_save" id="form1" method="post">
				<input type="hidden" value="${relation_id}" name="relation_id"/>
				<table>
					<tr>
						<td>用户交易帐号:</td><td><input type="text" name="username" value="${relation.account.user.username}" id="auto_un" <c:if test="${relation_id!=null}"> disabled="disabled"</c:if>/></td>
					</tr>
					<c:if test="${relation_id!=null}">
						<tr>
							<td>用户姓名:</td><td><input type="text" value="${relation.account.user.realname}" disabled="disabled" /></td>
						</tr>
					</c:if>
					</tr>				
					<tr>
						<td>适用政策:</td>
						<td>
							<s:select list="#request.rules" listKey="id" listValue="code" name="rule_id" value="#request.relation.rule.id"></s:select>
						</td>
					</tr>	
					<tr>
						<td>签约日期:</td>
						<td><input type="text" name="relation.from" class="end_table" readonly="readonly" value="<fmt:formatDate value="${relation.from}" pattern="yyyy-MM-dd"/>"  /></td>
					</tr>
					<tr>
						<td>结束日期:</td>
						<td><input type="text" name="relation.end" class="end_table" readonly="readonly" value="<fmt:formatDate value="${relation.end}" pattern="yyyy-MM-dd"/>" /></td>
					</tr>
					<tr>
						<td colspan="2"><span id="save_button">保存</span></td>
					</tr>				
				</table>
			</form>
		
		</div>
	</body>
</html>
