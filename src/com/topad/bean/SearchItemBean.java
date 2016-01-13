package com.topad.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 *
 * @author ouruiqiang
 * @data: on 15/10/20 10:11
 */
public class SearchItemBean implements Serializable,Parcelable {
    /**
     * serialVersionUID:TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = 4768927122317982665L;
    public String locaion;
    public String type;
    public String name;
    public String lanmu_name;//第一分类
    public String voice;
    public SearchItemBean() {
    }
    public SearchItemBean(Parcel in) {
        locaion = in.readString();
        type = in.readString();
        name = in.readString();
        lanmu_name = in.readString();
        voice = in.readString();
    }

    public static final Creator<SearchItemBean> CREATOR = new Creator<SearchItemBean>() {
        @Override
        public SearchItemBean createFromParcel(Parcel in) {
            return new SearchItemBean(in);
        }

        @Override
        public SearchItemBean[] newArray(int size) {
            return new SearchItemBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(locaion);
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(lanmu_name);
        dest.writeString(voice);
    }
}
