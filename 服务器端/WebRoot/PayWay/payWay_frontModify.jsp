<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.PayWay" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    PayWay payWay = (PayWay)request.getAttribute("payWay");

%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
  <TITLE>修改支付方式信息</TITLE>
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
  		<li class="active">支付方式信息修改</li>
	</ul>
		<div class="row"> 
      	<form class="form-horizontal" name="payWayEditForm" id="payWayEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="payWay_payWayId_edit" class="col-md-3 text-right">支付方式id:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="payWay_payWayId_edit" name="payWay.payWayId" class="form-control" placeholder="请输入支付方式id" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="payWay_payWayName_edit" class="col-md-3 text-right">支付方式名称:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="payWay_payWayName_edit" name="payWay.payWayName" class="form-control" placeholder="请输入支付方式名称">
			 </div>
		  </div>
			  <div class="form-group">
			  	<span class="col-md-3""></span>
			  	<span onclick="ajaxPayWayModify();" class="btn btn-primary bottom5 top5">修改</span>
			  </div>
		</form> 
	    <style>#payWayEditForm .form-group {margin-bottom:5px;}  </style>
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
/*弹出修改支付方式界面并初始化数据*/
function payWayEdit(payWayId) {
	$.ajax({
		url :  basePath + "PayWay/" + payWayId + "/update",
		type : "get",
		dataType: "json",
		success : function (payWay, response, status) {
			if (payWay) {
				$("#payWay_payWayId_edit").val(payWay.payWayId);
				$("#payWay_payWayName_edit").val(payWay.payWayName);
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*ajax方式提交支付方式信息表单给服务器端修改*/
function ajaxPayWayModify() {
	$.ajax({
		url :  basePath + "PayWay/" + $("#payWay_payWayId_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#payWayEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                location.reload(true);
                location.href= basePath + "PayWay/frontlist";
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
    payWayEdit("<%=request.getParameter("payWayId")%>");
 })
 </script> 
</body>
</html>

