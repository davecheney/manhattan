package net.cheney.manhattan.resource.api;

import net.cheney.cocktail.application.Path;

public interface ResourceProvidor extends LockManagerProvidor {

	Resource resolveResource(Path path);

//	URI relativizeResource(Resource resource);
	
}
