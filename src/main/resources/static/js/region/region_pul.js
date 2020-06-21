$.ajaxSetup({     
    contentType:"application/x-www-form-urlencoded;charset=utf-8",     
    complete:function(XMLHttpRequest,textStatus){   
     //通过XMLHttpRequest取得响应头，sessionstatus，    
      var sessionstatus=XMLHttpRequest.getResponseHeader("sessionstatus");   
             if(sessionstatus=="timeout"){   
            	 Public.alert(3,"用户信息过期,请重新登录！",function(){
					window.location.href="../../login.html";
				 });  
             }     
       }     
});

$(function(){
	//回显数据
	Public.editData("../../selectRegion.do");
	
	//添加验证
	$('#frm').bootstrapValidator({
		message: '不能为空', //为每个字段指定通用错误提示语
		fields: { /*键名username和input name值对应*/
			year:{
				validators: {
					notEmpty: {message: '年份不能为空'}, //非空提示
					stringLength: { max: 4, message: '年份长度不能超过4'} ///长度提示
				}	
			},
			xxx: {
				validators: {
					notEmpty: {message: '管辖的小学校不能为空'}, //非空提示
					stringLength: { max: 10, message: '管辖的小学校长度不能超过10'}, ///长度提示
					regexp: {
				        regexp: /^[0-9]+$/,
				        message: '管辖的小学校只能填写数字'
				    }
				}
			},
			sjsfx: {
				validators: {
					notEmpty: {message: '市级示范校不能为空'}, //非空提示
					stringLength: { max: 10, message: '市级示范校长度不能超过10'}, ///长度提示
					regexp: {
						regexp: /^[0-9]+$/,
						message: '市级示范校只能填写数字'
					}
				}
			},
			qjsfx: {
				validators: {
					notEmpty: {message: '区级示范校不能为空'}, //非空提示
					stringLength: { max: 10, message: '区级示范校长度不能超过10'}, ///长度提示
					regexp: {
						regexp: /^[0-9]+$/,
						message: '区级示范校只能填写数字'
					}
				}
			},
			mbxx: {
				validators: {
					notEmpty: {message: '民办学校不能为空'}, //非空提示
					stringLength: { max: 10, message: '民办学校长度不能超过10'}, ///长度提示
					regexp: {
						regexp: /^[0-9]+$/,
						message: '民办学校只能填写数字'
					}
				}
			},
			sqdw: {
				validators: {
					notEmpty: {message: '申请单位不能为空'}, //非空提示
					stringLength: { max: 100, message: '申请单位长度不能超过100'} ///长度提示
				}
			},
			lxr: {
				validators: {
					notEmpty: {message: '联系人不能为空'}, //非空提示
					stringLength: { max: 25, message: '联系人长度不能超过25'} ///长度提示
				}
			},
			syqfzr: {
				validators: {
					stringLength: { max: 25, message: '实验区负责人长度不能超过25'} ///长度提示
				}
			},
			zw: {
				validators: {
					stringLength: { max: 25, message: '职务长度不能超过25'} ///长度提示
				}
			},
			xl: {
				validators: {
					stringLength: { max: 25, message: '学历长度不能超过25'} ///长度提示
				}
			},
			cjsj: {
				validators: {
					stringLength: { max: 25, message: '从教时间长度不能超过25'} ///长度提示
				}
			},
			dwdz: {
				validators: {
					stringLength: { max: 100, message: '单位地址长度不能超过100'} ///长度提示
				}
			},
			yb: {
				validators: {
					stringLength: { max: 25, message: '邮编长度不能超过25'}, ///长度提示 
					regexp: {
				        regexp: /^[1-9]\d{5}$/,
				        message: '邮编格式不正确'
				    }
				}
			},
			dh: {
				validators: {
					stringLength: { max: 20, message: '电话长度不能超过20'}, ///长度提示
					regexp: {
						regexp: /(^1\d{10}$)|(^0\d{2,3}-?\d{7,8}$)/,
				        message: '电话格式不正确'
				    }
				}
			},
			sj: {
				validators: {
					stringLength: { max: 20, message: '手机长度不能超过20'}, ///长度提示
					regexp: {
				        regexp: /(^1\d{10}$)|(^0\d{2,3}-?\d{7,8}$)/,
				        message: '手机格式不正确'
				    }
				}
			},
			yx: {
				validators: {
					stringLength: { max: 50, message: '邮箱长度不能超过50'}, ///长度提示
					emailAddress: { message: '邮箱格式不正确'}
				}
			},
			cdzktqk: {
				validators: {
					stringLength: { max: 2000, message: '本区学校承担子课题情况长度不能超过2000'} ///长度提示
				}
			}
		}
	});
	
	//年份
	$("#year").click(function(){
		WdatePicker({
			dateFmt:'yyyy',
			readOnly:true,
			onpicked:function(){  //重新验证
				$('#frm').data("bootstrapValidator").updateStatus('year', 'NOT_VALIDATED', null).validateField('year');
			},
			oncleared:function(){  //重新验证
				$('#frm').data("bootstrapValidator").updateStatus('year', 'NOT_VALIDATED', null).validateField('year');
			}
		});
	});
	
	//年份默认显示当前年份
	if($("#year").val()==""){
		$("#year").val( new Date().getFullYear() );
	}
	
});

