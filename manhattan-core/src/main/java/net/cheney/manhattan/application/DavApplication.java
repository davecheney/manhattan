package net.cheney.manhattan.application;

import java.io.File;

import net.cheney.cocktail.application.Application;
import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.application.Router;
import net.cheney.cocktail.message.Response;
import net.cheney.cocktail.message.Response.Status;
import net.cheney.manhattan.resource.ResourceProvidor;
import net.cheney.manhattan.resource.FileResourceProvidor;

public class DavApplication implements Application, Router {

	private final ResourceProvidor providor;

	public DavApplication(File root) {
		this.providor = new FileResourceProvidor(root);
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


}
