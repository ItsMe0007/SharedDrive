package com.peoplestrong.timeoff.encashment.mss.service.impl;

import com.peoplestrong.sessionmanagement.interceptor.AuthFilter;
import com.peoplestrong.sessionmanagement.to.SessionInfo;
import com.peoplestrong.timeoff.common.constant.AppConstant;
import com.peoplestrong.timeoff.common.exception.AppRuntimeException;
import com.peoplestrong.timeoff.common.exception.CountryMappingException;
import com.peoplestrong.timeoff.common.impl.pojo.SelectItemForLeaveStage;
import com.peoplestrong.timeoff.common.impl.pojo.SelectItemForLeaveType;
import com.peoplestrong.timeoff.common.impl.service.CommonService;
import com.peoplestrong.timeoff.common.util.*;
import com.peoplestrong.timeoff.dataservice.model.leave.*;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmCalenderBlockDetailRepository;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmCalenderBlockRepository;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmEmployeeEncashmentDetailRepository;
import com.peoplestrong.timeoff.dataservice.repo.encashment.TmEmployeeEncashmentRepository;
import com.peoplestrong.timeoff.dataservice.repo.leave.*;
import com.peoplestrong.timeoff.encashment.constant.EncashmentConstant;
import com.peoplestrong.timeoff.encashment.mss.pojo.*;
import com.peoplestrong.timeoff.encashment.mss.service.EncashmentMssService;
import com.peoplestrong.timeoff.encashment.pojo.CalendarBlocksTO;
import com.peoplestrong.timeoff.leave.constant.LeaveConstant;
import com.peoplestrong.timeoff.leave.flatTO.LeaveTypeTO;
import com.peoplestrong.timeoff.leave.helper.LeaveUtil;
import com.peoplestrong.timeoff.leave.pojo.UserInfo;
import com.peoplestrong.timeoff.leave.service.Impl.SysContentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EncashmentMssServiceImpl implements EncashmentMssService {
    
    private static final Logger logger = LoggerFactory.getLogger(EncashmentMssServiceImpl.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static final String LEAVE_TYPE_KEY = "LeaveTypes";
    public static final String LEAVE_STAGE_KEY = "LeaveStageKey";
    private static final long serialVersionUID = 1L;
    
    @Autowired
    CommonService commonService;
    @Autowired
    Environment environment;
    @Autowired
    EmployeeLeaveRepository employeeLeaveRepo;
    @Autowired
    TmEmployeeEncashmentRepository tmEmployeeEncashmentRepository;
    @Autowired
    TmEmployeeEncashmentDetailRepository tmEncashmentDetailRepository;
    @Autowired
    TmCalenderBlockRepository tmCalenderBlockRepository;
    @Autowired
    TmCalenderBlockDetailRepository tmCalenderBlockDetailRepository;
    @Autowired
    LeaveTypeRepository leaveTypeRepo;
    @Autowired
    SysWorkflowTypeRepository sysWorkflowTypeRepository;
    @Autowired
    SysContentServiceImpl sysContentServiceImpl;
    @Autowired
    SysWorkflowRepository sysWorkflowRepository;
    @Autowired
    SysWorkflowStageRepository sysWorkflowStageRepo;
    @Autowired
    private LeaveDurationTypeRepository leaveDurationTypeRepository;

    @Override
    public DayViewPageDTO dayViewMssPage(UserInfo userInfo, DayViewDTOin dayViewDTOin) {
        Integer dateTDI;
        DayViewPageDTO dayViewPageDTO = new DayViewPageDTO();
        List<DayViewDTO> list = null;
        try {
            Date date = new SimpleDateFormat(DateUtil.DATE_FORMAT3).parse(dayViewDTOin.getDate());
            dateTDI = DateUtil.getTimeDimensionId(date);
            setFilterMap(dayViewDTOin);
            LinkedHashMap<Integer, EmployeeInfoDTO> empFilteredMap = employeeFilter(userInfo, dayViewDTOin);


            StringBuffer isLeavefiltered = new StringBuffer("false");
            LinkedHashMap<Integer, EmployeeInfoDTO> empAndLeaveFilteredMap = leaveFilter(empFilteredMap, userInfo.getOrganizationId(), dayViewDTOin, isLeavefiltered);
            if (PSCollectionUtil.isMapNotNullOrEmpty(empAndLeaveFilteredMap)) {
                dayViewPageDTO.setRecords(empAndLeaveFilteredMap.size());
            }


            LinkedHashMap<Integer, EmployeeInfoDTO> paginatedResults = paginatedResults(empAndLeaveFilteredMap, dayViewDTOin.getOffset());

             list = dayViewMssList(paginatedResults, dateTDI, userInfo.getOrganizationId(), isLeavefiltered,
                    dayViewDTOin);
            dayViewPageDTO.setDayViewList( list);

        }catch (Exception e){
            e.printStackTrace();
        }
        return dayViewPageDTO;
    }

    @Override
    public Map<String, Object> getMssFilters(UserInfo userInfo) throws CountryMappingException {
        Map<String, Object> mssFilters = null;
        try {
            mssFilters = new HashMap<String, Object>();
            mssFilters.put(LEAVE_TYPE_KEY, getLeaveTypes(userInfo.getOrganizationId()));
            mssFilters.put(LEAVE_STAGE_KEY, getLeaveStages(userInfo.getOrganizationId(),userInfo.getUserId()));
        }
        catch (AppRuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new AppRuntimeException("Failed to load LeaveFilters");
        }
        return mssFilters;

    }

    private Object getLeaveStages(int organizationId, int userId) {
        List<SelectItemForLeaveStage> leaveStages = null;
        try {
            SysWorkflowType sysWorkflowType = sysWorkflowTypeRepository.findByWorkFlowType(LeaveConstant.LEAVE_MODULE_NAME);
            Map<String, HrContentType> hrContent = sysContentServiceImpl.getHrContentTypesBySysContentType("workflow status", 1);
            if (hrContent == null || hrContent.isEmpty()) {
                throw new AppRuntimeException("Failed to load LeaveFilters");
            }
            if (!hrContent.containsKey("Active")) {
                throw new AppRuntimeException("Failed to load LeaveFilters");
            }

            Integer typeId = hrContent.get("Active").getTypeId();
            SessionInfo sessionInfo = AuthFilter.threadLocal.get();
            Integer entityID = sessionInfo.getUserDetails().getEntityId();
            SysWorkflow sysWorkflow = sysWorkflowRepository.findByWorkflowTypeIDAndOrganizationIdAndStatusAndEntityID(sysWorkflowType.getWorkflowTypeId(), organizationId, typeId,entityID);
            List<SysWorkflowStage> sysWorkFlowStageList = sysWorkflowStageRepo.findByWorkflow(sysWorkflow.getWorkflowId());

            if (sysWorkFlowStageList != null && !sysWorkFlowStageList.isEmpty()) {
                leaveStages = new ArrayList<SelectItemForLeaveStage>();
                for (SysWorkflowStage sysWorkflowStage : sysWorkFlowStageList) {
                    SelectItemForLeaveStage selectItem = new SelectItemForLeaveStage();
                    selectItem.setLabel(sysWorkflowStage.getStageStatus());
                    selectItem.setValue(sysWorkflowStage.getWorkflowStageID());
                    selectItem.setSelected(false);
                    leaveStages.add(selectItem);
                }
                Collections.sort(leaveStages);
            }

        } catch (AppRuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new AppRuntimeException("Failed to load LeaveFilters");
        }
        return leaveStages;
    }

    private Object getLeaveTypes(int organizationId) {
        List<SelectItemForLeaveType> leaveTypes = null;
        try {
            List<TmLeaveType> tmLeaveTypeList = null;
            tmLeaveTypeList = leaveTypeRepo.findByOrganizationId(organizationId);
            if (tmLeaveTypeList != null && !tmLeaveTypeList.isEmpty()) {
                leaveTypes = new ArrayList<SelectItemForLeaveType>();
                for (TmLeaveType tmLeaveType : tmLeaveTypeList) {
                    SelectItemForLeaveType selectItem = new SelectItemForLeaveType();
                    selectItem.setLabel(tmLeaveType.getLeaveTypeCode());
                    selectItem.setValue(tmLeaveType.getLeaveTypeId());
                    selectItem.setSelected(false);
                    leaveTypes.add(selectItem);
                }
                Collections.sort(leaveTypes);
            }
        } catch (AppRuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new AppRuntimeException("Failed to load LeaveFilters");
        }
        return leaveTypes;
    }

    private List<DayViewDTO> dayViewMssList(LinkedHashMap<Integer, EmployeeInfoDTO> finalEmployeeMap, Integer dateTDI, int organizationId, StringBuffer isLeaveFiltered, DayViewDTOin dayViewDTOin) {
        // Step 1: Return empty list if the employee map is null or empty
        if (PSCollectionUtil.isMapNullOrEmpty(finalEmployeeMap)) {
            return new ArrayList<>();
        }

        // Step 4: Retrieve last encashments for the given organization and employees
        List<Object[]> lastEncashmentApplied = tmEmployeeEncashmentRepository.getLastEncashmentApplied(organizationId, finalEmployeeMap.keySet(), true);
        Map<Integer, LastEncashmentDetailsTO> encashmentDetailsMap = new HashMap<>();

        // Populate encashment details if available
        if (lastEncashmentApplied != null && !lastEncashmentApplied.isEmpty()) {
            Set<Long> encashmentIds = lastEncashmentApplied.stream()
                    .map(lastEncashment -> ((BigInteger) lastEncashment[1]) == null ? 0L : ((BigInteger) lastEncashment[1]).longValue())
                    .collect(Collectors.toSet());
            Set<Integer> leaveTypeIds = lastEncashmentApplied.stream()
                    .map(lastEncashment-> ((Integer) lastEncashment[9]))
                    .collect(Collectors.toSet());
            Map<Long,List<Object[]>> encashmentDetailMap = new HashMap<>();
            Map<Long,String> calendarBlockDetailMap = new HashMap<>();
            Map<Integer,Map<String,Object>> encashmentTypeMap = sysContentServiceImpl.getContentMapForEncashment(EncashmentConstant.ENCASHMENT_CONTENT_CATEGORY,organizationId);
            Map<Integer,String> leaveTypeMap = new HashMap<>();
            Integer hourlyWeightage = leaveDurationTypeRepository.getHourlyWightageByOrganization(organizationId,true,LeaveConstant.LEAVE_DURATION_FULL);
            if (leaveTypeIds != null && !leaveTypeIds.isEmpty()){
                List<LeaveTypeTO> leaveTypes = leaveTypeRepo.getLeavecodeAndDescriptionByLeaveTypeId(organizationId,leaveTypeIds);
                if (leaveTypes != null && !leaveTypes.isEmpty()){
                    leaveTypes.forEach(leaveTypeTO -> {
                        leaveTypeMap.put(leaveTypeTO.getLeaveTypeId(),leaveTypeTO.getLeaveTypeCode());
                    });
                }
            }
            if (encashmentIds != null && !encashmentIds.isEmpty()){
                List<Object[]> encashmentDetails = tmEncashmentDetailRepository.getTmEmployeeEncashmentDetails(encashmentIds,organizationId);
                if (encashmentDetails != null && !encashmentDetails.isEmpty()){
                    encashmentDetails.forEach(encashmentDetail->{
                        List<Object[]> encashmentDetailList = new ArrayList<>();
                        if (encashmentDetailMap.containsKey(((Long) encashmentDetail[0])))
                            encashmentDetailList.addAll(encashmentDetailMap.get(((Long) encashmentDetail[0])));
                        encashmentDetailList.add(encashmentDetail);
                        encashmentDetailMap.put(((Long) encashmentDetail[0]),encashmentDetailList);
                    });
                    Set<Long> calendarBlockDetailIds = encashmentDetails.stream().map(encashmentDetail-> ((Long) encashmentDetail[1])).collect(Collectors.toSet());
                    if (calendarBlockDetailIds != null && !calendarBlockDetailIds.isEmpty()){
                        List<Object[]> calendarBlockDetails = tmCalenderBlockDetailRepository.findCalendarBlockDetailsById(calendarBlockDetailIds,organizationId);
                        if (calendarBlockDetails != null && !calendarBlockDetails.isEmpty()){
                            calendarBlockDetails.forEach(calendarBlockDetail-> calendarBlockDetailMap.put(((Long) calendarBlockDetail[0]), ((String) calendarBlockDetail[1])));
                        }
                    }
                }
            }
            if (encashmentDetailMap != null && calendarBlockDetailMap != null && !encashmentDetailMap.isEmpty() && !calendarBlockDetailMap.isEmpty() && encashmentTypeMap != null && !encashmentTypeMap.isEmpty() && leaveTypeMap != null && !leaveTypeMap.isEmpty()){
                lastEncashmentApplied.forEach(lastEncashment->{
                    LastEncashmentDetailsTO lastEncashmentDetailsTO = processLastEncashmentApplied(lastEncashment,encashmentDetailMap,calendarBlockDetailMap,encashmentTypeMap,leaveTypeMap,hourlyWeightage);
                    encashmentDetailsMap.put(((Integer) lastEncashment[0]),lastEncashmentDetailsTO);
                });
            }
        }
        
        // Step 6: Prepare the final list to return
         List<DayViewDTO> dayViewToList = new ArrayList<>();

        for (Map.Entry<Integer, EmployeeInfoDTO> employeeInfoDTOEntry : finalEmployeeMap.entrySet()) {
            EmployeeInfoDTO emp = employeeInfoDTOEntry.getValue();
            if (emp == null) continue;

            DayViewDTO dayViewDTO = new DayViewDTO();

            // Populate employee details, checking for nulls
            dayViewDTO.setEmployeeID(emp.getEmployeeID());
            dayViewDTO.setEmployeeCode(emp.getEmployeeCode());
            dayViewDTO.setEmployeeName(emp.getEmployeeName());
            dayViewDTO.setEmploymentStatus(emp.getEmploymentStatus());
            dayViewDTO.setProfilePic(emp.getProfilePic());
            dayViewDTO.setEmploymentType(emp.getEmploymentType());
            dayViewDTO.setConfirmationDate(emp.getConfirmationDate());
            dayViewDTO.setSelected(false);
            dayViewDTO.setEntityID(emp.getEntityID());

            // Set last encashment details if available
            LastEncashmentDetailsTO encashmentDetails = encashmentDetailsMap.get(emp.getEmployeeID());
            if (encashmentDetails != null) {
                LastEncashmentApplied lastEncashment = new LastEncashmentApplied();
                lastEncashment.setApplicationDate(encashmentDetails.getApplicationDate());
                lastEncashment.setApplicationDateID(encashmentDetails.getApplicationDateId());
                lastEncashment.setEncashmentCount(encashmentDetails.getEncashmentCount());
                lastEncashment.setAppliedDateString(encashmentDetails.getAppliedDateString());
                lastEncashment.setEncashmentType(encashmentDetails.getEncashmentType());
                lastEncashment.setStatus(encashmentDetails.getStatus());
                lastEncashment.setLeaveType(encashmentDetails.getLeaveType());
                lastEncashment.setLeaveTypeID(encashmentDetails.getLeaveTypeId());
                lastEncashment.setCalendarBlocksTOS(encashmentDetails.getCalendarBlocksTOList());
                lastEncashment.setEncashmentID(encashmentDetails.getEncashmentId());
                lastEncashment.setSysEncashmentType(encashmentDetails.getSysEncashmentType());
                lastEncashment.setSysEncashmentTypeId(encashmentDetails.getSysEncashmentTypeId());
                lastEncashment.setEncashmentContentId(encashmentDetails.getEncashmentContentId());
                lastEncashment.setEncashmentCountInDays(encashmentDetails.getEncashmentCountInDays());
                lastEncashment.setEncashmentCountInHrs(encashmentDetails.getEncashmentCountInHrs() == null ? 0 :encashmentDetails.getEncashmentCountInHrs().intValue());
                lastEncashment.setEncashmentCount(encashmentDetails.getEncashmentCount());
                lastEncashment.setHourlyWeightage(encashmentDetails.getHourlyWeightage());
                dayViewDTO.setLastEncashmentApplied(lastEncashment);
            }
            // Add the populated DayViewDTO to the list
            dayViewToList.add(dayViewDTO);
        }

        return dayViewToList;
    }


        private LinkedHashMap<Integer, EmployeeInfoDTO> paginatedResults(LinkedHashMap<Integer, EmployeeInfoDTO> empFilteredMap, Integer offset) {

        if (PSCollectionUtil.isMapNullOrEmpty(empFilteredMap)) {
            return empFilteredMap;
        }
        LinkedHashMap<Integer, EmployeeInfoDTO> paginatedResults = new LinkedHashMap<Integer, EmployeeInfoDTO>();
        for (Map.Entry<Integer, EmployeeInfoDTO> entry : empFilteredMap.entrySet()) {
            if (offset == 0) {
                break;
            } else {
                paginatedResults.put(entry.getKey(), entry.getValue());
                offset--;
            }
        }

        return paginatedResults;
    }

    private LinkedHashMap<Integer, EmployeeInfoDTO> leaveFilter(LinkedHashMap<Integer, EmployeeInfoDTO> empFilteredMap, int organizationId, DayViewDTOin dayViewDTOin, StringBuffer isLeavefiltered) {
        if (PSCollectionUtil.isMapNullOrEmpty(dayViewDTOin.getFilterby())) {
            return empFilteredMap;
        }

        LinkedHashMap<Integer, EmployeeInfoDTO> employees = null;
        if (PSCollectionUtil.isMapNotNullOrEmpty(dayViewDTOin.getFilterby())) {

            List<Integer> leaveTypeIdList = getLeaveTypeFilterValues(dayViewDTOin);
            List<Integer> leaveStageIdList = getLeaveStageFilterValues(dayViewDTOin);

            List<Object[]> records = null;
            if (PSCollectionUtil.isNotNullOrEmpty(leaveTypeIdList)
                    || PSCollectionUtil.isNotNullOrEmpty(leaveStageIdList)) {
                isLeavefiltered.delete(0, isLeavefiltered.length());
                isLeavefiltered.append("true");
                if (PSCollectionUtil.isNotNullOrEmpty(leaveTypeIdList)
                        && PSCollectionUtil.isNotNullOrEmpty(leaveStageIdList)) {
                    // Fetch filtered results on basis of LeaveTypeId and LeaveStageId
                    records = employeeLeaveRepo.applyLeaveFilterWithLeaveTypeAndStageIds(organizationId, leaveTypeIdList,
                            leaveStageIdList, Boolean.TRUE);
                } else if (PSCollectionUtil.isNotNullOrEmpty(leaveTypeIdList)) {
                    // Fetch filtered results on basis of LeaveTypeId
                    records = employeeLeaveRepo.applyLeaveFilterWithLeaveTypeIds(organizationId, leaveTypeIdList, Boolean.TRUE);
                } else if (PSCollectionUtil.isNotNullOrEmpty(leaveStageIdList)) {
                    // Fetch filtered results on basis of LeaveStageId
                    records = employeeLeaveRepo.applyLeaveFilterWithStageIds(organizationId, leaveStageIdList, Boolean.TRUE);
                }

                LinkedHashMap<Integer, EmployeeInfoDTO> employees2 = new LinkedHashMap<Integer, EmployeeInfoDTO>();

                for (Object[] record : records) {

                    Integer employeeId = (Integer) record[0];
                    Long leaveId = (Long) record[1];
                    if (empFilteredMap.containsKey(employeeId)) {
                        EmployeeInfoDTO employeeInfoDTO = empFilteredMap.get(employeeId);
                        Set<Long> leaveIds = employeeInfoDTO.getLeaveIds();
                        if (leaveIds == null) {
                            leaveIds = new HashSet<Long>();
                            employeeInfoDTO.setLeaveIds(leaveIds);
                        }
                        leaveIds.add(leaveId);
                        employees2.put(employeeId, employeeInfoDTO);
                    }
                    employees = employees2;
                }
            } else {
                employees = empFilteredMap;
            }

        } else {
            employees = empFilteredMap;
        }

        return employees;
    }

    private List<Integer> getLeaveStageFilterValues(DayViewDTOin dayViewDTOin) {
        List<Integer> leaveStageIdList = null;
        if (PSCollectionUtil.isMapNotNullOrEmpty(dayViewDTOin.getFilterby())) {
            Map<String, List<Integer>> filterby = dayViewDTOin.getFilterby();
            List<Integer> leaveStageArray = filterby.get("leavestages".toLowerCase());
            if (PSCollectionUtil.isNotNullOrEmpty(leaveStageArray)) {
                leaveStageIdList = new ArrayList<Integer>();
                for (Integer leaveStage : leaveStageArray) {
                    leaveStageIdList.add(leaveStage);
                }
            }

        }
        return leaveStageIdList;
    }


    private List<Integer> getLeaveTypeFilterValues(DayViewDTOin dayViewDTOin) {
        List<Integer> leaveTypeIdList = null;
        if (PSCollectionUtil.isMapNotNullOrEmpty(dayViewDTOin.getFilterby())) {
            Map<String, List<Integer>> filterby = dayViewDTOin.getFilterby();
            List<Integer> leaveTypeArray = filterby.get("leavetypes".toLowerCase());
            if (PSCollectionUtil.isNotNullOrEmpty(leaveTypeArray)) {
                leaveTypeIdList = new ArrayList<Integer>();
                for (Integer leaveType : leaveTypeArray) {
                    leaveTypeIdList.add(leaveType);
                }
            }

        }
        return leaveTypeIdList;
    }



    private LinkedHashMap<Integer, EmployeeInfoDTO> employeeFilter(UserInfo userInfo, DayViewDTOin dayViewDTOin)throws Exception {
        StringBuffer filterString = new StringBuffer();
        List<Long> countryList = null;
        String filterFeild = null;
        LinkedHashMap<Integer, EmployeeInfoDTO> map = new LinkedHashMap<>();
        Map<String, List<Integer>> filters = dayViewDTOin.getFilterby();
        List<Integer> managerRoles = filters.get("ManagerRoles".toLowerCase());
        List<Integer> employmentTypesIds = filters.get("EmploymentTypes".toLowerCase());
        List<Integer> employmentStatusIds = filters.get("EmploymentStatus".toLowerCase());
        List<Integer> designationsIds = filters.get("Designations".toLowerCase());
        List<Integer> countryIDs = filters.get("CountryList".toLowerCase());
        List<Integer> entityIDs = filters.get("entityList".toLowerCase());

        if (countryIDs != null && countryIDs.size() > 0) {
            if (countryIDs.size() > 1) {
                throw new Exception("Some error occurred at System level;");
            }
            Integer selectCountryID = countryIDs.get(0);
            if (selectCountryID != 0) {
                boolean exists = commonService.checkIfCountryExistsForEmployee(selectCountryID, userInfo.getUserId(), userInfo.getTenantId());
                if (!exists) {
                    throw new Exception("User does not have rights for the country");
                }
                countryList = new ArrayList<>();
                for (Integer id : countryIDs) {
                    countryList.add(Long.valueOf(id));
                }
            }
        }

        filterString = addFilter(employmentTypesIds, "employmentTypeId", filterString);
        filterString = addFilter(employmentStatusIds, "employmentStatusId", filterString);
        filterString = addFilter(designationsIds, "designationId", filterString);
        if (PSCollectionUtil.isNotNullOrEmpty(managerRoles)) {
            addFilter(managerRoles, "selectedRoleIds", filterString);
        }
        filterFeild = filterString.toString();
        String serverName = environment.getProperty(AppConstant.HOSTNAME);
        String apiUrl = AppConstant.SERVER_PROTOCOL + serverName + "/service/employees";
        EmpGraphDTOi inputTo = new EmpGraphDTOi();
        RestUtil<EmpMiniDTOList> restUtil = new RestUtil<>();
        inputTo.setUserId(userInfo.getUserId());
        inputTo.setOrganizationId(userInfo.getOrganizationId());
        inputTo.setTenantId(userInfo.getTenantId());
        inputTo.setFields(
                "employeeId,employeeCode,employeeName,employmentStatus,employmentType,confirmationDate,profilePic");
        inputTo.setOffset(99999);
        inputTo.setFilters(filterFeild);
        inputTo.setGsearchText(dayViewDTOin.getGsearch());
        inputTo.setSort(dayViewDTOin.getSortby());
        inputTo.setFormName("MssEncashmentView");
        inputTo.setCountryIDs(countryList);
        inputTo.setEntityIDs(entityIDs);

        logger.info("TIMEOFF_TO_WORKLIFE_MSS_REQUEST_PAYLOAD ==> " + Utils.mapToJson(inputTo));
        EmpMiniDTOList list = restUtil.postHttpsV2(apiUrl,inputTo,EmpMiniDTOList.class,serverName);
        logger.info("TIMEOFF_TO_WORKLIFE_MSS_RESPONSE ==> " + Utils.mapToJson(list));
        list.getEmployees().forEach((r -> {
            if (r.getEntityID() != null && entityIDs.contains(r.getEntityID())) {
                map.put(r.getEmployeeId(),
                        new EmployeeInfoDTO(r.getEmployeeId(), r.getEmployeeName(), r.getEmployeeCode(),
                                r.getEmploymentStatus(), r.getProfilePic(), r.getEmploymentType(),
                                r.getConfirmationDate(),r.getEntityID()));
            }
        }));
        return map;
    }
    private StringBuffer addFilter(List<Integer> values, String jsonKey, StringBuffer filterString) {
        if (PSCollectionUtil.isNotNullOrEmpty(values)) {
            if (StringUtil.nonEmptyCheck(filterString.toString())) {
                filterString.append("&&");
            }
            filterString.append(jsonKey + "=");
            filterString.append(StringUtil.convertListToString(values));
        }
        return filterString;
    }

    private void setFilterMap(DayViewDTOin dayViewDTOin) {
        if(PSCollectionUtil.isNullOrEmpty(dayViewDTOin.getFilterby2())){
            return ;
        }

        List<EGenericFilter> eGenericFilters = dayViewDTOin.getFilterby2();

        if (PSCollectionUtil.isNullOrEmpty(eGenericFilters)) {
            return;
        }

        Map<String, List<Integer>> filterby = new HashMap<String, List<Integer>>();
        for (EGenericFilter genericFilter : eGenericFilters) {
            filterby.put(genericFilter.getFilterName().toLowerCase(), genericFilter.getValueList());
        }
        dayViewDTOin.setFilterby(filterby);
    }

    private LastEncashmentDetailsTO processLastEncashmentApplied(Object[] lastEncashment, Map<Long,List<Object[]>> encashmentDetailMap, Map<Long,String> calendarBlockMap, Map<Integer,Map<String,Object>> encashmentTypeMap, Map<Integer,String> leaveTypeMap, Integer hourlyWeightage){
        LastEncashmentDetailsTO lastEncashmentDetailsTO = new LastEncashmentDetailsTO();
        List<Object[]> encashmentDetails = encashmentDetailMap.get(((BigInteger) lastEncashment[1]) == null ? 0L : ((BigInteger) lastEncashment[1]).longValue());
        lastEncashmentDetailsTO.setEncashmentId(((BigInteger) lastEncashment[1]) == null ? 0L : ((BigInteger) lastEncashment[1]).longValue());
        lastEncashmentDetailsTO.setApplicationDate(((Date) lastEncashment[5]));
        lastEncashmentDetailsTO.setApplicationDateId(((Integer) lastEncashment[2]));
        lastEncashmentDetailsTO.setAppliedDateString(sdf.format(lastEncashment[5]));
        lastEncashmentDetailsTO.setEncashmentContentId(((Integer) lastEncashment[8]));
        lastEncashmentDetailsTO.setEncashmentType(((String) encashmentTypeMap.get(((Integer) lastEncashment[8])).get("hrContentType")));
        lastEncashmentDetailsTO.setSysEncashmentTypeId(((Integer) encashmentTypeMap.get(((Integer) lastEncashment[8])).get("sysContentTypeId")));
        lastEncashmentDetailsTO.setSysEncashmentType(((String) encashmentTypeMap.get(((Integer) lastEncashment[8])).get("sysContentType")));
        lastEncashmentDetailsTO.setLeaveTypeId(((Integer) lastEncashment[9]));
        lastEncashmentDetailsTO.setLeaveType(leaveTypeMap.get(((Integer) lastEncashment[9])));
        lastEncashmentDetailsTO.setStatus(((String) lastEncashment[6]));
        if (lastEncashment[7] == null || ((Short) lastEncashment[7]) <= 0){
            if (hourlyWeightage == null || hourlyWeightage <= 0)
                lastEncashmentDetailsTO.setHourlyWeightage(((short) 0));
            else lastEncashmentDetailsTO.setHourlyWeightage(hourlyWeightage.shortValue());
        } else lastEncashmentDetailsTO.setHourlyWeightage(((Short) lastEncashment[7]));
        lastEncashmentDetailsTO.setEncashmentCountInDays(((Integer) lastEncashment[3]) == null ? 0 : ((Integer) lastEncashment[3]));
        lastEncashmentDetailsTO.setEncashmentCountInHrs(((Short) lastEncashment[4]) == null ? 0 : ((Short) lastEncashment[4]));
        lastEncashmentDetailsTO.setEncashmentCount(LeaveUtil.quotaCalculation(lastEncashmentDetailsTO.getEncashmentCountInDays(),lastEncashmentDetailsTO.getEncashmentCountInHrs().intValue(),lastEncashmentDetailsTO.getHourlyWeightage().intValue()));
        List<CalendarBlocksTO> calendarBlocksTOList = new ArrayList<>();
        if (encashmentDetails != null && !encashmentDetails.isEmpty()){
            encashmentDetails.forEach(encashmentDetail->{
                CalendarBlocksTO calendarBlocksTO = new CalendarBlocksTO();
                calendarBlocksTO.setCalendarBlockId(((Long) encashmentDetail[1]));
                calendarBlocksTO.setCalendarBlock(calendarBlockMap.get(((Long) encashmentDetail[1])));
                calendarBlocksTO.setCalendarGroupId(((Long) encashmentDetail[3]));
                calendarBlocksTO.setEncashmentTypeId(((Long) encashmentDetail[2]));
                calendarBlocksTOList.add(calendarBlocksTO);
            });
            lastEncashmentDetailsTO.setCalendarBlocksTOList(calendarBlocksTOList.isEmpty() ? null : calendarBlocksTOList);
        }
        return lastEncashmentDetailsTO;
    }
}
