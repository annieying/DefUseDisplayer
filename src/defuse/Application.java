package defuse;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class Application implements IApplication {

    @Override
    public Object start(IApplicationContext context)
            throws Exception {        
        DefUseAnalyzer.analyze(AstUtil.getCompilationUnit());
        return IApplication.EXIT_OK;
    }

    @Override
    public void stop() {
        
    }
}
