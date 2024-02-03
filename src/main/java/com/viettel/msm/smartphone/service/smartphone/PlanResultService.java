package com.viettel.msm.smartphone.service.smartphone;

import com.viettel.msm.smartphone.repository.smartphone.PlanResultRepository;
import com.viettel.msm.smartphone.repository.smartphone.entity.PlanResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanResultService {
    @Autowired
    private PlanResultRepository planResultRepository;

    public List<PlanResult> saveAll(List<PlanResult> planResults){
        return planResultRepository.saveAll(planResults);
    }

    public List<PlanResult> getListPlanResultEIsOkList(Long visitPlanId){
        return planResultRepository.findPLIsOkByVisitPlanId(visitPlanId);
    }

    public PlanResult findById(Long visitPlanId){
        return planResultRepository.findById(visitPlanId).orElse(null);
    }

    public PlanResult save(PlanResult planResult){
        return planResultRepository.save(planResult);
    }
}
