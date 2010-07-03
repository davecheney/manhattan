package net.cheney.manhattan.resource.api;

import java.sql.Ref;
import java.util.UUID;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import net.cheney.snax.model.Node;
import net.cheney.snax.model.Text;

public final class Lock {
	
	public enum Scope {	
		NONE, 
		SHARED { 
			@Override
			public Node toXML() {
				return new Text("shared");
			}
		},
		EXCLUSIVE {
			@Override
			public Node toXML() {
				return new Text("exclusive");
			}
		};

		public Node toXML() {
			return null;
		}
		
	}
	
	public enum Type { 
		NONE, 
		READ { 
			@Override
			public Node toXML() {
				return new Text("exclusive");
			}
		},
		WRITE { 
			@Override
			public Node toXML() {
				return new Text("write");
			}
		};

		public Node toXML() {
			return null;
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
