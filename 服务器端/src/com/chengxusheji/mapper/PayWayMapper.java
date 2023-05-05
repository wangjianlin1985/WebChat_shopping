package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.PayWay;

public interface PayWayMapper {
	/*添加支付方式信息*/
	public void addPayWay(PayWay payWay) throws Exception;

	/*按照查询条件分页查询支付方式记录*/
	public ArrayList<PayWay> queryPayWay(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有支付方式记录*/
	public ArrayList<PayWay> queryPayWayList(@Param("where") String where) throws Exception;

	/*按照查询条件的支付方式记录数*/
	public int queryPayWayCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条支付方式记录*/
	public PayWay getPayWay(int payWayId) throws Exception;

	/*更新支付方式记录*/
	public void updatePayWay(PayWay payWay) throws Exception;

	/*删除支付方式记录*/
	public void deletePayWay(int payWayId) throws Exception;

}
