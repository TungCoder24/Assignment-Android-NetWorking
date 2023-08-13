package tungndph27567.fpoly.assignment_androidnetworiking;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import tungndph27567.fpoly.assignment_androidnetworiking.adapters.ViewPager2Adapter;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 idPager2;
    private BottomNavigationView idBottomNav;
    private ViewPager2Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        idPager2 = (ViewPager2) findViewById(R.id.id_pager2);
        idBottomNav = (BottomNavigationView) findViewById(R.id.id_bottomNav);
        adapter = new ViewPager2Adapter(this);
        idPager2.setAdapter(adapter);
        idPager2.setUserInputEnabled(false);


        idBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.menu_home){
                    idPager2.setCurrentItem(0);


                }else if(item.getItemId()==R.id.menu_acount){
                    idPager2.setCurrentItem(3);
                }
                return true;
            }
        });
        idPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        idBottomNav.getMenu().findItem(R.id.menu_home).setChecked(true);
                        break;

                    case 1:
                        idBottomNav.getMenu().findItem(R.id.menu_acount).setChecked(true);
                        break;
                }
            }
        });

    }
}