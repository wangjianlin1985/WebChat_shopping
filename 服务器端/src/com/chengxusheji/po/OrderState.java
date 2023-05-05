package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;
import com.client.utils.JsonUtils;
import com.client.utils.SessionConsts;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class OrderState {
    /*状态编号*/
    private Integer stateId;
    public Integer getStateId(){
        return stateId;
    }
    public void setStateId(Integer stateId){
        this.stateId = stateId;
    }

    /*订单状态名称*/
    @NotEmpty(message="订单状态名称不能为空")
    private String stateName;
    public String getStateName() {
        return stateName;
    }
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @JsonIgnore
    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonOrderState=new JSONObject(); 
		jsonOrderState.accumulate("stateId", this.getStateId());
		jsonOrderState.accumulate("stateName", this.getStateName());
		return jsonOrderState;
    }

    @Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}