package net.cheney.manhattan.resource.file;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.cheney.manhattan.resource.api.Lock;
import net.cheney.manhattan.resource.api.LockManager;
import net.cheney.manhattan.resource.api.Resource;
import net.cheney.manhattan.resource.api.Lock.Scope;
import net.cheney.manhattan.resource.api.Lock.Type;

public class InMemoryLockManager implements LockManager {
	
	private final Multimap<Resource, Lock> resourceLocks = ArrayListMultimap.create();
	
	@Override
	public boolean isLocked(Resource resource) {
		return resourceLocks.containsKey(resource);
	}

	@Override
	public Lock lock(Resource resource, Type type, Scope scope) {
		Lock lock = new Lock(type, scope, resource);
		resourceLocks.get(resource).add(lock);
		return lock;
	}

	@Override
	public Lock unlock(Resource resource) {
		resourceLocks.get(resource).clear();
		return new Lock(Type.NONE, Scope.NONE, resource);
	}

	@Override
	public Iterable<Lock> activeLocks(Resource resource) {
		return resourceLocks.get(resource);
	}
	
}