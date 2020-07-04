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
// 出入库区分
var stockTypeMap = {0:"入库", 1:"出库"};
var warehouseMap = {};
$(function(){

    // 类型下拉回显
    var typeHtml = '<option value="">--请选择--</option>';
    for(var key in stockTypeMap) {
        typeHtml += '<option value="' + key + '">' + stockTypeMap[key] + '</option>';
    }
    $("#type").html(typeHtml);

    // 加载仓库下拉列表
    warehouseMap = Public.initWarehouse("warehouseId");

	load(curr);

    //开始时间
    layui.laydate.render({
        elem: '#startDate'
    });
    //结束时间
    layui.laydate.render({
        elem: '#endDate'
    });
});


function load(pageNum){
	var project = $("#project").val();
	var product = $("#product").val();
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var type = $("#type").val();
	var warehouseId = $("#warehouseId").val();
	$.ajax({
        url: "../inoutStock/list",
        type: "POST",
		contentType:"application/json;charset=utf-8",
        dataType: "json",
        async: false,
        data: JSON.stringify({ "pageNum":pageNum,
        		"pageSize":pageSize,
        		"type":type,
        		"warehouseId":warehouseId,
        		"project":project,
        		"product":product,
        		"startTime":startDate,
        		"endTime":endDate
        		}),
        success: function (data) {
            var html= "";
            if(data.flag == true){
                 $.each(data.data, function (i, item) {
                 html +="<tr>"+
                            "<td>"+(i+1)+"</td>"+
                            "<td>"+Public.ifNull(item.typeText)+"</td>"+
                            "<td>"+Public.ifNull(item.warehouseName)+"</td>"+
                            "<td>"+Public.ifNull(item.project)+"</td>"+
                            "<td>"+Public.ifNull(item.product)+"</td>"+
                            "<td>"+Public.ifNull(item.model)+"</td>"+
                            "<td>"+Public.ifNull(item.unit)+"</td>"+
                            "<td>"+Public.ifNull(item.count)+"</td>"+
                            "<td>"+Public.ifNull(item.price)+"</td>"+
                            "<td>"+Public.ifNull(item.source)+"</td>"+
                            "<td>"+Public.ifNull(item.handle)+"</td>"+
                            "<td>"+Public.ifNull(item.remark)+"</td>"+
                            "<td>"+Public.ifNull(item.createTime)+"</td>"+
                            "</tr>";
                });
                if(html == ""){
                    html = "<tr><td colspan=\"13\">暂无数据</td></tr>";
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
            }else{
                Public.alert(2, data.message);
            }
        }
    });
}

/**
 * 下载模版
 */
function exportExcel(){
    var type = $("#type").val();
    var warehouseId = $("#warehouseId").val();
    var project = encodeURI($("#project").val());
    var product = encodeURI($("#product").val());
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    window.location.href="../inoutStock/exportInoutStock?type="+type+"&warehouseId="+warehouseId+"&project="+project+"&product="+product+"&startTime="+startDate+"&endTime="+endDate;
}
