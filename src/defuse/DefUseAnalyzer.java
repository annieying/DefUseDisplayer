package defuse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IVariableBinding;

import defuse.server.Strategy;

public class DefUseAnalyzer {

    public static String analyze(String code, Strategy strategy) {
    	System.out.println("DefUse.analyze");

    	String message = "";
		try {
			
			ASTNode ast = getCompilationUnit(code, strategy);			
			Collection<VariableDef> defs = analyzeReturnList(ast);			
			message = outputMessagesForListOfDefs(defs);
		} catch (CoreException e) {
			e.printStackTrace();
		}
   
        return message;
    }

    public static String analyze(ICompilationUnit unit) {
    	System.out.println("DefUse.analyze");

    	String message = "";
			
		ASTNode ast = AstUtil.getEclipseAst(unit);			
		Collection<VariableDef> defs = analyzeReturnList(ast);			
		message = outputMessagesForListOfDefs(defs);
   
        return message;
    }
    

    public static Collection<VariableDef> analyzeReturnList(String code, Strategy strategy) {
    	try { 
    		ASTNode ast = getCompilationUnit(code, strategy);
    		return analyzeReturnList(ast);
    	} catch(CoreException e) {
    		e.printStackTrace();
    	}
    	return Collections.EMPTY_LIST;
    }
    
    
    public static Collection<VariableDef> analyzeReturnList(ASTNode ast) {
    	System.out.println("DefUse.analyze");

        DefUseVisitor printer = new DefUseVisitor();
    	try {
	        ast.accept(printer);
    	} catch(Throwable e ) {
    		e.printStackTrace();
    	}
        
        System.out.println("Visited AST");
        
        Collection<VariableDef> defs = new ArrayList<VariableDef>();
        defs.addAll(printer.getVarBindings().values());
//        defs.addAll(printer.getParameterBindings());
        
	    return defs;
    }

    public static ASTNode getCompilationUnit(String code, Strategy strategy) 
    	throws CoreException {
    	ASTNode ast = null;
    	if( strategy == Strategy.eclipse) {
    		ICompilationUnit cu = AstUtil.createCompilationUnit(code);
    		ast = AstUtil.getEclipseAst(cu);
    	} else if ( strategy == Strategy.ppa) {
    		ast = AstUtil.getPpaAst(code);
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
    
