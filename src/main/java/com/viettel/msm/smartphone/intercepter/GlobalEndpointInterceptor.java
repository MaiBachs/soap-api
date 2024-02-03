package com.viettel.msm.smartphone.intercepter;

import com.viettel.msm.smartphone.service.smartphone.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

@Slf4j
@Component
public class GlobalEndpointInterceptor implements EndpointInterceptor {
    private static final String SOAP_ENV_NAMESPACE = "http://www.viettel.com/msm/smartphone/ws";
    private static final String PREFERRED_PREFIX = "S";
    @Autowired
    private Jaxb2Marshaller jaxb2Marshaller;
    @Autowired
    private TokenService tokenService;

    @Override
    public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
        log.info("Handle BeerEndpoint Request");
//        SaajSoapMessage saajSoapMessage = (SaajSoapMessage) messageContext.getRequest();
//
//        SoapBody requestBody = saajSoapMessage.getSoapBody();
//        Object obj = jaxb2Marshaller.unmarshal(requestBody.getPayloadSource());
//        if (obj instanceof BaseRequestType){
//            BaseRequestType request = (BaseRequestType) obj;
//            String token = request.getToken();
//            log.info("Token Request:", token);
//            if (StringUtils.isEmpty(token)){
//                return false;
//            }else {
//                Token tokenEntity = tokenService.getToken(token);
//                String validateResult = tokenService.validateToken(tokenEntity);
//                if (StringUtils.isEmpty(validateResult)){
//                    return false;
//                }
//            }
//        }
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
        log.info("Handle BeerEndpoint Response");
        SaajSoapMessage saajSoapMessage = (SaajSoapMessage) messageContext.getResponse();
        alterSoapEnvelope(saajSoapMessage);
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception {
        log.info("Handle BeerEndpoint Exceptions");
        SaajSoapMessage saajSoapMessage = (SaajSoapMessage) messageContext.getResponse();
        alterSoapEnvelope(saajSoapMessage);
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) throws Exception {
        log.info("Handle BeerEndpoint After Completion");
    }
    private void alterSoapEnvelope(SaajSoapMessage soapResponse) {
        try {
            SOAPMessage soapMessage = soapResponse.getSaajMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            SOAPHeader header = soapMessage.getSOAPHeader();
            SOAPBody body = soapMessage.getSOAPBody();
            SOAPFault fault = body.getFault();
            envelope.removeNamespaceDeclaration(envelope.getPrefix());
            envelope.addNamespaceDeclaration(PREFERRED_PREFIX, SOAP_ENV_NAMESPACE);
            envelope.setPrefix(PREFERRED_PREFIX);
            header.setPrefix(PREFERRED_PREFIX);
            body.setPrefix(PREFERRED_PREFIX);
            if (fault != null) {
                fault.setPrefix(PREFERRED_PREFIX);
            }
        } catch (SOAPException e) {
            e.printStackTrace();
        }
    }
}
