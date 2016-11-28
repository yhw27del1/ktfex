<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>投资会员月度理财报告</title>
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/metro/easyui.css">
    	<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/icon.css">
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/easyui-lang-zh_CN.js"/></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<style>
			body{padding:0;margin:0;overflow:hidden !important;}
			.combo{height:20px;background-color: #fff;}
			#win{padding:10px;}
		</style>
	</head>
	<body>
		<table id="dg" width="100%" class="easyui-datagrid"
            data-options="rownumbers:true,
            singleSelect:true,
            method:'post',
            toolbar:'#tb',
            pagination:true,
            pageList:[15,30,50,100],
            sortName:'ZZC',
            sortOrder:'desc'
            ">
            <thead>
				<tr>
					<th data-options="field:'USER_USERNAME',sortable:true,width:80">交易号</th>
		        	<th data-options="field:'NAME',width:80,formatter:name_formatter">姓名</th>
		        	<th data-options="field:'MEMBER_LEVEL',width:60">会员级别</th>
		        	<th data-options="field:'ORGNAME',width:60">开户机构</th>
		        	<th data-options="field:'STATE',width:60,formatter:state_formatter">状态</th>
		        	<th data-options="field:'ZZC',width:120,sortable:true,formatter:float_number_formatter">总资产</th>
		        	<th data-options="field:'operat',width:80,formatter:operator_formatter">查看理财报告</th>
				</tr>
	        </thead>
	        
	    </table>
	    
	    
	    <div id="tb" style="padding:5px;height:auto">
	        <div>
	           	<input placeholder="会员交易号" style="width:100px" class="combo" name="username" id="username" />
	           	状态:<select class="easyui-combobox" data-options="editable:false" name="state" id="state">
	        		<option value="">不限</option>
	        		<option value="1">未审核</option>
	        		<option value="2">正常</option>
	        		<option value="3">审核未通过</option>
	        		<option value="4">停用</option>
	        		<option value="5">注销</option>
	        	</select>&nbsp;
	        	<a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
	        </div>
	    </div>
	    
	    <div id="win" title="选择日期">
	    </div>
	</body>
</html>
<script>
    		function showdialog(username){
    			$('#win').dialog({
    				width:350,
				    modal:true,
				    href:'/back/statistics/select_date_for_mouth_lc_report.jsp?username='+username
				});
			}
			function cur_mon_rp(username){
				window.showModelessDialog("/back/statistics/stcsMouthLiCaiAction!html_for_mouth_lc_report?username="+username,"html_for_mouth_lc_report","dialogWidth=900px;dialogHeight=600px;");
			}
			
			function operator_formatter(val,row,index){
				//var str = '<a href="javascript:;" onclick="cur_mon_rp(\''+row.USER_USERNAME+'\')">本月</a>';
				var str = '<a href="javascript:;" onclick="showdialog(\''+row.USER_USERNAME+'\')">查看报告</a>';
				return str;
			}
			
			/*会员状态翻译*/
    		function state_formatter(val,row,index){
    			if(val==null){
					return null;
				}else if(val == 1){
					return "待审核";
				}else if(val == 2){
					return "正常";
				}else if(val == 3){
					return "审核未通过";
				}else if(val == 4){
					return "<span style='color:#C31C1C'>停用</span>";
				}else if(val == 5){
					return "<span style='color:#937D49'>注销</span>";
				}
    		}
			
    		
    		/*浮点数格式化*/
			function float_number_formatter(val,row,index){
				if(val==null){
					return null;
				}else if(isNaN(val)){
					return val;
				}else{
					return val.toFixed(2);
				}
			}
    		
    		
    		
    		$(function(){
				
				$("#search").click(function(){
					var source_url = '/back/statistics/stcsMouthLiCaiAction!list_for_mouth_lc_report';
					var username = $("[name='username']").val();
					var state = $("[name='state']").val();
					var url_ = source_url+
				   		"?username=" + username +
				   		"&state=" + state ;
					$("#dg").datagrid({
				   		url: url_
					});
				});
				
				$(window).resize(function(){
					$("#dg").datagrid('resize');
				}).keypress(function(){
					if(event.keyCode==13){
					  $("#search").trigger("click");
					}
				});
				
				var height_ = document.body.clientHeight;
			   	$("#dg").datagrid({
				    height: height_
				});
				$("#view_report").click(function(){
					alert($("#year_select").val());
				});
				
			});
</script>