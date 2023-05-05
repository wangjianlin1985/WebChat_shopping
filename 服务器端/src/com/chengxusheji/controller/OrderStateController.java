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
import com.chengxusheji.service.OrderStateService;
import com.chengxusheji.po.OrderState;

//OrderState管理控制层
@Controller
@RequestMapping("/OrderState")
public class OrderStateController extends BaseController {

    /*业务层对象*/
    @Resource OrderStateService orderStateService;

	@InitBinder("orderState")
	public void initBinderOrderState(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("orderState.");
	}
	/*跳转到添加OrderState视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new OrderState());
		return "OrderState_add";
	}

	/*客户端ajax方式提交添加订单状态信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated OrderState orderState, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        orderStateService.addOrderState(orderState);
        message = "订单状态添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询订单状态信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if(rows != 0)orderStateService.setRows(rows);
		List<OrderState> orderStateList = orderStateService.queryOrderState(page);
	    /*计算总的页数和总的记录数*/
	    orderStateService.queryTotalPageAndRecordNumber();
	    /*获取到总的页码数目*/
	    int totalPage = orderStateService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = orderStateService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(OrderState orderState:orderStateList) {
			JSONObject jsonOrderState = orderState.getJsonObject();
			jsonArray.put(jsonOrderState);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询订单状态信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<OrderState> orderStateList = orderStateService.queryAllOrderState();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(OrderState orderState:orderStateList) {
			JSONObject jsonOrderState = new JSONObject();
			jsonOrderState.accumulate("stateId", orderState.getStateId());
			jsonOrderState.accumulate("stateName", orderState.getStateName());
			jsonArray.put(jsonOrderState);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询订单状态信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		List<OrderState> orderStateList = orderStateService.queryOrderState(currentPage);
	    /*计算总的页数和总的记录数*/
	    orderStateService.queryTotalPageAndRecordNumber();
	    /*获取到总的页码数目*/
	    int totalPage = orderStateService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = orderStateService.getRecordNumber();
	    request.setAttribute("orderStateList",  orderStateList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
		return "OrderState/orderState_frontquery_result"; 
	}

     /*前台查询OrderState信息*/
	@RequestMapping(value="/{stateId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer stateId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键stateId获取OrderState对象*/
        OrderState orderState = orderStateService.getOrderState(stateId);

        request.setAttribute("orderState",  orderState);
        return "OrderState/orderState_frontshow";
	}

	/*ajax方式显示订单状态修改jsp视图页*/
	@RequestMapping(value="/{stateId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer stateId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键stateId获取OrderState对象*/
        OrderState orderState = orderStateService.getOrderState(stateId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonOrderState = orderState.getJsonObject();
		out.println(jsonOrderState.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新订单状态信息*/
	@RequestMapping(value = "/{stateId}/update", method = RequestMethod.POST)
	public void update(@Validated OrderState orderState, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			orderStateService.updateOrderState(orderState);
			message = "订单状态更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "订单状态更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除订单状态信息*/
	@RequestMapping(value="/{stateId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer stateId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  orderStateService.deleteOrderState(stateId);
	            request.setAttribute("message", "订单状态删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "订单状态删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条订单状态记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String stateIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = orderStateService.deleteOrderStates(stateIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出订单状态信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel( Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        List<OrderState> orderStateList = orderStateService.queryOrderState();
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "OrderState信息记录"; 
        String[] headers = { "状态编号","订单状态名称"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<orderStateList.size();i++) {
        	OrderState orderState = orderStateList.get(i); 
        	dataset.add(new String[]{orderState.getStateId() + "",orderState.getStateName()});
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
			response.setHeader("Content-disposition","attachment; filename="+"OrderState.xls");//filename是下载的xls的名，建议最好用英文 
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
