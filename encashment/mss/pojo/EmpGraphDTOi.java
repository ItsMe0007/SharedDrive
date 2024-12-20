package com.peoplestrong.timeoff.encashment.mss.pojo;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class EmpGraphDTOi implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @JsonProperty("fields")
    private String fields;
    
    @JsonProperty("filters")
    private String filters;
    
    @JsonProperty("offset")
    private Integer offset;
    
    @JsonProperty("userId")
    private long userId;
    
    @JsonProperty("organizationId")
    private long organizationId;
    
    @JsonProperty("tenantId")
    private long tenantId;
    
    @JsonProperty("gsearchText")
    private String gsearchText;
    
    @JsonProperty("sort")
    private String sort;
    
    @JsonProperty("formName")
    private String formName;
    
    @JsonProperty("countryIDs")
    protected List<Long> countryIDs;
    
    @JsonProperty("entityIDs")
    protected List<Integer> entityIDs;
    
    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(long organizationId) {
        this.organizationId = organizationId;
    }

    public long getTenantId() {
        return tenantId;
    }

    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }

    public String getGsearchText() {
        return gsearchText;
    }

    public void setGsearchText(String gsearchText) {
        this.gsearchText = gsearchText;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public List<Long> getCountryIDs() {
        return countryIDs;
    }

    public void setCountryIDs(List<Long> countryIDs) {
        this.countryIDs = countryIDs;
    }

    public List<Integer> getEntityIDs() {
        return entityIDs;
    }

    public void setEntityIDs(List<Integer> entityIDs) {
        this.entityIDs = entityIDs;
    }
}
