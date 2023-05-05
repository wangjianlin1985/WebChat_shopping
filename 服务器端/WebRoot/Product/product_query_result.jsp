<%@ page language="java"  contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/> 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/product.css" /> 

<div id="product_manage"></div>
<div id="product_manage_tool" style="padding:5px;">
	<div style="margin-bottom:5px;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit-new" plain="true" onclick="product_manage_tool.edit();">修改</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-delete-new" plain="true" onclick="product_manage_tool.remove();">删除</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true"  onclick="product_manage_tool.reload();">刷新</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="product_manage_tool.redo();">取消选择</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-export" plain="true" onclick="product_manage_tool.exportExcel();">导出到excel</a>
	</div>
	<div style="padding:0 0 0 7px;color:#333;">
		<form id="productQueryForm" method="post">
			商品类别：<input class="textbox" type="text" id="productClassObj_classId_query" name="productClassObj.classId" style="width: auto"/>
			商品名称：<input type="text" class="textbox" id="productName" name="productName" style="width:110px" />
			是否推荐：<input type="text" class="textbox" id="recommendFlag" name="recommendFlag" style="width:110px" />
			上架时间：<input type="text" id="addTime" name="addTime" class="easyui-datebox" editable="false" style="width:100px">
			<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="product_manage_tool.search();">查询</a>
		</form>	
	</div>
</div>

<div id="productEditDiv">
	<form id="productEditForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">商品id:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="product_productId_edit" name="product.productId" style="width:200px" />
			</span>
		</div>
		<div>
			<span class="label">商品类别:</span>
			<span class="inputControl">
				<input class="textbox"  id="product_productClassObj_classId_edit" name="product.productClassObj.classId" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">商品名称:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="product_productName_edit" name="product.productName" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">商品主图:</span>
			<span class="inputControl">
				<img id="product_mainPhotoImg" width="200px" border="0px"/><br/>
    			<input type="hidden" id="product_mainPhoto" name="product.mainPhoto"/>
				<input id="mainPhotoFile" name="mainPhotoFile" type="file" size="50" />
			</span>
		</div>
		<div>
			<span class="label">商品价格:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="product_price_edit" name="product.price" style="width:80px" />

			</span>

		</div>
		<div>
			<span class="label">商量数量:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="product_productCount_edit" name="product.productCount" style="width:80px" />

			</span>

		</div>
		<div>
			<span class="label">商品描述:</span>
			<span class="inputControl">
				<textarea id="product_productDesc_edit" name="product.productDesc" rows="8" cols="60"></textarea>

			</span>

		</div>
		<div>
			<span class="label">是否推荐:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="product_recommendFlag_edit" name="product.recommendFlag" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">人气值:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="product_hotNum_edit" name="product.hotNum" style="width:80px" />

			</span>

		</div>
		<div>
			<span class="label">上架时间:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="product_addTime_edit" name="product.addTime" />

			</span>

		</div>
	</form>
</div>
<script type="text/javascript" src="Product/js/product_manage.js"></script> 
