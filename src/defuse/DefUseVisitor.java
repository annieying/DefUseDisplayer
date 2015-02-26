package defuse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class DefUseVisitor extends ASTVisitor {
    
    Map<IVariableBinding,VariableDefUse> varBindings = new HashMap<IVariableBinding,VariableDefUse>();
    List<VariableDefUse> parameterBindings = new ArrayList<VariableDefUse>();
    
    public Map<IVariableBinding,VariableDefUse> getVarBindings() {
        return varBindings;
    }
    
    public List<VariableDefUse> getParameterBindings() {
        return parameterBindings;
    }    
    
    ////////////////////////// variable declaration ///////////////////////////////////
    
    /**
     * Single variable declaration nodes are used in a limited number of places, 
     * including formal parameter lists and catch clauses. They are not used for 
     * field declarations and regular variable declaration statements.
     * @param method
     * @return
     */
    @Override
    public boolean visit(MethodDeclaration  method) {
    	List<SingleVariableDeclaration> parameters = method.parameters();
    	int i = 0;
    	for( SingleVariableDeclaration var : parameters  ){
    	
    		IVariableBinding binding = var.resolveBinding();
    		SimpleName varName = var.getName();

    		String typeName = var.getType().toString();
    		int variableId = i;
    		
			VariableDefUse defUse = new VariableDefUse(varName, 	variableId, typeName);
			parameterBindings.add(defUse);   	              
			System.out.println("Set usage of var " + variableId);	              

    		defUse.setUses(varName); 	          

    		i+=1;
    	}
    	return true;
    }
    
    @Override
    public boolean visit(SimpleName varName) {

      IBinding binding = varName.resolveBinding();
      
      if( binding != null) {
	      if(binding instanceof IVariableBinding) {

	    	  IVariableBinding varBinding = (IVariableBinding) binding;
        	  int variableId = varBinding.getVariableId();
        	  
        	  VariableDefUse defUse = varBindings.get(varBinding);
	          if (defUse == null ) {
	              defUse = new VariableDefUse(varName,
	                      variableId, varBinding.getType().getName());
	              varBindings.put(varBinding, defUse);   	              
	              System.out.println("Set usage of var " + variableId);	              
	          } 
	          
              defUse.setUses(varName);     
	          
	      }   
      }
      
      return true;
    }
        
    /**
     * Variable declaration fragment AST node type, used in field declarations, 
     * local variable declarations, and ForStatement initializers. It contrast 
     * to SingleVariableDeclaration, fragments are missing the modifiers and the 
     * type; these are located in the fragment's parent node.
     */
    @Override
    public boolean visit(VariableDeclarationFragment fragment) {
  	  
//      List<VariableDeclarationFragment> fragments = node.fragments();
//      for (Iterator iter = node.fragments().iterator(); iter.hasNext();) {
//          VariableDeclarationFragment fragment = (VariableDeclarationFragment) iter.next();

          IVariableBinding binding = fragment.resolveBinding();
          if( binding != null ) {
              int variableId = binding.getVariableId();
              SimpleName varName = fragment.getName();
              
              VariableDefUse defUse = new VariableDefUse(varName,
	                      variableId, binding.getType().getName());
	          varBindings.put(binding, defUse);

	          System.out.println("Set declaration var " + variableId);
  
	          defUse.setDef(fragment.getName());              
          }

     
  	  return true;
    }
        
    @Override
    public boolean visit(FieldDeclaration node) {
	  List<VariableDeclarationFragment> fragments = node.fragments();
//	  if( fragments.size()>0) {
//		  new Exception("new variable delcaration fragments than expected");
//	  }
	  
	  // TODO handle multi variable declaration
	  
	  SimpleName varName = fragments.iterator().next().getName();
  
	  IVariableBinding binding = fragments.iterator().next().resolveBinding();
	  if( binding != null ) {
            int variableId = binding.getVariableId();
            VariableDefUse defUse = new VariableDefUse(varName,
                    variableId+1000, binding.getType().getName());
            varBindings.put(binding, defUse);        
            defUse.setDef(varName);
            
            System.out.println("Set declaration field " + variableId);	        
	  }			    	  
  	  
  	  return true;		    	  
    }
    
    
}
