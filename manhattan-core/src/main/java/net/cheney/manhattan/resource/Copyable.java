package net.cheney.manhattan.resource;

import java.io.IOException;

public interface Copyable {

	void copyTo(Resource destination) throws IOException;
}
