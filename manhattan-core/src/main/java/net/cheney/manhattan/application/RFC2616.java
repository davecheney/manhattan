package net.cheney.manhattan.application;

import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.TimeZone;

import net.cheney.cocktail.application.Application;
import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.application.Environment.Depth;
import net.cheney.cocktail.application.Path;
import net.cheney.cocktail.message.Header;
import net.cheney.cocktail.message.Response;
import net.cheney.cocktail.message.Response.Status;	
import net.cheney.manhattan.resource.api.Resource;
import net.cheney.manhattan.resource.api.ResourceProvidor;
import net.cheney.snax.SNAX;
import net.cheney.snax.model.Document;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;

public abstract class RFC2616 implements Application {
	
	protected static final Charset CHARSET_UTF_8 = Charset.forName("UTF-8");
	private final ResourceProvidor providor;
	
	private static final String RFC1123_DATE_FORMAT_PATTERN = "EEE, dd MMM yyyy HH:mm:ss zzz";
	protected static final FastDateFormat RFC1123_DATE_FORMAT = FastDateFormat.getInstance(RFC1123_DATE_FORMAT_PATTERN, TimeZone.getTimeZone("GMT"), Locale.US);

	protected RFC2616(ResourceProvidor providor) {
		this.providor = providor;
	}
	
	protected ResourceProvidor providor() {
		return this.providor;
	}
	
	protected Resource resolveResource(Environment env) {
		return resolveResource(env.path());
	}

	protected Resource resolveResource(Path path) {
		return providor.resolveResource(path);
	}
	
	protected Depth depth(Environment env) {
		return Depth.parse(env.header(Header.DEPTH).getOnlyElementWithDefault(""), Depth.INFINITY);
	}
	
	protected URI destination(Environment env) {
		// TODO destination can contain "," 
		// join it just in case
		String dest = StringUtils.join(env.header(Header.DESTINATION).iterator(),',');
		return URI.create(dest);
	}

	public static Response serverErrorNotImplemented() {
		return Response.builder(Status.SERVER_ERROR_NOT_IMPLEMENTED).build();
	}

	public static Response clientErrorNotFound() {
		return Response.builder(Status.CLIENT_ERROR_NOT_FOUND).build();
	}

	public static Response clientErrorMethodNotAllowed() {
		return Response.builder(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED).build();
	}

	public static Response successCreated() {
		return Response.builder(Status.SUCCESS_CREATED).build();
	}

	public static Response serverErrorInternal(final Throwable t) {
		return Response.builder(Status.SERVER_ERROR_INTERNAL).body(CHARSET_UTF_8.encode(t.toString())).build();
	}

	public static Response clientErrorConflict() {
		return Response.builder(Status.CLIENT_ERROR_CONFLICT).build();
	}
	
	public static Response clientErrorBadRequest() {
		return Response.builder(Status.CLIENT_ERROR_BAD_REQUEST).build();
	}

	public static Response clientErrorLocked() {
		return Response.builder(Status.CLIENT_ERROR_LOCKED).build();
	}

	public static Response successNoContent() {
		return Response.builder(Status.SUCCESS_NO_CONTENT).build();
	}

	public static Response clientErrorPreconditionFailed() {
		return Response.builder(Status.CLIENT_ERROR_PRECONDITION_FAILED).build();
	}

	public static Response clientErrorUnsupportedMediaType() {
		return Response.builder(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE).build();
	}
	
	protected Document bodyAsXML(Environment env) {
		CharBuffer buffer = CHARSET_UTF_8.decode(env.body());
		return SNAX.parse(buffer);
	}
	
	protected boolean overwrite(Environment env) {
		return env.header(Header.OVERWRITE).getOnlyElementWithDefault("T").equals("T");
	}
}