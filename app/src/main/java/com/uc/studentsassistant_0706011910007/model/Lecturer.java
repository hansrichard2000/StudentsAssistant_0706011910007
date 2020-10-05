package com.uc.studentsassistant_0706011910007.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Lecturer implements Parcelable {
    private String id, name, gender, expertise;

    public Lecturer(){

    }

    public Lecturer(String id, String name, String gender, String expertise) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.expertise = expertise;
    }

    protected Lecturer(Parcel in) {
        id = in.readString();
        name = in.readString();
        gender = in.readString();
        expertise = in.readString();
    }

    public static final Creator<Lecturer> CREATOR = new Creator<Lecturer>() {
        @Override
        public Lecturer createFromParcel(Parcel in) {
            return new Lecturer(in);
        }

        @Override
        public Lecturer[] newArray(int size) {
            return new Lecturer[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getExpertise() {
        return expertise;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(gender);
        parcel.writeString(expertise);
    }
}
