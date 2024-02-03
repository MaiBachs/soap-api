package com.viettel.msm.smartphone.service.smartphone;

import com.viettel.msm.smartphone.bean.ItemConfigBean;
import com.viettel.msm.smartphone.repository.sm.VisitPlanMapRepository;
import com.viettel.msm.smartphone.repository.sm.entity.VisitPlanMap;
import com.viettel.msm.smartphone.repository.smartphone.ItemConfigRepository;
import com.viettel.msm.smartphone.ws.GetCheckListACOfAuditorDetailRequest;
import com.viettel.msm.smartphone.ws.GetCheckListOfAuditorDetailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemConfigService {
    @Autowired
    private ItemConfigRepository itemConfigRepository;

    @Autowired
    private VisitPlanMapRepository visitPlanMapRepository;

    public List<ItemConfigBean> findItemConfigByEvaluationIdAndChannelTypeId(GetCheckListOfAuditorDetailRequest request){
        return itemConfigRepository.findItemConfigByEvaluationIdAndChannelTypeId(request.getEvaluationId(), request.getChannelTypeId());
    }

    public List<ItemConfigBean> findItemConfigAcByEvaluationIdAndChannelTypeId(GetCheckListACOfAuditorDetailRequest request){
        List<Long> visitPlanMaps = visitPlanMapRepository.findVPMOfActionPlanCheckList(request.getAuditorId());
        return itemConfigRepository.findItemConfigActionPlanByEvaluationIdAndChannelTypeId(request.getEvaluationId(), request.getChannelTypeId(), visitPlanMaps);
    }
}
