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
var stockType = 0;
$(function(){
	load(curr);
    // 表单校验
    validate();

    //开始时间
    layui.laydate.render({
        elem: '#startDateQuery'
    });
    //结束时间
    layui.laydate.render({
        elem: '#endDateQuery'
    });
});


function load(pageNum){
	var project = $("#projectQuery").val();
	var product = $("#productQuery").val();
	var model = $("#modelQuery").val();
	var source = $("#sourceQuery").val();
	var startDate = $("#startDateQuery").val();
	var endDate = $("#endDateQuery").val();
	$.ajax({
        url: "../inoutStock/list",
        type: "POST",
		contentType:"application/json;charset=utf-8",
        dataType: "json",
        data: JSON.stringify({ "pageNum":pageNum,
        		"pageSize":pageSize,
        		"type":stockType,
        		"project":project,
        		"product":product,
        		"model":model,
        		"source":source,
        		"startTime":startDate,
        		"endTime":endDate
        		}),
        async: false,
        success: function (data) {
            var html= "";
            if(data.flag == true){
                 $.each(data.data, function (i, item) {
                 html +="<tr>"+
                            "<td>"+(i+1)+"</td>"+
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
                    html = "<tr><td colspan=\"11\">暂无数据</td></tr>";
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
function exportTemplate(){
    window.location.href="../inoutStock/exportTemplate/" + stockType;
}

/**
 * 打开导入页面
  */
function openImportFile(){
    // 打开模态窗口
    Public.openModal("importModal");
}

/**
 * 导入
 */
function importFile() {

    var file = $("#file").val();
    if (!file) {
        Public.alert(2,"请选择导入文件！");
        return;
    }

    var formData = new FormData($('#uploadForm')[0]);
    $.ajax({
        type: "POST",
        url:"../inoutStock/import/" + stockType,
        data: formData,
        processData: false,
        contentType: false,
        async: false,
        error: function(request) {
            Public.alert(2,"上传出现异常！");
        },
        success: function(data) {
            Public.closeModal("importModal");
            if(data.flag == true){
                Public.alert(1,"导入成功！");
                load(1);
            }else{
                Public.alert(2, data.message);
            }
        }
    });
}

/**
 * 导出
 */
function exportExcel(){
    var project = encodeURI($("#projectQuery").val());
    var product = encodeURI($("#productQuery").val());
    var model = encodeURI($("#modelQuery").val());
    var source = encodeURI($("#sourceQuery").val());
    var startDate = $("#startDateQuery").val();
    var endDate = $("#endDateQuery").val();
    window.location.href="../inoutStock/exportInoutStock?type="+stockType+"&project="+project+"&product="+product+"&model="+model+"&source="+source+"&startTime="+startDate+"&endTime="+endDate;
}

/**
 * 打开新增窗口
  */
function openSave(){
    // 打开模态窗口
    Public.openModal("saveModal");
    // 重置校验
    Public.resetValidate("save-form");
    // 添加校验
    validate();
}

/**
 * 保存
 */
function save(){
    var validate = Public.doValidate("save-form");
    if(!validate){
        return;
    }
    var project = $("#project").val();
    var product = $("#product").val();
    var model = $("#model").val();
    var unit = $("#unit").val();
    var count = $("#count").val();
    var price = $("#price").val();
    var source = $("#source").val();
    var handle = $("#handle").val();
    var remark = $("#remark").val();
    $.ajax({
        url: "../inoutStock/save",
        type: "POST",
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            "type":stockType,
            "project":project,
            "product":product,
            "model":model,
            "unit":unit,
            "count":count,
            "price":price,
            "source":source,
            "handle":handle,
            "remark":remark
        }),
        async: false,
        success: function (data) {
            if(data.flag){
                Public.closeModal("saveModal");
                Public.alert(1,"新增成功");
                load(1);
            }else{
                Public.alert(2, data.message);
            }
        }
    });
}

/**
 * 表单验证
 */
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
            project: {
                validators: {
                    notEmpty: {message: '请输入工程名称'},
                    stringLength: { max: 50, message: '不能超过50个字符'}
                }
            },
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
            count: {
                validators: {
                    notEmpty: {message: '请输入数量'},
                    stringLength: { max: 10, message: '不能超过10个字符'},
                    regexp:{
                        regexp: /((^[1-9]\d*)|^0)$/,  //正则规则用两个/包裹起来
                        message: '请输入正确的数量'
                    }
                }
            },
            price: {
                validators: {
                    notEmpty: {message: '请输入物资单价'},
                    stringLength: { max: 10, message: '不能超过11个字符'},
                    regexp:{
                        regexp: /((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/,  //正则规则用两个/包裹起来
                        message: '请输入正确的物资单价'
                    }
                }
            },
            source: {
                validators: {
                    notEmpty: {message: '请输入物资来源'},
                    stringLength: { max: 50, message: '不能超过50个字符'}
                }
            },
            handle: {
                validators: {
                    notEmpty: {message: '请输入经手人'},
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

}
