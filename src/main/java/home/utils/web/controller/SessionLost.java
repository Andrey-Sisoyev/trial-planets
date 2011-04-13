package home.utils.web.controller;


import javax.servlet.http.HttpServlet;

public class SessionLost extends Exception {

    // ================================
    // CONSTRUCTORS

    public SessionLost() {
    }

    public SessionLost(String s) {
        super(s);
    }

    public SessionLost(String s, Throwable _throwable) {
        super(s, _throwable);
    }

    public SessionLost(Throwable _throwable) {
        super(_throwable);
    }
}
