package net.cheney.manhattan.example;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import net.cheney.cocktail.application.Application;
import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.httpsimple.HttpServer;
import net.cheney.cocktail.message.Header;
import net.cheney.cocktail.message.Response;

import org.apache.log4j.BasicConfigurator;

public class StartupReadOnly {
	
	public static void main(String[] args) throws InterruptedException, IOException {
		BasicConfigurator.configure();
		
		File root = new File("/tmp/dav");
		root.mkdir();
		final Application dav = new DavApplication(root);
		Application readOnly = new Application() {

			@Override
			public Response call(Environment env) {
				Response response = dav.call(env);
				if(response.header(Header.DAV).any()) {
					Response.Builder readOnly = Response.builder(response.status());
					readOnly.body(response.body());
					for(Header key : response.keys()) {
						readOnly.header(key).set(response.header(key));
					}
					readOnly.header(Header.DAV).set("1");
					return readOnly;
				} else {
					return response;
				}
			}
			
		};
		
		HttpServer.builder(readOnly).bind(new InetSocketAddress(8080)).build().start();
	}
}
