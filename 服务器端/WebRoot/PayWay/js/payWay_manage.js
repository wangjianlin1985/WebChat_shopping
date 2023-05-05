var payWay_manage_tool = null; 
$(function () { 
	initPayWayManageTool(); //建立PayWay管理对象
	payWay_manage_tool.init(); //如果需要通过下拉框查询，首先初始化下拉框的值
	$("#payWay_manage").datagrid({
		url : 'PayWay/list',
		fit : true,
		fitColumns : true,
		striped : true,
		rownumbers : true,
		border : false,
		pagination : true,
		pageSize : 5,
		pageList : [5, 10, 15, 20, 25],
		pageNumber : 1,
		sortName : "payWayId",
		sortOrder : "desc",
		toolbar : "#payWay_manage_tool",
		columns : [[
			{
				field : "payWayId",
				title : "支付方式id",
				width : 70,
			},
			{
				field : "payWayName",
				title : "支付方式名称",
				width : 140,
			},
		]],
	});

	$("#payWayEditDiv").dialog({
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
				if ($("#payWayEditForm").form("validate")) {
					//验证表单 
					if(!$("#payWayEditForm").form("validate")) {
						$.messager.alert("错误提示","你输入的信息还有错误！","warning");
					} else {
						$("#payWayEditForm").form({
						    url:"PayWay/" + $("#payWay_payWayId_edit").val() + "/update",
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
						    	console.log(data);
			                	var obj = jQuery.parseJSON(data);
			                    if(obj.success){
			                        $.messager.alert("消息","信息修改成功！");
			                        $("#payWayEditDiv").dialog("close");
			                        payWay_manage_tool.reload();
			                    }else{
			                        $.messager.alert("消息",obj.message);
			                    } 
						    }
						});
						//提交表单
						$("#payWayEditForm").submit();
					}
				}
			},
		},{
			text : "取消",
			iconCls : "icon-redo",
			handler : function () {
				$("#payWayEditDiv").dialog("close");
				$("#payWayEditForm").form("reset"); 
			},
		}],
	});
});

function initPayWayManageTool() {
	payWay_manage_tool = {
		init: function() {
		},
		reload : function () {
			$("#payWay_manage").datagrid("reload");
		},
		redo : function () {
			$("#payWay_manage").datagrid("unselectAll");
		},
		search: function() {
			$("#payWay_manage").datagrid("load");
		},
		exportExcel: function() {
			$("#payWayQueryForm").form({
			    url:"PayWay/OutToExcel",
			});
			//提交表单
			$("#payWayQueryForm").submit();
		},
		remove : function () {
			var rows = $("#payWay_manage").datagrid("getSelections");
			if (rows.length > 0) {
				$.messager.confirm("确定操作", "您正在要删除所选的记录吗？", function (flag) {
					if (flag) {
						var payWayIds = [];
						for (var i = 0; i < rows.length; i ++) {
							payWayIds.push(rows[i].payWayId);
						}
						$.ajax({
							type : "POST",
							url : "PayWay/deletes",
							data : {
								payWayIds : payWayIds.join(","),
							},
							beforeSend : function () {
								$("#payWay_manage").datagrid("loading");
							},
							success : function (data) {
								if (data.success) {
									$("#payWay_manage").datagrid("loaded");
									$("#payWay_manage").datagrid("load");
									$("#payWay_manage").datagrid("unselectAll");
									$.messager.show({
										title : "提示",
										msg : data.message
									});
								} else {
									$("#payWay_manage").datagrid("loaded");
									$("#payWay_manage").datagrid("load");
									$("#payWay_manage").datagrid("unselectAll");
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
			var rows = $("#payWay_manage").datagrid("getSelections");
			if (rows.length > 1) {
				$.messager.alert("警告操作！", "编辑记录只能选定一条数据！", "warning");
			} else if (rows.length == 1) {
				$.ajax({
					url : "PayWay/" + rows[0].payWayId +  "/update",
					type : "get",
					data : {
						//payWayId : rows[0].payWayId,
					},
					beforeSend : function () {
						$.messager.progress({
							text : "正在获取中...",
						});
					},
					success : function (payWay, response, status) {
						$.messager.progress("close");
						if (payWay) { 
							$("#payWayEditDiv").dialog("open");
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
						}
					}
				});
			} else if (rows.length == 0) {
				$.messager.alert("警告操作！", "编辑记录至少选定一条数据！", "warning");
			}
		},
	};
}
