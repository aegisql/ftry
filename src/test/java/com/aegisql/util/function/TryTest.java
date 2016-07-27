/*
 *Copyright (c) 2015, AEGIS DATA SOLUTIONS, All rights reserved. 
 */
package com.aegisql.util.function;

import static org.junit.Assert.*;

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
 * The Class TryTest.
 * @author Mikhail Teplitskiy
*/
public class TryTest {

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
	 * Test simple try.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testSimpleTry() throws Throwable {

		Try t = new Try(()->{
			System.out.println("SimpleTryTest");
		}).orCatch(Exception.class, (Exception e)->{
			System.out.println("SimpleTryTest Exception: "+e.getMessage());
		}).withFinal(()->{
			System.out.println("SimpleTryTest finalize");
		})
		;

		boolean result = t.evaluator().eval();
		assertTrue(result);
	}

	/**
	 * Test simple checked try.
	 */
	@Test
	public void testSimpleCheckedTry() {

		Try t = new Try(()->{
			System.out.println("SimpleTryTest");
		}).orCatch(Exception.class, (Exception e)->{
			System.out.println("SimpleTryTest Exception: "+e.getMessage());
		}).withFinal(()->{
			System.out.println("SimpleTryTest finalize");
		})
		;

		boolean result = t.runtimeEvaluator().eval();
		assertTrue(result);
	}

	/**
	 * Test simple wrapped try.
	 */
	@Test
	public void testSimpleWrappedTry() {

		Try t = new Try(()->{
			System.out.println("SimpleTryTest");
		}).orCatch(Exception.class, (Exception e)->{
			System.out.println("SimpleTryTest Exception: "+e.getMessage());
		}).withFinal(()->{
			System.out.println("SimpleTryTest finalize");
		})
		;

		Result<Boolean> result = t.wrappedEvaluator().eval();
		assertTrue(result.get());
	}

	
	/**
	 * Test simple try with cathed exception.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testSimpleTryWithCathedException() throws Throwable {

		Try t = new Try(()->{
			System.out.println("SimpleTryTest");
			throw new Exception("ERROR");
		}).orCatch(Exception.class, (Exception e)->{
			System.out.println("SimpleTryTest Exception: "+e.getMessage());
		}).withFinal(()->{
			System.out.println("SimpleTryTest finalize");
		})
		;

		boolean result = t.evaluator().eval();
		assertFalse(result);
	}

	/**
	 * Test simple try with cathed wrapped exception.
	 */
	@Test
	public void testSimpleTryWithCathedWrappedException() {

		Try t = new Try(()->{
			System.out.println("SimpleTryTest");
			throw new Exception("ERROR");
		}).orCatch(Exception.class, (Exception e)->{
			System.out.println("SimpleTryTest Exception: "+e.getMessage());
		}).withFinal(()->{
			System.out.println("SimpleTryTest finalize");
		})
		;

		boolean result = t.runtimeEvaluator().eval();
		assertFalse(result);
	}

	/**
	 * Test simple wrapped try with catched exception.
	 */
	@Test
	public void testSimpleWrappedTryWithCatchedException() {

		Try t = new Try(()->{
			System.out.println("SimpleTryTest");
			throw new Exception("ERROR");
		}).orCatch(Exception.class, (Exception e)->{
			System.out.println("SimpleTryTest Exception: "+e.getMessage());
		}).withFinal(()->{
			System.out.println("SimpleTryTest finalize");
		})
		;

		Result<Boolean> result = t.wrappedEvaluator().eval();
		System.out.println(result);
		assertFalse(result.get());
	}

	
	/**
	 * Test simple try with un cathed exception.
	 *
	 * @throws Throwable the throwable
	 */
	@Test(expected=Exception.class)
	public void testSimpleTryWithUnCathedException() throws Throwable {

		Try t = new Try(()->{
			System.out.println("SimpleTryTest");
			throw new Exception("ERROR");
		}).orCatch(Exception.class, (Exception e)->{
			System.out.println("SimpleTryTest Exception: "+e.getMessage());
		}).withFinal(()->{
			System.out.println("SimpleTryTest finalize");
			throw new Exception("FINAL ERROR");
		})
		;

		boolean result = t.evaluator().eval();
		assertFalse(result);
	}

	/**
	 * Test simple try with un cathed runtime exception.
	 */
	@Test(expected=RuntimeException.class)
	public void testSimpleTryWithUnCathedRuntimeException() {

		Try t = new Try(()->{
			System.out.println("SimpleTryTest");
			throw new Exception("ERROR");
		}).orCatch(Exception.class, (Exception e)->{
			System.out.println("SimpleTryTest Exception: "+e.getMessage());
		}).withFinal(()->{
			System.out.println("SimpleTryTest finalize");
			throw new Exception("FINAL ERROR");
		})
		;

		boolean result = t.runtimeEvaluator().eval();
		assertFalse(result);
	}

	/**
	 * Test simple try with un cathed checked exception.
	 *
	 * @throws Exception the exception
	 */
	@Test(expected=Exception.class)
	public void testSimpleTryWithUnCathedCheckedException() throws Exception {

		Try t = new Try(()->{
			System.out.println("SimpleTryTest");
			throw new Exception("ERROR");
		}).orCatch(Exception.class, (Exception e)->{
			System.out.println("SimpleTryTest Exception: "+e.getMessage());
		}).withFinal(()->{
			System.out.println("SimpleTryTest finalize");
			throw new Exception("FINAL ERROR");
		})
		;

		Eval<Exception> ev = t.<Exception>checkedEvaluator(Exception.class);
		boolean result = ev.eval();
		assertFalse(result);
	}

	
	/**
	 * Test simple wrapped try with un catched exception.
	 */
	@Test
	public void testSimpleWrappedTryWithUnCatchedException() {

		Try t = new Try(()->{
			System.out.println("SimpleTryTest");
			throw new Exception("ERROR");
		}).orCatch(Exception.class, (Exception e)->{
			System.out.println("SimpleTryTest Exception: "+e.getMessage());
		}).withFinal(()->{
			System.out.println("SimpleTryTest finalize");
			throw new Exception("FINAL ERROR");
		})
		;

		EvalStatus result = t.wrappedEvaluator().eval();
		System.out.println(result);
		assertFalse(result.success());
	}

	/**
	 * Test simple try with un cathed un handeled exception.
	 *
	 * @throws Throwable the throwable
	 */
	@Test(expected=Exception.class)
	public void testSimpleTryWithUnCathedUnHandeledException() throws Throwable {

		Try t = new Try(()->{
			System.out.println("SimpleTryTest");
			throw new Exception("ERROR");
		}).withFinal(()->{
			System.out.println("SimpleTryTest finalize");
		})
		;

		boolean result = t.evaluator().eval();
		assertFalse(result);
	}

	
}
