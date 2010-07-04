package net.cheney.manhattan.resource.file;

import java.io.File;

import net.cheney.cocktail.application.Path;
import net.cheney.manhattan.resource.api.LockManager;
import net.cheney.manhattan.resource.api.Resource;
import net.cheney.manhattan.resource.api.ResourceProvidor;

public class FileResourceProvidor implements ResourceProvidor {

	private final File root;
	private final LockManager lockManager;

	public FileResourceProvidor(LockManager lockManager, String root) {
		this(lockManager, new File(root));
	}
	
	public FileResourceProvidor(LockManager lockManager, File root) {
		this.root = root;
		this.lockManager = lockManager;
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
	
//	public URI relativizeResource(Resource resource) {
//		return root.toURI().relativize(((FileResource) resource).file().toURI());
//	}

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
