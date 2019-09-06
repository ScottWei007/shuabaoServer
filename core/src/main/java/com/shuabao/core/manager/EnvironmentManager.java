package com.shuabao.core.manager;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by Scott Wei on 4/8/2018.
 */
//環境資源管理
@Component
public class EnvironmentManager implements ApplicationContextAware {

    @Autowired
    private Environment env;

    private static ApplicationContext applicationContext;
    private static Environment environment;

    public static Environment getEnvironment (){
        return environment;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        EnvironmentManager.applicationContext = applicationContext;
        EnvironmentManager.environment = env;
    }
}
