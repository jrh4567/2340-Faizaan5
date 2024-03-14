package com.example.spotify_app;

public class Data {
    // handle data
    private static String time = "medium_term";
    private static String data = "";
    public static void setTime(String t) {
        time = t;
    }
    public static String getTime() {
        return time;
    }
    public static void setData(String t) {
        data = t;
    }
    public static void appendData(String t) {
        data += t;
    }
    public static String getData() {
        return data;
    }


}
