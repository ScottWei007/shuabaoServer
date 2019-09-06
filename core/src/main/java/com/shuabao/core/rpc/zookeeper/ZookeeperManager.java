package com.shuabao.core.rpc.zookeeper;

import com.shuabao.core.manager.EnvironmentManager;
import com.shuabao.core.rpc.bean.ServerEntity;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Scott Wei on 9/13/2018.
 */
public class ZookeeperManager {
    public static final Logger log = LoggerFactory.getLogger(ZookeeperManager.class);


    private static volatile ZookeeperManager instance;
    private static final Lock lock = new ReentrantLock();

    public static ZookeeperManager getInstance() {
        if (Objects.isNull(instance)) {
            try {
                lock.lock();
                if (Objects.isNull(instance)) {
                    instance = new ZookeeperManager();
                }
            }finally {
                lock.unlock();
            }
        }
        return instance;
    }

    private ZookeeperManager() {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(EnvironmentManager.getEnvironment().getProperty("spring.zookeeper.host"))
                .connectionTimeoutMs(1500)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,6))
                .build();
        curatorFramework.start();
        try {
            curatorFramework.blockUntilConnected();//记得开放端口
        }catch (Exception e) {
            log.error("=========ZookeeperManager curatorFramework init error : " + e.getMessage());
        }
        //create root path
        create(ROOT);
    }

    private final CuratorFramework curatorFramework;

    public static final String ROOT = "/shuabao";
    public static final String LINESEPARATOR = "/";

    public static final int SOCKETSERVER = 1;


    private String currentPath;

    public void saveRegDatas(int type, String name, String url) {
        //注册服务路径
        String regPath = ROOT + LINESEPARATOR + type;
        create(regPath);//创建路径
        currentPath = regPath + LINESEPARATOR + name;
        create(currentPath, url, CreateMode.EPHEMERAL);//保持数据
        addListener(currentPath, (CuratorFramework arg0, TreeCacheEvent event) -> {
            ChildData childData = event.getData();
            if(Objects.nonNull(childData)) {
                switch (event.getType()) {
                    case NODE_REMOVED:
                        if(currentPath.equals(childData.getPath())) {
                            create(currentPath, new String(childData.getData()), CreateMode.EPHEMERAL);
                        }
                        log.info("=========ZookeeperManager NODE_REMOVED : " + childData.getPath() + "  数据:" + new String(childData.getData()));
                        break;
                    default:
                        break;
                }
            }else {
                if(event.getType() == TreeCacheEvent.Type.CONNECTION_RECONNECTED) {
                    create(currentPath, url, CreateMode.EPHEMERAL);
                }
            }
        });
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public final void create(String path) {
        this.create(path, "", CreateMode.PERSISTENT);
    }

    public final void create(String path, String data, CreateMode mode) {
       try{
           if(Objects.isNull(curatorFramework.checkExists().forPath(path))) {
               curatorFramework.create().withMode(mode).forPath(path, data.getBytes());
           }
       }catch (Exception e) {
           log.error("=========ZookeeperManager create error : " + e.getMessage());
       }
    }

    public void addListener(String path, TreeCacheListener listener) {
        try{
            TreeCache treeCache = TreeCache.newBuilder(curatorFramework, path).build();
            treeCache.getListenable().addListener(listener);
            treeCache.start();//沒有啟動，坑啊
        }catch (Exception e) {
            log.error("=========ZookeeperManager addListener error : " + e.getMessage());
        }
    }

    public Map<String,List<String>> getServers(){
        Map<String,List<String>> ret = new HashMap<>();
        try {
            List<String> list = curatorFramework.getChildren().forPath(ROOT);
            for(String node : list){
                String path = ROOT + LINESEPARATOR + node;
                List<String> servers = curatorFramework.getChildren().forPath(path);
                List<String> serverList = new ArrayList<>();
                for(String serverNode : servers){
                    String serverPath = path + LINESEPARATOR + serverNode;
                    serverList.add(new String(curatorFramework.getData().forPath(serverPath)));
                }
                ret.put(node, serverList);
            }
        } catch (Exception e) {
        }
        return ret;
    }
}
