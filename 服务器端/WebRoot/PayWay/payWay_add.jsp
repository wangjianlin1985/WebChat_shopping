<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/payWay.css" />
<div id="payWayAddDiv">
	<form id="payWayAddForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">支付方式名称:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="payWay_payWayName" name="payWay.payWayName" style="width:200px" />

			</span>

		</div>
		<div class="operation">
			<a id="payWayAddButton" class="easyui-linkbutton">添加</a>
			<a id="payWayClearButton" class="easyui-linkbutton">重填</a>
		</div> 
	</form>
</div>
<script src="${pageContext.request.contextPath}/PayWay/js/payWay_add.js"></script> 
