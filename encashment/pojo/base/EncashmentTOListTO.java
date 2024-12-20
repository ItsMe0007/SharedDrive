package com.peoplestrong.timeoff.encashment.pojo.base;

import java.util.Map;

public class EncashmentTOListTO {
    private String  encashmentTypeCode;
    private String sysEncashmentType;
    private Integer sysEncashmentTypeId;
    private Integer encashmentTypeContentId;
    private Map<Long,Long> encashmentTypeIdMap;
    
    public String getEncashmentTypeCode() {
        return encashmentTypeCode;
    }
    
    public void setEncashmentTypeCode(String encashmentTypeCode) {
        this.encashmentTypeCode = encashmentTypeCode;
    }
    
    public String getSysEncashmentType() {
        return sysEncashmentType;
    }
    
    public void setSysEncashmentType(String sysEncashmentType) {
        this.sysEncashmentType = sysEncashmentType;
    }
    
    public Integer getSysEncashmentTypeId() {
        return sysEncashmentTypeId;
    }
    
    public void setSysEncashmentTypeId(Integer sysEncashmentTypeId) {
        this.sysEncashmentTypeId = sysEncashmentTypeId;
    }
    
    public Map<Long, Long> getEncashmentTypeIdMap() {
        return encashmentTypeIdMap;
    }
    
    public void setEncashmentTypeIdMap(Map<Long, Long> encashmentTypeIdMap) {
        this.encashmentTypeIdMap = encashmentTypeIdMap;
    }
    
    public Integer getEncashmentTypeContentId() {
        return encashmentTypeContentId;
    }
    
    public void setEncashmentTypeContentId(Integer encashmentTypeContentId) {
        this.encashmentTypeContentId = encashmentTypeContentId;
    }
    
    public EncashmentTOListTO(String encashmentTypeCode, String sysEncashmentType, Integer sysEncashmentTypeId, Integer encashmentTypeContentId, Map<Long, Long> encashmentTypeIdMap) {
        this.encashmentTypeCode = encashmentTypeCode;
        this.sysEncashmentType = sysEncashmentType;
        this.sysEncashmentTypeId = sysEncashmentTypeId;
        this.encashmentTypeContentId = encashmentTypeContentId;
        this.encashmentTypeIdMap = encashmentTypeIdMap;
    }
    
    public EncashmentTOListTO() {
    }
}
