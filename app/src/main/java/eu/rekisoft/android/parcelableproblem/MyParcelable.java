package eu.rekisoft.android.parcelableproblem;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/**
 * Created by rekire on 20.05.2016.
 */
public class MyParcelable implements Parcelable {
    public String text;
    public int count;
    public UUID id;
    public float problem;

    public MyParcelable(String text, int count, UUID id, float problem) {
        this.text = text;
        this.count = count;
        this.id = id;
        this.problem = problem;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeInt(this.count);
        dest.writeSerializable(this.id);
        dest.writeFloat(this.problem);
    }

    protected MyParcelable(Parcel in) {
        this.text = in.readString();
        this.count = in.readInt();
        this.id = (UUID)in.readSerializable();
        this.problem = in.readFloat();
    }

    public static final Parcelable.Creator<MyParcelable> CREATOR = new Parcelable.Creator<MyParcelable>() {
        @Override
        public MyParcelable createFromParcel(Parcel source) {
            return new MyParcelable(source);
        }

        @Override
        public MyParcelable[] newArray(int size) {
            return new MyParcelable[size];
        }
    };
}
