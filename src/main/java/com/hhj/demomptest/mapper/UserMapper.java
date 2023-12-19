package com.hhj.demomptest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hhj.demomptest.entity.User;
import org.springframework.stereotype.Repository;

/**
 * ClassName: UserMapper
 * Package: com.hhj.demomptest.mapper
 * Description:
 *
 * @Author honghuaijie
 * @Create 2023/12/18 20:27
 * @Version 1.0
 * Yesterday is history,tomorrow is a mystery,
 * but today is a gift.That is why it's called the present
 */

//@Repository注解用于标识一个类是数据访问层的组件，
// 通常与持久化操作相关的类会使用这个注解。
// 它让 Spring 框架能够自动识别并注册这些类，方便在其他组件中进行依赖注入和管理。
@Repository
public interface UserMapper extends BaseMapper<User> {


}
