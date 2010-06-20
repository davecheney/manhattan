package net.cheney.manhattan.application;

import java.io.IOException;

import net.cheney.cocktail.application.Environment;
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
			try {
				return Response.builder(Status.SUCCESS_OK).body(resource.body()).build();
			} catch (IOException e) {
				return serverErrorInternal(e);
			}
		} else {
			return clientErrorNotFound();
		} 
	}

}
