package org.noear.solon.boot.smarthttp.http;

import org.noear.solon.Solon;
import org.noear.solon.boot.ServerProps;
import org.noear.solon.boot.smarthttp.XPluginImp;
import org.noear.solon.core.event.EventBus;
import org.smartboot.http.common.enums.HttpStatus;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;
import org.smartboot.http.server.HttpServerHandler;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class SmHttpContextHandler extends HttpServerHandler {
    protected ExecutorService executor;

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, CompletableFuture<Object> future) throws IOException {
        if (executor == null) {
            handle0(request, response, future);
        } else {
            executor.execute(() -> {
                handle0(request, response, future);
            });
        }
    }

    protected void handle0(HttpRequest request, HttpResponse response, CompletableFuture<Object> future) {
        try {
            handleDo(request, response);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            future.complete(this);
        }
    }

    protected void handleDo(HttpRequest request, HttpResponse response) {
        try {
            if ("PRI".equals(request.getMethod())) {
                response.setHttpStatus(HttpStatus.NOT_IMPLEMENTED);
                return;
            }

            SmHttpContext ctx = new SmHttpContext(request, response);

            ctx.contentType("text/plain;charset=UTF-8");
            if (ServerProps.output_meta) {
                ctx.headerSet("Solon-Boot", XPluginImp.solon_boot_ver());
            }

            Solon.app().tryHandle(ctx);

            if (ctx.getHandled() || ctx.status() >= 200) {
                ctx.commit();
            } else {
                ctx.status(404);
                ctx.commit();
            }
        } catch (Throwable e) {
            EventBus.pushTry(e);
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}