package defuse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class VariableDef {
    private VariableUse def;
    private Set<VariableUse> uses = new HashSet<VariableUse>();
    
    public VariableDef( String name, String nodeType, String type, String parent,
    		int charStart, int charEnd ) {
        this.def=new VariableUse(name,nodeType,type,parent,charStart,charEnd);
    }
    
	public static String toJson(Collection<VariableDef> defs) {
		Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.create(); 
		String json = gson.toJson(defs);
		
		return json;
	}
	
	public static String toJson(List<VariableDef> defs) {
		Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.create(); 
		String json = gson.toJson(defs);
		
		return json;
	}
	
	public static List<VariableDef> fromJson(String json) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		
		Type listType = new TypeToken<ArrayList<VariableDef>>() {}.getType();
		
		List<VariableDef> defs = gson.fromJson(json, listType);	    
	    return defs;
	}
    
    public String getName() {
        return def.getName();
    }
    
    public Set<VariableUse> getUses() {
        return uses;
    }    
    
    public void setDef(VariableUse def) {
    	this.def = def;
    }
    
    public void setUses(VariableUse use) {
        if( !use.equals(def)) {
            this.uses.add(use);
        }
    }

    public String getType()  {
        return def.getType();
    }

    public String getNodeType() {
        return def.getNodeType();
    }
    
    public int getCharStart() {
    	return def.getCharStart();
    }
    
    public int getCharEnd() {
    	return def.getCharEnd();
    }
    
    public String getParent() {
    	return def.getParent();
    }
    
    @Override
    public String toString() {
    	return "Def: " + def + "\n" + "Uses: " + uses;
    }
}