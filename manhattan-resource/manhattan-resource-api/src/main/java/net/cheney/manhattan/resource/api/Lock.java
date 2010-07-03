package net.cheney.manhattan.resource.api;

import java.util.UUID;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import net.cheney.snax.model.Element;
import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.Node;
import net.cheney.snax.model.QName;

public final class Lock {
	
	private static final Namespace DAV_NAMESPACE = Namespace.valueOf("", "DAV:");
	
	public enum Scope {	
		NONE, 
		SHARED { 
			@Override
			public Node toXML() {
				return new Element(QName.valueOf(DAV_NAMESPACE, "shared"));
			}
		},
		EXCLUSIVE {
			@Override
			public Node toXML() {
				return new Element(QName.valueOf(DAV_NAMESPACE, "exclusive"));
			}
		};

		public Node toXML() {
			return null;
		}

		public static Scope parse(Element scope) {
			return valueOf(scope.localpart().toUpperCase());
		}
		
	}
	
	public enum Type { 
		NONE, 
		READ { 
			@Override
			public Node toXML() {
				return new Element(QName.valueOf(DAV_NAMESPACE, "read"));
			}
		},
		WRITE { 
			@Override
			public Node toXML() {
				return new Element(QName.valueOf(DAV_NAMESPACE, "write"));
			}
		};

		public Node toXML() {
			return null;
		}

		public static Type parse(Element type) {
			return valueOf(type.localpart().toUpperCase());
		}

	}
	
	private final Type type;
	private final Scope scope;
	private final String token;
	private final Resource resource;
	
	public Lock(Type type, Scope scope, Resource resource) {
		this.type = type;
		this.scope = scope;
		this.token = generateToken();
		this.resource = resource;
	}

	private String generateToken() {
		return String.format("urn:uuid:%s",UUID.randomUUID());
	}
	
	public Resource resource() {
		return resource;
	}
	
	public Lock.Type type() {
		return type;
	}
	
	public Lock.Scope scope() {
		return scope;
	}
	
	public String token() {
		return token;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
