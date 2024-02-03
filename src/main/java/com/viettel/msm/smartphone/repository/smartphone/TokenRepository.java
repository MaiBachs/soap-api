package com.viettel.msm.smartphone.repository.smartphone;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.viettel.msm.smartphone.repository.smartphone.entity.Token;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface TokenRepository extends GenericJpaRepository<Token, String>  {
    Token findFirstByTokenValue(String tokenValue);

    @Query(value = "SELECT sysdate as system_datetime FROM Dual",nativeQuery = true)
    Date getSysdate();
}
