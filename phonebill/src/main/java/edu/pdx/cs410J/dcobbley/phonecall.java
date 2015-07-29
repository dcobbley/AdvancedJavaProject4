package edu.pdx.cs410J.dcobbley;

import edu.pdx.cs410J.AbstractPhoneCall;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by david on 7/6/15.
 * A single instance of a phonecall that has occured. Includes a caller and callee number
 */
public class phonecall extends AbstractPhoneCall implements Comparable<phonecall>{
    String callerNumber;
    String calleeNumber;
    Date startTime;
    Date endTime;
    //DateFormat dateFormat;
    DateFormat ShortDateFormat;

    /**
     * Constructor for the phonecall class. Holds all relavent data for a particular phonecall
     * @param callerNumber The phone number of the customer
     * @param calleeNumber The phone number that the customer is trying to reach
     * @param startTime The time at which the phonecall began
     * @param endTime The time at which the phonecall ended
     */
    phonecall(String callerNumber, String calleeNumber, String startTime, String endTime){
        /*startTime=null;
        endTime=null;*/
        //dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        ShortDateFormat = new SimpleDateFormat("MM/dd/yyy hh:mm a", Locale.ENGLISH);
        //Check for bad data
        try{
            if(startTime.contains("\"")||endTime.contains("\""))
                throw new IllegalArgumentException("Date and time cannot contain quotes ");

            if(!callerNumber.matches("\\d{3}-\\d{3}-\\d{4}$")||!calleeNumber.matches("\\d{3}-\\d{3}-\\d{4}$"))
                throw new IllegalArgumentException("Valid phone numbers must contain exactly 10 numbers plus two dashes");

            String[] tempStart = startTime.split(" ");
            String[] tempEnd= endTime.split(" ");

            if(!tempStart[0].matches("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)")||!tempEnd[0].matches("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)")) {
                throw new IllegalArgumentException("Date format must follow mm/dd/yyyy");
            }

            if(!tempStart[1].matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")||!tempEnd[1].matches("([01]?[0-9]|2[0-3]):[0-5][0-9]"))
                throw new IllegalArgumentException("Time format must follow mm:hh (12 hour time)");
            if(!tempStart[2].matches("(am|pm|AM|PM)")&&!tempEnd[2].matches("(am|pm|AM|PM)"))
                throw new IllegalArgumentException("Time must include am/pm");
        }
        catch(IllegalArgumentException ex){
            System.out.println(ex.getMessage());
            System.exit(1);
        }

        this.callerNumber = callerNumber;
        this.calleeNumber = calleeNumber;
        setDate(startTime,endTime);
    }
    phonecall(){
        //dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        ShortDateFormat = new SimpleDateFormat("MM/dd/yyy hh:mm a", Locale.ENGLISH);
        try {
            calleeNumber = "";
            callerNumber = "";
            startTime =null;
            endTime = null;
        }
        catch(Exception ex){
            System.out.println(ex);
            System.exit(1);
        }
    }

    public void setDate(String start, String end){
        try {
            this.startTime=ShortDateFormat.parse(start);
            this.endTime = ShortDateFormat.parse(end);
        }
        catch(ParseException ex){
            System.out.println("Error Parsing the time, please enter valid time, dont forget to include am/pm " +ex.getMessage());
            System.exit(1);
        }
    }

    /**
     *
     * @return Returns callerNumber - Getter function
     */
    @Override
    public String getCaller() {
        return callerNumber;
    }

    /**
     *
     * @return Returns calleeNumber - Getter function
     */
    @Override
    public String getCallee() {
        return calleeNumber;
    }

    /**
     *
     * @return Returns startTime - Getter function
     */
    @Override
    public String getStartTimeString() {
        if(startTime != null)
            return (ShortDateFormat.format(startTime));
        else
            return "";
    }

    /**
     *
     * @return Returns endTime - Getter function
     */
    @Override
    public String getEndTimeString() {

        //System.out.println("String Date Formatting "+endTime.getTim )
        if(endTime != null)
            return (ShortDateFormat.format(endTime));
        else
            return "";
    }

    public String duration(){
        long duration =startTime.getTime()-endTime.getTime();
        long diffMinutes = duration / (60 * 1000) % 60;
        long diffHours = duration / (60 * 60 * 1000);
        if(diffHours ==0)
            return -1*diffMinutes + " minutes";
        else
            return "  "+-1*diffHours+":"+ -1*diffMinutes +"min";
    }



    public Date compareSetData(String end){
        String[] endArray=end.split("[ /:]");
        Date mine = new Date(Integer.parseInt(endArray[2])-1900, (Integer.parseInt(endArray[0]))-1, Integer.parseInt(endArray[1]),Integer.parseInt(endArray[3]),Integer.parseInt(endArray[4]),0);
        return mine;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(phonecall o) {
        try {
            if (this.startTime == null) {
                throw new NullPointerException("No start time to compare");
            }
            if (o.startTime == null) {
                throw new NullPointerException("No end time to compare");
            }
            long diff = this.startTime.getTime()-o.startTime.getTime();

            if (diff > 0) {
                return 1;
            }
            if (diff < 0) {
                return -1;
            }
            if (diff == 0) {
                //equal - differ by caller number
                //String numberDiff = this.callerNumber-o.callerNumber;
                String callerStringA = this.getCaller();
                String callerStringB = o.getCaller();
                callerStringA = callerStringA.replaceAll("\\D", "");
                callerStringB = callerStringB.replaceAll("\\D", "");
                long numberDiff = Long.parseLong(callerStringA)-Long.parseLong(callerStringB);

                if(numberDiff >0){
                    return 1;
                }
                if(numberDiff<0){
                    return -1;
                }
                if(numberDiff == 0){
                    return 0;
                }
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            System.exit(1);
        }
        return 0;
    }
}