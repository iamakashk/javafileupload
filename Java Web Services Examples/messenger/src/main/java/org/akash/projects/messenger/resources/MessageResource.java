package org.akash.projects.messenger.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/message")
public class MessageResource {

	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getMessages() {
		return "This is a message";
	}
}
