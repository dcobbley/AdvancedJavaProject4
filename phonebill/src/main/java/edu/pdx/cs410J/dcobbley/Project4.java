package edu.pdx.cs410J.dcobbley;

import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * The main class that parses the command line and communicates with the
 * Phone Bill server using REST.
 */
public class Project4 {

    public static final String MISSING_ARGS = "Missing command line arguments";
    static ArrayList<String> commands; //used to keep track of all the commands that will be run at the end of the program
    static phonebill MyPhoneBill;//keep a local copy of the customer to be added
    static phonebill MySearchBill;//keep a local copy of the customer that we are looking for, only used for -search command

    public static void main(String... args) {
        setGlobalsToNull();
        int element = parseCommandsAtBeginning(args);
        parseCustomerIfExists(args, element);
        executeCommands();





        System.exit(0);
    }

    /**
     * Makes sure that the give response has the expected HTTP status code
     * @param code The expected status code
     * @param response The response from the server
     */
    private static void checkResponseCode( int code, HttpRequestHelper.Response response )
    {
        if (response.getCode() != code) {
            error(String.format("Expected HTTP code %d, got code %d.\n\n%s", code,
                                response.getCode(), response.getContent()));
        }
    }

    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);

        System.exit(1);
    }


    /** -----PARSE COMMANDS AT BEGINNING-----
     *
     * @param args
     * @throws
     */
    private static int parseCommandsAtBeginning(String[] args){
        int element = 0;
        try {
            if (args.length == 0) {
                throw new IllegalArgumentException("Cannot have zero arguments");
            }

            boolean flag = false;
            for (;element < args.length; element++) {
                //check if -print, -README, -textFile filename

                switch (args[element]) {
                    case "-README":
                        //add readme to the command list
                        addArgumentCommand("-README");
                        break;
                    case "-print":
                        //add print to the list
                        addArgumentCommand("-print");
                        break;
                    case "-textFile":
                        //check for ++element
                        if (args.length > element + 1) {
                            //save -textfile Filename
                            addArgumentCommand("-textFile");
                            addArgumentCommand(args[++element]);
                        } else {
                            //throw error
                            throw new IllegalArgumentException("-textFile argument must be followed by <filename>");
                        }
                        break;
                    case "-pretty":
                        //check for ++element
                        if (args.length > element + 1) {
                            //save -textfile Filename
                            addArgumentCommand("-pretty");
                            addArgumentCommand(args[++element]);
                        } else {
                            //throw error
                            throw new IllegalArgumentException("-pretty argument must be followed by <filename>");
                        }
                        break;
                    case "host":
                        //check for ++element
                        if (args.length > element + 1) {
                            //save -textfile Filename
                            addArgumentCommand("-host");
                            if(!dashExists(args[element+1]))
                                addArgumentCommand(args[++element]);
                            else
                                throw new IllegalArgumentException("-host must contains a valid <hostname>. No dashes allowed");
                        } else {
                            //throw error
                            throw new IllegalArgumentException("-host argument must be followed by <hostname>");
                        }
                        break;
                    case "port":
                        //check for ++element
                        if (args.length > element + 1) {
                            //save -textfile Filename
                            addArgumentCommand("-port");
                            if(!dashExists(args[element+1]))
                                addArgumentCommand(args[++element]);
                            else
                                throw new IllegalArgumentException("-port must contains a valid <port>. No dashes allowed");
                        } else {
                            //throw error
                            throw new IllegalArgumentException("-port argument must be followed by <port>");
                        }
                        break;
                    case "-search":
                        //add print to the list
                        addArgumentCommand("-search");
                        break;
                    default:
                        if(dashExists(args[element]))
                        {
                            throw new IllegalArgumentException("Non-Valid Argument");
                        }
                        return element;
                }
            }
        }
        catch(IllegalArgumentException ex){
            if(ex.getMessage()!= null)
                usage(ex.getMessage());
            else
                usage("");
            System.exit(1);
        }
        return element;
    }
    /**-----Check if a dash is in the beggning of the argument-----
     *
     */
    private static boolean dashExists(String arg){
        //Check if a dash exists in the arg
        return arg.startsWith("-");
    }

    /** -----PARSE CUSTOMER IF THEY EXIST-----
     *
     * @param args
     * @param element
     * @throws IllegalArgumentException if not enough args are provided
     */
    private static void parseCustomerIfExists(String[] args, int element){

        //collect all customer data and phone call data.
        //Try to use only locals as much as possible

        try {
            //check that element to element+8 exists
            if (args.length > element + 8) {
                if(args.length>element+8)
                    System.out.println("There are extra commands not getting parsed");

                //parse out customer information
                MyPhoneBill = new phonebill(args[element++], new phonecall(args[element++], args[element++], args[element++] + " " + args[element++]+ " "+ args[element++], args[element++] + " " + args[element++]+ " " + args[element++]));
                //                              customer                       caller            callee        starttime         +         date         +       am/pm         endtime           +         date        +         am:pm
            } else {
                if (args.length > element) {
                    if(args.length>element+6)
                        parseSearch(args,element);
                    else {
                        //didn't provide enough args
                        //throw error
                        throw new IllegalArgumentException("Not enough arguments provided");
                    }
                }
            }
        }
        catch(IllegalArgumentException ex){
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }

    /**-----Check if enough args exist to be a customer used for searching only, not creating a new one-----
     *
     */
    private static void parseSearch(String[] args, int element){
        try{
            phonecall tempPhoneCall = new phonecall();
            tempPhoneCall.setDate(args[element++] + " " + args[element++]+ " "+ args[element++],args[element++] + " " + args[element++]+ " "+ args[element++]);
            MySearchBill = new phonebill(args[element++], tempPhoneCall);
        }
        catch(IllegalArgumentException ex){
            if(ex.getMessage()!= null)
                usage(ex.getMessage());
        }
    }

    /** -----ADD AN ARGUMENT TO THE LIST-----
     *
     * @param arg is a single string to be added to the list of commands
     */
    private static void addArgumentCommand(String arg){
        //Modify the list array of commands.
        //This list of commands ie -README, -print, -textFile will get executed after any other work is done.
        if(!commands.contains(arg)){
            //Check if the list already contains it
            commands.add(arg);
        }
    }

    /** -----EXECUTE COMMANDS THAT EXIST-----
     * @throws Exception if something bad occurs
     * @TODO ask Whitlock what to do if they supply hostname and port number, but no phonebill or other command arguments
     */
    private static void executeCommands(){
        PhoneBillRestClient client; //Client for all the HTTP commands
        HttpRequestHelper.Response response; //Response from client command

        boolean printFlag   = false;
        boolean textFileFlag= false;
        boolean ReadmeFlag  = false;
        boolean pretty      = false;
        boolean host        = false;
        boolean port        = false;
        boolean search      = false;
        String fileName     = null;
        String prettyName   = null;
        String hostName     = null;
        int portNumber      = 0;


        try {
            for (String comm : commands) {
                switch(comm){
                    case "-textFile":
                        textFileFlag=true;
                        fileName = commands.get(commands.indexOf(comm)+1);
                        break;
                    case "-README":
                        ReadmeFlag = true;
                        break;
                    case "-print":
                        printFlag = true;
                        break;
                    case "-pretty":
                        pretty = true;
                        prettyName = commands.get(commands.indexOf(comm)+1);
                        break;
                    case "-host":
                        host= true;
                        hostName = commands.get(commands.indexOf(comm)+1);
                        break;
                    case "-port":
                        port = true;
                        try {
                            portNumber = Integer.parseInt(commands.get(commands.indexOf(comm)+1));

                        } catch (NumberFormatException ex) {
                            usage("Port \"" + commands.get(commands.indexOf(comm)+1) + "\" must be an integer");
                            return;
                        }
                        break;
                    case "-search":
                        search = true;
                    default:
                        //fileName = comm;
                        break;

                }
            }
            if(host == false){
                //throw new exception
                throw new Exception("No host to connect to");
            }
            else{
                //execute host stuff
                if(port == false){
                    //get mad
                    throw new Exception("No port to connect through");
                }
                else{
                    //be happy, do all the connection things here
                    client = new PhoneBillRestClient(hostName,portNumber);
                }
            }
            //Web app stuff, this is where it all happens
            try {
                if (search == true) {
                    //check that either MySearchBill is != null || MyPhoneBill!= null
                    if (MySearchBill != null) {
                        //do a GET with mysearchbill
                        response = client.getValues(MySearchBill);
                    } else if (MyPhoneBill != null) {
                        //do a GET with myphonebill
                        response = client.getValues(MyPhoneBill);
                    } else {
                        throw new Exception("No data to search for");
                    }
                } else if (MyPhoneBill != null) {
                    //do a POST of MyPhoneBill and update MyPhoneBill if any extra phone calls were on the server.
                    response = client.addKeyValuePair(MyPhoneBill);
                } else {
                    //They supplied no commands, check the server and getAllKeysAndValues???????

                    response = client.getAllKeysAndValues();
                }
                checkResponseCode(HttpURLConnection.HTTP_OK, response);
            }
            catch ( IOException ex ) {
                error("While contacting server: " + ex);
                return;
            }

            System.out.println(response.getContent());

            if(textFileFlag && pretty){
                //check to make sure they don't have the same fileName
                if(fileName.equals(prettyName)){
                    throw new Exception("textFile name and pretty name cannot be the same");
                }
            }
            if(textFileFlag){
                //textFileFunction(fileName);
                throw new Exception("Command \"-textFile\" is not supported in the current program");
            }
            if(printFlag){
                if (MyPhoneBill != null) {
                    System.out.println("Customer: " + MyPhoneBill.getCustomer() + " " + MyPhoneBill.getPhoneCalls());
                    printFlag = true;
                } else {
                    //MyphoneBill is null, throw exception
                    throw new Exception("Must provide a phone bill with the -print command");
                }
            }
            if(ReadmeFlag){
                Readme();
            }
            if(pretty){
                throw new Exception("Command \"-pretty\" is not supported in the current program");
                //prettyPrintFunction(prettyName);
            }
        }
        catch(Exception ex)
        {
            if(ex.getMessage()!=null) {
                usage(ex.getMessage());
            }
            else{
                usage("Empty Exception");
            }

            System.exit(1);
        }
    }


    /**-----README FUNCTION WITH ERRORS-----
     * Prints usage information for this program and exits
     * @param message An error message to print
     */
    private static void usage( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project4 host port [key] [value]");
        err.println("  host    Host of web server");
        err.println("  port    Port of web server");
        err.println("  key     Key to query");
        err.println("  value   Value to add to server");
        err.println();
        err.println("This simple program posts key/value pairs to the server");
        err.println("If no value is specified, then all values are printed");
        err.println("If no key is specified, all key/value pairs are printed");
        err.println();

        System.exit(1);
    }

    public static void setGlobalsToNull(){
        commands = null;
        MyPhoneBill = null;
        MySearchBill = null;
    }

    /**
     * Readme function contains the readme of all useful information the user may need to know.
     */
    private static void Readme() {
        System.out.println("README has been called");
        System.out.println("This program is a phonebill application which takes a very specific amount of arguments");
        System.out.println("You must provide a customer name, caller number, callee number, start time, and end time (mm/dd/yyyy mm:hh)");
        System.out.println();
        System.out.println("usage: java edu.pdx.cs410J.<login-id>.Project4 [options] <args>\n" +
                "args are (in this order):\n" +
                "customer Person whose phone bill we’re modeling\n" +
                "callerNumber Phone number of caller\n" +
                "calleeNumber Phone number of person who was called\n" +
                "startTime Date and time call began\n" +
                "endTime Date and time call ended\n" +
                "options are (options may appear in any order):\n" +
                "-host hostname Host computer on which the server runs\n" +
                "-port port Port on which the server is listening\n" +
                "-search Phone calls should be searched for\n" +
                "-print Prints a description of the new phone call\n" +
                "-README Prints a README for this project and exits\n" +
                "Dates and times should be in the format: mm/dd/yyyy hh:mm am");
    }
}