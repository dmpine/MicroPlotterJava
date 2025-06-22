package microplotter.listeners;

public interface SerialDataListener {
    void onDataReceived(String data);
    void onConnectionStatusChanged(boolean connected);
    void onError(String error);
}
