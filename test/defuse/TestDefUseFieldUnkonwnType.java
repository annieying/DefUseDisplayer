package defuse;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import defuse.server.Strategy;

public class TestDefUseFieldUnkonwnType {				

	String code = "public class foo { Preference a = null;  void bar() { a.printPreference();} }";
	
	@Test
	public void testFieldPpa() throws Exception {
		Collection<VariableDef> defs = DefUseAnalyzer.analyzeReturnList(code, Strategy.ppa);
		
		VariableDef a = VariableDef.getDef(defs, "a", 30, 31);
		
		Assert.assertEquals(2, a.getUses().size());	
	}
	
	@Test
	public void testFieldEclipse() throws Exception {
		Collection<VariableDef> defs = DefUseAnalyzer.analyzeReturnList(code, Strategy.eclipse);
				
		Assert.assertEquals(0, defs.size());
		
		
	}
	
}
