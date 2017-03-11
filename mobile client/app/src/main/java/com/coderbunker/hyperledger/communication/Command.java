package com.coderbunker.hyperledger.communication;


import android.os.Parcelable;

public abstract class Command implements Parcelable{
    public abstract void send();
}
