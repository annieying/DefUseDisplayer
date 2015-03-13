--- Setting up Headless Eclipse for testing---

Adapted from

	http://www.sable.mcgill.ca/ppa/tut_4.html

Write the "main" by implementing the IApplication interface

	Add the extension org.eclipse.core.runtime.applications (in the Extensions view)
	
	ID is the extension id (needed to invoke the application)
	
	Add "run" (right click application --> New --> run)
	The class specified is the "main" (click "class" to create a new class or verify)


To test it in Eclipse:
	
	Run --> Run Configurations 

	Program to Run --> Run an application --> <plugin-id>.<extension-id>


To run it command-line:
	
	Pre-install eclipse on $ECLIPSE_DIR: /diskless/local/annie/eclipse-servers/
	
	Export --> Deployable plugins and fragments

	Directory: $ECLIPSE_DIR/eclipse
	
	cd $ECLIPSE_DIR/eclipse

	Copy res directory to here
	
	./eclipse -nosplash -application DefUseDisplayer.server deployment # <plugin-id>.<extension-id> 


References:

	http://blogs.operationaldynamics.com/andrew/software/java-gnome/eclipse-code-format-from-command-line


--- Packaging it ---


Set variables for the directories we need

	export CODE=/diskless/local/annie/workspaces/20150223-headlessEclipseServer/
	
	export ZIP_DIR=/diskless/local/annie/workspaces/20140228-summarizer/headless-eclipse/zipped
	
	export WORKSPACE=/diskless/local/annie/workspaces/runtime-AstProject/

	export ECLIPSE=/diskless/local/annie/eclipse-rcp-helios-SR2/

Package Eclipse with PPA

	zip -r $ZIP_DIR/DefUseServer.zip ./

Generating the binary files

	Export --> Deployable plugins and fragments

	Archive it ($ZIP_DIR/formatter-eclipse-server.zip/plugins/*.jar)

Adding res directory to the zip file

	cd $ZIP_DIR
	
	cp -r $CODE/DefUseDisplayer/res $ZIP_DIR
	
	zip -r $ZIP_DIR/DefUseServer.zip ./
	
Zipping the workspace

	cd $ZIP_DIR
	
	cp -r $WORKSPACE $ZIP_DIR
	
	zip -r $ZIP_DIR/DefUseServerWorkspace.zip ./runtime-Ast
	
	
	
--- Running it on aspect ---

Pre-install eclipse 

	export SERVER_DIR=/local/annie/formatter/

Copy the zip with the plugins to the aspect server

	scp $ZIP_DIR/DefUseServer*.zip aying1@aspect.cs.mcgill.ca:/local/annie/formatter/
	
Unzip the plugins in the right place on aspect server

	cd $SERVER_DIR
	
	mv DefUseServer.zip eclipse
	
	unzip DefUseServer.zip
	
Unzip the workspace

	cd $SERVER_DIR
	
	unzip DefUseServerWorkspace.zip
	
Run the server

	cd $SERVER_DIR/eclipse

	./eclipse -nosplash -application DefUseDisplayer.server -data ../runtime-def-use/ deployment
	# '-application' flag expects <plugin-id>.<extension-id>
	# 'deployment' is a program argument when the program runs from command line (outside Eclipse
	# '-data' flag specifies the workspace
	
	
	
