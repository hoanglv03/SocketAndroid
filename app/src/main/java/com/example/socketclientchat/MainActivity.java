package com.example.socketclientchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    TextView tvMessage;
    Button btnSend;
    EditText edSend;
    private ArrayList<String> chatMessages;
    private ChatAdapter chatAdapter;
    private ListView listViewChat;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.0.104:3000/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSocket.on("chat message", onNewMessage);
        listViewChat = findViewById(R.id.listViewChat);
        btnSend = findViewById(R.id.btnSend);
        edSend = findViewById(R.id.edSend);
        mSocket.connect();
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, chatMessages);
        listViewChat.setAdapter(chatAdapter);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSend();
            }
        });
    }
    private void attemptSend() {
        String message = edSend.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }

        edSend.setText("");
        mSocket.emit("chat message", message);
    }
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
          runOnUiThread(new Runnable() {
                @Override
                public void run() {
                        JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    addMessage(username, message);
                }
            });
        }
    };
    private void addMessage(String user,String mess){
        chatMessages.add(user+":"+mess);
        chatAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("chat message", onNewMessage);
    }


}