package net.cheney.manhattan.application;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.NoSuchElementException;

import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.message.Header;
import net.cheney.cocktail.message.Response;
import net.cheney.manhattan.resource.api.Lock;
import net.cheney.manhattan.resource.api.Resource;
import net.cheney.manhattan.resource.api.ResourceProvidor;

public class Put extends RFC2616 {

	public Put(ResourceProvidor providor) {
		super(providor);
	}

	@Override
	public Response call(Environment env) {
		Resource resource = resolveResource(env);
		
		if (resource.exists() && resource.isCollection()) {
			return clientErrorMethodNotAllowed();
		}

		if (env.header(Header.IF).any()) {
			return conditionalPut(env);
		} else {
			return unconditionalPut(env);
		}
	}

	private Response conditionalPut(Environment env) {
		return clientErrorConflict();
	}

	private Response unconditionalPut(Environment env) {
		Resource resource = resolveResource(env);
		if(resource.isLocked()) {
			return clientErrorLocked();
		}
		
		boolean exists = resource.exists();
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
