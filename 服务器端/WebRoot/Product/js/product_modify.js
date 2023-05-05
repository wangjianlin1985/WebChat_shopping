$(function () {
	$.ajax({
		url : "Product/" + $("#product_productId_edit").val() + "/update",
		type : "get",
		data : {
			//productId : $("#product_productId_edit").val(),
		},
		beforeSend : function () {
			$.messager.progress({
				text : "正在获取中...",
			});
		},
		success : function (product, response, status) {
			$.messager.progress("close");
			if (product) { 
				$("#product_productId_edit").val(product.productId);
				$("#product_productId_edit").validatebox({
					required : true,
					missingMessage : "请输入商品id",
					editable: false
				});
				$("#product_productClassObj_classId_edit").combobox({
					url:"ProductClass/listAll",
					valueField:"classId",
					textField:"className",
					panelHeight: "auto",
					editable: false, //不允许手动输入 
					onLoadSuccess: function () { //数据加载完毕事件
						$("#product_productClassObj_classId_edit").combobox("select", product.productClassObjPri);
						//var data = $("#product_productClassObj_classId_edit").combobox("getData"); 
						//if (data.length > 0) {
							//$("#product_productClassObj_classId_edit").combobox("select", data[0].classId);
						//}
					}
				});
				$("#product_productName_edit").val(product.productName);
				$("#product_productName_edit").validatebox({
					required : true,
					missingMessage : "请输入商品名称",
				});
				$("#product_mainPhoto").val(product.mainPhoto);
				$("#product_mainPhotoImg").attr("src", product.mainPhoto);
				$("#product_price_edit").val(product.price);
				$("#product_price_edit").validatebox({
					required : true,
					validType : "number",
					missingMessage : "请输入商品价格",
					invalidMessage : "商品价格输入不对",
				});
				$("#product_productCount_edit").val(product.productCount);
				$("#product_productCount_edit").validatebox({
					required : true,
					validType : "integer",
					missingMessage : "请输入商量数量",
					invalidMessage : "商量数量输入不对",
				});
				$("#product_productDesc_edit").val(product.productDesc);
				$("#product_productDesc_edit").validatebox({
					required : true,
					missingMessage : "请输入商品描述",
				});
				$("#product_recommendFlag_edit").val(product.recommendFlag);
				$("#product_recommendFlag_edit").validatebox({
					required : true,
					missingMessage : "请输入是否推荐",
				});
				$("#product_hotNum_edit").val(product.hotNum);
				$("#product_hotNum_edit").validatebox({
					required : true,
					validType : "integer",
					missingMessage : "请输入人气值",
					invalidMessage : "人气值输入不对",
				});
				$("#product_addTime_edit").datetimebox({
					value: product.addTime,
					required: true,
					showSeconds: true,
				});
			} else {
				$.messager.alert("获取失败！", "未知错误导致失败，请重试！", "warning");
				$(".messager-window").css("z-index",10000);
			}
		}
	});

	$("#productModifyButton").click(function(){ 
		if ($("#productEditForm").form("validate")) {
			$("#productEditForm").form({
			    url:"Product/" +  $("#product_productId_edit").val() + "/update",
			    onSubmit: function(){
					if($("#productEditForm").form("validate"))  {
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
			$("#productEditForm").submit();
		} else {
			$.messager.alert("错误提示","你输入的信息还有错误！","warning");
			$(".messager-window").css("z-index",10000);
		}
	});
});
