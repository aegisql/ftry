/*
 *Copyright (c) 2015, AEGIS DATA SOLUTIONS, All rights reserved. 
 */
package com.aegisql.util.log;

import java.util.ArrayList;

/**
 * The Interface TestingAppender.
 * @author Mikhail Teplitskiy
 *
 * @param <L> the generic type
 */
public interface TestingAppender<L> {
	
	/**
	 * Reset.
	 */
	public void reset();
	
	/**
	 * Gets the events snapshot.
	 *
	 * @return the events snapshot
	 */
	public ArrayList<L> getEventsSnapshot();
	
	/**
	 * Gets the last event.
	 *
	 * @return the last event
	 */
	public L getLastEvent();
}
