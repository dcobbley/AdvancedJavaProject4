package edu.pdx.cs410J.dcobbley;

import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send key/value pairs.
 */
public class PhoneBillRestClient extends HttpRequestHelper
{
    private static final String WEB_APP = "phonebill";
    private static final String SERVLET = "calls";

    private final String url;


    /**
     * Creates a client to the Phone Bil REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public PhoneBillRestClient( String hostName, int port )
    {
        this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );
    }

    /**
     * Returns all keys and values from the server
     */
    public Response getAllKeysAndValues() throws IOException
    {
        return get(this.url );
    }

    /**
     * Returns all values for the given key
     */
    public Response getValues( String customer, String startTime, String endTime ) throws IOException
    {
        return get(this.url, "customer", customer, "startTime", startTime, "endTime", endTime);
    }

    public Response addKeyValuePair( String customer, String caller, String callee, String startTime, String endTime ) throws IOException
    {
        return post( this.url, "customer", customer,"caller", caller, "callee", callee, "startTime", startTime, "endTime", endTime );
    }
}
