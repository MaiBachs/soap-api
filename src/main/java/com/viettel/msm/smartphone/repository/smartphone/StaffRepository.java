package com.viettel.msm.smartphone.repository.smartphone;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.viettel.msm.smartphone.repository.smartphone.entity.Staff;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StaffRepository extends GenericJpaRepository<Staff, Long> {
    @Query("SELECT s FROM Staff s WHERE s.shopId = :shopId AND s.status = 1")
    List<Staff> getStaffByShopId(Long shopId);

    @Query("SELECT COUNT(s) FROM Staff s WHERE s.shopId = :shopId AND s.status = 1")
    long getCountStaffOfShop(Long shopId);
}
