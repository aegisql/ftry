/*
 *Copyright (c) 2015, AEGIS DATA SOLUTIONS, All rights reserved. 
 */
package com.aegisql.util.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.apache.log4j.Level;
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

/**
 * The Class ExceptionBlockTest.
 * @author Mikhail Teplitskiy
 */
public class ExceptionBlockTest {

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
	 * The Class A.
	 */
	public static class A extends Throwable {}
	
	/**
	 * The Class B.
	 */
	public static class B extends Throwable {}
	
	/**
	 * The Class A1.
	 */
	public static class A1 extends A {}
	
	/**
	 * The Class B1.
	 */
	public static class B1 extends B {}
	
	/**
	 * The Class A2.
	 */
	public static class A2 extends A {}
	
	/**
	 * The Class A3.
	 */
	public static class A3 extends A {}
	
	/**
	 * The Class A21.
	 */
	public static class A21 extends A2 {}
	
	/**
	 * The Class B2.
	 */
	public static class B2 extends B {}

	/**
	 * The Class A11.
	 */
	public static class A11 extends A1 {}
	
	/**
	 * The Class B11.
	 */
	public static class B11 extends B1 {}
	
	/**
	 * The Class A12.
	 */
	public static class A12 extends A1 {}
	
	/**
	 * The Class B12.
	 */
	public static class B12 extends B1 {}
	
	/**
	 * The Class B111.
	 */
	public static class B111 extends B11 {}
	
	/** The buff. */
	StringBuilder buff = new StringBuilder();
	
	/** The a. */
	A a       = new A();
	
	/** The a1. */
	A1 a1     = new A1();
	
	/** The a11. */
	A11 a11   = new A11();
	
	/** The a12. */
	A12 a12   = new A12();
	
	/** The a2. */
	A2 a2     = new A2();
	
	/** The a21. */
	A21 a21   = new A21();
	
	/** The a3. */
	A3 a3     = new A3();

	/** The b. */
	B b       = new B();
	
	/** The b1. */
	B1 b1     = new B1();
	
	/** The b2. */
	B2 b2     = new B2();
	
	/** The b11. */
	B11 b11   = new B11();
	
	/** The b12. */
	B12 b12   = new B12();
	
	/** The b111. */
	B111 b111 = new B111();
	
	/** The thr. */
	Throwable thr = new Throwable("Throwable");
	
	/** The exc. */
	Exception exc = new Exception("Exception");
	
	/** The err. */
	Error err     = new Error("Error");
	
