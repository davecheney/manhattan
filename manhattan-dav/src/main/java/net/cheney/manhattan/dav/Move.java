package net.cheney.manhattan.dav;

import java.io.IOException;

import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.application.Path;
import net.cheney.cocktail.message.Response;
import net.cheney.manhattan.resource.api.Resource;
import net.cheney.manhattan.resource.api.ResourceProvidor;

import org.apache.log4j.Logger;

public class Move extends RFC4918 {
	private static final Logger LOG = Logger.getLogger(Move.class);

	public Move(ResourceProvidor providor) {
		super(providor);
	}

	@Override
	public Response call(Environment env) {
		final Resource source = resolveResource(env);
		
		final Resource destination = resolveResource(Path.fromURI(destination(env)));
		
		if (source.isLocked() || destination.isLocked()) {
			return clientErrorLocked();
		}
		
		boolean overwrite = overwrite(env);
		
		try {
			return move(source, destination, overwrite);
		} catch (IOException e) {
			return serverErrorInternal(e);
		}
	}

	private Response move(Resource source, Resource destination, boolean overwrite) throws IOException {
		LOG.debug(String.format("MOVE: src[%s], dest[%s], overwrite[%b]", source, destination, overwrite));
		if (source.exists()) {
			if (source.isCollection()) { // source exists
				if (destination.exists()) { // source exists and is a collection
					if (destination.isCollection()) {
						if(overwrite) {
							source.moveTo(destination);
							return successNoContent();
						} else {
							return clientErrorPreconditionFailed();
						}
					} else {
						if(overwrite) {
							source.moveTo(destination);
							return successCreated();
						} else {
							return clientErrorPreconditionFailed();
						}
					}
				} else {
					if(destination.parent().exists()) {
						source.moveTo(destination);
						return successNoContent();
					} else {
						return clientErrorPreconditionFailed();
					}
				}
			} else {
				if (destination.exists()) { // source exists
					if (destination.isCollection()) { // source exists,
						if(overwrite) {
							source.moveTo(destination);
							return successNoContent();
						} else {
							return clientErrorPreconditionFailed();
						}
					} else {
						if(overwrite) {
							source.moveTo(destination);
							return successNoContent();
						} else {
							return clientErrorPreconditionFailed();
						}
					}
				} else {
					if (destination.parent().exists()) {
						source.moveTo(destination);
						return successCreated();
					} else {
						return clientErrorConflict();
					}
				}
			}
		} 
		return clientErrorNotFound();
	}

}
