package com.pan.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "s_user")
public class User implements Serializable {

    private Integer id;

    private String username;

    private String password;

    private String salt;

    private boolean available = false;
}
