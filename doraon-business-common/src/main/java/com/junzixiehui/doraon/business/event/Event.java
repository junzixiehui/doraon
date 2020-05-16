package com.junzixiehui.doraon.business.event;

import java.io.Serializable;

public class Event implements Serializable {

	private static final long serialVersionUID = 5740150436439366761L;
	protected String eventId;
	protected String eventType;

	/**
	 * event type,for example "CREATE"/"UPDATE"...
	 * @return
	 */
	public String getEventType() {
		return eventType;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
}
