//mid2为二级菜单id
//accordionId为使用accordion的id
function gotoMenu(mid2, accordionId){
	var $mid2 = $("a[mid2='" + mid2 + "']");
    $mid2.trigger('click');
    if (accordionId) {
		var $accordion = $("#"+accordionId);
		var aid = $("div",$accordion).index($mid2.parent().parent().parent());//找索引的方法
        $accordion.accordion({
            active: aid
        });
    }
    $("a[mid2]").removeClass("ui-state-active");
    $("a[mid2='" + mid2 + "']").addClass("ui-state-active");
}

function setTitle(mid2,newTitle){
	$("a[href='#"+mid2+"']",parent.document).text(newTitle);
}

//mid2动态取
function setTitle2(newTitle){
	var iframemid2 = $("iframe",parent.document).attr("id");
	var mid2 = iframemid2.substring(6);
	//alert(iframemid2);
	//alert(mid2);
	$("a[href='#"+mid2+"']",parent.document).text(newTitle);
}
//addHeight必须是数字
function setIframeHeight(addHeight){
	var iframe = $("iframe",parent.document);
	//alert(iframe.height());
	iframe.height(iframe.height()+addHeight);
	//alert(iframe.height());
}

/**
 * 时间对象的格式化;
 */
Date.prototype.format = function(format) {
	/*
	 * eg:format="yyyy-MM-dd hh:mm:ss";
	 */
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
		// millisecond
	}

	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4
						- RegExp.$1.length));
	}

	for (var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1
							? o[k]
							: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
}
/*超链接文字提示*/
$(function(){
    var x = 10;  
	var y = 20;
	$("a.tooltip").mouseover(function(e){
       	this.myTitle = this.title;
		this.title = "";	
	    var tooltip = "<div id='tooltip'>"+ this.myTitle +"</div>"; //创建 div 元素
		$("body").append(tooltip);	//把它追加到文档中
		$("#tooltip")
			.css({
				"top": (e.pageY+y) + "px",
				"left": (e.pageX+x)  + "px"
			}).show("fast");	  //设置x坐标和y坐标，并且显示
    }).mouseout(function(){		
		this.title = this.myTitle;
		$("#tooltip").remove();   //移除 
    }).mousemove(function(e){
		$("#tooltip")
			.css({
				"top": (e.pageY+y) + "px",
				"left": (e.pageX+x)  + "px"
			});
	});
})
/*超链接文字提示*/
$(function(){
	var x = 10;
	var y = 20;
	$("a.tooltipImg").mouseover(function(e){
		this.myTitle = this.title;
		this.title = "";	
		var tooltip = "<div id='tooltipImg'><img src='"+ this.href +"' alt='图片预览'/><\/div>"; //创建 div 元素
		$("body").append(tooltip);	//把它追加到文档中						 
		$("#tooltipImg")
			.css({
				"top": (e.pageY+y) + "px",
				"left":  (e.pageX+x)  + "px"
			}).show("fast");	  //设置x坐标和y坐标，并且显示
    }).mouseout(function(){
		this.title = this.myTitle;	
		$("#tooltipImg").remove();	 //移除 
    }).mousemove(function(e){
		$("#tooltipImg")
			.css({
				"top": (e.pageY+y) + "px",
				"left":  (e.pageX+x)  + "px"
			});
	});
})

/*超链接文字提示*/
$(function(){
	var x = 10;
	var y = 20;
	$("a.tooltipImg2").mouseover(function(e){
		this.myTitle = this.title;
		this.title = ""; 
		//alert("hrefUrl="+hrefUrl);
		var tooltip = "<div id='tooltipImg2'><img src='"+ this.href +"' alt='图片预览'/  width='600px' height='400px'><\/div>"; //创建 div 元素
		$("body").append(tooltip);	//把它追加到文档中						 
		$("#tooltipImg2")
			.css({
				"top": (e.pageY+y) + "px",
				"left":  (e.pageX+x)  + "px"
			}).show("fast");	  //设置x坐标和y坐标，并且显示
    }).mouseout(function(){
		this.title = this.myTitle;	
		$("#tooltipImg2").remove();	 //移除 
    }).mousemove(function(e){
		$("#tooltipImg2")
			.css({
				"top": (e.pageY+y) + "px",
				"left":  (e.pageX+x)  + "px"
			});
	});
})
 

/*超链接文字提示*/
$(function(){
	var x = 10;
	var y = 20;
	$("a.tooltipImg3").mouseover(function(e){
		this.myTitle = this.title;
		this.title = "";
		var hrefUrl=$("img",this).attr("src");
		//alert("hrefUrl="+hrefUrl);
		//var tooltip = "<div id='tooltipImg3'><img src='"+ hrefUrl +"' alt='图片预览'/  width='600px' height='400px'><\/div>"; //创建 div 元素
		//alert($("body").html());
		//$("body").append(tooltip);	//把它追加到文档中		
		$("img","#tooltipImg3").attr("src",hrefUrl);
		$("#tooltipImg3")
			.css({
				"top": (e.pageY+y) + "px",
				"left":  (e.pageX+x)  + "px"
			}).show("fast");	  //设置x坐标和y坐标，并且显示
    }).mouseout(function(){
		this.title = this.myTitle;	
		//$("#tooltipImg3").remove();	 //移除 
		$("#tooltipImg3").hide();
    }).mousemove(function(e){
		$("#tooltipImg3")
			.css({
				"top": (e.pageY+y) + "px",
				"left":  (e.pageX+x)  + "px"
			});
	});
})