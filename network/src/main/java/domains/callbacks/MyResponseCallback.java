package domains.callbacks;

public interface MyResponseCallback {

    void onCompile(String result);
    void onError(String error);
}