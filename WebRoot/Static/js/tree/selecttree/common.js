 
//用DIV模拟出树形select
function Offset(e) 
{ 
//取标签的绝对位置 
	var t = e.offsetTop; 
	var l = e.offsetLeft; 
	var w = e.offsetWidth-2; 
	var h = e.offsetHeight-2; 
	while(e=e.offsetParent) 
	{ 
		t+=e.offsetTop; 
		l+=e.offsetLeft; 
	} 
	return { 
		top : t, 
		left : l, 
		width : w, 
		height : h 
	};
} 
function initSelect(classType,title,obj,url){  
	var $Obj = $("#"+obj);
	var $Obj_name = $("#"+obj+"_name");
	var offset_text=Offset($Obj.get(0));  
	$Obj.css("display","none"); 
	var $iDiv = $("<div id='selectof"+obj+"'></div>"); 
	$iDiv.css("width",offset_text.width+"px");
	$iDiv.css("height",offset_text.height+"px");
	$iDiv.css("z-index","2");
	$iDiv.css("background","url(images/icon_select.gif) no-repeat right 4px");
	$iDiv.css("border","1px solid #7C9BCF");
	$iDiv.css("fontSize","12px");
	$iDiv.css("lineHeight",offset_text.height+"px");
	$iDiv.css("textIndent","4px");
	if($Obj_name.val()==""||$Obj_name.val()==null){
		$iDiv.text(title);
	}else{
		$iDiv.text($("#"+obj+"_name").val());
	}
	$Obj.parent().append($iDiv); 
	$iDiv.click(function(){
		if($("#selectchild"+obj).length==1){ 
			if(($("#selectchild"+obj+":hidden").length==1)){ 
				var offset_div = Offset($iDiv.get(0)); 
				$("#selectchild"+obj).css("top",offset_div.top+offset_div.height+2+"px");
				$("#selectchild"+obj).css("left",offset_div.left+"px"); 
				$("#selectchild"+obj).css("display","block");
			}else{ 
				$("#selectchild"+obj).css("display","none");
			}
		}else{
 
			var $cDiv = $("<div id='selectchild"+obj+"'></div>");
			$cDiv.css("position","absolute");
			$cDiv.css("top",offset_text.top+offset_text.height+2+"px");
			$cDiv.css("left",offset_text.left+"px");
			//$cDiv.css("width",offset_text.width+"px");
			$cDiv.css("z-index","3");
			$cDiv.css("font-size","12px");
			$cDiv.css("background","#f7f7f7");
			$cDiv.css("border","1px solid silver");
			$deptDiv = $("<div></div>"); 
			conf={data:{
				type:"json",  			
				url:url
				},
				ui : {
					theme_name : classType,
					context: [{visible:true}]
				},
				callback:{
					 onselect: function(node) {  
					 	if(node.getAttribute("selectable")!="false"){ 
							$iDiv.text($(node).children().eq(0).text());
							//将值写入可能有的隐藏域
							$Obj_name.val($(node).children().eq(0).text());
							//改变容器元素的value
							$("#"+obj).val(node.id);
							$("#"+obj).change();
							$cDiv.css("display","none");
						}					
					}
				}			
			};
			var treeFunc = $.tree_create(); 
			treeFunc.init($deptDiv, $.extend({},conf));
			$cDiv.append($deptDiv);
			$("body").append($cDiv);
		}
	});
}