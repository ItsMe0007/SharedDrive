package com.peoplestrong.timeoff.encashment.pojo.base;

public class DependentDetailTO {
    
    private Integer dependentId;
    private String name;
    private String relation;
    private int relationshipTypeId;
    private int age;
    private int percentage;

    public Integer getDependentId() {
        return dependentId;
    }

    public void setDependentId(Integer dependentId) {
        this.dependentId = dependentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public int getRelationshipTypeId() {
        return relationshipTypeId;
    }

    public void setRelationshipTypeId(int relationshipTypeId) {
        this.relationshipTypeId = relationshipTypeId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
