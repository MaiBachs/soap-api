package com.viettel.msm.smartphone.config;

import com.viettel.msm.smartphone.Constants;
import com.viettel.msm.smartphone.intercepter.GlobalEndpointInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.List;

@EnableWs
@Configuration
public class WebserviceConfig extends WsConfigurerAdapter {
    @Autowired
    private GlobalEndpointInterceptor globalEndpointInterceptor;

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        // register global interceptor
        interceptors.add(globalEndpointInterceptor);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(servlet);
//        registrationBean.addUrlMappings("/ws/*");
        registrationBean.addUrlMappings("/ws","/ws/surveys.wsdl","/ws/checkList.wsdl","/ws/manageActionPlan.wsdl");
        return registrationBean;
    }

    @Bean(name = "surveys")
    public DefaultWsdl11Definition surveysWsdl11Definition(XsdSchema surveysSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("surveysPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace(Constants.URI_BASE);
        wsdl11Definition.setSchema(surveysSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema surveysSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/survey.xsd"));
    }

    @Bean(name = "base")
    public DefaultWsdl11Definition baseWsdl11Definition(XsdSchema baseSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("basePort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace(Constants.URI_BASE);
        wsdl11Definition.setSchema(baseSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema baseSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/base.xsd"));
    }

    @Bean(name = "checkList")
    public DefaultWsdl11Definition checkListWsdl11Definition(XsdSchema checkListSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("checkListPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace(Constants.URI_BASE);
        wsdl11Definition.setSchema(checkListSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema checkListSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/checkList.xsd"));
    }

    @Bean(name = "manageActionPlan")
    public DefaultWsdl11Definition manageActionPlanWsdl11Definition(XsdSchema manageActionPlanSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("manageActionPlanPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace(Constants.URI_BASE);
        wsdl11Definition.setSchema(manageActionPlanSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema manageActionPlanSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/manageActionPlan.xsd"));
    }
}
