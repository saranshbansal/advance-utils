/**
 * 
 */
package com.advanceutils.util.misc;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * Programmatic access to properties in Spring without re-reading the properties file
 * @author sbansal
 *
 */
public class SpringPropertiesLoader extends PropertyPlaceholderConfigurer {

    private static Map<String, String> propertiesMap;
    // Default as in PropertyPlaceholderConfigurer
    private int springSystemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;

    @Override
    public void setSystemPropertiesMode(int systemPropertiesMode) {
        super.setSystemPropertiesMode(systemPropertiesMode);
        springSystemPropertiesMode = systemPropertiesMode;
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
        super.processProperties(beanFactory, props);

        propertiesMap = new HashMap<>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String valueStr = resolvePlaceholder(keyStr, props, springSystemPropertiesMode);
            propertiesMap.put(keyStr, valueStr);
        }
    }

    public static String getProperty(String name) {
        if(propertiesMap!=null && propertiesMap.get(name)!=null)
            return propertiesMap.get(name).toString();
        else
            return null;
    }

}
