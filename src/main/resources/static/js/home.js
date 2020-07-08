$.ajaxSetup({
    contentType:"application/json;charset=utf-8",
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
            if(data.flag){
                Public.setCurrentUser(data.data);
                if(data.data.admin == "1"){
                    $(".normal").remove();
                }else if(data.data.admin == "0"){
                    $(".admin").remove();
                }
                $("#currentUserName").html(data.data.name);
                $("#warehouseName").html(data.data.warehouseName);
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

    // 表单校验
    validateChangePassword();
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

// 打开修改密码页面
function openChange(id){
    var user = Public.getCurrentUser();
    if (!user) {
        Public.alert(2, "获取用户信息失败！");
    }
    // 打开模态窗口
    Public.openModal("changePasswordModal");
    // 重置校验
    Public.resetValidate("changePassword-form");
    // 添加校验
    validateChangePassword();

    // 回显数据
    $.ajax({
        cache: true,
        type: "GET",
        url:"../user/get/"+user.id ,
        async: false,
        error: function(request) {
            Public.alert(2,"请求失败！");
        },
        success: function(data) {
            if(data.flag){
                $("#name_c").val(data.data.name);
                $("#id_c").val(data.data.id);
            }else{
                Public.alert(2,data.message);
            }
        }
    });
}

// 提交修改密码
function changePassword() {
    var validate = Public.doValidate("changePassword-form");
    if(!validate){
        return;
    }
    var id = $("#id_c").val();
    var password = $("#password_c").val();
    var data = '{"id":"'+id+'","password":"'+password+'"}';
    $.ajax({
        url: "../user/updatePassword",
        dataType: "json",
        type: "POST",
        data: data,
        success: function (data) {
            if(data.flag){
                Public.closeModal("changePasswordModal");
                Public.alert(1,"修改成功，请重新登录！", function(){
                    window.location.href="../login.html";
                });
            }else{
                Public.alert(2,data.message);
            }
        }
    });

}

// 校验修改密码页面
function validateChangePassword() {
    $('#changePassword-form').bootstrapValidator({
        message: '不能为空', //为每个字段指定通用错误提示语
        feedbackIcons: {   //输入框不同状态，显示图片的样式
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        live : 'enabled', //enabled代表当表单控件内容发生变化时就触发验证，默认提交时验证，
        fields: {
            password_c: {
                validators: {
                    notEmpty: {message: '请输入密码'}, //非空提示
                    stringLength: { min: 6, max: 20, message: '密码长度必须在6到20之间'}, ///长度提示
                    regexp: {
                        regexp: /^[a-zA-Z0-9_\.]+$/,
                        message: '密码只能包含字母、数字、下划线和小数点'
                    },
                    identical: {
                        field: 'password_c2',
                        message: '两次输入的密码不相符'
                    }
                }
            },
            password_c2: {
                validators: {
                    notEmpty: {message: '请再次输入密码'}, //非空提示
                    stringLength: { min: 6, max: 20, message: '密码长度必须在6到20之间'}, ///长度提示
                    regexp: {
                        regexp: /^[a-zA-Z0-9_\.]+$/,
                        message: '密码只能包含字母、数字、下划线和小数点'
                    },
                    identical: {
                        field: 'password_c',
                        message: '两次输入的密码不相符'
                    }
                }
            }
        }
    })
}
