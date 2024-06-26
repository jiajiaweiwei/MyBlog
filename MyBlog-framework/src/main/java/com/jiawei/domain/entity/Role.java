package com.jiawei.domain.entity;
import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色信息表(Role)表实体类
 *
 * @author makejava
 * @since 2024-03-18 22:06:20
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role")
public class Role {

    //角色ID
    @TableId
    private Long id;

    //角色名称
    private String roleName;

    //角色权限字符串
    private String roleKey;

    //显示顺序
    private Integer roleSort;

    //角色状态（0正常 1停用）
    private String status;

    //删除标志（0代表存在 1代表删除）
    @TableLogic // 标记逻辑删除字段
    private String delFlag;

    //创建者
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    //更新者
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    //备注
    private String remark;
}
