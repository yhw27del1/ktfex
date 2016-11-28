
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%
    Date now = new Date();
%>
<html>
    <head>
        <title></title>
        <%@ include file="/common/taglib.jsp"%>
        <script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
        <link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/metro/easyui.css">
        <link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/icon.css">
        <script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/easyui-lang-zh_CN.js"/></script>
        <style>
                body{
                 overflow:auto !important;padding;0;margin:0;
                }
        </style>
    </head>
    <body>
        <table id="dgdlf" class="easyui-datagrid"
            data-options="rownumbers:true,
            url:'/back/sqOrgAction!getDaiLiFeeList',
            singleSelect:true,
            method:'get',
            toolbar:'#tbdlf',
            pagination:true,
            showFooter: true,
            pageList:[15,30,50,100]
            ">
            <thead data-options="frozen:true">
                <tr>
                    <th data-options="field:'ORGSHOWCODING',width:80">机构名</th>
                    <th data-options="field:'SHOWNAME',width:80">合同简称</th>
                    <th data-options="field:'STARTDATE',width:80,formatter:date_formatter">开始日期</th> 
                    <th data-options="field:'ENDDATE',width:80,formatter:date_formatter">结束日期</th> 
                    <th data-options="field:'TYPE',width:80" >计算类型</th>
                </tr>
            </thead>
            <thead>
                <tr>
                    
                    <th data-options="field:'ALLOCATIONPROPORTION',width:80">B类分配比例</th>
                    <th data-options="field:'CHECKSTANDARD',width:80">B类每月核算标准</th>
                    <th data-options="field:'MOUTHFORTHREE',width:80" >A类三月期</th>
                    <th data-options="field:'MOUTHFORSIX',width:80" >A类六月期</th>
                    <th data-options="field:'MOUTHFORNINE',width:80" >A类九月期</th>
                    <th data-options="field:'MOUTHFORTWELVE',width:80" >A类十二月期</th>
                    <th data-options="field:'showok',width:100,formatter:operator_formatter" rowspan="2">操作</th>
                </tr> 
            </thead>
             
        </table>
        <div id="tbdlf" style="padding:5px;height:auto">
            <div>
                <input type="text" name="queryByOrgCode" placeholder="机构编码" class="combo"  id="queryByOrgCode"/>
                <a href="#" id="searchdlf" class="easyui-linkbutton" iconCls="icon-search">查询</a> 
                <a href="#" id="adddlf" class="easyui-linkbutton" iconCls="icon-add">新增</a> 
            </div>
        </div>
        <div id="win"></div>
        <%@ include file="/common/messageTip.jsp" %>
        <script>

        function toURL(url){ 
        	   window.location.href = url; 
        } 
    	
        function date_formatter(val,row,index){
            if(val==null){
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
            var str = '';
            if(row.ID==null) return null;
            str += '<a href="javascript:;" onclick="toURL(\'/back/sqOrgAction!modify?_id='+row.ID+'\')">修改</a>&nbsp;&nbsp;&nbsp;';
            str += '<a href="javascript:;" onclick="toURL(\'/back/sqOrgAction!delContract?_id='+row.ID+'\')">删除</a>';
            return str;
        }
        
    $(function(){
    	var height_ = document.body.clientHeight ;
        $("#dgdlf").datagrid({
            height: height_
        });
        
        $("#searchdlf").click(function(){
            var source_url = '/back/sqOrgAction!getDaiLiFeeList'; 
            var queryByOrgCode = $("[name='queryByOrgCode']").val();
            var url_ = source_url+
                "?queryByOrgCode=" + queryByOrgCode;
            $("#dgdlf").datagrid({
                url: url_
            });
        });

        $("#adddlf").click(function(){
            var source_url = '/back/sqOrgAction!modify'; 
            toURL(source_url);
        });

        $(window).resize(function(){
            $("#dg").datagrid('resize');
        });
    });
</script>
    </body> 
</html>



