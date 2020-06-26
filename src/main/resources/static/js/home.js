$.ajaxSetup({     
    contentType:"application/x-www-form-urlencoded;charset=utf-8",     
    complete:function(XMLHttpRequest,textStatus){   
     //通过XMLHttpRequest取得响应头，sessionstatus，    
      var sessionstatus=XMLHttpRequest.getResponseHeader("sessionstatus");
             if(sessionstatus=="timeout"){  
            	 Public.alert(3,"用户信息过期,请重新登录！",function(){
 					window.location.href="../login.html";
 				 });  
             }     
       }     
});

//退出
function loginOut(){
	$.ajax({
        cache: true,
        type: "GET",
        url:"../user/logout",
        async: false,
        error: function(request) {
        	Public.alert(2,"服务器出现异常！");
        },
        success: function(data) {
        	window.location.href="../login.html";   
        }
    });
	
}

$(function(){
	
    /**
     * 加载权限
     */
	$.ajax({
        cache: true,
        type: "GET",
        url:"../user/authority",
        async: false,
        error: function(request) {
        	Public.alert(2,"服务器出现异常！");
        },
        success: function(data) {
            if(data.flag ){
                if(data.data.admin == "1"){
                    //$(".normal").remove();
                }else if(data.data.admin == "0"){
                    $(".admin").remove();
                }
                $("#currentUserName").html(data.data.name);
                var now = new Date();
                var weekNum = now.getDay(); //当月第一天星期几
                var week = new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
                var dateHtml = now.getFullYear()+"年"+(now.getMonth()+1)+"月"+now.getDate()+"日   "+week[weekNum];
                $("#currentTimeInfo").html(dateHtml);
            }
        }
    });
	

	$("div.main").load($("ul.nav-sidebar li.active").attr("id"));
	
	/**
	 * 左侧列表切换事件
	 */
	$("ul.nav-sidebar li").click(function(e){
		$("ul.nav-sidebar li.active").removeClass("active");
		$(this).addClass("active");
		
		if($(this).attr("id")){
			$("div.main").load($(this).attr("id"));
		}else{
			var $span = $(this).children("a").children("span:last");
			var name = $(this).attr("name");
			if($span.attr("class")=="glyphicon glyphicon-menu-down"){
				$span.removeClass("glyphicon-menu-down").addClass("glyphicon-menu-up");
				//展开子列表
				$("[name='"+name+"'].child_li").show(200);
			}else{
				$span.removeClass("glyphicon-menu-up").addClass("glyphicon-menu-down");
				//隐藏子列表
				$("[name='"+name+"'].child_li").hide(200);
			}
		}
		
		e.stopPropagation();
	});
	
	
	//我的消息--待读提醒
	//getMsgCount();
	
});

//我的消息--待读总条数
function getMsgCount(){
	/**
     * 加载权限
     */
	$.ajax({
        cache: true,
        type: "POST",
        url:"../getMsgCount.do",
        async: false,
        error: function(request) {
        	Public.alert(2,"服务器出现异常！");
        },
        success: function(data) {
        	if(data!=null && data.count>0){
        		$("#msg_count").html(data.count).show();
        	}else{
        		$("#msg_count").hide();
        	}
        }
    });
}