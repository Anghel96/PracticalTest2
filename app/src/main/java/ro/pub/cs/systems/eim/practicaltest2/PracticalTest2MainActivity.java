package ro.pub.cs.systems.eim.practicaltest2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest2MainActivity extends AppCompatActivity {

    EditText serverPort = null;
    Button connectButton = null;
    Button makeOperation = null;
    EditText clientAddress = null;
    EditText clientPort = null;
    Spinner operDialog = null;
    TextView messageTextView = null;
    EditText operator1 = null;
    EditText operator2 = null;

    ServerThread serverThread = null;
    ClientThread clientThread = null;
    //ClientThreadAsyncTask clientThreadAsyncTask = null;

    public ConnectButtonListener connectButtonListener = new ConnectButtonListener();
    private class ConnectButtonListener implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            String getServerPort = serverPort.getText().toString();
            if (getServerPort == null && getServerPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            serverThread = new ServerThread(Integer.parseInt(getServerPort));

            if (serverThread.getServerSocket() == null) {
                Log.e("[MAIN TAG]", "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }

            serverThread.start();
        }
    }

    public MakeOperationButtonListener makeOperationButtonListener = new MakeOperationButtonListener();
    private class MakeOperationButtonListener implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            String getClientPort = clientPort.getText().toString();
            String getClientAddress = clientAddress.getText().toString();


            if (getClientPort == null || getClientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (getClientAddress == null || getClientAddress.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client address should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }


            if (serverThread == null || !serverThread.isAlive()){
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server!", Toast.LENGTH_SHORT).show();
                return;
            }

            String oper1 = operator1.getText().toString();
            String oper2 = operator2.getText().toString();
            String operationDialog = operDialog.getSelectedItem().toString();

            if (oper1 == null || oper1.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Oper1 should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (oper2 == null || oper2.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Oper2 should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (operationDialog == null || operationDialog.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] OperDialog should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            clientThread = new ClientThread(Integer.parseInt(getClientPort), getClientAddress,
                    Integer.parseInt(oper1), Integer.parseInt(oper2), operationDialog, messageTextView);
            //clientThreadAsyncTask = new ClientThreadAsyncTask(messageTextView);
            //clientThreadAsyncTask.execute(getClientPort, getClientAddress, city, getInfo);

            clientThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test2_main);

        serverPort = (EditText)findViewById(R.id.server_port);
        connectButton = (Button)findViewById(R.id.listen_button);
        connectButton.setOnClickListener(connectButtonListener);
        clientAddress = (EditText)findViewById(R.id.client_address);
        clientPort = (EditText)findViewById(R.id.client_port);
        operDialog = (Spinner)findViewById(R.id.spinner_information);
        operator1 = (EditText)findViewById(R.id.text_operator1);
        operator2 = (EditText)findViewById(R.id.text_operator2);
        makeOperation = (Button)findViewById(R.id.math_operation_button);
        makeOperation.setOnClickListener(makeOperationButtonListener);
        messageTextView = (TextView)findViewById(R.id.result_text_view);
    }
}