//保存或提交
function saveOrSubmit(status){
	
	//触发全部验证
	$('#frm').data("bootstrapValidator").validate();
	//获取当前表单验证状态
	var flag = $('#frm').data("bootstrapValidator").isValid();
	
	if(flag){
	
		var msg = "";
		if(status =="0"){
			msg = "您确定要保存吗？";
		}else{
			msg = "您确定要提交吗？";
		}
		layer.confirm(msg, {icon: 3, title:'提示'}, function(index){
			$("#status").val(status);
			$.ajax({
		        cache: true,
		        type: "POST",
		        url:"../../saveRegion.do",
		        data:$('#frm').serialize(),
		        async: true,
		        error: function(request) {
		        	Public.alert(2,"系统异常，操作失败！");
		        },
		        success: function(data) {
		        	if(""!=data && null!=data && typeof(data)!="undefined"){
						if("1"==data.msg){
							if(status == "0"){
								$("#id").val(data.id); //回显id
								Public.alert(1,"保存成功！",function(){
									window.opener.load(1);
									layer.close(layer.index); 
								});
							}else{
								Public.alert(1,"提交成功！",function(){
				        			window.opener.load(1);
				        			window.close();
				        		});  
							}
						}else{
							Public.alert(2,"操作失败！");
						}
					}else{
						Public.alert(2,"系统异常，操作失败！");
					}
		        }
		    });
	
			layer.close(index);
		});
		
	}	
}

//下载
function download(){
	var id = $("#id").val();
	if(id==""){
		Public.alert(3,"请先保存!");   
	}else{
		window.location.href="../../downloadRegion.do?id="+id;	
	}
}

//通过或者驳回
function pass(type){
	var tip = "通过";
	if(type=='n'){
		tip = "不通过";
	}
	layer.confirm('您确定要'+tip+'吗?', {icon: 3, title:'提示'}, function(index){
		var status = "3"; //通过
		if(type=='n'){
			status = "2"; //驳回
		}
		var id = $("#id").val();
		$.ajax({
			cache: true,
			type:"post",
			url:"../../updateRegionStatus.do",
			data:{id:id,status:status},
			dataType:"json",
			async:false,
			success:function(data){
				if(""!=data && null!=data && typeof(data)!="undefined"){
					if(type=='n'){ //驳回
						if(data.msg=="1"){
							Public.alert(1,"操作成功！",function(){
								window.opener.load(1);
								window.close();
							});   
						}else{
							Public.alert(2,"操作失败！"); 
						}
						
					}else{  //通过
						if(data.msg=="1"){
							Public.alert(1,"操作成功！",function(){
								window.opener.load(1);
								window.close();
							});   
						}else{
							Public.alert(2,"操作失败！"); 
						}
					}
				}else{
					Public.alert(2,"请求出现异常！"); 
				}
			},
			error:function(data){
				Public.alert(2,"系统异常，操作失败！");   
			}
		});
		
		layer.close(index);
	});
}
