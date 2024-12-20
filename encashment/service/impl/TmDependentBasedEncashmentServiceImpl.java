package com.peoplestrong.timeoff.encashment.service.impl;

import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmDependentBasedEncashment;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmDependentBasedEncashmentDetail;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmDependentBasedEncashmentDetailRepository;
import com.peoplestrong.timeoff.encashment.pojo.*;
import com.peoplestrong.timeoff.encashment.service.TmDependentBasedEncashmentService;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmDependentBasedEncashmentRepository;
import com.peoplestrong.timeoff.encashment.utils.AppLogger;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import com.peoplestrong.timeoff.leave.service.Impl.transaction.LeaveTransactionService;
import org.apache.hbase.thirdparty.org.apache.commons.collections4.trie.analyzer.StringKeyAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TmDependentBasedEncashmentServiceImpl implements TmDependentBasedEncashmentService {

    private final TmDependentBasedEncashmentRepository repository;

    private static final AppLogger logger = AppLogger.get(TmAgeBasedEncashmentServiceImpl.class);

    private final TmDependentBasedEncashmentDetailRepository encashmentDetailRepository;

    private final LeaveTransactionService transactionService;

    @Autowired
    public TmDependentBasedEncashmentServiceImpl(TmDependentBasedEncashmentRepository repository, TmDependentBasedEncashmentDetailRepository encashmentDetailRepository, LeaveTransactionService transactionService) {
        this.repository = repository;
        this.encashmentDetailRepository = encashmentDetailRepository;
        this.transactionService = transactionService;
    }

    @Override
    public DependentBasedDetailResponseTo save(DependentBasedDetailRequestAllTo request, UserInfo userInfo) {
        try {
            DependentBasedDetailResponseTo response = new DependentBasedDetailResponseTo();
            DependentBasedRequestTo dependentBasedRequestTo = request.getDependentBasedRequestTo();
            List<DependentBasedDetailRequestTo> dependentBasedDetailRequestTo = request.getEncashmentDetails();
            TmDependentBasedEncashment encashment = setDependentEncashmentFromTo(dependentBasedRequestTo);

            if (dependentBasedRequestTo.getConfigurationName() == null || dependentBasedRequestTo.getConfigurationName().isEmpty()) {
                throw new AppRuntimeException("Configuration Name can not be Null or Empty");
            }
            Boolean exists = repository.existsByConfigurationNameAndOrganizationId(
                    dependentBasedRequestTo.getConfigurationName().trim(), userInfo.getOrganizationId()
            );
            if (exists) {
                throw new AppRuntimeException("Configuration Name already exists for this organization");
            }
            this.validateDependentBasedEncashment(dependentBasedDetailRequestTo);
            encashment.setCreatedDate(new Date());
            encashment.setModifiedDate(new Date());
            encashment.setCreatedBy(userInfo.getUserId());
            encashment.setModifiedBy(userInfo.getUserId());
            encashment.setOrganizationId(userInfo.getOrganizationId());
            encashment.setTenantID(userInfo.getTenantId());

            List<TmDependentBasedEncashmentDetail> tmDependentDetailsSaved = new ArrayList<>();
            for (DependentBasedDetailRequestTo dependentBasedDetailRequestTo1 : dependentBasedDetailRequestTo) {
                TmDependentBasedEncashmentDetail encashmentDetail = setDependentEncashmentDetailTo(dependentBasedDetailRequestTo1, encashment.getDependentBasedEncashmentID());
                encashmentDetail.setCreatedBy(userInfo.getUserId());
                encashmentDetail.setCreatedDate(new Date());
                encashmentDetail.setModifiedBy(userInfo.getUserId());
                encashmentDetail.setModifiedDate(new Date());
                encashmentDetail.setOrganizationId(userInfo.getOrganizationId());
                encashmentDetail.setTenantID(userInfo.getTenantId());
                tmDependentDetailsSaved.add(encashmentDetail);
            }

            transactionService.saveDependentEncashmentAndDetails(encashment, tmDependentDetailsSaved);
            response.setTmDependentBasedEncashment(encashment);
            response.setTmDependentDetailsSaved(tmDependentDetailsSaved);

            return response;
        } catch (AppRuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error saving encashment", e);
            throw new AppRuntimeException(e, "Error saving encashment");
        }
    }

    private TmDependentBasedEncashment setDependentEncashmentFromTo(DependentBasedRequestTo request) {
        TmDependentBasedEncashment tmDependentBasedEncashment = new TmDependentBasedEncashment();
        tmDependentBasedEncashment.setOrganizationId(request.getOrganizationId());
        tmDependentBasedEncashment.setConfigurationName(request.getConfigurationName());
        tmDependentBasedEncashment.setActive(request.getActive());
        tmDependentBasedEncashment.setTenantID(request.getTenantID());

        return tmDependentBasedEncashment;
    }

    private TmDependentBasedEncashmentDetail setDependentEncashmentDetailTo(DependentBasedDetailRequestTo request, Long encashmentId) {
        TmDependentBasedEncashmentDetail tmDependentBasedEncashmentDetail = new TmDependentBasedEncashmentDetail();
        tmDependentBasedEncashmentDetail.setDependentBasedEncashmentID(encashmentId);
        tmDependentBasedEncashmentDetail.setStartAge(request.getStartAge());
        tmDependentBasedEncashmentDetail.setEndAge(request.getEndAge());
        tmDependentBasedEncashmentDetail.setPercentageEncashment(request.getPercentageEncashment());

        return tmDependentBasedEncashmentDetail;
    }

    @Override
    public List<TmDependentBasedEncashment> findDependentBasedDetail(int tenantId) throws AppRuntimeException {
        try {
            List<TmDependentBasedEncashment> dependentBasedEncashmentDetailsResponse = new ArrayList<>();
            List<Object[]> dependentBasedEncashmentDetails = repository.findDependentBasedDetail(tenantId);
            if (dependentBasedEncashmentDetails != null && !dependentBasedEncashmentDetails.isEmpty()){
                dependentBasedEncashmentDetails.forEach(dependentBasedEncashmentDetail->{
                    TmDependentBasedEncashment tmDependentBasedEncashment = new TmDependentBasedEncashment();
                    tmDependentBasedEncashment.setConfigurationName(((String) dependentBasedEncashmentDetail[0]));
                    tmDependentBasedEncashment.setActive(((Boolean) dependentBasedEncashmentDetail[1]));
                    tmDependentBasedEncashment.setDependentBasedEncashmentID(((Long) dependentBasedEncashmentDetail[2]));
                    dependentBasedEncashmentDetailsResponse.add(tmDependentBasedEncashment);
                });
            }
            return dependentBasedEncashmentDetailsResponse;
        } catch (Exception e) {
            logger.error("Error finding encashment", e);
            throw new AppRuntimeException(e, "Error finding encashment");
        }
    }

    @Override
    public DependentBasedEncashmentResponseTo findEncashmentDetails(int tenantID, int organizationID, Long dependentBasedEncashmentID) throws AppRuntimeException {
        try {
            TmDependentBasedEncashment tmDependentBasedEncashment = repository.findByDependentBasedEncashmentId(tenantID, dependentBasedEncashmentID);
            if (tmDependentBasedEncashment != null) {
                List<TmDependentBasedEncashmentDetail> tmDependentBasedEncashmentDetails = repository.findDependentBasedEncashmentDetail(tenantID, dependentBasedEncashmentID);
                DependentBasedEncashmentResponseTo response = new DependentBasedEncashmentResponseTo();
                response.setActive(tmDependentBasedEncashment.getActive());
                response.setConfigurationName(tmDependentBasedEncashment.getConfigurationName());
                List<DependentBasedEncashmentDetailResponseTo> dependentBasedEncashmentDetailResponseToList = new ArrayList<>();
                if (tmDependentBasedEncashmentDetails != null && !tmDependentBasedEncashmentDetails.isEmpty()) {
                    tmDependentBasedEncashmentDetails.forEach(tmDependentBasedEncashmentDetail -> {
                        DependentBasedEncashmentDetailResponseTo dependentBasedEncashmentDetailResponseTo = new DependentBasedEncashmentDetailResponseTo();
                        dependentBasedEncashmentDetailResponseTo.setDependentBasedEncashmentId(tmDependentBasedEncashmentDetail.getDependentBasedEncashmentID());
                        dependentBasedEncashmentDetailResponseTo.setStartAge(tmDependentBasedEncashmentDetail.getStartAge());
                        dependentBasedEncashmentDetailResponseTo.setEndAge(tmDependentBasedEncashmentDetail.getEndAge());
                        dependentBasedEncashmentDetailResponseTo.setPercentageEncashment(tmDependentBasedEncashmentDetail.getPercentageEncashment());
                        dependentBasedEncashmentDetailResponseTo.setOrganizationId(tmDependentBasedEncashmentDetail.getOrganizationId());
                        dependentBasedEncashmentDetailResponseTo.setTenantID(tmDependentBasedEncashmentDetail.getTenantID());
                        dependentBasedEncashmentDetailResponseToList.add(dependentBasedEncashmentDetailResponseTo);
                    });
                    response.setDependentBasedEncashmentDetailResponseTos(dependentBasedEncashmentDetailResponseToList);
                }
                return response;
            } else {
                throw new AppRuntimeException("No Dependent Based Encashment Found");
            }
        } catch (Exception e) {
            logger.error("Error finding dependent-based encashment details", e);
            throw new AppRuntimeException(e, "Error finding dependent-based encashment details");
        }
    }

    public TmDependentBasedEncashment updateEncashment(DependentBasedRequestTo request, UserInfo userInfo) throws AppRuntimeException {
        try {
            Long id = request.getDependentBasedEncashmentID();
            if (!repository.existsById(id)) {
                throw new AppRuntimeException("Encashment not found with id " + id);
            }
            TmDependentBasedEncashment existingEncashment = repository.findById(id)
                    .orElseThrow(() -> new AppRuntimeException("Encashment not found with id " + id));

            // Update only the specified fields
            existingEncashment.setActive(request.getActive());
            existingEncashment.setModifiedDate(new Date());
            existingEncashment.setModifiedBy(userInfo.getUserId());

            // Save the updated entity
            return repository.save(existingEncashment);
        } catch (AppRuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error saving encashment", e);
            throw new AppRuntimeException(e, "Error saving encashment");
        }
    }

    private void validateDependentBasedEncashment(List<DependentBasedDetailRequestTo> dependentBasedDetailRequestTo) {
        if (dependentBasedDetailRequestTo != null && dependentBasedDetailRequestTo.isEmpty()) {
            throw new AppRuntimeException("Please Enter Some Values...");
        }

        Integer prevEndAge = 0;
        int totalPercentageEncashment = 0;

        for (int i = 0; i < dependentBasedDetailRequestTo.size(); i++) {
            DependentBasedDetailRequestTo config = dependentBasedDetailRequestTo.get(i);

            // Check if the first StartAge value is 0
            if (i == 0 && (config.getStartAge() == null || config.getStartAge().intValue() != 0)) {
                throw new AppRuntimeException("The Start Age value for the first entry must be 0.");
            }

            // Check if StartAge is non-negative and not null
            if (config.getStartAge() == null || config.getStartAge().intValue() < 0) {
                throw new AppRuntimeException("Start Age value cannot be negative or null.");
            }

            // Ensure that the Start Age is greater than 0 for subsequent entries
            if (i != 0 && config.getStartAge().intValue() == 0) {
                throw new AppRuntimeException("The Start Age value should be greater than 0 for all entries except the first.");
            }

            // Ensure that StartAge is not greater than 300
            if (config.getStartAge().intValue() > 300) {
                throw new AppRuntimeException("The value for Start Age exceeds the permissible limit");
            }

            // Check if EndAge is non-negative, not null, and not zero
            if (config.getEndAge() == null || config.getEndAge().intValue() <= 0) {
                throw new AppRuntimeException("End Age value cannot be null or zero.");
            }

            // Ensure that EndAge is not less than StartAge
            if (config.getEndAge() != null && config.getEndAge().intValue() < config.getStartAge().intValue()) {
                throw new AppRuntimeException("End Age value cannot be lesser than Start Age value.");
            }

            // Ensure that EndAge is not greater than 300
            if (config.getEndAge().intValue() > 300) {
                throw new AppRuntimeException("The value for End Age exceeds the permissible limit");
            }

            // Ensure that StartAge starts from the consecutive value of the previous EndAge
            if (config.getEndAge() != null && prevEndAge > 0 && !config.getStartAge().equals(prevEndAge + 1)) {
                throw new AppRuntimeException("Start Age value should start from the next consecutive value of the previous End Age.");
            }

            // Check if EncashmentLimit is non-negative and not null
            if (config.getPercentageEncashment() == null || config.getPercentageEncashment() < 0 || config.getPercentageEncashment() > 100) {
                throw new AppRuntimeException("Encashment percentage value cannot be null, negative, or greater than 100.");
            }

            totalPercentageEncashment += config.getPercentageEncashment();

            prevEndAge = config.getEndAge();
        }

        // Validate that total percentage encashment equals 100
        if (totalPercentageEncashment != 100) {
            throw new AppRuntimeException("Total encashment percentage must equal exactly 100.");
        }
    }
}
