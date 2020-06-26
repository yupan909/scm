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
                             "<button class= \"btn btn-primary btn-xs btn-danger\" onclick=\"deleteById('"+item.id+"');\">删除</button> </td>"+
                                    "</tr>";
                    });
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
    var name = $("#name").val();
    var mobile = $("#mobile").val();
    var password = $("#password").val();
    var admin = $("#admin").val();
    var warehouseId = $("#warehouseId").val();
    var data = '{"name":"'+name+'","mobile":"'+mobile+'","password":"'+password+'","admin":"'+admin+'","warehouseId":"'+warehouseId+'"}';
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


