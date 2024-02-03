package com.viettel.msm.smartphone.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author luan
 */
@Configuration
@ConfigurationProperties(prefix = "spring.vsa.datasource")
public class DatabaseVSAConfiguration extends BaseDatabaseConfiguration {
    @Value("${spring.vsa.datasource.encode_file}")
    private String encodeFilePath;

    @Bean(name = "vsaDataSource")
    public DataSource vsaDataSource() {
        return readDatabase(encodeFilePath);

    }

    @Bean(name = "vsaSessionFactory")
    public LocalSessionFactoryBean vsaSessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(vsaDataSource());
        sessionFactory.setPackagesToScan(
                new String[]{"com.bitel.repository.vsa.entity"});
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
        return hibernateProperties;
    }
}
