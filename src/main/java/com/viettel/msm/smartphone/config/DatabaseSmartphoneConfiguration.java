package com.viettel.msm.smartphone.config;

import com.slyak.spring.jpa.GenericJpaRepositoryFactoryBean;
import com.slyak.spring.jpa.GenericJpaRepositoryImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author luan
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.viettel.msm.smartphone.repository.smartphone",
        repositoryBaseClass = GenericJpaRepositoryImpl.class,
        repositoryFactoryBeanClass = GenericJpaRepositoryFactoryBean.class,
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager")
@ConfigurationProperties(prefix = "spring.datasource")
public class DatabaseSmartphoneConfiguration extends BaseDatabaseConfiguration {
    @Value("${spring.datasource.encode_file}")
    private String encodeFilePath;

    @Bean(name = "dataSource")
    @Primary
    public DataSource defaultDataSource() {
        return readDatabase(encodeFilePath);
    }

    @Bean(name = "entityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean defaultEntityManagerFactory(
            final EntityManagerFactoryBuilder builder, @Qualifier("dataSource") final DataSource dataSource,
            final JpaProperties jpaProperties) {
        return builder
                .dataSource(dataSource)
                .packages("com.viettel.msm.smartphone.repository.smartphone.entity")
                .mappingResources(jpaProperties.getMappingResources().toArray(new String[0]))
                .persistenceUnit("default")
                .build();
    }

    @Bean(name = "transactionManager")
    @Primary
    public JpaTransactionManager defaultTransactionManager(
            @Qualifier("entityManagerFactory") final EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
}
