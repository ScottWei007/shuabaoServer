package com.shuabao.core.mapper;

import com.shuabao.core.entity.AdvertiseEntity;
import com.shuabao.core.entity.RechargeEntity;
import com.shuabao.core.entity.TagEntity;

import java.util.List;

/**
 * Created by Scott Wei on 4/15/2018.
 */
public interface SettingDao {
    List<TagEntity> getHomePageTags();

    List<AdvertiseEntity> getHomePageAdvertises();

    List<RechargeEntity> getAllRecharges();

}