	/** The eb a. */
	ExceptionBlock<A> ebA = (A e) -> {
		buff.append("in A: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	
	/** The eb a1. */
	ExceptionBlock<A1> ebA1 = (A1 e) -> {
		buff.append("in A1: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	
	/** The eb a2. */
	ExceptionBlock<A2> ebA2 = (A2 e) -> {
		buff.append("in A2: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	
	/** The eb a11. */
	ExceptionBlock<A11> ebA11 = (A11 e) -> {
		buff.append("in A11: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	
	/** The eb a12. */
	ExceptionBlock<A12> ebA12 = (A12 e) -> {
		buff.append("in A12: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	
	/** The eb a21. */
	ExceptionBlock<A21> ebA21 = (A21 e) -> {
		buff.append("in A21: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	
	/** The eb b. */
	ExceptionBlock<B> ebB = (B e) -> {
		buff.append("in B: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	
	/** The eb b1. */
	ExceptionBlock<B1> ebB1 = (B1 e) -> {
		buff.append("in B1: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	
	/** The eb b2. */
	ExceptionBlock<B2> ebB2 = (B2 e) -> {
		buff.append("in B2: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	
	/** The eb b11. */
	ExceptionBlock<B11> ebB11 = (B11 e) -> {
		buff.append("in B11: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	
	/** The eb b12. */
	ExceptionBlock<B12> ebB12 = (B12 e) -> {
		buff.append("in B12: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	
	/** The eb b111. */
	ExceptionBlock<B111> ebB111 = (B111 e) -> {
		buff.append("in B111: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};

	/** The eb thr. */
	ExceptionBlock<Throwable> ebThr = (Throwable e) -> {
		buff.append("in Throwable: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	
	/** The eb exc. */
	ExceptionBlock<Exception> ebExc = (Exception e) -> {
		buff.append("in Exception: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	
	/** The eb err. */
	ExceptionBlock<Error> ebErr = (Error e) -> {
		buff.append("in Error: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
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
		logEvents.reset();
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
	 * Test simple exception block.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testSimpleExceptionBlock() throws Throwable {
		AtomicInteger x = new AtomicInteger(0);
		ExceptionHandler<Exception> eb = new ExceptionHandler<>(Exception.class,(Exception e) -> {
			x.incrementAndGet();
		});
		assertEquals(0, x.get());
		eb.accept(new Exception());;
		assertEquals(1, x.get());
	}

	/**
	 * Test chained exception block.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testChainedExceptionBlock() throws Throwable {
		
		buff.setLength(0);
		ebA.accept(a);
		assertEquals("in A: A",buff.toString());
		
		buff.setLength(0);
		ebA.accept(a1);
		assertEquals("in A: A1",buff.toString());

	}

	/**
	 * Test as.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testAs() throws Throwable {
		ExceptionHandler<Throwable> eh = new ExceptionHandler<>(A12.class,ebA12)
			.orCatch(A11.class, ebA11)
			.orCatch(A2.class, ebA2)
			.orCatch(A1.class, ebA1)
			.orCatch(A.class, ebA)
			.orCatch(Throwable.class, ebThr);


		assertExceptionHandler("in A: A",eh,a);
		assertExceptionHandler("in A2: A2",eh,a2);
		assertExceptionHandler("in A1: A1",eh,a1);
		assertExceptionHandler("in A11: A11",eh,a11);
		assertExceptionHandler("in A12: A12",eh,a12);
		assertExceptionHandler("in A2: A21",eh,a21);
		assertExceptionHandler("in A: A3",eh,a3);

		//These all excected in Throwable
		assertExceptionHandler("in Throwable: B",eh,b);
		assertExceptionHandler("in Throwable: B1",eh,b1);
		assertExceptionHandler("in Throwable: B2",eh,b2);
		assertExceptionHandler("in Throwable: B11",eh,b11);
		assertExceptionHandler("in Throwable: B12",eh,b12);
		assertExceptionHandler("in Throwable: B111",eh,b111);

		assertExceptionHandler("in Throwable: Error",eh,err);
		assertExceptionHandler("in Throwable: Exception",eh,exc);
		assertExceptionHandler("in Throwable: Throwable",eh,thr);

	}

	/**
	 * Test as in try.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testAsInTry() throws Throwable {
		ExceptionHandler<Throwable> eh = new ExceptionHandler<>(A12.class,ebA12)
			.orCatch(A11.class, ebA11)
			.orCatch(A2.class, ebA2)
			.orCatch(A1.class, ebA1)
			.orCatch(A.class, ebA)
			.orCatch(Throwable.class, ebThr);


		Try tA = new Try(()-> { throw a; })
			.orCatch(A12.class,ebA12)
			.orCatch(A11.class, ebA11)
			.orCatch(A2.class, ebA2)
			.orCatch(A1.class, ebA1)
			.orCatch(A.class, ebA)
			.orCatch(Throwable.class, ebThr);
		;
		
		assertExceptionHandler("in A: A",tA.evaluator(),"A",a);

		Try tA1 = new Try(()-> { throw a1; }).orCatch(Throwable.class, eh);
		
		assertExceptionHandler("in A1: A1",tA1.evaluator(),"A1",a1);

	}
	
	/**
	 * Test bs.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testBs() throws Throwable {
		ExceptionHandler<Throwable> eh = new ExceptionHandler<>(B111.class,ebB111)
			.orCatch(B11.class, ebB11)
			.orCatch(B12.class, ebB12)
			.orCatch(B2.class, ebB2)
			.orCatch(B1.class, ebB1)
			.orCatch(B.class, ebB)
			.orCatch(Throwable.class, ebThr);

		//These all excected in Throwable
		assertExceptionHandler("in Throwable: A",eh,a);
		assertExceptionHandler("in Throwable: A2",eh,a2);
		assertExceptionHandler("in Throwable: A1",eh,a1);
		assertExceptionHandler("in Throwable: A11",eh,a11);
		assertExceptionHandler("in Throwable: A12",eh,a12);
		assertExceptionHandler("in Throwable: A21",eh,a21);
		assertExceptionHandler("in Throwable: A3",eh,a3);

		assertExceptionHandler("in B: B",eh,b);
		assertExceptionHandler("in B1: B1",eh,b1);
		assertExceptionHandler("in B2: B2",eh,b2);
		assertExceptionHandler("in B11: B11",eh,b11);
		assertExceptionHandler("in B12: B12",eh,b12);
		assertExceptionHandler("in B111: B111",eh,b111);

		assertExceptionHandler("in Throwable: Error",eh,err);
		assertExceptionHandler("in Throwable: Exception",eh,exc);
		assertExceptionHandler("in Throwable: Throwable",eh,thr);

	}

	/**
	 * Test a bs.
	 *
	 * @throws Throwable the throwable
	 */
	@Test(expected=Exception.class)
	public void testABs() throws Throwable {
		ExceptionHandler eh = new ExceptionHandler<>(B111.class,ebB111)
			.orCatch(B11.class, ebB11)
			.orCatch(B12.class, ebB12)
			.orCatch(B2.class, ebB2)
			.orCatch(B1.class, ebB1.printStackTrace())
			.orCatch(A12.class,ebA12)
			.orCatch(A11.class, ebA11)
			.orCatch(A2.class, ebA2)
			.orCatch(A1.class, ebA1)
			.orCatch(A.class, ebA)
			.orCatch(B.class, ebB)
//			.orCatch(Throwable.class, ebThr)
			;

		assertExceptionHandler("in A: A",eh,a);
		assertExceptionHandler("in A2: A2",eh,a2);
		assertExceptionHandler("in A1: A1",eh,a1);
		assertExceptionHandler("in A11: A11",eh,a11);
		assertExceptionHandler("in A12: A12",eh,a12);
		assertExceptionHandler("in A2: A21",eh,a21);
		assertExceptionHandler("in A: A3",eh,a3);

		
		assertExceptionHandler("in B: B",eh,b);
		assertExceptionHandler("in B1: B1",eh,b1);
		assertExceptionHandler("in B2: B2",eh,b2);
		assertExceptionHandler("in B11: B11",eh,b11);
		assertExceptionHandler("in B12: B12",eh,b12);
		assertExceptionHandler("in B111: B111",eh,b111);

		//These is not intercepted and should throw Exception
		eh.accept(exc);
	}


	/**
	 * Assert exception handler.
	 *
	 * @param expected the expected
	 * @param eh the eh
	 * @param clazz the clazz
	 * @param e the e
	 * @throws Throwable the throwable
	 */
	private void assertExceptionHandler(String expected, Eval eh, String clazz, Throwable e) throws Throwable {
		buff.setLength(0);
		log.debug("Testing Eval <{}> with <{}>",clazz,e.getClass().getSimpleName());
		boolean res = eh.eval();
		assertFalse(res);
		assertEquals("assertion EH: <"+clazz+"> EX: <"+e.getClass().getSimpleName()+">",expected,buff.toString());
	}
	
	/**
	 * Assert exception handler.
	 *
	 * @param expected the expected
	 * @param eh the eh
	 * @param e the e
	 * @throws Throwable the throwable
	 */
	private void assertExceptionHandler(String expected, ExceptionHandler eh, Throwable e) throws Throwable {
		buff.setLength(0);
		log.debug("Testing Handler <{}> with <{}>",eh.getExceptionClass().getSimpleName(),e.getClass().getSimpleName());
		eh.accept(e);
		assertEquals("assertion EH: <"+eh.getExceptionClass().getSimpleName()+"> EX: <"+e.getClass().getSimpleName()+">",expected,buff.toString());
	}
	
	/**
	 * Test error exception block.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testErrorExceptionBlock() throws Throwable {
		AtomicInteger x = new AtomicInteger(0);
		
		
		ExceptionHandler<RuntimeException> eb0 = new ExceptionHandler<>(RuntimeException.class,(RuntimeException e) -> {
			log.debug("   in RuntimeException "+e.getMessage());
		});
		
		ExceptionBlock<Exception> eb1 = (Exception e) -> {
			log.debug("   in Exception "+e.getMessage());
		};
		ExceptionBlock<Throwable> eb2 = (Throwable e) -> {
			log.debug("   in Throwable "+e.getMessage());
			x.incrementAndGet();
		};
		
		ExceptionBlock<Error> error = (Error t) -> {
			log.debug("   in Error");
		};
		//.orCatch(Error.class, error)
		ExceptionHandler<Throwable> eb = eb0.orCatch(Exception.class, eb1).orCatch(Throwable.class, eb2);
		
		eb.accept(new Exception("Ex"));
		eb.accept(new Throwable("Th"));
		eb.accept(new NullPointerException("NPE"));
		eb.accept(new RuntimeException("Re"));
		eb.accept(new Error("Error"));
	}

	/**
	 * Test same exception block.
	 *
	 * @throws Throwable the throwable
	 */
	@Test(expected=RuntimeException.class)
	public void testSameExceptionBlock() throws Throwable {
		
		
		ExceptionHandler<RuntimeException> eb0 = new ExceptionHandler<>(RuntimeException.class,(RuntimeException e) -> {
			log.debug("   in RuntimeException "+e.getMessage());
		});
		
		ExceptionBlock<RuntimeException> eb1 = (RuntimeException e) -> {
			log.debug("   in RuntimeException 2 "+e.getMessage());
		};
		ExceptionHandler<RuntimeException> eb = eb0.orCatch(RuntimeException.class, eb1);
		
		eb.accept(new NullPointerException("NPE"));
		eb.accept(new RuntimeException("Re"));
	}

	/**
	 * Test error exception block with2 handlers of same class.
	 *
	 * @throws Throwable the throwable
	 */
	@Test(expected=RuntimeException.class)
	public void testErrorExceptionBlockWith2HandlersOfSameClass() throws Throwable {
		ExceptionHandler<RuntimeException> eb0 = new ExceptionHandler<>(RuntimeException.class,(RuntimeException e) -> {
			log.debug("   in RuntimeException "+e.getMessage());
		});
		
		ExceptionBlock<Exception> eb1 = (Exception e) -> {
			log.debug("   in Exception "+e.getMessage());
		};
		ExceptionBlock<RuntimeException> eb2 = (RuntimeException e) -> {
			log.debug("   in RuntimeException 2 "+e.getMessage());
		};
		
		ExceptionHandler eb = eb0.orCatch(Exception.class, eb1).orCatch(RuntimeException.class, eb2);
		
		eb.accept(new Exception("Ex"));
		eb.accept(new NullPointerException("NPE"));
		eb.accept(new RuntimeException("Re"));
	}
	
	/**
	 * Test wrappers.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testWrappers() throws Throwable {
		ExceptionBlock<Exception> eb1 = (Exception e) -> {
			log.debug("   in Exception "+e.getMessage());
		};

		ExceptionBlock<Exception> eb2 = eb1.logDebug(log, "Intercepted: {} {}\n","param1","param2").andDieAs(RuntimeException.class, "in RuntimeException");

		eb1.accept(new Exception("E"));
		try{
			eb2.accept(new Exception("E"));
		}catch(RuntimeException e) {
			e.printStackTrace(System.out);
		}catch(Throwable t) {
			fail();
		}
		
	}
	
	/**
	 * Test error2 exception block.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testError2ExceptionBlock() throws Throwable {
		AtomicInteger x = new AtomicInteger(0);
		

		ExceptionHandler<NullPointerException> eb01 = new ExceptionHandler<>(NullPointerException.class,(NullPointerException e) -> {
			log.debug("   in NullPointerException "+e.getMessage());
		});

		ExceptionBlock<NumberFormatException> eb02 = (NumberFormatException e) -> {
			log.debug("   in NumberFormatException "+e.getMessage());
		};
		
		ExceptionBlock<RuntimeException> eb0 = (RuntimeException e) -> {
			log.debug("   in RuntimeException "+e.getMessage());
		};

		ExceptionBlock<TimeoutException> eb11 = (TimeoutException e) -> {
			log.debug("   in TimeoutException "+e.getMessage());
		};

		ExceptionBlock<Exception> eb1 = (Exception e) -> {
			log.debug("   in Exception "+e.getMessage());
		};
		ExceptionBlock<Throwable> eb2 = (Throwable e) -> {
			log.debug("   in Throwable "+e.getMessage());
			x.incrementAndGet();
		};
		
		ExceptionBlock<Error> error = (Error t) -> {
			log.debug("   in Error");
		};
		//.orCatch(Error.class, error)
		ExceptionHandler<Throwable> eb = eb01
				.orCatch(NumberFormatException.class, eb02)
				.orCatch(RuntimeException.class,eb0)
				.orCatch(TimeoutException.class, eb11)
				.orCatch(Exception.class, eb1)
				.orCatch(Throwable.class, eb2);
		
		eb.accept(new Exception("Ex"));
		eb.accept(new Throwable("Th"));
		eb.accept(new NumberFormatException("NFE"));
		eb.accept(new NullPointerException("NPE"));
		eb.accept(new RuntimeException("Re"));
		eb.accept(new TimeoutException("TimeoutException"));
		eb.accept(new Error("Error"));
	}

	
	/**
	 * Test shorter exception block.
	 *
	 * @throws Throwable the throwable
	 */
	@Test(expected=Error.class)
	public void testShorterExceptionBlock() throws Throwable {
		AtomicInteger x = new AtomicInteger(0);
		
		
		ExceptionHandler<RuntimeException> eb0 = new ExceptionHandler<RuntimeException>(RuntimeException.class,(RuntimeException e) -> {
			log.debug("in RuntimeException "+e.getMessage());
		});
		
		ExceptionBlock<Exception> eb1 = (Exception e) -> {
			log.debug("in Exception "+e.getMessage());
		};
		ExceptionBlock<Throwable> eb2 = (Throwable e) -> {
			log.debug("in Throwable "+e.getMessage());
			x.incrementAndGet();
		};
		
		ExceptionBlock<Error> error = (Error t) -> {
			log.debug("in Error");
		};
		//.orCatch(Error.class, error)
		ExceptionHandler eb = eb0.orCatch(Exception.class, eb1);
		
		eb.accept(new Exception("Ex"));
		eb.accept(new NullPointerException("NPE"));
		eb.accept(new RuntimeException("Re"));
		eb.accept(new Error("Error"));
	}

	/**
	 * Test log appender.
	 */
	@Test
	public void testLogAppender() {
		log.debug("TEST");
		ArrayList<LoggingEvent> e = logEvents.getEventsSnapshot();
		assertEquals(1, e.size());
		LoggingEvent le = e.get(0);
		assertEquals("TEST", le.getMessage());
		assertEquals(Level.DEBUG, le.getLevel());
		log.error("ERROR");
		e = logEvents.getEventsSnapshot();
		assertEquals(2, e.size());
		LoggingEvent le1 = e.get(1);
		assertEquals("ERROR", le1.getMessage());
		assertEquals(Level.ERROR, le1.getLevel());
		
		LoggingEvent le2 = logEvents.getLastEvent();
		assertEquals(le1, le2);
	}
	
	/**
	 * Test throwable factory.
	 */
	@Test
	public void testThrowableFactory() {
		RuntimeException re = (RuntimeException) ExceptionHandler.<RuntimeException>wrapCommentedThrowable()
		.apply(RuntimeException.class)
		.apply("Wrapped").apply(new Exception("EXCEPTION"));
		assertNotNull(re);
		assertEquals("Wrapped", re.getMessage());
		assertEquals("EXCEPTION", re.getCause().getMessage());
		
		Function<String,Function<Throwable,RuntimeException>> part1 = ExceptionHandler.<RuntimeException>wrapCommentedThrowable()
				.apply(RuntimeException.class);
		
		Function<Throwable,RuntimeException> part21 = part1.apply("TEST1");
		Function<Throwable,RuntimeException> part22 = part1.apply("TEST2");
		
		RuntimeException re1 = part21.apply(new NullPointerException("NPE"));
		RuntimeException re2 = part22.apply(new NumberFormatException("NFE"));

		assertEquals("TEST1", re1.getMessage());
		assertEquals("NPE", re1.getCause().getMessage());

		assertEquals("TEST2", re2.getMessage());
		assertEquals("NFE", re2.getCause().getMessage());
		
	}
	

	/**
	 * Test nested try with un cathed runtime exception.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testNestedTryWithUnCathedRuntimeException() throws Throwable {

		Try t = new Try(()->{
			System.out.println("testNestedTryWithUnCathedRuntimeException");
			throw new Exception("ERROR");
		}).orCatch(Exception.class, (Exception e)->{
			System.out.println("SimpleTryTest Exception: "+e.getMessage());
		}).withFinal(()->{
			System.out.println("SimpleTryTest finalize");
			throw new Exception("FINAL ERROR");
		})
		;

		Try n = new Try( t.evaluator().toCodeBlock() ).orCatch(Exception.class, (Exception e)->{
			System.out.println("Nested Exception: "+e.getMessage());
		});
		
		boolean result = n.runtimeEvaluator().eval();
		assertFalse(result);
	}

	/**
	 * Test nested try with cathed runtime exception.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testNestedTryWithCathedRuntimeException() throws Throwable {

		Try t = new Try(()->{
			System.out.println("testNestedTryWithCathedRuntimeException");
			throw new Exception("ERROR");
		}).orCatch(Exception.class, (Exception e)->{
			System.out.println("SimpleTryTest Exception: "+e.getMessage());
		}).withFinal(()->{
			System.out.println("SimpleTryTest finalize");
		})
		;

		AtomicBoolean ab = new AtomicBoolean(true);
		Try n = new Try( t.evaluator().toCodeBlock(ab) )
			.orCatch(Exception.class, (Exception e)->{
				System.out.println("Nested Exception: "+e.getMessage());
			});
		
		boolean result = n.runtimeEvaluator().eval();
		assertFalse(ab.get());
		assertTrue(result);
	}
	
}
