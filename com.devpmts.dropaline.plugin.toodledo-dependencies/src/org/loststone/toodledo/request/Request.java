package org.loststone.toodledo.request;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.loststone.toodledo.response.Response;

public abstract class Request {

	// url to the service
	protected String url = null;

	protected String xmlResponse = null;

	/**
	 * Should execute the exec method and give back an appropiate response type.
	 * 
	 * @return
	 */
	public abstract Response getResponse();

	/**
	 * This methods executes the HTTP GET of the URL and stores the result in
	 * String format in the xmlResponse field.
	 */
	public void exec() {
		HttpClient client = new DefaultHttpClient();
		HttpGet method = null;
		method = new HttpGet(this.url);
		try {
			HttpResponse response = client.execute(method);
			this.xmlResponse = EntityUtils.toString(response.getEntity());
			System.err.println(xmlResponse);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
	}

}
