$(function () {
	$("#product_productClassObj_classId").combobox({
	    url:'ProductClass/listAll',
	    valueField: "classId",
	    textField: "className",
	    panelHeight: "auto",
        editable: false, //不允许手动输入
        required : true,
        onLoadSuccess: function () { //数据加载完毕事件
            var data = $("#product_productClassObj_classId").combobox("getData"); 
            if (data.length > 0) {
                $("#product_productClassObj_classId").combobox("select", data[0].classId);
            }
        }
	});
	$("#product_productName").validatebox({
		required : true, 
		missingMessage : '请输入商品名称',
	});

	$("#product_price").validatebox({
		required : true,
		validType : "number",
		missingMessage : '请输入商品价格',
		invalidMessage : '商品价格输入不对',
	});

	$("#product_productCount").validatebox({
		required : true,
		validType : "integer",
		missingMessage : '请输入商量数量',
		invalidMessage : '商量数量输入不对',
	});

	$("#product_productDesc").validatebox({
		required : true, 
		missingMessage : '请输入商品描述',
	});

	$("#product_recommendFlag").validatebox({
		required : true, 
		missingMessage : '请输入是否推荐',
	});

	$("#product_hotNum").validatebox({
		required : true,
		validType : "integer",
		missingMessage : '请输入人气值',
		invalidMessage : '人气值输入不对',
	});

	$("#product_addTime").datetimebox({
	    required : true, 
	    showSeconds: true,
	    editable: false
	});

	//单击添加按钮
	$("#productAddButton").click(function () {
		//验证表单 
		if(!$("#productAddForm").form("validate")) {
			$.messager.alert("错误提示","你输入的信息还有错误！","warning");
			$(".messager-window").css("z-index",10000);
		} else {
			$("#productAddForm").form({
			    url:"Product/add",
			    onSubmit: function(){
					if($("#productAddForm").form("validate"))  { 
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
                    //此处data={"Success":true}是字符串
                	var obj = jQuery.parseJSON(data); 
                    if(obj.success){ 
                        $.messager.alert("消息","保存成功！");
                        $(".messager-window").css("z-index",10000);
                        $("#productAddForm").form("clear");
                    }else{
                        $.messager.alert("消息",obj.message);
                        $(".messager-window").css("z-index",10000);
                    }
			    }
			});
			//提交表单
			$("#productAddForm").submit();
		}
	});

	//单击清空按钮
	$("#productClearButton").click(function () { 
		$("#productAddForm").form("clear"); 
	});
});
