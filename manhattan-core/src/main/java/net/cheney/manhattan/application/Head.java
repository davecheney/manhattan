package net.cheney.manhattan.application;

import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.message.Header;
import net.cheney.cocktail.message.Response;
import net.cheney.cocktail.message.Response.Builder;
import net.cheney.cocktail.message.Response.Status;
import net.cheney.manhattan.resource.Resource;
import net.cheney.manhattan.resource.ResourceProvidor;

public class Head extends BaseApplication {

	public Head(ResourceProvidor providor) {
		super(providor);
	}

	@Override
	public Response call(Environment env) {
		Resource resource = resolveResource(env);
		resource.activeLocks();
		Builder builder = Response.builder(Status.SUCCESS_OK);
		builder.header(Header.LAST_MODIFIED).add(RFC1123_DATE_FORMAT.format(resource.lastModified()));
		builder.header(Header.CONTENT_LENGTH).add(Long.toString(resource.contentLength()));
		return builder.build();
	}

}
