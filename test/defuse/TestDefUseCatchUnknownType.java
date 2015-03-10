package defuse;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import defuse.server.Strategy;

public class TestDefUseCatchUnknownType {
				

	String code = "public class foo { void bar() { try{ } catch(WhateverException a) { a.printStackTrace();}} }";
	
	@Test
	public void testCatchPpa() throws Exception {
		Collection<VariableDef> defs = DefUseAnalyzer.analyzeReturnList(code, Strategy.ppa);
		
		VariableDef a = VariableDef.getDef(defs, "a", 63, 64);
		
		Assert.assertEquals(2, a.getUses().size());	
	}
	
	@Test
	public void testCatchEclipse() throws Exception {
		Collection<VariableDef> defs = DefUseAnalyzer.analyzeReturnList(code, Strategy.eclipse);
				
		Assert.assertEquals(0, defs.size());
		
		
	}
	
}
