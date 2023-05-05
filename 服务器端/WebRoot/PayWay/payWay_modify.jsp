<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/payWay.css" />
<div id="payWay_editDiv">
	<form id="payWayEditForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">支付方式id:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="payWay_payWayId_edit" name="payWay.payWayId" value="<%=request.getParameter("payWayId") %>" style="width:200px" />
			</span>
		</div>

		<div>
			<span class="label">支付方式名称:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="payWay_payWayName_edit" name="payWay.payWayName" style="width:200px" />

			</span>

		</div>
		<div class="operation">
			<a id="payWayModifyButton" class="easyui-linkbutton">更新</a> 
		</div>
	</form>
</div>
<script src="${pageContext.request.contextPath}/PayWay/js/payWay_modify.js"></script> 
