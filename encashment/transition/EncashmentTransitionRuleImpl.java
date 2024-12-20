package com.peoplestrong.timeoff.encashment.transition;

import java.io.Serializable;

import com.peoplestrong.timeoff.encashment.transport.input.EncashmentRequestTO;
import com.peoplestrong.timeoff.leave.trasnport.input.LeaveRequestTO;

public class EncashmentTransitionRuleImpl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getDefaultRule(EncashmentRequestTO EncashmentRequest) {
		return "goToDefaultTransition";
	}

	public String getDynamicRule(EncashmentRequestTO EncashmentRequest) {
		return "goToDefaultTransition";
	}

	public String getEmpEncashmentHRBPApproval(EncashmentRequestTO EncashmentRequest) {
		String action = "goToDefaultTransition";
		if (EncashmentRequest.getEncashmentType() != null && EncashmentRequest.getEncashmentType().equals("Sabbatical Encashment")) {
			action = "goToHRBPApprover";
		}
		return action;

	}

	public String getEncashmentApprovalTransition(EncashmentRequestTO EncashmentRequest) {
		return "goToEncashmentApproval";
	}

	public String getEncashmentRejectedTransition(EncashmentRequestTO EncashmentRequest) {
		return "goToEncashmentRejected";
	}

	public String getEncashmentApprovedTransition(EncashmentRequestTO EncashmentRequest) {
		return "goToEncashmentApproved";
	}

	public String getEncashmentDeletedTransition(EncashmentRequestTO EncashmentRequest) {
		return "goToEncashmentDeleted";
	}

	public String getEmpEncashmentAdminApproval(EncashmentRequestTO EncashmentRequest) {
		String action = "goToDefaultTransition";
		if (EncashmentRequest.getEncashmentCode() != null) {
			if (EncashmentRequest.getEncashmentCode().equals("Paternity Encashment")
					|| EncashmentRequest.getEncashmentCode().equals("Maternity Encashment")
					|| EncashmentRequest.getEncashmentCode().equals("Exigency Sick Encashment")) {
				action = "gotoEncashmentAdminApprover";
			}
		}
		return action;

	}

}
