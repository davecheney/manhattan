package net.cheney.manhattan.resource;

import javax.annotation.Nonnull;

import net.cheney.manhattan.resource.Lock.Scope;
import net.cheney.manhattan.resource.Lock.Type;

public interface LockManager {

	@Nonnull Lock lock(Resource resource, Type type, Scope scope);
	
	@Nonnull Lock unlock(Resource resource);

	boolean isLocked(Resource resource);
}