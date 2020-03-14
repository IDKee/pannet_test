package com.pan.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "s_role")
public class Role {

  private Integer id;
  private String role;
  private String description;
  private boolean available=false;

}
