package cn.liondance.liondataignite;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterState;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lifecycle.LifecycleBean;
import org.apache.ignite.lifecycle.LifecycleEventType;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * @author sunwei
 */
@Slf4j
@Data
@Configuration
public class IgniteConfig {


    @Bean
    public static Ignite igniteClient() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        // The node will be started as a client node.
        cfg.setClientMode(true);
        cfg.setClusterStateOnStart(ClusterState.ACTIVE_READ_ONLY);
        // Classes of custom Java logic will be transferred over the wire from this app.
        cfg.setPeerClassLoadingEnabled(true);
        // Setting up an IP Finder to ensure the client can locate the servers.
        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));
        cfg.setLifecycleBeans(new MyLifecycleBean());
        // Ignite实现了AutoCloseable接口，可以使用try-with-resource语句来自动关闭。
        Ignite ignite = Ignition.start(cfg);
        log.error("创建 Ignite Client 完成 ");
        return ignite;
    }


    public static class MyLifecycleBean implements LifecycleBean {
        @IgniteInstanceResource
        public Ignite ignite;

        @Override
        public void onLifecycleEvent(LifecycleEventType evt) {
            log.error("LifecycleEventType [{}]", evt);
            if (evt == LifecycleEventType.AFTER_NODE_START) {
                System.out.format("After the node (consistentId = %s) starts.\n", ignite.cluster().node().consistentId());
            }
        }
    }
}
