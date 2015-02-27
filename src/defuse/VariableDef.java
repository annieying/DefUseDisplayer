package defuse;

import java.util.HashSet;
import java.util.Set;

public class VariableDef {
    private VariableUse def;
    private Set<VariableUse> uses = new HashSet<VariableUse>();
    
    public VariableDef( String name, int variableId, String type, String parent,
    		int charStart, int charEnd ) {
        this.def=new VariableUse(name,variableId,type,parent,charStart,charEnd);
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