$(function(){
	
	//添加验证
	$('#registerInfo').bootstrapValidator({
		message: '不能为空', //为每个字段指定通用错误提示语
		feedbackIcons: {   //输入框不同状态，显示图片的样式
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: { /*键名username和input name值对应*/
			userName: {
				validators: {
					notEmpty: {message: '用户名不能为空'}, //非空提示
					stringLength: { min: 4, max: 20, message: '用户名长度必须在4到20之间'} ///长度提示
				}
			},
			password: {
				validators: {
					notEmpty: {message: '密码不能为空'}, //非空提示
					stringLength: { min: 6, max: 20, message: '密码长度必须在6到20之间'}, ///长度提示
					regexp: {
				        regexp: /^[a-zA-Z0-9_\.]+$/,
				        message: '密码只能包含字母、数字、下划线和小数点'
				    },
				    identical: {
				        field: 'password2',
				        message: '两次输入的密码不一样'
				    }
				}
			},
			password2: {
				validators: {
					notEmpty: {message: '确认密码不能为空'}, //非空提示
					stringLength: { min: 6, max: 20, message: '确认密码长度必须在6到20之间'}, ///长度提示
					identical: {
				        field: 'password',
				        message: '两次输入的密码不一样'
				    }
				}
			},
			question: {
				validators: {
					notEmpty: {message: '密码问题不能为空'}, //非空提示
					stringLength: { max: 200, message: '密码问题长度不能超过200'} ///长度提示
				}
			},
			answer: {
				validators: {
					notEmpty: {message: '问题答案不能为空'}, //非空提示
					stringLength: { max: 200, message: '问题答案长度不能超过200'} ///长度提示
				}
			},
			email: {
				validators: {
					notEmpty: {message: '邮箱地址不能为空'}, //非空提示
					stringLength: { max: 50, message: '邮箱地址长度不能超过50'}, ///长度提示
					emailAddress: {message: '邮箱地址格式不正确'}
				}
			},
			phoneNumber: {
				validators: {
					notEmpty: {message: '电话不能为空'}, //非空提示
					stringLength: { max: 13, message: '电话长度不能超过13'}, ///长度提示
					regexp: {
				        regexp: /(^1\d{10}$)|(^0\d{2,3}-?\d{7,8}$)/,
				        message: '电话格式不正确'
				    }
				}
			},
			qq: {
				validators: {
					notEmpty: {message: 'QQ号码不能为空'}, //非空提示
					stringLength: {max: 10, message: 'QQ号码长度不能超过10'}, ///长度提示 
					regexp: {
				        regexp: /^[1-9][0-9]{4,9}$/,
				        message: 'QQ号码格式不正确'
				    }
				}
			},
			realName: {
				validators: {
					notEmpty: {message: '真实姓名不能为空'}, //非空提示
					stringLength: { max: 10, message: '真实姓名长度不能超过10'} ///长度提示
				}
			},
			cid: {
				validators: {
					notEmpty: {message: '身份证号码不能为空'}, //非空提示 
					stringLength: { max: 18, message: '身份证号码长度不能超过18'}, ///长度提示
					regexp: {
				        regexp: /(^\d{15}$)|(^\d{17}([0-9]|X)$)/,
				        message: '身份证号码格式不正确'
				    }
				}
			},
			companyName: {
				validators: {
					notEmpty: {message: '单位名称不能为空'}, //非空提示
					stringLength: { max: 50, message: '单位名称长度不能超过50'} ///长度提示
				}
			},
			address: {
				validators: {
					notEmpty: {message: '联系地址不能为空'}, //非空提示
					stringLength: { max: 100, message: '联系地址长度不能超过100'} ///长度提示
				}
			},
			endTime: {
				validators: {
					notEmpty: {message: '理想中的结课时间不能为空'}, //非空提示
					stringLength: { max: 10, message: '理想中的结课时间长度不能超过10'} ///长度提示
				}
			}
		}
	});
	
	$("#endTime").click(function(){
		WdatePicker({
			dateFmt:'yyyy-MM',
			readOnly:true,
			onpicked:function(){  //重新验证
				$('#registerInfo').data("bootstrapValidator").updateStatus('endTime', 'NOT_VALIDATED', null).validateField('endTime');
			},
			oncleared:function(){  //重新验证
				$('#registerInfo').data("bootstrapValidator").updateStatus('endTime', 'NOT_VALIDATED', null).validateField('endTime');
			}
		});
	});
	
	//回显数据
	edit();
	
});

//回显个人信息
function edit(){
	$.ajax({
        cache: true,
        type: "POST",
        url:"../getUserInfoMyself.do",
        data:$('#registerInfo').serialize(),
        dataType:"json",
        async: false,
        error: function(request) {
            Public.alert(2,"回显个人信息失败！");
        },
        success: function(data) {
        	if(""==data || null==data || typeof(data)=="undefined"){
        		Public.alert(2,"回显个人信息失败！");
				return false;
			}
			for(var keys in data){
				var value = data[keys];
                var $key = $("#"+keys);
                if($key.is("select")){
					$key.find("option[value='"+value+"']").attr("selected",true);						
				}else if($key.attr("type")=="radio" || $key.attr("type")=="checkbox"){
					$("input[name="+keys+"][value="+value+"]").attr("checked",true);
				}else{
					if($key.is("span") || $key.is("font")){
						$key.html(value);
					}else{
						$key.val(value);
					}
				}
                if(keys=="password"){
                	$("#password2").val(value);
                }
			}
        }
    });
}


//保存
function doSave(){
	
	layer.confirm('您确定要保存修改吗?', {icon: 3, title:'提示'}, function(index){
		//触发全部验证
		$('#registerInfo').data("bootstrapValidator").validate();
		//获取当前表单验证状态
		var flag = $('#registerInfo').data("bootstrapValidator").isValid();
		
		if(flag){
			$.ajax({
		        cache: true,
		        type: "POST",
		        url:"../updateUserInfoMyself.do",
		        data:$('#registerInfo').serialize(),
		        contentType: "application/x-www-form-urlencoded; charset=utf-8", 
		        async: false,
		        error: function(request) {
		            Public.alert(2,"保存失败！");
		        },
		        success: function(data) {
		        	if(data.status =="ok"){
		        		Public.alert(1,"保存成功！");
		        	}else{
		        		Public.alert(2,"保存失败！");
		        	}
		        }
		    });
		}
	
		layer.close(index);
	});
	
}