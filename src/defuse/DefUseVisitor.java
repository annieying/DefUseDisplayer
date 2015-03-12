package defuse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class DefUseVisitor extends ASTVisitor {

	Map<IVariableBinding,VariableDef> varBindings = new HashMap<IVariableBinding,VariableDef>();
	List<VariableUse> otherSymbols = new ArrayList<VariableUse>();

	public Map<IVariableBinding,VariableDef> getVarBindings() {
		return varBindings;
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
	public boolean visit(SingleVariableDeclaration  var) {
		//    	List<SingleVariableDeclaration> parameters = method.parameters();
		//    	int i = 0;
		//    	for( SingleVariableDeclaration var : parameters  ){

		IVariableBinding binding = var.resolveBinding();
		// catching parameters that are non-primitive type: 
		// public void foo(IApplicationContext context)
		if( binding != null ) {
			SimpleName varName = var.getName();

			String typeName = var.getType().toString();
			String parent = varName.getParent().toString();
			int charStart = varName.getStartPosition();
			int charEnd = charStart + varName.getLength();
			String name = varName.getIdentifier();
			String nodeType = var.getClass().toString();

			VariableDef defUse = new VariableDef(name, nodeType, typeName, parent,
					charStart, charEnd);
			varBindings.put(binding, defUse);   	              
			System.out.println("Set usage of var " + name);	              
		}

		return true;
	}

	@Override
	public boolean visit(SimpleName varName) {

		IBinding binding = varName.resolveBinding();    
		IVariableBinding varBinding = null;
		VariableDef defUse = null;

		if( binding != null ) {
			if( binding instanceof IVariableBinding ) {
				varBinding = (IVariableBinding)binding;
				defUse = varBindings.get(varBinding);
				String type = null;
				String parent = varName.getParent().toString();     
				String name = varName.getIdentifier();
				int charStart = varName.getStartPosition();
				int charEnd = charStart + varName.getLength();
				String nodeType = varName.getClass().toString();

				if (defUse == null ) {	              
					defUse = new VariableDef(name, nodeType, type, parent,
							charStart, charEnd);

					varBindings.put(varBinding, defUse);
				} else {
					if( defUse.getCharStart() == charStart && 
							defUse.getCharEnd() == charEnd ) {
						// don't add a use if it's the same instance of the variable defined
					} else {
						defUse.setUses(new VariableUse(name, nodeType, type, parent,
								charStart, charEnd));
					}
				}

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
	public boolean visit(VariableDeclarationStatement statement) {

		List<VariableDeclarationFragment> fragments = statement.fragments();
		for (Iterator iter = statement.fragments().iterator(); iter.hasNext();) {    	  

			VariableDeclarationFragment fragment = (VariableDeclarationFragment) iter.next();

			IVariableBinding binding = fragment.resolveBinding();      
			if( binding != null ) {
				SimpleName varName = fragment.getName();
				String name = varName.getIdentifier();
				int charStart = varName.getStartPosition();
				int charEnd = charStart + varName.getLength();
				String parent = fragment.toString();
				String nodeType = statement.getClass().toString();

				String type = binding.getType().getName();           

				VariableDef defUse = new VariableDef(name, nodeType, type, parent, charStart, charEnd);
				varBindings.put(binding, defUse);

				System.out.println("Set declaration var " + varName);
			}
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

			int charStart = varName.getStartPosition();
			int charEnd = charStart + varName.getLength();
			String name = varName.getIdentifier();
			String parent = varName.getParent().toString();
			String type = binding.getType().getName();
			String nodeType = node.getClass().toString();

			VariableDef defUse = new VariableDef(name, nodeType, type,
					parent, charStart, charEnd);
			varBindings.put(binding, defUse);

			System.out.println("Set declaration field " + variableId);	        
		}			    	  

		return true;		    	  
	}    
}
