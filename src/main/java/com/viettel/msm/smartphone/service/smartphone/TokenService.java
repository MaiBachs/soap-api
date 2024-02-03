package com.viettel.msm.smartphone.service.smartphone;

import com.viettel.msm.smartphone.repository.smartphone.TokenRepository;
import com.viettel.msm.smartphone.repository.smartphone.entity.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class TokenService {
    @Value("${tokenTimeOut:300000}")
    private Long tokenTimeOut;
    @Value("${tokenLifeTime:600000}")
    private Long tokenLifeTime;
    @Autowired
    private TokenRepository tokenRepository;

    public String validateToken(Token token) {
        try {
            if (token == null) {
                return "";
            }
            Date sysdate = tokenRepository.getSysdate();
            Long currentTime = sysdate.getTime();
            //Token timeout
            if (currentTime - token.getLastRequest().getTime() > tokenTimeOut) {
                return "";
            }
            //Neu da qua thoi gian cho phep ton tai, ReloadToken
            log.error("currentTime - token.getCreateTime().getTime()" + (currentTime - token.getCreateTime().getTime()));
            if (currentTime - token.getCreateTime().getTime() > tokenLifeTime) {
                return "";
            } else {
                token.setLastRequest(sysdate);
                tokenRepository.save(token);
                return token.getTokenValue();
            }
        } catch (Exception e) {
            log.error("validateToken", e);
        }
        return "";
    }

    public Token getToken(String tokenValue) {
        return tokenRepository.findFirstByTokenValue(tokenValue);
    }

}
