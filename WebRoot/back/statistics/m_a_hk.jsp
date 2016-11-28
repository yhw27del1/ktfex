<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/common/taglib.jsp"%>
<%
	String queryByOrgCode = request.getParameter("queryByOrgCode");  
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
%>
<html>
	<head>
        <title>投资会员还款情况</title> 
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
       <table id="dghy" class="easyui-datagrid"
            data-options="rownumbers:false,
            singleSelect:true,
            url:'/back/stcsAuthorityAction!m_a_hk_list?queryByOrgCode='+'<%=request.getParameter("queryByOrgCode")!=null?request.getParameter("queryByOrgCode"):""%>',
            method:'get',
            toolbar:'#tbhy',
            sortName:'SHDATE',
            sortOrder:'desc',
            pagination:true,
            height:370,
            showFooter: true,
            pageList:[15,30,50,100]
            ">
            <thead>
                <tr>
                    <th data-options="field:'YHDATE',width:90,sortable:true,hidden:true">应还日期</th>
                    <th data-options="field:'SHDATE',width:90,sortable:true">实还日期</th>
                    <th data-options="field:'YHBJ',width:90,hidden:true">应还本金</th>
                    <th data-options="field:'YHLX',width:90,hidden:true">应还利息</th>
                    <th data-options="field:'SHBJ',width:90">实还本金</th>
                    <th data-options="field:'SHLX',width:90">实还利息</th>
                    <th data-options="field:'FJ',width:90">罚金</th>
                    <th data-options="field:'XJ',width:90">小计</th>
                </tr>
            </thead>
    </table>
    <div id="tbhy" style="padding:5px;height:auto">
        <div>
            <select class="easyui-combobox" name="dateState" id="dateState"   data-options="onChange:yearChanged">
                <option value="shdate">实还日期</option>
                <option value="yhdate">应还日期</option>
            </select>
            <input class="easyui-datebox" name="startDatehy" style="width:100px" placeholder="开始日期" value="<%= sdf.format(new Date()) %>">
            <input class="easyui-datebox" name="endDatehy" style="width:100px" placeholder="结束日期" value="<%= sdf.format(new Date()) %>">
            <select class="easyui-combobox" name="repayState" id="repayState">
                <option value="">还款类型</option>
                <option value="1">正常还款</option>
                <option value="2">提前还款</option>
                <option value="3">逾期还款</option>
                <option value="4">担保代偿</option>
                <option value="5">全部已还款</option>
                <option value="0">未还款</option>
            </select>
            <a href="javascript:;" id="searchhk" class="easyui-linkbutton" iconCls="icon-search">查询</a>
        </div>
    </div>
    <div>
    </div>
    <script type="text/javascript">
    $(function(){
        var height_ = 370;
        $("#dghy").datagrid({
            height: height_ 
        });
        $("#dateState").combobox({  
             editable:false 
         }); 
        $("#repayState").combobox({  
            editable:false 
        }); 
        $("#searchhk").click(function(){
            
        	 var source_url = '/back/stcsAuthorityAction!m_a_hk_list';
             var startDate = $("[name='startDatehy']").val();
             var endDate = $("[name='endDatehy']").val();
             var dateState = $("[name='dateState']").val();
             var repayState = $("[name='repayState']").val();
             var queryByOrgCode = $("[name='queryByOrgCode']").val();
             var url_ = source_url+
                 "?startDate=" + startDate + 
                 "&endDate=" + endDate +
                 "&dateState=" + dateState +
                 "&repayState=" + repayState +
                 "&queryByOrgCode=<%=request.getParameter("queryByOrgCode")!=null?request.getParameter("queryByOrgCode"):""%>";
             $("#dghy").datagrid({
                 url: url_
             });
        });

        
        $(window).resize(function(){
            $("#dghy").datagrid('resize');
        });
    });
    function yearChanged(newvalue,old){
        if(newvalue == 'shdate'){
            $('#dghy').datagrid('hideColumn','YHDATE');
            $('#dghy').datagrid('hideColumn','YHBJ');
            $('#dghy').datagrid('hideColumn','YHLX');
            $('#dghy').datagrid('showColumn','SHDATE');
            $('#dghy').datagrid('showColumn','SHBJ');
            $('#dghy').datagrid('showColumn','SHLX');
            $('#dghy').datagrid('showColumn','FJ');
        }else{
            $('#dghy').datagrid('showColumn','YHDATE');
            $('#dghy').datagrid('showColumn','YHBJ');
            $('#dghy').datagrid('showColumn','YHLX');
            $('#dghy').datagrid('hideColumn','SHDATE');
            $('#dghy').datagrid('hideColumn','SHBJ');
            $('#dghy').datagrid('hideColumn','SHLX');
            $('#dghy').datagrid('hideColumn','FJ');
        }
    }
    
    </script>
        
    </body> 
</html>

 