package net.cheney.manhattan.resource;

import java.io.File;
import java.net.URI;
import java.util.Collections;

import net.cheney.cocktail.application.Path;
import net.cheney.manhattan.resource.Lock;
import net.cheney.manhattan.resource.LockManager;
import net.cheney.manhattan.resource.Resource;
import net.cheney.manhattan.resource.ResourceProvidor;
import net.cheney.manhattan.resource.Lock.Scope;
import net.cheney.manhattan.resource.Lock.Type;

public class FileResourceProvidor implements ResourceProvidor {

	private final File root;
	private final LockManager lockManager;

	public FileResourceProvidor(String root) {
		this(new File(root));
	}
	
	public FileResourceProvidor(File root) {
		this.root = root;
		this.lockManager = new FileResourceLockManager();
	}
	
	public final Resource resolveResource(Path path) {
		return new FileResource(this, fileForPath(path));
	}
	
	private File fileForPath(Path path) {
		return path.isEmpty() ? root : new File(root, path.toString());
	}

	public final LockManager lockManager() {
		return lockManager;
	}
	
	private class FileResourceLockManager implements LockManager {
		
		@Override
		public boolean isLocked(Resource resource) {
			return false;
		}

		@Override
		public Lock lock(Resource resource, Type type, Scope scope) {
			return new Lock(type, scope, resource);
		}

		@Override
		public Lock unlock(Resource resource) {
			return new Lock(Type.NONE, Scope.NONE, resource);
		}

		@Override
		public Iterable<Lock> activeLocks(Resource resource) {
			return Collections.emptyList();
		}
		
	}

	public URI relativizeResource(Resource resource) {
		return root.toURI().relativize(((FileResource) resource).file().toURI());
	}

	public boolean isRoot(FileResource fileResource) {
		return fileResource.file().equals(root);
	}

	public long quotaUsedBytes() {
		return root.getTotalSpace() - quotaAvailbleBytes();
	}

	public long quotaAvailbleBytes() {
		return root.getUsableSpace();
	}

}
