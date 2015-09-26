package org.loststone.toodledo.response;

import org.loststone.toodledo.exception.ToodledoApiException;


public class AddTodoResponse extends Response {

	public AddTodoResponse(String resp) {
		super(resp);
	}
	

	public String getResponseContent() throws ToodledoApiException {
		String result = null;		
		// check for errors:
		if (!this.succeeded()) {
			throw new ToodledoApiException(response.substring(response.indexOf("<error>")+7, 
					response.indexOf("</error>")));
		}
		
		result = response.substring(response.indexOf("<added>")+7, response.indexOf("</added>"));
		return result; 
	}

}
