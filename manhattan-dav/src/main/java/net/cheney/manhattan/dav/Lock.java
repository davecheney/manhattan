package net.cheney.manhattan.dav;

import java.nio.channels.IllegalSelectorException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

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
		if(env.hasBody()) {
			return lock(env);
		} else {
			return refresh(env);
		}
	}
	
	private Response refresh(Environment env) {
		Resource resource = resolveResource(env);
		final String lockToken = ifLockToken(env);
		Depth depth = depth(env);
		net.cheney.manhattan.resource.api.Lock lock = Iterables.find(resource.activeLocks(), new Predicate<net.cheney.manhattan.resource.api.Lock>() {

			@Override
			public boolean apply(net.cheney.manhattan.resource.api.Lock input) {
				return input.token().equals(lockToken);
			}
		});
		
		Response.Builder builder = Response.builder(Status.SUCCESS_OK);
		Document response = new Document(prop(lockDiscovery(activeLock(lock, depth, env.path()))));
		
		builder.header(Header.LOCK_TOKEN).add(String.format("<%s>", lock.token()));
		builder.header(Header.CONTENT_TYPE).add("application/xml; charset=\"utf-8\"");
		return builder.body(CHARSET_UTF_8.encode(XMLWriter.write(response))).build();
	}

	private String ifLockToken(Environment env) {
		String token = env.header(Header.IF).getOnlyElement();
		token = token.substring(token.indexOf('<') + 1, token.indexOf('>'));
		return token;
	}

	private Response lock(Environment env) {
		Resource resource = resolveResource(env);
		Depth depth = depth(env);

		Document document = bodyAsXML(env);
		
		Element lockinfo = document.childElements().first();
		Element scope = lockinfo.getChildren(RFC4918.LOCK_SCOPE).first().childElements().first();
		Element type = lockinfo.getChildren(RFC4918.LOCK_TYPE).first().childElements().first();
//		Element owner = lockinfo.getChildren(RFC4918.OWNER).first();
		net.cheney.manhattan.resource.api.Lock lock = resource.lock(Type.parse(type), Scope.parse(scope));
		Response.Builder builder = Response.builder(Status.SUCCESS_OK);
		Document response = new Document(prop(lockDiscovery(activeLock(lock, depth, env.path()))));
		
		builder.header(Header.LOCK_TOKEN).add(String.format("<%s>", lock.token()));
		builder.header(Header.CONTENT_TYPE).add("application/xml; charset=\"utf-8\"");
		return builder.body(CHARSET_UTF_8.encode(XMLWriter.write(response))).build();
	}

}
