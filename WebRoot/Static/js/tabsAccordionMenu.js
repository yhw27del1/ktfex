$(function(){
    var loadUrl;
    var param;
    $("#accordion").accordion({
        //animated: 'bounceslide'
        //autoHeight: false,
        //clearStyle: true
    });
    
    $("#themeSwitch").change(function(){//换肤
        var value = $(this).val();
        var href = $("link[skin='skin']").attr("href", "/Static/css/themes/" + value + "/jquery-ui.css");
    });
    
    $("a[mid1]:first").addClass("xietijiacu");
    $("a[mid1]").click(function(){
        $("a[mid1]").removeClass("xietijiacu");
        $(this).addClass("xietijiacu");
    });
    
    $("a[mid2]").hover(function(){
        $(this).addClass("ui-state-hover");
    }, function(){
        $(this).removeClass("ui-state-hover");
    }).mousedown(function(){
        $("a[mid2]").removeClass("ui-state-active");
        $(this).addClass("ui-state-active");
    });
    
    var $tabs = $("#tabs").tabs({
        tabTemplate: "<li><a href='\#{href}'>\#{label}</a> <span class='ui-icon ui-icon-close' style='margin:0 0'>Remove Tab</span></li>",
        add: function(event, ui){
			$tabs.showLoading(); 
			
			if(loadUrl.indexOf('?') > 0) {
				var src = loadUrl + "time=" + new Date().getTime() + param;
			} else {
				var src = loadUrl + "?time=" + new Date().getTime() + param;
			}	 
            var iframe = '<iframe id="iframe' + ui.panel.id + '" scrolling="no" frameborder="0"  src="' + src + '" style="width:100%;"></iframe>';
			$(ui.panel).append(iframe);
			$(ui.panel).css({"margin":"0 0","padding":"10px 10px"});
            $tabs.tabs('select', '#' + ui.panel.id);//新添加的标签立即选中
            $('.ui-tabs-nav li a').css({
                'float': 'left',
                'padding': '0.1em 1em',
                'text-decoration': 'none',
                'font-size': '12px',
                'font-weight': 'normal',
                'font-family': '宋体'
            });
			$("#iframe" + ui.panel.id).load(function(){//添加新标签后，标签下iframe加载链接地址，内容载入完毕后，设置该iframe的高度
				var h1 = $("#iframe" + ui.panel.id).contents().find(".autoheight").val();
				//var h2 = $("#iframe" + ui.panel.id).height();
				//if(!$("#iframe" + ui.panel.id).attr('autoheight')){
					$("#iframe" + ui.panel.id).height(h1);
				//	$("#iframe" + ui.panel.id).attr("autoheight",h1);
				//}
				$tabs.hideLoading();
			});
        }
    });
    
    function addTab(tab_href, tab_title){
        $tabs.tabs("add", "#" + tab_href, tab_title);
    }
    
    $("#tabs span.ui-icon-close").live("click", function(){
        var index = $("li", $tabs).index($(this).parent());
        $tabs.tabs("remove", index);
    });
    
    $("a[mid2]").click(function(){
        var allTabs = $("li", "#tabs").length;//当前的所有标签数
        var size = 6;//最多能打开2个标签(包括不能删除的 首页标签)
        var ts = $(this);
        var mid2 = ts.attr("mid2");
        var has = $("a[href=#" + mid2 + "]").text();
        loadUrl = ts.attr("url");
        if (ts.attr("urlParam")) {
            param = "&"+ts.attr("urlParam");
        }
        else {
            param = "";
        }
        if ("" == has || null == has) {//没有标签则创建
            //添加标签之前，检查当前的标签数，多了则先删除前面的标签再添加新标签
            if (allTabs >= size) {//当前标签数超过或等于3个，则删除第二个标签
                $tabs.tabs("remove", 0);//删除第一个标签
            }
            addTab(mid2, ts.text());
            return false;
        }
        else {//有标签则选定
            $tabs.tabs("select", "#" + mid2);//通过href来选定，通过index选定比较麻烦
			$("#iframe"+mid2).attr("src",loadUrl);//选定后刷新
            return false;
        }
    });
    //解决风琴菜单太高的问题
    $('.ui-accordion .ui-accordion-header a').css({
        'display': 'block',
        'font-size': '12px',
        'padding': '.2em .5em .2em 2.2em',
        'font-weight': 'normal',
        'font-family': '宋体'
    });
    $('ui-accordion-content a').css({
        'font-size': '18px'
    });
    //解决面板太高的问题
	$('.ui-tabs-panel').css({"margin":"0 0","padding":"10px 10px"});
    $('.ui-tabs').css({
        'padding': '0 0'
    });
    $('.ui-tabs-nav').css({
        'padding': '0 0'
    });
    $('.ui-tabs-nav li a').css({
        'float': 'left',
        'padding': '0.1em 1em',
        'text-decoration': 'none',
        'font-size': '12px',
        'font-weight': 'normal',
        'font-family': '宋体'
    });
    
    
    $("#changepassword").click(function(){
    	if ($('#changepassword_tab').length==0) {
    		loadUrl='/back/selfmanager/changepassword.jsp';
        	addTab('changepassword_tab','自助密码修改');
    	} else { 
    		 $tabs.tabs("select", "#changepassword_tab");
    	} 
    });
    
});
