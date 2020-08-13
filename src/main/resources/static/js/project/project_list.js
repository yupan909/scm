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
    // 加载角色权限
    Public.initRoleAuth(user.role);

    load(curr);
    // 表单校验
    validate();

    // 流水账表单校验
    validateDetail();

    //流水账日期
    layui.laydate.render({
        elem: '#recordDate'
    });
    layui.laydate.render({
        elem: '#recordDate_e'
    });

    //上传附件绑定
    initUpload();
});

// 列表
function load(pageNum){
    var name = $("#nameQuery").val();
    $.ajax({
        url: "../project/list",
        dataType: "json",
        type: "POST",
        data: JSON.stringify({ "pageNum":pageNum,
            "pageSize":pageSize,
            "name":name
        }),
        success: function (data) {
            var html= "";
            if(data.flag){
                $.each(data.data, function (i, item) {
                    var handel = "";
                    var money = "";
                    // 管理员
                    if(user.role == "1"){
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
                            "<button class= \"btn btn-success btn-xs\" onclick=\"detail('"+item.id+"');\">流水账</button></td>";

                        // 金额
                        money = "<td>"+Public.ifNull(item.contractMoney)+"</td>"+
                            "<td>"+Public.ifNull(item.finalMoney)+"</td>"+
                            "<td>"+Public.ifNull(item.inMoney)+"</td>"+
                            "<td>"+Public.ifNull(item.outMoney)+"</td>"+
                            "<td>"+Public.ifNull(item.sumMoney)+"</td>";
                    }
                    html +="<tr>"+
                        "<td>"+(i+1)+"</td>"+
                        "<td>"+Public.ifNull(item.name)+"</td>"+
                        "<td>"+Public.ifNull(item.content)+"</td>"+
                        money+
                        "<td>"+Public.ifNull(item.stateInfo)+"</td>"+
                        handel+
                        "</tr>";
                });
                if(html == ""){
                    if(user.role == "1"){
                        html = "<tr><td colspan=\"12\">暂无数据</td></tr>";
                    } else {
                        html = "<tr><td colspan=\"7\">暂无数据</td></tr>";
                    }
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

// 打开新增工程页面
function openSave(){
    // 打开模态窗口
    Public.openModal("saveModal");
    // 重置校验
    Public.resetValidate("save-form");
    // 添加校验
    validate();
}

// 新增工程
function save(){
    var validate = Public.doValidate("save-form");
    if(!validate){
        return;
    }
    var name = $("#name").val();
    var content = $("#content").val();
    var contractMoney = $("#contractMoney").val();
    var finalMoney = $("#finalMoney").val();
    var data = '{"name":"'+name+'","content":"'+content+'","contractMoney":"'+contractMoney+'","finalMoney":"'+finalMoney+'"}';
    $.ajax({
        url: "../project/save",
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

/**
 * 删除工程
 * @param id
 */
function deleteById(id){

    Public.confirm('您确定要删除吗?', function(){
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
    });
}

/**
 * 打开编辑工程页面
 * @param id
 */
function edit(id){
    // 打开模态窗口
    Public.openModal("editModal");
    // 重置校验
    Public.resetValidate("edit-form");
    // 添加校验
    validate();
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
                $("#id_e").val(data.data.id);
                $("#name_e").val(data.data.name);
                $("#content_e").val(data.data.content);
                $("#contractMoney_e").val(data.data.contractMoney);
                $("#finalMoney_e").val(data.data.finalMoney);
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
    var contractMoney = $("#contractMoney_e").val();
    var finalMoney = $("#finalMoney_e").val();
    var data = '{"name":"'+name+'","content":"'+content+'","id":"'+id+'","contractMoney":"'+contractMoney+'","finalMoney":"'+finalMoney+'"}';
    $.ajax({
        url: "../project/modify",
        dataType: "json",
        type: "POST",
        data: data,
        async: false,
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
                Public.alert(1, "修改成功！");
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
            },
            contractMoney: {
                validators: {
                    stringLength: { max: 13, message: '不能超过13个字符'},
                    regexp:{
                        regexp: /((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/,  //正则规则用两个/包裹起来
                        message: '请输入正确的合同金额'
                    }
                }
            },
            finalMoney: {
                validators: {
                    stringLength: { max: 13, message: '不能超过13个字符'},
                    regexp:{
                        regexp: /((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/,  //正则规则用两个/包裹起来
                        message: '请输入正确的结算金额'
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
            },
            contractMoney_e: {
                validators: {
                    stringLength: { max: 13, message: '不能超过13个字符'},
                    regexp:{
                        regexp: /((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/,  //正则规则用两个/包裹起来
                        message: '请输入正确的合同金额'
                    }
                }
            },
            finalMoney_e: {
                validators: {
                    stringLength: { max: 13, message: '不能超过13个字符'},
                    regexp:{
                        regexp: /((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/,  //正则规则用两个/包裹起来
                        message: '请输入正确的结算金额'
                    }
                }
            }
        }
    })
}

/**
 * 工程明细
 */
var detailCnt = 1;
var detailPageSize = 10;

// 打开工程明细页面
function detail(id){
    // 打开模态窗口
    Public.openModal("detail");

    $("#projectId").val(id);
    loadDetail(detailCnt);
}

function loadDetail(pageNum){
    var projectId = $("#projectId").val();
    var type = $("#typeQuery").val();
    var digest = $("#digestQuery").val();
    $.ajax({
        url: "../project/detail",
        dataType: "json",
        type: "POST",
        data: JSON.stringify({ "pageNum":pageNum,
            "pageSize":detailPageSize,
            "projectId":projectId,
            "type":type,
            "digest":digest
        }),
        success: function (data) {
            var html= "";
            if(data.flag){
                $.each(data.data, function (i, item) {
                    html +="<tr>"+
                        "<td>"+(i+1)+"</td>"+
                        "<td>"+Public.ifNull(item.typeInfo)+"</td>"+
                        "<td>"+Public.ifNull(item.recordDate)+"</td>"+
                        "<td>"+Public.ifNull(item.digest)+"</td>"+
                        "<td>"+Public.ifNull(item.money)+"</td>"+
                        "<td>"+Public.ifNull(item.handle)+"</td>"+
                        "<td>"+Public.ifNull(item.remark)+"</td>"+
                        "<td>"+Public.ifNull(item.createTime)+"</td>"+
                        "<td>" +
                        "<button class= \"btn btn-primary btn-xs\" onclick=\"openEditDetail('"+item.id+"');\">修改</button> " +
                        "<button class= \"btn btn-danger btn-xs\" onclick=\"deleteDetail('"+item.id+"');\">删除</button> "+
                        "<button class= \"btn btn-info btn-xs\" onclick=\"openDetailFile('"+item.id+"');\">附件</button> " +
                        "</td>"+
                        "</tr>";
                });
                if(html == ""){
                    html = "<tr><td colspan=\"9\">暂无数据</td></tr>";
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
                            loadDetail(detailCnt);
                        }
                    }
                });
            }
        }
    });
}

/**
 * 导出流水账
 */
function exportDetailExcel(){
    var projectId = $("#projectId").val();
    var type = $("#typeQuery").val();
    var digest = encodeURI($("#digestQuery").val());
    window.location.href="../project/exportProjectDetail?projectId="+projectId+"&type="+type+"&digest="+digest;
}

// 打开新增流水账页面
function openDetailSave(){
    // 打开模态窗口
    Public.openModal("saveDetailModal");
    // 重置校验
    Public.resetValidate("saveDetail-form");
    // 添加校验
    validateDetail();
}

// 新增流水账
function saveDetail(){
    var validate = Public.doValidate("saveDetail-form");
    if(!validate){
        return;
    }
    var projectId = $("#projectId").val();
    var type = $("#type").val();
    var recordDate = $("#recordDate").val();
    var digest = $("#digest").val();
    var money = $("#money").val();
    var handle = $("#handle").val();
    var remark = $("#remark").val();
    $.ajax({
        url: "../project/addDetail",
        dataType: "json",
        type: "POST",
        data: JSON.stringify({ "projectId":projectId,
            "type":type,
            "recordDate":recordDate,
            "digest":digest,
            "money":money,
            "handle":handle,
            "remark":remark
        }),
        async: false,
        success: function (data) {
            if(data.flag){
                Public.closeModal("saveDetailModal");
                Public.alert(1,"新增成功");
                loadDetail(1);
                // 刷新工程列表
                load(curr);
            }else{
                Public.alert(2,data.message);
            }
        }
    });
}

// 打开修改流水账页面
function openEditDetail(id){
    // 打开模态窗口
    Public.openModal("editDetailModal");
    // 重置校验
    Public.resetValidate("editDetail-form");
    // 添加校验
    validateDetail();
    loadDetailOne(id);
}

function loadDetailOne(id){
    $.ajax({
        type: "GET",
        url:"../project/getDetail/"+id ,
        async: false,
        error: function(request) {
            Public.alert(2,"请求失败！");
        },
        success: function(data) {
            if(data.flag){
                $("#recordId_e").val(data.data.id);
                $("#type_e").val(data.data.type);
                $("#recordDate_e").val(data.data.recordDate);
                $("#digest_e").val(data.data.digest);
                $("#money_e").val(data.data.money);
                $("#handle_e").val(data.data.handle);
                $("#remark_e").val(data.data.remark);
            }else{
                Public.alert(2,data.message);
            }
        }
    });
}

// 修改流水账
function editDetail(){
    var validate = Public.doValidate("editDetail-form");
    if(!validate){
        return;
    }
    var id = $("#recordId_e").val();
    var type = $("#type_e").val();
    var recordDate = $("#recordDate_e").val();
    var digest = $("#digest_e").val();
    var money = $("#money_e").val();
    var handle = $("#handle_e").val();
    var remark = $("#remark_e").val();
    $.ajax({
        url: "../project/modifyDetail",
        dataType: "json",
        type: "POST",
        data: JSON.stringify({ "id":id,
            "type":type,
            "recordDate":recordDate,
            "digest":digest,
            "money":money,
            "handle":handle,
            "remark":remark
        }),
        async: false,
        success: function (data) {
            if(data.flag){
                Public.closeModal("editDetailModal");
                Public.alert(1,"修改成功！");
                loadDetail(detailCnt);
                // 刷新工程列表
                load(curr);
            }else{
                Public.alert(2,data.message);
            }
        }
    });
}

// 删除工程明细
function deleteDetail(id) {
    Public.confirm('您确定要删除吗?', function(){
        $.ajax({
            type: "GET",
            url:"../project/deleteDetail/"+id ,
            async: false,
            error: function(request) {
                Public.alert(2,"请求失败！");
            },
            success: function(data) {
                if(data.flag){
                    Public.alert(1,"删除成功！");
                    loadDetail(detailCnt);
                    // 刷新工程列表
                    load(curr);
                }else{
                    Public.alert(2,"删除失败！");
                }
            }
        });
    });
}

// 工程流水账校验
function validateDetail(){
    $('#saveDetail-form').bootstrapValidator({
        message: '不能为空', //为每个字段指定通用错误提示语
        feedbackIcons: {   //输入框不同状态，显示图片的样式
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        live : 'enabled', //enabled代表当表单控件内容发生变化时就触发验证，默认提交时验证，
        fields: {
            type: {
                validators: {
                    notEmpty: {message: '请选择类型'}
                }
            },
            recordDate: {
                validators: {
                    notEmpty: {message: '请输入日期'},
                    stringLength: { max: 50, message: '不能超过50个字符'}
                }
            },
            digest: {
                validators: {
                    stringLength: { max: 500, message: '不能超过500个字符'}
                }
            },
            money: {
                validators: {
                    notEmpty: {message: '请输入金额'},
                    stringLength: { max: 11, message: '不能超过11个字符'},
                    regexp:{
                        regexp: /((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/,  //正则规则用两个/包裹起来
                        message: '请输入正确的金额'
                    }
                }
            },
            handle: {
                validators: {
                    stringLength: { max: 50, message: '不能超过50个字符'}
                }
            },
            remark: {
                validators: {
                    stringLength: { max: 500, message: '不能超过500个字符'}
                }
            }
        }
    });

    $('#editDetail-form').bootstrapValidator({
        message: '不能为空', //为每个字段指定通用错误提示语
        feedbackIcons: {   //输入框不同状态，显示图片的样式
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        live : 'enabled', //enabled代表当表单控件内容发生变化时就触发验证，默认提交时验证，
        fields: {
            type_e: {
                validators: {
                    notEmpty: {message: '请选择类型'}
                }
            },
            recordDate_e: {
                validators: {
                    notEmpty: {message: '请输入日期'},
                    stringLength: { max: 50, message: '不能超过50个字符'}
                }
            },
            digest_e: {
                validators: {
                    stringLength: { max: 500, message: '不能超过500个字符'}
                }
            },
            money_e: {
                validators: {
                    notEmpty: {message: '请输入金额'},
                    stringLength: { max: 11, message: '不能超过11个字符'},
                    regexp:{
                        regexp: /((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/,  //正则规则用两个/包裹起来
                        message: '请输入正确的金额'
                    }
                }
            },
            handle_e: {
                validators: {
                    stringLength: { max: 50, message: '不能超过50个字符'}
                }
            },
            remark_e: {
                validators: {
                    stringLength: { max: 500, message: '不能超过500个字符'}
                }
            }
        }
    });
}

/**
 * 工程明细附件
 */
var detailFileCnt = 1;
var detailFilePageSize = 10;

// 打开附件页面
function openDetailFile(id){
    // 打开模态窗口
    Public.openModal("detailFileModal");
    // 工程明细id
    $("#detailId").val(id);
    // 加载工程明细附件列表
    loadDetailFile(detailFileCnt);
}

// 加载工程明细附件列表
function loadDetailFile(pageNum){
    var detailId = $("#detailId").val();
    $.ajax({
        url: "../project/listFile",
        dataType: "json",
        type: "POST",
        data: JSON.stringify({ "pageNum":pageNum,
            "pageSize":detailFilePageSize,
            "businessId":detailId
        }),
        success: function (data) {
            var html= "";
            if(data.flag){
                $.each(data.data, function (i, item) {
                    html +="<tr>"+
                        "<td>"+(i+1)+"</td>"+
                        "<td>"+Public.ifNull(item.name)+"</td>"+
                        "<td>"+Public.ifNull(item.createTime)+"</td>"+
                        "<td>" +
                        "<button class= \"btn btn-info btn-xs\" onclick=\"downloadDetailFile('"+item.id+"');\">下载</button> "+
                        "<button class= \"btn btn-danger btn-xs\" onclick=\"deleteDetailFile('"+item.id+"');\">删除</button> "+
                        "</td>"+
                        "</tr>";
                });
                if(html == ""){
                    html = "<tr><td colspan=\"4\">暂无数据</td></tr>";
                }
                $("#detailFile_tbody").html(html);
                // 分页
                layui.laypage.render({
                    elem: 'detailFile_page', // 指向存放分页的容器，值可以是容器ID、DOM对象。如：1. elem: 'id' 注意：这里不能加 # 号 2. elem: document.getElementById('id')
                    theme: '#009688', // 自定义主题
                    count: data.totalCount,   // 数据总数
                    limit: detailPageSize,           // 每页显示的条数
                    curr: pageNum || 1,           // 当前页
                    layout: ['count', 'prev', 'page', 'next', 'skip'], //自定义排版。可选值有：count（总条目输区域）、prev（上一页区域）、page（分页区域）、next（下一页区域）、limit（条目选项区域）、refresh（页面刷新区域。注意：layui 2.3.0 新增） 、skip（快捷跳页区域）
                    jump: function (obj, first) { //触发分页后的回调
                        if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                            detailFileCnt = obj.curr;
                            loadDetailFile(detailFileCnt);
                        }
                    }
                });
            }
        }
    });
}

// 上传附件绑定
function initUpload(){
    //上传附件绑定
    layui.upload.render({
        elem: '#uploadBtn',
        url: '../project/upload',       // 请求url
        accept: 'file',      	   // 指定允许上传时校验的文件类型，可选值有：images（图片）、file（所有文件）、video（视频）、audio（音频）
        size: 51200,  		       // 设置文件最大可允许上传的大小，单位 KB， 不支持ie8/9
        data: {
            businessId: function(){
                return $("#detailId").val()
            }
        },
        done: function(res, index, upload){ // 请求成功后的回调
            if(res.flag){
                // 附件列表刷新
                loadDetailFile(1);
                Public.alert(1,"上传成功");
            }else{
                Public.alert(2,data.message);
            }
        },
        error: function(index, upload){
            Public.alert(2, "上传异常");
        }
    });
}

// 工程明细附件下载
function downloadDetailFile(id) {
    window.location.href="../project/download/" + id;
}

// 删除工程明细附件
function deleteDetailFile(id) {
    Public.confirm('您确定要删除附件吗?', function(){
        $.ajax({
            type: "GET",
            url:"../project/deleteFile/"+id ,
            async: false,
            error: function(request) {
                Public.alert(2,"请求失败！");
            },
            success: function(data) {
                if(data.flag){
                    Public.alert(1,"删除成功！");
                    loadDetailFile(detailFileCnt);
                }else{
                    Public.alert(2,"删除失败！");
                }
            }
        });
    });
}
