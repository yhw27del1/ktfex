<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*"%>
<%@ include file="/common/taglib.jsp"%>
<%
	Date now = new Date();
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/metro/easyui.css">
    	<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/icon.css">
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/easyui-lang-zh_CN.js"/></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<title></title>
		<style>
			body{padding:0;margin:0;overflow:hidden !important;}
			.combo{height:20px;background-color: #fff;}
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
            onRowContextMenu:contextmenu,
            cache:true,
            showFooter:true
            ">
            <thead data-options="frozen:true">
				<tr>
					<th data-options="field:'USER_USERNAME',width:80">交易号</th>
		        	<th data-options="field:'NAME',width:80,formatter:name_formatter">姓名</th>
		        	<th data-options="field:'SEXSTR',width:30">性别</th>
		        	<th data-options="field:'INVEST_ALL_COUNT',width:60">投标次数</th>
		        	<th data-options="field:'ZUIJINSHENGOU',width:120,formatter:datetime_formatter">最近投标</th>
		        	<th data-options="field:'JINGBANREN',width:50">介绍人</th>
				</tr>
	        </thead>
	        <thead>
	            <tr>
		        	<th data-options="field:'CREATEDATE',width:70,formatter:date_formatter">开户日期</th>
		        	<th data-options="field:'MEMBER_LEVEL',width:50">级别</th>
		        	<th data-options="field:'ORGNAME',width:80">开户机构</th>
		        	<th data-options="field:'BANKLIB',width:60">开户银行</th>
		        	<th data-options="field:'STATE',width:40,formatter:state_formatter">状态</th>
		        	<th data-options="field:'CITYNAME',width:50">地区</th>
		        	<th data-options="field:'USER_ACCOUNT_BALANCE',width:70,formatter:float_number_formatter">可用余额</th>
		        	<th data-options="field:'ZZC',width:80,formatter:float_number_formatter">总资产</th>
	                <th data-options="field:'showok',width:80,formatter:operator_formatter">操作</th>
	            </tr>
	        </thead>
	    </table>
	    
	    
	    <div id="tb" style="padding:5px;height:auto">
	        <div>
	           	<input placeholder="会员交易号" style="width:100px" class="combo" name="username" id="username" />
	           	<input placeholder="会员姓名" style="width:100px" class="combo" name="realname" id="realname" />
	           	<input placeholder="介绍人" style="width:100px" class="combo" name="jingbanren" id="jingbanren" />
	           	级别:<select class="easyui-combobox" data-options="width:120,
	           			editable:false,
	           			url:'/back/statistics/stcsMemBaseAction!memberlevel_json',
	           			valueField:'id',
	           			textField:'level',
	           			formatter: function(row){
							return row.level+'[<span style=\'color:#118513\'>'+row.highpercent+'</span>]';
						}
	           			" name="memberlevel" id="memberlevel">
	        	</select>
	           	状态:<select class="easyui-combobox" data-options="editable:false" name="state" id="state">
	        		<option value="">不限</option>
	        		<option value="1">未审核</option>
	        		<option value="2">正常</option>
	        		<option value="3">审核未通过</option>
	        		<option value="4">停用</option>
	        		<option value="5">注销</option>
	        	</select>&nbsp;
	            余额等级:
	        	<select class="easyui-combobox" data-options="editable:false" name="balancelevel" id="balancelevel">
	        		<option value="">不限</option>
	        		<option value="<10000">1万以下</option>
	        		<option value="10000,100000">1~10万</option>
	        		<option value="100000,500000">10~50万</option>
	        		<option value="500000,1000000">50~100万</option>
	        		<option value=">1000000">100万以上</option>
	        	</select>
	            总资产
	        	<select class="easyui-combobox" data-options="editable:false" name="zzclevel" id="zzclevel">
	        		<option value="">不限</option>
	        		<option value="<10000">1万以下</option>
	        		<option value="10000,100000">1~10万</option>
	        		<option value="100000,500000">10~50万</option>
	        		<option value="500000,1000000">50~100万</option>
	        		<option value=">1000000">100万以上</option>
	        	</select>
	        	
	        </div>
	        <div style="margin-top:3px;">
	        	开户日期
	        	<input class="easyui-datebox" name="startDate" style="width:100px" placeholder="开始日期"/>
	            <input class="easyui-datebox" name="endDate" style="width:100px" placeholder="结束日期"/>
	        	<a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
	        </div>
	    </div>
   		
   		
   		<div id="mm" class="easyui-menu" style="width:120px;">
	        <div>列表</div>
	    </div>
    	<div id="win"></div>
    	<div id="pwin"></div>
	</body>
</html>


<script>
			function contextmenu(e, rowIndex, rowData){
				e.preventDefault();
                 $('#mm').menu('show', {
                    left: e.pageX,
                    top: e.pageY
                });
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
    		
    		function datetime_formatter(val,row,index){
				if(val==null){
					return null;
				}else{
					return new Date(val.time).format('yyyy-MM-dd hh:mm:ss');
				}
			}
			function date_formatter(val,row,index){
				if(val == '合计'){
					return val;
				}else if(val==null){
					return null;
				}else{
					return new Date(val.time).format('yyyy-MM-dd');
				}
			}
			
			
			
			
			
    		Date.prototype.format =function(format){
				var o = {
					"M+" : this.getMonth()+1, //month
					"d+" : this.getDate(), //day
					"h+" : this.getHours(), //hour
					"m+" : this.getMinutes(), //minute
					"s+" : this.getSeconds(), //second
					"q+" : Math.floor((this.getMonth()+3)/3), //quarter
					"S" : this.getMilliseconds() //millisecond
				}
				if(/(y+)/.test(format)){
					format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4- RegExp.$1.length));
				}
				for(var k in o)if(new RegExp("("+ k +")").test(format)){
					format = format.replace(RegExp.$1, RegExp.$1.length==1? o[k] :("00"+ o[k]).substr((""+ o[k]).length));
				}
				return format; 
			}
    		function operator_formatter(val,row,index){
    			if(row.USER_USERNAME == '合计') return null;
    			return '<a href="javascript:;" onclick="showdialog(\'-'+row.USER_USERNAME+'-'+name(row.NAME)+'\',\'win\',\'/back/statistics/member_tabs_json.jsp?username='+row.USER_USERNAME+'\',900,510)">查看详细</a>';
    		}
    		
    		
			function tabsResize(width, height){
				
			}
			function dialogResize(width, height){
				if($("#member-tabs").length==0){
					return;
				} else {
					var tabs = $("#member-tabs").tabs('tabs');
					$.each(tabs,function(index,obj){
						obj.panel({
							width:'100%'
						}).panel('resize');
					})
				}
			}
			
			
    		
    		function showdialog(title,ele,url,width,height){
    			$('#'+ele).dialog({
					title:'业务详细'+title,
				    width:width,
				    height:height,
				    modal:true,
				    href:url,
				    maximizable:true,
				    onResize:dialogResize
				   
				});
			}
			
    		
    		
    		
    		
    		
    		$(function(){
				
				$("#search").click(function(){
					var source_url = '/back/statistics/stcsMemBaseAction!m_detail';
					var username = $("[name='username']").val();
					var realname = $("[name='realname']").val();
					var jingbanren = $("[name='jingbanren']").val();
					var memberlevel = $("[name='memberlevel']").val();
					var state = $("[name='state']").val();
					var zzclevel = $("[name='zzclevel']").val();
					var balancelevel = $("[name='balancelevel']").val();
					var startDate = $("[name='startDate']").val();
					var endDate = $("[name='endDate']").val();
				
					var url_ = source_url+
				   		"?username=" + username +
				   		"&realname=" + realname + 
				   		"&jingbanren=" + jingbanren + 
				   		"&memberlevel=" + memberlevel + 
				   		"&state=" + state + 
				   		"&zzclevel=" + zzclevel + 
				   		"&balancelevel=" + balancelevel + 
				   		"&startDate=" + startDate + 
				   		"&endDate=" + endDate ;
					$("#dg").datagrid({
				   		url: url_
					});
				});
				
				$(window).resize(function(){
					$("#dg").datagrid('resize');
				});
			});
			var height_ = document.body.clientHeight;
		    	$("#dg").datagrid({
				    height: height_
				});
    	</script>