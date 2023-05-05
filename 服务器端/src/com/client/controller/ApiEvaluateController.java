package com.client.controller;

import java.text.SimpleDateFormat;
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
import com.chengxusheji.po.Evaluate;
import com.chengxusheji.po.OrderItem;
import com.chengxusheji.po.Product;
import com.chengxusheji.po.UserInfo;
import com.chengxusheji.service.EvaluateService;
import com.chengxusheji.service.OrderItemService;
import com.client.service.AuthService;
import com.client.utils.JsonResult;
import com.client.utils.JsonResultBuilder;
import com.client.utils.ReturnCode;

@RestController
@RequestMapping("/api/evaluate") 
public class ApiEvaluateController {
	@Resource EvaluateService evaluateService;
	@Resource AuthService authService;
	@Resource OrderItemService orderItemService;

	@InitBinder("productObj")
	public void initBinderproductObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("productObj.");
	}
	@InitBinder("userObj")
	public void initBinderuserObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("userObj.");
	}
	@InitBinder("evaluate")
	public void initBinderEvaluate(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("evaluate.");
	}

	/*客户端ajax方式添加商品评价信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(@Validated Evaluate evaluate, BindingResult br, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		if (br.hasErrors()) //验证输入参数
			return JsonResultBuilder.error(ReturnCode.INPUT_PARAM_ERROR);
        evaluateService.addEvaluate(evaluate); //添加到数据库
        return JsonResultBuilder.ok();
	}
	
	
	
	/*客户端ajax方式添加商品评价信息*/
	@RequestMapping(value = "/userAdd", method = RequestMethod.POST)
	public JsonResult userAdd(Evaluate evaluate, int itemId, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		
		OrderItem item =orderItemService.getOrderItem(itemId);
		if(item.getOrderObj().getOrderState().getStateId() != 5) 
			return JsonResultBuilder.error(ReturnCode.EVALUATE_STATE_ERROR);
		
		UserInfo userObj = new UserInfo();
		userObj.setUser_name(userName);
		evaluate.setUserObj(userObj);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		evaluate.setEvaluateTime(sdf.format(new java.util.Date()));
		
        evaluateService.addEvaluate(evaluate); //添加到数据库
        return JsonResultBuilder.ok();
	}
	
	
	

	/*客户端ajax更新商品评价信息*/
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public JsonResult update(@Validated Evaluate evaluate, BindingResult br, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		if (br.hasErrors())  //验证输入参数
			return JsonResultBuilder.error(ReturnCode.INPUT_PARAM_ERROR); 
        evaluateService.updateEvaluate(evaluate);  //更新记录到数据库
        return JsonResultBuilder.ok(evaluateService.getEvaluate(evaluate.getEvaluateId()));
	}

	/*ajax方式显示获取商品评价详细信息*/
	@RequestMapping(value="/get/{evaluateId}",method=RequestMethod.POST)
	public JsonResult getEvaluate(@PathVariable int evaluateId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键evaluateId获取Evaluate对象*/
        Evaluate evaluate = evaluateService.getEvaluate(evaluateId); 
        return JsonResultBuilder.ok(evaluate);
	}

	/*ajax方式删除商品评价记录*/
	@RequestMapping(value="/delete/{evaluateId}",method=RequestMethod.POST)
	public JsonResult deleteEvaluate(@PathVariable int evaluateId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		try {
			evaluateService.deleteEvaluate(evaluateId);
			return JsonResultBuilder.ok();
		} catch (Exception ex) {
			return JsonResultBuilder.error(ReturnCode.FOREIGN_KEY_CONSTRAINT_ERROR);
		}
	}

	//客户端查询商品评价信息
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public JsonResult list(@ModelAttribute("productObj") Product productObj,@ModelAttribute("userObj") UserInfo userObj,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		userObj = new UserInfo();
		userObj.setUser_name(userName);
				
		if (page==null || page == 0) page = 1;
		if(rows != 0)evaluateService.setRows(rows);
		List<Evaluate> evaluateList = evaluateService.queryEvaluate(productObj, userObj, page);
	    /*计算总的页数和总的记录数*/
	    evaluateService.queryTotalPageAndRecordNumber(productObj, userObj);
	    /*获取到总的页码数目*/
	    int totalPage = evaluateService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = evaluateService.getRecordNumber();
	    HashMap<String, Object> resultMap = new HashMap<String, Object>();
	    resultMap.put("totalPage", totalPage);
	    resultMap.put("list", evaluateList);
	    return JsonResultBuilder.ok(resultMap);
	}
	
	
	//客户端查询商品评价信息
		@RequestMapping(value="/productList",method=RequestMethod.POST)
		public JsonResult productList(@ModelAttribute("productObj") Product productObj,@ModelAttribute("userObj") UserInfo userObj,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
			//通过accessToken获取到用户信息 
			String userName = authService.getUserName(request);
			if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
			 
					
			if (page==null || page == 0) page = 1;
			if(rows != 0)evaluateService.setRows(rows);
			List<Evaluate> evaluateList = evaluateService.queryEvaluate(productObj, userObj, page);
		    /*计算总的页数和总的记录数*/
		    evaluateService.queryTotalPageAndRecordNumber(productObj, userObj);
		    /*获取到总的页码数目*/
		    int totalPage = evaluateService.getTotalPage();
		    /*当前查询条件下总记录数*/
		    int recordNumber = evaluateService.getRecordNumber();
		    HashMap<String, Object> resultMap = new HashMap<String, Object>();
		    resultMap.put("totalPage", totalPage);
		    resultMap.put("list", evaluateList);
		    return JsonResultBuilder.ok(resultMap);
		}
		
		

	//客户端ajax获取所有商品评价
	@RequestMapping(value="/listAll",method=RequestMethod.POST)
	public JsonResult listAll() throws Exception{
		List<Evaluate> evaluateList = evaluateService.queryAllEvaluate(); 
		return JsonResultBuilder.ok(evaluateList);
	}
}

