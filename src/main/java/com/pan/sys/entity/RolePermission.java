package com.pan.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "s_role_permission")
public class RolePermission {

  private long id;
  private long roleId;
  private long permissionId;

}
