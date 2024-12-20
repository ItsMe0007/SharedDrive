package com.peoplestrong.timeoff.encashment.pojo;

import java.io.Serializable;

public class EncModuleConfigurationTO implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = -3587142015941545427L;
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getDataTypeName() {
        return dataTypeName;
    }
    public void setDataTypeName(String dataTypeName) {
        this.dataTypeName = dataTypeName;
    }
    private String value ;
    private String dataTypeName ;




}
