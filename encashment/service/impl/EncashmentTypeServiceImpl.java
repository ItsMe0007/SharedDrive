package com.peoplestrong.timeoff.encashment.service.impl;

import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.dataservice.model.encashment.*;
import com.peoplestrong.timeoff.dataservice.model.leave.HrContentType;
import com.peoplestrong.timeoff.dataservice.repo.encashment.*;
import com.peoplestrong.timeoff.dataservice.repo.leave.HrContentTypeRepository;
import com.peoplestrong.timeoff.encashment.pojo.*;
import com.peoplestrong.timeoff.encashment.pojo.base.AppResponse;
import com.peoplestrong.timeoff.encashment.service.EncashmentTypeService;
import com.peoplestrong.timeoff.encashment.service.common.SessionService;
import com.peoplestrong.timeoff.encashment.utils.AppLogger;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import com.peoplestrong.timeoff.leave.service.Impl.transaction.LeaveTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class EncashmentTypeServiceImpl implements EncashmentTypeService {

    private static final AppLogger logger = AppLogger.get(EncashmentTypeServiceImpl.class);

    private final TmAgeBasedEncashmentRepository ageBasedEncashmentRepository;
    private final TmDependentBasedEncashmentRepository dependentBasedEncashmentRepository;
    private final HrContentTypeRepository hrContentTypeRepository;
    private final TmEncashmentCalenderGroupRepository tmEncashmentCalenderGroupRepository;
    private final TmEncashmentTypeRepository tmEncashmentTypeRepository;
    private final SessionService sessionService;

    private final LeaveTransactionService leaveTransactionService;


    @Autowired
    public EncashmentTypeServiceImpl(TmAgeBasedEncashmentRepository ageBasedEncashmentRepository, TmDependentBasedEncashmentRepository dependentBasedEncashmentRepository, TmEncashmentCalenderGroupRepository tmEncashmentCalenderGroupRepository, HrContentTypeRepository hrContentTypeRepository, TmEncashmentTypeRepository tmEncashmentTypeRepository, SessionService sessionService, LeaveTransactionService leaveTransactionService) {
        this.ageBasedEncashmentRepository = ageBasedEncashmentRepository;
        this.dependentBasedEncashmentRepository = dependentBasedEncashmentRepository;
        this.tmEncashmentCalenderGroupRepository = tmEncashmentCalenderGroupRepository;
        this.hrContentTypeRepository = hrContentTypeRepository;
        this.tmEncashmentTypeRepository = tmEncashmentTypeRepository;
        this.sessionService = sessionService;
        this.leaveTransactionService = leaveTransactionService;
    }



    @Override
    public List<EncashmentTypeResponseTO> listEncashmentTypes() throws AppRuntimeException {
        try {
            UserInfo userInfo= sessionService.getUserInfo();
            List<TmEncashmentType> encashmentTypes = tmEncashmentTypeRepository.getEncashmentForOrganization(userInfo.getOrganizationId());

            if (encashmentTypes == null || encashmentTypes.isEmpty()) {
                logger.warn("No encashment types found");
                return Collections.emptyList();
            }

            return encashmentTypes.stream()
                    .map(this::mapToResponseTO)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching encashment types", e);
            throw new AppRuntimeException("Failed to fetch encashment types");
        }
    }

    @Override
    public DropdownResponseTO getDropdowns(EncashmentTypeRequestTO request) throws AppRuntimeException {
        logger.info("Fetching dropdown values");
        UserInfo userInfo= sessionService.getUserInfo();

        List<SelectItem> encashmentTypes = fetchEncashmentTypes(userInfo.getOrganizationId(), userInfo.getEntityId());
        List<SelectItem> employeeCalendarGroups = fetchEmployeeCalendarGroups(userInfo.getOrganizationId());
        List<SelectItem> ageBasedEncashment = fetchActiveAgeBasedEncashment(userInfo.getOrganizationId());
        List<SelectItem> dependentBasedEncashment = fetchActiveDependentBasedEncashment(userInfo.getOrganizationId());

        List<Long> existingCalGroupIds = Collections.emptyList();
        List<Integer> existingEncashmentTypeIds = Collections.emptyList();

        if (request.getEncashmentType().getValue() != null) {
            existingCalGroupIds = tmEncashmentTypeRepository.getActiveConfigECGIdsForContentId(
                    userInfo.getOrganizationId(),
                    true,
                    Integer.valueOf(request.getEncashmentType().getValue())
            );
        } else if (request.getEncashmentCalenderGroup().getValue() != null) {
            existingEncashmentTypeIds = tmEncashmentTypeRepository.getActiveConfigContentIdsForECGId(
                    userInfo.getOrganizationId(),
                    true,
                    Long.valueOf(request.getEncashmentCalenderGroup().getValue())
            );
        }

        List<Integer> finalExistingEncashmentTypeIds = existingEncashmentTypeIds;
        encashmentTypes = encashmentTypes.stream()
                .filter(item -> !finalExistingEncashmentTypeIds.contains(Integer.valueOf(item.getValue())))
                .collect(Collectors.toList());

        List<Long> finalExistingCalGroupIds = existingCalGroupIds;
        employeeCalendarGroups = employeeCalendarGroups.stream()
                .filter(item -> !finalExistingCalGroupIds.contains(Long.valueOf(item.getValue())))
                .collect(Collectors.toList());
        return new DropdownResponseTO(encashmentTypes, employeeCalendarGroups, ageBasedEncashment, dependentBasedEncashment);
    }

    private List<SelectItem> fetchEncashmentTypes(int organizationId, int entityId) {
        List<Object[]> contentTypes = hrContentTypeRepository.findByOrganizationIdAndContentCategory(organizationId, "LeaveEncashmentType", true, entityId);

        if (contentTypes == null || contentTypes.isEmpty()) {
            return Collections.emptyList();
        }

        return contentTypes.stream()
                .filter(Objects::nonNull)
                .map(type -> {
                    if (type[1] != null && type[0] != null) {
                        return new SelectItem(type[1].toString(), type[0].toString());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    private List<SelectItem> fetchEmployeeCalendarGroups(int organizationId) {
        List<Object[]> calendarGroups = tmEncashmentCalenderGroupRepository.findByOrganizationIdAndActive(true, organizationId);

        if (calendarGroups == null || calendarGroups.isEmpty()) {
            return Collections.emptyList();
        }

        return calendarGroups.stream()
                .filter(Objects::nonNull)
                .map(block -> {
                    if (block[1] != null && block[0] != null) {
                        return new SelectItem(block[1].toString(), block[0].toString());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<SelectItem> fetchActiveAgeBasedEncashment(int organizationId) {
        List<Object[]> ageBasedEncashments = ageBasedEncashmentRepository.findByIsActiveAndOrganizationId(true, organizationId);

        if (ageBasedEncashments == null || ageBasedEncashments.isEmpty()) {
            return Collections.emptyList();
        }

        return ageBasedEncashments.stream()
                .filter(Objects::nonNull)
                .map(encashment -> {
                    if (encashment[1] != null && encashment[0] != null) {
                        return new SelectItem(encashment[1].toString(), encashment[0].toString());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<SelectItem> fetchActiveDependentBasedEncashment(int organizationId) {
        List<Object[]> dependentEncashments = dependentBasedEncashmentRepository.findByIsActiveAndOrganizationId(true, organizationId);

        if (dependentEncashments == null || dependentEncashments.isEmpty()) {
            return Collections.emptyList();
        }

        return dependentEncashments.stream()
                .filter(Objects::nonNull)
                .map(encashment -> {
                    if (encashment[1] != null && encashment[0] != null) {
                        return new SelectItem(encashment[1].toString(), encashment[0].toString());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    @Override
    public EncashmentTypeResponseTO viewDetail(Long id) throws AppRuntimeException {
        logger.info("Fetching details for encashment type ID: " + id);

        return tmEncashmentTypeRepository.findById(id)
                .map(this::mapToResponseTO)
                .orElseThrow(() -> new NoSuchElementException("Encashment type not found for ID: " + id));
    }

    @Override
    public void saveEncashmentType(EncashmentTypeRequestTO request ) throws AppRuntimeException {
        UserInfo userInfo = sessionService.getUserInfo();
        leaveTransactionService.saveEncashmentType(request, userInfo);
}

    @Override
    public AppResponse<Object> updateEncashmentType(EncashmentTypeRequestTO request) throws AppRuntimeException {
        AppResponse<Object> appResponse;

        try {
            UserInfo userInfo = sessionService.getUserInfo();
            leaveTransactionService.updateEncashmentType(request, userInfo);
            appResponse = AppResponse.success("Data updated successfully", null);
        } catch (NoSuchElementException e) {
            appResponse = AppResponse.error(e.getMessage());
        } catch (Exception e) {
            logger.error("Error updating encashment type", e);
            appResponse = AppResponse.unknownError();
        }
        return  appResponse;
    }

    private EncashmentTypeResponseTO mapToResponseTO(TmEncashmentType encashmentType) {
        EncashmentTypeResponseTO responseTO = new EncashmentTypeResponseTO();
        UserInfo userInfo = sessionService.getUserInfo();

        if (encashmentType != null) {
            responseTO.setEncashmentTypeID(encashmentType.getEncashmentTypeID());

            SelectItem encType = new SelectItem();
            if (encashmentType.getEncashmentTypeContentID() != null) {
                HrContentType hrContent = hrContentTypeRepository.findByTypeId(encashmentType.getEncashmentTypeContentID());
                if (hrContent != null) {
                    encType.setLabel(hrContent.getType());
                    encType.setValue(hrContent.getTypeId().toString());
                }
            }
            responseTO.setEncashmentType(encType);
            responseTO.setMaxEncashmentAllowed(encashmentType.getMaximumEncashmentInTransaction());
            responseTO.setMinEncashmentAllowed(encashmentType.getMinimumEncashmentInTransaction());

            SelectItem calenderGroup = new SelectItem();
            if (encashmentType.getEncashmentCalenderGroupID() != null) {
                String calenderGroupEntityName = tmEncashmentCalenderGroupRepository.findbyCalenderId(encashmentType.getEncashmentCalenderGroupID());
                if (calenderGroupEntityName != null) {
                    calenderGroup.setLabel(calenderGroupEntityName);
                    calenderGroup.setValue(encashmentType.getEncashmentCalenderGroupID().toString());
                }
            }
            responseTO.setEncashmentCalenderGroup(calenderGroup);

            responseTO.setActive(encashmentType.getActive() != null ? encashmentType.getActive() : false);
            responseTO.setAttachmentMandatory(encashmentType.getAttachmentMandatory() != null ? encashmentType.getAttachmentMandatory() : false);
            responseTO.setAllowDuringNotice(encashmentType.getAllowDuringNoticePeriod() != null ? encashmentType.getAllowDuringNoticePeriod() : false);
            responseTO.setAllowDuringProbation(encashmentType.getAllowDuringProbationPeriod() != null ? encashmentType.getAllowDuringProbationPeriod() : false);

            SelectItem ageBased = new SelectItem();
            if (encashmentType.getAgeBasedEncashmentID() != null) {
                TmAgeBasedEncashment ageEncashment = ageBasedEncashmentRepository.findByAgeBasedEncashmentId(userInfo.getTenantId(), encashmentType.getAgeBasedEncashmentID());
                if (ageEncashment != null) {
                    ageBased.setLabel(ageEncashment.getConfigurationName());
                    ageBased.setValue(encashmentType.getAgeBasedEncashmentID().toString());
                }
            }
            responseTO.setAgeBasedEncashment(ageBased);

            SelectItem dependentBased = new SelectItem();
            if (encashmentType.getDependentBasedEncashmentID() != null) {
                TmDependentBasedEncashment dependentEncashment = dependentBasedEncashmentRepository.findByDependentBasedEncashmentId(userInfo.getTenantId(), encashmentType.getDependentBasedEncashmentID());
                if (dependentEncashment != null) {
                    dependentBased.setLabel(dependentEncashment.getConfigurationName());
                    dependentBased.setValue(encashmentType.getDependentBasedEncashmentID().toString());
                }
            }
            responseTO.setDependentBasedEncashment(dependentBased);

            responseTO.setMaxTransactions(encashmentType.getMaximumNumberOfTransactions());
        }
        return responseTO;
    }

}
