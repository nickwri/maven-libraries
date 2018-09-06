package uk.co.referencepoint.mavenlibraries;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import uk.co.referencepoint.publishtest.*;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Helpers.getCurrentDateStringPlusMinutes(1, "xxx");
    }
}
