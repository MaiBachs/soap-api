package com.viettel.msm.smartphone.service.smartphone;

import com.viettel.msm.smartphone.repository.smartphone.ShopRepository;
import com.viettel.msm.smartphone.repository.smartphone.entity.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopService {
    @Autowired
    private ShopRepository shopRepository;

    public Shop findById(Long shopId){
        return shopRepository.findById(shopId).orElse(null);
    }
}
