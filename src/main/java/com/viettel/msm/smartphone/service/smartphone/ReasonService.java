package com.viettel.msm.smartphone.service.smartphone;

import com.viettel.msm.smartphone.repository.smartphone.ReasonRepository;
import com.viettel.msm.smartphone.repository.smartphone.entity.Reason;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReasonService {
    @Autowired
    private ReasonRepository reasonRepository;

    public Reason findByName(String name){
        return reasonRepository.findByName(name);
    }

    public List<Reason> getReasonDtoByItemConfig(Long itemConfigId){
        return reasonRepository.getReasonDtoByItemConfig(itemConfigId);
    }
}
