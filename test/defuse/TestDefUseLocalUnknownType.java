package defuse;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;

import defuse.server.ParsingAttribute;
import defuse.server.Strategy;

public class TestDefUseLocalUnknownType {

	String code = "public class foo { void bar() {Preference a = null;} void baz() { Preference a = null; a.printPreference();}}";
	ParsingAttribute parsing = ParsingAttribute.JavaCompilationUnit;
	@Test
	public void testLocal() throws Exception {
		Collection<VariableDef> defs = DefUseAnalyzer.analyzeReturnList(code, Strategy.ppa, parsing);
		
		VariableDef a1 = VariableDef.getDef(defs, "a", 42, 43);
		VariableDef a2 = VariableDef.getDef(defs, "a", 77, 78);
		
		Assert.assertEquals(1, a1.getUses().size());
		Assert.assertEquals(2, a2.getUses().size());
		
	}
	
	@Test
	public void testLocalEclipse() throws Exception {
		Collection<VariableDef> defs = DefUseAnalyzer.analyzeReturnList(code, Strategy.eclipse, parsing);
		
		Assert.assertEquals(0, defs.size());
	}
	
}
