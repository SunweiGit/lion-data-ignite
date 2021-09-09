package cn.liondance.liondataignite;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.lifecycle.LifecycleBean;
import org.apache.ignite.lifecycle.LifecycleEventType;
import org.apache.ignite.resources.IgniteInstanceResource;

/**
 * 节点生命周期事件
 * 生命周期事件使开发者有机会在节点生命周期的不同阶段执行自定义代码。
 * <p>
 * 共有4个生命周期事件：
 * <p>
 * BEFORE_NODE_START：Ignite节点的启动程序初始化之前调用；
 * AFTER_NODE_START：Ignite节点启动之后调用；
 * BEFORE_NODE_STOP：Ignite节点的停止程序初始化之前调用；
 * AFTER_NODE_STOP：Ignite节点停止之后调用。
 * 下面的步骤介绍如何添加一个自定义生命周期事件监听器：
 * <p>
 * 开发一个实现LifecycleBean接口的类，该接口有一个onLifecycleEvent()方法，每个生命周期事件都会调用。
 */
@Slf4j
public class MyLifecycleBean implements LifecycleBean {
    @IgniteInstanceResource
    public Ignite ignite;

    @Override
    public void onLifecycleEvent(LifecycleEventType evt) {
        log.error("LifecycleEventType {}", evt);
        if (evt == LifecycleEventType.AFTER_NODE_START) {
            System.out.format("After the node (consistentId = %s) starts.\n", ignite.cluster().node().consistentId());

        }
    }
}