package com.kk.xxljob.enums;

/**
 * @author linshiqiang
 * date 2023-01-23 19:40:00
 * 调度过期策略枚举
 */
public enum MisfireStrategyEnum {

    /**
     * 忽略
     */
    DO_NOTHING,

    /**
     * 立即执行一次
     */
    FIRE_ONCE_NOW;

}
