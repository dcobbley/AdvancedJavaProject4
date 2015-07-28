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
    private static final String[] SampleData = {"-host" ,HOSTNAME,"-port", PORT ,"David", "10/25/2015","11:25","am","10/25/2015", "11:50","am"};

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
        MainMethodResult result = invokeMain( Project4.class, SampleData);
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));
        String out = result.getOut();
        assertThat(out, out, containsString(Messages.getMappingCount(0)));
        //assertThat(out, out, containsString(Messages.formatKeyValuePair(key, null)));
        //disp(result.getErr(),result.getOut(), result.getExitCode());
    }

    @Test
    public void test4AddValue() {
        String key = "KEY";
        String value = "VALUE";

        MainMethodResult result = invokeMain( Project4.class, HOSTNAME, PORT, key, value );
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));
        String out = result.getOut();
        assertThat(out, out, containsString(Messages.mappedKeyValue(key, value)));

        result = invokeMain( Project4.class, HOSTNAME, PORT, key );
        out = result.getOut();
        assertThat(out, out, containsString(Messages.getMappingCount(1)));
        assertThat(out, out, containsString(Messages.formatKeyValuePair(key, value)));

        result = invokeMain( Project4.class, HOSTNAME, PORT );
        out = result.getOut();
        assertThat(out, out, containsString(Messages.getMappingCount(1)));
        assertThat(out, out, containsString(Messages.formatKeyValuePair(key, value)));
        //disp(result.getErr(),result.getOut(), result.getExitCode());
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