package com.coderbunker.hyperledger.qrcode;


import android.os.Parcel;
import android.os.Parcelable;

public class CodeEntity implements Parcelable {
    private String code;

    public CodeEntity(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
    }

    public static Parcelable.Creator<CodeEntity> CREATOR = new Creator<CodeEntity>() {
        @Override
        public CodeEntity createFromParcel(Parcel source) {
            return new CodeEntity(source.readString());
        }

        @Override
        public CodeEntity[] newArray(int size) {
            return new CodeEntity[0];
        }
    };

}
