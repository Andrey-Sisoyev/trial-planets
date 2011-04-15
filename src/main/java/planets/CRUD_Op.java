package planets;

public enum CRUD_Op {
    CREATE, READ, UPDATE, DELETE;
    public static CRUD_Op valueOf2(String str) {
        if(str == null) return null;
        CRUD_Op ret = null;
        try { ret = valueOf(str); } catch(IllegalArgumentException e) {}
        return ret;
    }
    public boolean areFieldsReadOnly() {
        return this == READ || this == DELETE;
    }

    public boolean getAreFieldsReadOnly() { // workaround for JSF EL 2.0
        return areFieldsReadOnly();
    }

    public boolean isRead() {
        return this == READ;
    }

    public String getString() { // workaround for JSF EL 2.0
        return this.toString();
    }
}