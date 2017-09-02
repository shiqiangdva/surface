package sq.mytest_surfaceview;

import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private MySurfaceView mySurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySurfaceView = (MySurfaceView)findViewById(R.id.my_surface_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);


        mySurfaceView.setUriAddress(uri);
        mySurfaceView.playVideo();

    }
}
