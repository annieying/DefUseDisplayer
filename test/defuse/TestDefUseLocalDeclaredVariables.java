package defuse;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;

import defuse.server.Strategy;

public class TestDefUseLocalDeclaredVariables {

	String code = "public class foo { void bar() { int number = 4; a.print(); a.setNumber(number);} }";
	
	@Test
	public void testUndeclaredVariablePPA() throws Exception {
		Collection<VariableDef> defs = DefUseAnalyzer.analyzeReturnList(code, Strategy.ppa);
		
		VariableDef a = null;
		for( VariableDef def : defs ) {
			if( def.getName().equals("a") ) {
				a = def;
				break;
			}
		}
		
		Assert.assertNull(a);		
	}
	
	@Test
	public void testUndeclaredVariableEclipse() throws Exception {
		Collection<VariableDef> defs = DefUseAnalyzer.analyzeReturnList(code, Strategy.ppa);
		
		VariableDef a = null;
		for( VariableDef def : defs ) {
			if( def.getName().equals("a") ) {
				a = def;
				break;
			}
		}
		
		Assert.assertNull(a);	
	}
	
}
