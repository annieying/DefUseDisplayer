Installing Eclipse with PPA (version Helio)

	Unzip Eclipse in $ECLIPSE_DIR
	
	
Installing the server

	cd $ECLIPSE_DIR$/eclipse
	
	Copy DefUseServer.zip to $ECLIPSE_DIR$/eclipse 
   
	Unzip DefUseServer.zip - which creates plugins/*.jar and res 
   	

Installing the workspacee

	cd $ECLIPSE_DIR$
	
	

Running the server as headless Eclipse

	cd $ECLISE_DIR/eclipse

	screen
	
	Ctrl-a Shift-a  # To give a meaningful name to the current window
	   
	./eclipse -nosplash -application DefUseDisplayer.server -data ../runtime-def-use deployment 
	# '-data' flag specifies the location of the workspace
	# 'deployment' is a program argument
	

	References:
		http://blogs.operationaldynamics.com/andrew/software/java-gnome/eclipse-code-format-from-command-line
		
		
Accessing the web site

	http://aspect.cs.mcgill.ca:8844/Formatter