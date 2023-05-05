package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;
import com.client.utils.JsonUtils;
import com.client.utils.SessionConsts;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PayWay {
    /*支付方式id*/
    private Integer payWayId;
    public Integer getPayWayId(){
        return payWayId;
    }
    public void setPayWayId(Integer payWayId){
        this.payWayId = payWayId;
    }

    /*支付方式名称*/
    @NotEmpty(message="支付方式名称不能为空")
    private String payWayName;
    public String getPayWayName() {
        return payWayName;
    }
    public void setPayWayName(String payWayName) {
        this.payWayName = payWayName;
    }

    @JsonIgnore
    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonPayWay=new JSONObject(); 
		jsonPayWay.accumulate("payWayId", this.getPayWayId());
		jsonPayWay.accumulate("payWayName", this.getPayWayName());
		return jsonPayWay;
    }

    @Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}