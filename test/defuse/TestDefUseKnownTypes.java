package defuse;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import defuse.server.ParsingAttribute;
import defuse.server.Strategy;

public class TestDefUseKnownTypes {
	ParsingAttribute parsing = ParsingAttribute.JavaCompilationUnit;
	@Test
	public void testParameter() throws Exception {
		String code = "public class foo { void bar(String a) {} }";
		Collection<VariableDef> defs = DefUseAnalyzer.analyzeReturnList(code, Strategy.eclipse, parsing);
		
		for( VariableDef def : defs) {
			Assert.assertEquals("a", def.getName());
		}
	}
		
	
	@Test
	public void testLocal() throws Exception {
		String code = "public class foo { void bar() {String a = 1;} }";
		Collection<VariableDef> defs = DefUseAnalyzer.analyzeReturnList(code, Strategy.eclipse, parsing);
		
		for( VariableDef def : defs) {
			System.out.println("test: " + def.getName());
		}
	}
	
	@Test
	public void testCatch() throws Exception {
		String code = "public class foo { void bar() { try{ } catch(Exception a) { }} }";
		Collection<VariableDef> defs = DefUseAnalyzer.analyzeReturnList(code, Strategy.eclipse, parsing);
		
		for( VariableDef def : defs) {
			System.out.println("test: " + def.getName());
		}
	}
	
}
