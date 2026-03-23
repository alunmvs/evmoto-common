package com.evmoto.common.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.connection.balancer.RoundRobinLoadBalancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RedissonBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedissonBuilder.class);

    public static RedissonClient build(RedissonProperties redissonProperties) {
        Config config = new Config();
        try {
            config.setCodec((Codec) Class.forName(redissonProperties.getCodec()).newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        config.setTransportMode(redissonProperties.getTransportMode());
        if (redissonProperties.getThreads() != null) {
            config.setThreads(redissonProperties.getThreads());
        }
        if (redissonProperties.getNettyThreads() != null) {
            config.setNettyThreads(redissonProperties.getNettyThreads());
        }
        config.setReferenceEnabled(redissonProperties.getReferenceEnabled());
        config.setLockWatchdogTimeout(redissonProperties.getLockWatchdogTimeout());
        config.setKeepPubSubOrder(redissonProperties.getKeepPubSubOrder());
        config.setUseScriptCache(redissonProperties.getUseScriptCache());
        config.setMinCleanUpDelay(redissonProperties.getMinCleanUpDelay());
        config.setMaxCleanUpDelay(redissonProperties.getMaxCleanUpDelay());

        if (Objects.equals(redissonProperties.getModel(), Model.SINGLE)) {
            SingleServerConfig singleServerConfig = config.useSingleServer();
            buildSingle(singleServerConfig, redissonProperties);
        } else if (Objects.equals(redissonProperties.getModel(), Model.CLUSTER)) {
            ClusterServersConfig clusterServersConfig = config.useClusterServers();
            buildCluster(clusterServersConfig, redissonProperties);
        }

        return Redisson.create(config);
    }

    private static String prefixAddress(String address) {
        if (StringUtils.hasLength(address) && !address.startsWith("redis")) {
            return "redis://" + address;
        }
        return address;
    }

    private static void buildSingle(SingleServerConfig singleServerConfig, RedissonProperties redissonProperties) {
        RedissonProperties.Single single = redissonProperties.getSingle();
        if (Objects.isNull(single)) {
            throw new RuntimeException("Redisson初始化错误，单节点配置不能为空");
        }
        singleServerConfig.setAddress(prefixAddress(single.getAddress()));
        singleServerConfig.setConnectionMinimumIdleSize(single.getConnectionMinimumIdleSize());
        singleServerConfig.setConnectionPoolSize(single.getConnectionPoolSize());
        singleServerConfig.setDatabase(single.getDatabase());
        singleServerConfig.setDnsMonitoringInterval(single.getDnsMonitoringInterval());
        singleServerConfig.setSubscriptionConnectionMinimumIdleSize(single.getSubscriptionConnectionMinimumIdleSize());
        singleServerConfig.setSubscriptionConnectionPoolSize(single.getSubscriptionConnectionPoolSize());

        singleServerConfig.setClientName(redissonProperties.getClientName());
        singleServerConfig.setConnectTimeout(redissonProperties.getConnectTimeout());
        singleServerConfig.setIdleConnectionTimeout(redissonProperties.getIdleConnectionTimeout());
        singleServerConfig.setKeepAlive(redissonProperties.getKeepAlive());
        singleServerConfig.setPassword(redissonProperties.getPassword());
        singleServerConfig.setPingConnectionInterval(redissonProperties.getPingConnectionInterval());
        singleServerConfig.setRetryAttempts(redissonProperties.getRetryAttempts());
        singleServerConfig.setRetryInterval(redissonProperties.getRetryInterval());
        singleServerConfig.setSslEnableEndpointIdentification(redissonProperties.getSslEnableEndpointIdentification());
        singleServerConfig.setSslKeystore(redissonProperties.getSslKeystore());
        singleServerConfig.setSslKeystorePassword(redissonProperties.getSslKeystorePassword());
        singleServerConfig.setSslProvider(redissonProperties.getSslProvider());
        singleServerConfig.setSslTruststore(redissonProperties.getSslTruststore());
        singleServerConfig.setSslTruststorePassword(redissonProperties.getSslTruststorePassword());
        singleServerConfig.setSubscriptionsPerConnection(redissonProperties.getSubscriptionsPerConnection());
        singleServerConfig.setTcpNoDelay(redissonProperties.getTcpNoDelay());
        singleServerConfig.setTimeout(redissonProperties.getTimeout());
        LOGGER.info(">>>>>>>>>>> create single redisson");
    }

    private static void buildCluster(ClusterServersConfig clusterServersConfig, RedissonProperties redissonProperties) {
        RedissonProperties.Cluster cluster = redissonProperties.getCluster();
        if (Objects.isNull(cluster)) {
            throw new RuntimeException("Redisson初始化错误，集群配置不能为空");
        }

        List<String> nodeAddresses =
                cluster.getNodeAddresses().stream().map(RedissonBuilder::prefixAddress).collect(Collectors.toList());
        clusterServersConfig.setNodeAddresses(nodeAddresses);
        clusterServersConfig.setScanInterval(cluster.getScanInterval());

        clusterServersConfig.setSlaveConnectionPoolSize(cluster.getSlaveConnectionPoolSize());
        clusterServersConfig.setFailedSlaveReconnectionInterval(cluster.getFailedSlaveReconnectionInterval());
        clusterServersConfig.setFailedSlaveCheckInterval(cluster.getFailedSlaveCheckInterval());
        clusterServersConfig.setMasterConnectionPoolSize(cluster.getMasterConnectionPoolSize());
        clusterServersConfig.setLoadBalancer(new RoundRobinLoadBalancer());
        clusterServersConfig.setSubscriptionConnectionPoolSize(cluster.getSubscriptionConnectionPoolSize());
        clusterServersConfig.setSlaveConnectionMinimumIdleSize(cluster.getSlaveConnectionMinimumIdleSize());
        clusterServersConfig.setMasterConnectionMinimumIdleSize(cluster.getMasterConnectionMinimumIdleSize());
        clusterServersConfig.setSubscriptionConnectionMinimumIdleSize(cluster.getSubscriptionConnectionMinimumIdleSize());
        clusterServersConfig.setReadMode(cluster.getReadMode());
        clusterServersConfig.setSubscriptionMode(cluster.getSubscriptionMode());
        clusterServersConfig.setDnsMonitoringInterval(cluster.getDnsMonitoringInterval());

        clusterServersConfig.setSubscriptionsPerConnection(redissonProperties.getSubscriptionsPerConnection());
        clusterServersConfig.setPassword(redissonProperties.getPassword());
        clusterServersConfig.setUsername(redissonProperties.getUsername());
        clusterServersConfig.setRetryAttempts(redissonProperties.getRetryAttempts());
        clusterServersConfig.setRetryInterval(redissonProperties.getRetryInterval());
        clusterServersConfig.setTimeout(redissonProperties.getTimeout());
        clusterServersConfig.setClientName(redissonProperties.getClientName());
        clusterServersConfig.setConnectTimeout(redissonProperties.getConnectTimeout());
        clusterServersConfig.setIdleConnectionTimeout(redissonProperties.getIdleConnectionTimeout());
        clusterServersConfig.setSslEnableEndpointIdentification(redissonProperties.getSslEnableEndpointIdentification());
        clusterServersConfig.setSslProvider(redissonProperties.getSslProvider());
        clusterServersConfig.setSslTruststore(redissonProperties.getSslTruststore());
        clusterServersConfig.setSslTruststorePassword(redissonProperties.getSslTruststorePassword());
        clusterServersConfig.setSslKeystore(redissonProperties.getSslKeystore());
        clusterServersConfig.setSslKeystorePassword(redissonProperties.getSslKeystorePassword());
        clusterServersConfig.setPingConnectionInterval(redissonProperties.getPingConnectionInterval());
        clusterServersConfig.setKeepAlive(redissonProperties.getKeepAlive());
        clusterServersConfig.setTcpNoDelay(redissonProperties.getTcpNoDelay());
        LOGGER.info(">>>>>>>>>>> create cluster redisson");
    }
}
