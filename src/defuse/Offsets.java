package defuse;

import ca.mcgill.cs.swevo.ppa.SnippetUtil;

public class Offsets {
  
  public static final int BlockStatementsOffset;
  public static final int ClassBodyMemberDeclarationOffset;
    
  static {
    BlockStatementsOffset = getMethodBodyOffset();
    ClassBodyMemberDeclarationOffset = getTypeBodyOffset();
  }
  
  private static int getTypeBodyOffset() {
    StringBuffer newContent = new StringBuffer();
    newContent.append("package " + SnippetUtil.SNIPPET_PACKAGE + ";\n");
    newContent.append("public class " + SnippetUtil.SNIPPET_CLASS + " extends "
        + SnippetUtil.SNIPPET_SUPER_CLASS + " {\n");
    return newContent.length();
  }

  private static int getMethodBodyOffset() {
    StringBuffer newContent = new StringBuffer();
    newContent.append("package " + SnippetUtil.SNIPPET_PACKAGE + ";\n");
    newContent.append("public class " + SnippetUtil.SNIPPET_CLASS + " extends "
        + SnippetUtil.SNIPPET_SUPER_CLASS + " {\n");
    newContent.append("  public void " + SnippetUtil.SNIPPET_METHOD + "() {\n");
    return newContent.length();
  }
  
  
}
