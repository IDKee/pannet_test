package com.pan.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "s_role_user")
public class RoleUser {

  private Integer id;
  private Integer roleId;
  private Integer userId;

}
