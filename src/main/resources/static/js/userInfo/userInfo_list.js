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
	var userName = $("#userName").val();
	var realName = $("#realName").val();
	$.ajax({
        url: "../getUserInfos.do",
        dataType: "json",
        type: "POST",
        data: {"cnt":cnt,"userName":userName,"realName":realName},
        success: function (data) {
                var html= "";
                if(data.status == "ok"){
                	 $.each(data.userInfos, function (i, item) {
                     html +="<tr>"+
							    "<td>"+(i+1)+"</td>"+
							    "<td>"+item.userName+"</td>"+
							    "<td>"+item.realName+"</td>"+
							    "<td>"+item.phoneNumber+"</td>"+
							    "<td>"+item.qq+"</td>"+
							    "<td><button class= \"btn btn-primary btn-xs\" onclick=\"view('"+item.id+"');\">查看详情</button> </td>"+
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
        }
    });
}


function view(id) {
	window.open('userInfo/userInfo_detail.html?id='+id);
}


