$(function () {
	$.ajax({
		url : "Evaluate/" + $("#evaluate_evaluateId_edit").val() + "/update",
		type : "get",
		data : {
			//evaluateId : $("#evaluate_evaluateId_edit").val(),
		},
		beforeSend : function () {
			$.messager.progress({
				text : "正在获取中...",
			});
		},
		success : function (evaluate, response, status) {
			$.messager.progress("close");
			if (evaluate) { 
				$("#evaluate_evaluateId_edit").val(evaluate.evaluateId);
				$("#evaluate_evaluateId_edit").validatebox({
					required : true,
					missingMessage : "请输入评价编号",
					editable: false
				});
				$("#evaluate_productObj_productId_edit").combobox({
					url:"Product/listAll",
					valueField:"productId",
					textField:"productName",
					panelHeight: "auto",
					editable: false, //不允许手动输入 
					onLoadSuccess: function () { //数据加载完毕事件
						$("#evaluate_productObj_productId_edit").combobox("select", evaluate.productObjPri);
						//var data = $("#evaluate_productObj_productId_edit").combobox("getData"); 
						//if (data.length > 0) {
							//$("#evaluate_productObj_productId_edit").combobox("select", data[0].productId);
						//}
					}
				});
				$("#evaluate_userObj_user_name_edit").combobox({
					url:"UserInfo/listAll",
					valueField:"user_name",
					textField:"name",
					panelHeight: "auto",
					editable: false, //不允许手动输入 
					onLoadSuccess: function () { //数据加载完毕事件
						$("#evaluate_userObj_user_name_edit").combobox("select", evaluate.userObjPri);
						//var data = $("#evaluate_userObj_user_name_edit").combobox("getData"); 
						//if (data.length > 0) {
							//$("#evaluate_userObj_user_name_edit").combobox("select", data[0].user_name);
						//}
					}
				});
				$("#evaluate_content_edit").val(evaluate.content);
				$("#evaluate_content_edit").validatebox({
					required : true,
					missingMessage : "请输入评价内容",
				});
				$("#evaluate_evaluateTime_edit").val(evaluate.evaluateTime);
				$("#evaluate_evaluateTime_edit").validatebox({
					required : true,
					missingMessage : "请输入评价时间",
				});
			} else {
				$.messager.alert("获取失败！", "未知错误导致失败，请重试！", "warning");
				$(".messager-window").css("z-index",10000);
			}
		}
	});

	$("#evaluateModifyButton").click(function(){ 
		if ($("#evaluateEditForm").form("validate")) {
			$("#evaluateEditForm").form({
			    url:"Evaluate/" +  $("#evaluate_evaluateId_edit").val() + "/update",
			    onSubmit: function(){
					if($("#evaluateEditForm").form("validate"))  {
	                	$.messager.progress({
							text : "正在提交数据中...",
						});
	                	return true;
	                } else {
	                    return false;
	                }
			    },
			    success:function(data){
			    	$.messager.progress("close");
                	var obj = jQuery.parseJSON(data);
                    if(obj.success){
                        $.messager.alert("消息","信息修改成功！");
                        $(".messager-window").css("z-index",10000);
                        //location.href="frontlist";
                    }else{
                        $.messager.alert("消息",obj.message);
                        $(".messager-window").css("z-index",10000);
                    } 
			    }
			});
			//提交表单
			$("#evaluateEditForm").submit();
		} else {
			$.messager.alert("错误提示","你输入的信息还有错误！","warning");
			$(".messager-window").css("z-index",10000);
		}
	});
});
