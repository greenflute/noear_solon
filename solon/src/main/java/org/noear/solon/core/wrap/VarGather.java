package org.noear.solon.core.wrap;

import org.noear.solon.core.AopContext;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.VarHolder;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 变量收集器（主要为 Configuration 构建 method bean 时服务）
 *
 * @see AopContext#tryBuildBean
 * @author noear
 * @since 1.0
 * */
public class VarGather implements Runnable {
    //变量
    List<VarHolder> vars;
    int varSize;
    //完成时
    Consumer<Object[]> done;

    public VarGather(int varSize, Consumer<Object[]> done) {
        this.done = done;
        this.varSize = varSize;
        this.vars = new ArrayList<>(varSize);
    }

    public void add(VarHolder p) {
        //VarHolderOfParam p2 = new VarHolderOfParam(bw.context(), p, this);
        vars.add(p);
    }

    @Override
    public void run() {
        for (VarHolder p1 : vars) {
            if (p1.isDone() == false) {
                return;
            }
        }

        if (vars.size() != varSize) {
            return;
        }

        List<Object> args = new ArrayList<>(vars.size());
        for (VarHolder p1 : vars) {
            args.add(p1.getValue());
        }

        done.accept(args.toArray());
    }
}
