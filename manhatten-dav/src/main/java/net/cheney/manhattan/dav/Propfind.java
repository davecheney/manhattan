package net.cheney.manhattan.dav;

import static com.google.common.collect.Lists.newArrayList;
import static net.cheney.manhattan.dav.RFC4918.depth;

import java.util.ArrayList;
import java.util.List;

import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.application.Environment.Depth;
import net.cheney.cocktail.message.Response;
import net.cheney.cocktail.message.Response.Status;
import net.cheney.manhattan.resource.api.Resource;
import net.cheney.manhattan.resource.api.ResourceProvidor;
import net.cheney.snax.model.Document;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.QName;

import org.apache.log4j.Logger;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class Propfind extends RFC4918 {
	
	private static final Logger LOG = Logger.getLogger(Propfind.class);
	
	private static final Iterable<QName> ALL_PROPS = newArrayList(CREATIONDATE, DISPLAY_NAME, GET_CONTENT_LENGTH, GET_LAST_MODIFIED, RESOURCE_TYPE);

	public Propfind(ResourceProvidor providor) {
		super(providor);
	}

	@Override
	public Response call(Environment env) {
		Resource resource = resolveResource(env);
		if(resource.exists()) {
			try {
				List<RESPONSE> propfind = propfind(getSearchProperties(env), resource, depth(env));
				return successMultiStatus(multistatus(propfind));
			} catch (IllegalArgumentException e) {
				LOG.error("Unable to parse PROPFIND xml body", e);
				return clientErrorBadRequest();
			}
		} else {
			return clientErrorNotFound();
		}
	}
	
	private final Iterable<QName> getSearchProperties(final Environment env) {
		final Document doc = bodyAsXML(env);
		if(doc == null) {
			throw new IllegalArgumentException();
		}
		final Element propfind = doc.rootElement();
		if(propfind == null) {
			throw new IllegalArgumentException();
		}
		final Element props = propfind.getChildren(RFC4918.PROP).first();
		if (props == null || !props.hasChildren()) {
			return ALL_PROPS;
		} else {
			return Iterables.transform(props.childElements(), new Function<Element, QName>() {
				@Override
				public QName apply(Element property) {
					return property.qname();
				}
			});
		}
	}
	
	private List<RFC4918.RESPONSE> propfind(Iterable<QName> searchProps, Resource resource, Depth depth) {
		List<RFC4918.RESPONSE> responses = new ArrayList<RFC4918.RESPONSE>();
		
		responses.add(response(href(resource), getProperties(resource, searchProps)));
		if (depth != Depth.ZERO) {
			for (final Resource child : resource.children()) {
				responses.addAll(propfind(searchProps, child, depth.decreaseDepth()));
			}
		}
		return responses;
	}
	
	private final List<PROPSTAT> getProperties(final Resource resource, final Iterable<QName> properties) {
		final List<PROPSTAT> propstats = new ArrayList<PROPSTAT>(2);
		final List<Element> foundProps = new ArrayList<Element>();
		final List<Element> notFoundProps = new ArrayList<Element>();
		for (final QName property : properties) {
			final Element prop = resource.property(property);
			if(prop == null) {
				notFoundProps.add(new Element(property));
			} else {
				foundProps.add(prop);
			}
		}
		if(!foundProps.isEmpty()) {
			propstats.add(propertyStatus(prop(foundProps), Status.SUCCESS_OK));
		} 
		if(!notFoundProps.isEmpty()) {
			propstats.add(propertyStatus(prop(notFoundProps), Status.CLIENT_ERROR_NOT_FOUND));
		}
		return propstats;
	}
	
}
