package net.cheney.manhattan.example;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import net.cheney.cocktail.application.Application;
import net.cheney.cocktail.httpsimple.HttpServer;

import org.apache.log4j.BasicConfigurator;

public class Startup {
	
	public static void main(String[] args) throws InterruptedException, IOException {
		BasicConfigurator.configure();
		
		File root = new File("/tmp/dav");
		root.mkdir();
		Application dav = new DavApplication(root);
		
		HttpServer.builder(dav).bind(new InetSocketAddress(8080)).build().start();
	}
}
