

package org.apache.skywalking.apm.plugin.logpattern.define;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.apache.skywalking.apm.agent.core.plugin.match.NameMatch.byName;

/**
 * @author bwyan
 * @date 2020/4/5
 */
public class Log4j2TraceInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    private static final String INTERCEPT_CLASS = "org.apache.skywalking.apm.plugin.logpattern.Log4j2Interceptor";
    private static final String ENHANCE_CLASS   = "org.apache.logging.log4j.core.layout.PatternLayout$Builder";
    private static final String ENHANCE_METHOD  = "build";

    @Override
    public ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        System.out.println("Log4j2TraceInstrumentation::getConstructorsInterceptPoints*************");

        return null;
    }

    @Override
    public InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        System.out.println("Log4j2TraceInstrumentation::getInstanceMethodsInterceptPoints*************");
        return new InstanceMethodsInterceptPoint[]{
                new InstanceMethodsInterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {

                        return named(ENHANCE_METHOD);
                    }

                    @Override
                    public String getMethodsInterceptor() {
                        return INTERCEPT_CLASS;
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return false;
                    }
                }
        };
    }

    @Override
    protected ClassMatch enhanceClass() {
        System.out.println("Log4j2TraceInstrumentation::enhanceClass*************");
        return byName(ENHANCE_CLASS);
    }
}
