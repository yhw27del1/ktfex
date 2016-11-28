<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/common/taglib.jsp"%>
<%
    String queryByOrgCode = request.getParameter("queryByOrgCode");  
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
%>
<html>
	<head>
		<title>投资会员活跃度</title> 
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
            url:'/back/stcsAuthorityAction!m_a_hy_list?queryByOrgCode='+'<%=request.getParameter("queryByOrgCode")!=null?request.getParameter("queryByOrgCode"):""%>',
            method:'get',
            toolbar:'#tbhy',
            pagination:false,
            showFooter: true,
            pageList:[15,30,50,100]
            ">
            <thead>
                <tr>
                    <th data-options="field:'buyer',width:120,sortable:true" rowspan="2">机构下总投资会员数</th>
                    <th data-options="field:'nobuy',width:120,sortable:true" rowspan="2">机构下未投资会员数</th>
                    <th colspan="4">已投资会员数</th>
                </tr>
                <tr>
                    <th data-options="field:'buy1',width:90,sortable:true">投资1次人数</th>
                    <th data-options="field:'buy2',width:90,sortable:true">投资2次人数</th>
                    <th data-options="field:'buy3_10',width:90,sortable:true">投资3-10次人数</th>
                    <th data-options="field:'buy10_',width:90">投资10次以上人数</th>
                </tr>
            </thead>
    </table>
    <div id="tbhy" style="padding:5px;height:auto">
        <div>
            <input class="easyui-datebox" name="startDatehy" style="width:100px" placeholder="开始日期" value="<%= sdf.format(new Date()) %>">
            <input class="easyui-datebox" name="endDatehy" style="width:100px" placeholder="结束日期" value="<%= sdf.format(new Date()) %>">
            <a href="#" id="searchhy" class="easyui-linkbutton" iconCls="icon-search">查询</a>
            <a href="#" id="btn" class="easyui-linkbutton" iconCls="icon-search">查看饼状分布图</a>
        </div>
    </div>
    <div>
        <img id="pin" src="" style="display: none;">
    </div>
    <script type="text/javascript">
    $(function(){
        var height_ = 115;
        $("#dghy").datagrid({
            height: height_ 
        });
        $("#searchhy").click(function(){
            var source_url = '/back/stcsAuthorityAction!m_a_hy_list';
            var startDate = $("[name='startDatehy']").val();
            var endDate = $("[name='endDatehy']").val();
            var queryByOrgCode = $("[name='queryByOrgCode']").val();
            var url_ = source_url+
                "?startDate=" + startDate + 
                "&endDate=" + endDate +
                "&queryByOrgCode=" + '<%=request.getParameter("queryByOrgCode")!=null?request.getParameter("queryByOrgCode"):""%>';
            $("#dghy").datagrid({
                url: url_
            });
            $("#btn").trigger("click");
        });
        $("#btn").click(function(){
        	var obj = document.getElementById("pin");
            if(obj.style.display == "block"){
            	var source_url = '/statistics/jfcHyAction';
                var startDate = $("[name='startDatehy']").val();
                var endDate = $("[name='endDatehy']").val();
                var url_ = source_url+
                "?startDate=" + startDate + 
                "&endDate=" + endDate +
                "&queryByOrgCode=" + '<%=request.getParameter("queryByOrgCode")!=null?request.getParameter("queryByOrgCode"):""%>';
                obj.src = url_;
                obj.style.display = "none";
                obj.style.display = "block";
            }else{
            	var source_url = '/statistics/jfcHyAction';
            	var startDate = $("[name='startDatehy']").val();
                var endDate = $("[name='endDatehy']").val();
                var url_ = source_url+
                "?startDate=" + startDate + 
                "&endDate=" + endDate +
                "&queryByOrgCode=" + '<%=request.getParameter("queryByOrgCode")!=null?request.getParameter("queryByOrgCode"):""%>';
                obj.src = url_;
                obj.style.display = "none";
                obj.style.display = "block";
            }           
        });
        $(window).resize(function(){
            $("#dghy").datagrid('resize');
        });
    });
    </script>
    
	</body> 
</html>

 