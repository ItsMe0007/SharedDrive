package com.peoplestrong.timeoff.encashment.pojo;

import java.io.Serializable;

public class SelectItem implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1736248884267370342L;

    private String label;

    private String value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SelectItem() {
        super();
    }

    public SelectItem(String label, String value) {
        super();
        this.label = label;
        this.setValue(value);
    }
}

