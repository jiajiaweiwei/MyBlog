package com.jiawei.domain.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleStatusDto {


    //
    private Long roleId;
    //角色状态（0正常 1停用）
    private String status;




}
