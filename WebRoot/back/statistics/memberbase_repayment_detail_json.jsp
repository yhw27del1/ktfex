<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
	<head>
		<title></title>
		<%@ include file="/common/taglib.jsp"%>
	</head>
	<body>
		
	<table id="dg3" class="easyui-datagrid" 
            data-options="rownumbers:true,
            singleSelect:true,
            url:'/back/stcsMemBaseAction!getPaymentRecordJsonDetail?uid='+'<%=request.getParameter("uid")!=null?request.getParameter("uid"):""%>'+'&shdate='+'<%=request.getParameter("shdate")!=null?request.getParameter("shdate"):""%>',
            method:'get',
            toolbar:'#tb_repay_detail',
            sortName:'FBCODE',
            sortOrder:'asc',
            height:363,
            showFooter: true
            ">
        
            <thead>
                <tr>
                	<th data-options="field:'FBCODE',width:80,sortable:true" rowspan="2">项目编号</th>
                    <th data-options="field:'INVESTAMOUNT',width:90" rowspan="2">投标金额</th>
                    <th data-options="field:'STATE',width:60" rowspan="2">还款状态</th>
                    <th data-options="field:'QS',width:50" rowspan="2">还款<br/>期次</th>
                    <th colspan="2">应还款额</th>   
                    <th colspan="4">实还款额</th>
                </tr>
                <tr>
                    <th data-options="field:'YHBJ',width:60,formatter:float_number_formatter">本金</th>
                    <th data-options="field:'YHLX',width:60,formatter:float_number_formatter">利息</th>
                    <th data-options="field:'SHB',width:60,formatter:float_number_formatter">已还本金</th>
                    <th data-options="field:'SHX',width:60,formatter:float_number_formatter">利息</th>
                    <th data-options="field:'FJ',width:60,formatter:float_number_formatter">罚金</th>
                    <th data-options="field:'yhtotal',width:60,formatter:yhtotal_formatter">小计</th>
                </tr>
            </thead>
    </table>
    <div id="tb_repay_detail" style="padding:5px;height:auto">
        <div>
            项目编号
        	<input type="text" name="fbcode" id="fbcode" class="combo" style="width:100px"  placeholder="项目编号" title="项目编号"/>
            <select class="easyui-combobox" name="state_repay" id="state_repay">
                <option value="">请选择</option>
                <option value="0">未还款</option>
                <option value="1">正常还款</option>
                <option value="2">提前还款</option>
                <option value="3">逾期还款</option>
                <option value="4">担保代偿</option>
            </select>
            <a href="#" id="search_repay_detail" class="easyui-linkbutton" iconCls="icon-search" title="">查询</a>
            </div>
            <script>
			        $(function(){
			            $("#search_repay_detail").click(function(){
			                var source_url = '/back/stcsMemBaseAction!getPaymentRecordJsonDetail';
			                var fbcode = $("[name='fbcode']").val();
			                var state = $("[name='state_repay']").val();
			                var ivcode = $("[name='ivcode']").val();
			                var url_ = source_url+
			                     "?fbcode=" + fbcode + 
			                    "&shdate=" + '<%=request.getParameter("shdate")!=null?request.getParameter("shdate"):""%>' + 
			                    "&state=" + state +
			                    "&uid=" + '<%=request.getParameter("uid")!=null?request.getParameter("uid"):""%>';
			                $("#dg3").datagrid({
			                    url: url_
			                });
			            });
			        });
					
			</script>
    </div>
    	<div id="win">
    		 
    	</div>
				
	</body>
</html>
