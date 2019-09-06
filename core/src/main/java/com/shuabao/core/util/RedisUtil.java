package com.shuabao.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.shuabao.core.config.redis.CommonRedisConfigurer;
import com.shuabao.core.entity.*;
import com.shuabao.core.mapper.SettingDao;
import com.shuabao.core.mapper.UserDao;
import com.shuabao.core.rpc.bean.MessageWrapper;
import com.shuabao.core.vo.HomePageVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public final class RedisUtil {
    private static final Logger log = LoggerFactory.getLogger(RedisUtil.class);

    /*
        redis的key命名规则:
        项目名称(shuabao)  + 功能名称
     */
    private static final String SHUABAO_HOMEPAGE_TAG = "SHUABAO_HOMEPAGE_TAG";//首頁標簽
    private static final String SHUABAO_HOMEPAGE_ADVERTISE = "SHUABAO_HOMEPAGE_ADVERTISE";//首頁廣告
    private static final String SHUABAO_HOMEPAGE_NEW = "SHUABAO_HOMEPAGE_NEW";//新人
    private static final String SHUABAO_HOMEPAGE_HOT = "SHUABAO_HOMEPAGE_HOT";//热门
    private static final String SHUABAO_HOMEPAGE_FOLLOW = "SHUABAO_HOMEPAGE_FOLLOW";//关注  +uid
    private static final String SHUABAO_USERSESSION = "SHUABAO_USERSESSION";//在线可直播的用户 + uid

    //充值
    private static final String SHUABAO_RECHARGE_ALL = "SHUABAO_RECHARGE_ALL";//充值點
    private static final String SHUABAO_RECHARGE_ANDROID = "SHUABAO_RECHARGE_ANDROID";//安卓充值點
    private static final String SHUABAO_RECHARGE_IOS = "SHUABAO_RECHARGE_IOS";//蘋果充值點
    private static final String SHUABAO_SANDBOX = "SHUABAO_SANDBOX";//蘋果沙盒标记

    //消息推送
    private static final String SHUABAO_OFFLINE_MESSAGE = "SHUABAO_OFFLINE_MESSAGE";//离线消息+ uid
    private static final int CACHE_MESSAGE_TIME = 60 * 60 * 24 * 15;//消息缓存时间 15天

    //转换对象
    public static <T,E> T convertOject(E from, Class<T> toClass) {
        T t = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(from);
            t = mapper.readValue(json, toClass);
        } catch (Exception e) {
            log.error("======RedisUtil convertOject error：" + e.getMessage());
        }
        return t;
    }

    //对象转换字符串
    public static <T> String objectToString(T from) {
        String json = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(from);
        } catch (Exception e) {
            log.error("======RedisUtil objectToString error:" + e.getMessage());
        }
        return json;
    }

    //字符串转换对象
    public static <T> T stringToObject(String from, Class<T> toClass) {
        T t = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            t = mapper.readValue(from, toClass);
        } catch (Exception e) {
            log.error("======RedisUtil stringToObject error:" + e.getMessage());
        }
        return t;
    }

    //首頁標簽和廣告
    public static HomePageVo getHomepageTagAndAdvertise(SettingDao settingDao) {
        ObjectMapper mapper = new ObjectMapper();
        List<TagEntity> tags = new ArrayList<>();
        List<AdvertiseEntity> advertises = new ArrayList<>();
        try {
            //標簽
            Set<String> tagSet = CommonRedisConfigurer.getInstance().smembers(SHUABAO_HOMEPAGE_TAG);
            if(Objects.isNull(tagSet) || tagSet.isEmpty()) {
                //没有缓存去數據庫查詢，并且存緩存
                List<TagEntity> tagList = settingDao.getHomePageTags();
                tags = tagList;
                int size = tagList.size();
                String[] tagArray = new String[size];
                for(int i = 0; i < size; i ++) {
                    tagArray[i] = mapper.writeValueAsString(tagList.get(i));
                }
                CommonRedisConfigurer.getInstance().sadd(SHUABAO_HOMEPAGE_TAG, tagArray);
            }else {
                for(String json : tagSet) {
                    tags.add(mapper.readValue(json, TagEntity.class));
                }
            }
            tags.sort(Comparator.comparingInt(TagEntity::getOrder));//標簽排序
            //廣告
            Set<String> advertiseSet = CommonRedisConfigurer.getInstance().smembers(SHUABAO_HOMEPAGE_ADVERTISE);
            if(Objects.isNull(advertiseSet) || advertiseSet.isEmpty()) {
                //没有缓存去數據庫查詢，并且存緩存
                List<AdvertiseEntity> advertiseList = settingDao.getHomePageAdvertises();
                advertises = advertiseList;
                int size = advertiseList.size();
                String[] advertiseArray = new String[size];
                for(int i = 0; i < size; i ++) {
                    advertiseArray[i] = mapper.writeValueAsString(advertiseList.get(i));
                }
                CommonRedisConfigurer.getInstance().sadd(SHUABAO_HOMEPAGE_ADVERTISE, advertiseArray);
            }else {
                for(String json : advertiseSet) {
                    advertises.add(mapper.readValue(json, AdvertiseEntity.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HomePageVo homePageEntity = new HomePageVo();
        homePageEntity.setTags(tags);
        homePageEntity.setAdvertises(advertises);
        return homePageEntity;
    }

    //获取首页分类,与数据库的setting_homepage_tag表一致
    public static List<UserSession> getHomepageType(HomePageTypeEntity entity, UserDao userDao){
        final List<UserSession> list = new ArrayList<>();
        String key = getKey(entity.getType(), entity.getUid());
        if(Objects.nonNull(key)) {
            Set<String> set = CommonRedisConfigurer.getInstance().zrevrange(key, entity.getStart(), entity.getEnd());
            if(Objects.nonNull(set)) {
                set.forEach(uid -> {
                    UserSession session = getUserSession(uid, userDao);
                    if(Objects.nonNull(session)) {
                        list.add(session);
                    }
                });
            }
        }
        return list;
    }

    //设置首页分类
    public static void setHomepageType(int uid, long score, HomePageTypeEntity.HomePageType homePageType, Integer myUid) {
        String key = getKey(homePageType, myUid);
        if(Objects.nonNull(key)) {
            if(SHUABAO_HOMEPAGE_NEW.equals(key)) {//新人缓存有过期时间
                CommonRedisConfigurer.getInstance().zadd(key, String.valueOf(uid), Double.valueOf(score),CACHE_MESSAGE_TIME);
            }else if(SHUABAO_HOMEPAGE_HOT.equals(key)){//热门,目前没有调用这个逻辑
                CommonRedisConfigurer.getInstance().zadd(key, String.valueOf(uid), Double.valueOf(score));
            }else {//我的关注
                CommonRedisConfigurer.getInstance().zadd(key, String.valueOf(uid), Double.valueOf(score));
                //热门，按照关注的总人数排名
                CommonRedisConfigurer.getInstance().zincrby(SHUABAO_HOMEPAGE_HOT, String.valueOf(uid), 1);
            }
        }
    }

    //类型与数据库的setting_homepage_tag表一致
    private static String getKey(HomePageTypeEntity.HomePageType homePageType, Integer myUid) {
        String key;
        switch (homePageType) {
            case FOLLOW://我关注的人
                Objects.requireNonNull(myUid);
                key = SHUABAO_HOMEPAGE_FOLLOW + myUid;
                break;
            case NEW://新人
                key = SHUABAO_HOMEPAGE_NEW;
                break;
            case HOT: //推荐(热门)
                key = SHUABAO_HOMEPAGE_HOT;
                break;
            default://默认
                key = null;
        }
        return key;
    }


    //保存用户缓存
    public static void setUserSession(UserSession session) {
        Map<String, String> map = convertOject(session, Map.class);
        if(!map.isEmpty()) {
            CommonRedisConfigurer.getInstance().hmset(SHUABAO_USERSESSION + session.getUid(), map);
        }
    }

    public static void removeUserSession(String uid, String... fields) {
        CommonRedisConfigurer.getInstance().hdel(SHUABAO_USERSESSION + uid, fields);
    }

    //获取用户缓存
    public static UserSession getUserSession(String uid, UserDao userDao) {
        //先取缓存
        Map<String, String> map = CommonRedisConfigurer.getInstance().hgetAll(SHUABAO_USERSESSION + uid);
        if(Objects.nonNull(map) && !map.isEmpty()) {
            return convertOject(map, UserSession.class);
        }
        if(Objects.isNull(userDao)) {
            return null;
        }
        //没有缓存则查数据库
        UserInfoEntity entity = new UserInfoEntity();
        entity.setUid(Integer.valueOf(uid));
        entity = userDao.getUserInfoByUid(entity);
        if(Objects.isNull(entity)) {
            return null;
        }
        UserSession session = new UserSession();
        session.setUid(String.valueOf(entity.getUid()));
        session.setNickname(entity.getNickname());
        session.setAvatar(entity.getAvatar());
        session.setSex(String.valueOf(entity.getSex()));
        session.setAddr(entity.getAddr());
        session.setSign(entity.getSign());
        session.setLevel(String.valueOf(entity.getLevel()));
        session.setHeight(String.valueOf(entity.getHeight()));
        session.setEducation(String.valueOf(entity.getEducation()));
        session.setMarriage(String.valueOf(entity.getMarriage()));
        session.setIncome(entity.getIncome());
        session.setExpectedtime(String.valueOf(entity.getExpectedtime()));
        session.setViplevel(String.valueOf(entity.getViplevel()));
        session.setBonds(String.valueOf(entity.getBonds()));
        session.setGold(String.valueOf(entity.getGold()));
        session.setPrice(String.valueOf(entity.getPrice()));
        session.setPreview(String.valueOf(entity.getPreview()));
        session.setStatus(String.valueOf(entity.getStatus()));
        session.setBirth(entity.getBirth());
        session.setToken(entity.getToken());
        UserInfoEntity phone = userDao.getPhoneByUid(entity.getUid());
        if(Objects.nonNull(phone)) {
            session.setPhone(String.valueOf(phone.getPhone()));
        }
        setUserSession(session);//存redis
        return session;
    }


    //獲取充值點
    public static RechargeEntity getRechargeByRid(int rid , SettingDao settingDao){
        RechargeEntity rechargeEntity = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = CommonRedisConfigurer.getInstance().hget(SHUABAO_RECHARGE_ALL, String.valueOf(rid));
            if(StringUtils.isEmpty(json)){ //没有缓存则加載数据库
                Map<String, String> map = new HashMap<>(64);
                List<RechargeEntity> rechargeEntities = settingDao.getAllRecharges();
                for(RechargeEntity entity : rechargeEntities) {
                    String str = mapper.writeValueAsString(entity);
                    map.put(String.valueOf(entity.getRid()), str);
                    if(entity.getRid() == rid) {
                        rechargeEntity = entity;
                    }
                }
                if(!map.isEmpty()) {
                    CommonRedisConfigurer.getInstance().hmset(SHUABAO_RECHARGE_ALL, map);
                }
            }
            rechargeEntity = mapper.readValue(json, RechargeEntity.class);
        } catch (Exception e) {
            log.error("getRechargeByRid 对象转换错误error : " + e.getMessage());
        }finally {
            return rechargeEntity;
        }
    }

    //是否是沙盒
    public static boolean isSandbox(String member){
        return  CommonRedisConfigurer.getInstance().sismember(SHUABAO_SANDBOX, member);
    }


    //消息缓存消息
    public static void lPushMessage(int uid, MessageWrapper messageWrapper){
        CommonRedisConfigurer.getInstance().lPush(SHUABAO_OFFLINE_MESSAGE + uid, objectToString(messageWrapper), CACHE_MESSAGE_TIME);
    }

    //获取并删除缓存消息
    public static MessageWrapper rPopMessage(int uid){
        String json = CommonRedisConfigurer.getInstance().rPop(SHUABAO_OFFLINE_MESSAGE + uid);
        if(StringUtils.isEmpty(json)) {
            return null;
        }
        return stringToObject(json, MessageWrapper.class);
    }




}
