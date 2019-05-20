package ro.pub.cs.systems.eim.practicaltest2;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private int port = 0;
    private String address;
    int operation1 = 0;
    int operation2 = 0;
    String operation;
    TextView messageTextView;

    public ClientThread(int port, String address, int operation1, int operation2,
                        String operation, TextView messageTextView) {

        this.port = port;
        this.address = address;
        this.operation = operation;
        this.operation1 = operation1;
        this.operation2 = operation2;
        this.messageTextView = messageTextView;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = new Socket(address, port);

            if (socket == null) {
                Log.e("TAG", "[CLIENT THREAD] Could not create socket!");
                return;
            }
            PrintWriter printWriter = Utilities.getWriter(socket);
            BufferedReader bufferedReader = Utilities.getReader(socket);
            if(printWriter == null || bufferedReader == null) {
                Log.e("TAG", "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            String request = operation + "," + operation1 + "," + operation2 + "\n";
            printWriter.println(request);
            printWriter.flush();
            String content;
            while ((content = bufferedReader.readLine()) != null) {
                final String finalContent = content;
                messageTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        messageTextView.setText(finalContent);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e("TAG", "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e("TAG", "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                }
            }
        }
    }
}
