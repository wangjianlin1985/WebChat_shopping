<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/evaluate.css" />
<div id="evaluate_editDiv">
	<form id="evaluateEditForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">评价编号:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="evaluate_evaluateId_edit" name="evaluate.evaluateId" value="<%=request.getParameter("evaluateId") %>" style="width:200px" />
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
		<div class="operation">
			<a id="evaluateModifyButton" class="easyui-linkbutton">更新</a> 
		</div>
	</form>
</div>
<script src="${pageContext.request.contextPath}/Evaluate/js/evaluate_modify.js"></script> 
