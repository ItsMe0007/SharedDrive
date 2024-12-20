package com.peoplestrong.timeoff.encashment.constant;

import java.io.Serializable;

public class EncashmentConstant implements Serializable {

    public static final String TRANSITION_RULE_IMPL = "com.peoplestrong.timeoff.encashment.transition.EncashmentTransitionRuleImpl";

    public static final String STAGE_TRANSITION_EXCEPTION = "Exception occured while retrieving decisicionTransition for sysWorkflowStageActionID ---> ";

    public static final String ENCASHMENT_ENTITY_NAME = "TmEmployeeEncashment";
    
    public static final String ENCASHMENT_MODULE_NAME = "LeaveEncashment";

    public static final String ATTACHMENT_MANDATORY = "validate.attachmentmandatory.message";
    
    public static final String EMPLOYEE_ON_NOTICE_PERIOD="validate.serving.notice.period.message";

    public static final String EMPLOYEE_ON_PROBATION_PERIOD="validate.probation.period.message";
    
    public static final String INSUFFICIENT_LEAVE_BALANCE = "validate.insufficient.leave.balance.message";
    
    public static final String EMPLOYEE_TYPE_RESIGNED = "Resigned";
    
    public static final String EMPLOYEE_TYPE_ON_PROBATION = "On Probation";
    
    public static final String ENCASHMENT_GROUP_POLICY = "LEAVE-ENCASHMENT-POLICY";
    
    public static final String ENCASHMENT_CONTENT_CATEGORY = "LeaveEncashmentType";
    
    public static final String RELATION_TYPE_CONTENT_CATEGORY = "RelationshipType";
    
    public static final String CREDIT = "Credit";
    
    public static final String DEBIT = "Debit";
    
    public static final String ENCASHMENT_CODE_START_CHARACTER = "E";
    
    public static final String ENCASHMENT_ID_KEY = "ENCASHMENT_ID_KEY";
    
    public static final String TEMPLATE_PLACEHOLDER_MAIL_TYPE = "EmailType";
    
    public static final String TEMPLATE_PLACEHOLDER_CONTENT_TYPE = "ContentType";
    
    public static final String WORKFLOW_STAGE_TYPE_APPROVAL = "Aproval";
    
    public static final String WORKFLOW_STAGE_TYPE_COMPLETED = "Completed";
    
    public static final String WORKFLOW_WITHDRAW_ACTION = "WITHDRAW";
    
    public static final String WORKFLOW_REJECT_ACTION = "REJECT";
    
    public static final String WORKFLOW_STAGE_TYPE_REJECT = "Reject";
    
    public static final String WORKFLOW_STAGE_TYPE_INITIAL = "Initial";
    
    public static final String WORKFLOW_APPROVE_ACTION = "APPROVE";
    
    public static final String WORKFLOW_STAGE_TYPE_ENCASHMENT_CANCEL_REJECTED = "Encashment cancel rejected";
    
    public static final String WORKFLOW_SUBMIT_ACTION = "SUBMIT";
    
    public static final String WORKFLOW_STAGE_TYPE_CANCEL_APPROVAL = "Cancel approval";
    
    public static final String WORKFLOW_MANAGER_WITHDRAW_ACTION = "MANAGER_WITHDRAW";
    
    public static final String MULTIPLE_ACTIONS_ON_ENCASHMENT_ERROR_MESSAGE = "Action has already been taken on this Encashment, Please reload the screen and try again";
    
    public static final String MINIMUM_ENCASHMENT_LIMIT="validate.minimum.encashment.message";
    
    public static final String MAXIMUM_ENCASHMENT_LIMIT="validate.maximum.encashment.message";
    
    public static final String AGE_BASED_ENCASHMENT_LIMIT="validate.age.based.encashment.limit.message";
    
    public static final String INSUFFICIENT_LEAVE_BALANCE_FOR_CALENDAR_BLOCK="validate.insufficient.leave.balance.for.calendar.block.message";
    
    public static final String INSUFFICIENT_LEAVE_BALANCE_AFTER_CALENDAR_BLOCK = "validate.insufficient.leave.balance.after.calendar.block.message";
    
    public static final String AGED_BASED_ENCASHMENT = "Age Based";
    
    public static final String ACTUAL_TRAVEL = "Actual Travel";
    
    public static final String MULTI_CALENDAR_PARAMETER = "Enable_Multicalendar_Encashment";
    
    public static final String MAXIMUM_NUMBER_OF_TRANSACTION_LIMIT="validate.maximum.number.of.transaction.encashment.message";
    
    public static final String MAXIMUM_ENCASHMENT_TRANSACTION="validate.maximum.encashment.transaction.message";
    
    public static final String MAXIMUM_ENCASHMENT_LIMIT_EXCEEDED="validate.maximum.encashment.exceeded.message";
    
    public static final String MAXIMUM_ENCASHMENT_LIMIT_FOR_SAME_CALENDAR_GROUP="validate.maximum.encashment.for.same.calendar.group.message";
    
    public static final String MAXIMUM_NUMBER_OF_TRANSACTION_LIMIT_WITHIN_MONTH="validate.maximum.number.of.transaction.encashment.within.month.message";
    
    public static final String MAXIMUM_NUMBER_OF_TRANSACTION_LIMIT_WITHIN_QUARTER="validate.maximum.number.of.transaction.encashment.within.quarter.message";
    
    public static final String MAXIMUM_NUMBER_OF_TRANSACTION_LIMIT_WITHIN_YEAR="validate.maximum.number.of.transaction.encashment.within.year.message";
    
    public static final String MAXIMUM_NUMBER_OF_ENCASHMENT_LIMIT_IN_GROUP = "validate.maximum.number.of.transaction.encashment.in.calendar.group";
    
    public static class MessageReponse
    {
        public static final String SUCCESS = "EC200";

        public static final String FAIL = "EC201";
    }
}
