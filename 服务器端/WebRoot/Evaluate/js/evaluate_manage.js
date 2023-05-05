var evaluate_manage_tool = null; 
$(function () { 
	initEvaluateManageTool(); //建立Evaluate管理对象
	evaluate_manage_tool.init(); //如果需要通过下拉框查询，首先初始化下拉框的值
	$("#evaluate_manage").datagrid({
		url : 'Evaluate/list',
		fit : true,
		fitColumns : true,
		striped : true,
		rownumbers : true,
		border : false,
		pagination : true,
		pageSize : 5,
		pageList : [5, 10, 15, 20, 25],
		pageNumber : 1,
		sortName : "evaluateId",
		sortOrder : "desc",
		toolbar : "#evaluate_manage_tool",
		columns : [[
			{
				field : "evaluateId",
				title : "评价编号",
				width : 70,
			},
			{
				field : "productObj",
				title : "商品名称",
				width : 140,
			},
			{
				field : "userObj",
				title : "用户名",
				width : 140,
			},
			{
				field : "content",
				title : "评价内容",
				width : 140,
			},
			{
				field : "evaluateTime",
				title : "评价时间",
				width : 140,
			},
		]],
	});

	$("#evaluateEditDiv").dialog({
		title : "修改管理",
		top: "50px",
		width : 700,
		height : 515,
		modal : true,
		closed : true,
		iconCls : "icon-edit-new",
		buttons : [{
			text : "提交",
			iconCls : "icon-edit-new",
			handler : function () {
				if ($("#evaluateEditForm").form("validate")) {
					//验证表单 
					if(!$("#evaluateEditForm").form("validate")) {
						$.messager.alert("错误提示","你输入的信息还有错误！","warning");
					} else {
						$("#evaluateEditForm").form({
						    url:"Evaluate/" + $("#evaluate_evaluateId_edit").val() + "/update",
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
						    	console.log(data);
			                	var obj = jQuery.parseJSON(data);
			                    if(obj.success){
			                        $.messager.alert("消息","信息修改成功！");
			                        $("#evaluateEditDiv").dialog("close");
			                        evaluate_manage_tool.reload();
			                    }else{
			                        $.messager.alert("消息",obj.message);
			                    } 
						    }
						});
						//提交表单
						$("#evaluateEditForm").submit();
					}
				}
			},
		},{
			text : "取消",
			iconCls : "icon-redo",
			handler : function () {
				$("#evaluateEditDiv").dialog("close");
				$("#evaluateEditForm").form("reset"); 
			},
		}],
	});
});

function initEvaluateManageTool() {
	evaluate_manage_tool = {
		init: function() {
			$.ajax({
				url : "Product/listAll",
				type : "post",
				success : function (data, response, status) {
					$("#productObj_productId_query").combobox({ 
					    valueField:"productId",
					    textField:"productName",
					    panelHeight: "200px",
				        editable: false, //不允许手动输入 
					});
					data.splice(0,0,{productId:0,productName:"不限制"});
					$("#productObj_productId_query").combobox("loadData",data); 
				}
			});
			$.ajax({
				url : "UserInfo/listAll",
				type : "post",
				success : function (data, response, status) {
					$("#userObj_user_name_query").combobox({ 
					    valueField:"user_name",
					    textField:"name",
					    panelHeight: "200px",
				        editable: false, //不允许手动输入 
					});
					data.splice(0,0,{user_name:"",name:"不限制"});
					$("#userObj_user_name_query").combobox("loadData",data); 
				}
			});
		},
		reload : function () {
			$("#evaluate_manage").datagrid("reload");
		},
		redo : function () {
			$("#evaluate_manage").datagrid("unselectAll");
		},
		search: function() {
			var queryParams = $("#evaluate_manage").datagrid("options").queryParams;
			queryParams["productObj.productId"] = $("#productObj_productId_query").combobox("getValue");
			queryParams["userObj.user_name"] = $("#userObj_user_name_query").combobox("getValue");
			$("#evaluate_manage").datagrid("options").queryParams=queryParams; 
			$("#evaluate_manage").datagrid("load");
		},
		exportExcel: function() {
			$("#evaluateQueryForm").form({
			    url:"Evaluate/OutToExcel",
			});
			//提交表单
			$("#evaluateQueryForm").submit();
		},
		remove : function () {
			var rows = $("#evaluate_manage").datagrid("getSelections");
			if (rows.length > 0) {
				$.messager.confirm("确定操作", "您正在要删除所选的记录吗？", function (flag) {
					if (flag) {
						var evaluateIds = [];
						for (var i = 0; i < rows.length; i ++) {
							evaluateIds.push(rows[i].evaluateId);
						}
						$.ajax({
							type : "POST",
							url : "Evaluate/deletes",
							data : {
								evaluateIds : evaluateIds.join(","),
							},
							beforeSend : function () {
								$("#evaluate_manage").datagrid("loading");
							},
							success : function (data) {
								if (data.success) {
									$("#evaluate_manage").datagrid("loaded");
									$("#evaluate_manage").datagrid("load");
									$("#evaluate_manage").datagrid("unselectAll");
									$.messager.show({
										title : "提示",
										msg : data.message
									});
								} else {
									$("#evaluate_manage").datagrid("loaded");
									$("#evaluate_manage").datagrid("load");
									$("#evaluate_manage").datagrid("unselectAll");
									$.messager.alert("消息",data.message);
								}
							},
						});
					}
				});
			} else {
				$.messager.alert("提示", "请选择要删除的记录！", "info");
			}
		},
		edit : function () {
			var rows = $("#evaluate_manage").datagrid("getSelections");
			if (rows.length > 1) {
				$.messager.alert("警告操作！", "编辑记录只能选定一条数据！", "warning");
			} else if (rows.length == 1) {
				$.ajax({
					url : "Evaluate/" + rows[0].evaluateId +  "/update",
					type : "get",
					data : {
						//evaluateId : rows[0].evaluateId,
					},
					beforeSend : function () {
						$.messager.progress({
							text : "正在获取中...",
						});
					},
					success : function (evaluate, response, status) {
						$.messager.progress("close");
						if (evaluate) { 
							$("#evaluateEditDiv").dialog("open");
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
						}
					}
				});
			} else if (rows.length == 0) {
				$.messager.alert("警告操作！", "编辑记录至少选定一条数据！", "warning");
			}
		},
	};
}
