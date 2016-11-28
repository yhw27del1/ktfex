 
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*"%>
<%@ include file="/common/taglib.jsp"%>
<%
	String username = request.getParameter("username");
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
       <table id="dgjyl" class="easyui-datagrid"
            data-options="rownumbers:false,
            singleSelect:true,
            url:'/back/stcsAuthorityAction!m_a_jyl_list?queryByOrgCode='+'<%=request.getParameter("queryByOrgCode")!=null?request.getParameter("queryByOrgCode"):""%>',
            method:'get',
            toolbar:'#tbjyl',
            pagination:false,
            showFooter: true,
            pageList:[15,30,50,100]
            ">
            <thead>
                <tr>
                    <th data-options="field:'M1',width:60">一月</th>
                    <th data-options="field:'M2',width:60">二月</th>
                    <th data-options="field:'M3',width:60">三月</th>
                    <th data-options="field:'M4',width:60">四月</th>
                    <th data-options="field:'M5',width:60">五月</th>
                    <th data-options="field:'M6',width:60">六月</th>
                    <th data-options="field:'M7',width:60">七月</th>
                    <th data-options="field:'M8',width:60">八月</th>
                    <th data-options="field:'M9',width:60">九月</th>
                    <th data-options="field:'M10',width:60">十月</th>
                    <th data-options="field:'M11',width:60">十一月</th>
                    <th data-options="field:'M12',width:60">十二月</th>
                </tr>
            </thead>
    </table>
    <div id="tbjyl" style="padding:5px;height:auto">
        <div>
            <select class="easyui-combobox" name="year" id="year" data-options="onChange:yearChanged">
                <option value="2013">2013年</option>
                <option value="2014">2014年</option>
                <option value="2012">2012年</option>
            </select>
            <a href="#" id="searchjyl" class="easyui-linkbutton" iconCls="icon-search">查询</a>
            <a href="#" id="btn" class="easyui-linkbutton" iconCls="icon-search">查看/刷新柱状分布图</a>
        </div>
    </div>
    <div>
        <img id="pin" src="" style="display: none;">
    </div>
    <script type="text/javascript">
    function yearChanged(old,newvalue){
    	$("#btn").trigger("click");
    	$("#searchjyl").trigger("click");
    }
    $(function(){
        var height_ = 90;
        $("#dgjyl").datagrid({
            height: height_ 
        });
        $("#searchjyl").click(function(){
            var source_url = '/back/stcsAuthorityAction!m_a_jyl_list';
            var year = $("[name='year']").val();
            var url_ = source_url+
                "?year=" + year+
                "&queryByOrgCode=" + 
                '<%=request.getParameter("queryByOrgCode")!=null?request.getParameter("queryByOrgCode"):""%>';
           $("#dgjyl").datagrid({
                url: url_
            });
        });
        $("#btn").click(function(){
            var obj = document.getElementById("pin");
            if(obj.style.display == "block"){
                var source_url = '/statistics/jfcJylAction';
                var year = $("[name='year']").val();
                var url_ = source_url+
                "?year=" + year + 
                "&queryByOrgCode=" + '<%=request.getParameter("queryByOrgCode")!=null?request.getParameter("queryByOrgCode"):""%>';
                obj.src = url_;
                obj.style.display = "none";
                obj.style.display = "block";
            }else{
                var source_url = '/statistics/jfcJylAction';
                var year = $("[name='year']").val();
                var url_ = source_url+
                "?year=" + year + 
                "&queryByOrgCode=" + '<%=request.getParameter("queryByOrgCode")!=null?request.getParameter("queryByOrgCode"):""%>';
                obj.src = url_;
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

 