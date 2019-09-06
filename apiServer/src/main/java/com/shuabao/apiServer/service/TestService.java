package com.shuabao.apiServer.service;

import com.shuabao.core.entity.TestEntity;

import java.util.List;

/**
 * Created by Scott Wei on 4/7/2018.
 */
public interface TestService {
    List<TestEntity> findAllEntity();

    TestEntity findEntityById(int id);

    int addEntity(TestEntity entity) throws Exception;

    int updateEntity(TestEntity entity) throws Exception;

    int deleteEntity(int id) throws Exception;
}
