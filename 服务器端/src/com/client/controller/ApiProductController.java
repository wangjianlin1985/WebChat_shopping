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
import com.chengxusheji.po.Product;
import com.chengxusheji.po.ProductClass;
import com.chengxusheji.service.ProductService;
import com.client.service.AuthService;
import com.client.utils.JsonResult;
import com.client.utils.JsonResultBuilder;
import com.client.utils.ReturnCode;

@RestController
@RequestMapping("/api/product") 
public class ApiProductController {
	@Resource ProductService productService;
	@Resource AuthService authService;

	@InitBinder("productClassObj")
	public void initBinderproductClassObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("productClassObj.");
	}
	@InitBinder("product")
	public void initBinderProduct(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("product.");
	}

	/*客户端ajax方式添加商品信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(@Validated Product product, BindingResult br, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		if (br.hasErrors()) //验证输入参数
			return JsonResultBuilder.error(ReturnCode.INPUT_PARAM_ERROR);
        productService.addProduct(product); //添加到数据库
        return JsonResultBuilder.ok();
	}

	/*客户端ajax更新商品信息*/
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public JsonResult update(@Validated Product product, BindingResult br, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		if (br.hasErrors())  //验证输入参数
			return JsonResultBuilder.error(ReturnCode.INPUT_PARAM_ERROR); 
        productService.updateProduct(product);  //更新记录到数据库
        return JsonResultBuilder.ok(productService.getProduct(product.getProductId()));
	}

	/*ajax方式显示获取商品详细信息*/
	@RequestMapping(value="/get/{productId}",method=RequestMethod.POST)
	public JsonResult getProduct(@PathVariable int productId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键productId获取Product对象*/
        Product product = productService.getProduct(productId); 
        return JsonResultBuilder.ok(product);
	}

	/*ajax方式删除商品记录*/
	@RequestMapping(value="/delete/{productId}",method=RequestMethod.POST)
	public JsonResult deleteProduct(@PathVariable int productId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		try {
			productService.deleteProduct(productId);
			return JsonResultBuilder.ok();
		} catch (Exception ex) {
			return JsonResultBuilder.error(ReturnCode.FOREIGN_KEY_CONSTRAINT_ERROR);
		}
	}

	//客户端查询商品信息
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public JsonResult list(@ModelAttribute("productClassObj") ProductClass productClassObj,String productName,String recommendFlag,String addTime,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (productName == null) productName = "";
		if (recommendFlag == null) recommendFlag = "";
		if (addTime == null) addTime = "";
		if(rows != 0)productService.setRows(rows);
		List<Product> productList = productService.queryProduct(productClassObj, productName, recommendFlag, addTime, page);
	    /*计算总的页数和总的记录数*/
	    productService.queryTotalPageAndRecordNumber(productClassObj, productName, recommendFlag, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = productService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = productService.getRecordNumber();
	    HashMap<String, Object> resultMap = new HashMap<String, Object>();
	    resultMap.put("totalPage", totalPage);
	    resultMap.put("list", productList);
	    return JsonResultBuilder.ok(resultMap);
	}

	//客户端ajax获取所有商品
	@RequestMapping(value="/listAll",method=RequestMethod.POST)
	public JsonResult listAll() throws Exception{
		List<Product> productList = productService.queryAllProduct(); 
		return JsonResultBuilder.ok(productList);
	}
}

