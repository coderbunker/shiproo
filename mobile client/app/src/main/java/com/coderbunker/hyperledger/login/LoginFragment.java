package com.coderbunker.hyperledger.login;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.coderbunker.hyperledger.R;
import com.coderbunker.hyperledger.communication.Command;
import com.coderbunker.hyperledger.communication.LoginCommand;

public class LoginFragment extends Fragment {

    private EditText login;
    private EditText pwd;
    private Button confirm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_login, container, false);
        login = (EditText) view.findViewById(R.id.login);
        pwd = (EditText) view.findViewById(R.id.password);
        confirm = (Button) view.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO send data to the socket
                User user = new User(
                        login.getText().toString(),
                        pwd.getEditableText().toString()
                );
                Command command = new LoginCommand(user);
//                command.send();
            }
        });
        return view;
    }
}
