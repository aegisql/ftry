package com.aegisql.util.log;

import java.util.ArrayList;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class Log4JTestingAppender extends AppenderSkeleton implements TestingAppender<LoggingEvent>{
	
	protected Log4JTestingAppender() {
		
	}
	
	private LoggingEvent last = null;
	
	public static Log4JTestingAppender getAppender(String name) {
		Log4JTestingAppender a = new Log4JTestingAppender();
		org.apache.log4j.Logger l = org.apache.log4j.Logger.getLogger(name);
		l.addAppender(a);
		return a;
	}

	private final ArrayList<LoggingEvent> events = new ArrayList<LoggingEvent>();
	
	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	protected void append(LoggingEvent e) {
		events.add(e);
		last = e;
	}
	
	@Override
	public void reset() {
		events.clear();
	}

	@Override
	public ArrayList<LoggingEvent> getEventsSnapshot() {
		return new ArrayList<LoggingEvent>(events);
	}

	@Override
	public LoggingEvent getLastEvent() {
		return last;
	}

	@Override
	public void close() {
	}
	
}
