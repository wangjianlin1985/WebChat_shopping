package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;
import com.client.utils.JsonUtils;
import com.client.utils.SessionConsts;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Evaluate {
    /*评价编号*/
    private Integer evaluateId;
    public Integer getEvaluateId(){
        return evaluateId;
    }
    public void setEvaluateId(Integer evaluateId){
        this.evaluateId = evaluateId;
    }

    /*商品名称*/
    private Product productObj;
    public Product getProductObj() {
        return productObj;
    }
    public void setProductObj(Product productObj) {
        this.productObj = productObj;
    }

    /*用户名*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*评价内容*/
    @NotEmpty(message="评价内容不能为空")
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*评价时间*/
    @NotEmpty(message="评价时间不能为空")
    private String evaluateTime;
    public String getEvaluateTime() {
        return evaluateTime;
    }
    public void setEvaluateTime(String evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    @JsonIgnore
    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonEvaluate=new JSONObject(); 
		jsonEvaluate.accumulate("evaluateId", this.getEvaluateId());
		jsonEvaluate.accumulate("productObj", this.getProductObj().getProductName());
		jsonEvaluate.accumulate("productObjPri", this.getProductObj().getProductId());
		jsonEvaluate.accumulate("userObj", this.getUserObj().getName());
		jsonEvaluate.accumulate("userObjPri", this.getUserObj().getUser_name());
		jsonEvaluate.accumulate("content", this.getContent());
		jsonEvaluate.accumulate("evaluateTime", this.getEvaluateTime());
		return jsonEvaluate;
    }

    @Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}