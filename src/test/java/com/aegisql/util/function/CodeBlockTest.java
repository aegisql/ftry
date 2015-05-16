package com.aegisql.util.function;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CodeBlockTest {

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
	public void testExecute() throws Throwable {
		AtomicInteger x = new AtomicInteger(0);
		CodeBlock b = () -> {
			x.set(1);
		};
		assertEquals(0, x.get());
		b.eval();
		assertEquals(1, x.get());
	}

	@Test(expected=Exception.class)
	public void testException() throws Throwable {
		CodeBlock b = () -> {
			throw new Exception("");
		};
		b.eval();
	}

}
