var orderState_manage_tool = null; 
$(function () { 
	initOrderStateManageTool(); //建立OrderState管理对象
	orderState_manage_tool.init(); //如果需要通过下拉框查询，首先初始化下拉框的值
	$("#orderState_manage").datagrid({
		url : 'OrderState/list',
		fit : true,
		fitColumns : true,
		striped : true,
		rownumbers : true,
		border : false,
		pagination : true,
		pageSize : 5,
		pageList : [5, 10, 15, 20, 25],
		pageNumber : 1,
		sortName : "stateId",
		sortOrder : "desc",
		toolbar : "#orderState_manage_tool",
		columns : [[
			{
				field : "stateId",
				title : "状态编号",
				width : 70,
			},
			{
				field : "stateName",
				title : "订单状态名称",
				width : 140,
			},
		]],
	});

	$("#orderStateEditDiv").dialog({
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
				if ($("#orderStateEditForm").form("validate")) {
					//验证表单 
					if(!$("#orderStateEditForm").form("validate")) {
						$.messager.alert("错误提示","你输入的信息还有错误！","warning");
					} else {
						$("#orderStateEditForm").form({
						    url:"OrderState/" + $("#orderState_stateId_edit").val() + "/update",
						    onSubmit: function(){
								if($("#orderStateEditForm").form("validate"))  {
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
			                        $("#orderStateEditDiv").dialog("close");
			                        orderState_manage_tool.reload();
			                    }else{
			                        $.messager.alert("消息",obj.message);
			                    } 
						    }
						});
						//提交表单
						$("#orderStateEditForm").submit();
					}
				}
			},
		},{
			text : "取消",
			iconCls : "icon-redo",
			handler : function () {
				$("#orderStateEditDiv").dialog("close");
				$("#orderStateEditForm").form("reset"); 
			},
		}],
	});
});

function initOrderStateManageTool() {
	orderState_manage_tool = {
		init: function() {
		},
		reload : function () {
			$("#orderState_manage").datagrid("reload");
		},
		redo : function () {
			$("#orderState_manage").datagrid("unselectAll");
		},
		search: function() {
			$("#orderState_manage").datagrid("load");
		},
		exportExcel: function() {
			$("#orderStateQueryForm").form({
			    url:"OrderState/OutToExcel",
			});
			//提交表单
			$("#orderStateQueryForm").submit();
		},
		remove : function () {
			var rows = $("#orderState_manage").datagrid("getSelections");
			if (rows.length > 0) {
				$.messager.confirm("确定操作", "您正在要删除所选的记录吗？", function (flag) {
					if (flag) {
						var stateIds = [];
						for (var i = 0; i < rows.length; i ++) {
							stateIds.push(rows[i].stateId);
						}
						$.ajax({
							type : "POST",
							url : "OrderState/deletes",
							data : {
								stateIds : stateIds.join(","),
							},
							beforeSend : function () {
								$("#orderState_manage").datagrid("loading");
							},
							success : function (data) {
								if (data.success) {
									$("#orderState_manage").datagrid("loaded");
									$("#orderState_manage").datagrid("load");
									$("#orderState_manage").datagrid("unselectAll");
									$.messager.show({
										title : "提示",
										msg : data.message
									});
								} else {
									$("#orderState_manage").datagrid("loaded");
									$("#orderState_manage").datagrid("load");
									$("#orderState_manage").datagrid("unselectAll");
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
			var rows = $("#orderState_manage").datagrid("getSelections");
			if (rows.length > 1) {
				$.messager.alert("警告操作！", "编辑记录只能选定一条数据！", "warning");
			} else if (rows.length == 1) {
				$.ajax({
					url : "OrderState/" + rows[0].stateId +  "/update",
					type : "get",
					data : {
						//stateId : rows[0].stateId,
					},
					beforeSend : function () {
						$.messager.progress({
							text : "正在获取中...",
						});
					},
					success : function (orderState, response, status) {
						$.messager.progress("close");
						if (orderState) { 
							$("#orderStateEditDiv").dialog("open");
							$("#orderState_stateId_edit").val(orderState.stateId);
							$("#orderState_stateId_edit").validatebox({
								required : true,
								missingMessage : "请输入状态编号",
								editable: false
							});
							$("#orderState_stateName_edit").val(orderState.stateName);
							$("#orderState_stateName_edit").validatebox({
								required : true,
								missingMessage : "请输入订单状态名称",
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
