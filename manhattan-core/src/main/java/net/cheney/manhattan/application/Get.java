package net.cheney.manhattan.application;

import java.io.IOException;

import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.message.Header;
import net.cheney.cocktail.message.Response;
import net.cheney.cocktail.message.Response.Status;
import net.cheney.manhattan.resource.Resource;
import net.cheney.manhattan.resource.ResourceProvidor;

public class Get extends BaseApplication {

	public Get(ResourceProvidor providor) {
		super(providor);
	}

	@Override
	public Response call(Environment env) {
		Resource resource = resolveResource(env);
		if(resource.exists()) {
			if(env.header(Header.RANGE).any()) {
				return getWithRange(resource, env);
			} else {
				return getWithoutRange(resource, env);
			}
		} else {
			return clientErrorNotFound();
		} 
	}

	private Response getWithoutRange(Resource resource, Environment env) {
		try {
			return Response.builder(Status.SUCCESS_OK).body(resource.body()).build();
		} catch (IOException e) {
			return serverErrorInternal(e);
		}
	}

	private Response getWithRange(Resource resource, Environment env) {
		String range = env.header(Header.RANGE).getOnlyElement();
		try {
			return Response.builder(Status.SUCCESS_PARTIAL_CONTENT).body(resource.body()).build();
		} catch (IOException e) {
			return serverErrorInternal(e);
		}
	}

}
