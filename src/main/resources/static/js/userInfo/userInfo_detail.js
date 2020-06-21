/**
 * 用户信息
 */
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
var curr = 1;
$(function(){
	id = Public.urlParam("id");
	load(id);
});

function load(id){
	$.ajax({
        url: "../../getUserInfoDetail.do",
        dataType: "json",
        type: "POST",
        data: {"id":id},
        success: function (data) {
        	var userInfo = data.userInfo;
        	$("#userName").html(userInfo.userName);
        	$("#email").html(userInfo.email);
        	$("#phoneNumber").html(userInfo.phoneNumber);
        	$("#qq").html(userInfo.qq);
        	$("#realName").html(userInfo.realName);
        	$("#cid").html(userInfo.cid);
        	$("#companyName").html(userInfo.companyName);
        	$("#address").html(userInfo.address);
        	$("#endTime").html(userInfo.endTime);
        }
    });
}


