package com.auto.framework;

import com.auto.framework.auto.AutoConf;
import com.auto.framework.env.TestEnvironment;
import com.auto.framework.iface.ITestCase;
import com.auto.framework.iface.ITestComponent;
import com.auto.framework.reporter.TestReporter;
import com.auto.framework.reporter.data.TestDataReporter;
import com.auto.framework.rules.error.HaltOnErrorRule;
import com.auto.framework.rules.logging.KeepLogRule;
import com.auto.framework.rules.mock.MockRequestResponseRule;
import com.auto.framework.rules.tags.TagsRule;
import com.auto.framework.runner.console.ConsoleStyle;
import com.auto.framework.utils.FileUtil;
import com.auto.framework.utils.JsonUtil;
import com.auto.framework.utils.TestBaseDirectory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
@RunWith(CustomTestRunner.class)
public abstract class AbstractTestCase implements ITestCase {
    public static int MAX_RUN_TIME_MIN = 10;
    public static final String BASE_DIR_ROOT = "/tmp" + File.separator + "auto" + File.separator;
    private final String currentApplication;
    private String baseDir;
    private boolean running = true;
    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();
    @Rule
    public Timeout testGlobalTimeout = new Timeout(MAX_RUN_TIME_MIN, TimeUnit.MINUTES);
    @Rule
    public TestName testName = new TestName();
    @Rule
    public MockRequestResponseRule mockRequestResponseRule = new MockRequestResponseRule();
    @Rule
    public HaltOnErrorRule haltOnErrorRule = new HaltOnErrorRule();
    @Rule
    public KeepLogRule keepLogRule = new KeepLogRule();
    @Rule
    public TagsRule tagsRule = new TagsRule();

    static {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
    }

    protected AutoConf config;

    protected AbstractTestCase(String aApplication) {
        currentApplication = aApplication;
    }

    @Before
    public final void init() {
        decideOnBaseDir();
        initConfiguration();
        initTestEnvironment();
        initReporter();
        initComponents();
        startComponents();
        cleanComponents(true);
        prepareTags();
        prepareComponentsForExecution();
        TestReporter.TRACE(prepareTick() + "Startup sequence finished" + prepareTick());
        TestEnvironment.setReadyTime();
    }

    private void prepareTags() {
        TestDataReporter.addData("tags", tagsRule.getTags(), true);
    }

    private String prepareTick() {
        StringBuilder tick = new StringBuilder(" ");
        for (int i = 0; i < 20; i++) {
            tick.append(ConsoleStyle.TICK).append(" ");
        }
        return tick.toString();
    }

    protected void initConfiguration() {
        try {
            InputStream in = getClass().getResourceAsStream("/auto.json");
            config = JsonUtil.getObjectMapper().readValue(in, AutoConf.class);
            if (StringUtils.isEmpty(config.getResourcePath())) {
                TestReporter.FATAL("config file doesn't have resource path");
            }
            if (!FileUtil.exist(config.getResourcePath())) {
                TestReporter.TRACE("resource path doesn't exists, creating folder with path name " + config.getResourcePath());
                new File(config.getResourcePath()).mkdirs();
            }
            if (CollectionUtils.isEmpty(config.getApplications()) || config.getApplications().stream().noneMatch(a -> getCurrentApplication().equals(a.getName()))) {
                TestReporter.FATAL("config file doesn't have application : " + getCurrentApplication());
            }
        } catch (Exception e) {
            TestReporter.FATAL(e);
        }
    }

    protected void initTestEnvironment() {
        TestEnvironment.init(this, getSandBoxDir(), tmpFolder);
        TestEnvironment.setShouldKeepLog(keepLogRule.keepLog());
        TestEnvironment.setHaltOnError(haltOnErrorRule.isHaltOnError());
        TestEnvironment.setConfig(config);
        TestEnvironment.setCurrentApplication(getCurrentApplication());
    }

    protected String getCurrentApplication() {
        return currentApplication;
    }

    protected AutoConf.Application getCurrentApplicationConfig() {
        return config.getApplications().stream().filter(a -> getCurrentApplication().equals(a.getName())).findFirst().get();
    }

    protected void prepareComponentsForExecution() {
        onAllComponents(com -> {
            if (com.isRunning()) {
                com.prepare();
            }
        });
    }

    private void initReporter() {
        TestReporter.init(this);
        TestReporter.start();
        TestDataReporter.init();
    }

    protected abstract void initComponents();

