package defuse;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import defuse.server.ParsingAttribute;
import defuse.server.Strategy;

public class TestDefUseCatchUnknownType {
				

	String code = "public class foo { void bar() { try{ } catch(WhateverException a) { a.printStackTrace();}} }";
	ParsingAttribute parsing = ParsingAttribute.JavaCompilationUnit;
	
	@Test
	public void testCatchPpa() throws Exception {
		Collection<VariableDef> defs = DefUseAnalyzer.analyzeReturnList(code, Strategy.ppa, parsing);
		
		VariableDef a = VariableDef.getDef(defs, "a", 63, 64);
		
		Assert.assertEquals(1, a.getUses().size());	
	}
	
	@Test
	public void testCatchEclipse() throws Exception {
		Collection<VariableDef> defs = DefUseAnalyzer.analyzeReturnList(code, Strategy.eclipse, parsing);
				
		Assert.assertEquals(0, defs.size());
		
		
	}
	
}
