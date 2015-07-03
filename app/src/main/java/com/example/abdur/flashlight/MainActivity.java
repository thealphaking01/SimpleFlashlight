package com.example.abdur.flashlight;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
//import android.graphics.Camera;
import android.hardware.Camera;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import com.example.abdur.flashlight.R;



public class MainActivity extends ActionBarActivity {

    private Camera camera;
    ImageButton flashlight;
    private Boolean ison=false;
    Camera.Parameters params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashlight=((ImageButton)findViewById(R.id.flashlightSwitch));
        boolean isCameraFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if(!isCameraFlash) {
            showNoCameraAlert();
        } else {
                camera = Camera.open();
            params = camera.getParameters();
        }

        flashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ison) {
                    setFlashlightOff();
                } else {
                    setFlashlightOn();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void showNoCameraAlert(){
        new AlertDialog.Builder(this)
                .setTitle("Error: No Camera Flash!")
                .setMessage("Camera flashlight not available in this Android device!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish(); // close the Android app
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        return;
    }
    private void setFlashlightOn() {
        params = camera.getParameters();
        params.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        camera.startPreview();
        ison = true;
        flashlight.setImageResource(R.drawable.off);
    }

    private void setFlashlightOff() {
        params.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();
        ison = false;
        flashlight.setImageResource(R.drawable.on);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(camera!=null) {
            camera.release(); camera=null;
        }
    }
}
