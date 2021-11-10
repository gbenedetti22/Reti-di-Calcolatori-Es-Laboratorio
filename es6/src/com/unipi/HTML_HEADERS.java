package com.unipi;

public enum HTML_HEADERS {
    OK("""
            HTTP/1.1 200 OK\r
            Server: es6Server\r
            """),
    NOT_FOUND("""
            HTTP/1.1 404 Not Found\r
            Server: es6Server\r
            Content-Type: text/html\r
            \r
            <h1>ERROR 404&nbsp;</h1>
            <p>Not Found</p>""");
    private final String value;

    HTML_HEADERS(String s) {
        value = s;
    }

    public String message() {
        return value;
    }
}
