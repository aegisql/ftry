/*
 *Copyright (c) 2015, AEGIS DATA SOLUTIONS, All rights reserved. 
 */
package com.aegisql.util.log;

import java.util.ArrayList;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * The Class Log4JTestingAppender.
 * @author Mikhail Teplitskiy
 */
public class Log4JTestingAppender extends AppenderSkeleton implements TestingAppender<LoggingEvent>{
	
	/**
	 * Instantiates a new log4 j testing appender.
	 */
	protected Log4JTestingAppender() {
		
	}
	
	/** The last. */
	private LoggingEvent last = null;
	
	/**
	 * Gets the appender.
	 *
	 * @param name the name
	 * @return the appender
	 */
	public static Log4JTestingAppender getAppender(String name) {
		Log4JTestingAppender a = new Log4JTestingAppender();
		org.apache.log4j.Logger l = org.apache.log4j.Logger.getLogger(name);
		l.addAppender(a);
		return a;
	}

	/** The events. */
	private final ArrayList<LoggingEvent> events = new ArrayList<LoggingEvent>();
	
	/* (non-Javadoc)
	 * @see org.apache.log4j.Appender#requiresLayout()
	 */
	@Override
	public boolean requiresLayout() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
	 */
	@Override
	protected void append(LoggingEvent e) {
		events.add(e);
		last = e;
	}
	
	/* (non-Javadoc)
	 * @see com.aegisql.util.log.TestingAppender#reset()
	 */
	@Override
	public void reset() {
		events.clear();
	}

	/* (non-Javadoc)
	 * @see com.aegisql.util.log.TestingAppender#getEventsSnapshot()
	 */
	@Override
	public ArrayList<LoggingEvent> getEventsSnapshot() {
		return new ArrayList<LoggingEvent>(events);
	}

	/* (non-Javadoc)
	 * @see com.aegisql.util.log.TestingAppender#getLastEvent()
	 */
	@Override
	public LoggingEvent getLastEvent() {
		return last;
	}

	/* (non-Javadoc)
	 * @see org.apache.log4j.Appender#close()
	 */
	@Override
	public void close() {
	}
	
}
