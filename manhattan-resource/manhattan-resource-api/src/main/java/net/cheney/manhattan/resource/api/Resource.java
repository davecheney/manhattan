package net.cheney.manhattan.resource.api;

import java.util.Collection;
import java.util.Date;

import javax.annotation.Nonnull;

import net.cheney.cocktail.message.Request.Method;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.QName;

public interface Resource extends Lockable, Getable, Deletable, CollectionResource, Moveable, Copyable {
	
	boolean exists();
	
	@Nonnull Collection<Method> supportedMethods();

	@Nonnull Element property(QName name);
	
	@Nonnull Iterable<Element> properties();
	
	@Nonnull Resource parent();
	
	boolean hasParent();

	boolean makeCollection(String name);

	@Nonnull boolean isCollection();

	Iterable<Lock> activeLocks();

	Date lastModified();

}
