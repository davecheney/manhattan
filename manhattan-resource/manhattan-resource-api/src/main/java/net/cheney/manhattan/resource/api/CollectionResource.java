package net.cheney.manhattan.resource.api;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

public interface CollectionResource {

	@Nonnull Resource create(String name, ByteBuffer buffer) throws IOException;
	
	@Nonnull Resource child(String name);
	
	@Nonnull Iterable<Resource> children();
}
