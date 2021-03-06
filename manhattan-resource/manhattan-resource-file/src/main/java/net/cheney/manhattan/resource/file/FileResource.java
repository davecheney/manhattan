package net.cheney.manhattan.resource.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import net.cheney.cocktail.message.Request.Method;
import net.cheney.manhattan.dav.RFC3441;
import net.cheney.manhattan.dav.RFC4918;
import net.cheney.manhattan.resource.api.CollectionResource;
import net.cheney.manhattan.resource.api.Lock;
import net.cheney.manhattan.resource.api.Lock.Scope;
import net.cheney.manhattan.resource.api.Lock.Type;
import net.cheney.manhattan.resource.api.Resource;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.QName;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class FileResource implements Resource {

	private final File file;
	private final FileResourceProvidor providor;

	FileResource(FileResourceProvidor providor, File file) {
		this.providor = providor;
		this.file = file;
	}

	public boolean exists() {
		return file().exists();
	}

	public Date lastModified() {
		return new Date(file().lastModified());
	}

	public String etag() {
		return String.format("\"%d\"", file().lastModified()); // "2134123412"
	}

	public ByteBuffer entity() throws IOException {
		FileChannel fc = channel();
		try {
			return fc.map(MapMode.READ_ONLY, 0, fc.size());
		} finally {
			fc.close();
		}
	}

	public boolean mkcol() {
		return file().mkdir();
	}

	public FileResource parent() {
		return new FileResource(providor, file().getParentFile());
	}

	public void put(ByteBuffer entity) throws IOException {
		FileChannel fc = new FileOutputStream(file()).getChannel();
		fc.write(entity);
		fc.close();
	}

	public void delete() throws IOException {
		if(isCollection()) {
			FileUtils.deleteDirectory(file());
		} else {
			file().delete();
		}
	}

	public boolean isCollection() {
		return file().isDirectory();
	}

	public File file() {
		return file;
	}

	public List<Resource> members() {
		File[] files = file.listFiles();
		List<Resource> children = new ArrayList<Resource>();
		for(File c : (files == null ? new File[0] : files )) {
			children.add(new FileResource(providor, c));
		}
		return children;
	}

	public void copyTo(final Resource destination) throws IOException {
		File dest = ((FileResource)destination).file;
		File source = this.file;
		
		if(source.isDirectory()) {
			if(dest.isDirectory()) {
				FileUtils.copyDirectoryToDirectory(source, dest);
			} else {
				dest.delete();
				FileUtils.copyDirectory(source, dest);
			}
		} else {
			if(dest.isDirectory()) {
				FileUtils.copyFileToDirectory(source, dest);
			} else {
				FileUtils.copyFile(source, dest);
			}
		}
	}
	
	public void moveTo(CollectionResource destination) throws IOException {
		File dest = ((FileResource)destination).file;
		File source = this.file;
		
		if(source.isDirectory()) {
			if(dest.isDirectory()) {
				FileUtils.copyDirectoryToDirectory(source, dest);
				FileUtils.deleteDirectory(source);
			} else {
				dest.delete();
				FileUtils.copyDirectory(source, dest);
				FileUtils.deleteDirectory(source);
			}
		} else {
			if(dest.isDirectory()) {
				FileUtils.copyFileToDirectory(source, dest);
				source.delete();
			} else {
				FileUtils.copyFile(source, dest);
				source.delete();
			}
		}
	}
	
	public long size() {
		return file.length();
	}
	
	public String displayName() {
		return file.getName();
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof FileResource && ((FileResource)o).file.equals(this.file));
	}
	
	@Override
	public int hashCode() {
		return file.hashCode();
	}

	@Override
	public Collection<Method> supportedMethods() {
		return Arrays.asList(Method.values());
	}

	private FileChannel channel() throws IOException {
		return new FileInputStream(file()).getChannel();
	}

	@Override
	public String name() {
		return file.getName();
	}

	@Override
	public boolean isLocked() {
		return providor.lockManager().isLocked(this);
	}

	@Override
	public long contentLength() {
		return file.length();
	}

	@Override
	public Resource create(String name, ByteBuffer buffer) throws IOException {
		File f = new File(file, name);
		FileOutputStream fos = new FileOutputStream(f);
		FileChannel fc = fos.getChannel();
		fc.position(0);
		int written = fc.write((ByteBuffer) buffer);
		if (written != buffer.limit()) throw new IllegalStateException();
		fc.close();
		return new FileResource(providor, f);
	}

	@Override
	public Resource child(String name) {
		return new FileResource(providor, new File(file, name));
	}

	@Override
	public Iterable<Resource> children() {
		return Iterables.transform(filterHidden(childFiles()), new Function<File, Resource>() {

			@Override
			public Resource apply(File file) {
				return new FileResource(providor, file);
			}
		});
	}

	private Iterable<File> childFiles() {
		return file.isDirectory() ? Arrays.asList(file.listFiles()) : Collections.<File>emptyList();
	}

	private Iterable<File> filterHidden(Iterable<File> files) {
		return files;
	}

	@Override
	public Element property(QName name) {
		if(name.equals(RFC4918.DISPLAY_NAME)) {
			return RFC4918.displayName(name());
		} else if(name.equals(RFC4918.RESOURCE_TYPE)) {
			return RFC4918.resourceType(isCollection());
		} else if(name.equals(RFC4918.GET_CONTENT_LENGTH)) {
			return RFC4918.getContentLength(size());
		} else if(name.equals(RFC4918.GET_LAST_MODIFIED)) {
			return RFC4918.getLastModified(new Date(file.lastModified()));
		} else if(name.equals(RFC3441.QUOTA_AVAILABLE_BYTES)) {
			return RFC3441.quotaAvailbleBytes(providor.quotaAvailbleBytes());
		} else if(name.equals(RFC3441.QUOTA_USED_BYTES)) {
			return RFC3441.quotaUsedBytes(providor.quotaUsedBytes());
		}
		return null;
	}

	@Override
	public Iterable<Element> properties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean makeCollection(String name) {
		return new File(file, name).mkdir();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public Lock lock(Type type, Scope scope) {
		return providor.lockManager().lock(this, type, scope);
	}
	
	@Override
	public Lock unlock() {
		return providor.lockManager().unlock(this);
	}

	@Override
	public boolean hasParent() {
		return !providor.isRoot(this);
	}
	
	@Override
	public Iterable<Lock> activeLocks() {
		return providor.lockManager().activeLocks(this);
	}
	
	@Override
	public Lock activeLock(final String lockToken) throws NoSuchElementException {
		return Iterables.find(activeLocks(), new Predicate<Lock>() { 
			@Override
			public boolean apply(Lock lock) {
				return lock.token().equals(lockToken);
			}
		});
	}
	

}
