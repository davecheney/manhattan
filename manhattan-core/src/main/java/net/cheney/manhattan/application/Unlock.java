package net.cheney.manhattan.application;

import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.message.Response;
import net.cheney.manhattan.resource.ResourceProvidor;

public class Unlock extends BaseApplication {

	public Unlock(ResourceProvidor providor) {
		super(providor);
	}

	@Override
	public Response call(Environment env) {
		return successNoContent();
	}


}
