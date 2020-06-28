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
        url: "../stock/list?key="+key+"&pageNum="+cnt+"&pageSize=10",
        dataType: "json",
        type: "GET",
        //data: {"name":name,"mobile":mobile,"pageNum":cnt,"pageSize":10},
        success: function (data) {
            var html= "";
            if(data.flag){
                $.each(data.data, function (i, item) {
                    html +="<tr>"+
                        "<td>"+(i+1)+"</td>"+
                        "<td>"+item.product+"</td>"+
                        "<td>"+item.model+"</td>"+
                        "<td>"+item.unit+"</td>"+
                        "<td>"+item.count+"</td>"+
                        "<td>"+item.warehouseName+"</td>"+
                        "<td>"+item.createTime+"</td>"+
                        "<td> <button class= \"btn btn-primary btn-xs\" onclick=\"edit('"+item.id+"');\">修改物资</button> " +
                             "<button class= \"btn btn-primary btn-xs\" onclick=\"editCount('"+item.id+"');\">修改库存</button> " +
                             "<button class= \"btn btn-primary btn-xs\" onclick=\"detail('"+item.id+"');\">明细</button>" +
                        " </td>"+
                        "</tr>";
                });
                if(html == ""){
                    html = "<tr><td colspan=\"8\">暂无数据</td></tr>";
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

function editSave(){
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

function editCountSave(){
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
var detailCnt = 1;
function detail(id){
    detailCnt = 1;
    $("#detail_id").val(id);
    $('#detail').modal('show');
    loadDetail(detailCnt);

}

function loadDetail(cnt){
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    var detailId = $("#detail_id").val();
    $.ajax({
        url: "../stock/detail?id="+detailId+"&pageNum="+cnt+"&pageSize=10"+"&startDate="+startDate+"&endDate="+endDate,
        dataType: "json",
        type: "GET",
        success: function (data) {
            var html= "";
            if(data.flag){
                $.each(data.data, function (i, item) {
                    html +="<tr>"+
                        "<td>"+(i+1)+"</td>"+
                        "<td>"+item.typeInfo+"</td>"+
                        "<td>"+item.createUser+"</td>"+
                        "<td>"+item.count+"</td>"+
                        "<td>"+item.project+"</td>"+
                        "<td>"+item.createTime+"</td>"+
                        "</tr>";
                });
                if(html == ""){
                    html = "<tr><td colspan=\"6\">暂无数据</td></tr>";
                }
                $("#tdetailbody").html(html);
                laypage({
                    cont: 'detailpage', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
                    pages: Math.ceil(data.totalCount / 10), //通过后台拿到的总页数
                    skin: "#49afcd",
                    curr: detailCnt || 1, //当前页
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