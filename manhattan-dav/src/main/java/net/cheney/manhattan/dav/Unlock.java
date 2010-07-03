package net.cheney.manhattan.dav;

import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.message.Response;
import net.cheney.manhattan.application.BaseApplication;
import net.cheney.manhattan.resource.api.ResourceProvidor;

public class Unlock extends BaseApplication {

	public Unlock(ResourceProvidor providor) {
		super(providor);
	}

	@Override
	public Response call(Environment env) {
		return successNoContent();
	}


}