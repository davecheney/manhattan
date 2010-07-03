package net.cheney.manhattan.caldav;

import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.QName;

public class RFC4791 {

	public static final Namespace CALDAV_NAMESPACE = Namespace.valueOf("C", "urn:ietf:params:xml:ns:caldav");
	
	public static final QName CALENDAR_DESCRIPTION = QName.valueOf(CALDAV_NAMESPACE, "calendar-description");
	public static final QName CALENDAR_TIMEZONE = QName.valueOf(CALDAV_NAMESPACE, "calendar-timezone");
	public static final QName SUPPORTED_CALENDAR_COMPONENT_SET = QName.valueOf(CALDAV_NAMESPACE, "supported-calendar-component-set");
	public static final QName SUPPORTED_CALENDAR_DATA = QName.valueOf(CALDAV_NAMESPACE, "supported-calendar-data");
	public static final QName MAX_RESOURCE_SIZE = QName.valueOf(CALDAV_NAMESPACE, "max-resource-size");
	public static final QName MIN_DATE_TIME = QName.valueOf(CALDAV_NAMESPACE, "min-date-time");
	public static final QName MAX_DATE_TIME = QName.valueOf(CALDAV_NAMESPACE, "max-date-time");
	public static final QName MAX_INSTANCES = QName.valueOf(CALDAV_NAMESPACE, "max-instances");
	public static final QName MAX_ADDENDEES_PER_INSTANCE = QName.valueOf(CALDAV_NAMESPACE, "max-attendees-per-instance");
	
	private RFC4791() {
		// TODO Auto-generated constructor stub
	}
}
