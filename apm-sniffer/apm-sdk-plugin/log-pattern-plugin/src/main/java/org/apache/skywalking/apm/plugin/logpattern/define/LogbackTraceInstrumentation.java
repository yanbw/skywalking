

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
public class LogbackTraceInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    private static final String INTERCEPT_CLASS = "org.apache.skywalking.apm.plugin.logpattern.LogbackTraceInterceptor";
    private static final String ENHANCE_CLASS   = "ch.qos.logback.core.pattern.PatternLayoutEncoderBase";
    private static final String ENHANCE_METHOD  = "getPattern";

    @Override
    public ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        System.out.println("LogbackTraceInstrumentation::getConstructorsInterceptPoints*************");

        return null;
    }

    @Override
    public InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        System.out.println("LogbackTraceInstrumentation::getInstanceMethodsInterceptPoints*************");
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
        System.out.println("LogbackTraceInstrumentation::enhanceClass*************");
        return byName(ENHANCE_CLASS);
    }
}
