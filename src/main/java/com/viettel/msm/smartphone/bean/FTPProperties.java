package com.viettel.msm.smartphone.bean;

import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FTPProperties {
    private String server;
    private String username;
    private String password;
    private int port;
    private int keepAliveTimeout;
    private boolean autoStart;
}
