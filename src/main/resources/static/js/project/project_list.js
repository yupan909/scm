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
var user = Public.getCurrentUser();
$(function(){
    // 页面权限
    if(user.admin == "0"){
        $(".admin").remove();
    }
    load(curr);
    validate();
});


// 列表
function load(pageNum){
    var name = $("#key").val();
    $.ajax({
        url: "../project/list?name="+name+"&pageNum="+pageNum+"&pageSize="+pageSize,
        dataType: "json",
        type: "GET",
        success: function (data) {
            var html= "";
            if(data.flag){
                $.each(data.data, function (i, item) {
                    var handel = "";
                    if(user.admin == "1"){
                        var stopStyle
                        var stopName ;
                        if(item.state == "0"){
                            stopStyle = " btn-warning";
                            stopName = "停用"
                        }else{
                            stopStyle = "btn-success";
                            stopName = "启用"
                        }
                        handel = "<td> <button class= \"btn btn-primary btn-xs\" onclick=\"edit('"+item.id+"');\">修改</button> " +
                            "<button class= \"btn btn-primary btn-xs "+stopStyle+"\" onclick=\"stopUsing('"+item.id+"');\">"+stopName+"</button> " +
                            "<button class= \"btn btn-primary btn-xs btn-danger\" onclick=\"deleteById('"+item.id+"');\">删除</button> </td>";
                    }
                    html +="<tr>"+
                        "<td>"+(i+1)+"</td>"+
                        "<td>"+Public.ifNull(item.name)+"</td>"+
                        "<td>"+Public.ifNull(item.content)+"</td>"+
                        "<td>"+Public.ifNull(item.stateInfo)+"</td>"+
                        handel+
                        "</tr>";
                });
                if(html == ""){
                    html = "<tr><td colspan=\"5\">暂无数据</td></tr>";
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

// 新增工程
function save(){
    var validate = Public.doValidate("save-form");
    if(!validate){
        return;
    }
    var name = $("#name").val();
    var content = $("#content").val();
    var data = '{"name":"'+name+'","content":"'+content+'"}';
    $.ajax({
        url: "../project/save",
        dataType: "json",
        type: "POST",
        data: data,
        success: function (data) {
            if(data.flag){
                $("#saveModal input").val("");
                $("#saveModal textarea").val("");
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

/**
 * 删除工程
 * @param id
 */
function deleteById(id){
    layui.layer.confirm('您确定要删除吗?', {icon: 3, title:'提示'}, function(index){

        $.ajax({
            cache: true,
            type: "GET",
            url:"../project/delete/"+id ,
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

        layui.layer.close(index);
    });


}

/**
 * 编辑工程页面
 * @param id
 */
function edit(id){
    $('#editModal').modal('show');
    loadOneProject(id);
}

function loadOneProject(id){
    $.ajax({
        cache: true,
        type: "GET",
        url:"../project/getProject/"+id ,
        async: false,
        error: function(request) {
            Public.alert(2,"请求失败！");
        },
        success: function(data) {
            if(data.flag){
                $("#name_e").val(data.data.name);
                $("#content_e").val(data.data.content);
                $("#id_e").val(data.data.id);
            }else{
                Public.alert(2,data.message);
            }
        }
    });

}

// 编辑提交
function editSave(){
    var validate = Public.doValidate("edit-form");
    if(!validate){
        return;
    }
    var id = $("#id_e").val();
    var name = $("#name_e").val();
    var content = $("#content_e").val();
    var data = '{"name":"'+name+'","content":"'+content+'","id":"'+id+'"}';
    $.ajax({
        url: "../project/modify",
        dataType: "json",
        type: "POST",
        data: data,
        success: function (data) {
            if(data.flag){
                $("#editModal input").val("");
                $("#editModal textarea").val("");
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

// 启用禁用
function stopUsing(id){
    $.ajax({
        cache: true,
        type: "GET",
        url:"../project/stopUsing/"+id ,
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

// 表单校验初始化
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
                    notEmpty: {message: '请输入工程名称'},
                    stringLength: { max: 50, message: '不能超过50个字符'}
                }
            },
            content: {
                validators: {
                    notEmpty: {message: '请输入设计内容'},
                    stringLength: { max: 500, message: '不能超过500个字符'}
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
            name_e: {
                validators: {
                    notEmpty: {message: '请输入工程名称'},
                    stringLength: { max: 50, message: '不能超过50个字符'}
                }
            },
            content_e: {
                validators: {
                    notEmpty: {message: '请输入设计内容'},
                    stringLength: { max: 500, message: '不能超过500个字符'}
                }
            }
        }
    })

}
