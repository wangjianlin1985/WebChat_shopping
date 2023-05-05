package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.PayWay;

import com.chengxusheji.mapper.PayWayMapper;
@Service
public class PayWayService {

	@Resource PayWayMapper payWayMapper;
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

    /*添加支付方式记录*/
    public void addPayWay(PayWay payWay) throws Exception {
    	payWayMapper.addPayWay(payWay);
    }

    /*按照查询条件分页查询支付方式记录*/
    public ArrayList<PayWay> queryPayWay(int currentPage) throws Exception { 
     	String where = "where 1=1";
    	int startIndex = (currentPage-1) * this.rows;
    	return payWayMapper.queryPayWay(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<PayWay> queryPayWay() throws Exception  { 
     	String where = "where 1=1";
    	return payWayMapper.queryPayWayList(where);
    }

    /*查询所有支付方式记录*/
    public ArrayList<PayWay> queryAllPayWay()  throws Exception {
        return payWayMapper.queryPayWayList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber() throws Exception {
     	String where = "where 1=1";
        recordNumber = payWayMapper.queryPayWayCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取支付方式记录*/
    public PayWay getPayWay(int payWayId) throws Exception  {
        PayWay payWay = payWayMapper.getPayWay(payWayId);
        return payWay;
    }

    /*更新支付方式记录*/
    public void updatePayWay(PayWay payWay) throws Exception {
        payWayMapper.updatePayWay(payWay);
    }

    /*删除一条支付方式记录*/
    public void deletePayWay (int payWayId) throws Exception {
        payWayMapper.deletePayWay(payWayId);
    }

    /*删除多条支付方式信息*/
    public int deletePayWays (String payWayIds) throws Exception {
    	String _payWayIds[] = payWayIds.split(",");
    	for(String _payWayId: _payWayIds) {
    		payWayMapper.deletePayWay(Integer.parseInt(_payWayId));
    	}
    	return _payWayIds.length;
    }
}
