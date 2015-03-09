package defuse;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IVariableBinding;

public class DefUseAnalyzer {
	
    public static String analyze(ICompilationUnit unit) {
    	System.out.println("DefUse.analyze");

        Collection<VariableDef> defs = analyzeReturnList(unit);
        
        String message = outputMessagesForListOfDefs(defs);
   
        return message;
    }
    
    public static Collection<VariableDef> analyzeReturnList(ICompilationUnit unit) {
    	System.out.println("DefUse.analyze");

        DefUseVisitor printer = new DefUseVisitor();
    	try {
            ASTNode ast = AstUtil.getEclipseAst(unit);
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

    
    public static String outputMessagesForListOfDefs(Collection<VariableDef> defs) {
    	String json = VariableDef.toJson(defs);
    	return json;
    }
            
//    public static String outputMessagesForParameters(Collection<VariableDef> bindings) {
//    	
//    	List<String> messages = new ArrayList<String>();
//        for( VariableDef e : bindings ) {
//
//            String message = outputMessage(e, "Parameter");
//            messages.add(message);
//        } 
//        
//        Collections.sort(messages);
//        
//        String finalMessage = "";
//        for(String m : messages ) {
//        	finalMessage += m;
//        }
//        		
//        return finalMessage;
//    }
//    
//    public static String outputMessagesForBindings(Map<IVariableBinding,VariableDef> bindings) {
//    	
//    	List<String> messages = new ArrayList<String>();
//        for( Entry<IVariableBinding, VariableDef> e : bindings.entrySet()) {
//
//            VariableDef defUse = e.getValue();
//                        
//            String typeOfVar = getTypeOfVariable(e.getKey()).toUpperCase();
//            String message = outputMessage(defUse, typeOfVar);            
//            messages.add(message);
//        } 
//        
//        Collections.sort(messages);
//        
//        String finalMessage = "";
//        for(String m : messages ) {
//        	finalMessage += m;
//        }
//        		
//        return finalMessage;
//    }
//    
//    public static String outputMessage(VariableDef def, String typeOfVar) {
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
//       
//         
//         return message;
//    }
    
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
    
