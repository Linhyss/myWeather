package myJni;

/**
 * Created by Administrator on 2019/5/13.
 */

public class myJni {

    public native int getInt();


    static {
        System.loadLibrary("hello");
    }

}
