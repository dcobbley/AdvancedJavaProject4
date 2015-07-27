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
    public Response getValues( phonebill bill ) throws IOException
    {
        try {
            //Search was properly handled.
            if (bill.searchCallOnly != null)
                return get(this.url, "customer", bill.getCustomer(), "startTime", bill.searchCallOnly.getStartTimeString(), "endTime", bill.searchCallOnly.getEndTimeString());
            else {//need to look inside of bill, may contain a list of phonecalls
                if (bill.getPhoneCalls().size() > 1)
                    throw new Exception("Please specify search parameters");

                else{
                    Collection<phonecall> tempPhoneCalls = bill.getPhoneCalls();
                    phonecall tempCall=null;
                    boolean flag = false;
                    for(phonecall call: tempPhoneCalls){
                        if(flag)
                            throw new Exception("Please provide one set of data to search for");

                        flag=true;
                        tempCall=call;
                    }
                    return get(this.url, "customer", bill.getCustomer(), "startTime", tempCall.getStartTimeString(), "endTime", tempCall.getEndTimeString());
                }
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            System.exit(1);
            return null;
        }
    }

    public Response addKeyValuePair( phonebill bill ) throws IOException
    {
        //return post( this.url, "customer", customer,"caller", caller, "callee", callee, "startTime", startTime, "endTime", endTime );

        return null;
    }
}
