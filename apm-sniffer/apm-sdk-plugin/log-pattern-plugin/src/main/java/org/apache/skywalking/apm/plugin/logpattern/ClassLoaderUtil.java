package org.apache.skywalking.apm.plugin.logpattern;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author bwyan
 * @date 2020/4/14
 */
public class ClassLoaderUtil {

    public static ClassLoader addPath(String jarUrl, ClassLoader classLoader) throws Exception {
//        System.out.println(String.format("**********ClassLoaderUtil::addPath(%s,%s)", jarUrl, classLoader));
        if (classLoader instanceof URLClassLoader) {
            File           file           = new File(jarUrl);
            URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
            URL            targetUrl      = file.toURI().toURL();
            boolean        isLoader       = false;
            for (URL url : urlClassLoader.getURLs()) {
                if (url.equals(targetUrl)) {
                    isLoader = true;
                    break;
                }
            }
            if (!isLoader) {
                Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
                add.setAccessible(true);
                add.invoke(urlClassLoader, targetUrl);
            }
        }
        return classLoader;
    }

}
