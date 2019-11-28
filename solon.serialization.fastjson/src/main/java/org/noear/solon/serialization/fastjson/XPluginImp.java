package org.noear.solon.serialization.fastjson;

import org.noear.solon.XApp;
import org.noear.solon.core.Aop;
import org.noear.solon.core.XRenderManager;
import org.noear.solon.core.XPlugin;

public class XPluginImp implements XPlugin {
    public static boolean output_meta = false;

    @Override
    public void start(XApp app) {
        output_meta = app.prop().getInt("solon.output.meta", 0) > 0;

        FastjsonRender render = new FastjsonRender();

        //XRenderManager.register(render);
        XRenderManager.mapping(XRenderManager.mapping_model,render);
    }
}
