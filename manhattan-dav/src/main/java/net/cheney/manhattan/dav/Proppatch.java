package net.cheney.manhattan.dav;

import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.message.Response;
import net.cheney.cocktail.message.Response.Status;
import net.cheney.manhattan.application.BaseApplication;
import net.cheney.manhattan.resource.api.ResourceProvidor;
import net.cheney.snax.model.Document;
import net.cheney.snax.model.Element;

import org.apache.log4j.Logger;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class Proppatch extends BaseApplication {
	private static final Logger LOG = Logger.getLogger(Proppatch.class);

	public Proppatch(ResourceProvidor providor) {
		super(providor);
	}

	@Override
	public Response call(Environment env) {
		Document document = bodyAsXML(env);
		if(document == null) {
			return clientErrorBadRequest();
		}
		Element propertyUpdate = document.rootElement();
		Iterable<Element> sets = propertyUpdate.getChildren(RFC4918.SET);
		Iterable<Element> removes = propertyUpdate.getChildren(RFC4918.REMOVE);
		RESPONSE response = RFC4918.response(RFC4918.href(env.path()), Iterables.concat(set(sets), remove(removes)));
		MULTISTATUS multistatus = RFC4918.multistatus(response);
		return successMultiStatus(multistatus);
	}

	private Iterable<PROPSTAT> remove(Iterable<Element> removes) {
		LOG.info(Iterables.toString(removes));
		return Iterables.transform(removes, new Function<Element, PROPSTAT>() {

			@Override
			public PROPSTAT apply(Element remove) {
				return remove(remove);
			}
			
		});
	}
	
	private Iterable<PROPSTAT> set(Iterable<Element> sets) {
		LOG.info(Iterables.toString(sets));
		return Iterables.transform(sets, new Function<Element, PROPSTAT>() {

			@Override
			public PROPSTAT apply(Element set) {
				return set(set);
			}
			
		});
	}

	private PROPSTAT set(Element set) {
		Element prop = set.getChildren(RFC4918.PROP).first().childElements().first();
		return RFC4918.propertyStatus(RFC4918.prop(new Element(prop.qname())), Status.SUCCESS_OK);
	}


	private PROPSTAT remove(Element remove) {
		Element prop = remove.getChildren(RFC4918.PROP).first().childElements().first();
		return RFC4918.propertyStatus(RFC4918.prop(new Element(prop.qname())), Status.SUCCESS_OK);
	}

}
