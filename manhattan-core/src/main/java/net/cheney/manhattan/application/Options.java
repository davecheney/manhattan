package net.cheney.manhattan.application;

import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.message.Header;
import net.cheney.cocktail.message.Request.Method;
import net.cheney.cocktail.message.Response;
import net.cheney.cocktail.message.Response.Status;
import net.cheney.manhattan.resource.Resource;
import net.cheney.manhattan.resource.ResourceProvidor;

public class Options extends BaseApplication {

	public Options(ResourceProvidor providor) {
		super(providor);
	}

	@Override
	public Response call(Environment env) {
		Resource resource = resolveResource(env);
		Response.Builder builder = Response.builder(Status.SUCCESS_NO_CONTENT);
		
		for(Method method : resource.supportedMethods()) {
			builder.header(Header.ALLOW).add(method.name());
		}
		
		builder.header(Header.DAV).add("1").add("2");
		
		return builder.build();
	}

}
