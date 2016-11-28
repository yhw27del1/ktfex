<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<html>
	<head>
		<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
		<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script>
		<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css" />
		<script>
		$(function(){
        	var cache = {};
        	$("#auto_un").autocomplete({
            	minLength: 6,
            	source: function( request, response ) {
                	var term = request.term;
                	if ( term in cache ) {
                    	response( cache[ term ] );
                    	return;
                	}
                	$.getJSON( "/back/activity/lend!autoc_un", request, function( data, status, xhr ) {
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
		          "relation.end":{ required:"请选择到期日期"}
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
		<div>
			<form action="/back/activity/lend!saveOrUpdate" id="form1" method="post">
				<input type="hidden" value="${id}" name="id"/>
				<table>
					<tr>
						<td>用户交易帐号:</td>
						<td><input type="text" name="username" value="${lend.user.username}" id="auto_un" <c:if test="${id!=null}"> disabled="disabled"</c:if> /></td>
					</tr>
					<c:if test="${id!=null}">
						<tr>
							<td>用户姓名:</td><td><input type="text" value="${lend.user.realname}" disabled="disabled" /></td>
						</tr>
					</c:if>
					</tr>
					<tr>
						<td>限制金额:</td><td><input type="text" name="lend.money" value="${lend.money}" value="${lend.money}" /></td>
					</tr>			
					<tr>
						<td>签约日期:</td>
						<td><input type="text" name="lend.startDate" class="end_table" readonly="readonly" value="<fmt:formatDate value="${lend.startDate}" pattern="yyyy-MM-dd"/>"  /></td>
					</tr>
					<tr>
						<td>到期日期:</td>
						<td><input type="text" name="lend.endDate" class="end_table" readonly="readonly" value="<fmt:formatDate value="${lend.endDate}" pattern="yyyy-MM-dd"/>" /></td>
					</tr>
					<tr>
						<td>备注:</td>
						<td>
							<c:if test="${id!=null}"><input type="text" name="lend.memo" value="${lend.memo}" /></c:if>
							<c:if test="${id==null}"><input type="text" name="lend.memo" value="投转贷" /></c:if>
						</td>
					</tr>
					<tr>
						<td colspan="2"><span id="save_button">保存</span></td>
					</tr>				
				</table>
			</form>
		
		</div>
	</body>
</html>
