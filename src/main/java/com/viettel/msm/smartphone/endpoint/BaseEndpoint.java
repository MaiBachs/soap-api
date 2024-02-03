package com.viettel.msm.smartphone.endpoint;

import com.viettel.msm.smartphone.repository.smartphone.entity.Token;
import com.viettel.msm.smartphone.service.smartphone.TokenService;
import com.viettel.msm.smartphone.ws.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@Data
@AllArgsConstructor
public class BaseEndpoint {
    private TokenService tokenService;
    protected Token authenticateValidate(BaseRequest request){
        String token = request.getToken();
        Token tokenEntity = null;
        log.info("Token Request:", token);
        if (StringUtils.isEmpty(token)){
            return null;
        }else {
            tokenEntity = tokenService.getToken(token);
//            String validateResult = tokenService.validateToken(tokenEntity);
//            if (StringUtils.isEmpty(validateResult)){
//                return null;
//            }
        }
        return tokenEntity;
    }
}
