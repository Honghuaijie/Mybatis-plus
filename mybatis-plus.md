mybatis-plus实现数据库crud操作

1. mp是什么
2. mp实现添加，修改 删除 查询 
3. mp自动填充  乐观锁
4. mp逻辑删除
5. mp分页查询







# mp入门

1. 创建数据库，数据库表
2. 创建工程

注意，springboot  2.xx对应JDK8   springboot3 对应JDK17

![1702901046413](C:\Users\25220\AppData\Roaming\Typora\typora-user-images\1702901046413.png)

3. 在项目中引入mp和相关依赖

```
<dependencies>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-test</artifactId>
<scope>test</scope>
<exclusions>
<exclusion>
<groupId>org.junit.vintage</groupId>
<artifactId>junit-vintage-engine</artifactId>
</exclusion>
</exclusions>
</dependency>
<!--mybatis-plus-->
<dependency>
<groupId>com.baomidou</groupId>
<artifactId>mybatis-plus-boot-starter</artifactId>
<version>3.3.1</version>
</dependency>

<!--mysql依赖-->
<dependency>
<groupId>mysql</groupId>
<artifactId>mysql-connector-java</artifactId>
</dependency>
<!--lombok用来简化实体类-->
<dependency>
<groupId>org.projectlombok</groupId>
<artifactId>lombok</artifactId>
<optional>true</optional>
</dependency>
</dependencies>
```



4. 配置数据库信息

在application.properties中配置

```properties
#mysql数据库连接
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/mybatis_plus?serverTimezone=GMT%2B8  #serverTimezone是设置时区
spring.datasource.username=root
spring.datasource.password=root
```



5. 编写核心代码

![1702902866430](C:\Users\25220\AppData\Roaming\Typora\typora-user-images\1702902866430.png)

测试

![1702902878008](C:\Users\25220\AppData\Roaming\Typora\typora-user-images\1702902878008.png)





# mp 添加

```java
  //添加
    @Test
    public void testAdd(){
        User user = new User();
        user.setName("zhang");
        user.setAge(20);
        user.setEmail("25552131@qq.com");
        int insert = userMapper.insert(user);
        System.out.println("insert = " + insert);
    }
```

**主键策略**

雪花（默认 IdType.*ASSIGN_ID*）

自增长（IdType.*AUTO*）.

**要想影响所有实体的配置，可以设置全局主键配置**

#全局设置主键生成策略mybatis-plus.global-config.db-config.id-type=auto





# mp 修改

updateById





# mp 自动填充

在新增和修改记录的时候自动填充对应的属性值



1. 新增字段（添加时间、修改时间）![1702906685172](C:\Users\25220\AppData\Roaming\Typora\typora-user-images\1702906685172.png)

2. 设置对应实体类字段属性![1702906877975](C:\Users\25220\AppData\Roaming\Typora\typora-user-images\1702906877975.png)

3. 创建类，实现接口两个方法，一个方法新增执行，一个方法修改执行

   ```java
   package com.hhj.demomptest.hander;
   
   @Component  //一定要放入到IOC容器，不放入到IOC容器没用
   public class MyMetaObjectHander implements MetaObjectHandler {
   
       //mp执行添加操作，这个方法执行
       @Override
       public void insertFill(MetaObject metaObject) {
           this.setFieldValByName("createTime",new Date(),metaObject);
           this.setFieldValByName("updateTime",new Date(),metaObject);
       }
       //mp执行修改操作，这个方法执行
       @Override
       public void updateFill(MetaObject metaObject) {
           this.setFieldValByName("updateTime",new Date(),metaObject);
       }
   } 
   ```





# mp 乐观锁

使用场景：当你要更新一条记录时，希望这条记录没有被别人更新，也就是说实现线程安全的数据更新

主要是为了解决多线程操作中产生的一系列问题



在修改数据之前比较版本号，在修改数据之后修改版本号



**实现过程**

1. 添加表字段，在实体类中添加版本属性version，并给改属性添加@Version注解![1702909055477](C:\Users\25220\AppData\Roaming\Typora\typora-user-images\1702909055477.png)
2. 创建配置文件注册乐观锁插件

```java
@Configuration //配置类
@MapperScan("com.hhj.demomptest.mapper") //扫描mapper文件
public class MpConfig {

    /**
     * 乐观锁插件
     * @return
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor(){
        return new OptimisticLockerInterceptor();
    }
}
```





# mp查询

## 多个ID的批量查询

```java
 //多个ID的批量查询
    
    @Test
    public void testSelect1(){
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        System.out.println("users = " + users);
    }
```



## 简单条件查询

```java
//简单条件查询
@Test
public void testSelect2(){
    Map<String,Object> columnMap = new HashMap<>();
    columnMap.put("name","Tom");
    columnMap.put("age",28);

    List<User> users = userMapper.selectByMap(columnMap);
    System.out.println("users = " + users);
}
```



## 分页查询

1. 配置分页插件

```java
配置类中配置，或者直接在main中配置只要加入到bean中就可以了
/**
 * 分页插件
 */
@Bean
public PaginationInterceptor paginationInterceptor() {
    return new PaginationInterceptor();
}
```

2. 编写分页代码
   1. 插件page对象，传入两个参数（当前页，每页显示的记录数）
   2. 调用mp中的方法，实现分页

```java
/**
     * 分页查询
     */
    @Test
    public void testPage(){
        //显示第二页，每页显示3条数据
        Page<User> page = new Page<>(1,3);

        userMapper.selectPage(page,null);

        long pages = page.getPages(); //获取总页数  SELECT COUNT(1) FROM user
        long current = page.getCurrent(); //当前的页数
        List<User> records = page.getRecords(); //获取查询的记录
        long total = page.getTotal(); //获取总记录数

        boolean hasNext = page.hasNext(); //判断是否有下一页
        boolean hasPrevious = page.hasPrevious(); //判断是否有上一页

        System.out.println("pages = " + pages);
        System.out.println("current = " + current);
        System.out.println("records = " + records);
        System.out.println("total = " + total);
        System.out.println("hasNext = " + hasNext);
        System.out.println("hasPrevious = " + hasPrevious);
    }
```





# mp删除

## 根据ID删除

userMapper.deleteById();

## 批量删除

userMapper.deleteBatchIds()

## 简单条件删除

userMapper.deleteByMap()



## 逻辑删除

物理删除和逻辑删除

物理删除：真实删除 ，在数据库中看不到该记录

逻辑删除：假删除，  在数据库中还可以看到该记录



**逻辑删除的实现方式**

1. 数据库修改：添加delete字段
2. 实体类修改：添加属性，并使用@TableLogic注解修饰
3. 可以给delete 设置TableField 在添加时自动填充为0
4. 测试





# mp条件构造器

**wapper**

![1702988418065](C:\Users\25220\AppData\Roaming\Typora\typora-user-images\1702988418065.png)





1. ge、gt、le、lt、isNull、isNotNull
2.  eq、ne
3. between、notBetween
4. like、notLike、likeLeft、likeRight
5. orderBy、orderByDesc、orderByAsc

```java
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
```

