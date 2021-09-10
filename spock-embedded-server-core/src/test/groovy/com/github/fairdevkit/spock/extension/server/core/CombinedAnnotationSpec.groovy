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
package com.github.fairdevkit.spock.extension.server.core

import spock.lang.Specification

@EmbedResource(resource = "/root.txt")
class CombinedAnnotationSpec extends Specification {
    @EmbedResource(path = "/foo/", resource = "/foo.txt")
    def "resolve paths for both spec and feature embedded resources"() {
        expect:
        new URL("http://localhost:8081/").text == "root"
        and:
        new URL("http://localhost:8081/foo/").text == "foo"
    }

    @EmbedResource(path = "/foo/", resource = "/foo.txt")
    def "resolve paths with injected port for both spec and feature embedded resource"(port) {
        expect:
        new URL("http://localhost:$port/").text == "root"
        and:
        new URL("http://localhost:$port/foo/").text == "foo"
    }
}
