package com.lucas.common.util;

import cn.hutool.core.util.IdUtil;

/**
 * 封装hutool雪花算法
 */
public class SnowUtil {
    public static long dataCenterId = 1;
    public static long workerId = 1;

    public static long getSnowflakeNextId() {
        return IdUtil.getSnowflake(workerId, dataCenterId).nextId();
    }

    public static String getSnowflakeNextStr() {
        return IdUtil.getSnowflake(workerId, dataCenterId).nextIdStr();
    }
}
