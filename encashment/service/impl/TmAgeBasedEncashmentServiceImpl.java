package com.peoplestrong.timeoff.encashment.service.impl;

import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmAgeBasedEncashment;
import com.peoplestrong.timeoff.dataservice.model.encashment.TmAgeBasedEncashmentDetail;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmAgeBasedEncashmentDetailRepository;
import com.peoplestrong.timeoff.encashment.pojo.*;
import com.peoplestrong.timeoff.encashment.service.TmAgeBasedEncashmentService;
import com.peoplestrong.timeoff.encashment.utils.AppLogger;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmAgeBasedEncashmentRepository;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import com.peoplestrong.timeoff.leave.service.Impl.transaction.LeaveTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class TmAgeBasedEncashmentServiceImpl implements TmAgeBasedEncashmentService {

    private static final AppLogger logger = AppLogger.get(TmAgeBasedEncashmentServiceImpl.class);

    private final TmAgeBasedEncashmentRepository repository;

    private final TmAgeBasedEncashmentRepository tmAgeBasedEncashmentRepository;
    private final TmAgeBasedEncashmentDetailRepository encashmentDetailRepository;

    private final LeaveTransactionService transactionService;

    @Autowired
    public TmAgeBasedEncashmentServiceImpl(TmAgeBasedEncashmentRepository tmAgeBasedEncashmentRepository, TmAgeBasedEncashmentRepository repository, TmAgeBasedEncashmentDetailRepository encashmentDetailRepository, LeaveTransactionService transactionService) {
        this.repository = repository;
        this.encashmentDetailRepository = encashmentDetailRepository;
        this.tmAgeBasedEncashmentRepository = tmAgeBasedEncashmentRepository;
        this.transactionService = transactionService;
    }

    @Override
    public AgeBasedEncashmentResponseTo save(AgeBasedEncashmentRequestTo req, UserInfo userInfo) throws AppRuntimeException {
        try {
            AgeBasedEncashmentResponseTo response = new AgeBasedEncashmentResponseTo();
            TmAgeBasedEncashmentRequestTo tmAgeBasedEncashmentRequestTo = req.getAgeBasedEncashment();
            List<TmAgeBasedEncashmentDetailRequestTo> tmAgeBasedEncashmentDetailRequestTos = req.getEncashmentDetails();
            TmAgeBasedEncashment encashment = setEncashmentDataFromTO(tmAgeBasedEncashmentRequestTo);

            if (tmAgeBasedEncashmentRequestTo.getConfigurationName() == null || tmAgeBasedEncashmentRequestTo.getConfigurationName().isEmpty()) {
                throw new AppRuntimeException("Configuration Name can not be Null or Empty");
            }

            Boolean exists = repository.existsByConfigurationNameAndOrganizationId(
                    tmAgeBasedEncashmentRequestTo.getConfigurationName().trim(), userInfo.getOrganizationId()
            );

            if (exists) {
                throw new AppRuntimeException("Configuration Name already exists for this organization");
            }

            this.validateAgeBasedEncashment(tmAgeBasedEncashmentDetailRequestTos);
            encashment.setCreatedDate(new Date());
            encashment.setModifiedDate(new Date());
            encashment.setCreatedBy(userInfo.getUserId());
            encashment.setModifiedBy(userInfo.getUserId());
            encashment.setOrganizationId(userInfo.getOrganizationId());
            encashment.setTenantID(userInfo.getTenantId());

            List<TmAgeBasedEncashmentDetail> tmAgeBasedEncashmentDetails = new ArrayList<>();
            for (TmAgeBasedEncashmentDetailRequestTo detailRequestTo : tmAgeBasedEncashmentDetailRequestTos) {
                TmAgeBasedEncashmentDetail encashmentDetail = setEncashmentDetailFromTo(detailRequestTo, encashment.getAgeBasedEncashmentID());
                encashmentDetail.setCreatedDate(new Date());
                encashmentDetail.setModifiedDate(new Date());
                encashmentDetail.setCreatedBy(userInfo.getUserId());
                encashmentDetail.setModifiedBy(userInfo.getUserId());
                encashmentDetail.setTenantID(userInfo.getTenantId());
                encashmentDetail.setOrganizationId(userInfo.getOrganizationId());
                tmAgeBasedEncashmentDetails.add(encashmentDetail);
            }

            transactionService.saveEncashmentAndDetails(encashment, tmAgeBasedEncashmentDetails);
            response.setTmAgeBasedEncashment(encashment);
            response.setTmAgeBasedEncashmentDetailsSavedListinDb(tmAgeBasedEncashmentDetails);

            return response;

        } catch (AppRuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error saving encashment", e);
            throw new AppRuntimeException(e, "Error saving encashment");
        }
    }

    private TmAgeBasedEncashmentDetail setEncashmentDetailFromTo(TmAgeBasedEncashmentDetailRequestTo request, Long encashmentId) {
        TmAgeBasedEncashmentDetail tmAgeBasedEncashmentDetail = new TmAgeBasedEncashmentDetail();
        tmAgeBasedEncashmentDetail.setAgeBasedEncashmentID(encashmentId);
        tmAgeBasedEncashmentDetail.setStartAge(request.getStartAge());
        tmAgeBasedEncashmentDetail.setEndAge(request.getEndAge());
        tmAgeBasedEncashmentDetail.setEncashmentLimit(request.getEncashmentLimit());

        return tmAgeBasedEncashmentDetail;
    }

    private TmAgeBasedEncashment setEncashmentDataFromTO(TmAgeBasedEncashmentRequestTo request) {
        TmAgeBasedEncashment tmAgeBasedEncashment = new TmAgeBasedEncashment();
        tmAgeBasedEncashment.setOrganizationId(request.getOrganizationId());
        tmAgeBasedEncashment.setConfigurationName(request.getConfigurationName());
        tmAgeBasedEncashment.setActive(request.getActive());
        tmAgeBasedEncashment.setTenantID(request.getTenantID());

        return tmAgeBasedEncashment;
    }

    @Override
    public List<TmAgeBasedEncashment> findAll(int tenantId) throws AppRuntimeException {
        try {
            return repository.findAllByTenantId(tenantId);
        } catch (Exception e) {
            logger.error("Error finding encashment", e);
            throw new AppRuntimeException(e, "Error finding encashment");
        }
    }

    @Override
    public TmAgeBasedEncashmentDetailResponseTo findAgeBasedEncashmentDetails(int tenantID, int organizationID, Long ageBasedEncashmentID) throws AppRuntimeException {
        try {
            TmAgeBasedEncashment tmAgeBasedEncashment = tmAgeBasedEncashmentRepository.findByAgeBasedEncashmentId(tenantID, ageBasedEncashmentID);
            if (tmAgeBasedEncashment != null) {
                List<TmAgeBasedEncashmentDetail> tmAgeBasedEncashmentDetails = repository.findAgeBasedEncashmentDetails(tenantID, ageBasedEncashmentID);
                TmAgeBasedEncashmentDetailResponseTo response = new TmAgeBasedEncashmentDetailResponseTo();
                response.setActive(tmAgeBasedEncashment.getActive());
                response.setConfigurationName(tmAgeBasedEncashment.getConfigurationName());
                List<AgeBasedEncashmentDetailsResponse> ageBasedEncashmentDetailsResponseList = new ArrayList<>();
                if (tmAgeBasedEncashmentDetails != null && !tmAgeBasedEncashmentDetails.isEmpty()) {
                    tmAgeBasedEncashmentDetails.forEach(tmAgeBasedEncashmentDetail -> {
                        AgeBasedEncashmentDetailsResponse ageBasedEncashmentDetailsResponse = new AgeBasedEncashmentDetailsResponse();
                        ageBasedEncashmentDetailsResponse.setAgeBasedEncashmentId(tmAgeBasedEncashmentDetail.getAgeBasedEncashmentID());
                        ageBasedEncashmentDetailsResponse.setEndAge(tmAgeBasedEncashmentDetail.getEndAge());
                        ageBasedEncashmentDetailsResponse.setStartAge(tmAgeBasedEncashmentDetail.getStartAge());
                        ageBasedEncashmentDetailsResponse.setOrganizationId(tmAgeBasedEncashmentDetail.getOrganizationId());
                        ageBasedEncashmentDetailsResponse.setTenantID(tmAgeBasedEncashmentDetail.getTenantID());
                        ageBasedEncashmentDetailsResponse.setEncashmentLimit(tmAgeBasedEncashmentDetail.getEncashmentLimit());
                        ageBasedEncashmentDetailsResponseList.add(ageBasedEncashmentDetailsResponse);
                    });
                    response.setAgeBasedEncashmentDetailsResponse(ageBasedEncashmentDetailsResponseList);
                }
                return response;
            } else {
                throw new AppRuntimeException("No Age Based Encashment Found");
            }
        } catch (Exception e) {
            logger.error("Error finding age-based encashment details", e);
            throw new AppRuntimeException(e, "Error finding age-based encashment details");
        }
    }

    public TmAgeBasedEncashment update(TmAgeBasedEncashmentRequestTo request, UserInfo userInfo) throws AppRuntimeException {
        // Check if the entity exists
        try {
            Long id = request.getAgeBasedEncashmentId();
            if (!repository.existsById(id)) {
                throw new AppRuntimeException("Encashment not found with id " + id);
            }
            TmAgeBasedEncashment existingEncashment = repository.findById(id)
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

    private void validateAgeBasedEncashment(List<TmAgeBasedEncashmentDetailRequestTo> ageBasedEncashmentDetailRequestTo) {
        if (ageBasedEncashmentDetailRequestTo == null || ageBasedEncashmentDetailRequestTo.isEmpty()) {
            throw new AppRuntimeException("Please Enter Some Values...");
        }

        Integer prevEndAge = 0;

        for (int i = 0; i < ageBasedEncashmentDetailRequestTo.size(); i++) {
            TmAgeBasedEncashmentDetailRequestTo config = ageBasedEncashmentDetailRequestTo.get(i);

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
            if (config.getEncashmentLimit() == null || config.getEncashmentLimit() < 0) {
                throw new AppRuntimeException("Encashment limit cannot be null or negative.");
            }

            // New validation: Check for more than 2 decimal points in EncashmentLimit
            if (hasMoreThanTwoDecimalPoints(config.getEncashmentLimit())) {
                throw new AppRuntimeException("Encashment limit cannot have more than two decimal points.");
            }

            prevEndAge = config.getEndAge();
        }
    }

    private boolean hasMoreThanTwoDecimalPoints(Number number) {
        if (number == null) {
            return false;
        }
        String stringValue = number.toString();
        String[] parts = stringValue.split("\\.");
        return parts.length > 1 && parts[1].length() > 2;
    }

}
