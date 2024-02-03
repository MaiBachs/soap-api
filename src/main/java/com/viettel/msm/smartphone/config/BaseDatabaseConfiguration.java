package com.viettel.msm.smartphone.config;

import com.viettel.common.util.EncryptDecryptUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luan
 */
public class BaseDatabaseConfiguration extends HikariConfig {
    public DataSource readDatabase(String encodeFilePath) {
        HikariConfig hikariConfig = new HikariConfig();
        try {
            Map<String, String> config = new HashMap<>();
            String decryptString = EncryptDecryptUtils.decryptFile(encodeFilePath);
            String[] properties = decryptString.split("\r\n");
            for (String property : properties) {
                String[] temp = property.split("=", 2);
                if (temp.length == 2) {
                    String key = temp[0];
                    String value = temp[1];
                    config.put(key, value);
                }
            }
            hikariConfig.setJdbcUrl(config.get("hibernate.connection.url"));
            hikariConfig.setDriverClassName(this.getDriverClassName());
            hikariConfig.setUsername(config.get("hibernate.connection.username"));
            hikariConfig.setPassword(config.get("hibernate.connection.password"));
            hikariConfig.setConnectionTimeout(this.getConnectionTimeout());
            hikariConfig.setIdleTimeout(this.getIdleTimeout());
            hikariConfig.setMaxLifetime(this.getMaxLifetime());
            hikariConfig.setMinimumIdle(this.getMinimumIdle());
            hikariConfig.setMaximumPoolSize(this.getMaximumPoolSize());
        } catch (Exception e) {
            hikariConfig = this;
        }
        return new HikariDataSource(hikariConfig);

    }
}
