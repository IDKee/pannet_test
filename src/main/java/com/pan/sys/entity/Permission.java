package com.pan.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "s_permission")
public class Permission {

  private Integer id;
  private String permissionName;
  private String permissionCode;
  private String type;
  private Integer pid;
  private String url;
  private boolean available=false;

}
