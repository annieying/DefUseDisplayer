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
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;

import defuse.DefUseAnalyzer;

public class DefUseEclipseSerlvet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String FORMATTED_CODE_FILENAME = "./res/html/def-use-index.html";
    private static final String UNFORMATTED_CODE_PLACEHOLDER = "<!--***CODE***-->";

    private static final String DEF_USE_PLACEHOLDER = "<!--***DEF-USE OUTPUT***-->";
    
	private static final String PARSING_ATTRIBUTE_PLACEHOLDER = "***PARSING_ATTRIBUTE***";
	private static final String PARSING_ATTRIBUTE_ROW = "<input type=\"radio\" name=\"parsing-attribute\" " 
			+ "value=\"" + PARSING_ATTRIBUTE_PLACEHOLDER + "\">" 
			+ PARSING_ATTRIBUTE_PLACEHOLDER + "<br>";
	private static final String PARSING_HTML_PLACEHOLDER = "<!--***PARSING_ATTRIBUTES***-->";
	
	enum ParsingAttribute {
		JavaClassBodyMemberDeclaration,
		JavaCompilationUnit,
		JavaBlockStatements,
		JavaSwitchBlockStatementGroup,
		JavaMethodDeclaration
	}
	
	enum Format {
		ui,
		json
	}
	
    public void doGet(HttpServletRequest aRequest, HttpServletResponse aResponse) 
            throws ServletException, IOException {
    	process(aRequest, aResponse);
    }
	
	public void doPost(HttpServletRequest aRequest, HttpServletResponse aResponse)
			throws ServletException, IOException {		
		process(aRequest, aResponse);
	}
	
	public void process(HttpServletRequest aRequest, HttpServletResponse aResponse) 
			throws ServletException, IOException {
		String code =  aRequest.getParameter("code"); 
		
		String parsingAttributeString = aRequest.getParameter("parsing-attribute");
		String formatString = aRequest.getParameter("format");
		String strategyString = aRequest.getParameter("strategy");
		
		ParsingAttribute parsingAttribute = parsingAttributeString == null ? 
				ParsingAttribute.JavaCompilationUnit : ParsingAttribute.valueOf(parsingAttributeString);
		Format format = formatString==null ? Format.ui : Format.valueOf(formatString);
		Strategy strategy = strategyString==null ? Strategy.eclipse : Strategy.valueOf(strategyString);
			
	    process(aRequest, aResponse, code, parsingAttribute, format, strategy);
	
	}
	
	public void process(HttpServletRequest aRequest, HttpServletResponse aResponse,
			String code, ParsingAttribute parsingAttribute, Format format,
			Strategy strategy) throws ServletException, IOException {

		String path = aRequest.getPathInfo();
		long time = System.currentTimeMillis();		
		
		String htmlString = "";
        String jsonOutput = "";
		
		if( path.equals("/DefUse") ) {			
					
            aResponse.setStatus(HttpServletResponse.SC_CREATED);
            ServletOutputStream out = aResponse.getOutputStream();
      	         	            	
            jsonOutput = getResult(code, strategy);
            
			if( format == Format.json ) {
				aResponse.setContentType("application/json");
				htmlString = jsonOutput;
			} else {
				aResponse.setContentType("text/html");
				jsonOutput = jsonOutput.replace("\n", "<br>");
	            htmlString = getHtmlString(code, parsingAttribute).
	            		replace(DEF_USE_PLACEHOLDER, jsonOutput);
			}            
            
            out.print(htmlString);
			out.close();				
		} 
					

		long diff = (System.currentTimeMillis() - time)/1000;
		System.out.println("page loaded in " + diff + " s" );
	}
	
	private static String getHtmlString(String code, ParsingAttribute parsingAttribute)
			throws IOException {
		
        String htmlString = FileUtils.readFileToString(
        		getWebFileFromBundle(FORMATTED_CODE_FILENAME));            
        htmlString = htmlString.replace(PARSING_HTML_PLACEHOLDER, getParsingAttributesHtml());    
                    
        if( code == null ) {
        	htmlString = htmlString.replace(UNFORMATTED_CODE_PLACEHOLDER, "");
        } else {            	
        	htmlString = htmlString.replace(UNFORMATTED_CODE_PLACEHOLDER, code);
        }
                        			    
	    if( parsingAttribute == ParsingAttribute.JavaCompilationUnit ){		    		
        	System.out.println(code);
        	System.out.println("*** " + parsingAttribute + "***");    		

	    } else {
	    	htmlString = htmlString.replace(DEF_USE_PLACEHOLDER, "Choose parsing attributes");
	    }         
	    
	    return htmlString;
	}
	
	private static String getResult(String code, Strategy strategy) {
		String output = "Result:\n\n"; 
		output += "<pre>\n";
		output += code == null ? "" : DefUseAnalyzer.analyze(code, strategy);
		output = output.replace("\n", "<br>");
		output += "</pre>\n";
		return output;
		
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

