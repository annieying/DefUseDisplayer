package defuse.server;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class DefUseEclipseServerApplication implements IApplication {

	public static boolean TEST;
	
	@Override
	public Object start(IApplicationContext context) throws Exception {

		setArguments(context);		
		
		System.out.println("inside DefUseEclipseServerApplication");
		System.out.println("Workspace in " + ResourcesPlugin.getWorkspace().getRoot().getLocation().toString());

		Server server = new Server(8845);
		Context root = new Context(server,"/",Context.SESSIONS);
		root.addServlet(new ServletHolder(new  DefUseEclipseSerlvet()), "/*");	 
		server.start();
		server.join();
		return null;
	}

	@Override
	public void stop() {
	}

	private void setArguments(IApplicationContext context) {
        Object argsValue = context.getArguments().get("application.args");
        if( argsValue == null || ! (argsValue instanceof String[]) ) {
            System.out.println("Put either 'development' or 'deployment' as the first argument");
        } else { 
            String[] args = (String[])argsValue;
            if( args.length == 0 ) {
        		TEST = true;
            } else {
            	if( args[0].toLowerCase().equals("development") ) {
            		TEST = true;
            	} else if( args[0].toLowerCase().equals("deployment")) {
            		TEST = false;
            	} else {
                    System.out.println("Require argument 'development' or 'deployment' as the first argument");            		
            	}                
            }
        }
	}
}
