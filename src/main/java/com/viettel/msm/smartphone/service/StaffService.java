package com.viettel.msm.smartphone.service;

import com.viettel.msm.smartphone.repository.smartphone.StaffRepository;
import com.viettel.msm.smartphone.repository.smartphone.entity.Staff;
import com.viettel.msm.smartphone.repository.smartphone.entity.Token;
import com.viettel.msm.smartphone.service.smartphone.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private TokenService tokenService;
    public List<Staff> getListStaffByChannel(Long shopId, String tokenReq){
        try {
            Token token = tokenService.getToken(tokenReq);
            tokenService.validateToken(token);
            List<Staff> result = staffRepository.getStaffByShopId(shopId);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
