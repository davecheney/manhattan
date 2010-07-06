package net.cheney.manhattan.resource.api;

import java.io.File;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public interface Getable {
	
	@Nonnull String name();

	@Nonnegative long contentLength();

	@Nonnull File file();
}
