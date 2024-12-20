package com.peoplestrong.timeoff.encashment.service.common;

import com.peoplestrong.sessionmanagement.interceptor.AuthFilter;
import com.peoplestrong.sessionmanagement.to.SessionInfo;
import com.peoplestrong.sessionmanagement.to.UserDetailsResponseTO;
import com.peoplestrong.timeoff.common.exception.AppUnauthorizedUserException;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@Service
public class SessionService {

    public UserInfo getUserInfo() {
        SessionInfo sessionInfo = AuthFilter.threadLocal.get();
        if (sessionInfo == null || sessionInfo.getToken() == null) {
            throw new AppUnauthorizedUserException();
        }
        UserDetailsResponseTO userDetail = sessionInfo.getUserDetails();
        if (userDetail == null) {
            throw new AppUnauthorizedUserException();
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setSessionToken(sessionInfo.getToken());
        userInfo.setEmployeeId(userDetail.getEmpId());
        userInfo.setEmployeeCode(userDetail.getEmpCode());
        userInfo.setUserId(userDetail.getUserId());
        userInfo.setUserName(userDetail.getUserName());
        userInfo.setOrganizationId(userDetail.getOrgId());
        userInfo.setTenantId(userDetail.getTenantId());
        userInfo.setCountryID((long) userDetail.getCountryId());
        userInfo.setEntityId(userDetail.getEntityId());
        userInfo.setEntityVsRoleMap(userDetail.getEntityIdVsRoleMapping());
        HashSet<Integer> roles = new HashSet<>();
        if (userDetail.getEntityIdVsRoleMapping() != null) {
            for (List<Integer> roleList : userDetail.getEntityIdVsRoleMapping().values()) {
                roleList.removeIf(Objects::isNull);
                roles.addAll(roleList);
            }
            userInfo.setEntityRoleMap(userDetail.getEntityIdVsRoleMapping());
        }
        userInfo.setRoleList(roles);
        userInfo.setEntityIdVsRoleMapping(userDetail.getEntityIdVsRoleMapping());
        userInfo.setOrgUnitId(userDetail.getOrgUnitId());
        userInfo.setWorkSiteID(userDetail.getWorksiteId());
        userInfo.setBundleId(userDetail.getBundleId());
        userInfo.setBundleName(userDetail.getBundleName());
        return userInfo;
    }


    public String getSessionToken() {
        return getUserInfo().getSessionToken();
    }

    public int getOrgId() {
        UserInfo userInfo = getUserInfo();
        return userInfo != null ? userInfo.getOrganizationId() : 0;
    }

    public int getTenantId() {
        UserInfo userInfo = getUserInfo();
        return userInfo != null ? userInfo.getTenantId() : 0;
    }

    public int getEntityId() {
        UserInfo userInfo = getUserInfo();
        return userInfo != null ? userInfo.getEntityId() : 0;
    }

    public int getUserId() {
        UserInfo userInfo = getUserInfo();
        return userInfo != null ? userInfo.getUserId() : 0;
    }

    public int getEmployeeId() {
        UserInfo userInfo = getUserInfo();
        return userInfo != null ? userInfo.getEmployeeId() : 0;
    }
}
