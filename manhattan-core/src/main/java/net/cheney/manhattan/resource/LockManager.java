package net.cheney.manhattan.resource;

import javax.annotation.Nonnull;

import net.cheney.manhattan.resource.Lock.Scope;
import net.cheney.manhattan.resource.Lock.Type;

public interface LockManager {

	@Nonnull Lock lock(@Nonnull Resource resource, @Nonnull Type type, @Nonnull Scope scope);
	
	@Nonnull Lock unlock(@Nonnull Resource resource);

	boolean isLocked(@Nonnull Resource resource);

	@Nonnull Iterable<Lock> activeLocks(@Nonnull Resource resource);
}