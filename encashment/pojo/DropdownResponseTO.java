package com.peoplestrong.timeoff.encashment.pojo;

import java.util.List;

public class DropdownResponseTO {
    
        private List<SelectItem> encashmentTypes;
        private List<SelectItem> employeeCalendarGroups;
        private List<SelectItem> ageBasedEncashment;
        private List<SelectItem> dependentBasedEncashment;

        public DropdownResponseTO(List<SelectItem> encashmentTypes, List<SelectItem> employeeCalendarGroups,
                                  List<SelectItem> ageBasedEncashment, List<SelectItem> dependentBasedEncashment) {
                this.encashmentTypes = encashmentTypes;
                this.employeeCalendarGroups = employeeCalendarGroups;
                this.ageBasedEncashment = ageBasedEncashment;
                this.dependentBasedEncashment = dependentBasedEncashment;
        }

        public List<SelectItem> getEncashmentTypes() {
                return encashmentTypes;
        }

        public void setEncashmentTypes(List<SelectItem> encashmentTypes) {
                this.encashmentTypes = encashmentTypes;
        }

        public List<SelectItem> getEmployeeCalendarGroups() {
                return employeeCalendarGroups;
        }

        public void setEmployeeCalendarGroups(List<SelectItem> employeeCalendarGroups) {
                this.employeeCalendarGroups = employeeCalendarGroups;
        }

        public List<SelectItem> getAgeBasedEncashment() {
                return ageBasedEncashment;
        }

        public void setAgeBasedEncashment(List<SelectItem> ageBasedEncashment) {
                this.ageBasedEncashment = ageBasedEncashment;
        }

        public List<SelectItem> getDependentBasedEncashment() {
                return dependentBasedEncashment;
        }

        public void setDependentBasedEncashment(List<SelectItem> dependentBasedEncashment) {
                this.dependentBasedEncashment = dependentBasedEncashment;
        }
}
