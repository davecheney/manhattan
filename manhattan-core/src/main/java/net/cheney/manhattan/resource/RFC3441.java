package net.cheney.manhattan.resource;

import static net.cheney.manhattan.resource.RFC4918.DAV_NAMESPACE;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.QName;
import net.cheney.snax.model.Text;

public class RFC3441 {

	public static final QName QUOTA_AVAILABLE_BYTES = QName.valueOf(DAV_NAMESPACE, "quota-available-bytes");
	public static final QName QUOTA_USED_BYTES = QName.valueOf(DAV_NAMESPACE, "quota-used-bytes");
	
	public static Element quotaUsedBytes(long quotaUsedBytes) {
		return new Element(QUOTA_USED_BYTES, new Text(quotaUsedBytes));
	}

	public static Element quotaAvailbleBytes(long quotaAvailbleBytes) {
		return new Element(QUOTA_AVAILABLE_BYTES, new Text(quotaAvailbleBytes));
	}

}
