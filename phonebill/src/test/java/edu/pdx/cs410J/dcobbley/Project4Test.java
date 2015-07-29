package edu.pdx.cs410J.dcobbley;

import edu.pdx.cs410J.InvokeMainTestCase;
import junit.framework.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * Tests the {@link Project4} class by invoking its main method with various arguments 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Project4Test extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");
    private static final String[] BadPort =  {"-host" ,HOSTNAME,"-port", System.getProperty("http.port", "1234") ,"David","503-709-4866","503-880-6960", "10/25/2015","11:25","am","10/25/2015", "11:50","am"};
    private static final String[] customerA1 =  {"-host" ,HOSTNAME,"-port", PORT ,"David","503-709-4866","503-880-6960", "10/25/2015","11:25","am","10/25/2015", "11:50","am"};
    private static final String[] customerA2 =  {"-host" ,HOSTNAME,"-port", PORT ,"David","503-709-4866","503-231-8877", "10/28/2015","6:23","am","10/28/2015", "1:50","pm"};
    private static final String[] CustomerSearchA1 = {"-host" ,HOSTNAME,"-port", PORT ,"-search","David", "10/25/2015","11:35","AM","10/25/2015", "11:50","AM"};
    private static final String[] customerB1 =  {"-host" ,HOSTNAME,"-port", PORT ,"-print","Steph","503-111-2345","503-445-6778", "10/25/2015","11:25","am","10/25/2015", "11:50","am"};
    private static final String[] customerSearchB1 =  {"-host" ,HOSTNAME,"-port", PORT ,"-search","Steph","10/25/2015","11:25","am","10/25/2015", "11:50","am"};
    private static final String[] customerSearchNoName =  {"-host" ,HOSTNAME,"-port", PORT ,"-search","Zaphod Beeblebrox ","10/25/2015","11:25","am","10/25/2015", "11:50","am"};
    private static final String[] customerSearchWriteAll =  {"-host" ,HOSTNAME,"-port", PORT};

    @Test
    public void test1NoCommandLineArguments() {
        MainMethodResult result = invokeMain( Project4.class );
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getErr(), containsString(Project4.MISSING_ARGS));

    }

    @Test
    public void test2EmptyServer() {
        MainMethodResult result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT);
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));
        String out = result.getOut();
        assertThat(out, out, containsString(Messages.getMappingCount(0)));
        //disp(result.getErr(), result.getOut(), result.getExitCode());
    }

    @Test
    public void test3NoValues() {
        MainMethodResult result = invokeMain( Project4.class, CustomerSearchA1);
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));
        String out = result.getOut();
        assertThat(out, out, containsString("Customer does not exists"));
        //assertThat(out, out, containsString(Messages.formatKeyValuePair(key, null)));
        disp(result.getErr(),result.getOut(), result.getExitCode());
    }

    @Test
    public void test4AddValue() {

        MainMethodResult result = invokeMain(Project4.class, customerA1);
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));
        String out = result.getOut();
        //assertThat(out, out, containsString("attempting to add a new customer"));
        disp(result.getErr(), result.getOut(), result.getExitCode());

        result = invokeMain( Project4.class, customerA1);
        out = result.getOut();
        //assertThat(out, out, containsString("attempting to add a new phonecall to existing customer"));
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));
        disp(result.getErr(),result.getOut(), result.getExitCode());

        result = invokeMain( Project4.class, customerB1);
        out = result.getOut();
        //assertThat(out, out, containsString("attempting to add a new customer"));
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));
        disp(result.getErr(), result.getOut(), result.getExitCode());

        result = invokeMain( Project4.class, customerA2);
        out = result.getOut();
       // assertThat(out, out, containsString("attempting to add a new phonecall to existing customer"));
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));
        disp(result.getErr(),result.getOut(), result.getExitCode());
    }

    @Test
    public void test5InvalidHostName() {
        MainMethodResult result = invokeMain(Project4.class, BadPort);
        assertThat(result.getErr(), result.getExitCode(), equalTo(1));
        String out = result.getErr().trim();
        assertThat(out, out, containsString("Please try again with a a valid host name"));
        //disp(result.getErr(), result.getOut(), result.getExitCode());
    }

    @Test
    public void test6UserDoesNotExist() {
        MainMethodResult result = invokeMain(Project4.class, customerSearchNoName);
        //assertThat(result.getErr(), result.getExitCode(), equalTo(1));
        String out = result.getOut().trim();
        //disp(result.getErr(), result.getOut(), result.getExitCode());
        assertEquals(out, "Customer does not exists");

    }

    @Test
    public void test7Search() {
        MainMethodResult result = invokeMain( Project4.class, CustomerSearchA1);
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));
        String out = result.getOut();
        //assertThat(out, out, containsString(Messages.getMappingCount(0)));
        //assertThat(out, out, containsString(Messages.formatKeyValuePair(key, null)));
        disp(result.getErr(),result.getOut(), result.getExitCode());
    }

    @Test
    public void test8WriteAll() {
        MainMethodResult result = invokeMain( Project4.class);
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));
        String out = result.getOut();
        assertThat(out, out, containsString(Messages.getMappingCount(0)));
        //assertThat(out, out, containsString(Messages.formatKeyValuePair(key, null)));
        //disp(result.getErr(),result.getOut(), result.getExitCode());
    }






    private void disp(String error, String toDisplay,int code ){
        System.out.println("******************************");
        System.out.println();
        System.out.println("Code: "+code);
        System.out.println("-----");
        System.out.println(toDisplay);
        System.out.println("-----");
        System.out.println("Error: " + error);
        System.out.println();
        System.out.println("******************************");
    }

    private String errorDisplay(String err){
        return err+"usage: java Project4 host port [key] [value]\n" +
                "  host    Host of web server\n" +
                "  port    Port of web server\n" +
                "  key     Key to query\n" +
                "  value   Value to add to server\n" +
                "\n" +
                "This simple program posts key/value pairs to the server\n" +
                "If no value is specified, then all values are printed\n" +
                "If no key is specified, all key/value pairs are printed";
    }

    private String errorConnecting(){
        return "** While contacting server: Connection refused: connect,\n" +
                "Please try again with a a valid host name\n" +
                "\n" +
                "** Empty Exception\n" +
                "\n";
    }

}



