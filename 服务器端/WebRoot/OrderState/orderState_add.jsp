<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/orderState.css" />
<div id="orderStateAddDiv">
	<form id="orderStateAddForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">订单状态名称:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="orderState_stateName" name="orderState.stateName" style="width:200px" />

			</span>

		</div>
		<div class="operation">
			<a id="orderStateAddButton" class="easyui-linkbutton">添加</a>
			<a id="orderStateClearButton" class="easyui-linkbutton">重填</a>
		</div> 
	</form>
</div>
<script src="${pageContext.request.contextPath}/OrderState/js/orderState_add.js"></script> 
