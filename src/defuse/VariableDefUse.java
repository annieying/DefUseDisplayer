package defuse;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.SimpleName;

public class VariableDefUse {
    private SimpleName def;
    private Set<SimpleName> uses = new HashSet<SimpleName>();
    private String type;
    private int variableId;
   
    
    public VariableDefUse( SimpleName def, int variableId, String type ) {
        this.def=def;
        this.type = type;
        this.variableId = variableId;
    }
    
    public SimpleName getDef() {
        return def;
    }
    
    public Set<SimpleName> getUses() {
        return uses;
    }    
    
    public void setDef(SimpleName def) {
        this.def = def;
    }
    
    public void setUses(SimpleName use) {
        if( !use.equals(def)) {
            this.uses.add(use);
        }
    }

    public String getType()
    {
        return type;
    }

    public int getVariableId()
    {
        return variableId;
    }    
}