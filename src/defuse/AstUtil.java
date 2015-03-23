package defuse;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import ca.mcgill.cs.swevo.ppa.PPAOptions;
import ca.mcgill.cs.swevo.ppa.ui.PPAUtil;

public class AstUtil {
	
    public static ASTNode getEclipseAst(ICompilationUnit unit) {
        ASTParser parser = ASTParser.newParser(AST.JLS3);
//        parser.setBindingsRecovery(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource(unit); // set source
        parser.setResolveBindings(true); // we need bindings later on
        ASTNode ast = (ASTNode) parser.createAST(null /* IProgressMonitor */); // parse
        return ast;
    }
    
    public static ICompilationUnit getCompilationUnit() throws CoreException {
    	IPackageFragment pkg = getPackageFragment();
        ICompilationUnit unit = pkg.getCompilationUnit("Temp.java");        
        return unit;
    }
    
    public static ICompilationUnit createCompilationUnit(String code) throws CoreException {

    	IPackageFragment pkg = getPackageFragment();

        ICompilationUnit unit = pkg.createCompilationUnit("Temp1.java", code, true, null);
        
        return unit;
    }
    
    public static IPackageFragment getPackageFragment() throws CoreException {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot wsRoot = workspace.getRoot();
        IProject project  = wsRoot.getProject("MyProject");
        IJavaProject javaProject = JavaCore.create(project);
        
        IFolder sourceFolder = project.getFolder("src");
        
        IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(sourceFolder);
        IPackageFragment pkg = root.getPackageFragment("temp");
        return pkg;
    }
    
	public static CompilationUnit getCompilationUnitUsingPPA(String source) {
		CompilationUnit ast = PPAUtil.getCU(source, new PPAOptions());
		while( ast == null ) {
//			try {
//				Thread.sleep(1000);
				PPAUtil.cleanUpAll();
//				Thread.sleep(1000);
				ast = PPAUtil.getCU(source, new PPAOptions());
				if( ast != null) break;
//			} catch (InterruptedException e) {}
			
		}
		return ast;
	}

	public static ASTNode getASTUsingPPA(String source, boolean isTypeBody) {
		ASTNode ast = PPAUtil.getSnippet(source, new PPAOptions(), isTypeBody);
		return ast;
	}
    
}
