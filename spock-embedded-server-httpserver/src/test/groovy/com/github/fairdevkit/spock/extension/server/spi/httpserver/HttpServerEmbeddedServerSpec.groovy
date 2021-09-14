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
package com.github.fairdevkit.spock.extension.server.spi.httpserver

import com.github.fairdevkit.spock.extension.server.spi.ResourceContext
import spock.lang.Specification

class HttpServerEmbeddedServerSpec extends Specification {
    /** System under test. */
    def server = new HttpServerEmbeddedServer()

    def "invoking methods without calling start will fail"() {
        when: "one of the state based methods is invoked"
        server.invokeMethod(method, null)

        then: "an illegal state is triggered"
        def ex = thrown IllegalStateException
        ex.message == "HttpServer was not started"

        where:
        method << [
            "getAddress",
            "createContext",
            "removeContext",
            "stop"
        ]
    }

    def "invoking start multiple times without providing a port"() {
        when: "the server is started once, all is fine"
        server.start()
        and: "the server is attempted to start a second time"
        server.start()

        then: "an illegal state is triggered"
        def ex = thrown IllegalStateException
        ex.message == "Server already running"

        cleanup:
        server.stop()
    }

    def "invoking start multiple times with an explicit port"() {
        when: "the server is started once, all is fine"
        server.start(12345)
        and: "the server is attempted to start a second time"
        server.start(12345)

        then: "an illegal state is triggered"
        def ex = thrown IllegalStateException
        ex.message == "Server already running"

        cleanup:
        server.stop()
    }

    def "resource contexts are served in a predictable manner"() {
        given:
        server.start()

        when:
        server.createContext(new ResourceContext(path, resource, 200, "text/plain"))

        then:
        new URL("http://localhost:${server.address.port}$request").text == value

        cleanup:
        server.stop()

        where:
        path       | resource       | request         || value
        "/"        | "/root.txt"    | "/"             || "root"
        "/foo/"    | "/foo.txt"     | "/foo/"         || "foo"
        "/foobar/" | "/foobar.txt"  | "/foobar/"      || "foobar"
        "/foo/bar" | "/foo-bar.txt" | "/foo/bar"      || "foo-bar"
        "/bar.txt" | "/bar.txt"     | "/bar.txt"      || "bar"
        "/foo"     | "/foo.txt"     | "/foo#fragment" || "foo"
        "/foo"     | "/foo.txt"     | "/foo?bar"      || "foo"
    }

    def "invoking stop multiple times does not cause an unstable situation"() {
        given:
        server.start()

        when: "the server is stopped once, all is fine"
        server.stop()
        and: "the server is stopped for a second time"
        server.stop()

        then: "any exceptions are handled silently"
        notThrown Throwable
    }
}
