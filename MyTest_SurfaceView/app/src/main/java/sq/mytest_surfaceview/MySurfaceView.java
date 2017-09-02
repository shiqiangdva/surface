package sq.mytest_surfaceview;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by sp01 on 2017/9/2.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnVideoSizeChangedListener {

    private Context context;
    private MediaPlayer player;
    // 可以理解为ui上的夹层
    private SurfaceHolder surfaceHolder;
    private Uri uri;
    private int progressValue = 0;

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what){
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.e("MySurfaceView", "MEDIA_ERROR_SERVER_DIED");
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.e("MySurfaceView", "MEDIA_ERROR_UNKNOWN");
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // 启动播放器
        player.start();
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        // 当一些特定信息出现或者警告时触发
        switch (what){
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                Log.e("MySurfaceView", "MEDIA_INFO_BUFFERING_START");
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                Log.e("MySurfaceView", "MEDIA_INFO_BUFFERING_END");
                break;
            case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                Log.e("MySurfaceView", "MEDIA_INFO_BAD_INTERLEAVING");
                break;
            case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                Log.e("MySurfaceView", "MEDIA_INFO_METADATA_UPDATE");
                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                Log.e("MySurfaceView", "MEDIA_INFO_VIDEO_TRACK_LAGGING");
                break;
            case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                Log.e("MySurfaceView", "MEDIA_INFO_NOT_SEEKABLE");
                break;
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (!mp.isPlaying()){
            mp.start();
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        Log.e("MySurfaceView", "onSeekComplete");
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

    }

    /**
     * 枚举视频地址的类型
     */
    public enum URITYPE{
        LOCAL,NETWORK,ONLINE
    }

    private URITYPE TYPE;

    /**
     * 继承surfaceView
     * super 改成了 this ?
     * @param context
     */
    public MySurfaceView(Context context) {
        this(context,null);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        initPlayer();
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        player.setDisplay(holder);
        playVideo();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (player.isPlaying()){
            progressValue = player.getCurrentPosition();
            player.stop();
        }else {
            player.stop();
        }
    }

    /**
     * 播放视频
     */
    public void playVideo() {
        switch (TYPE){
            case ONLINE:
                playOnline();
                break;
            case LOCAL:
                playLocal();
                break;
            case NETWORK:
                playNetwork();
                break;
        }
    }

    /**
     * 播放http视频
     */
    private void playNetwork() {
        try {
            initPlayer();
            player.setDataSource(context,uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.prepareAsync();
    }

    /**
     * 播放本地视频
     */
    private void playLocal() {
        try {
            initPlayer();
            player.setDataSource(context,uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.prepareAsync();
    }

    /**
     * 播放在线视频
     */
    private void playOnline() {
        try {
            initPlayer();
            player.setDataSource(context,uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.prepareAsync();
    }

    /**
     * 初始化 MediaPlayer
     * 并设置播放器监听器
     */
    private void initPlayer() {
        if (player == null){
            player = new MediaPlayer();
        }
        player.reset();
        player.setOnErrorListener(this);
        player.setOnCompletionListener(this);
        player.setOnInfoListener(this);
        player.setOnPreparedListener(this);
        player.setOnSeekCompleteListener(this);
        player.setOnVideoSizeChangedListener(this);
    }


    /**
     * 对外提供的设置uri的方法
     */
    public void setUriAddress(Uri uriAddress){
        uri = uriAddress;
        TYPE = URITYPE.LOCAL;
    }

    /**
     * 暂停播放
     */
    public void pause(){
        progressValue = player.getCurrentPosition();
        player.pause();
    }

    /**
     * 继续播放
     */
    public void resume(){
        if (!player.isPlaying()){
            player.seekTo(progressValue);
            player.start();
        }
    }

    /**
     * 停止播放
     */
    public void stop(){
        progressValue = 0;
        if (player.isPlaying()){
            player.seekTo(0);
            player.pause();
        }else {
            player.pause();
        }
    }

    /**
     * 重播
     */
    public void reset(){
        progressValue = 0;
        if (player.isPlaying()){
            player.seekTo(0);
            player.start();
        }else {
            playVideo();
        }
    }

}
