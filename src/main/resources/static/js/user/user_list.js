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
var pageSize = 20;
$(function(){
	load(curr);
    Public.initWarehouse(new Array("warehouseId", "warehouseId_e"));
    // 表单校验
    validate();
});

function load(pageNum){
	var key = $("#key").val();
	$.ajax({
        url: "../user/list",
        dataType: "json",
        type: "POST",
        data: JSON.stringify({ "pageNum":pageNum,
            "pageSize":pageSize,
            "key":key
        }),
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
                                "<td>"+Public.ifNull(item.name)+"</td>"+
                                "<td>"+Public.ifNull(item.mobile)+"</td>"+
                                "<td>"+Public.ifNull(item.roleInfo)+"</td>"+
                                "<td>"+Public.ifNull(item.warehouseName)+"</td>"+
                                "<td>"+Public.ifNull(item.stateInfo)+"</td>"+
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
                // 分页
                layui.laypage.render({
                    elem: 'page', // 指向存放分页的容器，值可以是容器ID、DOM对象。如：1. elem: 'id' 注意：这里不能加 # 号 2. elem: document.getElementById('id')
                    theme: '#009688', // 自定义主题
                    count: data.totalCount,   // 数据总数
                    limit: pageSize,           // 每页显示的条数
                    curr: pageNum || 1,           // 当前页
                    layout: ['count', 'prev', 'page', 'next', 'skip'], //自定义排版。可选值有：count（总条目输区域）、prev（上一页区域）、page（分页区域）、next（下一页区域）、limit（条目选项区域）、refresh（页面刷新区域。注意：layui 2.3.0 新增） 、skip（快捷跳页区域）
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

// 打开新增用户页面
function openSave(){
    // 打开模态窗口
    Public.openModal("saveModal");
    // 重置校验
    Public.resetValidate("save-form");
    // 添加校验
    validate();
}

// 新增用户
function save(){

    var validate = Public.doValidate("save-form");
    if(!validate){
        return;
    }
    var name = $("#name").val();
    var mobile = $("#mobile").val();
    var role = $("#role").val();
    var warehouseId = $("#warehouseId").val();
    var data = '{"name":"'+name+'","mobile":"'+mobile+'","warehouseId":"'+warehouseId+'","role":"'+role+'"}';
    $.ajax({
        url: "../user/add",
        dataType: "json",
        type: "POST",
        data: data,
        async: false,
        success: function (data) {
            if(data.flag){
                Public.closeModal("saveModal");
                Public.alert(1,"新增成功");
                load(1);
            }else{
                Public.alert(2,data.message);
            }
        }
    });

}

// 删除用户
function deleteById(id){

    Public.confirm('您确定要删除吗?', function(){
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
    });

}

// 启用/停用
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
                Public.alert(1, "修改成功！");
                load(curr);
            }else{
                Public.alert(2, data.message);
            }
        }
    });
}

// 打开修改用户页面
function edit(id){
    // 打开模态窗口
    Public.openModal("editModal");
    // 重置校验
    Public.resetValidate("edit-form");
    // 添加校验
    validate();

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
                $("#role_e").val(data.data.role);
                $("#id_e").val(data.data.id);
            }else{
                Public.alert(2,data.message);
            }
        }
    });

}

// 修改用户提交
function editSave(){
    var validate = Public.doValidate("edit-form");
    if(!validate){
        return;
    }
    var id = $("#id_e").val();
    var name = $("#name_e").val();
    var mobile = $("#mobile_e").val();
    var role = $("#role_e").val();
    var warehouseId = $("#warehouseId_e").val();
    var data = '{"name":"'+name+'","mobile":"'+mobile+'","id":"'+id+'","warehouseId":"'+warehouseId+'","role":"'+role+'"}';
    $.ajax({
        url: "../user/modify",
        dataType: "json",
        type: "POST",
        data: data,
        success: function (data) {
            if(data.flag){
                Public.closeModal("editModal");
                Public.alert(1,"修改成功！");
                load(curr);
            }else{
                Public.alert(2,data.message);
            }
        }
    });
}

// 密码重置
function resetPassword(id) {

    Public.confirm('您确定要重置密码吗?<br>（重置后密码：123456）', function(){
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
    });
}

function validate(){

    $('#save-form').bootstrapValidator({
        message: '不能为空', //为每个字段指定通用错误提示语
        feedbackIcons: {   //输入框不同状态，显示图片的样式
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        live : 'enabled', //enabled代表当表单控件内容发生变化时就触发验证，默认提交时验证，
        fields: {
            name: {
                validators: {
                    notEmpty: {message: '请输入姓名'},
                    stringLength: { max: 50, message: '不能超过50个字符'}
                }
            },
            mobile: {
                validators: {
                    regexp:{
                        regexp: /^1[3-9][\d]{9}$/,  //正则规则用两个/包裹起来
                        message: '请输入正确的手机号'
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
                        message: '请选择角色'
                    }
                }
            }
        }
    })

    $('#edit-form').bootstrapValidator({
        message: '不能为空', //为每个字段指定通用错误提示语
        feedbackIcons: {   //输入框不同状态，显示图片的样式
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        live : 'enabled', //enabled代表当表单控件内容发生变化时就触发验证，默认提交时验证，
        fields: {
            mobile_e: {
                validators: {
                    regexp:{
                        regexp: /^1[3-9][\d]{9}$/,  //正则规则用两个/包裹起来
                        message: '请输入正确的手机号'
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
                        message: '请选择角色'
                    }
                }
            }
        }
    })
}

