<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.Evaluate" %>
<%@ page import="com.chengxusheji.po.Product" %>
<%@ page import="com.chengxusheji.po.UserInfo" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的productObj信息
    List<Product> productList = (List<Product>)request.getAttribute("productList");
    //获取所有的userObj信息
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    Evaluate evaluate = (Evaluate)request.getAttribute("evaluate");

%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
  <TITLE>修改商品评价信息</TITLE>
  <link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
  <link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
  <link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
  <link href="<%=basePath %>plugins/animate.css" rel="stylesheet"> 
</head>
<body style="margin-top:70px;"> 
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
	<div class="col-md-9 wow fadeInLeft">
	<ul class="breadcrumb">
  		<li><a href="<%=basePath %>index.jsp">首页</a></li>
  		<li class="active">商品评价信息修改</li>
	</ul>
		<div class="row"> 
      	<form class="form-horizontal" name="evaluateEditForm" id="evaluateEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="evaluate_evaluateId_edit" class="col-md-3 text-right">评价编号:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="evaluate_evaluateId_edit" name="evaluate.evaluateId" class="form-control" placeholder="请输入评价编号" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="evaluate_productObj_productId_edit" class="col-md-3 text-right">商品名称:</label>
		  	 <div class="col-md-9">
			    <select id="evaluate_productObj_productId_edit" name="evaluate.productObj.productId" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="evaluate_userObj_user_name_edit" class="col-md-3 text-right">用户名:</label>
		  	 <div class="col-md-9">
			    <select id="evaluate_userObj_user_name_edit" name="evaluate.userObj.user_name" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="evaluate_content_edit" class="col-md-3 text-right">评价内容:</label>
		  	 <div class="col-md-9">
			    <textarea id="evaluate_content_edit" name="evaluate.content" rows="8" class="form-control" placeholder="请输入评价内容"></textarea>
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="evaluate_evaluateTime_edit" class="col-md-3 text-right">评价时间:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="evaluate_evaluateTime_edit" name="evaluate.evaluateTime" class="form-control" placeholder="请输入评价时间">
			 </div>
		  </div>
			  <div class="form-group">
			  	<span class="col-md-3""></span>
			  	<span onclick="ajaxEvaluateModify();" class="btn btn-primary bottom5 top5">修改</span>
			  </div>
		</form> 
	    <style>#evaluateEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
   </div>
</div>


<jsp:include page="../footer.jsp"></jsp:include>
<script src="<%=basePath %>plugins/jquery.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap.js"></script>
<script src="<%=basePath %>plugins/wow.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap-datetimepicker.min.js"></script>
<script src="<%=basePath %>plugins/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jsdate.js"></script>
<script>
var basePath = "<%=basePath%>";
/*弹出修改商品评价界面并初始化数据*/
function evaluateEdit(evaluateId) {
	$.ajax({
		url :  basePath + "Evaluate/" + evaluateId + "/update",
		type : "get",
		dataType: "json",
		success : function (evaluate, response, status) {
			if (evaluate) {
				$("#evaluate_evaluateId_edit").val(evaluate.evaluateId);
				$.ajax({
					url: basePath + "Product/listAll",
					type: "get",
					success: function(products,response,status) { 
						$("#evaluate_productObj_productId_edit").empty();
						var html="";
		        		$(products).each(function(i,product){
		        			html += "<option value='" + product.productId + "'>" + product.productName + "</option>";
		        		});
		        		$("#evaluate_productObj_productId_edit").html(html);
		        		$("#evaluate_productObj_productId_edit").val(evaluate.productObjPri);
					}
				});
				$.ajax({
					url: basePath + "UserInfo/listAll",
					type: "get",
					success: function(userInfos,response,status) { 
						$("#evaluate_userObj_user_name_edit").empty();
						var html="";
		        		$(userInfos).each(function(i,userInfo){
		        			html += "<option value='" + userInfo.user_name + "'>" + userInfo.name + "</option>";
		        		});
		        		$("#evaluate_userObj_user_name_edit").html(html);
		        		$("#evaluate_userObj_user_name_edit").val(evaluate.userObjPri);
					}
				});
				$("#evaluate_content_edit").val(evaluate.content);
				$("#evaluate_evaluateTime_edit").val(evaluate.evaluateTime);
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*ajax方式提交商品评价信息表单给服务器端修改*/
function ajaxEvaluateModify() {
	$.ajax({
		url :  basePath + "Evaluate/" + $("#evaluate_evaluateId_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#evaluateEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                location.reload(true);
                $("#evaluateQueryForm").submit();
            }else{
                alert(obj.message);
            } 
		},
		processData: false,
		contentType: false,
	});
}

$(function(){
        /*小屏幕导航点击关闭菜单*/
        $('.navbar-collapse > a').click(function(){
            $('.navbar-collapse').collapse('hide');
        });
        new WOW().init();
    evaluateEdit("<%=request.getParameter("evaluateId")%>");
 })
 </script> 
</body>
</html>

