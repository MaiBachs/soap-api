package com.viettel.msm.smartphone.service.smartphone;

import com.viettel.msm.smartphone.bean.CheckListBean;
import com.viettel.msm.smartphone.repository.sm.VisitPlanMapRepository;
import com.viettel.msm.smartphone.repository.sm.entity.VisitPlanMap;
import com.viettel.msm.smartphone.repository.smartphone.ItemConfigRepository;
import com.viettel.msm.smartphone.repository.smartphone.MapChannelTypeCheckListRepository;
import com.viettel.msm.smartphone.ws.GetActionPlanCheckListOfAuditorRequest;
import com.viettel.msm.smartphone.ws.GetCheckListOfAuditorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MapChannelTypeCheckListService {

    @Autowired
    private MapChannelTypeCheckListRepository mapChannelTypeCheckListRepository;
    @Autowired
    private ItemConfigRepository itemConfigRepository;
    @Autowired
    private VisitPlanMapRepository visitPlanMapRepository;

    public List<CheckListBean> getCheckListByBrIdAndChannelAndMonth(GetCheckListOfAuditorRequest request){
        List<CheckListBean> checkListBeanList = mapChannelTypeCheckListRepository.getCheckListByBrIdAndChannelAndMonth(request.getBrId(), request.getChannelTypeId());
        checkListBeanList = checkListBeanList.stream().map(cl->{
            long count = itemConfigRepository.countItemConfigByEvaluationIdAndChannelTypeId(cl.getJobId(), cl.getChannelTypeId());
            return count > 0 ? cl : null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return checkListBeanList;
    }

    public List<CheckListBean> getActionPlanCheckListByBrIdAndChannelAndMonth(GetActionPlanCheckListOfAuditorRequest request){
        List<CheckListBean> checkListBeans = mapChannelTypeCheckListRepository.getCheckListAPByBrIdAndChannelAndMonth(request.getBrId(), request.getChannelTypeId());
        List<VisitPlanMap> visitPlanMaps = visitPlanMapRepository.findVisitPlanActionPlanBybrIdAndChannelTypeId(request.getBrId(), request.getChannelTypeId());

        Set<Long> jobIds = visitPlanMaps.stream().map(VisitPlanMap::getJobId).collect(Collectors.toSet());
        List<CheckListBean> filteredCheckLists = checkListBeans.stream()
                .filter(cl -> jobIds.contains(cl.getJobId()))
                .collect(Collectors.toList());

        filteredCheckLists = filteredCheckLists.stream()
                .filter(cl -> itemConfigRepository.countItemConfigByEvaluationIdAndChannelTypeId(cl.getJobId(), cl.getChannelTypeId()) > 0)
                .collect(Collectors.toList());

        return filteredCheckLists;
    }
}
