package net.cheney.manhattan.example;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import net.cheney.cocktail.application.Application;
import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.application.Router;
import net.cheney.cocktail.httpsimple.HttpServer;
import net.cheney.cocktail.message.Header;
import net.cheney.cocktail.message.Request.Method;
import net.cheney.cocktail.message.Response;
import net.cheney.cocktail.message.Response.Status;
import net.cheney.manhattan.application.Get;
import net.cheney.manhattan.application.Head;
import net.cheney.manhattan.dav.Lock;
import net.cheney.manhattan.dav.Propfind;
import net.cheney.manhattan.dav.Unlock;
import net.cheney.manhattan.resource.api.ResourceProvidor;
import net.cheney.manhattan.resource.file.FileResourceProvidor;
import net.cheney.manhattan.resource.file.InMemoryLockManager;

import org.apache.log4j.BasicConfigurator;

public class DavLevel1 implements Application, Router {

	private final ResourceProvidor providor;

	public DavLevel1(File root) {
		this.providor = new FileResourceProvidor(new InMemoryLockManager(),
				root);
	}

	public Response call(Environment env) {
		return route(env).call(env);
	}

	@Override
	public Application route(Environment env) {
		switch (env.method()) {
		case OPTIONS:
			return new Application() {
				public Response call(Environment env) {
					Response.Builder builder = Response.builder(Status.SUCCESS_NO_CONTENT);
					
					for(Method method : new Method[] {Method.OPTIONS, Method.PROPFIND, Method.HEAD, Method.GET }) {
						builder.header(Header.ALLOW).add(method.name());
					}
					
					builder.header(Header.DAV).add("1");
					
					return builder.build();
				}
			};

		case PROPFIND:
			return new Propfind(providor);

		case HEAD:
			return new Head(providor);

		case GET:
			return new Get(providor);

		case LOCK:
			return new Lock(providor);

		case UNLOCK:
			return new Unlock(providor);

		default:
			return new Application() {

				@Override
				public Response call(Environment env) {
					return Response
							.builder(Status.SERVER_ERROR_NOT_IMPLEMENTED);
				}
			};
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		BasicConfigurator.configure();

		File root = new File("/tmp/dav");
		root.mkdir();
		final Application dav = new DavLevel1(root);

		HttpServer.builder(dav).bind(new InetSocketAddress(8080)).build()
				.start();
	}
}
