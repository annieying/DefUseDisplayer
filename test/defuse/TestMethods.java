package defuse;

import java.util.Collection;
import org.junit.Assert;
import org.junit.Test;
import defuse.server.ParsingAttribute;
import defuse.server.Strategy;

public class TestMethods {

	String code = "void method() { int foo; }";
	ParsingAttribute parsing = ParsingAttribute.JavaClassBodyMemberDeclaration;
	@Test
	public void testPaddingPPA() throws Exception {
		Collection<VariableDef> defs = DefUseAnalyzer.analyzeReturnList(code, Strategy.ppa, parsing);
		
		String var = "foo";
		int index = code.indexOf("foo");
		VariableDef a = VariableDef.getDef(defs, var, index, index + var.length());

		Assert.assertNotNull(a);
	}
}
