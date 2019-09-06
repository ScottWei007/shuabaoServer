package com.shuabao.core.mapper;

import com.shuabao.core.entity.TestEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Scott Wei on 4/7/2018.
 */
//@Mapper
public interface TestDao {
//    @Select("select * from test")
    List<TestEntity> findAllEntity();

    TestEntity findEntityById(int id);

    int addEntity(TestEntity entity);

    int updateEntity(TestEntity entity);

    int deleteEntity(@Param("id") int id);//這種方式也可以


}
