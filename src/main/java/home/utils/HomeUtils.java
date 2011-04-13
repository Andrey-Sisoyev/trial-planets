package home.utils;


public class HomeUtils {
    public static Integer str2int(String str) {
        Integer ret = null;
        if(str == null) return ret;
        try { ret = new Integer(str); } catch (NumberFormatException e) {}
        return ret;
    }
}
