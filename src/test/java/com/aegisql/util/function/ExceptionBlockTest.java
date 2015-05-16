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

public class ExceptionBlockTest {

	public static final Logger log = LoggerFactory.getLogger("UnitTestLogger");
	
	private static TestingAppender<LoggingEvent> logEvents = Log4JTestingAppender.getAppender("UnitTestLogger");
	
	@Rule
	public TestName testName = new TestName();
	@Rule
	public TestRule watchman = new TestWatcher() {
		public void starting(Description description) {
			super.starting(description);
			log.debug("{} ================================", description.getMethodName());
		}
	};
	
	public static class A extends Throwable {}
	public static class B extends Throwable {}
	
	public static class A1 extends A {}
	public static class B1 extends B {}
	public static class A2 extends A {}
	public static class A3 extends A {}
	public static class A21 extends A2 {}
	public static class B2 extends B {}

	public static class A11 extends A1 {}
	public static class B11 extends B1 {}
	public static class A12 extends A1 {}
	public static class B12 extends B1 {}
	
	public static class B111 extends B11 {}
	
	StringBuilder buff = new StringBuilder();
	
	A a       = new A();
	A1 a1     = new A1();
	A11 a11   = new A11();
	A12 a12   = new A12();
	A2 a2     = new A2();
	A21 a21   = new A21();
	A3 a3     = new A3();

	B b       = new B();
	B1 b1     = new B1();
	B2 b2     = new B2();
	B11 b11   = new B11();
	B12 b12   = new B12();
	B111 b111 = new B111();
	
	Throwable thr = new Throwable("Throwable");
	Exception exc = new Exception("Exception");
	Error err     = new Error("Error");
	
	ExceptionBlock<A> ebA = (A e) -> {
		buff.append("in A: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	ExceptionBlock<A1> ebA1 = (A1 e) -> {
		buff.append("in A1: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	ExceptionBlock<A2> ebA2 = (A2 e) -> {
		buff.append("in A2: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	ExceptionBlock<A11> ebA11 = (A11 e) -> {
		buff.append("in A11: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	ExceptionBlock<A12> ebA12 = (A12 e) -> {
		buff.append("in A12: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	ExceptionBlock<A21> ebA21 = (A21 e) -> {
		buff.append("in A21: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	
	ExceptionBlock<B> ebB = (B e) -> {
		buff.append("in B: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	ExceptionBlock<B1> ebB1 = (B1 e) -> {
		buff.append("in B1: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	ExceptionBlock<B2> ebB2 = (B2 e) -> {
		buff.append("in B2: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	ExceptionBlock<B11> ebB11 = (B11 e) -> {
		buff.append("in B11: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	ExceptionBlock<B12> ebB12 = (B12 e) -> {
		buff.append("in B12: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	ExceptionBlock<B111> ebB111 = (B111 e) -> {
		buff.append("in B111: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};

	ExceptionBlock<Throwable> ebThr = (Throwable e) -> {
		buff.append("in Throwable: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	ExceptionBlock<Exception> ebExc = (Exception e) -> {
		buff.append("in Exception: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};
	ExceptionBlock<Error> ebErr = (Error e) -> {
		buff.append("in Error: "+e.getClass().getSimpleName());
		log.debug(buff.toString());
	};

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		logEvents.reset();
	}

	@After
	public void tearDown() throws Exception {
	}

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

	@Test
	public void testChainedExceptionBlock() throws Throwable {
		
		buff.setLength(0);
		ebA.accept(a);
		assertEquals("in A: A",buff.toString());
		
		buff.setLength(0);
		ebA.accept(a1);
		assertEquals("in A: A1",buff.toString());

	}

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


	private void assertExceptionHandler(String expected, Eval eh, String clazz, Throwable e) throws Throwable {
		buff.setLength(0);
		log.debug("Testing Eval <{}> with <{}>",clazz,e.getClass().getSimpleName());
		boolean res = eh.eval();
		assertFalse(res);
		assertEquals("assertion EH: <"+clazz+"> EX: <"+e.getClass().getSimpleName()+">",expected,buff.toString());
	}
	
	private void assertExceptionHandler(String expected, ExceptionHandler eh, Throwable e) throws Throwable {
		buff.setLength(0);
		log.debug("Testing Handler <{}> with <{}>",eh.getExceptionClass().getSimpleName(),e.getClass().getSimpleName());
		eh.accept(e);
		assertEquals("assertion EH: <"+eh.getExceptionClass().getSimpleName()+"> EX: <"+e.getClass().getSimpleName()+">",expected,buff.toString());
	}
	
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
	
	@Test
	public void testWrappers() throws Throwable {
		ExceptionBlock<Exception> eb1 = (Exception e) -> {
			log.debug("   in Exception "+e.getMessage());
		};

		ExceptionBlock<Exception> eb2 = eb1.logStackTrace("Intercepted: ", log, "debug").andDieAs(RuntimeException.class, "in RuntimeException");

		eb1.accept(new Exception("E"));
		try{
			eb2.accept(new Exception("E"));
		}catch(RuntimeException e) {
			e.printStackTrace(System.out);
		}catch(Throwable t) {
			fail();
		}
		
	}
	
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
