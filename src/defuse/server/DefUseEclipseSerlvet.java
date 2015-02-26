package defuse.server;

//Import required java libraries
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.mortbay.util.StringUtil;

import defuse.AstUtil;
import defuse.DefUse;

public class DefUseEclipseSerlvet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String FORMATTED_CODE_FILENAME = "./res/html/def-use-index.html";
	private static final String FORMATTED_CODE_PLACEHOLDER = "<!--***FORMATTED CODE***-->";
    private static final String UNFORMATTED_CODE_PLACEHOLDER = "<!--***CODE***-->";

	private static final String FORMATTED_CODE_TEMPLATE = "<pre class=\"prettyprint\">" 
	        + FORMATTED_CODE_PLACEHOLDER + "</pre>";
	
    private static final String DEF_USE_PLACEHOLDER = "<!--***DEF-USE OUTPUT***-->";
    
	private static final String PARSING_ATTRIBUTE_PLACEHOLDER = "***PARSING_ATTRIBUTE***";
	private static final String PARSING_ATTRIBUTE_ROW = "<input type=\"radio\" name=\"parsing-attribute\" " 
			+ "value=\"" + PARSING_ATTRIBUTE_PLACEHOLDER + "\">" 
			+ PARSING_ATTRIBUTE_PLACEHOLDER + "<br>";
	private static final String PARSING_HTML_PLACEHOLDER = "<!--***PARSING_ATTRIBUTES***-->";
	
	enum  ParsingAttribute {
		JavaClassBodyMemberDeclaration,
		JavaCompilationUnit,
		JavaBlockStatements,
		JavaSwitchBlockStatementGroup,
		JavaMethodDeclaration
	}
	
    public void doGet(HttpServletRequest aRequest, HttpServletResponse aResponse) 
            throws ServletException, IOException {
        doPost(aRequest, aResponse);
    }
	
	public void doPost(HttpServletRequest aRequest, HttpServletResponse aResponse)
			throws ServletException, IOException {
		String path = aRequest.getPathInfo();
		String url = aRequest.getRequestURL().toString();
		int colon = url.lastIndexOf(":") + 1;
		int end = url.indexOf("/", colon);
		String hostAndPort = url.substring("http://".length(), end);
		long time = System.currentTimeMillis();


		if( path.startsWith("/DefUse") ) { 
			
			String code =  aRequest.getParameter("code"); 
			String parsingAttribute = aRequest.getParameter("parsing-attribute");

            aResponse.setContentType("text/html");
            aResponse.setStatus(HttpServletResponse.SC_OK);
            ServletOutputStream out = aResponse.getOutputStream();
            
            String htmlString = FileUtils.readFileToString(
            		getWebFileFromBundle(FORMATTED_CODE_FILENAME));
            
            htmlString = htmlString.replace(PARSING_HTML_PLACEHOLDER, getParsingAttributesHtml());    
                        
            if( code == null ) {
            	htmlString = htmlString.replace(UNFORMATTED_CODE_PLACEHOLDER, "");
            } else {            	
            	htmlString = htmlString.replace(UNFORMATTED_CODE_PLACEHOLDER, code);
            }
            
            if( parsingAttribute != null ) {           
            	
			    ParsingAttribute pa = ParsingAttribute.valueOf(parsingAttribute);
			    
			    if( pa == ParsingAttribute.JavaCompilationUnit ){
			    	try {

		            	System.out.println(code);
		            	System.out.println("*** " + parsingAttribute + "***");
			    		String output = DefUse.analyze(AstUtil.createCompilationUnit(code));
			    		output = output.replace("\n", "<br>");
			    		htmlString = htmlString.replace(DEF_USE_PLACEHOLDER, output);
			    	} catch( CoreException e ) {
			    		
			    	}
			    } else {
			    	htmlString = htmlString.replace(DEF_USE_PLACEHOLDER, "Arguments good");
			    }            	
            }

            out.print(htmlString);
			out.close();				
		} 
					

		long diff = (System.currentTimeMillis() - time)/1000;
		System.out.println("page loaded in " + diff + " s" );
	}
	
    public static File getWebFileFromBundle(String path) {
    	if ( DefUseEclipseServerApplication.TEST ) {
			URL url = Platform.getBundle("DefUseDisplayer").getEntry(path);
		    	
			File file = null;
			if( url != null ) {
				try { 
					URL resolvedUrl = FileLocator.resolve(url);			
					file = new File(resolvedUrl.toURI());
				} catch( IOException e) {
					e.printStackTrace();
				} catch ( URISyntaxException e) {
					e.printStackTrace();
				}
			}
			return file;
    	} else {
    		return new File(path);
    	}
    }
    
    private static String getParsingAttributesHtml() {
        String html = "";
        for( ParsingAttribute a : ParsingAttribute.values() ) {
        	if( a == ParsingAttribute.JavaCompilationUnit ) {
        		html += PARSING_ATTRIBUTE_ROW.replace(PARSING_ATTRIBUTE_PLACEHOLDER, a.name());
        	}
        }
        return html;
    }

}

