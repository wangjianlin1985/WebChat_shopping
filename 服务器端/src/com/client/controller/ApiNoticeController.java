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
import com.chengxusheji.po.Notice;
import com.chengxusheji.service.NoticeService;
import com.client.service.AuthService;
import com.client.utils.JsonResult;
import com.client.utils.JsonResultBuilder;
import com.client.utils.ReturnCode;

@RestController
@RequestMapping("/api/notice") 
public class ApiNoticeController {
	@Resource NoticeService noticeService;
	@Resource AuthService authService;

	@InitBinder("notice")
	public void initBinderNotice(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("notice.");
	}

	/*客户端ajax方式添加新闻公告信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(@Validated Notice notice, BindingResult br, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		if (br.hasErrors()) //验证输入参数
			return JsonResultBuilder.error(ReturnCode.INPUT_PARAM_ERROR);
        noticeService.addNotice(notice); //添加到数据库
        return JsonResultBuilder.ok();
	}

	/*客户端ajax更新新闻公告信息*/
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public JsonResult update(@Validated Notice notice, BindingResult br, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		if (br.hasErrors())  //验证输入参数
			return JsonResultBuilder.error(ReturnCode.INPUT_PARAM_ERROR); 
        noticeService.updateNotice(notice);  //更新记录到数据库
        return JsonResultBuilder.ok(noticeService.getNotice(notice.getNoticeId()));
	}

	/*ajax方式显示获取新闻公告详细信息*/
	@RequestMapping(value="/get/{noticeId}",method=RequestMethod.POST)
	public JsonResult getNotice(@PathVariable int noticeId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键noticeId获取Notice对象*/
        Notice notice = noticeService.getNotice(noticeId); 
        return JsonResultBuilder.ok(notice);
	}

	/*ajax方式删除新闻公告记录*/
	@RequestMapping(value="/delete/{noticeId}",method=RequestMethod.POST)
	public JsonResult deleteNotice(@PathVariable int noticeId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		try {
			noticeService.deleteNotice(noticeId);
			return JsonResultBuilder.ok();
		} catch (Exception ex) {
			return JsonResultBuilder.error(ReturnCode.FOREIGN_KEY_CONSTRAINT_ERROR);
		}
	}

	//客户端查询新闻公告信息
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public JsonResult list(String title,String publishDate,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (title == null) title = "";
		if (publishDate == null) publishDate = "";
		if(rows != 0)noticeService.setRows(rows);
		List<Notice> noticeList = noticeService.queryNotice(title, publishDate, page);
	    /*计算总的页数和总的记录数*/
	    noticeService.queryTotalPageAndRecordNumber(title, publishDate);
	    /*获取到总的页码数目*/
	    int totalPage = noticeService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = noticeService.getRecordNumber();
	    HashMap<String, Object> resultMap = new HashMap<String, Object>();
	    resultMap.put("totalPage", totalPage);
	    resultMap.put("list", noticeList);
	    return JsonResultBuilder.ok(resultMap);
	}

	//客户端ajax获取所有新闻公告
	@RequestMapping(value="/listAll",method=RequestMethod.POST)
	public JsonResult listAll() throws Exception{
		List<Notice> noticeList = noticeService.queryAllNotice(); 
		return JsonResultBuilder.ok(noticeList);
	}
}

