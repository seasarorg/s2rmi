package org.seasar.remoting.rmi.server;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bootstrap {

    protected static final String DEFAULT_DICON_FILE = "app.dicon";
    protected static final String INITIALIZER_CLASS = "org.seasar.framework.container.external.GenericS2ContainerInitializer";
    protected static final String SET_CONFIG_PATH_METHOD = "setConfigPath";
    protected static final String INITIALIZER_METHOD = "initialize";
    protected static final String DESTROY_METHOD = "destroy";
    protected static final String BOOTSTRAP_CLASS_FILE_NAME = Bootstrap.class.getName().replace(
            '.', '/')
            + ".class";

    private static final Logger logger = Logger.getLogger(Bootstrap.class.getName(),
            "S2RMIServerMessages");

    protected Object s2container;
    protected boolean terminate;

    public static void main(final String[] args) {
        logger.log(Level.INFO, "IRMI3000");
        try {
            new Bootstrap().run(args);
        }
        catch (final Exception e) {
            logger.log(Level.SEVERE, "ERMI3001", e);
            System.exit(1);
        }
    }

    protected void run(final String[] args) throws Exception {
        final String dicon = getDicon(args);
        final String classpathArg = getClasspath(args);
        setupClasspath(classpathArg.split(File.pathSeparator));
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                logger.log(Level.INFO, "IRMI3002");
                try {
                    final Method destoroy = s2container.getClass().getMethod(DESTROY_METHOD, null);
                    destoroy.invoke(s2container, null);
                }
                catch (final Exception e) {
                    logger.log(Level.SEVERE, "ERMI3003", e);
                }
                synchronized (Bootstrap.class) {
                    terminate = true;
                }
            }
        });
        s2container = createS2Container(dicon);

        try {
            synchronized (Bootstrap.class) {
                while (!terminate) {
                    Bootstrap.class.wait();
                }
            }
        }
        catch (final InterruptedException ignore) {
        }
    }

    protected Object createS2Container(final String dicon) throws Exception {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final Class clazz = classLoader.loadClass(INITIALIZER_CLASS);
        final Object initializer = clazz.newInstance();
        if (dicon != null) {
            final Method setConfigPathMethod = clazz.getMethod(SET_CONFIG_PATH_METHOD,
                    new Class[] { String.class });
            setConfigPathMethod.invoke(initializer, new Object[] { dicon });
        }
        final Method iInitializeMethod = initializer.getClass().getMethod(INITIALIZER_METHOD, null);
        return iInitializeMethod.invoke(initializer, null);
    }

    protected String getDicon(final String[] args) throws IllegalArgumentException {
        final String dicon = getArg("--dicon", args);
        return dicon.equals("") ? DEFAULT_DICON_FILE : dicon;
    }

    protected String getClasspath(final String[] args) throws IllegalArgumentException {
        final String classpath = getArg("--classpath", args);
        return classpath.equals("") ? "." : classpath;
    }

    protected String getArg(final String name, final String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(name)) {
                if (i + 1 < args.length) {
                    return args[i + 1];
                }
                throw new IllegalArgumentException(args.toString());
            }
        }
        return "";
    }

    protected void setupClasspath(final String[] pathStrings) throws IOException {
        final List urls = new ArrayList();
        for (int i = 0; i < pathStrings.length; ++i) {
            addPath(urls, new File(pathStrings[i]));
        }

        final File bootstrapJarFile = getBootstrapJarFile();
        if (bootstrapJarFile != null) {
            final File[] files = getJarFiles(bootstrapJarFile.getParentFile());
            for (int i = 0; i < files.length; ++i) {
                addPath(urls, files[i]);
            }
        }

        final ClassLoader classLoader = new URLClassLoader((URL[]) urls
                .toArray(new URL[urls.size()]));
        Thread.currentThread().setContextClassLoader(classLoader);
    }

    protected File getBootstrapJarFile() {
        try {
            final URL url = getClass().getClassLoader().getResource(BOOTSTRAP_CLASS_FILE_NAME);
            final JarURLConnection con = (JarURLConnection) url.openConnection();
            return new File(new URI(con.getJarFileURL().toExternalForm()));
        }
        catch (final Exception e) {
            return null;
        }
    }

    protected void addPath(final List urls, final File path) throws IOException {
        if (path.isDirectory()) {
            final File[] jarFiles = getJarFiles(path);
            if (0 < jarFiles.length) {
                for (int i = 0; i < jarFiles.length; ++i) {
                    urls.add(new URL("jar:" + jarFiles[i].toURL().toExternalForm() + "!/"));
                    logger.log(Level.INFO, "IRMI3004", path.getCanonicalPath());
                }
            }
            else {
                urls.add(path.toURL());
                logger.log(Level.INFO, "IRMI3004", path.getCanonicalPath());
            }
        }
        else {
            if (isJar(path)) {
                urls.add(path.toURL());
                logger.log(Level.INFO, "IRMI3004", path.getCanonicalPath());
            }
        }
    }

    protected File[] getJarFiles(final File dir) {
        return dir.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return isJar(pathname);
            }
        });
    }

    protected boolean isJar(final File pathname) {
        final int dot = pathname.getName().lastIndexOf('.');
        if (0 <= dot) {
            final String extention = pathname.getName().substring(dot + 1);
            return "jar".equalsIgnoreCase(extention);
        }
        return false;
    }

}
