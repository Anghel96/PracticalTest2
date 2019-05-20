package ro.pub.cs.systems.eim.practicaltest2;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class CommunicationThread extends Thread {
    Socket socket = null;

    public CommunicationThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            PrintWriter printWriter = Utilities.getWriter(socket);
            BufferedReader bufferedReader = Utilities.getReader(socket);

            if (printWriter == null || bufferedReader == null) {
                Log.e("TAG", "[SERVER THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            String info = bufferedReader.readLine();
            if (info == null || info.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type!");
                return;
            }
            String[] tokens = info.split(",");
            String operation = tokens[0];
            int operator1 = Integer.parseInt(tokens[1]);
            int operator2 = Integer.parseInt(tokens[2]);
            int result = 0;
            String stringResult;

            switch (operation) {
                case "add":
                    result = operator1 + operator2;

                    if (result > 2147483647) {
                        stringResult = "overflow";
                    } else {
                        stringResult = Integer.toString(result);
                    }
                    break;
                case "mul":
                    Thread.sleep(2000);
                    if (result > 2147483647) {
                        stringResult = "overflow";
                    } else {
                        stringResult = Integer.toString(result);
                    }
                    break;
                default:
                    stringResult = "[COMMUNICATION THREAD] Wrong information typ)!";
                    break;

            }

            printWriter.println(stringResult);
            printWriter.flush();

        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
        catch (InterruptedException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception interrupt has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
