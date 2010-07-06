package net.cheney.manhattan.application;

import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.message.Header;
import net.cheney.cocktail.message.Response;
import net.cheney.cocktail.message.Response.Status;
import net.cheney.manhattan.resource.api.Resource;
import net.cheney.manhattan.resource.api.ResourceProvidor;

public class Get extends RFC2616 {

	public Get(ResourceProvidor providor) {
		super(providor);
	}

	@Override
	public Response call(Environment env) {
		Resource resource = resolveResource(env);
		if(resource.exists()) {
			if(env.header(Header.RANGE).any()) {
				return serverErrorNotImplemented();
			} else {
				return getWithoutRange(resource, env);
			}
		} else {
			return clientErrorNotFound();
		} 
	}

	private Response getWithoutRange(Resource resource, Environment env) {
		return Response.builder(Status.SUCCESS_OK).body(resource.file()).build();
	}

}
