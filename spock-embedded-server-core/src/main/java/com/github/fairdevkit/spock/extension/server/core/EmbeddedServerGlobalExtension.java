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
package com.github.fairdevkit.spock.extension.server.core;

import com.github.fairdevkit.spock.extension.server.spi.EmbeddedServer;
import java.io.IOException;
import java.util.ServiceLoader;
import org.spockframework.runtime.extension.IGlobalExtension;

public class EmbeddedServerGlobalExtension implements IGlobalExtension {
    @Override
    public void start() {
        var server = ServiceLoader.load(EmbeddedServer.class)
                .findFirst()
                .orElseThrow();

        try {
            server.start(8081);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        EmbeddedServerHolder.INSTANCE.setServer(server);
    }

    @Override
    public void stop() {
        EmbeddedServerHolder.INSTANCE.getServer().stop();
    }
}
