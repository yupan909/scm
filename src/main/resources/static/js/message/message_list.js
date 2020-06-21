/**
 * 消息列表
 */
$.ajaxSetup({     
    contentType:"application/x-www-form-urlencoded;charset=utf-8",     
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
});

function load(cnt){
	$.ajax({
        url: "../getMsg.do",
        dataType: "json",
        type: "POST",
        data: {"cnt":cnt},
        success: function (data) {
            var html= "";
            	 $.each(data.msgs, function (i, item) {
                 var isRead = "";
            	 if(item.isRead =='N'){
            		 isRead = "<td><span class='label label-danger'>未 读</span></td><td><button class=\"btn btn-primary btn-xs\" onclick=\"setIsRead('"+item.id+"');\">设为已读</button></td>";
            	 }else{
            		 isRead = "<td><span class='label label-success'>已 读</span></td><td></td>";
            	 }
                 html +="<tr>"+
						    "<td>"+(i+1)+"</td>"+
						    "<td>"+item.createTime+"</td>"+
						    "<td>"+item.msgDetail+"</td>"+
						    isRead+
					        "</tr>";
                });
                $("#tbody").html(html);
                laypage({
                    cont: 'page', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
                    pages: Math.ceil(data.cnt / 10), //通过后台拿到的总页数
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
    });
}


function setIsRead(id){
	$.ajax({
        url: "../setIsRead.do",
        dataType: "json",
        type: "POST",
        data: {"id":id},
        success: function (data) {
        	if(data.status =="ok"){
        		load(curr);
        		
        		getMsgCount(); //我的消息--待读总条数
        	}
        }
    });
}


