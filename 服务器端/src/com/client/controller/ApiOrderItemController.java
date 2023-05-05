package com.client.controller;

import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.chengxusheji.po.OrderItem;
import com.chengxusheji.po.OrderInfo;
import com.chengxusheji.po.Product;
import com.chengxusheji.service.OrderItemService;
import com.client.service.AuthService;
import com.client.utils.JsonResult;
import com.client.utils.JsonResultBuilder;
import com.client.utils.ReturnCode;

@RestController
@RequestMapping("/api/orderItem") 
public class ApiOrderItemController {
	@Resource OrderItemService orderItemService;
	@Resource AuthService authService;

	@InitBinder("orderObj")
	public void initBinderorderObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("orderObj.");
	}
	@InitBinder("productObj")
	public void initBinderproductObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("productObj.");
	}
	@InitBinder("orderItem")
	public void initBinderOrderItem(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("orderItem.");
	}

	/*客户端ajax方式添加订单条目信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(@Validated OrderItem orderItem, BindingResult br, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		if (br.hasErrors()) //验证输入参数
			return JsonResultBuilder.error(ReturnCode.INPUT_PARAM_ERROR);
        orderItemService.addOrderItem(orderItem); //添加到数据库
        return JsonResultBuilder.ok();
	}

	/*客户端ajax更新订单条目信息*/
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public JsonResult update(@Validated OrderItem orderItem, BindingResult br, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		if (br.hasErrors())  //验证输入参数
			return JsonResultBuilder.error(ReturnCode.INPUT_PARAM_ERROR); 
        orderItemService.updateOrderItem(orderItem);  //更新记录到数据库
        return JsonResultBuilder.ok(orderItemService.getOrderItem(orderItem.getItemId()));
	}

	/*ajax方式显示获取订单条目详细信息*/
	@RequestMapping(value="/get/{itemId}",method=RequestMethod.POST)
	public JsonResult getOrderItem(@PathVariable int itemId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键itemId获取OrderItem对象*/
        OrderItem orderItem = orderItemService.getOrderItem(itemId); 
        return JsonResultBuilder.ok(orderItem);
	}

	/*ajax方式删除订单条目记录*/
	@RequestMapping(value="/delete/{itemId}",method=RequestMethod.POST)
	public JsonResult deleteOrderItem(@PathVariable int itemId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		try {
			orderItemService.deleteOrderItem(itemId);
			return JsonResultBuilder.ok();
		} catch (Exception ex) {
			return JsonResultBuilder.error(ReturnCode.FOREIGN_KEY_CONSTRAINT_ERROR);
		}
	}

	//客户端查询订单条目信息
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public JsonResult list(@ModelAttribute("orderObj") OrderInfo orderObj,@ModelAttribute("productObj") Product productObj,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if(rows != 0)orderItemService.setRows(rows);
		List<OrderItem> orderItemList = orderItemService.queryOrderItem(orderObj, productObj, page);
	    /*计算总的页数和总的记录数*/
	    orderItemService.queryTotalPageAndRecordNumber(orderObj, productObj);
	    /*获取到总的页码数目*/
	    int totalPage = orderItemService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = orderItemService.getRecordNumber();
	    HashMap<String, Object> resultMap = new HashMap<String, Object>();
	    resultMap.put("totalPage", totalPage);
	    resultMap.put("list", orderItemList);
	    return JsonResultBuilder.ok(resultMap);
	}

	//客户端ajax获取所有订单条目
	@RequestMapping(value="/listAll",method=RequestMethod.POST)
	public JsonResult listAll() throws Exception{
		List<OrderItem> orderItemList = orderItemService.queryAllOrderItem(); 
		return JsonResultBuilder.ok(orderItemList);
	}
}

