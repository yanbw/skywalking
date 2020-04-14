package org.apache.skywalking.apm.plugin.logpattern;

import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.MethodInterceptResult;

import java.lang.reflect.Method;
import java.net.URLClassLoader;

/**
 * @author bwyan
 * @date 2020/4/5
 */
public class LogbackTraceInterceptor implements InstanceMethodsAroundInterceptor {
    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, MethodInterceptResult result) throws Throwable {
        try {
            String jarUrl = System.getProperty("apm-toolkit-logback-1.x");
            if (jarUrl == null) {
                jarUrl="/home/admin/lib/apm-toolkit-logback-1.x-6.6.0.jar";
            }
            System.out.println("**********jarUrl:"+jarUrl);
            URLClassLoader loader = (URLClassLoader) ClassLoaderUtil.addPath(jarUrl, Thread.currentThread().getContextClassLoader());
            Class.forName("org.apache.skywalking.apm.toolkit.log.logback.v1.x.LogbackPatternConverter", true, loader);
            Class.forName("org.apache.skywalking.apm.toolkit.log.logback.v1.x.TraceIdPatternLogbackLayout", true, loader);
            Class.forName("org.apache.skywalking.apm.toolkit.log.logback.v1.x.mdc.LogbackMDCPatternConverter", true, loader);
            Class.forName("org.apache.skywalking.apm.toolkit.log.logback.v1.x.mdc.TraceIdMDCPatternLogbackLayout", true, loader);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object ret) throws Throwable {
        String pattern = (String) ret;
        try {
            String[] strings = new String[2];
            // %m %msg %message
            int index = indexSplit(pattern, M, strings);
            if (index < 0) {
                index = indexSplit(pattern, MSG, strings);
                if (index < 0) {
                    index = indexSplit(pattern, MESSAGE, strings);
                }
            }
            return strings[0] + " [%tid] " + strings[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pattern;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Throwable t) {
        t.printStackTrace();
    }

    /**
     * 匹配 规则 ，切分
     * @param pattern
     * @param msg
     * @param strings
     * @return
     */
    private static int indexSplit(String pattern, String msg, String[] strings) {
        int index = pattern.lastIndexOf(msg + "%");
        if (index > 0) {
            strings[0] = pattern.substring(0, index);
            strings[1] = pattern.substring(index);
        } else {
            index = pattern.lastIndexOf(msg + " ");
            if (index > 0) {
                strings[0] = pattern.substring(0, index);
                strings[1] = pattern.substring(index);
            } else {
                index = pattern.lastIndexOf(msg);
                if (index > 0) {
                    strings[0] = pattern.substring(0, index);
                    strings[1] = pattern.substring(index);
                } else {
                    strings[0] = pattern;
                    strings[1] = "";
                }
            }
        }
        return index;
    }

    private static final String M       = "%m";
    private static final String MSG     = "%msg";
    private static final String MESSAGE = "%message";

}
