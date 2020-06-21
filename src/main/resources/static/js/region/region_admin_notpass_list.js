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
	var sqdw = $("#sqdw").val();
	var lxr = $("#lxr").val();
	var realName = $("#realName").val();
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	$.ajax({
        url: "../selectRegionListAdmin.do",
        dataType: "json",
        type: "POST",
        data: { "cnt":cnt,
        		"sqdw":sqdw,
        		"lxr":lxr,
        		"realName":realName,
        		"startDate":startDate,
        		"endDate":endDate,
        		"status":"2"},
        success: function (data) {
                var html= "";
                if(data.status == "ok"){
                	 $.each(data.list, function (i, item) {
                     html +="<tr>"+
							    "<td>"+(i+1)+"</td>"+
							    "<td>"+item.sqdw+"</td>"+
							    "<td>"+item.lxr+"</td>"+
							    "<td>"+item.userName+"</td>"+
							    "<td>"+item.createtime+"</td>"+
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

//查看
function view(id){
	if(id==""||id=="undefined"){
		Public.alert(2,"查看出现异常！");   
	}else{
		window.open("region/region_VIEW.html?id="+id);
	}
}

//下载
function download(id){
	if(id==""||id=="undefined"){
		Public.alert(2,"下载出现异常！");   
	}else{
		window.location.href="../downloadRegion.do?id="+id;	
	}
}

