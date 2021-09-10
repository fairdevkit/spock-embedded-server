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
import com.github.fairdevkit.spock.extension.server.spi.ResourceContext;
import java.io.IOException;
import java.util.ServiceLoader;
import org.spockframework.runtime.extension.IAnnotationDrivenExtension;
import org.spockframework.runtime.model.FeatureInfo;
import org.spockframework.runtime.model.SpecInfo;

public class EmbedResourceAnnotationExtension implements IAnnotationDrivenExtension<EmbedResource> {
    @Override
    public void visitSpecAnnotation(EmbedResource annotation, SpecInfo spec) {
        spec.addInterceptor(methodInvocation -> {
            var server = EmbeddedServerHolder.INSTANCE.getServer();
            var ctx = new ResourceContext(annotation.path(), annotation.resource(), annotation.status(), annotation.contentType());
            server.createContext(ctx);

            try {
                methodInvocation.proceed();
            } finally {
                server.removeContext(ctx);
            }
        });
    }

    @Override
    public void visitFeatureAnnotation(EmbedResource annotation, FeatureInfo feature) {
        feature.addInterceptor(methodInvocation -> {
            var server = EmbeddedServerHolder.INSTANCE.getServer();
            var ctx = new ResourceContext(annotation.path(), annotation.resource(), annotation.status(), annotation.contentType());
            server.createContext(ctx);

            try {
                methodInvocation.proceed();
            } finally {
                server.removeContext(ctx);
            }
        });
    }
}
