/*
 * MIT License
 *
 * Copyright (c) 2021 fairdevkit
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.fairdevkit.spock.extension.server.spi.httpserver;

import com.github.fairdevkit.spock.extension.server.spi.EmbeddedServer;
import com.github.fairdevkit.spock.extension.server.spi.ResourceContext;
import com.sun.net.httpserver.HttpServer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class HttpServerEmbeddedServer implements EmbeddedServer {
    private HttpServer server;

    @Override
    public InetSocketAddress start() throws IOException {
        return start(0);
    }

    @Override
    public InetSocketAddress start(int port) throws IOException {
        if (server != null) {
            throw new IllegalStateException("Server already running");
        }

        server = HttpServer.create();
        server.bind(new InetSocketAddress(InetAddress.getLoopbackAddress(), port), 0);
        server.start();
        
        return server.getAddress();
    }

    @Override
    public InetSocketAddress getAddress() {
        checkServerState();

        return server.getAddress();
    }

    @Override
    public void createContext(ResourceContext ctx) {
        checkServerState();

        server.createContext(ctx.path(), exchange -> {
            var headers = exchange.getResponseHeaders();
            headers.add("Content-Type", ctx.contentType());

            var stream = new ByteArrayOutputStream();
            var length = getClass().getResourceAsStream(ctx.resource()).transferTo(stream);

            exchange.sendResponseHeaders(ctx.status(), length);

            try (var output = exchange.getResponseBody()) {
                stream.writeTo(output);
            }
        });
    }

    @Override
    public void removeContext(ResourceContext ctx) {
        checkServerState();

        server.removeContext(ctx.path());
    }

    @Override
    public void stop() {
        checkServerState();

        server.stop(0);
    }

    private void checkServerState() {
        if (server == null) {
            throw new IllegalStateException("HttpServer was not started");
        }
    }
}
