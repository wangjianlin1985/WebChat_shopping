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
import com.chengxusheji.service.PayWayService;
import com.chengxusheji.po.PayWay;

//PayWay管理控制层
@Controller
@RequestMapping("/PayWay")
public class PayWayController extends BaseController {

    /*业务层对象*/
    @Resource PayWayService payWayService;

	@InitBinder("payWay")
	public void initBinderPayWay(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("payWay.");
	}
	/*跳转到添加PayWay视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new PayWay());
		return "PayWay_add";
	}

	/*客户端ajax方式提交添加支付方式信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated PayWay payWay, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        payWayService.addPayWay(payWay);
        message = "支付方式添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询支付方式信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if(rows != 0)payWayService.setRows(rows);
		List<PayWay> payWayList = payWayService.queryPayWay(page);
	    /*计算总的页数和总的记录数*/
	    payWayService.queryTotalPageAndRecordNumber();
	    /*获取到总的页码数目*/
	    int totalPage = payWayService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = payWayService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(PayWay payWay:payWayList) {
			JSONObject jsonPayWay = payWay.getJsonObject();
			jsonArray.put(jsonPayWay);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询支付方式信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<PayWay> payWayList = payWayService.queryAllPayWay();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(PayWay payWay:payWayList) {
			JSONObject jsonPayWay = new JSONObject();
			jsonPayWay.accumulate("payWayId", payWay.getPayWayId());
			jsonPayWay.accumulate("payWayName", payWay.getPayWayName());
			jsonArray.put(jsonPayWay);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询支付方式信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		List<PayWay> payWayList = payWayService.queryPayWay(currentPage);
	    /*计算总的页数和总的记录数*/
	    payWayService.queryTotalPageAndRecordNumber();
	    /*获取到总的页码数目*/
	    int totalPage = payWayService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = payWayService.getRecordNumber();
	    request.setAttribute("payWayList",  payWayList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
		return "PayWay/payWay_frontquery_result"; 
	}

     /*前台查询PayWay信息*/
	@RequestMapping(value="/{payWayId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer payWayId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键payWayId获取PayWay对象*/
        PayWay payWay = payWayService.getPayWay(payWayId);

        request.setAttribute("payWay",  payWay);
        return "PayWay/payWay_frontshow";
	}

	/*ajax方式显示支付方式修改jsp视图页*/
	@RequestMapping(value="/{payWayId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer payWayId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键payWayId获取PayWay对象*/
        PayWay payWay = payWayService.getPayWay(payWayId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonPayWay = payWay.getJsonObject();
		out.println(jsonPayWay.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新支付方式信息*/
	@RequestMapping(value = "/{payWayId}/update", method = RequestMethod.POST)
	public void update(@Validated PayWay payWay, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			payWayService.updatePayWay(payWay);
			message = "支付方式更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "支付方式更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除支付方式信息*/
	@RequestMapping(value="/{payWayId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer payWayId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  payWayService.deletePayWay(payWayId);
	            request.setAttribute("message", "支付方式删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "支付方式删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条支付方式记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String payWayIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = payWayService.deletePayWays(payWayIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出支付方式信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel( Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        List<PayWay> payWayList = payWayService.queryPayWay();
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "PayWay信息记录"; 
        String[] headers = { "支付方式id","支付方式名称"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<payWayList.size();i++) {
        	PayWay payWay = payWayList.get(i); 
        	dataset.add(new String[]{payWay.getPayWayId() + "",payWay.getPayWayName()});
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
			response.setHeader("Content-disposition","attachment; filename="+"PayWay.xls");//filename是下载的xls的名，建议最好用英文 
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
