package net.cheney.manhattan.example;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import net.cheney.cocktail.application.Application;
import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.application.Router;
import net.cheney.cocktail.httpsimple.HttpServer;
import net.cheney.cocktail.message.Response;
import net.cheney.cocktail.message.Response.Status;
import net.cheney.cocktail.middleware.PrettyErrors;
import net.cheney.manhattan.application.Delete;
import net.cheney.manhattan.application.Get;
import net.cheney.manhattan.application.Head;
import net.cheney.manhattan.application.Put;
import net.cheney.manhattan.dav.Copy;
import net.cheney.manhattan.dav.Lock;
import net.cheney.manhattan.dav.Mkcol;
import net.cheney.manhattan.dav.Move;
import net.cheney.manhattan.dav.Options;
import net.cheney.manhattan.dav.Propfind;
import net.cheney.manhattan.dav.Proppatch;
import net.cheney.manhattan.dav.Unlock;
import net.cheney.manhattan.resource.api.ResourceProvidor;
import net.cheney.manhattan.resource.file.FileResourceProvidor;
import net.cheney.manhattan.resource.file.InMemoryLockManager;

import org.apache.log4j.BasicConfigurator;

public class DavLevel2 implements Application, Router {
	
	private final ResourceProvidor providor;

	public DavLevel2(File root) {
		this.providor = new FileResourceProvidor(new InMemoryLockManager(), root);
	}
	
	public Response call(Environment env) {
		return route(env).call(env);
	}
	
	@Override
	public Application route(Environment env) {
		switch(env.method()) {
		case OPTIONS:
			return new Options(providor);
			
		case PROPFIND:
			return new Propfind(providor);
			
		case PROPPATCH:
			return new Proppatch(providor);
			
		case MKCOL:
			return new Mkcol(providor);
			
		case COPY:
			return new Copy(providor);
			
		case MOVE:
			return new Move(providor);
			
		case PUT:
			return new Put(providor);
			
		case DELETE:
			return new Delete(providor);
			
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
					return Response.builder(Status.SERVER_ERROR_NOT_IMPLEMENTED); 
				}
			};
		}
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		BasicConfigurator.configure();
		
		File root = new File("/tmp/dav");
		root.mkdir();
		Application app = new PrettyErrors(new DavLevel2(root));
		HttpServer.builder(app).bind(new InetSocketAddress(8080)).build().start();
	}
}
