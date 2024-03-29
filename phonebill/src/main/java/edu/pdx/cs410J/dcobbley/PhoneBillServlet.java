package edu.pdx.cs410J.dcobbley;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>PhoneBill</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple key/value pairs.
 */
public class PhoneBillServlet extends HttpServlet
{
    //private final Map<String, String> data = new HashMap<>();
    private final Map<String, phonebill> data = new HashMap<String, phonebill>();
    private phonebill phoneBill = null;

    /**
     * Handles an HTTP GET request from a client by writing the value of the key
     * specified in the "key" HTTP parameter to the HTTP response.  If the "key"
     * parameter is not specified, all of the key/value pairs are written to the
     * HTTP response.
     * @param request is the text coming in from the url that needs to be parsed
     * @param response url for where the response goes
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );


        String customer = getParameter( "customer", request );
        String startTime = getParameter( "startTime", request );
        String endTime = getParameter( "endTime", request );

        //check to see if the date.customer == null

        if(customer != null && startTime != null && endTime != null){
            phonecall tempPhonecall = new phonecall();
            tempPhonecall.setDate(startTime,endTime);
            //client is performing a search
            //return all phonecalls that begin between the start and end time
            writeSearchValue(new phonebill(customer,tempPhonecall,"-search"),response);
        }
        else if(customer != null && startTime == null && endTime == null){
            //Client is trying writevalue
            writeValue(customer,response);
        }
        else{
            writeAllMappings(response);
        }
    }

    /**
     * Handles an HTTP POST request by storing the key/value pair specified by the
     * "key" and "value" request parameters.  It writes the key/value pair to the
     * HTTP response.
     * @param request is the text coming in from the url that needs to be parsed
     * @param response url for where the response goes
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );


        String customer = getParameter( "customer", request );
        if (customer == null) {
            missingRequiredParameter( response, "customer" );
            return;
        }
        String caller = getParameter( "caller", request );
        if (caller == null) {
            missingRequiredParameter( response, "caller" );
            return;
        }
        String callee = getParameter( "callee", request );
        if (callee == null) {
            missingRequiredParameter( response, "callee" );
            return;
        }
        String startTime = getParameter( "startTime", request );
        if (startTime == null) {
            missingRequiredParameter( response, "startTime" );
            return;
        }
        String endTime = getParameter( "endTime", request );
        if (endTime == null) {
            missingRequiredParameter( response, "endTime" );
            return;
        }

        PrintWriter pw = response.getWriter();
        //Now we have all relavent information about customers and their phonecalls
        if(data!=null &&data.get(customer) != null){
            //Customer exists, just add a new phonecall
            phoneBill = data.get(customer);
            phoneBill.addPhoneCall(new phonecall(caller, callee, startTime, endTime));
            data.put(customer, phoneBill);
            System.out.println("attempting to add new phonecall");


        }
        else{
            //Customer doesn't exist, create a new phonebill.
            data.put(customer, new phonebill(customer, new phonecall(caller, callee, startTime, endTime)));
            System.out.println("new customer added");
            pw.println("attempting to add a new customer");

        }

        int counter=0;
        Collection<phonecall> phoneCalls = data.get(customer).getPhoneCalls();
        pw.println("  _____  _                        ____  _ _ _   ____   ___   ___   ___  \n" +
                " |  __ \\| |                      |  _ \\(_) | | |___ \\ / _ \\ / _ \\ / _ \\ \n" +
                " | |__) | |__   ___  _ __   ___  | |_) |_| | |   __) | | | | | | | | | |\n" +
                " |  ___/| '_ \\ / _ \\| '_ \\ / _ \\ |  _ <| | | |  |__ <| | | | | | | | | |\n" +
                " | |    | | | | (_) | | | |  __/ | |_) | | | |  ___) | |_| | |_| | |_| |\n" +
                " |_|    |_| |_|\\___/|_| |_|\\___| |____/|_|_|_| |____/ \\___/ \\___/ \\___/ \n" +
                "                                                                        \n" +
                "                                                                        ");
        pw.println("#     customer      caller      callee           Start Time        End Time        Duration \n");

        for(phonecall call: phoneCalls){
            pw.println(++counter +" "+ customer+ "  "+call.getCaller()+ "  "+call.getCallee()+"   "+call.getStartTimeString()+"  "+call.getEndTimeString()+  "   "+call.duration()+"\n");
            //writer.write(++x +" "+mine.getCaller()+ "  "+mine.getCallee()+"   "+mine.getStartTimeString()+"  "+mine.getEndTimeString()+  "   "+mine.duration()+"\n");
        }

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK);
    }

    /**
     * Writes an error message about a missing parameter to the HTTP response.
     *
     * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
     * @param parameterName used to write an error about bad parameters
     * @param response url for where the response goes
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
        throws IOException
    {
        PrintWriter pw = response.getWriter();
        pw.println( Messages.missingRequiredParameter(parameterName));
        pw.flush();
        
        response.setStatus( HttpServletResponse.SC_PRECONDITION_FAILED );
    }

    /**
     * Writes the value of the given key to the HTTP response.
     *
     * The text of the message is formatted with {@link Messages#getMappingCount(int)}
     * and {@link Messages#formatKeyValuePair(String, String)}
     * @TODO use the messages.java class to format properly.
     * @TODO does this need to be pretty printing?
     * @param customer is the name of the phonebills customer
     * @param response url for where the response goes
     */
    private void writeValue( String customer, HttpServletResponse response ) throws IOException
    {
        //String value = this.data.get(key);

        PrintWriter pw = response.getWriter();
        //pw.println(Messages.getMappingCount( value != null ? 1 : 0 ));
        //pw.println(Messages.formatKeyValuePair( key, value ));
        //pw.println("WriteValue function to be displayed on server page");
        if(data.get(customer)!= null) {
            //pw.println(data.get(customer).toString());
            int counter=0;
            Collection<phonecall> phoneCalls = data.get(customer).getPhoneCalls();
            pw.println("  _____  _                        ____  _ _ _   ____   ___   ___   ___  \n" +
                    " |  __ \\| |                      |  _ \\(_) | | |___ \\ / _ \\ / _ \\ / _ \\ \n" +
                    " | |__) | |__   ___  _ __   ___  | |_) |_| | |   __) | | | | | | | | | |\n" +
                    " |  ___/| '_ \\ / _ \\| '_ \\ / _ \\ |  _ <| | | |  |__ <| | | | | | | | | |\n" +
                    " | |    | | | | (_) | | | |  __/ | |_) | | | |  ___) | |_| | |_| | |_| |\n" +
                    " |_|    |_| |_|\\___/|_| |_|\\___| |____/|_|_|_| |____/ \\___/ \\___/ \\___/ \n" +
                    "                                                                        \n" +
                    "                                                                        ");
            pw.println("#     customer      caller      callee           Start Time        End Time        Duration \n");

            for(phonecall call: phoneCalls){
                pw.println(++counter +" "+ customer+ "  "+call.getCaller()+ "  "+call.getCallee()+"   "+call.getStartTimeString()+"  "+call.getEndTimeString()+  "   "+call.duration()+"\n");
                //writer.write(++x +" "+mine.getCaller()+ "  "+mine.getCallee()+"   "+mine.getStartTimeString()+"  "+mine.getEndTimeString()+  "   "+mine.duration()+"\n");
            }
        }
        else
            pw.println("Customer does not exists");

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK );
    }

    /**
     *@param bill is the phonebill to be searched
     * @param response url for where the response goes
     */
    private void writeSearchValue( phonebill bill, HttpServletResponse response ) throws IOException
    {
        //String value = this.data.get(key);

        PrintWriter pw = response.getWriter();
        //pw.println(Messages.getMappingCount( value != null ? 1 : 0 ));
        //pw.println(Messages.formatKeyValuePair( key, value ));
        //pw.println("WriteValue function to be displayed on server page");
        String customer = bill.getCustomer();
        Long begin = bill.searchCallOnly.startTime.getTime();

        if(data.get(customer)!= null) {
            pw.println("Searching for: " +data.get(customer).toString());
            Collection<phonecall> phoneCalls = data.get(customer).getPhoneCalls();
            boolean flag = true;
            int counter = 0;
            pw.println("  _____  _                        ____  _ _ _   ____   ___   ___   ___  \n" +
                    " |  __ \\| |                      |  _ \\(_) | | |___ \\ / _ \\ / _ \\ / _ \\ \n" +
                    " | |__) | |__   ___  _ __   ___  | |_) |_| | |   __) | | | | | | | | | |\n" +
                    " |  ___/| '_ \\ / _ \\| '_ \\ / _ \\ |  _ <| | | |  |__ <| | | | | | | | | |\n" +
                    " | |    | | | | (_) | | | |  __/ | |_) | | | |  ___) | |_| | |_| | |_| |\n" +
                    " |_|    |_| |_|\\___/|_| |_|\\___| |____/|_|_|_| |____/ \\___/ \\___/ \\___/ \n" +
                    "                                                                        \n" +
                    "                                                                        ");
            pw.println("#     customer      caller      callee           Start Time        End Time        Duration \n");

            for(phonecall call: phoneCalls){
                if(begin>= (Long)call.startTime.getTime()&&begin<=(Long)call.endTime.getTime()){
                    flag = false;
                    pw.println(++counter +" "+ customer+ "  "+call.getCaller()+ "  "+call.getCallee()+"   "+call.getStartTimeString()+"  "+call.getEndTimeString()+  "   "+call.duration()+"\n");
                   //writer.write(++x +" "+mine.getCaller()+ "  "+mine.getCallee()+"   "+mine.getStartTimeString()+"  "+mine.getEndTimeString()+  "   "+mine.duration()+"\n");

                }
            }
            if(flag){
                //No phone calls match your request
                pw.println("No phonecalls match for: "+customer);
            }
        }
        else
            pw.println("Customer does not exists");

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK );
    }
    /**
     * Writes all of the key/value pairs to the HTTP response.
     *
     * The text of the message is formatted with
     * {@link Messages#formatKeyValuePair(String, String)}
     * @param response url for where the response goes
     */
    private void writeAllMappings( HttpServletResponse response ) throws IOException
    {
        PrintWriter pw = response.getWriter();
        pw.println(Messages.getMappingCount( data.size() ));

        for (Map.Entry<String, phonebill> entry : this.data.entrySet()) {
            pw.println(Messages.formatKeyValuePair(entry.getKey(), entry.getValue().toString()));
        }

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK );
    }

    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @return <code>null</code> if the value of the parameter is
     *         <code>null</code> or is the empty string
     *
     */
    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;

      } else {
        return value;
      }
    }

}
