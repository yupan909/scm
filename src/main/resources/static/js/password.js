$(function(){
	$("#step").val("1"); //初始化置为第一步
	$("#userName").val(""); //用户名置为空
	$("#answer").val(""); //答案置为空	
	$("#password").val(""); //密码置为空
	$("#password2").val(""); //确认密码置为空
});

//确定
function save(){
	if($("#step").val()=="1"){  //1、填写用户名
		//用户名验证
		if($("#userName").val()==""){
			Public.alert(3,"请输入用户名");
			return false;
		}else if($("#userName").val().length>20){
			Public.alert(3,"问题答案长度不能超过20");
			return false;
		}
		
		//1、根据用户名查询指定的问题
		$.ajax({
	        cache: true,
	        type: "POST",
	        url:"getQuestionByUserName.do",
	        data:$('#frm').serialize(),
	        async: false,
	        error: function(request) {
	        	Public.alert(2,"服务器异常！");
	        },
	        success: function(data) {
	        	if(data.status == "ok"){
	        		//如果用户名存在，进入第二步
	        		//(1)修改状态
	        		$("#question").html(data.question);
	        		$("#step").val("2");
	        		//(2)修改进度
	        		$("#two").addClass("active");
	        		//(3)修改标题
	        		$(".title").html("2、问题验证");
	        		//(4)隐藏用户名，显示问题和答案
	        		$(".step_1").hide();
	        		$(".step_2").show();
	        	}else{
	        		//如果用户名不存在，提醒重新输入
	        		Public.alert(2,"用户名不存在，请重新输入！");
	        	}
	        }
	    });
	
	}else if($("#step").val()=="2"){ //2、验证问题
		//答案验证
		if($("#answer").val()==""){
			Public.alert(3,"请输入问题答案");
			return false;
		}else if($("#answer").val().length>200){
			Public.alert(3,"问题答案长度不能超过200");
			return false;
		}
		
		//将答案传入后台比对
		$.ajax({
	        cache: true,
	        type: "POST",
	        url:"chackAnswer.do",
	        data:$('#frm').serialize(),
	        async: false,
	        error: function(request) {
	        	Public.alert(2,"服务器异常！");
	        },
	        success: function(data) {
	        	if(data.status == "ok"){
	        		//若正确，进入第三步
	        		//(1)修改状态
	        		$("#step").val("3");
	        		//(2)修改进度
	        		$("#three").addClass("active");
	        		//(3)修改标题
	        		$(".title").html("3、设置新密码");
	        		//(4)隐藏用户名，显示问题和答案
	        		$(".step_2").hide();
	        		$(".step_3").show();
	        	}else{
	        		//若错误，提示
	        		Public.alert(2,"答案错误！");
	        	}
	        }
	    });
		
		
	}else if($("#step").val()=="3"){ //3.重置密码
		//密码验证
		if($("#password").val()==""){
			Public.alert(3,"请输入密码");
			return false;
		}else if($("#password").val().length>20 || $("#password").val().length<6){
			Public.alert(3,"密码长度必须在6到20之间");
			return false;
		}else{
			var reg = /^[a-zA-Z0-9_\.]+$/;
			if(!reg.test($("#password").val())){
				Public.alert(3,"密码只能包含字母、数字、下划线和小数点");
				return false;
			}
		}
		
		//确认密码验证
		if($("#password2").val()==""){
			Public.alert(3,"请输入确认密码");
			return false;
		}else if($("#password2").val().length>20 || $("#password2").val().length<6){
			Public.alert(3,"确认密码长度必须在6到20之间");
			return false;
		}else{
			var reg = /^[a-zA-Z0-9_\.]+$/;
			if(!reg.test($("#password2").val())){
				Public.alert(3,"确认密码只能包含字母、数字、下划线和小数点");
				return false;
			}
		}
		
		if( $("#password").val() != $("#password2").val() ){
			Public.alert(3,"两次输入的密码不一样");
			return false;
		}
		
		//将密码保存至数据库中
		$.ajax({
	        cache: true,
	        type: "POST",
	        url:"modifyPassword.do",
	        data:$('#frm').serialize(),
	        async: false,
	        error: function(request) {
	        	Public.alert(2,"服务器异常！");
	        },
	        success: function(data) {
	        	if(data.status == "ok"){
	        		//若成功，进入第四步
	        		//(1)修改状态
	        		$("#step").val("4");
	        		//(2)修改进度
	        		$("#four").addClass("active");
	        		//(3)修改标题
	        		$(".title").html("4、完成");
	        		//(4)隐藏用户名，显示问题和答案
	        		$(".step_3").hide();
	        		$(".step_4").show();
	        		
	        	}else{
	        		//若错误，提示
	        		Public.alert(2,"服务器异常");
	        	}
	        }
	    });
		
	}else if($("#step").val()=="4"){ //4、完成
		window.close();
	}
	
	
}