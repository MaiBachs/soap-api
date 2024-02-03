package com.viettel.msm.smartphone.config;

import com.slyak.spring.jpa.GenericJpaRepositoryFactoryBean;
import com.slyak.spring.jpa.GenericJpaRepositoryImpl;
import com.viettel.common.util.EncryptDecryptUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author luan
 */
@Configuration
@ConfigurationProperties(prefix = "spring.sm.datasource")
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "smEntityManager",
        transactionManagerRef = "smTransactionManager",
        basePackages = "com.viettel.msm.smartphone.repository.sm"
)
public class DatabaseSmConfiguration extends BaseDatabaseConfiguration {
    @Value("${spring.sm.datasource.encode_file}")
    private String encodeFilePath;

    @Bean(name = "smDataSource")
    public DataSource smartphoneDataSource() {
        return readDatabase(encodeFilePath);

    }

    @Bean(name = "smEntityManager")
    public LocalContainerEntityManagerFactoryBean smartphoneEntityManager(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(smartphoneDataSource())
                .packages("com.viettel.msm.smartphone.repository.sm.entity")
                .persistenceUnit("smPU")
                .build();
    }

    @Bean(name = "smTransactionManager")
    public PlatformTransactionManager smartphoneTransactionManager(@Qualifier("smEntityManager") EntityManagerFactory smartphoneEntityManager) {
        return new JpaTransactionManager(smartphoneEntityManager);
    }
}
