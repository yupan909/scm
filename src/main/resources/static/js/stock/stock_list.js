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
var user = Public.getCurrentUser();
$(function(){
    if(user.admin == "0"){
        $(".admin").remove();
    }
    load(curr);
    Public.initWarehouse("warehouseId", "warehouseId_e");
    validate();

    //开始时间
    layui.laydate.render({
        elem: '#startDate'
    });
    //结束时间
    layui.laydate.render({
        elem: '#endDate'
    });
});

// 列表
function load(pageNum){
    var key = $("#key").val();
    $.ajax({
        url: "../stock/list?key="+key+"&pageNum="+pageNum+"&pageSize="+pageSize,
        dataType: "json",
        type: "GET",
        success: function (data) {
            var html= "";
            if(data.flag){
                $.each(data.data, function (i, item) {
                    var buttonInfo = "";
                    if(user.admin == "1"){
                        buttonInfo = "<button class= \"btn btn-primary btn-xs\" onclick=\"edit('"+item.id+"');\">修改物资</button> " +
                            "<button class= \"btn btn-info btn-xs\" onclick=\"editCount('"+item.id+"');\">修改库存</button> "
                    }

                    html +="<tr>"+
                        "<td>"+(i+1)+"</td>"+
                        "<td>"+Public.ifNull(item.product)+"</td>"+
                        "<td>"+Public.ifNull(item.model)+"</td>"+
                        "<td>"+Public.ifNull(item.unit)+"</td>"+
                        "<td>"+Public.ifNull(item.count)+"</td>"+
                        "<td>"+Public.ifNull(item.warehouseName)+"</td>"+
                        "<td>"+Public.ifNull(item.createTime)+"</td>"+
                        "<td> " + buttonInfo + "<button class= \"btn btn-success btn-xs\" onclick=\"detail('"+item.id+"');\">明细</button>" +
                        " </td>"+
                        "</tr>";
                });
                if(html == ""){
                    html = "<tr><td colspan=\"8\">暂无数据</td></tr>";
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

// 新增库存
function save(){
    var validate = Public.doValidate("save-form");
    if(!validate){
        return;
    }
    var product = $("#product").val();
    var model = $("#model").val();
    var unit = $("#unit").val();
    var count = $("#count").val();
    var warehouseId = $("#warehouseId").val();
    var data = '{"product":"'+product+'","model":"'+model+'","unit":"'+unit+'","count":"'+count+'","warehouseId":"'+warehouseId+'"}';
    $.ajax({
        url: "../stock/init",
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

// 打开修改物资页面
function edit(id){
    $('#editModal').modal('show');
    loadOne(id);
}
function loadOne(id){
    $.ajax({
        cache: true,
        type: "GET",
        url:"../stock/get/"+id ,
        async: false,
        error: function(request) {
            Public.alert(2,"请求失败！");
        },
        success: function(data) {
            if(data.flag){
                $("#product_e").val(data.data.product);
                $("#model_e").val(data.data.model);
                $("#unit_e").val(data.data.unit);
                $("#warehouseId_e").val(data.data.warehouseId);
                $("#id_e").val(data.data.id);
            }else{
                Public.alert(2,data.message);
            }
        }
    });

}

// 修改物资提交
function editSave(){
    var validate = Public.doValidate("edit-form");
    if(!validate){
        return;
    }
    var id = $("#id_e").val();
    var product = $("#product_e").val();
    var model = $("#model_e").val();
    var unit = $("#unit_e").val();
    var warehouseId = $("#warehouseId_e").val();
    var data = '{"product":"'+product+'","model":"'+model+'","id":"'+id+'","unit":"'+unit+'","warehouseId":"'+warehouseId+'"}';
    $.ajax({
        url: "../stock/modifyStockInfo",
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

// 打开修改库存页面
function editCount(id){
    $('#editModal_count').modal('show');
    loadOneForCount(id);
}

function loadOneForCount(id){
    $.ajax({
        cache: true,
        type: "GET",
        url:"../stock/get/"+id ,
        async: false,
        error: function(request) {
            Public.alert(2,"请求失败！");
        },
        success: function(data) {
            if(data.flag){
                $("#id_count_e").val(data.data.id);
                $("#count_e").val(data.data.count);
            }else{
                Public.alert(2,data.message);
            }
        }
    });
}

// 修改库存提交
function editCountSave(){
    var validate = Public.doValidate("edit-form-count");
    if(!validate){
        return;
    }
    var id = $("#id_count_e").val();
    var count = $("#count_e").val();
    var data = '{"count":"'+count+'","id":"'+id+'"}';
    $.ajax({
        url: "../stock/modifyStockCount",
        dataType: "json",
        type: "POST",
        data: data,
        success: function (data) {
            if(data.flag){
                $("#editModal_count input").val("");
                $("#editModal_count select option:first").prop("selected", 'selected');
                document.getElementById("edit-count-close-btn").click();
                Public.alert(1,"修改成功！");
                load(curr);
            }else{
                Public.alert(2,data.message);
            }
        }
    });

}

// 库存明细
var detailCnt = 1;
var detailPageSize = 10;
function detail(id){
    $("#detail_id").val(id);
    $('#detail').modal('show');
    loadDetail(detailCnt);

}

function loadDetail(pageNum){
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    var detailId = $("#detail_id").val();
    $.ajax({
        url: "../stock/detail?id="+detailId+"&pageNum="+pageNum+"&pageSize="+detailPageSize+"&startDate="+startDate+"&endDate="+endDate,
        dataType: "json",
        type: "GET",
        success: function (data) {
            var html= "";
            if(data.flag){
                $.each(data.data, function (i, item) {
                    html +="<tr>"+
                        "<td>"+(i+1)+"</td>"+
                        "<td>"+Public.ifNull(item.typeInfo)+"</td>"+
                        "<td>"+Public.ifNull(item.count)+"</td>"+
                        "<td>"+Public.ifNull(item.project)+"</td>"+
                        "<td>"+Public.ifNull(item.createUser)+"</td>"+
                        "<td>"+Public.ifNull(item.createTime)+"</td>"+
                        "</tr>";
                });
                if(html == ""){
                    html = "<tr><td colspan=\"6\">暂无数据</td></tr>";
                }
                $("#tdetailbody").html(html);
                // 分页
                layui.laypage.render({
                    elem: 'detailpage', // 指向存放分页的容器，值可以是容器ID、DOM对象。如：1. elem: 'id' 注意：这里不能加 # 号 2. elem: document.getElementById('id')
                    theme: '#009688', // 自定义主题
                    count: data.totalCount,   // 数据总数
                    limit: detailPageSize,           // 每页显示的条数
                    curr: pageNum || 1,           // 当前页
                    layout: ['count', 'prev', 'page', 'next', 'skip'], //自定义排版。可选值有：count（总条目输区域）、prev（上一页区域）、page（分页区域）、next（下一页区域）、limit（条目选项区域）、refresh（页面刷新区域。注意：layui 2.3.0 新增） 、skip（快捷跳页区域）
                    jump: function (obj, first) { //触发分页后的回调
                        if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                            detailCnt = obj.curr;
                            load(detailCnt);
                        }
                    }
                });
            }
        }
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
            product: {
                validators: {
                    notEmpty: {message: '请输入物资名称'},
                    stringLength: { max: 50, message: '不能超过50个字符'}
                }
            },
            model: {
                validators: {
                    notEmpty: {message: '请输入物资型号'},
                    stringLength: { max: 50, message: '不能超过50个字符'}
                }
            },
            unit: {
                validators: {
                    notEmpty: {message: '请输入单位'},
                    stringLength: { max: 20, message: '不能超过20个字符'}
                }
            },
            warehouseId: {
                validators: {
                    notEmpty: {message: '请选择所属仓库'}
                }
            },
            count: {
                validators: {
                    notEmpty: {message: '请输入库存数量'},
                    stringLength: { max: 10, message: '不能超过10个字符'},
                    regexp:{
                        regexp: /^[1-9]\d*|0$/,  //正则规则用两个/包裹起来
                        message: '请输入正确的库存数量'
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
            product_e: {
                validators: {
                    notEmpty: {message: '请输入物资名称'},
                    stringLength: { max: 50, message: '不能超过50个字符'}
                }
            },
            model_e: {
                validators: {
                    notEmpty: {message: '请输入物资型号'},
                    stringLength: { max: 50, message: '不能超过50个字符'}
                }
            },
            unit_e: {
                validators: {
                    notEmpty: {message: '请输入单位'},
                    stringLength: { max: 20, message: '不能超过20个字符'}
                }
            },
            warehouseId_e: {
                validators: {
                    notEmpty: {message: '请选择所属仓库'}
                }
            }
        }
    })


    $('#edit-form-count').bootstrapValidator({
        message: '不能为空', //为每个字段指定通用错误提示语
        feedbackIcons: {   //输入框不同状态，显示图片的样式
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        live : 'enabled', //enabled代表当表单控件内容发生变化时就触发验证，默认提交时验证，
        fields: {
            count_e: {
                validators: {
                    notEmpty: {message: '请输入库存数量'},
                    stringLength: { max: 10, message: '不能超过10个字符'},
                    regexp:{
                        regexp: /^[1-9]\d*|0$/,  //正则规则用两个/包裹起来
                        message: '请输入正确的库存数量'
                    }
                }
            }
        }
    })

}
