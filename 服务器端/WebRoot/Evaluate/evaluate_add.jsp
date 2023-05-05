<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/evaluate.css" />
<div id="evaluateAddDiv">
	<form id="evaluateAddForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">商品名称:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="evaluate_productObj_productId" name="evaluate.productObj.productId" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">用户名:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="evaluate_userObj_user_name" name="evaluate.userObj.user_name" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">评价内容:</span>
			<span class="inputControl">
				<textarea id="evaluate_content" name="evaluate.content" rows="6" cols="80"></textarea>

			</span>

		</div>
		<div>
			<span class="label">评价时间:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="evaluate_evaluateTime" name="evaluate.evaluateTime" style="width:200px" />

			</span>

		</div>
		<div class="operation">
			<a id="evaluateAddButton" class="easyui-linkbutton">添加</a>
			<a id="evaluateClearButton" class="easyui-linkbutton">重填</a>
		</div> 
	</form>
</div>
<script src="${pageContext.request.contextPath}/Evaluate/js/evaluate_add.js"></script> 
