package com.kk.xxljob.enums;

/**
 * @author linshiqiang
 * date 2023-01-23 19:31:00
 * 路由策略枚举
 */
public enum ExecutorRouteStrategyEnum {

    /**
     * 第一个
     */
    FIRST,

    /**
     * 最后一个
     */
    LAST,

    /**
     * 轮训
     */
    ROUND,

    /**
     * 随机
     */
    RANDOM,

    /**
     * 一致性HASH
     */
    CONSISTENT_HASH,

    /**
     * 最不经常使用
     */
    LEAST_FREQUENTLY_USED,

    /**
     * 最近最久未使用
     */
    LEAST_RECENTLY_USED,

    /**
     * 故障转移
     */
    FAILOVER,

    /**
     * 忙碌转移
     */
    BUSYOVER,

    /**
     * 分片广播
     */
    SHARDING_BROADCAST;

}
