/**
 * 用户信息
 */
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
var curr = 1;
$(function(){
	load(curr);
    loadWarehouse();
    validate();
});

function loadWarehouse() {
    $.ajax({
        url: "../warehouse/list",
        dataType: "json",
        type: "GET",
        success: function (data) {
            var html= "";
            if(data.flag){
                $.each(data.data, function (i, item) {
                    html += "<option value=\""+item.id+"\">"+item.name+"</option>";
                });
                $("#warehouseId").html(html);
                $("#warehouseId_e").html(html);
            }
        }
    });

}

function load(cnt){
	var key = $("#key").val();
	$.ajax({
        url: "../user/list?key="+key+"&pageNum="+cnt+"&pageSize=10",
        dataType: "json",
        type: "GET",
        //data: {"name":name,"mobile":mobile,"pageNum":cnt,"pageSize":10},
        success: function (data) {
                var html= "";
                if(data.flag){
                	 $.each(data.data, function (i, item) {
                	     var stopStyle
                         var stopName ;
                	     if(item.state == "0"){
                             stopStyle = " btn-warning";
                             stopName = "停用"
                         }else{
                             stopStyle = "btn-success";
                             stopName = "启用"
                         }
                         html +="<tr>"+
                                    "<td>"+(i+1)+"</td>"+
                                    "<td>"+item.name+"</td>"+
                                    "<td>"+item.mobile+"</td>"+
                                    "<td>"+item.adminInfo+"</td>"+
                                    "<td>"+item.stateInfo+"</td>"+
                                    "<td>"+item.warehouseInfo+"</td>"+
                             "<td> <button class= \"btn btn-primary btn-xs\" onclick=\"edit('"+item.id+"');\">修改</button> " +
                             "<button class= \"btn btn-primary btn-xs "+stopStyle+"\" onclick=\"stopUsing('"+item.id+"');\">"+stopName+"</button> " +
                             "<button class= \"btn btn-primary btn-xs btn-danger\" onclick=\"deleteById('"+item.id+"');\">删除</button> "+
                             "<button class= \"btn btn-primary btn-xs btn-danger\" onclick=\"resetPassword('"+item.id+"');\">重置密码</button> </td>"+
                                    "</tr>";
                    });
                    if(html == ""){
                        html = "<tr><td colspan=\"7\">暂无数据</td></tr>";
                    }
                    $("#tbody").html(html);
                    laypage({
                        cont: 'page', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
                        pages: Math.ceil(data.totalCount / 10), //通过后台拿到的总页数
                        skin: "#49afcd",
                        curr: curr || 1, //当前页
                        jump: function (obj, first) { //触发分页后的回调
                            if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                                curr = obj.curr;
                                load(curr);
                            }
                        }
                    });
                }
        }
    });
}

function save(){

    var validate = doValidate("save-form");
    if(!validate){
        return;
    }
    var name = $("#name").val();
    var mobile = $("#mobile").val();
    var admin = $("#admin").val();
    var warehouseId = $("#warehouseId").val();
    var data = '{"name":"'+name+'","mobile":"'+mobile+'","admin":"'+admin+'","warehouseId":"'+warehouseId+'"}';
    $.ajax({
        url: "../user/add",
        dataType: "json",
        type: "POST",
        data: data,
        success: function (data) {
            if(data.flag){
                $("#saveModal input").val("");
                $("#saveModal select option:first").prop("selected", 'selected');
                document.getElementById("save-close-btn").click();
                Public.alert(1,"新增成功");
                load(1);
            }else{
                Public.alert(2,data.message);
            }
        }
    });

}

function deleteById(id){
    layer.confirm('您确定要删除吗?', {icon: 3, title:'提示'}, function(index){

        $.ajax({
            cache: true,
            type: "GET",
            url:"../user/delete/"+id ,
            async: false,
            error: function(request) {
                Public.alert(2,"请求失败！");
            },
            success: function(data) {
                if(data.flag){
                    Public.alert(1,"删除成功！");
                    load(curr);
                }else{
                    Public.alert(2,"删除失败！");
                }
            }
        });

        layer.close(index);
    });

}

function stopUsing(id){
    $.ajax({
        cache: true,
        type: "GET",
        url:"../user/stopUsing/"+id ,
        async: false,
        error: function(request) {
            Public.alert(2,"请求失败！");
        },
        success: function(data) {
            if(data.flag){
                Public.alert(1,data.message);
                load(curr);
            }else{
                Public.alert(2,data.message);
            }
        }
    });
}

function edit(id){
    $('#editModal').modal('show');
    loadOneUser(id);
}

function loadOneUser(id){
    $.ajax({
        cache: true,
        type: "GET",
        url:"../user/get/"+id ,
        async: false,
        error: function(request) {
            Public.alert(2,"请求失败！");
        },
        success: function(data) {
            if(data.flag){
                $("#name_e").val(data.data.name);
                $("#mobile_e").val(data.data.mobile);
                $("#warehouseId_e").val(data.data.warehouseId);
                $("#admin_e").val(data.data.admin);
                $("#id_e").val(data.data.id);
            }else{
                Public.alert(2,data.message);
            }
        }
    });

}

