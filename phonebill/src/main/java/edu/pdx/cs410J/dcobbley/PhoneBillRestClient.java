package edu.pdx.cs410J.dcobbley;

import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.util.Collection;

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
     * @param port The port to connect through
     */
    public PhoneBillRestClient( String hostName, int port )
    {
        this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );
    }

    /**
     * Returns all keys and values from the server
     * @throws IOException is the type of exception it throws
     */
    public Response getAllKeysAndValues() throws IOException
    {
        return get(this.url );
    }

    /**
     * Returns all values for the given key
     * @throws IOException is the type of exception it throws
     * @param bill takes a bill and parses it to post to the servlet
     */
    public Response getValues( phonebill bill ) throws IOException
    {
        try {
            //Search was properly handled.
            if (bill.searchCallOnly != null)
                return get(this.url, "customer", bill.getCustomer(), "startTime", bill.searchCallOnly.getStartTimeString(), "endTime", bill.searchCallOnly.getEndTimeString());
            else {//need to look inside of bill for single regular phonecall
                if (bill.phoneCalls != null)
                    throw new Exception("Please specify search parameters");
                else{
                    return get(this.url, "customer", bill.getCustomer(), "startTime", bill.singlePhoneCall.getStartTimeString(), "endTime", bill.singlePhoneCall.getEndTimeString());
                }
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            System.exit(1);
            return null;
        }
    }

    /**
     *
     * @param customer name of the customer
     * @param call the customers phonecall to be added
     * @return Returns the response from the server
     * @throws IOException if bad things happen
     */
    public Response addKeyValuePair(String customer, phonecall call) throws IOException
    {
        return post( this.url, "customer", customer,"caller", call.callerNumber, "callee", call.calleeNumber, "startTime", call.getStartTimeString(), "endTime", call.getEndTimeString() );
    }
}
