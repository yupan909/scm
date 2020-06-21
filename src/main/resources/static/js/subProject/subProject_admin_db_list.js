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

//查看
function view(id){
	if(id==""||id=="undefined"){
		Public.alert(2,"查看出现异常！");   
	}else{
		window.open("subProject/subProject_HANDEL.html?id="+id);
	}
}
function load(cnt){
	var ktmc = $("#ktmc_search").val();
	var ztc = $("#ztc_search").val();
	var realName = $("#realName_search").val();
	var startDate = $("#startDate").val();
	var endTime = $("#endDate").val();
	$.ajax({
        url: "../getSubProjectForAdmin.do",
        dataType: "json",
        type: "POST",
        data: {"cnt":cnt,"ktmc":ktmc,"ztc":ztc,"realName":realName,"startDate":startDate,"endTime":endTime,"status":"1"},
        success: function (data) {
                var html= "";
                if(data.status == "ok"){
                	 $.each(data.subProjects, function (i, item) {
                     html +="<tr>"+
							    "<td>"+(i+1)+"</td>"+
							    "<td>"+item.ktmc+"</td>"+
							    "<td>"+item.ztc+"</td>"+
							    "<td>"+item.userName+"</td>"+
							    "<td>"+item.createTime+"</td>"+
							    "<td><button class=\"btn btn-primary btn-xs\" onclick=\"view('"+item.id+"');\">查看</button>  <button class=\"btn btn-info btn-xs\" onclick=\"download('"+item.id+"');\">下载</button></td>"+
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

//下载
function download(id){
	if(id==""||id=="undefined"){
		Public.alert(2,"下载出现异常！");   
	}else{
		window.location.href="../downloadSubProject.do?id="+id;	
	}
}

