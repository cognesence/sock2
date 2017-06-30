import org.nlogo.api.*;

/**
 * This extension contains a hook-up to some of the methods provided by the Java
 * String class. For further documentation check out the Java API documentation.
 * 
 * @author Simon Lynch
 */
public class SocketExtension extends org.nlogo.api.DefaultClassManager 
{
    private static NetlogoSocketIOStub comm;
    
    @Override
    public void load(PrimitiveManager primManager) {
        primManager.addPrimitive("connect-local", new ConnectLocal());
        primManager.addPrimitive("connect-distant", new ConnectDist());
	primManager.addPrimitive("serve-socket", new Advertise());
	primManager.addPrimitive("is-connected", new IsConnected());
	primManager.addPrimitive("disconnect", new Disconnect());
	primManager.addPrimitive("clear", new Clear());
	primManager.addPrimitive("peek", new Peek());
	primManager.addPrimitive("poll", new Poll());
	primManager.addPrimitive("read", new Take());
	primManager.addPrimitive("write", new Write());
	primManager.addPrimitive("is-empty", new IsEmpty());
    }

    /*
     * Extension requirements.
     */
    
    public String getExtensionName() {
        return "sock2";
    }

    public String getNLTypeName() {
        /*
         * Following comment taken from NetLogo table extension. Since this 
         * extension only defines one type, we don't need to give it a name; 
         * "table:" is enough, "table:table" would be redundant.
         */
        return "";
    }

    /* 
     * The exportWorld and importWorld methods not specified because there is no 
     * persistent data.
     */

    /*
     * Extension primitives.
     */
    
    /**
     * Connects to a socket via a served port number, return value indicates 
     * success/failure.
     */
    public static class ConnectLocal extends DefaultCommand
    {
        @Override
        public Syntax getSyntax() {
            return Syntax.commandSyntax(new int[]{Syntax.NumberType()});
        }

        @Override
        public String getAgentClassString() { 
            return "OTPL"; 
        }

        @Override
        public void perform(Argument args[], Context context) 
                throws ExtensionException, LogoException {
            int port = (int)args[0].getIntValue();
            comm = new NetlogoSocketIOStub(port, SocketConnection.CLIENT);
        }
    }

    public static class ConnectDist extends DefaultCommand
    {
        @Override
        public Syntax getSyntax() {
          return Syntax.commandSyntax(new int[]{Syntax.StringType(), 
              Syntax.NumberType()});
        }

        @Override
        public String getAgentClassString() { 
            return "OTPL"; 
        }

        @Override
        public void perform(Argument args[], Context context)
                throws ExtensionException, LogoException {
            String address = (String)args[0].get();
            int port = (int)args[1].getIntValue();
            comm = new NetlogoSocketIOStub(address, port,
                    SocketConnection.CLIENT);
        }
    }

    public static class Advertise extends DefaultCommand
    {
        @Override
        public Syntax getSyntax() {
          return Syntax.commandSyntax(new int[]{Syntax.NumberType()});
        }

        @Override
        public String getAgentClassString() { 
            return "OTPL"; 
        }

        @Override
        public void perform(Argument args[], Context context)
                throws ExtensionException, LogoException {
            int port = (int)args[0].getIntValue();
            comm = new NetlogoSocketIOStub(port, SocketConnection.SERVER);
        }
    }

    /**
     * Clears any waiting input from the socket connection.
     */
    public static class Clear extends DefaultCommand
    {
        @Override
        public Syntax getSyntax() {
            return Syntax.commandSyntax(new int[]{});
        }

        @Override
        public String getAgentClassString() { 
            return "OTPL"; 
        }

        @Override
        public void perform(Argument args[], Context context)
                throws ExtensionException, LogoException {
            comm.clear();
        }
    }

    /**
     * Retrieves, but does not remove, the next line of input from the socket.
     * Returns null if there is no input.
     */
    public static class Peek extends DefaultReporter
    {
        @Override
        public Syntax getSyntax() {
            return Syntax.reporterSyntax(new int[]{}, Syntax.StringType());
        }

        @Override
        public String getAgentClassString() { 
            return "OTPL"; 
        }

        @Override
        public Object report(Argument args[], Context context)
                throws ExtensionException, LogoException {
            return comm.peek();
        }
    }

    /**
     * Retrieves and removes the next input line from the socket. Returns null 
     * if there is no input. 
     */
    public static class Poll extends DefaultReporter
    {
        @Override
        public Syntax getSyntax() {
            return Syntax.reporterSyntax(new int[]{}, Syntax.StringType());
        }

        @Override
        public String getAgentClassString() { 
            return "OTPL"; 
        }

        @Override
        public Object report(Argument args[], Context context)
                throws ExtensionException, LogoException {
            return comm.poll();
        }
    }

    /**
     * Retrieves and removes the next input line from the socket. Waits if there 
     * is no input. 
     */
    public static class Take extends DefaultReporter
    {
        @Override
        public Syntax getSyntax() {
            return Syntax.reporterSyntax(new int[]{}, Syntax.StringType());
        }
        
        @Override
        public String getAgentClassString() { 
            return "OTPL"; 
        }

        @Override
        public Object report(Argument args[], Context context)
                throws ExtensionException, LogoException {
            return comm.take();
        }
    }

    /**
     * Returns true if the socket is connected, otherwise returns false.
     */
    public static class IsConnected extends DefaultReporter
    {
        @Override
        public Syntax getSyntax() {
            return Syntax.reporterSyntax(new int[]{}, Syntax.BooleanType());
        }

        @Override
        public String getAgentClassString() { 
            return "OTPL"; 
        }

        @Override
        public Object report(Argument args[], Context context)
                throws ExtensionException, LogoException {
            return comm.isConnected();
        }
    }

    public static class IsEmpty extends DefaultReporter
    {
        @Override
        public Syntax getSyntax() {
            return Syntax.reporterSyntax(new int[]{}, Syntax.BooleanType());
        }

        @Override
        public String getAgentClassString() { 
            return "OTPL"; 
        }

        @Override
        public Object report(Argument args[], Context context)
                throws ExtensionException, LogoException {
            return comm.isEmpty();
        }
    }

    /**
     * Writes a line of text onto the socket.
     */
    public static class Write extends DefaultCommand
    {
        @Override
        public Syntax getSyntax() {
            return Syntax.commandSyntax(new int[]{Syntax.StringType()});
        }

        @Override
        public String getAgentClassString() { 
            return "OTPL"; 
        }

        @Override
        public void perform(Argument args[], Context context)
                throws ExtensionException, LogoException {
            String str = (String)args[0].get();
            comm.write(str);
        }
    }

    public static class Disconnect extends DefaultCommand
    {
        @Override
        public Syntax getSyntax() {
            return Syntax.commandSyntax(new int[]{});
        }

        @Override
        public String getAgentClassString() { 
            return "OTPL"; 
        }

        @Override
        public void perform(Argument args[], Context context)
                throws ExtensionException, LogoException {
            comm.close();
        }
    }
}
