package defuse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IVariableBinding;

import ca.mcgill.cs.swevo.ppa.ui.PPAUtil;
import defuse.server.ParsingAttribute;
import defuse.server.Strategy;

public class DefUseAnalyzer {

    public static String analyze(String code, Strategy strategy, ParsingAttribute parsing) {

    	String message = "";
		try {
			
			ASTNode ast = getAstNode(code, strategy, parsing);			
			Collection<VariableDef> defs = analyzeReturnList(ast);			
			message = outputMessagesForListOfDefs(defs);
			
			if( strategy == Strategy.ppa) {
				PPAUtil.cleanUpAll();
			}
			
		} catch (CoreException e) {
			e.printStackTrace();
		}
   
        return message;
    }

    public static String analyze(ICompilationUnit unit) {
    	String message = "";
			
		ASTNode ast = AstUtil.getEclipseAst(unit);			
		Collection<VariableDef> defs = analyzeReturnList(ast);			
		message = outputMessagesForListOfDefs(defs);
   
        return message;
    }
    

    public static Collection<VariableDef> analyzeReturnList(String code, Strategy strategy, ParsingAttribute parsing) {
    	try { 
    		ASTNode ast = getAstNode(code, strategy, parsing);
    		return analyzeReturnList(ast);
    	} catch(CoreException e) {
    		e.printStackTrace();
    	}
    	return Collections.EMPTY_LIST;
    }
    
    
    public static Collection<VariableDef> analyzeReturnList(ASTNode ast) {

        DefUseVisitor printer = new DefUseVisitor();
    	try {
	        ast.accept(printer);
    	} catch(Throwable e ) {
    		e.printStackTrace();
    	}
                
        Collection<VariableDef> defs = new ArrayList<VariableDef>();
        defs.addAll(printer.getVarBindings().values());
//        defs.addAll(printer.getParameterBindings());
        
	    return defs;
    }

    public static ASTNode getAstNode(String code, Strategy strategy, ParsingAttribute parsing) 
    	throws CoreException {
    	ASTNode ast = null;
    	if( strategy == Strategy.eclipse) {
    		ICompilationUnit cu = AstUtil.createCompilationUnit(code);
    		ast = AstUtil.getEclipseAst(cu);
    	} else if ( strategy == Strategy.ppa) {
    		if( parsing == ParsingAttribute.JavaCompilationUnit) {
    			ast = AstUtil.getCompilationUnitUsingPPA(code);
    		} else if (parsing == ParsingAttribute.JavaClassBodyMemberDeclaration ) {
    			boolean isTypeBody = true;
    			ast = AstUtil.getASTUsingPPA(code, isTypeBody);
    		} else {
    			boolean isTypeBody = false;
    			ast = AstUtil.getASTUsingPPA(code, isTypeBody);    			
    		}
    	}
    	return ast;
    }
    
    public static String outputMessagesForListOfDefs(Collection<VariableDef> defs) {
    	String json = VariableDef.toJson(defs);
    	return json;
    }
    
    public static String getTypeOfVariable(IVariableBinding binding) {
    	if ( binding.isField() ) {
    		return "Feild";
    	} else if( binding.isParameter() ) {
    		return "Parameter";
    	} else {
    		return "Variable";
    	}
    }

}
    
