package com.jundat95.locationtracking.View.Activity;



import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jundat95.locationtracking.Common.TiSharedPreferences;
import com.jundat95.locationtracking.Model.Node;
import com.jundat95.locationtracking.R;
import com.jundat95.locationtracking.View.Adapter.SelectNodeAdapter;
import com.jundat95.locationtracking.View.Fragment.MapsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.navigation_view) NavigationView navigationView;
    @BindView(R.id.drawer) DrawerLayout drawerLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.spinner_select_node) Spinner spnSelectNode;

    private ToggleButton toggleButton;
    public static boolean onPUT = false;

    private MapsFragment mapFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private List<Node> nodes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initNavigationDrawer();
        initMapsFragment();
        getListNode();
        initSpinner();
        onPUT = false;
    }

    public void initNavigationDrawer() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){

                    case R.id.about:
                        Toast.makeText(getApplicationContext(),"About",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.exit:
                        finish();
                        drawerLayout.closeDrawers();
                        break;
                }
                return true;

            }
        });
        View header = navigationView.getHeaderView(0);
        //GroupUserModel groupUserModel = TiSharedPreferences.getGroupUserModel(MainActivity.this,"Login_Shared");
        TextView tv_email = (TextView)header.findViewById(R.id.tv_email);
        tv_email.setText("location-tracking");

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void initMapsFragment() {
        mapFragment = MapsFragment.newInstance(this);
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_main, mapFragment);
        transaction.commit();
    }

    private void initSpinner(){
        if(checkGoToFirst()){
            nodes.add(new Node("All",0));
        }

        spnSelectNode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mapFragment.setSelectNode(nodes.get(position).getNodeId());
//                mapFragment.removeRefuses();
//                mapFragment.removeAllMakers();
//                mapFragment.removeAllPolylines();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> adapter = new SelectNodeAdapter(MainActivity.this,R.layout.spinner_select_node_item,nodes);
        spnSelectNode.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapFragment.initMapsFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        initToggleButton(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.add_node :
                showAddNode(MainActivity.this);
                return true;
            case R.id.remove_node :
                showRemoveNote(MainActivity.this);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void initToggleButton(Menu menu){
        // find toggle by menu
        toggleButton = (ToggleButton) menu.findItem(R.id.my_toggle).getActionView().findViewById(R.id.toggle_position);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mapFragment.setOnPut(true);
                    onPUT = true;
                    Toast.makeText(
                            MainActivity.this,
                            "On PUT",
                            Toast.LENGTH_SHORT
                    ).show();

                }else {
                    mapFragment.setOnPut(false);
                    onPUT = false;
                    Toast.makeText(
                            MainActivity.this,
                            "Off PUT",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }

    public void showAddNode(Context context) {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        final EditText edtInputNumber = (EditText) promptView.findViewById(R.id.input_number);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for(Node item: nodes){
                            if(Integer.parseInt(edtInputNumber.getText().toString()) == item.getNodeId()){
                                Toast.makeText(
                                        MainActivity.this,
                                        "Note existed, please check again",
                                        Toast.LENGTH_SHORT
                                ).show();
                                return;
                            }
                        }
                        nodes.add(new Node(
                                "Node "+edtInputNumber.getText().toString(),
                                Integer.parseInt(edtInputNumber.getText().toString())
                                )
                        );
                        syncListNode();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }

    public void showRemoveNote(Context context) {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        final EditText edtInputNumber = (EditText) promptView.findViewById(R.id.input_number);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int nodeId = Integer.parseInt(edtInputNumber.getText().toString());
                        if(nodeId == 0)
                            return;
                        for(Node item: nodes){
                            if(item.getNodeId() == nodeId){
                                nodes.remove(item);
                                syncListNode();
                                //TiSharedPreferences.removeSharedPreferences(MainActivity.this,"List_Node");
                                return;
                            }
                        }
                        Toast.makeText(
                                MainActivity.this,
                                "Note is not existed, please check again",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }

    private void syncListNode(){
        TiSharedPreferences.saveListNode(
                MainActivity.this,
                "List_Node",
                nodes
                );
    }

    private void getListNode(){
        if(TiSharedPreferences.getListNode(MainActivity.this, "List_Node") != null){
            nodes.addAll(TiSharedPreferences.getListNode(
                    MainActivity.this,
                    "List_Node"
            ));
        }

    }

    private boolean checkGoToFirst(){
        if(TiSharedPreferences.getSharedPreferences(MainActivity.this,"GoTo_First_Main") == null){
            TiSharedPreferences.saveSharedPreferences(
                    MainActivity.this,
                    "GoTo_First_Main",
                    "Go To MainActivity"
            );
            return true;
        }else {
            return false;
        }
    }

}
