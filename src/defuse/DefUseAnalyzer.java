package defuse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IVariableBinding;

public class DefUseAnalyzer {
    
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
        
    public static String outputMessages(List<VariableDef> bindings) {
    	
    	List<String> messages = new ArrayList<String>();
        for( VariableDef e : bindings ) {

            VariableDef defUse = e;
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
    
    public static String outputMessages(Map<IVariableBinding,VariableDef> bindings) {
    	
    	List<String> messages = new ArrayList<String>();
        for( Entry<IVariableBinding, VariableDef> e : bindings.entrySet()) {

            VariableDef defUse = e.getValue();
                        
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
    
    public static String outputMessage(VariableDef def, String typeOfVar) {
//    	 int variableId = def.getVariableId();
//         String typeName = def.getType();            
//         String varName = def.getName();
//         String parentName = def.getParent();
//                  
//         String message = typeOfVar + " #" + variableId 
//         		+ " '" + varName + "' " 
//         		+ " of type " + typeName + " ";
//         
//         message += varName.equals(parentName) ? 
//         		"\n" : 
//         		" (as in \"" + parentName +"\") " 
//         			+ " [" + def.getCharStart() + "," + def.getCharEnd() + "] " + "\n";            
//         
//         for( VariableUse ref : def.getUses() ) {
//         	String refParentName = ref.getParent();            	
//            message += "  * used in \"" + refParentName + "\"\n";
//
//         }
//         message += "\n";
       
         String message = VariableDef.toJson(def);
         
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
    
