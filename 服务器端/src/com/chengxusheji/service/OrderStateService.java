package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.OrderState;

import com.chengxusheji.mapper.OrderStateMapper;
@Service
public class OrderStateService {

	@Resource OrderStateMapper orderStateMapper;
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

    /*添加订单状态记录*/
    public void addOrderState(OrderState orderState) throws Exception {
    	orderStateMapper.addOrderState(orderState);
    }

    /*按照查询条件分页查询订单状态记录*/
    public ArrayList<OrderState> queryOrderState(int currentPage) throws Exception { 
     	String where = "where 1=1";
    	int startIndex = (currentPage-1) * this.rows;
    	return orderStateMapper.queryOrderState(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<OrderState> queryOrderState() throws Exception  { 
     	String where = "where 1=1";
    	return orderStateMapper.queryOrderStateList(where);
    }

    /*查询所有订单状态记录*/
    public ArrayList<OrderState> queryAllOrderState()  throws Exception {
        return orderStateMapper.queryOrderStateList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber() throws Exception {
     	String where = "where 1=1";
        recordNumber = orderStateMapper.queryOrderStateCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取订单状态记录*/
    public OrderState getOrderState(int stateId) throws Exception  {
        OrderState orderState = orderStateMapper.getOrderState(stateId);
        return orderState;
    }

    /*更新订单状态记录*/
    public void updateOrderState(OrderState orderState) throws Exception {
        orderStateMapper.updateOrderState(orderState);
    }

    /*删除一条订单状态记录*/
    public void deleteOrderState (int stateId) throws Exception {
        orderStateMapper.deleteOrderState(stateId);
    }

    /*删除多条订单状态信息*/
    public int deleteOrderStates (String stateIds) throws Exception {
    	String _stateIds[] = stateIds.split(",");
    	for(String _stateId: _stateIds) {
    		orderStateMapper.deleteOrderState(Integer.parseInt(_stateId));
    	}
    	return _stateIds.length;
    }
}
