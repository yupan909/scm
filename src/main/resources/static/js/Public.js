/**
* 公共js
*/
var Public = {
		
		/**
		 * 提示框 1：成功 2：失败 3：警告
		 */
		alert:function(type,msg,fn){
			var icon = 1; //成功
			if(2==type){ //失败
				icon = 2;
			}else if(3==type){ //警告
				icon = 7;
			}

            layui.layer.open({
                skin: 'layui-layer-molv',
				type:0,
				content:msg,
				icon: icon,
				btn: ['确定'],
			  	yes: fn,
			  	cancel: fn
			}); 
		},

		/**
		 * 获取url地址栏指定的参数
		 * @param name
		 */
		urlParam:function(name){
			var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
			var r = window.location.search.substr(1).match(reg);  //匹配目标参数
			if (r!=null){
				return unescape(r[2]);
			}else{
				 return null; //返回参数值
			} 
		},
		
		/**
		 * 回显数据
		 */
		editData:function(url){
			var id =$("#id").val();
			if(id == "" || id == null || typeof(id) == "undefined"){
				id = Public.urlParam("id");
			}
			$.ajax({
				cache: true,
				type:"post",
				url:url,
				data:{id:id},
				dataType:"json",
				async:false,
				success:function(data){
					if(""==data || null==data || typeof(data)=="undefined"){
						Public.alert(2,"回显数据失败！");
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
					}
				}
			});
		},
		
		/**
		 * 回显数据
		 */
		editDataForSub:function(url){
			var id =$("#id").val();
			if(id == "" || id == null || typeof(id) == "undefined"){
				id = Public.urlParam("id");
			}
			$.ajax({
				cache: true,
				type:"post",
				url:url,
				data:{id:id},
				dataType:"json",
				async:false,
				success:function(data){
					if(""==data || null==data || typeof(data)=="undefined"){
						Public.alert(2,"回显数据失败！");
						return false;
					}
					
					//回显主表
					var sub = data.subProject;
					for(var keys in sub){
						var value = sub[keys];
						var $key = $("#"+keys);
						if($key.is("select")){
							$key.find("option[value='"+value+"']").attr("selected",true);						
						}else if($key.attr("type")=="radio" || $key.attr("type")=="checkbox"){
							$("input[name="+keys+"][value='"+value+"']").attr("checked",true);
						}else if($("[name="+keys+"]").attr("type")=="checkbox"){
							var vals = value.split("|");
							for(var i=0; i<vals.length; i++){
								$("input[name="+keys+"][value='"+vals[i]+"']").attr("checked",true);
							}
						}else{
							if($key.is("span") || $key.is("font")){
								if(keys=="xb"){
									if(value=="1"){
										value = "男";
									}else if(value=="2"){
										value = "女";
									}
								}
								$key.html(value);
							}else{
								$key.val(value);
							}
						}
					}
					
					//回显子表
					var partiesInfo = data.partiesInfo;
					Public.editDataChild(partiesInfo, $("#party"));
					
					var expectAchievements = data.expectAchievements;
					Public.editDataChild(expectAchievements, $("#ach-expect"));
					
					var realAchievements = data.realAchievements;
					Public.editDataChild(realAchievements, $("#ach-real"));
					
				}
			});
		},
		
		//回显子表
		editDataChild:function(data, dataDIV){
			for(var i=0; i<data.length; i++){
				var oneData = data[i];
				var nowLine; //表示当前行
				if(i==0){ //第一行
					nowLine = $(dataDIV).children("div:first"); //第一行
				}else{
					//新建一行
					var firstDiv = "<div class='row'>" + $(dataDIV).children("div:first").html() + "</div>";
					$(dataDIV).append(firstDiv);
					//初始化新增行
					var newDiv = $(dataDIV).children("div:last");
					$("input[type=text]",newDiv).val("");
					$("span.glyphicon",newDiv).removeClass("glyphicon-plus").addClass("glyphicon-minus").attr("onclick","Public.delLine(this);");
				
					nowLine = newDiv; //赋值新行
				}
				
				//回显数据
				for(var key in oneData){
					var $key = $("[name="+key+"]",nowLine);
					var value = oneData[key];
					if($key.is("select")){
						$key.find("option").removeAttr("selected");
						$key.find("option[value='"+value+"']").attr("selected",true);						
					}else{
						if($key.is("span") || $key.is("font")){
							$key.html(value);
						}else{
							$key.val(value);
						}
					}
				 }
			 }	
		},
		
		//新增行
		addLine:function(obj){
			var div = $(obj).parent().parent().parent("div");
			//第一行
			var firstDiv = "<div class='row'>" + $(div).children("div:first").html() + "</div>";
			$(div).append(firstDiv);
			//初始化新增行
			var newDiv = $(div).children("div:last");
			$("input[type=text]",newDiv).val("");
			$("span.glyphicon",newDiv).removeClass("glyphicon-plus").addClass("glyphicon-minus").attr("onclick","Public.delLine(this);");
		},
		
		//删除行
		delLine:function(obj){
			layer.confirm('您确定要删除该行吗?', {icon: 3, title:'提示'}, function(index){
				$(obj).parent().parent("div.row").remove();
				layer.close(index);
			});
		},
		
		/**
		 * 保存表单
		 * 参数1：表单ID
		 * 参数2：提交路径
		 * type：提交或保存
		**/
		saveData:function(frm,action,type){
			var bool = false;
			try{
				$.ajax({
					cache: true,
					type:'post',
					url:action,
					data:$("#"+frm).serialize(),
					dataType:"json",
					async:false,
					success:function(data){
						if(""!=data && null!=data && typeof(data)!="undefined"){
							if("1"==data.msg){
								$("#id").val(data.id); //回显id
								if("submit"!=type){
									Public.alert(1,"保存成功"); 
								}
								bool = true;
							}else{
								if("submit"!=type){
									Public.alert(2,"保存失败");   
								}
								bool = false;
							}
						}else{
							if("submit"!=type){
								Public.alert(2,"保存时出现异常！");  
							}
							bool = false;
						}
					},
					error:function(data){
						Public.alert(2,"请求出现异常！");   
						bool = false;
					}		
				});
			}catch(e){
			}
			return bool;
		},

		storeInfo:function(key,value){
            var storage = window.sessionStorage;
            if(typeof value == "object"){//如果要存储对象，则先转为json串
                value = window.JSON.stringify(value);
            }
            storage.setItem(key, value);
        },
        getInfo:function (key) {
            var storage = window.sessionStorage;
            var value = storage.getItem(key);
            var json = window.JSON.parse(value);//json串转为js对象
            if(typeof json == "object" && json){//利用了一点，当符合json格式，串会成功转为js对象，否则为null
                return json;
            }
            return value;

        },
        getCurrentUser:function () {
            return Public.getInfo("user");
        },
        setCurrentUser:function (user) {
            Public.storeInfo("user",user);
        },

		// 加载仓库下拉列表
		initWarehouse:function(val, val2){
			var warehouseMap = {};
			// 仓库下拉回显
			$.ajax({
				url: "../warehouse/list",
				type: "GET",
				dataType: "json",
				async: false,
				success: function (data) {
					var html = '<option value="">--请选择--</option>';;
					if (data.flag == true) {
						$.each(data.data, function (i, item) {
							html += '<option value="' + item.id + '">' + item.name + '</option>';
							warehouseMap[item.id] = item.name;
						});
						if (val) {
							$("#" + val).html(html);
						}
						if (val2) {
							$("#" + val2).html(html);
						}
					}
				}
			});
			return warehouseMap;
		},

		// 重置验证信息
		resetValidate: function(id) {
			$("#" + id).data('bootstrapValidator').destroy();
		},

		// 验证表单
		doValidate: function(id) {
			$("#" + id).data('bootstrapValidator').validate();
			var flag = $("#" + id).data("bootstrapValidator").isValid();
			return flag;
		},

		// 打开模态窗口
		openModal: function(modalId) {
			var $modal = $('#' + modalId);
			// 显示窗口
			$modal.modal('show');
			// 数据重置
			$modal.find("input").val("");
			$modal.find("textarea").val("");
			$modal.find("select option:first").prop("selected", 'selected');
		},

		// 关闭模态窗口
		closeModal: function(modalId) {
			$('#' + modalId).modal('hide');
			// document.getElementById("edit-close-btn").click();
		},

		// null转化为""
		ifNull: function(value) {
			if (value == null) {
				return "";
			}
			return value;
		}

}