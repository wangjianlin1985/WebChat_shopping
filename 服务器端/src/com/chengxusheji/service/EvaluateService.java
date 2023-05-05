package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Product;
import com.chengxusheji.po.UserInfo;
import com.chengxusheji.po.Evaluate;

import com.chengxusheji.mapper.EvaluateMapper;
@Service
public class EvaluateService {

	@Resource EvaluateMapper evaluateMapper;
    /*每页显示记录数目*/
    private int rows = 10;;
    public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加商品评价记录*/
    public void addEvaluate(Evaluate evaluate) throws Exception {
    	evaluateMapper.addEvaluate(evaluate);
    }

    /*按照查询条件分页查询商品评价记录*/
    public ArrayList<Evaluate> queryEvaluate(Product productObj,UserInfo userObj,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != productObj && productObj.getProductId()!= null && productObj.getProductId()!= 0)  where += " and t_evaluate.productObj=" + productObj.getProductId();
    	if(null != userObj &&  userObj.getUser_name() != null  && !userObj.getUser_name().equals(""))  where += " and t_evaluate.userObj='" + userObj.getUser_name() + "'";
    	int startIndex = (currentPage-1) * this.rows;
    	return evaluateMapper.queryEvaluate(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Evaluate> queryEvaluate(Product productObj,UserInfo userObj) throws Exception  { 
     	String where = "where 1=1";
    	if(null != productObj && productObj.getProductId()!= null && productObj.getProductId()!= 0)  where += " and t_evaluate.productObj=" + productObj.getProductId();
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_evaluate.userObj='" + userObj.getUser_name() + "'";
    	return evaluateMapper.queryEvaluateList(where);
    }

    /*查询所有商品评价记录*/
    public ArrayList<Evaluate> queryAllEvaluate()  throws Exception {
        return evaluateMapper.queryEvaluateList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(Product productObj,UserInfo userObj) throws Exception {
     	String where = "where 1=1";
    	if(null != productObj && productObj.getProductId()!= null && productObj.getProductId()!= 0)  where += " and t_evaluate.productObj=" + productObj.getProductId();
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_evaluate.userObj='" + userObj.getUser_name() + "'";
        recordNumber = evaluateMapper.queryEvaluateCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取商品评价记录*/
    public Evaluate getEvaluate(int evaluateId) throws Exception  {
        Evaluate evaluate = evaluateMapper.getEvaluate(evaluateId);
        return evaluate;
    }

    /*更新商品评价记录*/
    public void updateEvaluate(Evaluate evaluate) throws Exception {
        evaluateMapper.updateEvaluate(evaluate);
    }

    /*删除一条商品评价记录*/
    public void deleteEvaluate (int evaluateId) throws Exception {
        evaluateMapper.deleteEvaluate(evaluateId);
    }

    /*删除多条商品评价信息*/
    public int deleteEvaluates (String evaluateIds) throws Exception {
    	String _evaluateIds[] = evaluateIds.split(",");
    	for(String _evaluateId: _evaluateIds) {
    		evaluateMapper.deleteEvaluate(Integer.parseInt(_evaluateId));
    	}
    	return _evaluateIds.length;
    }
}
