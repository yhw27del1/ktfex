<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
    <head>
		<title></title>
		<%@ include file="/common/taglib.jsp"%>
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/metro/easyui.css">
    	<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/icon.css">
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/easyui-lang-zh_CN.js"/></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<style>
			body{padding:0;margin:0;}
		</style>
	</head>
   <body>

     <table id="dg" class="easyui-datagrid"
            data-options="rownumbers:true,
            singleSelect:true,
            url:'/back/financing/businessTypeAction!list',
            method:'get',
            toolbar:'#tb',
            pagination:true,
            showFooter: true,
            pageList:[15,30,50,100]
            
            ">
        
            <thead>
                <tr>
                    <th data-options="field:'NAME',width:100" rowspan="2">类型名称</th>
                    <th data-options="field:'CODE',width:100 " rowspan="2">类型标识</th>
                    <th data-options="field:'TERM',width:100,formatter:term_formatter">期限(月数)</th>
                    <th data-options="field:'RETURNPATTERN',width:180">还款方式</th>
                    <th data-options="field:'RETURNTIMES',width:80">还款次数</th>
                    <th data-options="field:'',width:100,formatter:operator_formatter" rowspan="2">操作</th>                           
                </tr>
            </thead>
     </table>
	<div id="tb" style="padding:5px;height:auto">
        <div>
                       关键字
            <input id="qkeyWord" type="text" name="qkeyWord" class="combo" style="width:100px" />
            <a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
        </div>
    </div>
    <div id="win">   		 
    </div>
				
		<%@ include file="/common/messageTip.jsp"%>
</body>
</html>
<script>

          function operator_formatter(val,row,index){
		    var str = '';
		    if(row.ID==null) return null;
		    str += '<a href="javascript:;" onclick="showLogs(\'/back/financingBaseAction!logs?_id='+row.ID+'\',700)">日志</a>';
		    
		    
		    return str;
		}
       function term_formatter(val,row){
		    if(val == null) return null;
		    if(row.ID == "day"){
		        return "-";
		    }else{
		        return val+"个月";
		    }
		}
   
      $(function(){
		    var height_ = document.body.clientHeight ;
		    $("#dg").datagrid({
		        height: height_
		    });
		    $("#search").click(function(){
		        var source_url = '/back/finiancing/businessTypeAction!list'
		        $("#dg").datagrid({
		            url: url_
		        });
		    });
		    $(window).resize(function(){
		        $("#dg").datagrid('resize');
		    });
		});
     
</script>