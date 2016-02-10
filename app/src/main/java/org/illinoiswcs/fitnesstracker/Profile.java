package org.illinoiswcs.fitnesstracker;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.ValueShape;

public class Profile extends AppCompatActivity {
    Firebase myFirebaseRef;
    List<PointValue> rateList = new ArrayList<>();
    RelativeLayout profileLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileLayout = (RelativeLayout) findViewById(R.id.profileLayout);
        myFirebaseRef = new Firebase("https://techteam201516.firebaseio.com/ble_data");
        myFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                float count = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    Reading data = child.getValue(Reading.class);
                    rateList.add(new PointValue(count,data.getRate()));
                    Log.d("ONE",data.getRate().toString());
                    count ++;
                }
                showGraph();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    private void showGraph() {

        LineChartData data = new LineChartData();
        Line line = new Line(rateList);
        line.setColor(ChartUtils.COLOR_BLUE);
        List<Line> lines = new ArrayList<Line>(1);
        lines.add(line);
        data.setLines(lines);
        LineChartView view = new LineChartView(getApplicationContext());
        view.setLineChartData(data);
        data.setAxisXBottom(new Axis().setName("Axis X"));
        data.setAxisYLeft(new Axis().setName("Axis Y").setHasLines(true));
        profileLayout.addView(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
        if (id == R.id.action_logout){
            myFirebaseRef.unauth();
            startActivity(new Intent(this, Login.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
