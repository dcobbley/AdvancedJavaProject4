package edu.pdx.cs410J.dcobbley;

/**
 * Class for formatting messages on the server side.  This is mainly to enable
 * test methods that validate that the server returned expected strings.
 */
public class Messages
{
    public static String getMappingCount( int count )
    {
        return String.format( "Server contains %d customer/phonebill pairs", count );
    }

    public static String formatKeyValuePair( String customer, String bill )
    {
        return String.format("  %s -> %s", customer, bill);
    }

    public static String missingRequiredParameter( String parameterName )
    {
        return String.format("The required parameter \"%s\" is missing", parameterName);
    }

    public static String mappedKeyValue( String customer, String bill )
    {
        return String.format( "Mapped %s to %s", customer, bill );
    }
}
