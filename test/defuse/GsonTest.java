package defuse;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class GsonTest {
	
	static String targetCompact = "{\"def\":{\"name\":\"a\",\"type\":\"int\",\"variableId\":2,\"charStart\":1,\"charEnd\":3,\"parent\":\"int a=1;\"},\"uses\":[{\"name\":\"a\",\"type\":\"int\",\"variableId\":1,\"charStart\":6,\"charEnd\":9,\"parent\":\"b=a+a;\"}]}";
			
	@Test
	public void testToJson() {
		
		VariableUse use = new VariableUse("a",1,"int", "b=a+a;", 6,9);
		VariableDef def = new VariableDef("a", 2, "int" , "int a=1;", 1,3);
		def.setUses(use);
		
		String json = VariableDef.toJson(def);
		String jsonCompact = json.replace("\n", "").replace(" ", "");
		
		Assert.assertEquals(targetCompact.replace(" ", ""), jsonCompact);		
	}
	
	@Test 
	public void testFromJson() {
		VariableDef def = VariableDef.fromJson(targetCompact);
		
		Assert.assertEquals("a", def.getName());
		Assert.assertEquals("int", def.getType());
		Assert.assertEquals(2, def.getVariableId());
		Assert.assertEquals("int a=1;", def.getParent());
		
		Set<VariableUse> uses = def.getUses();
		Assert.assertEquals(1, uses.size());
		
	}
}
