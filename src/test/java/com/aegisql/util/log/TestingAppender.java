package com.aegisql.util.log;

import java.util.ArrayList;

public interface TestingAppender<L> {
	public void reset();
	public ArrayList<L> getEventsSnapshot();
	public L getLastEvent();
}
