package com.shuabao.apiServer.service.impl;

import com.shuabao.apiServer.service.TestService;
import com.shuabao.core.entity.TestEntity;
import com.shuabao.core.mapper.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Scott Wei on 4/7/2018.
 */

@Service
public class TestServiceImpl implements TestService{

    @Autowired
    TestDao testDao;

    @Override
    public List<TestEntity> findAllEntity() {
        return testDao.findAllEntity();
    }

    @Override
    public TestEntity findEntityById(int id) {
        return testDao.findEntityById(id);
    }

    @Override
    public int addEntity(TestEntity entity) throws Exception{
        int res = testDao.addEntity(entity);
//        if (res ==1) {
//            throw new Exception();//測試事務
//        }
        return res;
    }

    @Override
    public int updateEntity(TestEntity entity) throws Exception {
        int res = testDao.updateEntity(entity);
//        if (res ==1) {
//            throw new Exception();//測試事務
//        }
        return res;
    }

    @Override
    public int deleteEntity(int id)  throws Exception{
        int res = testDao.deleteEntity(id);
//        if (res ==1) {
//            throw new Exception();//測試事務
//        }
        return res;
    }
}
