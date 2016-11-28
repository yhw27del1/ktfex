<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
	
	function name_formatter(val,row,index){
		if(null==val||""==val){
			return "无";
		}else{
			if(row.TYPE=='合计'){
				return val;
			}else{
				var m = "${menuMap['name']}";
				if(m=='inline'){
					return val;
				}else{
					var show = 1;//显示第一位
					var size = val.length;
					if(size<show){
						show = size;
					}
					var f = val.substr(0,show);
					return f+'****';
				}
			}
		}
	}
	
	function phone_formatter(val,row,index){
		if(null==val||""==val){
			return "无";
		}else{
			if(row.TYPE=='合计'){
				return val;
			}else{
				var m = "${menuMap['phone']}";
				if(m=='inline'){
					return val;
				}else{
					var show = 4;//显示后四位
					var size = val.length;
					if(size<show){
						return "****";
					}else{
						return '****'+val.substr(size-show);
					}
				}
			}
		}
	}
	
	function idcard_formatter(val,row,index){
		if(null==val||""==val){
			return "无";
		}else{
			if(row.TYPE=='合计'){
				return val;
			}else{
				var m = "${menuMap['idcard']}";
				if(m=='inline'){
					return val;
				}else{
					var show = 4;//显示后四位
					var size = val.length;
					if(size<show){
						return "****";
					}else{
						return '****'+val.substr(size-show);
					}
				}
			}
		}
	}
	
	function bankcard_formatter(val,row,index){
		if(null==val||""==val){
			return "无";
		}else{
			if(row.TYPE=='合计'){
				return val;
			}else{
				var m = "${menuMap['bankcard']}";
				if(m=='inline'){
					return val;
				}else{
					var show = 4;//显示后四位
					var size = val.length;
					if(size<show){
						return "****";
					}else{
						return '****'+val.substr(size-show);
					}
				}
			}
		}
	}
	
	function name(val){
		if(null==val||""==val){
			return "无";
		}else{
			var m = "${menuMap['name']}";
			if(m=='inline'){
				return val;
			}else{
				var show = 1;//显示第一位
				var size = val.length;
				if(size<show){
					show = size;
				}
				var f = val.substr(0,show);
				return f+'****';
			}
		}
	}
	
	function phone(val){
		if(null==val||""==val){
			return "无";
		}else{
			var m = "${menuMap['phone']}";
			if(m=='inline'){
				return val;
			}else{
				var show = 4;//显示后四位
				var size = val.length;
				if(size<show){
					return "****";
				}else{
					return '****'+val.substr(size-show);
				}
			}
		}
	}
	
	function idcard(val){
		if(null==val||""==val){
			return "无";
		}else{
			var m = "${menuMap['idcard']}";
			if(m=='inline'){
				return val;
			}else{
				var show = 4;//显示后四位
				var size = val.length;
				if(size<show){
					return "****";
				}else{
					return '****'+val.substr(size-show);
				}
			}
		}
	}
	
	function bankcard(val){
		if(null==val||""==val){
			return "无";
		}else{
			var m = "${menuMap['bankcard']}";
			if(m=='inline'){
				return val;
			}else{
				var show = 4;//显示后四位
				var size = val.length;
				if(size<show){
					return "****";
				}else{
					return '****'+val.substr(size-show);
				}
			}
		}
	}
