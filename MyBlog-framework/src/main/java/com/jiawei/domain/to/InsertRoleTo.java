package com.jiawei.domain.to;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertRoleTo {

    //角色ID
    @TableId
    private Long id;

    //备注
    private String remark;

    //角色权限字符串
    private String roleKey;

    //显示顺序
    private Integer roleSort;

    //角色状态（0正常 1停用）
    private String status;

    //角色名称
    private String roleName;

    private List<Long> menuIds;



}
