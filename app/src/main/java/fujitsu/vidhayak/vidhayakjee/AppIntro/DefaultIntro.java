package fujitsu.vidhayak.vidhayakjee.AppIntro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;

import fujitsu.vidhayak.vidhayakjee.MainActivity;
import fujitsu.vidhayak.vidhayakjee.R;


/**
 * Created by Fujitsu on 14/07/2017.
 */

public class DefaultIntro extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//Down here, we add the xml layouts into sample slide inflater.
        addSlide(SampleSlide.newInstance(R.layout.intro));
        addSlide(SampleSlide.newInstance(R.layout.intro2));
        addSlide(SampleSlide.newInstance(R.layout.intro3));

        showStatusBar(true);

        setDepthAnimation();

    }



    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        loadMainActivity();
    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        loadMainActivity();
        Toast.makeText(getApplicationContext(), "Skip...", Toast.LENGTH_SHORT).show();
    }

    public void getStarted(View v){
        loadMainActivity();
    }
}
