package com.viettel.msm.smartphone.repository.smartphone;

import com.viettel.msm.smartphone.repository.smartphone.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Set;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findAllByShopIdIn(Set<Long> shopId);
}
