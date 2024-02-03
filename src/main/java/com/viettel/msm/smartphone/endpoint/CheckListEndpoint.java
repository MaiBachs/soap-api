package com.viettel.msm.smartphone.endpoint;

import com.viettel.msm.smartphone.Constants;
import com.viettel.msm.smartphone.MessageKey;
import com.viettel.msm.smartphone.bean.CheckListBean;
import com.viettel.msm.smartphone.bean.ItemConfigBean;
import com.viettel.msm.smartphone.repository.sm.entity.VisitPlanMap;
import com.viettel.msm.smartphone.repository.smartphone.entity.*;
import com.viettel.msm.smartphone.service.StaffService;
import com.viettel.msm.smartphone.service.VisitPlanMapService;
import com.viettel.msm.smartphone.service.smartphone.*;
import com.viettel.msm.smartphone.ws.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Endpoint
public class CheckListEndpoint extends BaseEndpoint{
    @Autowired
    private MapChannelTypeCheckListService mapChannelTypeCheckListService;
    @Autowired
    private VisitPlanMapService visitPlanMapService;
    @Autowired
    private ItemConfigService itemConfigService;
    @Autowired
    private PlanResultService planResultService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MapAuditorCheckListService mapAuditorCheckListService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ChannelTypeService channelTypeService;
    @Autowired
    private PlanService planService;
    @Autowired
    private PenaltyResultService penaltyResultService;
    @Value("${ftp.is_ssl}")
    private String ftpIsSsl;

    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "getMapAuditorCheckListsRequest")
    @ResponsePayload
    public GetListMapAuditorCheckListsResponse getMapChannelTypeChecklists(@RequestPayload GetMapAuditorCheckListsRequest request) {
        GetListMapAuditorCheckListsResponse response = new GetListMapAuditorCheckListsResponse();
        MapAuditorCheckListReturn returnType = new MapAuditorCheckListReturn();
        Locale locale = new Locale(request.getLocation());
        Token validateResult = authenticateValidate(request);
        if (validateResult == null){
            returnType.setErrorCode(Constants.TOKEN_INVALID);
            returnType.setDescription(messageSource.getMessage(MessageKey.COMMON_TOKEN_INVALID, null, locale));
            response.setReturn(returnType);
            return response;
        }
        List<MapAuditorCheckListDtos> mapAuditorCheckLists = mapAuditorCheckListService.findChannelCheckList(request.getStaffId());
        returnType.getMapAuditorCheckLists().addAll(mapAuditorCheckLists);
        response.setReturn(returnType);
        return response;
    }

    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "getCheckListOfAuditorRequest")
    @ResponsePayload
    public GetCheckListOfAuditorResponse getCheckListOfAuditor(@RequestPayload GetCheckListOfAuditorRequest request){
        GetCheckListOfAuditorResponse response = new GetCheckListOfAuditorResponse();
        CheckListOfAuditorReturn returnType = new CheckListOfAuditorReturn();
        Locale locale = new Locale(request.getLocation());
        Token validateResult = authenticateValidate(request);
        if (validateResult == null){
            returnType.setErrorCode(Constants.TOKEN_INVALID);
            returnType.setDescription(messageSource.getMessage(Constants.COMMON_TOKEN_INVALID, null, locale));
            response.setReturn(returnType);
            return response;
        }
        List<CheckListBean> checkListBeanList = mapChannelTypeCheckListService.getCheckListByBrIdAndChannelAndMonth(request);
        List<ChannelType> channelTypes = channelTypeService.getChannelTypeIdIn(planService.getChannelTypeIdByType(Constants.STATUS_CCAUDIT_IN_PLAN));
        List<VisitPlanMap> visitPlanMaps = visitPlanMapService.findVisitPlanBybrIdAndChannelId(request.getBrId(), request.getChannelId());
        List<CheckListDto> checkListDtoList = checkListBeanList.stream().map(cl ->{
            CheckListDto checkListDto = new CheckListDto();
            BeanUtils.copyProperties(cl, checkListDto);
            ChannelType channelType = channelTypes.stream().filter(ct->ct.getChannelTypeId().equals(cl.getChannelTypeId())).findFirst().orElse(null);
            checkListDto.setChannelTypeName(channelType == null ? null:channelType.getName());
            //Convert Date
            try {
                if(cl.getDateEvaluation1() != null){
                    checkListDto.setDateEvaluation1(DatatypeFactory.newInstance().newXMLGregorianCalendar(cl.getDateEvaluation1().toString()));
                }
                if(cl.getDateEvaluation2() != null){
                    checkListDto.setDateEvaluation2(DatatypeFactory.newInstance().newXMLGregorianCalendar(cl.getDateEvaluation2().toString()));
                }
                if(cl.getDateEvaluation3() != null){
                    checkListDto.setDateEvaluation3(DatatypeFactory.newInstance().newXMLGregorianCalendar(cl.getDateEvaluation3().toString()));
                }
                if(cl.getDateEvaluation4() != null){
                    checkListDto.setDateEvaluation4(DatatypeFactory.newInstance().newXMLGregorianCalendar(cl.getDateEvaluation4().toString()));
                }
            } catch (DatatypeConfigurationException e) {
                e.printStackTrace();
            }
            // check quantity  checklist
            List<VisitPlanMap> visitPlanMaps1 = visitPlanMaps.stream().filter(vpm->vpm.getJobId().equals(cl.getJobId())).collect(Collectors.toList());
            List<VisitPlanDto> visitPlanDtos = visitPlanMaps1.stream().map(vpm -> {
                VisitPlanDto visitPlanDto = new VisitPlanDto();
                BeanUtils.copyProperties(vpm, visitPlanDto);
                visitPlanDto.setStatus(vpm.getStatus());
                visitPlanDto.setDatePlan(vpm.getDatePlan().toString());
                visitPlanDto.setVisitTime(vpm.getVisitTime().toString());
                return visitPlanDto;
            }).collect(Collectors.toList());
            checkListDto.getVisitPlanList().addAll(visitPlanDtos);
            checkListDto.setQuantity(visitPlanMaps1.size());
            visitPlanMapService.checkEvaluationCalidad(visitPlanMaps1, checkListDto, cl, request);
            return checkListDto;
        }).collect(Collectors.toList());
        if(!visitPlanMapService.mapLocation(request.getChannelId(), request.getX(), request.getY(),request.getChannelTypeId())){
            returnType.setValid(false);
        }else{
            returnType.setValid(true);
        }
        returnType.getEvaluation().addAll(checkListDtoList);
        response.setReturn(returnType);
        return response;
    }

    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "listStaffsRequest")
    @ResponsePayload
    public ListStaffsResponse getStaffByShopId(@RequestPayload ListStaffsRequest request) {
        ListStaffsResponse response = new ListStaffsResponse();
        ListStaffsReturn returnType = new ListStaffsReturn();
        Locale locale = new Locale(request.getLocation());
        Token validateResult = authenticateValidate(request);
        if (validateResult == null){
            returnType.setErrorCode(Constants.TOKEN_INVALID);
            returnType.setDescription(messageSource.getMessage(MessageKey.COMMON_TOKEN_INVALID, null, locale));
            response.setReturn(returnType);
            return response;
        }
        List<Staff> list = staffService.getListStaffByChannel(request.getShopId(),request.getToken());
        List<ChannelType> channelTypes = channelTypeService.getChannelTypeIdIn(planService.getChannelTypeIdByType(Constants.STATUS_CCAUDIT_IN_PLAN));
        List<StaffDto> staffs = list.stream().map(s -> {
            StaffDto staff = new StaffDto();
            BeanUtils.copyProperties(s, staff);
            ChannelType channelType = channelTypes.stream().filter(ct->ct.getChannelTypeId().equals(s.getChannelTypeId())).findFirst().orElse(null);
            staff.setChannelTypeName(channelType == null ? null: channelType.getName());
            return staff;
        }).collect(Collectors.toList());
        returnType.getStaffs().addAll(staffs);
        response.setReturn(returnType);
        return response;
    }

    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "getCheckListOfAuditorDetailRequest")
    @ResponsePayload
    public GetCheckListOfAuditorDetailResponse getCheckListOfAuditorDetail(@RequestPayload GetCheckListOfAuditorDetailRequest request){
        GetCheckListOfAuditorDetailResponse response = new GetCheckListOfAuditorDetailResponse();
        CheckListOfAuditorDetailReturn returnType = new CheckListOfAuditorDetailReturn();
        Locale locale = new Locale(request.getLocation());
        Token validateResult = authenticateValidate(request);
        if (validateResult == null){
            returnType.setErrorCode(Constants.TOKEN_INVALID);
            returnType.setDescription(messageSource.getMessage(Constants.COMMON_TOKEN_INVALID, null, locale));
            response.setReturn(returnType);
            return response;
        }
        List<ItemConfigBean> itemConfigBeanList = itemConfigService.findItemConfigByEvaluationIdAndChannelTypeId(request);
        List<ItemConfigDto>  itemConfigDtoList = itemConfigBeanList.stream().map(ic -> {
            List<Reason>  reasonList =  reasonService.getReasonDtoByItemConfig(ic.getId());
            List<ReasonDto> reasonDtoList = reasonList.stream().map(r->{
                ReasonDto reasonDto = new ReasonDto();
                reasonDto.setReasonId(r.getReasonId());
                reasonDto.setCode(r.getCode());
                reasonDto.setName(r.getName());
                return reasonDto;
            }).collect(Collectors.toList());
            ItemConfigDto itemConfigDto = new ItemConfigDto();
            ic.setPlanResultId(0L);
            ic.setVisitPlanId(0L);
            BeanUtils.copyProperties(ic, itemConfigDto);
            itemConfigDto.getReasons().addAll(reasonDtoList);
            return itemConfigDto;
        }).collect(Collectors.toList());
        returnType.getItems().addAll(itemConfigDtoList);
        response.setReturn(returnType);
        return response;
    }

    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "getCheckListResultRequest")
    @ResponsePayload
    public GetCheckListResultResponse saveResultCheckList(@RequestPayload GetCheckListResultRequest request){
        GetCheckListResultResponse response = new GetCheckListResultResponse();
        CheckListResultReturn returnType = new CheckListResultReturn();
        Locale locale = new Locale(request.getLocation());
        Token validateResult = authenticateValidate(request);
        if (validateResult == null){
            returnType.setErrorCode(Constants.TOKEN_INVALID);
            returnType.setDescription(messageSource.getMessage(Constants.COMMON_TOKEN_INVALID, null, locale));
            response.setReturn(returnType);
            return response;
        }
        try{
            VisitPlanMap visitPlanMap = new VisitPlanMap();
            BeanUtils.copyProperties(request, visitPlanMap);
            visitPlanMap.setVisitTime(new Date());
            visitPlanMap.setDatePlan(request.getDatePlan().toGregorianCalendar().getTime());
            visitPlanMap.setIsDetail(1l);
            ResultCheckListDto resultCheckListDto = new ResultCheckListDto();
            if(StringUtils.isEmpty(request.getCheckListResultCommnet())){
                resultCheckListDto = visitPlanMapService.makeResultCheckListDto(visitPlanMap, request);
                // check location valid by feild valid or location
                if(resultCheckListDto.isValid()) {
                    visitPlanMap.setScore(resultCheckListDto.getScore());
                    VisitPlanMap visitPlanResult = visitPlanMapService.saveAuditCheckList(visitPlanMap, request, resultCheckListDto);
                    List<PlanResult> planResults = visitPlanMapService.setPlanResults(visitPlanResult, request);
                    planResultService.saveAll(planResults);
//                //<editor-fold defaultstate="collapsed" desc="Create actionplan">
                    VisitPlanMap actionPlan = null;
                    if(resultCheckListDto.isActionPlan() && request.getJobCode().equalsIgnoreCase(Constants.MINIMOS_CODE)){
                        actionPlan = visitPlanMapService.createActionPlan(visitPlanResult);
                    }else if(resultCheckListDto.isActionPlan()
                            && request.getJobCode().equalsIgnoreCase(Constants.IMAGEN_CODE)
                            && request.getQuantity() == request.getQuantityPerMonth()){
                        actionPlan = visitPlanMapService.createActionPlan(visitPlanResult);
                    }
//                //</editor-fold>
                }
            }else{
                resultCheckListDto = visitPlanMapService.rejectEvaluation(visitPlanMap, request);
                if(resultCheckListDto.isValid()){
                    visitPlanMapService.saveAuditCheckList(visitPlanMap, request, resultCheckListDto);
                }
            }
            returnType.setEvaluation(resultCheckListDto);
        }catch (Exception e){
            log.error(e.getMessage());
            returnType.setErrorCode(e.getMessage());
        }
        response.setReturn(returnType);
        return response;
    }

    public CheckListEndpoint(TokenService tokenService) {
        super(tokenService);
    }

    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "saveFileFtpRequest")
    @ResponsePayload
    public SaveFileFtpResponse saveFileFtp(@RequestPayload SaveFileFtpRequest request){
        SaveFileFtpResponse response = new SaveFileFtpResponse();
        SaveFileFtpReturn returnType = new SaveFileFtpReturn();
        Locale locale = new Locale(request.getLocation());
        Token validateResult = authenticateValidate(request);
        if (validateResult == null){
            returnType.setErrorCode(Constants.TOKEN_INVALID);
            returnType.setDescription(messageSource.getMessage(Constants.COMMON_TOKEN_INVALID, null, locale));
            response.setReturn(returnType);
            return response;
        }
        List<String> filePaths = visitPlanMapService.ConnectToUpLoadFile(request);
        returnType.getFilePath().addAll(filePaths);
        response.setReturn(returnType);
        return response;
    }

    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "getFileFtpRequest")
    @ResponsePayload
    public GetFileFtpResponse getFileFtp(@RequestPayload GetFileFtpRequest request){
        GetFileFtpResponse response = new GetFileFtpResponse();
        GetFileFtpReturn returnType = new GetFileFtpReturn();
        Locale locale = new Locale(request.getLocation());
        Token validateResult = authenticateValidate(request);
        if (validateResult == null){
            returnType.setErrorCode(Constants.TOKEN_INVALID);
            returnType.setDescription(messageSource.getMessage(Constants.COMMON_TOKEN_INVALID, null, locale));
            response.setReturn(returnType);
            return response;
        }
        returnType.setFileDto(visitPlanMapService.downloadFileToFtpServer(request.getFilePath()));
        response.setReturn(returnType);
        return response;
    }

    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "calendarRequest")
    @ResponsePayload
    public CalendarResponse getCalendar(@RequestPayload CalendarRequest request){
        CalendarResponse response = new CalendarResponse();
        CalendarReturn returnType = new CalendarReturn();
        Locale locale = new Locale(request.getLocation());
        Token validateResult = authenticateValidate(request);
        if (validateResult == null){
            returnType.setErrorCode(Constants.TOKEN_INVALID);
            returnType.setDescription(messageSource.getMessage(Constants.COMMON_TOKEN_INVALID, null, locale));
            response.setReturn(returnType);
            return response;
        }
        List<CalendarDto> calendarDtos =  mapAuditorCheckListService.getCalendar(request.getAuditorId(), request.getDate());
        returnType.getCalendarDtos().addAll(calendarDtos);
        response.setReturn(returnType);
        return response;
    }

    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "penaltyRequest")
    @ResponsePayload
    public PenaltyResponse getPenalty(@RequestPayload PenaltyRequest request){
        PenaltyResponse response = new PenaltyResponse();
        PenaltyReturn returnType = new PenaltyReturn();
        Locale locale = new Locale(request.getLocation());
        Token validateResult = authenticateValidate(request);
        if (validateResult == null){
            returnType.setErrorCode(Constants.TOKEN_INVALID);
            returnType.setDescription(messageSource.getMessage(Constants.COMMON_TOKEN_INVALID, null, locale));
            response.setReturn(returnType);
            return response;
        }
        TotalPenaltyDto totalPenaltyDto = penaltyResultService.getPenalty(request);
        returnType.setTotalPenaltyDtos(totalPenaltyDto);
        response.setReturn(returnType);
        return response;
    }
}
