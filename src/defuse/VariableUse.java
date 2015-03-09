package defuse;


public class VariableUse {
    private String name;
    private String type;
    private String nodeType;
    private int charStart;
    private int charEnd;   
    private String parent;
    
    public VariableUse( String name, String nodeType, String type,
    		String parent,
    		int charStart, int charEnd ) {
        this.name=name;
        this.type = type;
        this.nodeType = nodeType;
        this.charStart = charStart;
        this.charEnd = charEnd;
        this.parent = parent.trim();
    }
    
    public String getName() {
        return name;
    }
        
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }

    public String getNodeType()
    {
        return nodeType;
    }
    
    public int getCharStart() {
    	return charStart;
    }
    
    public int getCharEnd() {
    	return charEnd;
    }
    
    public String getParent() {
    	return parent;
    }
    
    @Override
    public String toString() {
    	return name + " as in (" + parent + ")"; 
    }
}