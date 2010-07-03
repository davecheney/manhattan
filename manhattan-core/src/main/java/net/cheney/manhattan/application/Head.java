package net.cheney.manhattan.application;

import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.message.Header;
import net.cheney.cocktail.message.Response;
import net.cheney.cocktail.message.Response.Builder;
import net.cheney.manhattan.resource.api.ResourceProvidor;

public class Head extends Get {

	public Head(ResourceProvidor providor) {
		super(providor);
	}

	@Override
	public Response call(Environment env) {
		Response get = super.call(env);
		Builder head = Response.builder(get.status());
		for(Header key : get.keys()) {
			head.header(key).set(get.header(key));
		}
		if(get.hasBody()) {
			head.header(Header.CONTENT_LENGTH).add(Long.toString(get.body().remaining()));
		}
		return head.build();
	}

}
