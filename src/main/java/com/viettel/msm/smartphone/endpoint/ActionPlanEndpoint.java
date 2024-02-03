package com.viettel.msm.smartphone.endpoint;

import com.viettel.msm.smartphone.Constants;
import com.viettel.msm.smartphone.MessageKey;
import com.viettel.msm.smartphone.bean.CheckListBean;
import com.viettel.msm.smartphone.bean.ItemConfigBean;
import com.viettel.msm.smartphone.repository.sm.entity.VisitPlanMap;
import com.viettel.msm.smartphone.repository.smartphone.entity.*;
import com.viettel.msm.smartphone.service.ActionPlanService;
import com.viettel.msm.smartphone.service.VisitPlanMapService;
import com.viettel.msm.smartphone.service.smartphone.*;
import com.viettel.msm.smartphone.utils.DateUtil;
import com.viettel.msm.smartphone.ws.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Endpoint
public class ActionPlanEndpoint extends BaseEndpoint {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    public ActionPlanEndpoint(TokenService tokenService) {
        super(tokenService);
    }

    @Autowired
    private ActionPlanService actionPlanService;
    @Autowired
    private PlanService planService;
    @Autowired
    private MapAuditorCheckListService mapAuditorCheckListService;
    @Autowired
    private AppParamsService appParamsService;
    @Autowired
    private ChannelTypeService channelTypeService;
    @Autowired
    private VisitPlanMapService visitPlanMapService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private MapChannelTypeCheckListService mapChannelTypeCheckListService;
    @Autowired
    private ItemConfigService itemConfigService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private PlanResultService planResultService;

    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "filActionPlanRequest")
    @ResponsePayload
    public FilActionPlanResponse filActionPlan(@RequestPayload FilActionPlanRequest request) {
        FilActionPlanResponse response = new FilActionPlanResponse();
        FilActionPlanReturn returnType = new FilActionPlanReturn();
        Locale locale = new Locale(request.getLocation());
        Token validateResult = authenticateValidate(request);
        if (validateResult == null) {
            returnType.setErrorCode(Constants.TOKEN_INVALID);
            returnType.setDescription(messageSource.getMessage(Constants.COMMON_TOKEN_INVALID, null, locale));
            response.setReturn(returnType);
            return response;
        }
        FilActionPlanDto actionPlanDto = new FilActionPlanDto();
        List<MapAuditorCheckList> mapAuditorCheckLists = mapAuditorCheckListService.findByAuditorId(request.getAuditorId());

        List<Long> ids = planService.getChannelTypeIdByType(Constants.STATUS_CCAUDIT_IN_PLAN);
        List<ChannelType> channelTypeList = channelTypeService.getChannelTypeIdIn(ids);
        List<AppParams> appParams = appParamsService.findByType(Constants.ACTION_PLAN_STATUS);
        // map with dto and ingore duplicate
        List<ChannelDto> channelDtos = mapAuditorCheckLists
                .stream()
                .map(macl -> {
                    ChannelDto channelDto = new ChannelDto();
                    Shop shop = shopService.findById(macl.getShopId());
                    channelDto.setChannelId(shop.getShopId());
                    channelDto.setChannelCode(shop.getShopCode());
                    return channelDto;
                })
                .collect(Collectors.toMap(channelDto -> channelDto.getChannelId(), channelDto -> channelDto, (existing, replacement) -> existing))
                .values()
                .stream()
                .collect(Collectors.toList());

        List<ChannelTypeDto> channelTypeDtos = channelTypeList.stream().map(ct -> {
            ChannelTypeDto channelTypeDto = new ChannelTypeDto();
            channelTypeDto.setChannelTypeId(ct.getChannelTypeId());
            channelTypeDto.setChannelTypeName(ct.getName());
            return channelTypeDto;
        }).collect(Collectors.toList());
        List<StatusDto> statusDtos = appParams.stream().map(ap -> {
            StatusDto statusDto = new StatusDto();
            statusDto.setStatusCode(ap.getCode());
            statusDto.setStatusName(ap.getValue());
            return statusDto;
        }).collect(Collectors.toList());

        Set<ChannelDto> test = new HashSet<>();
        actionPlanDto.getChannelDto().addAll(channelDtos);
        actionPlanDto.getChannelTypeDto().addAll(channelTypeDtos);
        actionPlanDto.getStatusDto().addAll(statusDtos);
        returnType.setFilActionPlanReturn(actionPlanDto);
        response.setReturn(returnType);

        return response;
    }

    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "searchActionPlanRequest")
    @ResponsePayload
    public ResultSearchActionPlanResponse searchActionPlan(@RequestPayload SearchActionPlanRequest request) throws ParseException {
        ResultSearchActionPlanResponse response = new ResultSearchActionPlanResponse();
        ResultSearchActionPlanReturn returnType = new ResultSearchActionPlanReturn();
        Locale locale = new Locale(request.getLocation());
        Token validateResult = authenticateValidate(request);
        if (validateResult == null) {
            returnType.setErrorCode(Constants.TOKEN_INVALID);
            returnType.setDescription(messageSource.getMessage(Constants.COMMON_TOKEN_INVALID, null, locale));
            response.setReturn(returnType);
            return response;
        }
        List<Long> ids = planService.getChannelTypeIdByType(Constants.STATUS_CCAUDIT_IN_PLAN);
        List<ChannelType> channelTypes = channelTypeService.getChannelTypeIdIn(ids);

        List<VisitPlanMap> visitPlanMaps = visitPlanMapService.findActionPlan(request);
        List<ActionPlanDto> actionPlanDtos = visitPlanMaps.stream().map(vpm -> {
            ActionPlanDto actionPlanDto = new ActionPlanDto();
            BeanUtils.copyProperties(vpm, actionPlanDto);
            ChannelType channelType = channelTypes.stream().filter(ct -> ct.getChannelTypeId().equals(vpm.getChannelTypeId())).findFirst().orElse(null);
            actionPlanDto.setChannelTypeName(channelType.getName());
            try {
                if (vpm.getDatePlan() != null) {
                    actionPlanDto.setDatePlan(DatatypeFactory.newInstance().newXMLGregorianCalendar(DateUtil.dbUpdateDateTime2String(vpm.getDatePlan()).split(" ")[0]));
                }
                if (vpm.getVisitTime() != null) {
                    actionPlanDto.setVisitTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(DateUtil.dbUpdateDateTime2String(vpm.getVisitTime()).split(" ")[0]));
                }
                if (vpm.getCreatedDate() != null) {
                    actionPlanDto.setCreatedDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(DateUtil.dbUpdateDateTime2String(vpm.getCreatedDate()).split(" ")[0]));
                }
                if (vpm.getUpdatedDate() != null) {
                    actionPlanDto.setUpdatedDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(DateUtil.dbUpdateDateTime2String(vpm.getUpdatedDate()).split(" ")[0]));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return actionPlanDto;
        }).collect(Collectors.toList());
        returnType.getResultSearchPlanReturn().addAll(actionPlanDtos);
        response.setReturn(returnType);
        return response;
    }

    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "scheduleActionPlanRequest")
    @ResponsePayload
    public ScheduleActionPlanResponse scheduleActionPlan(@RequestPayload ScheduleActionPlanRequest request) {
        List<AppParams> appParams = appParamsService.findByType(Constants.ACTION_PLAN_STATUS);
        ScheduleActionPlanResponse response = new ScheduleActionPlanResponse();
        ScheduleActionPlanReturn returnType = new ScheduleActionPlanReturn();
        Locale locale = new Locale(request.getLocation());
        Token validateResult = authenticateValidate(request);
        if (validateResult == null) {
            returnType.setErrorCode(Constants.TOKEN_INVALID);
            returnType.setDescription(messageSource.getMessage(Constants.COMMON_TOKEN_INVALID, null, locale));
            response.setReturn(returnType);
            return response;
        }
        ActionPlanDto actionPlanDto = request.getActionPlanDto();
        VisitPlanMap actionPlan = visitPlanMapService.findById(actionPlanDto.getId());
        actionPlan.setUpdatedDate(new Date());
        actionPlan.setCheckListResultCommnet(actionPlanDto.getCheckListResultCommnet());
        try {
            if (actionPlanDto.getDatePlan() != null) {
                actionPlan.setDatePlan(actionPlanDto.getDatePlan().toGregorianCalendar().getTime());
            }
            if (Constants.ACTION_PLAN_STATUS_NEW.equals(actionPlan.getStatus())) {
                actionPlan.setStatus(Constants.ACTION_PLAN_STATUS_IN_PROCESS);
                AppParams appParams1 = appParams.stream().filter(ap->Objects.equals(ap.getCode(), Constants.ACTION_PLAN_STATUS_IN_PROCESS.toString())).findFirst().orElse(null);
                actionPlan.setStatusName(appParams1.getType());
            } else if (Constants.ACTION_PLAN_STATUS_IN_PROCESS.equals(actionPlan.getStatus())) {
                actionPlan.setStatus(Constants.ACTION_PLAN_STATUS_REQUEST_UPDATE);
                AppParams appParams1 = appParams.stream().filter(ap->Objects.equals(ap.getCode(), Constants.ACTION_PLAN_STATUS_REQUEST_UPDATE.toString())).findFirst().orElse(null);
                actionPlan.setStatusName(appParams1.getType());
            }
            visitPlanMapService.save(actionPlan);
            actionPlanDto.setStatus(actionPlan.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
        returnType.setActionPlanDto(actionPlanDto);
        response.setReturn(returnType);
        return response;
    }

    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "actionPlanChannelRequest")
    @ResponsePayload
    public ListActionPlansChannelResponse actionPlansChannel(@RequestPayload ActionPlanChannelRequest request) {
        ListActionPlansChannelResponse response = new ListActionPlansChannelResponse();
        ListActionPlansChannelReturn returnType = new ListActionPlansChannelReturn();
        Locale locale = new Locale(request.getLocation());
        Token validateResult = authenticateValidate(request);
        if (validateResult == null) {
            returnType.setErrorCode(Constants.TOKEN_INVALID);
            returnType.setDescription(messageSource.getMessage(MessageKey.COMMON_TOKEN_INVALID, null, locale));
            response.setReturn(returnType);
            return response;
        }
        List<MapActionPlanChannelDto> mapActionPlanChannelDtos = actionPlanService.findActionPlanChannel(request.getZonalId());
        returnType.getMapActionPlans().addAll(mapActionPlanChannelDtos);
        response.setReturn(returnType);
        return response;
    }

    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "actionPlanDetailRequest")
    @ResponsePayload
    public ListActionPlansDetailResponse actionPlanDetailRequest(@RequestPayload ActionPlanDetailRequest request) {
        ListActionPlansDetailResponse response = new ListActionPlansDetailResponse();
        ListActionPlansDetailReturn returnType = new ListActionPlansDetailReturn();
        Locale locale = new Locale(request.getLocation());
        Token validateResult = authenticateValidate(request);
        if (validateResult == null) {
            returnType.setErrorCode(Constants.TOKEN_INVALID);
            returnType.setDescription(messageSource.getMessage(MessageKey.COMMON_TOKEN_INVALID, null, locale));
            response.setReturn(returnType);
            return response;
        }
        List<VisitPlanMap> list = actionPlanService.getActionPlan(request.getBranchId(), request.getChannelTypeId(), request.getZonalId(), request.getPdvId(), request.getToken());
        List<ActionPlanDto> actionPlans = list.stream().map(s -> {
            ActionPlanDto actionPlanDto = new ActionPlanDto();
            try {
                if (s.getDatePlan() != null) {
                    actionPlanDto.setDatePlan(DatatypeFactory.newInstance().newXMLGregorianCalendar(DateUtil.dbUpdateDateTime2String(s.getDatePlan()).split(" ")[0]));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            BeanUtils.copyProperties(s, actionPlanDto);
            return actionPlanDto;
        }).collect(Collectors.toList());
        returnType.getActionPlans().addAll(actionPlans);
        response.setReturn(returnType);
        return response;
    }

    //cc-audit -v2
    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "getMapAuditorActionPlanCheckListsRequest")
    @ResponsePayload
    public GetListMapAuditorCheckListsResponse getMapChannelTypeActionPlanChecklists(@RequestPayload GetMapAuditorActionPlanCheckListsRequest request) {
        GetListMapAuditorCheckListsResponse response = new GetListMapAuditorCheckListsResponse();
        MapAuditorCheckListReturn returnType = new MapAuditorCheckListReturn();
        Locale locale = new Locale(request.getLocation());
        Token validateResult = authenticateValidate(request);
        if (validateResult == null) {
            returnType.setErrorCode(Constants.TOKEN_INVALID);
            returnType.setDescription(messageSource.getMessage(MessageKey.COMMON_TOKEN_INVALID, null, locale));
            response.setReturn(returnType);
            return response;
        }
        List<MapAuditorCheckListDtos> mapAuditorCheckLists = mapAuditorCheckListService.findActionPlanCheckList(request.getStaffId());
        returnType.getMapAuditorCheckLists().addAll(mapAuditorCheckLists);
        response.setReturn(returnType);
        return response;
    }

    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "getActionPlanCheckListOfAuditorRequest")
    @ResponsePayload
    public GetCheckListOfAuditorResponse getActionPlanCheckListOfAuditor(@RequestPayload GetActionPlanCheckListOfAuditorRequest request) {
        GetCheckListOfAuditorResponse response = new GetCheckListOfAuditorResponse();
        CheckListOfAuditorReturn returnType = new CheckListOfAuditorReturn();
        Locale locale = new Locale(request.getLocation());
        Token validateResult = authenticateValidate(request);
        if (validateResult == null) {
            returnType.setErrorCode(Constants.TOKEN_INVALID);
            returnType.setDescription(messageSource.getMessage(Constants.COMMON_TOKEN_INVALID, null, locale));
            response.setReturn(returnType);
            return response;
        }
        List<CheckListBean> checkListBeanList = mapChannelTypeCheckListService.getActionPlanCheckListByBrIdAndChannelAndMonth(request);
        List<ChannelType> channelTypes = channelTypeService.getChannelTypeIdIn(planService.getChannelTypeIdByType(Constants.STATUS_CCAUDIT_IN_PLAN));
        List<VisitPlanMap> visitPlanMaps = visitPlanMapService.findActionPlanBybrIdAndChannelId(request.getBrId(), request.getChannelId());
        List<CheckListDto> checkListDtoList = checkListBeanList.stream().map(cl -> {
            Long countActionPlan = visitPlanMapService.countActionPlanBybrIdAndChannelId(request.getBrId(), request.getChannelId());
            cl.setQuantityPerMonth(countActionPlan);
            CheckListDto checkListDto = new CheckListDto();
            BeanUtils.copyProperties(cl, checkListDto);
            ChannelType channelType = channelTypes.stream().filter(ct -> ct.getChannelTypeId().equals(cl.getChannelTypeId())).findFirst().orElse(null);
            checkListDto.setChannelTypeName(channelType == null ? null : channelType.getName());
            //Convert Date
            try {
                if (cl.getDateEvaluation1() != null) {
                    checkListDto.setDateEvaluation1(DatatypeFactory.newInstance().newXMLGregorianCalendar(cl.getDateEvaluation1().toString()));
                }
                if (cl.getDateEvaluation2() != null) {
                    checkListDto.setDateEvaluation2(DatatypeFactory.newInstance().newXMLGregorianCalendar(cl.getDateEvaluation2().toString()));
                }
                if (cl.getDateEvaluation3() != null) {
                    checkListDto.setDateEvaluation3(DatatypeFactory.newInstance().newXMLGregorianCalendar(cl.getDateEvaluation3().toString()));
                }
                if (cl.getDateEvaluation4() != null) {
                    checkListDto.setDateEvaluation4(DatatypeFactory.newInstance().newXMLGregorianCalendar(cl.getDateEvaluation4().toString()));
                }
            } catch (DatatypeConfigurationException e) {
                e.printStackTrace();
            }
            // check quantity

            List<VisitPlanMap> visitPlanMaps1 = visitPlanMaps.stream().filter(vpm -> vpm.getJobId().equals(cl.getJobId())).collect(Collectors.toList());
            List<VisitPlanDto> visitPlanDtos = visitPlanMaps1.stream().map(vpm -> {
                VisitPlanDto visitPlanDto = new VisitPlanDto();
                BeanUtils.copyProperties(vpm, visitPlanDto);
                visitPlanDto.setStatus(vpm.getStatus());
                visitPlanDto.setDatePlan(vpm.getDatePlan().toString());
//                visitPlanDto.setVisitTime(vpm.getVisitTime().toString());
                return visitPlanDto;
            }).collect(Collectors.toList());
            checkListDto.getVisitPlanList().addAll(visitPlanDtos);
            checkListDto.setQuantity(visitPlanMaps1.size());
            visitPlanMapService.checkActionPlanOfEvaluationCalidad(visitPlanMaps1, checkListDto, cl, request);

            return checkListDto;
        }).collect(Collectors.toList());
        if (!visitPlanMapService.mapLocation(request.getChannelId(), request.getX(), request.getY(), request.getChannelTypeId())) {
            returnType.setValid(false);
        } else {
            returnType.setValid(true);
        }
        returnType.getEvaluation().addAll(checkListDtoList);
        response.setReturn(returnType);
        return response;
    }

    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "getCheckListACOfAuditorDetailRequest")
    @ResponsePayload
    public GetCheckListOfAuditorDetailResponse getActionPlanCheckListOfAuditorDetail(@RequestPayload GetCheckListACOfAuditorDetailRequest request){
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
        List<ItemConfigBean> itemConfigBeanList = itemConfigService.findItemConfigAcByEvaluationIdAndChannelTypeId(request);
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
            BeanUtils.copyProperties(ic, itemConfigDto);
            itemConfigDto.getReasons().addAll(reasonDtoList);
            return itemConfigDto;
        }).collect(Collectors.toList());
        returnType.getItems().addAll(itemConfigDtoList);
        response.setReturn(returnType);
        return response;
    }

    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "getCheckListACResultRequest")
    @ResponsePayload
    public GetCheckListResultResponse saveResultACCheckList(@RequestPayload GetCheckListACResultRequest request){
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
            List<CheckListACItemDto> checkListACItemDtos = request.getCheckListItem();
            //update Evaluation and plan result
            for(CheckListACItemDto checkListACItemDto1: checkListACItemDtos){
                VisitPlanMap visitPlanMap = visitPlanMapService.findById(checkListACItemDto1.getVisitPlanId());
                List<PlanResult> planResultEIsOKs = planResultService.getListPlanResultEIsOkList(visitPlanMap.getId());

                    PlanResult planResult = planResultService.findById(checkListACItemDto1.getPlanResultId());
                    BeanUtils.copyProperties(checkListACItemDto1, planResult);
                    if (planResult.getReasonId() == 0l) {
                        planResult.setReasonId(null);
                    } else {
                        planResult.setReasonId(checkListACItemDto1.getReasonId());
                    }
                    planResult.setItemConfigId(checkListACItemDto1.getId());
                    planResult.setStatus(1l);
                    planResult.setCreatedDate(new Date());
                    planResult.setLastUpdate(new Date());
                    planResult.setLatitude(checkListACItemDto1.getX());
                    planResult.setLongitude(checkListACItemDto1.getY());
                    planResult.setFilePath(checkListACItemDto1.getFilePath());
                planResultService.save(planResult);

                List<CheckListACItemDto> plCVIt = planResultEIsOKs.stream().map(pr -> {
                    CheckListACItemDto checkListACItemDto = new CheckListACItemDto();
                    checkListACItemDto.setPlanResultId(pr.getPlanResultId());
                    checkListACItemDto.setResult(pr.getResult());
                    checkListACItemDto.setPercent(pr.getPercent());
                    return checkListACItemDto;
                }).collect(Collectors.toList());
                visitPlanMapService.makeUpdateResultACCheckListDto(visitPlanMap, request, plCVIt);

                //create planResult of actionPlan
                VisitPlanMap actionPlan = visitPlanMapService.findACByParentId(checkListACItemDto1.getVisitPlanId());
                BeanUtils.copyProperties(request, actionPlan);
                actionPlan.setVisitTime(new Date());
                actionPlan.setDatePlan(request.getDatePlan().toGregorianCalendar().getTime());
                actionPlan.setIsDetail(1l);
                ResultCheckListDto resultCheckListDto = visitPlanMapService.makeResultACCheckListDto(actionPlan, request);

                if (resultCheckListDto.isValid()) {
                    actionPlan.setScore(resultCheckListDto.getScore());
                    VisitPlanMap visitPlanResult = visitPlanMapService.saveAuditACCheckList(actionPlan, request, resultCheckListDto);
                    List<PlanResult> planResults = visitPlanMapService.setACPlanResults(visitPlanResult, request);
                    planResultService.saveAll(planResults);
                }
                returnType.setEvaluation(resultCheckListDto);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            returnType.setErrorCode(e.getMessage());
        }
        response.setReturn(returnType);
        return response;
    }
}
