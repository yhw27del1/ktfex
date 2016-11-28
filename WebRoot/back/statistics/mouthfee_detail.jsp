
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
            url:'/back/stcsDaiLiFeeAction!getDaiLiFeeList?queryByOrgCode=<%=request.getParameter("queryByOrgCode")!=null?request.getParameter("queryByOrgCode"):""%>',
            singleSelect:true,
            method:'get',
            toolbar:'#tbdlf',
            pagination:true,
            showFooter: true,
            height:374,
            pageList:[15,30,50,100]
            ">
            <thead data-options="frozen:true">
                <tr>
                    <th data-options="field:'INVESTORNAME',width:80">投标方户名</th>
                    <th data-options="field:'FINANCBASECODE',width:80">融资项目号</th>
                    <th data-options="field:'FBCREATEDATE',width:80,formatter:date_formatter">融资项目创建时间</th>
                    <th data-options="field:'JINGBANREN',width:80">介绍人</th>
                </tr>
            </thead>
            <thead>
                <tr>
                    <th data-options="field:'BUSINESSTYPE',width:80,formatter:term_formatter">期限</th>
                    <th data-options="field:'INVESTAMOUNT'">交易额</th>
                    <th data-options="field:'DAILIFEE',width:80,formatter:float_number_formatter" >当期信息服务费</th>
                    <th data-options="field:'QIANYUEDATE',formatter:date_formatter">签约日期</th>
                    <th data-options="field:'RZFWF_TARIFF',formatter:fenqi_formatter">是否分期</th>
                    <th data-options="field:'MON',formatter:mon_formatter">当前期次</th>
                    <th data-options="field:'FIRSTDLF',formatter:date_formatter2">第一笔代理费日期</th>
                    <th data-options="field:'LASTDLF',formatter:date_formatter2">最后一笔代理费日期</th>
                    
                </tr> 
            </thead>
             
        </table>
        <div id="tbdlf" style="padding:5px;height:auto">
            <div>
                时间: 
            <select class="easyui-combobox" data-options="
                onSelect:year_changed,
                onLoadSuccess:initMonth,
                width:80,
                editable:false,
                url:'/back/stcsDaiLiFeeAction!year_json_for_mouth_lc_report?orgcode=<%=request.getParameter("queryByOrgCode")!=null?request.getParameter("queryByOrgCode"):""%>'"
                name="year_select" id="year_select">
            </select>
            &nbsp;&nbsp;
            <select class="easyui-combobox" data-options="
                width:80,
                editable:false,
                valueField:'value',
                textField:'text'" name="month" id="month">
            </select>
                &nbsp;&nbsp;
            <input type="text" name="jingbanren" placeholder="介绍人" class="combo"  id="jingbanren" style="width: 100;" />
            <input type="text" name="investorname" placeholder="投标人" class="combo"  id="investorname" style="width: 100;" />
            <input type="text" name="rztyper" placeholder="融资期限(日)" title="与按月类型不兼容，如果同时填写二者以日计算" class="combo"  id="rztyper" style="width: 100;"/>
               <input type="text" name="rztype" placeholder="融资期限(月)" title="与按日类型不兼容，如果同时填写二者以日计算" class="combo"  id="rztype" style="width: 100;"/>
                <select class="easyui-combobox" name="tariff" id="tariff">
                    <option value="">全部</option>
                    <option value="0">一次性支付</option>
                    <option value="1">按月支付</option>
                </select>
                <a href="#" id="searchdlf" class="easyui-linkbutton" iconCls="icon-search">查询</a> 
                <a href="/back/stcsDaiLiFeeAction!export_ex?queryByOrgCode=<%=request.getParameter("queryByOrgCode")!=null?request.getParameter("queryByOrgCode"):""%>"  id="searchdlfex" class="easyui-linkbutton" iconCls="icon-edit">导出Excel</a> 
                <a href="/back/stcsDaiLiFeeAction!export_pdf?queryByOrgCode=<%=request.getParameter("queryByOrgCode")!=null?request.getParameter("queryByOrgCode"):""%>" id="export_pdf" class="easyui-linkbutton" iconCls="icon-pdf">导出PDF</a> 
                <a href="/back/stcsDaiLiFeeAction!export_pdf_sum?queryByOrgCode=<%=request.getParameter("queryByOrgCode")!=null?request.getParameter("queryByOrgCode"):""%>" id="export_pdf_sum" class="easyui-linkbutton" iconCls="icon-pdf">导出PDF汇总表</a> 
                <span style="font-size: 12px;color: red;">*本功能提供初期，计算的费用可能与实际情况有差异。最终费用结算以双方稽核对帐结果为准。</span>
            </div>
        </div>
        <div id="win"></div>
        <%@ include file="/common/messageTip.jsp" %>
        <script>
        function year_changed(record){
            $("#month").combobox('reload','/back/statistics/stcsDaiLiFeeAction!month_json_for_mouth_lc_report?orgcode=<%=request.getParameter("queryByOrgCode")!=null?request.getParameter("queryByOrgCode"):""%>&year='+record['value']);
        }
        function initMonth(){
            var year_str = $("[name='year_select']").val();
            $("#month").combobox({url:'/back/statistics/stcsDaiLiFeeAction!month_json_for_mouth_lc_report?orgcode=<%=request.getParameter("queryByOrgCode")!=null?request.getParameter("queryByOrgCode"):""%>&year='+year_str});
        }
        
        function term_formatter(val,row,index){
            if(val == null) return null;
            if(row.INTERESTDAY == 0){
                return val+"[月]";
            }else{
                return row.INTERESTDAY+"[天]";
            }
        }
        function date_formatter(val,row,index){
            if(val==null){
                return null;
            }else{
                return new Date(val.time).format('yyyy-MM-dd');
            }
        }
        function date_formatter2(val,row,index){
            if(val==null){
                return null;
            }else if(row.RZFWF_TARIFF == '0'){
                return null;
            }{
                return new Date(val.time).format('yyyy-MM-dd');
            }
        }
        function mon_formatter(val,row,index){
            if(val==null){
                return null;
            }else if(row.RZFWF_TARIFF == '0'){
                return null;
                }else{
                return val + "\/" + row.BUSINESSTYPE;
            }
        }
        function fenqi_formatter(val,row,index){
            if(val==null){
                return null;
            }else if(val == '0'){
                return "一次性支付";
                }else{
                return "分期支付";
            }
        }
        function float_number_formatter(val,row,index){
            if(val==null){
                return null;
            }else if(isNaN(val)){
                return val;
            }else{
                return val.toFixed(2);
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
        
    $(function(){
        $("#searchdlf").click(function(){
            var source_url = '/back/stcsDaiLiFeeAction!getDaiLiFeeList'; 
            var year = $("[name='year_select']").val();
            var month = $("[name='month']").val();
            var rztype = $("[name='rztype']").val();
            var rztyper = $("[name='rztyper']").val();
            var tariff = $("[name='tariff']").val();
            var jingbanren = $("[name='jingbanren']").val();
            var investorname = $("[name='investorname']").val();
            var queryByOrgCode = <%=request.getParameter("queryByOrgCode")!=null?request.getParameter("queryByOrgCode"):""%>;
            var parameters = "?year=" + year + 
                "&month=" + month +
                "&rztype=" + rztype +
                "&rztyper=" + rztyper +
                "&tariff=" + tariff +
                "&jingbanren=" + jingbanren +
                "&investorname=" + investorname +
                "&queryByOrgCode=" + queryByOrgCode;
            var url_ = source_url+parameters;
            $("#dgdlf").datagrid({
                url: url_
            });

            $("#searchdlfex").attr("href","/back/stcsDaiLiFeeAction!export_ex"+parameters);
            $("#export_pdf").attr("href","/back/stcsDaiLiFeeAction!export_pdf"+parameters);
            $("#export_pdf_sum").attr("href","/back/stcsDaiLiFeeAction!export_pdf_sum"+parameters);
        });
        
       
       
    });
        </script>
    </body> 
</html>



