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
import com.chengxusheji.po.ProductClass;
import com.chengxusheji.service.ProductClassService;
import com.client.service.AuthService;
import com.client.utils.JsonResult;
import com.client.utils.JsonResultBuilder;
import com.client.utils.ReturnCode;

@RestController
@RequestMapping("/api/productClass") 
public class ApiProductClassController {
	@Resource ProductClassService productClassService;
	@Resource AuthService authService;

	@InitBinder("productClass")
	public void initBinderProductClass(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("productClass.");
	}

	/*客户端ajax方式添加商品类别信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(@Validated ProductClass productClass, BindingResult br, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		if (br.hasErrors()) //验证输入参数
			return JsonResultBuilder.error(ReturnCode.INPUT_PARAM_ERROR);
        productClassService.addProductClass(productClass); //添加到数据库
        return JsonResultBuilder.ok();
	}

	/*客户端ajax更新商品类别信息*/
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public JsonResult update(@Validated ProductClass productClass, BindingResult br, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		if (br.hasErrors())  //验证输入参数
			return JsonResultBuilder.error(ReturnCode.INPUT_PARAM_ERROR); 
        productClassService.updateProductClass(productClass);  //更新记录到数据库
        return JsonResultBuilder.ok(productClassService.getProductClass(productClass.getClassId()));
	}

	/*ajax方式显示获取商品类别详细信息*/
	@RequestMapping(value="/get/{classId}",method=RequestMethod.POST)
	public JsonResult getProductClass(@PathVariable int classId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键classId获取ProductClass对象*/
        ProductClass productClass = productClassService.getProductClass(classId); 
        return JsonResultBuilder.ok(productClass);
	}

	/*ajax方式删除商品类别记录*/
	@RequestMapping(value="/delete/{classId}",method=RequestMethod.POST)
	public JsonResult deleteProductClass(@PathVariable int classId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		try {
			productClassService.deleteProductClass(classId);
			return JsonResultBuilder.ok();
		} catch (Exception ex) {
			return JsonResultBuilder.error(ReturnCode.FOREIGN_KEY_CONSTRAINT_ERROR);
		}
	}

	//客户端查询商品类别信息
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public JsonResult list(Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if(rows != 0)productClassService.setRows(rows);
		List<ProductClass> productClassList = productClassService.queryProductClass(page);
	    /*计算总的页数和总的记录数*/
	    productClassService.queryTotalPageAndRecordNumber();
	    /*获取到总的页码数目*/
	    int totalPage = productClassService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = productClassService.getRecordNumber();
	    HashMap<String, Object> resultMap = new HashMap<String, Object>();
	    resultMap.put("totalPage", totalPage);
	    resultMap.put("list", productClassList);
	    return JsonResultBuilder.ok(resultMap);
	}

	//客户端ajax获取所有商品类别
	@RequestMapping(value="/listAll",method=RequestMethod.POST)
	public JsonResult listAll() throws Exception{
		List<ProductClass> productClassList = productClassService.queryAllProductClass(); 
		return JsonResultBuilder.ok(productClassList);
	}
}