function editSave(){
    var validate = doValidate("edit-form");
    if(!validate){
        return;
    }
    var id = $("#id_e").val();
    var name = $("#name_e").val();
    var mobile = $("#mobile_e").val();
    var admin = $("#admin_e").val();
    var warehouseId = $("#warehouseId_e").val();
    var data = '{"name":"'+name+'","mobile":"'+mobile+'","id":"'+id+'","admin":"'+admin+'","warehouseId":"'+warehouseId+'"}';
    $.ajax({
        url: "../user/modify",
        dataType: "json",
        type: "POST",
        data: data,
        success: function (data) {
            if(data.flag){
                $("#editModal input").val("");
                $("#editModal select option:first").prop("selected", 'selected');
                document.getElementById("edit-close-btn").click();
                Public.alert(1,"修改成功！");
                load(curr);
            }else{
                Public.alert(2,data.message);
            }
        }
    });
}

function openChange(id){
    $('#changePasswordModal').modal('show');
    $.ajax({
        cache: true,
        type: "GET",
        url:"../user/get/"+id ,
        async: false,
        error: function(request) {
            Public.alert(2,"请求失败！");
        },
        success: function(data) {
            if(data.flag){
                $("#name_c").val(data.data.name);
                $("#mobile_c").val(data.data.mobile);
                $("#id_c").val(data.data.id);
            }else{
                Public.alert(2,data.message);
            }
        }
    });
}

// 修改密码
function changePassword() {
    var validate = doValidate("changePassword-form");
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
                $("#changePasswordModal input").val("");
                $("#changePasswordModal select option:first").prop("selected", 'selected');
                document.getElementById("change-close-btn").click();
                Public.alert(1,"修改成功！");
            }else{
                Public.alert(2,data.message);
            }
        }
    });

}

// 密码重置
function resetPassword(id) {

    layer.confirm('您确定要重置密码吗?（重置后密码：123456）', {icon: 3, title:'提示'}, function(index){

        $.ajax({
            cache: true,
            type: "GET",
            url:"../user/resetPassword/"+id ,
            async: false,
            error: function(request) {
                Public.alert(2,"请求失败！");
            },
            success: function(data) {
                if(data.flag){
                    Public.alert(1,"重置成功！");
                    load(curr);
                }else{
                    Public.alert(2,"重置失败！");
                }
            }
        });

        layer.close(index);
    });
}

function validate(){

    $('#save-form').bootstrapValidator({
        live : 'enabled', //enabled代表当表单控件内容发生变化时就触发验证，默认提交时验证，
        fields: {
            name: {
                validators: {
                    notEmpty: {
                        message: '请输入姓名'
                    }
                }
            },
            mobile: {
                validators: {
                    regexp:{
                        regexp: /^1[3-9][\d]{9}$/,  //正则规则用两个/包裹起来
                        message: '请输入正确的手机号'
                    },
                    notEmpty: {
                        message: '请输入手机号'
                    }
                }
            },
            warehouseId: {
                validators: {
                    notEmpty: {
                        message: '请选择仓库'
                    }
                }
            },
            admin: {
                validators: {
                    notEmpty: {
                        message: '请选择是否为管理员'
                    }
                }
            },
            password: {
                validators: {
                    notEmpty: {
                        message: '请输入密码'
                    },
                    identical: {
                        field: 'password2',
                        message: '两次输入的密码不相符'
                    }
                }
            },
            password2: {
                validators: {
                    notEmpty: {
                        message: '请再次输入密码'
                    },
                    identical: {
                        field: 'password',
                        message: '两次输入的密码不相符'
                    }
                }
            }
        }
    })

    $('#edit-form').bootstrapValidator({
        live : 'enabled', //enabled代表当表单控件内容发生变化时就触发验证，默认提交时验证，
        fields: {
            mobile_e: {
                validators: {
                    regexp:{
                        regexp: /^1[3-9][\d]{9}$/,  //正则规则用两个/包裹起来
                        message: '请输入正确的手机号'
                    },
                    notEmpty: {
                        message: '请输入手机号'
                    }
                }
            },
            warehouseId: {
                validators: {
                    notEmpty: {
                        message: '请选择仓库'
                    }
                }
            },
            admin: {
                validators: {
                    notEmpty: {
                        message: '请选择是否为管理员'
                    }
                }
            }
        }
    })


    $('#changePassword-form').bootstrapValidator({
        live : 'enabled', //enabled代表当表单控件内容发生变化时就触发验证，默认提交时验证，
        fields: {
            password_c: {
                validators: {
                    notEmpty: {
                        message: '请输入密码'
                    },
                    identical: {
                        field: 'password_c2',
                        message: '两次输入的密码不相符'
                    }
                }
            },
            password_c2: {
                validators: {
                    notEmpty: {
                        message: '请再次输入密码'
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

function doValidate(id){
    $("#"+id).data('bootstrapValidator').resetForm();
    $("#"+id).data('bootstrapValidator').validate();
    var flag = $("#"+id).data("bootstrapValidator").isValid();
    return flag;
}


