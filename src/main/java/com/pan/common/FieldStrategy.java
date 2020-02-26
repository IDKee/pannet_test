package com.pan.common;

/**
 * <p>
 * 字段策略枚举类
 * </p>
 *
 * @author hubin
 * @since 2016-09-09
 */
public enum FieldStrategy {
    /**
     * 忽略判断
     */
    IGNORED,

    /**
     * 非NULL判断
     */
    NOT_NULL,

    /**
     * 非空判断
     */
    NOT_EMPTY
}
