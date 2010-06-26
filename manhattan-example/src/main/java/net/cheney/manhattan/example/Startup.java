package net.cheney.manhattan.example;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import net.cheney.cocktail.application.Application;
import net.cheney.cocktail.httpsimple.HttpServer;
import net.cheney.cocktail.middleware.CommonLogger;
import net.cheney.manhattan.application.DavApplication;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Startup {
	
	public static void main(String[] args) throws InterruptedException, IOException {
		BasicConfigurator.configure();
		
		File root = new File("/tmp/dav");
		root.mkdir();
		Application dav = new DavApplication(root);
		dav = new CommonLogger(dav, Logger.getRootLogger());
//		dav = new ResponseDebugger(dav, Logger.getRootLogger());
		
		HttpServer.builder(dav).bind(new InetSocketAddress(8080)).build().start(4);
	}
}
