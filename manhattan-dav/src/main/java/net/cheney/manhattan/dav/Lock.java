package net.cheney.manhattan.dav;

import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.application.Environment.Depth;
import net.cheney.cocktail.message.Header;
import net.cheney.cocktail.message.Response;
import net.cheney.cocktail.message.Response.Status;
import net.cheney.manhattan.resource.api.Lock.Scope;
import net.cheney.manhattan.resource.api.Lock.Type;
import net.cheney.manhattan.resource.api.Resource;
import net.cheney.manhattan.resource.api.ResourceProvidor;
import net.cheney.snax.model.Document;
import net.cheney.snax.model.Element;
import net.cheney.snax.writer.XMLWriter;

public class Lock extends RFC4918 {

	public Lock(ResourceProvidor providor) {
		super(providor);
	}

	@Override
	public Response call(Environment env) {
		Document document = bodyAsXML(env);
		Resource resource = resolveResource(env);
		Depth depth = depth(env);
		
		Element lockinfo = document.childElements().first();
		Element scope = lockinfo.getChildren(RFC4918.LOCK_SCOPE).first().childElements().first();
		Element type = lockinfo.getChildren(RFC4918.LOCK_TYPE).first().childElements().first();
//		Element owner = lockinfo.getChildren(RFC4918.OWNER).first();
		net.cheney.manhattan.resource.api.Lock lock = lock(Type.parse(type), Scope.parse(scope), resource);
		Response.Builder builder = Response.builder(Status.SUCCESS_OK);
		Document response = new Document(prop(lockDiscovery(activeLock(lock, depth, env.path()))));
		
		builder.header(Header.LOCK_TOKEN).add(String.format("<%s>", lock.token()));
		builder.header(Header.CONTENT_TYPE).add("application/xml; charset=\"utf-8\"");
		return builder.body(CHARSET_UTF_8.encode(XMLWriter.write(response))).build();
	}
	
	private net.cheney.manhattan.resource.api.Lock lock(net.cheney.manhattan.resource.api.Lock.Type type, net.cheney.manhattan.resource.api.Lock.Scope scope, Resource resource) {
		return new net.cheney.manhattan.resource.api.Lock(type, scope, resource);
	}

}
