package net.cheney.manhattan.dav;

import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.message.Response;
import net.cheney.manhattan.resource.api.ResourceProvidor;

public class Unlock extends RFC4918 {

	public Unlock(ResourceProvidor providor) {
		super(providor);
	}

	@Override
	public Response call(Environment env) {
		return successNoContent();
	}


}
