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
import com.chengxusheji.po.PayWay;
import com.chengxusheji.service.PayWayService;
import com.client.service.AuthService;
import com.client.utils.JsonResult;
import com.client.utils.JsonResultBuilder;
import com.client.utils.ReturnCode;

@RestController
@RequestMapping("/api/payWay") 
public class ApiPayWayController {
	@Resource PayWayService payWayService;
	@Resource AuthService authService;

	@InitBinder("payWay")
	public void initBinderPayWay(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("payWay.");
	}

	/*客户端ajax方式添加支付方式信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(@Validated PayWay payWay, BindingResult br, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		if (br.hasErrors()) //验证输入参数
			return JsonResultBuilder.error(ReturnCode.INPUT_PARAM_ERROR);
        payWayService.addPayWay(payWay); //添加到数据库
        return JsonResultBuilder.ok();
	}

	/*客户端ajax更新支付方式信息*/
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public JsonResult update(@Validated PayWay payWay, BindingResult br, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		if (br.hasErrors())  //验证输入参数
			return JsonResultBuilder.error(ReturnCode.INPUT_PARAM_ERROR); 
        payWayService.updatePayWay(payWay);  //更新记录到数据库
        return JsonResultBuilder.ok(payWayService.getPayWay(payWay.getPayWayId()));
	}

	/*ajax方式显示获取支付方式详细信息*/
	@RequestMapping(value="/get/{payWayId}",method=RequestMethod.POST)
	public JsonResult getPayWay(@PathVariable int payWayId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键payWayId获取PayWay对象*/
        PayWay payWay = payWayService.getPayWay(payWayId); 
        return JsonResultBuilder.ok(payWay);
	}

	/*ajax方式删除支付方式记录*/
	@RequestMapping(value="/delete/{payWayId}",method=RequestMethod.POST)
	public JsonResult deletePayWay(@PathVariable int payWayId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		try {
			payWayService.deletePayWay(payWayId);
			return JsonResultBuilder.ok();
		} catch (Exception ex) {
			return JsonResultBuilder.error(ReturnCode.FOREIGN_KEY_CONSTRAINT_ERROR);
		}
	}

	//客户端查询支付方式信息
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public JsonResult list(Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if(rows != 0)payWayService.setRows(rows);
		List<PayWay> payWayList = payWayService.queryPayWay(page);
	    /*计算总的页数和总的记录数*/
	    payWayService.queryTotalPageAndRecordNumber();
	    /*获取到总的页码数目*/
	    int totalPage = payWayService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = payWayService.getRecordNumber();
	    HashMap<String, Object> resultMap = new HashMap<String, Object>();
	    resultMap.put("totalPage", totalPage);
	    resultMap.put("list", payWayList);
	    return JsonResultBuilder.ok(resultMap);
	}

	//客户端ajax获取所有支付方式
	@RequestMapping(value="/listAll",method=RequestMethod.POST)
	public JsonResult listAll() throws Exception{
		List<PayWay> payWayList = payWayService.queryAllPayWay(); 
		return JsonResultBuilder.ok(payWayList);
	}
}

