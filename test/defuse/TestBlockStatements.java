package defuse;

import java.util.Collection;
import org.junit.Assert;
import org.junit.Test;
import defuse.server.ParsingAttribute;
import defuse.server.Strategy;

public class TestBlockStatements {

	String code = "int foo;";
	ParsingAttribute parsing = ParsingAttribute.JavaBlockStatements;
	@Test
	public void testPaddingPPA() throws Exception {
		Collection<VariableDef> defs = DefUseAnalyzer.analyzeReturnList(code, Strategy.ppa, parsing);
		
		String var = "foo";
		int index = code.indexOf("foo");
		VariableDef a = VariableDef.getDef(defs, var, index, index + var.length());

		Assert.assertNotNull(a);
	}
}
