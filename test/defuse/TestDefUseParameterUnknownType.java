package defuse;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import defuse.server.Strategy;

public class TestDefUseParameterUnknownType {

	String code = "public class foo { void baz(Preference b) { b = null;} void bar(Preference a) { }}";
	
	@Test
	public void testParameterPPA() throws Exception {
		Collection<VariableDef> defs = DefUseAnalyzer.analyzeReturnList(code, Strategy.ppa);
		
		VariableDef a = VariableDef.getDef(defs, "a", 75, 76);
		VariableDef b = VariableDef.getDef(defs, "b", 39, 40);		

		Assert.assertNotNull(a);
		Assert.assertNotNull(b);
		
		Assert.assertEquals(2, b.getUses().size());
		Assert.assertEquals(1, a.getUses().size());
	}

	
	@Test
	public void testParameterEclipse() throws Exception {
		Collection<VariableDef> defs = DefUseAnalyzer.analyzeReturnList(code, Strategy.eclipse);
		
		Assert.assertEquals(0, defs.size());
	}	
}
