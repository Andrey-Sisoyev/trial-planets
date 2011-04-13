package home.utils;

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
}