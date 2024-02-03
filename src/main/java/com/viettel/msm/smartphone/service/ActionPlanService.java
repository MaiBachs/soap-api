package com.viettel.msm.smartphone.service;

import com.viettel.msm.smartphone.Constants;
import com.viettel.msm.smartphone.bean.ActionPlanChannelBean;
import com.viettel.msm.smartphone.repository.sm.ActionPlanRepository;
import com.viettel.msm.smartphone.repository.sm.entity.VisitPlanMap;
import com.viettel.msm.smartphone.repository.smartphone.JobRepository;
import com.viettel.msm.smartphone.repository.smartphone.entity.ChannelType;
import com.viettel.msm.smartphone.repository.smartphone.entity.Job;
import com.viettel.msm.smartphone.service.smartphone.ChannelTypeService;
import com.viettel.msm.smartphone.service.smartphone.PlanService;
import com.viettel.msm.smartphone.service.smartphone.TokenService;
import com.viettel.msm.smartphone.ws.ActionPlanChannelDto;
import com.viettel.msm.smartphone.ws.MapActionPlanChannelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActionPlanService {

    @Autowired
    private ActionPlanRepository actionPlanRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ChannelTypeService channelTypeService;
    @Autowired
    private PlanService planService;
    @Autowired
    private JobRepository jobRepository;

    public List<VisitPlanMap> getActionPlan(Long branchId, Long channelTypeId, Long zonalId, Long pdvId, String tokenReq){
        try {
//            Token token = tokenService.getToken(tokenReq);
//            tokenService.validateToken(token);
            List<VisitPlanMap> result = actionPlanRepository.findByBranchIdAndChannelTypeIdIAndAuditIdnMonth(branchId, channelTypeId, zonalId, pdvId);
            result = result.stream().map(r->{
                Job job = jobRepository.findById(r.getJobId()).orElse(null);
                r.setJobName(job != null ? job.getName() : null);
                return r;
            }).collect(Collectors.toList());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<MapActionPlanChannelDto> findActionPlanChannel(Long zonalId){
        List<ActionPlanChannelBean> actionPlanChannelBeans = actionPlanRepository.findActionPlanChannelInMonth(zonalId);
        List<ActionPlanChannelDto> actionPlanChannelDtos = actionPlanChannelBeans.stream().map(apc -> {
            ActionPlanChannelDto actionPlanChannelDto = new ActionPlanChannelDto();
            actionPlanChannelDto.setBranchId(apc.getBranchId());
            actionPlanChannelDto.setPdvCode(apc.getPdvCode());
            actionPlanChannelDto.setPdvId(apc.getPdvId());
            actionPlanChannelDto.setChannelTypeId(apc.getChannelTypeId());
            return actionPlanChannelDto;
        }).collect(Collectors.toList());

        List<ChannelType> channelTypes = channelTypeService.getChannelTypeIdIn(planService.getChannelTypeIdByType(Constants.STATUS_CCAUDIT_IN_PLAN));
        List<MapActionPlanChannelDto> mapActionPlanChannelDtos  = channelTypes.stream().map(ct->{
            MapActionPlanChannelDto mapActionPlanChannelDto = new MapActionPlanChannelDto();
            mapActionPlanChannelDto.setChannelTypeId(ct.getChannelTypeId());
            mapActionPlanChannelDto.setChannelTypeName(ct.getName());
            List<ActionPlanChannelDto> actionPlanChannelDtos1 = actionPlanChannelDtos.stream().filter(acp->ct.getChannelTypeId().equals(acp.getChannelTypeId())).collect(Collectors.toList());
            mapActionPlanChannelDto.getActionPlans().addAll(actionPlanChannelDtos1);
            return mapActionPlanChannelDto;
        }).collect(Collectors.toList());

        return mapActionPlanChannelDtos;
    }

}
