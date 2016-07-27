/*
 *Copyright (c) 2015, AEGIS DATA SOLUTIONS, All rights reserved. 
 */
package com.aegisql.util.function;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aegisql.util.log.Log4JTestingAppender;
import com.aegisql.util.log.TestingAppender;

// TODO: Auto-generated Javadoc
/**
 * The Class CodeBlockTest.
 * @author Mikhail Teplitskiy
 */
public class CodeBlockTest {

	/** The Constant log. */
	public static final Logger log = LoggerFactory.getLogger("UnitTestLogger");
	
	/** The log events. */
	private static TestingAppender<LoggingEvent> logEvents = Log4JTestingAppender.getAppender("UnitTestLogger");
	
	/** The test name. */
	@Rule
	public TestName testName = new TestName();
	
	/** The watchman. */
	@Rule
	public TestRule watchman = new TestWatcher() {
		public void starting(Description description) {
			super.starting(description);
			log.debug("{} ================================", description.getMethodName());
		}
	};
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * Tear down after class.
	 *
	 * @throws Exception the exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test execute.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testExecute() throws Throwable {
		AtomicInteger x = new AtomicInteger(0);
		CodeBlock b = () -> {
			x.set(1);
		};
		assertEquals(0, x.get());
		b.eval();
		assertEquals(1, x.get());
	}

	/**
	 * Test exception.
	 *
	 * @throws Throwable the throwable
	 */
	@Test(expected=Exception.class)
	public void testException() throws Throwable {
		CodeBlock b = () -> {
			throw new Exception("");
		};
		b.eval();
	}

}
