package net.cheney.manhattan.dav;

import net.cheney.cocktail.application.Environment;
import net.cheney.cocktail.message.Header;
import net.cheney.cocktail.message.Response;
import net.cheney.manhattan.resource.api.Lock;
import net.cheney.manhattan.resource.api.Resource;
import net.cheney.manhattan.resource.api.ResourceProvidor;

public class Unlock extends RFC4918 {

	public Unlock(ResourceProvidor providor) {
		super(providor);
	}

	@Override
	public Response call(Environment env) {
		Resource resource = resolveResource(env);
		String lockToken = lockToken(env);
		for(Lock lock : resource.activeLocks()) {
			if(lock.token().equals(lockToken)) {
				resource.unlock(); // TODO fix
				return successNoContent(); 
			}
		}
		return successNoContent(); // TODO what is the correct status code ?
	}

	private String lockToken(Environment env) {
		String token = env.header(Header.LOCK_TOKEN).getOnlyElement();
		token = token.substring(token.indexOf('<') + 1, token.indexOf('>'));
		return token;
	}


}
