package net.cheney.manhattan.resource.api;

import javax.annotation.Nonnull;

import net.cheney.manhattan.resource.api.Lock.Scope;
import net.cheney.manhattan.resource.api.Lock.Type;

public interface Lockable {

	@Nonnull Lock lock(Type type, Scope scope);
	
	@Nonnull Lock unlock();

	boolean isLocked();
}
