<%@ page language="java"  contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/> 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/evaluate.css" /> 

<div id="evaluate_manage"></div>
<div id="evaluate_manage_tool" style="padding:5px;">
	<div style="margin-bottom:5px;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit-new" plain="true" onclick="evaluate_manage_tool.edit();">修改</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-delete-new" plain="true" onclick="evaluate_manage_tool.remove();">删除</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true"  onclick="evaluate_manage_tool.reload();">刷新</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="evaluate_manage_tool.redo();">取消选择</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-export" plain="true" onclick="evaluate_manage_tool.exportExcel();">导出到excel</a>
	</div>
	<div style="padding:0 0 0 7px;color:#333;">
		<form id="evaluateQueryForm" method="post">
			商品名称：<input class="textbox" type="text" id="productObj_productId_query" name="productObj.productId" style="width: auto"/>
			用户名：<input class="textbox" type="text" id="userObj_user_name_query" name="userObj.user_name" style="width: auto"/>
			<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="evaluate_manage_tool.search();">查询</a>
		</form>	
	</div>
</div>

<div id="evaluateEditDiv">
	<form id="evaluateEditForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">评价编号:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="evaluate_evaluateId_edit" name="evaluate.evaluateId" style="width:200px" />
			</span>
		</div>
		<div>
			<span class="label">商品名称:</span>
			<span class="inputControl">
				<input class="textbox"  id="evaluate_productObj_productId_edit" name="evaluate.productObj.productId" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">用户名:</span>
			<span class="inputControl">
				<input class="textbox"  id="evaluate_userObj_user_name_edit" name="evaluate.userObj.user_name" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">评价内容:</span>
			<span class="inputControl">
				<textarea id="evaluate_content_edit" name="evaluate.content" rows="8" cols="60"></textarea>

			</span>

		</div>
		<div>
			<span class="label">评价时间:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="evaluate_evaluateTime_edit" name="evaluate.evaluateTime" style="width:200px" />

			</span>

		</div>
	</form>
</div>
<script type="text/javascript" src="Evaluate/js/evaluate_manage.js"></script> 
