package defuse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;

public class DefUse {
    
    public static String analyze(ICompilationUnit unit) {
    	System.out.println("DefUse.analyze");

        DefUseVisitor printer = new DefUseVisitor();
    	try {
            ASTNode ast = AstUtil.getEclipseAst(unit);
	        ast.accept(printer);
    	} catch(Throwable e ) {
    		e.printStackTrace();
    	}
        
        System.out.println("Visited AST");
        
        String message = "";
        
        message += outputMessages(printer.getVarBindings());
        message += outputMessages(printer.getParameterBindings());
   
        return message;
    }
        
    public static String outputMessages(List<VariableDefUse> bindings) {
    	
    	List<String> messages = new ArrayList<String>();
        for( VariableDefUse e : bindings ) {

            VariableDefUse defUse = e;
            String message = outputMessage(e, "Parameter");
            messages.add(message);
        } 
        
        Collections.sort(messages);
        
        String finalMessage = "";
        for(String m : messages ) {
        	finalMessage += m;
        }
        		
        return finalMessage;
    }
    
    public static String outputMessages(Map<IVariableBinding,VariableDefUse> bindings) {
    	
    	List<String> messages = new ArrayList<String>();
        for( Entry<IVariableBinding, VariableDefUse> e : bindings.entrySet()) {

            VariableDefUse defUse = e.getValue();
                        
            String typeOfVar = getTypeOfVariable(e.getKey()).toUpperCase();
            String message = outputMessage(defUse, typeOfVar);            
            messages.add(message);
        } 
        
        Collections.sort(messages);
        
        String finalMessage = "";
        for(String m : messages ) {
        	finalMessage += m;
        }
        		
        return finalMessage;
    }
    
    public static String outputMessage(VariableDefUse defUse, String typeOfVar) {
    	 int variableId = defUse.getVariableId();
         String typeName = defUse.getType();            
         String varName = defUse.getDef().toString();
         String parentName = defUse.getDef().getParent().toString().trim();
                  
         String message = typeOfVar + " #" + variableId 
         		+ " '" + varName + "' " 
         		+ " of type " + typeName + " ";
         
         message += varName.equals(parentName) ? 
         		"\n" : 
         		" (as in \"" + parentName +"\") " 
         			+ " [" + defUse.getCharStart() + "," + defUse.getCharEnd() + "] " + "\n";            
         
         for( SimpleName ref : defUse.getUses() ) {
         	String refParentName = ref.getParent().toString().trim();            	
             message += "  * used in \"" + refParentName + "\"\n";

         }
         message += "\n";
         return message;
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
    
