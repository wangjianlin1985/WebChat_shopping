package com.chengxusheji.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.chengxusheji.utils.ExportExcelUtil;
import com.chengxusheji.utils.UserException;
import com.chengxusheji.service.EvaluateService;
import com.chengxusheji.po.Evaluate;
import com.chengxusheji.service.ProductService;
import com.chengxusheji.po.Product;
import com.chengxusheji.service.UserInfoService;
import com.chengxusheji.po.UserInfo;

//Evaluate管理控制层
@Controller
@RequestMapping("/Evaluate")
public class EvaluateController extends BaseController {

    /*业务层对象*/
    @Resource EvaluateService evaluateService;

    @Resource ProductService productService;
    @Resource UserInfoService userInfoService;
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
	/*跳转到添加Evaluate视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Evaluate());
		/*查询所有的Product信息*/
		List<Product> productList = productService.queryAllProduct();
		request.setAttribute("productList", productList);
		/*查询所有的UserInfo信息*/
		List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
		request.setAttribute("userInfoList", userInfoList);
		return "Evaluate_add";
	}

	/*客户端ajax方式提交添加商品评价信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Evaluate evaluate, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        evaluateService.addEvaluate(evaluate);
        message = "商品评价添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询商品评价信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("productObj") Product productObj,@ModelAttribute("userObj") UserInfo userObj,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if(rows != 0)evaluateService.setRows(rows);
		List<Evaluate> evaluateList = evaluateService.queryEvaluate(productObj, userObj, page);
	    /*计算总的页数和总的记录数*/
	    evaluateService.queryTotalPageAndRecordNumber(productObj, userObj);
	    /*获取到总的页码数目*/
	    int totalPage = evaluateService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = evaluateService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Evaluate evaluate:evaluateList) {
			JSONObject jsonEvaluate = evaluate.getJsonObject();
			jsonArray.put(jsonEvaluate);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询商品评价信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Evaluate> evaluateList = evaluateService.queryAllEvaluate();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Evaluate evaluate:evaluateList) {
			JSONObject jsonEvaluate = new JSONObject();
			jsonEvaluate.accumulate("evaluateId", evaluate.getEvaluateId());
			jsonArray.put(jsonEvaluate);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询商品评价信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("productObj") Product productObj,@ModelAttribute("userObj") UserInfo userObj,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		List<Evaluate> evaluateList = evaluateService.queryEvaluate(productObj, userObj, currentPage);
	    /*计算总的页数和总的记录数*/
	    evaluateService.queryTotalPageAndRecordNumber(productObj, userObj);
	    /*获取到总的页码数目*/
	    int totalPage = evaluateService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = evaluateService.getRecordNumber();
	    request.setAttribute("evaluateList",  evaluateList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("productObj", productObj);
	    request.setAttribute("userObj", userObj);
	    List<Product> productList = productService.queryAllProduct();
	    request.setAttribute("productList", productList);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "Evaluate/evaluate_frontquery_result"; 
	}

     /*前台查询Evaluate信息*/
	@RequestMapping(value="/{evaluateId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer evaluateId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键evaluateId获取Evaluate对象*/
        Evaluate evaluate = evaluateService.getEvaluate(evaluateId);

        List<Product> productList = productService.queryAllProduct();
        request.setAttribute("productList", productList);
        List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
        request.setAttribute("userInfoList", userInfoList);
        request.setAttribute("evaluate",  evaluate);
        return "Evaluate/evaluate_frontshow";
	}

	/*ajax方式显示商品评价修改jsp视图页*/
	@RequestMapping(value="/{evaluateId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer evaluateId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键evaluateId获取Evaluate对象*/
        Evaluate evaluate = evaluateService.getEvaluate(evaluateId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonEvaluate = evaluate.getJsonObject();
		out.println(jsonEvaluate.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新商品评价信息*/
	@RequestMapping(value = "/{evaluateId}/update", method = RequestMethod.POST)
	public void update(@Validated Evaluate evaluate, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			evaluateService.updateEvaluate(evaluate);
			message = "商品评价更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "商品评价更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除商品评价信息*/
	@RequestMapping(value="/{evaluateId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer evaluateId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  evaluateService.deleteEvaluate(evaluateId);
	            request.setAttribute("message", "商品评价删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "商品评价删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条商品评价记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String evaluateIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = evaluateService.deleteEvaluates(evaluateIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出商品评价信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("productObj") Product productObj,@ModelAttribute("userObj") UserInfo userObj, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        List<Evaluate> evaluateList = evaluateService.queryEvaluate(productObj,userObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Evaluate信息记录"; 
        String[] headers = { "评价编号","商品名称","用户名","评价内容","评价时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<evaluateList.size();i++) {
        	Evaluate evaluate = evaluateList.get(i); 
        	dataset.add(new String[]{evaluate.getEvaluateId() + "",evaluate.getProductObj().getProductName(),evaluate.getUserObj().getName(),evaluate.getContent(),evaluate.getEvaluateTime()});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		OutputStream out = null;//创建一个输出流对象 
		try { 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Evaluate.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
			String rootPath = request.getSession().getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,_title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
    }
}
