package edu.pdx.cs410J.dcobbley;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

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
    private static final String[] CustomerSearchA1 = {"-host" ,HOSTNAME,"-port", PORT ,"David", "10/25/2015","11:25","am","10/25/2015", "11:50","am"};
    private static final String[] customerB1 =  {"-host" ,HOSTNAME,"-port", PORT ,"Steph","503-111-2345","503-445-6778", "10/25/2015","11:25","am","10/25/2015", "11:50","am"};
    private static final String[] customerSearchB1 =  {"-host" ,HOSTNAME,"-port", PORT ,"Steph","10/25/2015","11:25","am","10/25/2015", "11:50","am"};

    @Test
    public void test1NoCommandLineArguments() {
        MainMethodResult result = invokeMain( Project4.class );
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getErr(), containsString(Project4.MISSING_ARGS));

    }

    @Test
    public void test2EmptyServer() {
        MainMethodResult result = invokeMain( Project4.class,"-host" ,HOSTNAME,"-port", PORT );
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
        assertThat(out, out, containsString(Messages.getMappingCount(0)));
        //assertThat(out, out, containsString(Messages.formatKeyValuePair(key, null)));
        //disp(result.getErr(),result.getOut(), result.getExitCode());
    }

    @Test
    public void test4AddValue() {

        MainMethodResult result = invokeMain( Project4.class, customerA1 );
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));
        String out = result.getOut();
        //assertThat(out, out, containsString(Messages.mappedKeyValue(key, value)));
/*
        result = invokeMain( Project4.class, HOSTNAME, PORT, key );
        out = result.getOut();
        assertThat(out, out, containsString(Messages.getMappingCount(1)));
        assertThat(out, out, containsString(Messages.formatKeyValuePair(key, value)));

        result = invokeMain( Project4.class, HOSTNAME, PORT );
        out = result.getOut();
        assertThat(out, out, containsString(Messages.getMappingCount(1)));
        assertThat(out, out, containsString(Messages.formatKeyValuePair(key, value)));*/
        disp(result.getErr(),result.getOut(), result.getExitCode());
    }
    @Test
    public void Testy(){
        MainMethodResult result = invokeMain(Project4.class,HOSTNAME,PORT, "stuff", "things");
        System.out.println(result.getOut());
    }

    private void disp(String error, String toDisplay,int code ){
        System.out.println("******************************");
        System.out.println();
        System.out.println("Code: "+code);
        System.out.println("-----");
        System.out.println(toDisplay);
        System.out.println("-----");
        System.out.println("Error: "+error);
        System.out.println();
        System.out.println("******************************");
    }

}