$(function () {
	$.ajax({
		url : "PayWay/" + $("#payWay_payWayId_edit").val() + "/update",
		type : "get",
		data : {
			//payWayId : $("#payWay_payWayId_edit").val(),
		},
		beforeSend : function () {
			$.messager.progress({
				text : "正在获取中...",
			});
		},
		success : function (payWay, response, status) {
			$.messager.progress("close");
			if (payWay) { 
				$("#payWay_payWayId_edit").val(payWay.payWayId);
				$("#payWay_payWayId_edit").validatebox({
					required : true,
					missingMessage : "请输入支付方式id",
					editable: false
				});
				$("#payWay_payWayName_edit").val(payWay.payWayName);
				$("#payWay_payWayName_edit").validatebox({
					required : true,
					missingMessage : "请输入支付方式名称",
				});
			} else {
				$.messager.alert("获取失败！", "未知错误导致失败，请重试！", "warning");
				$(".messager-window").css("z-index",10000);
			}
		}
	});

	$("#payWayModifyButton").click(function(){ 
		if ($("#payWayEditForm").form("validate")) {
			$("#payWayEditForm").form({
			    url:"PayWay/" +  $("#payWay_payWayId_edit").val() + "/update",
			    onSubmit: function(){
					if($("#payWayEditForm").form("validate"))  {
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
			$("#payWayEditForm").submit();
		} else {
			$.messager.alert("错误提示","你输入的信息还有错误！","warning");
			$(".messager-window").css("z-index",10000);
		}
	});
});
