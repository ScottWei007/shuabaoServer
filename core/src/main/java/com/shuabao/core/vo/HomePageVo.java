package com.shuabao.core.vo;

import com.shuabao.core.entity.AdvertiseEntity;
import com.shuabao.core.entity.TagEntity;

import java.util.List;

public class HomePageVo {
    private List<TagEntity> tags;
    private List<AdvertiseEntity> advertises;

    public List<TagEntity> getTags() {
        return tags;
    }

    public void setTags(List<TagEntity> tags) {
        this.tags = tags;
    }

    public List<AdvertiseEntity> getAdvertises() {
        return advertises;
    }

    public void setAdvertises(List<AdvertiseEntity> advertises) {
        this.advertises = advertises;
    }
}
