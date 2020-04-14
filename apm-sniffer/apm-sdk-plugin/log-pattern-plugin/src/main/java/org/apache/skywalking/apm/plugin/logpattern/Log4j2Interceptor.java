package org.apache.skywalking.apm.plugin.logpattern;

import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.MethodInterceptResult;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

/**
 * @author bwyan
 * @date 2020/4/5
 */
public class Log4j2Interceptor implements InstanceMethodsAroundInterceptor {
    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, MethodInterceptResult result) throws Throwable {
        try {
            String jarUrl = System.getProperty("apm-toolkit-log4j-2.x");
            if (jarUrl == null) {
                jarUrl = "/home/admin/lib/apm-toolkit-log4j-2.x-6.6.0.jar";
            }
            System.out.println("**********jarUrl:" + jarUrl);
            URLClassLoader loader = (URLClassLoader) ClassLoaderUtil.addPath(jarUrl, Thread.currentThread().getContextClassLoader());
            Class.forName("org.apache.skywalking.apm.toolkit.log.log4j.v2.x.TraceIdConverter", true, loader);
            Class.forName("org.apache.skywalking.apm.toolkit.log.log4j.v2.x.Log4j2OutputAppender", true, loader);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        PatternLayout.Builder builder = (PatternLayout.Builder) objInst;
        Field                 field   = PatternLayout.Builder.class.getDeclaredField("pattern");
        field.setAccessible(true);
        String pattern = (String) field.get(builder);
        int    index   = pattern.lastIndexOf(":");
        String prefix  = pattern.substring(0, index);
        String last    = pattern.substring(index);
        // 添加traceId
        field.set(builder, prefix + " [%traceId] " + last);
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object ret) throws Throwable {
        return ret;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Throwable t) {
        t.printStackTrace();
    }
}
