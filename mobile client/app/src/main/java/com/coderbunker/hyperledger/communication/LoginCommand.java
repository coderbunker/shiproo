package com.coderbunker.hyperledger.communication;


import android.os.Parcel;

import com.coderbunker.hyperledger.login.User;

public class LoginCommand extends Command {

    private User user;

    public LoginCommand(User user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
