package edu.pdx.cs410J.dcobbley;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.AbstractPhoneCall;

import java.util.*;

/**
 * Created by david on 7/6/15.
 * A phonebill object contains the customers name as well as all phonecalls they have made
 */

public class phonebill extends AbstractPhoneBill{
    String customer;
    ArrayList<phonecall> phoneCalls;
    phonecall searchCallOnly;
    phonecall singlePhoneCall;

    /**
     * Constructor is essentially a setter function. creates a new list which will hold all additional phonecalls.
     * @param customer Name of the customer
     * @param phoneCall An instance of the phone call which took place.
     */
    phonebill(String customer, phonecall phoneCall)
    {
        this.customer = customer;
        phoneCalls = new ArrayList<phonecall>();
        addPhoneCall(phoneCall);
        searchCallOnly=null;
        singlePhoneCall = null;
    }
    phonebill(String customer)
    {
        this.customer = customer;
        phoneCalls = new ArrayList<phonecall>();
        searchCallOnly=null;
        singlePhoneCall = null;
    }
    phonebill()
    {
        //Create an empty phonebill
        customer = "";
        phoneCalls = new ArrayList<phonecall>();
        searchCallOnly=null;
        singlePhoneCall = null;
    }

    public phonebill(String customer, phonecall tempPhoneCall, String s) {
        if(s.equals("-search")) {
            this.customer = customer;
            phoneCalls = null;
            searchCallOnly = tempPhoneCall;
        }
        if(s.equals("-single")) {
            this.customer = customer;
            phoneCalls = null;
            singlePhoneCall = tempPhoneCall;
        }
    }

    /**
     *
     * @return Returns customer name - Getter function
     */
    @Override
    public String getCustomer() {
        return customer;
    }

    /**
     *
     * @param abstractPhoneCall Takes an instance of the phone call and adds it to the list
     */
    @Override
    public void addPhoneCall(AbstractPhoneCall abstractPhoneCall) {
        boolean addPhoneCall = true;
        for(AbstractPhoneCall call:phoneCalls){
            if(call.toString().equals(abstractPhoneCall.toString())) {
                addPhoneCall = false;
            }
        }
        if(addPhoneCall) {
            phoneCalls.add((phonecall)abstractPhoneCall);
        }

        Collections.sort(phoneCalls);


    }

    /**
     *
     * @return Returns a list of all phonecalls made - Getter function
     */
    @Override
    public Collection getPhoneCalls() {
        return phoneCalls;
    }
}