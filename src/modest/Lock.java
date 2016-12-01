package modest;

public interface Lock extends MsgHandler {
    public void requestCS(); //may block
    public void releaseCS();
    public void handleMsg(Msg m, int src, String tag);
}
