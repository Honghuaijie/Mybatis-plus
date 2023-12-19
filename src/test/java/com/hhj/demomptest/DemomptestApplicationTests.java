package com.hhj.demomptest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hhj.demomptest.entity.User;
import com.hhj.demomptest.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.jws.soap.SOAPBinding;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class DemomptestApplicationTests {

    @Autowired
    private UserMapper userMapper;  //为什么会报错，因为IOC容器中还没有，是在程序运行时才会自动生成该接口对应的代理对象


    //添加
    @Test
    public void testAdd(){
        User user = new User();
        user.setName("李四");
        user.setAge(20);
        user.setEmail("25552131@qq.com");
        int insert = userMapper.insert(user);
        System.out.println("insert = " + insert);
    }

    //修改
    @Test
    public void testUpdate(){
        User user = new User();
        user.setName("luluu");
        user.setAge(222);
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<User>();
        userQueryWrapper.eq(User::getName,"lulu");
        Long id = userMapper.selectOne(userQueryWrapper).getId();
        user.setId(id);
        int i = userMapper.updateById(user);
        System.out.println("i = " + i);
        
    }
    
    //测试乐观锁
    @Test
    public void testOptimisticLocker(){
        //根据ID查询
        User user = userMapper.selectById(1736754447154028545L);
        //修改
        user.setName("李四");
        QueryWrapper queryWrapper = new QueryWrapper();
        userMapper.updateById(user);
    }

    @Test
    void findAll() {
        //查询表中所有记录
        List<User> users = userMapper.selectList(null);
        System.out.println("users = " + users);
    }
    
    //多个ID的批量查询
    
    @Test
    public void testSelect1(){
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        System.out.println("users = " + users);
    }

    //简单条件查询
    @Test
    public void testSelect2(){
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("name","Tom");
        columnMap.put("age",28);

        List<User> users = userMapper.selectByMap(columnMap);
        System.out.println("users = " + users);
    }

    /**
     * 分页查询
     */
    @Test
    public void testPage(){
        //显示第二页，每页显示3条数据
        Page<User> page = new Page<>(1,3);

        userMapper.selectPage(page,null);

        long pages = page.getPages(); //获取总页数
        long current = page.getCurrent(); //当前的页数
        List<User> records = page.getRecords(); //获取查询的记录
        long total = page.getTotal(); //获取总记录数 SELECT COUNT(1) FROM user

        boolean hasNext = page.hasNext(); //判断是否有下一页
        boolean hasPrevious = page.hasPrevious(); //判断是否有上一页
        System.out.println("pages = " + pages);
        System.out.println("current = " + current);
        System.out.println("records = " + records);
        System.out.println("total = " + total);
        System.out.println("hasNext = " + hasNext);
        System.out.println("hasPrevious = " + hasPrevious);
    }


    //根据ID删除记录
    @Test
    public void testDeleteById(){
        userMapper.deleteById(1737084210078588929L);
    }



    // 测试wapper
    @Test
    public void test(){

        QueryWrapper queryWrapper = new QueryWrapper();
        //ge、gt、le、lt、isNull、isNotNull
        //queryWrapper.ge("age",20);

        // eq、ne
        //queryWrapper.eq("name","Tom");

        //between、notBetween
        //queryWrapper.between("age",20,30);
        //like、notLike(排除包含指定字符的记录)、likeLeft（%李）、likeRight（李%）
        //queryWrapper.like("name","李");

        //orderBy、orderByDesc、orderByAsc
        queryWrapper.orderByDesc( "id");

        List list = userMapper.selectList(queryWrapper);

        System.out.println("list = " + list);
    }




}
