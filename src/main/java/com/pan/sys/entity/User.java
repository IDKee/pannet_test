package com.pan.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "s_user")
public class User {

    private Integer id;

    private String username;

    private String password;

    private String salt;

    private boolean available = false;
}
