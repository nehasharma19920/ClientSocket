package com.example.garima.clientsocket;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private TextView serverMessage;
    private Thread m_objectThreadClient;
    private Socket clientSocket;
    private EditText editText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverMessage= (TextView)findViewById(R.id.serverMessage);
        editText = (EditText)findViewById(R.id.editText);

    }
    public void Start(View view)
    {
       final String message = editText.getText().toString();
        m_objectThreadClient  = new Thread(new Runnable() {
        @Override
        public void run() {
            try
            {
                clientSocket = new Socket("192.168.43.1",8080);;
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                oos.writeObject(message);
                Message serverMessage = Message.obtain();
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                String strMessage = (String)ois.readObject();
                serverMessage.obj = strMessage;
                mHandler.sendMessage(serverMessage);
                oos.close();
                ois.close();

            }
            catch (Exception e )
            {
                Log.e("Exception",e.getMessage());
            }

        }
    });
        m_objectThreadClient.start();
    }
    Handler mHandler = new Handler()
    {
        public void handleMessage(Message arg)
        {
            messageDisplay(arg.obj.toString());
        }

        private void messageDisplay(String servermessage) {
            serverMessage.setText(""+servermessage);

        }
    };
}
