$(function(){
    //刷新操作
    $("#reflash").click(function(){
        var $div = $("div[url]:visible", $("#tabs"));
        var loadId = $div.attr("id");
        var loadUrl = $div.attr("url");
        $("#" + loadId).load(loadUrl + "?time=" + new Date().getTime());
    });
    
    //全选控制,反选控制
    $(".selectAll:visible").click(function(){
        var ts = $(this);
        if (ts.attr("checked") == "checked") {
            $("input.selectOne[name]:checkbox::visible").attr("checked", true);
        }
        else {
            $("input.selectOne[name]:checkbox::visible").attr("checked", false);
        }
    });
    //只有所有选项都选中了，全选复选框才选中
    $("input[name]:checkbox").click(function(){
        var len = $("input.selectOne[name]:checkbox:visible").length;
        var len_checked = $("input.selectOne[name][checked]:checkbox:visible").length;
        if (len == len_checked) {
            $(".selectAll:visible").attr("checked", true);
        }
        else {
            $(".selectAll:visible").attr("checked", false);
        }
    });
    
    $("#dialog-message").dialog({//删除要选中一行
        autoOpen: false,
        modal: true,
        width: 460,
        position: "center",
        buttons: {
            "我知道了": function(){
                $(this).dialog("close");
            }
        }
    });
    
    $("#dialog-message-delete").dialog({
        autoOpen: false,
        modal: true,
        width: 320,
        position: "center"
    });
    
    $("#dialog-message-delete2").dialog({
        autoOpen: false,
        modal: true,
        width: 320,
        position: "center"
    });
    $("#dialog-message-add").dialog({
        autoOpen: false,
        modal: true,
        width: 320,
        position: "center"
    });
    
    $("#dialog-message-add2").dialog({
        autoOpen: false,
        modal: true,
        width: 320,
        position: "center"
    });
});
