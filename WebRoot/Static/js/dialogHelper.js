dialogHelper = function(){
    var m_title = "";
    var m_msg = "";
    var m_btns = null;
    var m_width = 280;
    var m_height = 360;
    var m_position = "center";
    
	//这部分可根据情况自定义
    this.dlgDiv = $("<div><p><span class=\"ui-icon ui-icon-alert\" style=\"float: left; margin: 0 7px 20px 0;\"></span></p></div>");
    this.set_Title = function(val){
        this.m_title = val;
    }
    this.get_Title = function(){
        return this.m_title;
    }
    this.set_Msg = function(val){
        this.m_msg = val;
    }
    this.get_Msg = function(){
        return this.m_msg;
    }
    this.set_Buttons = function(val){
        this.m_btns = val;
    }
    this.get_Buttons = function(){
        return this.m_btns;
    }
	
	this.set_Width = function(val){
		this.m_width = val;
	}
	this.get_Width = function(){
		return this.m_width;
	}
	this.set_Height = function(val){
		this.m_height = val;
	}
	this.get_Height = function(){
		return this.m_height;
	}
	this.set_Position = function(val){
		this.m_position = val;
	}
	this.get_Position = function(){
		return this.m_position;
	}
    
    this.open = function(){
        $dlg = this.dlgDiv.clone(); //这个克隆很重要,否则反复添加正文。
        $dlg.children().filter("p").html(this.dlgDiv.children().filter("p").html() + this.get_Msg()); //增加自定义消息
        $dlg.dialog({
			modal: true,
            //show: 'blind',
            //hide: 'explode',
            position: this.get_Position(),
            height: this.get_Height(),
            width: this.get_Width(),
            title: this.get_Title(),
            buttons: this.get_Buttons()
        });
    }
}
