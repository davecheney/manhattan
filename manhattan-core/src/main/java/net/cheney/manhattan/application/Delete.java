package net.cheney.manhattan.application;

import java.io.IOException;

import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.message.Response;
import net.cheney.manhattan.resource.api.Resource;
import net.cheney.manhattan.resource.api.ResourceProvidor;

public class Delete extends RFC2616 {

	public Delete(ResourceProvidor providor) {
		super(providor);
	}

	@Override
	public Response call(Environment env) {
		Resource resource = resolveResource(env);
		if (resource.isLocked()) {
			return clientErrorLocked();
		}
		
		if (resource.exists()) {
			return delete(resource);
		} else {
			return clientErrorNotFound();
		}
	}

	private Response delete(Resource resource) {
		try {
			resource.delete();
			return successNoContent();
		} catch (IOException e) {
			return serverErrorInternal(e);
		}
	}

}
