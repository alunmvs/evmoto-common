package com.evmoto.common.redis;

import lombok.Data;
import org.redisson.config.ReadMode;
import org.redisson.config.SslProvider;
import org.redisson.config.SubscriptionMode;
import org.redisson.config.TransportMode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = RedissonConstants.REDISSON_PREFIX)
public class RedissonProperties {

    private boolean enable;
    private String codec = "org.redisson.codec.JsonJacksonCodec";
    private Integer threads;
    private Integer nettyThreads;
    private TransportMode transportMode = TransportMode.NIO;

    //公共参数
    private Integer idleConnectionTimeout = 10000;
    private Integer pingTimeout = 1000;
    private Integer connectTimeout = 10000;
    private Integer timeout = 3000;
    private Integer retryAttempts = 3;
    private Integer retryInterval = 1500;
    private String password;
    private String username;
    private Integer subscriptionsPerConnection = 5;
    private String clientName;
    private Boolean sslEnableEndpointIdentification = true;
    private SslProvider sslProvider = SslProvider.JDK;
    private URL sslTruststore;
    private String sslTruststorePassword;
    private URL sslKeystore;
    private String sslKeystorePassword;
    private Integer pingConnectionInterval = 0;
    private Boolean keepAlive = false;
    private Boolean tcpNoDelay = false;

    private Boolean referenceEnabled = true;
    private Long lockWatchdogTimeout = 30000L;
    private Boolean keepPubSubOrder = true;
    private Boolean useScriptCache = false;
    private Integer minCleanUpDelay = 5;
    private Integer maxCleanUpDelay = 1800;

    //等待加锁超时时间 -1一直等待
    private Long attemptTimeout = 10000L;

    //数据缓存时间 默认30分钟
    private Long dataValidTime = 1000 * 60 * 30L;
    //命名空间，所有缓存的key地址
    private String namespace;
    //配置模式
    private Model model;

    private Single single;

    private Cluster cluster;


    /**
     * 单节点模式
     */
    @Data
    public static class Single {
        private String address;
        private Integer subscriptionConnectionMinimumIdleSize = 1;
        private Integer subscriptionConnectionPoolSize = 50;
        private Integer connectionMinimumIdleSize = 32;
        private Integer connectionPoolSize = 64;
        private Integer database = 0;
        private Long dnsMonitoringInterval = 5000L;
    }

    /**
     * 集群模式
     */
    @Data
    public static class Cluster {
        private List<String> nodeAddresses = new ArrayList<>();
        private int scanInterval = 5000;
        private int slaveConnectionMinimumIdleSize = 24;
        private int slaveConnectionPoolSize = 64;
        private int failedSlaveReconnectionInterval = 3000;
        private int failedSlaveCheckInterval = 180000;
        private int masterConnectionMinimumIdleSize = 24;
        private int masterConnectionPoolSize = 64;
        private ReadMode readMode = ReadMode.SLAVE;
        private SubscriptionMode subscriptionMode = SubscriptionMode.MASTER;
        private int subscriptionConnectionMinimumIdleSize = 1;
        private int subscriptionConnectionPoolSize = 50;
        private long dnsMonitoringInterval = 5000;
    }
}
