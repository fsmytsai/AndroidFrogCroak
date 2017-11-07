package ViewModel;

/**
 * Created by user on 2017/7/22.
 */

public class ChatData {
    public int _id;
    public String message;
    public int from;
    public int type;

    public ChatData(int _id, String message, int from, int type) {
        this._id = _id;
        this.message = message;
        this.from = from;
        this.type = type;
    }
}
