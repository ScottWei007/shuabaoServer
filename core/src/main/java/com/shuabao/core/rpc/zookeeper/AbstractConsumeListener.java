package com.shuabao.core.rpc.zookeeper;

import com.shuabao.core.rpc.dubbo.ProductServerTask;
import com.shuabao.core.rpc.handler.RpcHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Scott Wei on 9/13/2018.
 */
public abstract class AbstractConsumeListener<T extends RpcHandler> implements TreeCacheListener {
    public static final Logger log = LoggerFactory.getLogger(AbstractConsumeListener.class);

    private List<ProductServerTask<T>> serverTasks = new CopyOnWriteArrayList();
    private Map<String, ProductServerTask<T>> serverTaskMap = new ConcurrentHashMap<>();
    private ProductServerTask<T> mainServerTask;

    @Override
    public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
        ChildData childData = event.getData();
        if(Objects.nonNull(childData)) {
            String url = new String(childData.getData());
            String path = childData.getPath();
            switch (event.getType()) {
                case NODE_ADDED:
                    addServer(path, url);
                    log.info("=============="+ this.getClass().getSimpleName() + ":NODE_ADDED : " + path + "  数据:" + url);
                    break;
                case NODE_REMOVED:
                    removeServer(path);
                    log.info("=============="+ this.getClass().getSimpleName() + ":NODE_REMOVED : " + path + "  数据:" + url);
                    break;
                case NODE_UPDATED:
                    updateServer(path, url);
                    log.info("=============="+ this.getClass().getSimpleName() + ":NODE_UPDATED : " + path + "  数据:" + url);
                    break;
                default:
                    break;
            }
        }
    }

    private final void addServer(String path, String url) {
        if(StringUtils.isNoneEmpty(path, url)) {
            ProductServerTask<T> productServerTask = createServer(path, url);
            serverTasks.add(productServerTask);
            reloadMainServerTask();
            serverTaskMap.put(productServerTask.getServerId(),productServerTask);
        }
    }

    private final void removeServer(String path) {
        serverTasks.forEach(e -> {
            if(e.getPath().equals(path)) {
                serverTasks.remove(e);
                serverTaskMap.remove(e.getServerId());
            }
        });
        reloadMainServerTask();
    }

    private final void updateServer(String path, String url) {
        serverTasks.forEach(e -> {
            if(e.getPath().equals(path)) {
                serverTaskMap.remove(e.getServerId());
                e.updataUrl(url);
                serverTaskMap.put(e.getServerId(), e);
            }
        });
        reloadMainServerTask();
    }

    private final void reloadMainServerTask() {
        if(serverTasks.isEmpty()) {
            mainServerTask = null;
        }else {
            serverTasks.sort(myComparator);
            mainServerTask = serverTasks.get(0);
        }
    }

    protected abstract ProductServerTask<T> createServer(String path, String url);

    public ProductServerTask findServer(String ip, String port) {
        return serverTaskMap.get(ip + ":" + port);
    }

    private final Comparator<ProductServerTask<T>> myComparator = (ProductServerTask<T> o1, ProductServerTask<T> o2) -> {
        if(o1.getSort() == o2.getSort())
            return o1.getName().compareTo(o2.getName());
        return o1.getSort() - o2.getSort();
    };
}
