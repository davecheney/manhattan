package net.cheney.manhattan.application;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.message.Response;
import net.cheney.manhattan.resource.api.Resource;
import net.cheney.manhattan.resource.api.ResourceProvidor;

public class Put extends RFC2616 {

	public Put(ResourceProvidor providor) {
		super(providor);
	}

	@Override
	public Response call(Environment env) {
		Resource resource = resolveResource(env);
		boolean exists = resource.exists();
		if (exists && resource.isCollection()) {
			return clientErrorMethodNotAllowed();
		}

		Resource parent = resource.parent();
		if (!parent.exists()) {
			return clientErrorConflict();
		}

		try {
			if (env.hasBody()) {
				parent.create(resource.name(), env.body());
			} else {
				parent.create(resource.name(), ByteBuffer.allocate(0));
			}
		} catch (IOException e) {
			return serverErrorInternal(e);
		}
		return exists ? successNoContent() : successCreated();
	}

}
