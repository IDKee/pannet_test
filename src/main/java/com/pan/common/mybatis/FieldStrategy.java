package com.pan.common.mybatis;


/** 
* 
* @author : panjie 
* @date : 2020/2/26
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
