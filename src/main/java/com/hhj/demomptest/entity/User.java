package com.hhj.demomptest.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * ClassName: User
 * Package: com.hhj.demomptest.entity
 * Description:
 *
 * @Author honghuaijie
 * @Create 2023/12/18 20:22
 * @Version 1.0
 * Yesterday is history,tomorrow is a mystery,
 * but today is a gift.That is why it's called the present
 */

@Data
public class User {

    private Long id;
    private String name;
    private Integer age;
    private String email;
    @Version
    @TableField(fill = FieldFill.INSERT) //自动填充
    private Integer version;

    @TableField(fill = FieldFill.INSERT) //添加时自动填充
    private Date createTime; //create_time
    @TableField(fill = FieldFill.INSERT_UPDATE) //添加和修改时自动填充
    private Date updateTime; //update_time


    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
