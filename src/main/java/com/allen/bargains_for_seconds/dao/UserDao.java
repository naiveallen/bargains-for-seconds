package com.allen.bargains_for_seconds.dao;

import com.allen.bargains_for_seconds.domain.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    public User getById(@Param("id") long id);

    @Update("update user set password=#{password} where id=#{id}")
    public void update(User toBeUpdate);

    @Insert("insert into user(id,password,salt,register_date)value(#{id},#{password},#{salt},#{registerDate})")
    public void createAccount(User user);

}
