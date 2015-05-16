package com.aegisql.util.function;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TryTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

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

		Result<Boolean> result = t.wrapedEvaluator().eval();
		assertTrue(result.get());
	}

	
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

		Result<Boolean> result = t.wrapedEvaluator().eval();
		System.out.println(result);
		assertFalse(result.get());
	}

	
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

		EvalStatus result = t.wrapedEvaluator().eval();
		System.out.println(result);
		assertFalse(result.success());
	}

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
