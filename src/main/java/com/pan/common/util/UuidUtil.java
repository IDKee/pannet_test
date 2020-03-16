package com.pan.common.util;

import java.util.UUID;

public class UuidUtil {

    public static String getUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
}
