package defuse.gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import defuse.VariableDef;
import defuse.VariableUse;

public class GsonTest {
	
	static String targetCompact = "[{\"def\":{\"name\":\"a\",\"type\":\"int\",\"nodeType\":\"\",\"charStart\":1,\"charEnd\":3,\"parent\":\"int a=1;\"},\"uses\":[{\"name\":\"a\",\"type\":\"int\",\"nodeType\":\"\",\"charStart\":6,\"charEnd\":9,\"parent\":\"b=a+a;\"}]}]";

			
	@Test
	public void testToJson() {
		
		VariableUse use = new VariableUse("a", "","int", "b=a+a;", 6,9);
		VariableDef def = new VariableDef("a", "", "int" , "int a=1;", 1,3);
		def.setUses(use);
		List<VariableDef> defs = new ArrayList<VariableDef>();
		defs.add(def);
		
		String json = VariableDef.toJson(defs);
		String jsonCompact = json.replace("\n", "").replace(" ", "");
		
		Assert.assertEquals(targetCompact.replace(" ", ""), jsonCompact);		
	}
	
	@Test 
	public void testFromJson() {
		List<VariableDef> defs = VariableDef.fromJson(targetCompact);
		
		VariableDef def = defs.get(0);
		
		Assert.assertEquals("a", def.getName());
		Assert.assertEquals("int", def.getType());
		Assert.assertEquals("int a=1;", def.getParent());
		
		Set<VariableUse> uses = def.getUses();
		Assert.assertEquals(1, uses.size());
		
	}
}
