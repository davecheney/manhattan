package net.cheney.manhattan.resource;

import javax.annotation.Nonnull;

import net.cheney.manhattan.resource.Lock.Scope;
import net.cheney.manhattan.resource.Lock.Type;

public interface Lockable {

	@Nonnull Lock lock(Type type, Scope scope);
	
	@Nonnull Lock unlock();

	boolean isLocked();
}
