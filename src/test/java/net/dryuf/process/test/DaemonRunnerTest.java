package net.dryuf.process.test;

import net.dryuf.config.map.MapIniConfig;
import net.dryuf.core.DummyAppContainer;
import net.dryuf.process.AbstractSyncSubProcess;
import net.dryuf.process.DaemonRunner;
import net.dryuf.core.StringUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;


@SuppressWarnings("serial")
public class DaemonRunnerTest
{
	public static class TestingProcess extends AbstractSyncSubProcess
	{
		public                          TestingProcess()
		{
		}

		@Override
		public void                     loop()
		{
			int base = Integer.parseInt(config.getValueDefault("value", "0"));
			@SuppressWarnings("unchecked")
			LinkedBlockingDeque<Integer> queue = appContainer.getBeanTyped("testQueue", LinkedBlockingDeque.class);
			queue.add(base);
			try {
				Thread.sleep(86400000);
			}
			catch (InterruptedException e) {
				queue.add(base+1);
			}
		}
	}

	@Test(timeout = 3000L)
	public void                     testTerminate() throws InterruptedException, ExecutionException
	{
		final DummyAppContainer appContainer = new DummyAppContainer();

		final ConcurrentMap<String, ConcurrentMap<String, String>> testConfig = new ConcurrentHashMap<String, ConcurrentMap<String, String>>() {{
			put("testdaemon", new ConcurrentHashMap<String, String>() {{
				put("processes", "testprocess");
			}});
			put("testprocess", new ConcurrentHashMap<String, String>() {{
				put("class", TestingProcess.class.getName());
			}});
		}};
		appContainer.addBean("testConfig", new MapIniConfig(testConfig));

		LinkedBlockingDeque<Integer> testQueue = new LinkedBlockingDeque<Integer>();
		appContainer.addBean("testQueue", testQueue);

		final DaemonRunner runner = new DaemonRunner()
			.setUseSignalHandlers(false);
		Future<Void> future = runner.startDaemon(appContainer, StringUtil.STRING_EMPTY_ARRAY, "testConfig", "testdaemon");

		Assert.assertEquals(0, (int) testQueue.take());

		runner.handleTermSignal();
		Assert.assertEquals(1, (int) testQueue.take());

		future.get();
	}

	@Test(timeout = 3000L)
	public void                     testReconfigure() throws InterruptedException, ExecutionException
	{
		final DummyAppContainer appContainer = new DummyAppContainer();

		final ConcurrentMap<String, ConcurrentMap<String, String>> testConfig = new ConcurrentHashMap<String, ConcurrentMap<String, String>>() {{
			put("testdaemon", new ConcurrentHashMap<String, String>() {{
				put("processes", "testprocess");
			}});
			put("testprocess", new ConcurrentHashMap<String, String>() {{
				put("class", TestingProcess.class.getName());
				put("dietime", "10");
			}});
		}};
		appContainer.addBean("testConfig", new MapIniConfig(testConfig));

		LinkedBlockingDeque<Integer> testQueue = new LinkedBlockingDeque<Integer>();
		appContainer.addBean("testQueue", testQueue);

		final DaemonRunner runner = new DaemonRunner()
				.setUseSignalHandlers(false);
		Future<Void> future = runner.startDaemon(appContainer, StringUtil.STRING_EMPTY_ARRAY, "testConfig", "testdaemon");

		Assert.assertEquals(0, (int) testQueue.take());

		testConfig.get("testprocess").put("value", "10");
		runner.handleReconfigureSignal();
		Assert.assertEquals(1, (int) testQueue.take());
		Assert.assertEquals(10, (int) testQueue.take());

		runner.handleTermSignal();
		Assert.assertEquals(11, (int) testQueue.take());

		future.get();
	}
}
