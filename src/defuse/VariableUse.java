package defuse;

import defuse.server.ParsingAttribute;


public class VariableUse {
    private String name;
    private String type;
    private String nodeType;
    private int charStart;
    private int charEnd;   
    private String parent;
    
    public VariableUse( String name, String nodeType, String type,
    		String parent,
    		int charStart, int charEnd, ParsingAttribute parsing ) {
        this.name=name;
        this.type = type;
        this.nodeType = nodeType;
        this.parent = parent.trim();
        
        int padding = 0;
        if( parsing == null ) padding = 0;
        else if( parsing == ParsingAttribute.JavaCompilationUnit ) padding = 0;
        else if( parsing == ParsingAttribute.JavaBlockStatements ) {
          padding = Offsets.BlockStatementsOffset;
        } else if( parsing == ParsingAttribute.JavaClassBodyMemberDeclaration) {
          padding = Offsets.ClassBodyMemberDeclarationOffset;
        }        

        this.charStart = charStart - padding;
        this.charEnd = charEnd - padding;
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