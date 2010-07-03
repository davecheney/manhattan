package net.cheney.manhattan.resource.api;

import java.io.IOException;

public interface Moveable {

	void moveTo(CollectionResource destination) throws IOException;
}
