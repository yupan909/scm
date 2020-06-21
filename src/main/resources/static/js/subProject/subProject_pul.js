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
	Public.editDataForSub("../../getSubDetail.do");
	
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
			ktmc: {
				validators: {
					notEmpty: {message: '课题名称不能为空'}, //非空提示
					stringLength: { max: 100, message: '课题名称长度不能超过100'} ///长度提示
				}
			},
			ztc: {
				validators: {
					notEmpty: {message: '主题词不能为空'}, //非空提示
					stringLength: { max: 100, message: '主题词长度不能超过100'} ///长度提示
				}
			},
			fzrxm: {
				validators: {
					notEmpty: {message: '负责人姓名不能为空'}, //非空提示
					stringLength: { max: 10, message: '负责人姓名长度不能超过10'} ///长度提示
				}
			},
			xb: {
				validators: {
					stringLength: { max: 1, message: '性别长度不能超过1'} ///长度提示
				}
			},
			mz: {
				validators: {
					stringLength: { max: 15, message: '民族长度不能超过15'} ///长度提示
				}
			},
			csrq: {
				validators: {
					stringLength: { max: 15, message: '出生日期长度不能超过15'} ///长度提示
				}
			},
			xzzz: {
				validators: {
					stringLength: { max: 50, message: '行政职务长度不能超过50'} ///长度提示
				}
			},
			zyzw: {
				validators: {
					stringLength: { max: 50, message: '专业职务长度不能超过50'} ///长度提示
				}
			},
			zhxl: {
				validators: {
					stringLength: { max: 50, message: '最后学历长度不能超过50'} ///长度提示
				}
			},
			ssxk: {
				validators: {
					stringLength: { max: 50, message: '所属学科长度不能超过50'} ///长度提示
				}
			},
			szs: {
				validators: {
					stringLength: { max: 50, message: '所在省（自治区、直辖市）长度不能超过50'} ///长度提示
				}
			},
			gzdw: {
				validators: {
					stringLength: { max: 200, message: '工作单位长度不能超过200'} ///长度提示
				}
			},
			dzyx: {
				validators: {
					stringLength: { max: 50, message: '电子信箱长度不能超过50'} ///长度提示
				}
			},
			txdz: {
				validators: {
					stringLength: { max: 200, message: '通讯地址长度不能超过200'} ///长度提示
				}
			},
			yzbm: {
				validators: {
					stringLength: { max: 20, message: '邮政编码长度不能超过20'}, ///长度提示
					regexp: {
				        regexp: /^[1-9]\d{5}$/,
				        message: '邮编格式不正确'
				    }
				}
			},
			lxdh_qh: {
				validators: {
					stringLength: { max: 10, message: '联系电话（区号）长度不能超过10'}, ///长度提示
					regexp: {
						regexp: /^[0-9]+$/,
						message: '联系电话（区号）只能填写数字'
					}
				}
			},
			lxdh_dw: {
				validators: {
					stringLength: { max: 20, message: '联系电话（单位）长度不能超过20'}, ///长度提示
					regexp: {
						//regexp: /(^1\d{10}$)|(^\d{7,8}$)/,
						//message: '联系电话（单位）格式不正确'
						regexp: /^[0-9]+$/,
						message: '联系电话（单位）只能填写数字'
					}
				}
			},
			lxdh_jt: {
				validators: {
					stringLength: { max: 20, message: '联系电话（家庭）长度不能超过20'}, ///长度提示
					regexp: {
						//regexp: /(^1\d{10}$)|(^\d{7,8}$)/,
						//message: '联系电话（家庭）格式不正确'
						regexp: /^[0-9]+$/,
						message: '联系电话（家庭）只能填写数字'
					}
				}
			},
			lxdh_sj: {
				validators: {
					stringLength: { max: 20, message: '联系电话（手机）长度不能超过20'}, ///长度提示
					regexp: {
						//regexp: /(^1\d{10}$)|(^\d{7,8}$)/,
						//message: '联系电话（手机）格式不正确'
						regexp: /^[0-9]+$/,
						message: '联系电话（手机）只能填写数字'
					}
				}
			},
			jflyjse: {
				validators: {
					stringLength: { max: 200, message: '经费来源及数额长度不能超过200'} ///长度提示
				}
			},
			yjwcsj: {
				validators: {
					stringLength: { max: 15, message: '预计完成时间长度不能超过15'} ///长度提示
				}
			},
			ktlz: {
				validators: {
					stringLength: { max: 2000, message: '对研究课题的论证长度不能超过2000'} ///长度提示
				}
			},
			sshwctjdlz: {
				validators: {
					stringLength: { max: 2000, message: '对课题实施和完成条件的论证长度不能超过2000'} ///长度提示
				}
			}
		}
	});
	
	//出生日期
	$("#csrq").click(function(){
		WdatePicker({
			dateFmt:'yyyy-MM-dd',
			readOnly:true,
			onpicked:function(){  //重新验证
				$('#frm').data("bootstrapValidator").updateStatus('csrq', 'NOT_VALIDATED', null).validateField('csrq');
			},
			oncleared:function(){  //重新验证
				$('#frm').data("bootstrapValidator").updateStatus('csrq', 'NOT_VALIDATED', null).validateField('csrq');
			}
		});
	});
	
	//预计完成时间
	$("#yjwcsj").click(function(){
		WdatePicker({
			dateFmt:'yyyy-MM-dd',
			readOnly:true,
			onpicked:function(){  //重新验证
				$('#frm').data("bootstrapValidator").updateStatus('yjwcsj', 'NOT_VALIDATED', null).validateField('yjwcsj');
			},
			oncleared:function(){  //重新验证
				$('#frm').data("bootstrapValidator").updateStatus('yjwcsj', 'NOT_VALIDATED', null).validateField('yjwcsj');
			}
		});
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

/**
 * 验证表体
 */
function checkTbody(){
	var flag = true;
	if(flag){
		//姓名
		$("[name='partyXm']").each(function(){
			if($(this).val().length>20){
				var obj = this;
				Public.alert(3,"主要参加者中的姓名长度不能超过20",function(){
					$(obj).focus();
					layer.close(layer.index); 
				});  
				flag = false;
				return false;
			}
		});
	}
	if(flag){
		//性别
		$("[name='partyXb']").each(function(){
			if($(this).val().length>1){
				var obj = this;
				Public.alert(3,"主要参加者中的性别长度不能超过1",function(){
					$(obj).focus();
					layer.close(layer.index); 
				});  
				flag = false;
				return false;
			}
		});
	}
	if(flag){
		//出生年月
		$("[name='partyCsny']").each(function(){
			if($(this).val().length>15){
				var obj = this;
				Public.alert(3,"主要参加者中的出生年月长度不能超过15",function(){
					$(obj).focus();
					layer.close(layer.index); 
				});  
				flag = false;
				return false;
			}
		});
	}
	if(flag){
		//专业职务
		$("[name='partyZyzw']").each(function(){
			if($(this).val().length>100){
				var obj = this;
				Public.alert(3,"主要参加者中的专业职务长度不能超过100",function(){
					$(obj).focus();
					layer.close(layer.index); 
				});  
				flag = false;
				return false;
			}
		});
	}
	if(flag){
		//研究专长
		$("[name='partyYjzc']").each(function(){
			if($(this).val().length>200){
				var obj = this;
				Public.alert(3,"主要参加者中的研究专长长度不能超过200",function(){
					$(obj).focus();
					layer.close(layer.index); 
				});  
				flag = false;
				return false;
			}
		});
	}
	if(flag){
		//学历
		$("[name='partyXl']").each(function(){
			if($(this).val().length>50){
				var obj = this;
				Public.alert(3,"主要参加者中的学历长度不能超过50",function(){
					$(obj).focus();
					layer.close(layer.index); 
				});  
				flag = false;
				return false;
			}
		});
	}
	if(flag){
		//学位
		$("[name='partyXw']").each(function(){
			if($(this).val().length>50){
				var obj = this;
				Public.alert(3,"主要参加者中的学位长度不能超过50",function(){
					$(obj).focus();
					layer.close(layer.index); 
				});  
				flag = false;
				return false;
			}
		});
	}
	if(flag){
		//工  作  单  位
		$("[name='partyGzdw']").each(function(){
			if($(this).val().length>200){
				var obj = this;
				Public.alert(3,"主要参加者中的工作单位长度不能超过200",function(){
					$(obj).focus();
					layer.close(layer.index); 
				});  
				flag = false;
				return false;
			}
		});
	}
	
	if(flag){
		//研究阶段（起止时间）
		$("[name='achYjjd']").each(function(){
			if($(this).val().length>50){
				var tip = "主要阶段性成果中的研究阶段（起止时间）";
				if($(this).parent().parent().children("[name='achType']").val()==2){
					tip = "最终研究成果中的完成时间";
				}
				var obj = this;
				Public.alert(3, tip + "长度不能超过50",function(){
					$(obj).focus();
					layer.close(layer.index); 
				});  
				flag = false;
				return false;
			}
		});
	}
	
	if(flag){
		//阶段成果名称
		$("[name='achCgmc']").each(function(){
			if($(this).val().length>200){
				var tip = "主要阶段性成果中的阶段成果名称";
				if($(this).parent().parent().children("[name='achType']").val()==2){
					tip = "最终研究成果中的最终成果名称";
				}
				var obj = this;
				Public.alert(3,tip + "长度不能超过200",function(){
					$(obj).focus();
					layer.close(layer.index); 
				});  
				flag = false;
				return false;
			}
		});
	}
	
	if(flag){
		//成果形式
		$("[name='achCgxs']").each(function(){
			if($(this).val().length>200){
				var tip = "主要阶段性成果中的成果形式";
				if($(this).parent().parent().children("[name='achType']").val()==2){
					tip = "最终研究成果中的成果形式";
				}
				var obj = this;
				Public.alert(3,tip + "长度不能超过200",function(){
					$(obj).focus();
					layer.close(layer.index); 
				});  
				flag = false;
				return false;
			}
		});
	}
	
	if(flag){
		//承担人
		$("[name='achCdr']").each(function(){
			if($(this).val().length>200){
				var tip = "主要阶段性成果中的承担人";
				if($(this).parent().parent().children("[name='achType']").val()==2){
					tip = "最终研究成果中的承担人";
				}
				var obj = this;
				Public.alert(3,tip + "长度不能超过200",function(){
					$(obj).focus();
					layer.close(layer.index); 
				});  
				flag = false;
				return false;
			}
		});
	}
	return flag;
}


function saveOrSubmit(status){
	
	//触发全部验证
	$('#frm').data("bootstrapValidator").validate();
	//获取当前表单验证状态
	var flag = $('#frm').data("bootstrapValidator").isValid();
	
	if(flag && checkTbody()){
	
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
		        url:"../../saveSubProject.do",
		        data:$('#frm').serialize(),
		        async: true,
		        error: function(request) {
		        	if(status =="0"){
		        		Public.alert(2,"系统异常，保存失败！");
		        	}else{
		        		Public.alert(2,"系统异常，提交失败！");
		        	}
		        	
		        },
		        success: function(data) {
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
		        	
		        }
		    });
	
			layer.close(index);
		});

	}
	
}


function changeStatus(status){
	var msg = "";
	if(status =="3"){
		msg = "您确定要通过吗？";
	}else{
		msg = "您确定要不通过吗？";
	}
	layer.confirm(msg, {icon: 3, title:'提示'}, function(index){
		
		var id = $("#id").val();
		$.ajax({
			cache: true,
			type: "POST",
			url:"../../changeStatus.do",
			data:{"id":id,"status":status},
			async: true,
			error: function(request) {
				Public.alert(2,"系统异常，操作失败！");
			},
			success: function(data) {
				if(status == "3"){
					Public.alert(1,"操作成功！",function(){
						window.opener.load(1);
						window.close();
					});  
				}else if(status == "2"){
					Public.alert(1,"操作成功！",function(){
						window.opener.load(1);
						window.close();
					});   
				}
			}
		});
		
		layer.close(index);
	});
	
}

//下载
function download(){
	var id = $("#id").val();
	if(id==""){
		Public.alert(3,"请先保存!");   
	}else{
		window.location.href="../../downloadSubProject.do?id="+id;	
	}
}
