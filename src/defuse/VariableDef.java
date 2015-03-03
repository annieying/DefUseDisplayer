package defuse;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class VariableDef {
    private VariableUse def;
    private Set<VariableUse> uses = new HashSet<VariableUse>();
    
    public VariableDef( String name, int variableId, String type, String parent,
    		int charStart, int charEnd ) {
        this.def=new VariableUse(name,variableId,type,parent,charStart,charEnd);
    }
    
	public static String toJson(VariableDef def) {
		Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.create(); 
		String json = gson.toJson(def);
		
		return json;
	}
	
	public static VariableDef fromJson(String json) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		VariableDef def = gson.fromJson(json, VariableDef.class);	    
	    return def;
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

    public int getVariableId() {
        return def.getVariableId();
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