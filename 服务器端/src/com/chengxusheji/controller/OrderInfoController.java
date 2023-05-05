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
import com.chengxusheji.service.OrderInfoService;
import com.chengxusheji.service.OrderItemService;
import com.chengxusheji.po.OrderInfo;
import com.chengxusheji.po.OrderItem;
import com.chengxusheji.service.OrderStateService;
import com.chengxusheji.po.OrderState;
import com.chengxusheji.service.PayWayService;
import com.chengxusheji.po.PayWay;
import com.chengxusheji.service.UserInfoService;
import com.chengxusheji.po.UserInfo;

//OrderInfo管理控制层
@Controller
@RequestMapping("/OrderInfo")
public class OrderInfoController extends BaseController {

    /*业务层对象*/
    @Resource OrderInfoService orderInfoService;
    @Resource OrderItemService orderItemService;
    @Resource OrderStateService orderStateService;
    @Resource PayWayService payWayService;
    @Resource UserInfoService userInfoService;
	@InitBinder("userObj")
	public void initBinderuserObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("userObj.");
	}
	@InitBinder("payWayObj")
	public void initBinderpayWayObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("payWayObj.");
	}
	@InitBinder("orderState")
	public void initBinderorderState(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("orderState.");
	}
	@InitBinder("orderInfo")
	public void initBinderOrderInfo(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("orderInfo.");
	}
	/*跳转到添加OrderInfo视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new OrderInfo());
		/*查询所有的OrderState信息*/
		List<OrderState> orderStateList = orderStateService.queryAllOrderState();
		request.setAttribute("orderStateList", orderStateList);
		/*查询所有的PayWay信息*/
		List<PayWay> payWayList = payWayService.queryAllPayWay();
		request.setAttribute("payWayList", payWayList);
		/*查询所有的UserInfo信息*/
		List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
		request.setAttribute("userInfoList", userInfoList);
		return "OrderInfo_add";
	}

	/*客户端ajax方式提交添加订单信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated OrderInfo orderInfo, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		if(orderInfoService.getOrderInfo(orderInfo.getOrderNo()) != null) {
			message = "订单编号已经存在！";
			writeJsonResponse(response, success, message);
			return ;
		}
        orderInfoService.addOrderInfo(orderInfo);
        message = "订单添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询订单信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(String orderNo,@ModelAttribute("userObj") UserInfo userObj,@ModelAttribute("payWayObj") PayWay payWayObj,String payAccount,@ModelAttribute("orderState") OrderState orderState,String receiveName,String orderTime,String telephone,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (orderNo == null) orderNo = "";
		if (payAccount == null) payAccount = "";
		if (receiveName == null) receiveName = "";
		if (orderTime == null) orderTime = "";
		if (telephone == null) telephone = "";
		if(rows != 0)orderInfoService.setRows(rows);
		List<OrderInfo> orderInfoList = orderInfoService.queryOrderInfo(orderNo, userObj, payWayObj, payAccount, orderState, receiveName, orderTime, telephone, page);
	    /*计算总的页数和总的记录数*/
	    orderInfoService.queryTotalPageAndRecordNumber(orderNo, userObj, payWayObj, payAccount, orderState, receiveName, orderTime, telephone);
	    /*获取到总的页码数目*/
	    int totalPage = orderInfoService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = orderInfoService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(OrderInfo orderInfo:orderInfoList) {
			JSONObject jsonOrderInfo = orderInfo.getJsonObject();
			jsonArray.put(jsonOrderInfo);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询订单信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<OrderInfo> orderInfoList = orderInfoService.queryAllOrderInfo();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(OrderInfo orderInfo:orderInfoList) {
			JSONObject jsonOrderInfo = new JSONObject();
			jsonOrderInfo.accumulate("orderNo", orderInfo.getOrderNo());
			jsonArray.put(jsonOrderInfo);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询订单信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(String orderNo,@ModelAttribute("userObj") UserInfo userObj,@ModelAttribute("payWayObj") PayWay payWayObj,String payAccount,@ModelAttribute("orderState") OrderState orderState,String receiveName,String orderTime,String telephone,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (orderNo == null) orderNo = "";
		if (payAccount == null) payAccount = "";
		if (receiveName == null) receiveName = "";
		if (orderTime == null) orderTime = "";
		if (telephone == null) telephone = "";
		List<OrderInfo> orderInfoList = orderInfoService.queryOrderInfo(orderNo, userObj, payWayObj, payAccount, orderState, receiveName, orderTime, telephone, currentPage);
	    /*计算总的页数和总的记录数*/
	    orderInfoService.queryTotalPageAndRecordNumber(orderNo, userObj, payWayObj, payAccount, orderState, receiveName, orderTime, telephone);
	    /*获取到总的页码数目*/
	    int totalPage = orderInfoService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = orderInfoService.getRecordNumber();
	    request.setAttribute("orderInfoList",  orderInfoList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("orderNo", orderNo);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("payWayObj", payWayObj);
	    request.setAttribute("payAccount", payAccount);
	    request.setAttribute("orderState", orderState);
	    request.setAttribute("receiveName", receiveName);
	    request.setAttribute("orderTime", orderTime);
	    request.setAttribute("telephone", telephone);
	    List<OrderState> orderStateList = orderStateService.queryAllOrderState();
	    request.setAttribute("orderStateList", orderStateList);
	    List<PayWay> payWayList = payWayService.queryAllPayWay();
	    request.setAttribute("payWayList", payWayList);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "OrderInfo/orderInfo_frontquery_result"; 
	}

     /*前台查询OrderInfo信息*/
	@RequestMapping(value="/{orderNo}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable String orderNo,Model model,HttpServletRequest request) throws Exception {
		/*根据主键orderNo获取OrderInfo对象*/
        OrderInfo orderInfo = orderInfoService.getOrderInfo(orderNo);

        List<OrderState> orderStateList = orderStateService.queryAllOrderState();
        request.setAttribute("orderStateList", orderStateList);
        List<PayWay> payWayList = payWayService.queryAllPayWay();
        request.setAttribute("payWayList", payWayList);
        List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
        request.setAttribute("userInfoList", userInfoList);
        request.setAttribute("orderInfo",  orderInfo);
        ArrayList<OrderItem> orderItemList = orderItemService.queryOrderItem(orderInfo, null);
        request.setAttribute("orderItemList", orderItemList);
        return "OrderInfo/orderInfo_frontshow";
        
     
        
        
	}

	/*ajax方式显示订单修改jsp视图页*/
	@RequestMapping(value="/{orderNo}/update",method=RequestMethod.GET)
	public void update(@PathVariable String orderNo,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键orderNo获取OrderInfo对象*/
        OrderInfo orderInfo = orderInfoService.getOrderInfo(orderNo);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonOrderInfo = orderInfo.getJsonObject();
		out.println(jsonOrderInfo.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新订单信息*/
	@RequestMapping(value = "/{orderNo}/update", method = RequestMethod.POST)
	public void update(@Validated OrderInfo orderInfo, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			orderInfoService.updateOrderInfo(orderInfo);
			message = "订单更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "订单更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除订单信息*/
	@RequestMapping(value="/{orderNo}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable String orderNo,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  orderInfoService.deleteOrderInfo(orderNo);
	            request.setAttribute("message", "订单删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "订单删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条订单记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String orderNos,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = orderInfoService.deleteOrderInfos(orderNos);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出订单信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(String orderNo,@ModelAttribute("userObj") UserInfo userObj,@ModelAttribute("payWayObj") PayWay payWayObj,String payAccount,@ModelAttribute("orderState") OrderState orderState,String receiveName,String orderTime,String telephone, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(orderNo == null) orderNo = "";
        if(payAccount == null) payAccount = "";
        if(receiveName == null) receiveName = "";
        if(orderTime == null) orderTime = "";
        if(telephone == null) telephone = "";
        List<OrderInfo> orderInfoList = orderInfoService.queryOrderInfo(orderNo,userObj,payWayObj,payAccount,orderState,receiveName,orderTime,telephone);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "OrderInfo信息记录"; 
        String[] headers = { "订单编号","下单用户","订单总金额","支付方式","支付账号","订单状态","收货人","收货人电话","下单时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<orderInfoList.size();i++) {
        	OrderInfo orderInfo = orderInfoList.get(i); 
        	dataset.add(new String[]{orderInfo.getOrderNo(),orderInfo.getUserObj().getName(),orderInfo.getTotalMoney() + "",orderInfo.getPayWayObj().getPayWayName(),orderInfo.getPayAccount(),orderInfo.getOrderState().getStateName(),orderInfo.getReceiveName(),orderInfo.getTelephone(),orderInfo.getOrderTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"OrderInfo.xls");//filename是下载的xls的名，建议最好用英文 
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