    public AutoConf getConfig() {
        return config;
    }

    @Override
    public String getTestName() {
        return testName.getMethodName();
    }

    @Override
    public String getTestGroup() {
        return getClass().getName();
    }

    @Override
    public String getSandBoxDir() {
        return baseDir;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public final void tearDown() {
        try {
            tearDownComponents();
        } finally {
            finishReporter();
        }
    }

    private void decideOnBaseDir() {
        baseDir = TestBaseDirectory.generate(this);
        FileUtil.delete(baseDir);
        FileUtil.createLinkSilent(BASE_DIR_ROOT + "current", baseDir);
    }

    protected void tearDownComponents() {
        try {
            setRunning(false);
            TestReporter.TRACE("tearDownComponents");
            TestReporter.TRACE(prepareTick() + "Test completed starting teardown" + prepareTick());
            tearDownComponentsSpecific();
            TestReporter.TRACE("Cleaning On Test Finish");
            clean();
            stopComponents();
            checkExceptions();
            checkMemoryLeaks();
        } finally {
            reportTestExecutionInfo();
        }
    }

    private void setRunning(boolean running) {
        this.running = running;
    }


    protected void reportTestExecutionInfo() {

    }

    private void checkMemoryLeaks() {
    }

    protected void tearDownComponentsSpecific() {
    }

    private void checkExceptions() {
       /* getExceptionChecker().checkExceptions(getSandBoxDir());
        if (isCheckForSuspiciousPatternsInLog()) {
            getExceptionChecker().checkSuspiciousPatterns(getSandBoxDir(), krantiConf);
        }*/
    }

    private void clean() {
        cleanComponents(false);
    }

    protected void stopComponents() {
        onAllComponents(comp -> {
                    if (comp.isRunning()) {
                        comp.stop();
                    }
                }
        );
    }

    protected void startComponents() {
        onAllComponents(comp -> {
            long lStart = System.currentTimeMillis();
            assert comp != null;
            comp.start();
            long lFinish = System.currentTimeMillis();
            recordStartupTime(comp, lFinish - lStart);
        });
    }

    protected void recordStartupTime(ITestComponent comp, long l) {
        TestReporter.TRACE("Startup Time for " + comp.getComponentName() + " in host " + comp.getHost() + " : " + l);
    }

    protected void cleanComponents(final boolean bForce) {
        onAllComponents(comp -> {
                    assert comp != null;
                    comp.clean(bForce);
                },
                comp -> {
                    assert comp != null;
                    return comp.getCleanOrder();
                }
        );
    }

    protected void onAllComponents(final Consumer<ITestComponent> fFunction) {
        onAllComponents(fFunction, null);
    }

    protected void onAllComponents(final Consumer<ITestComponent> fFunction, final Function<ITestComponent, Integer> orderFunc) {
        List<? extends ITestComponent> lstValidComponents = getTestComponents().stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (null != orderFunc) {
            TreeMap<Integer, List<ITestComponent>> treeMap = sortTestComponentsAccordingToOrder(lstValidComponents, orderFunc);
            for (Integer order : treeMap.navigableKeySet()) {
                treeMap.get(order).forEach(fFunction);
            }
        } else {
            lstValidComponents.forEach(fFunction);
        }
    }

    private TreeMap<Integer, List<ITestComponent>> sortTestComponentsAccordingToOrder(List<? extends ITestComponent> lstComponents, final Function<ITestComponent, Integer> orderFunc) {
        TreeMap<Integer, List<ITestComponent>> treeMap = new TreeMap<Integer, List<ITestComponent>>();
        for (ITestComponent t : lstComponents) {
            int order = (null == orderFunc) ? 0 : orderFunc.apply(t);
            List<ITestComponent> lst = treeMap.get(order);
            if (CollectionUtils.isEmpty(lst)) {
                lst = new ArrayList<>();
                treeMap.put(order, lst);
            }
            lst.add(t);
        }
        return treeMap;
    }

    public String getResourcePath() {
        return config.getResourcePath();
    }

    private void finishReporter() {
        TestReporter.finish();
    }

    protected AutoConf.Application getConfigForApp(String appName) {
        if (CollectionUtils.isEmpty(config.getApplications()) || config.getApplications().stream().noneMatch(a -> a.getName().equals(appName))) {
            TestReporter.FATAL("config file doesn't have application : " + appName);
        }
        return config.getApplications().stream().filter(a -> a.getName().equals(appName)).findAny().get();
    }
}
