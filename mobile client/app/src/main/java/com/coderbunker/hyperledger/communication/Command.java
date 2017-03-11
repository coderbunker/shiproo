package com.coderbunker.hyperledger.communication;


import android.os.Parcelable;

public abstract class Command implements Parcelable, Runnable {
    public abstract void execute();
}
