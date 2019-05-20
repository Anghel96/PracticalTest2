package ro.pub.cs.systems.eim.practicaltest2;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
    private int port = 0;
    ServerSocket serverSocket = null;

    public ServerThread(int port) {
        this.port = port;

        try{
            this.serverSocket = new ServerSocket(port);
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An error has occurred during server run: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }

    }

    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Log.i(Constants.TAG, "[SERVER THREAD] Waiting for a client invocation...");
                Socket socket = serverSocket.accept();

                if (socket == null) {
                    Log.e(Constants.TAG, "[SERVER THREAD] Exception has occured...");
                    return;
                }
                Log.i(Constants.TAG, "[SERVER THREAD] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());

                CommunicationThread communicationThread = new CommunicationThread(socket);
                communicationThread.start();
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An error has occurred during server run: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

    public void stopThread() {
        interrupt();
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException ioException) {
            Log.e("TAG EXCEPTION", "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
        }

    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
