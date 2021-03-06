package net.cheney.manhattan.dav;

import java.io.IOException;

import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.message.Response;
import net.cheney.manhattan.resource.api.Resource;
import net.cheney.manhattan.resource.api.ResourceProvidor;

public class Mkcol extends RFC4918 {

	public Mkcol(ResourceProvidor providor) {
		super(providor);
	}

	@Override
	public Response call(Environment env) {
		if (env.hasBody()) {
			return clientErrorUnsupportedMediaType();
		}
		Resource resource = resolveResource(env);
		
		if(resource.exists()) {
			return clientErrorMethodNotAllowed();
		} else {
			Resource parent = resource.parent();
			if (parent.exists()) {
				return parent.makeCollection(resource.name()) ? successCreated() : serverErrorInternal(new IOException("Cannot create"));
			} else {
				return clientErrorConflict();
			}
		}
	}

}
