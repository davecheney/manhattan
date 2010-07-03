package net.cheney.manhattan.resource.api;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public interface Getable {

	@Nonnegative long contentLength();

	@Nonnull ByteBuffer body() throws IOException;
}
