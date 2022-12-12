package com.example.reproducecrash;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.reproducecrash.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'reproducecrash' library on application startup.

    private ActivityMainBinding binding;

    @SuppressLint("UnsafeDynamicallyLoadedCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //copy .so to app file directory
        File libSavePath = new File(getFilesDir().getAbsolutePath() + "/libreproducecrash.so");
        try {
            InputStream is = getAssets().open("libreproducecrash.so");
            FileOutputStream fos = new FileOutputStream(libSavePath);
            byte[] buffer=new byte[1024];
            int byteCount=0;
            while((byteCount=is.read(buffer))!=-1) {
                fos.write(buffer,0,byteCount);
            }
            fos.flush();
            fos.close();
            is.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        System.load(libSavePath.getPath());
        Log.i("MyLogger", libSavePath.getPath() + " loaded");

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());
    }

    /**
     * A native method that is implemented by the 'reproducecrash' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}