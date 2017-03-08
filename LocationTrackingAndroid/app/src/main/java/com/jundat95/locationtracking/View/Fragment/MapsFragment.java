package com.jundat95.locationtracking.View.Fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jundat95.locationtracking.API.APIGPSGroupInterface;
import com.jundat95.locationtracking.Common.DateTimeManager;
import com.jundat95.locationtracking.Common.MarkerManager;
import com.jundat95.locationtracking.Common.PolylineManager;
import com.jundat95.locationtracking.Model.DataModel;
import com.jundat95.locationtracking.Model.ResponseModel;
import com.jundat95.locationtracking.R;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnPoiClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String Main_Activity = "Main_Activity";

    @BindView(R.id.map_fragment) MapView mMapView;

    private AppCompatActivity context;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;


    // List data get from api
    private List<DataModel> dataModels = new ArrayList<>();
    private List<Marker> listMaker = new ArrayList<>();
    private List<Polyline> listPolyline = new ArrayList<>();
    private List<LatLng> listLatLng = new ArrayList<>();
    // list temp, save idNode
    private List<Integer> listIdNode = new ArrayList<>();
    // List save list List<DataModel>
    private List<List<DataModel>> listDataModels = new ArrayList<>();

    private Handler mHandler;


    private boolean onPut = false;
    private boolean moveCamera = true;
    private int selectNode = 0;

    public static MapsFragment newInstance(AppCompatActivity context) {
        MapsFragment mapsFragment = new MapsFragment();
        mapsFragment.context = context;
        return mapsFragment;
    }

    public MapsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        ButterKnife.bind(this, view);
        initMaps(savedInstanceState);
        initMapsFragment();
        return view;
    }

    public void initMapsFragment(){
        mHandler = new Handler();
    }

    private void initMaps(Bundle bundle) {
        mMapView.onCreate(bundle);
        mMapView.onResume();
        mMapView.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Click position show info
        mMap.setOnPoiClickListener((GoogleMap.OnPoiClickListener) this);

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mHandler.post(mRunable);
    }

    public void requestServer() {

        Log.d(Main_Activity,"Start Request");
        if (selectNode == 0) {
            getAllPositions();
        } else {
            getPosition(selectNode);
        }
        Log.d(Main_Activity,"Stop Request------------->>>");

    }

    // Auto refresh
    private final Runnable mRunable = new Runnable() {
        @Override
        public void run() {
            requestServer();
            mHandler.postDelayed(mRunable,3000);
        }
    };

    // Get all position
    private void getAllPositions(){
        APIGPSGroupInterface.Factory.getInstance().getAllPositions().enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.isSuccessful()){
                    ResponseModel responseModel = response.body();
                    dataModels.addAll(responseModel.getData());
                    processListDataModel();
                    if(dataModels.size() > 0){
                        paintMarkerAll();
                    }else {
                        Toast.makeText(
                                context,
                                "Waiting for location tracking",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }else {
                    Toast.makeText(
                            context,
                            "Load All Position Fail: "+response.errorBody(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(
                        context,
                        "Load All Position Error: "+t.getLocalizedMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    // Paint Positions in map
    private void paintMarkerAll(){
        removeAllMakers();
        removeAllPolylines();

        for (DataModel item : dataModels){
            LatLng latLng = new LatLng(item.getLocation().get(0),item.getLocation().get(1));
            listLatLng.add(latLng);
            if(latLng != null ){
                int nodeId = Integer.parseInt(item.getNode());
                Marker marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .title("Time stamp: "+DateTimeManager.getTimeFromTimeStamp(item.getTimestamp())+" Node: "+item.getNode())
                                .icon(BitmapDescriptorFactory.fromResource(MarkerManager.getImages(nodeId)))
                );
                marker.setTag(item.getId());
                listMaker.add(marker);

            }
        }
        progressPolyline();
        paintPolyline();

        removeRefuses();
    }

    private void progressPolyline(){
        // Count item
        int countItem = 0;
        Integer a = 0;
        Integer b = 0;
        for (int i = 0; i < dataModels.size(); i++){
            boolean isOccur = false;
            a = Integer.parseInt(dataModels.get(i).getNode());
            for(int j = 0; j < i; j++){
                b = Integer.parseInt(dataModels.get(j).getNode());
                if(a == b){
                    isOccur = true;
                    break;
                }

            }
            if(!isOccur){
                countItem ++;
                listIdNode.add(a);
            }
        }
        // Create array list polylines

        for (Integer item1: listIdNode){
            List<DataModel> temp = new ArrayList<>();
            for(DataModel item2 : dataModels){
                if(Integer.parseInt(item2.getNode()) == item1){
                    temp.add(item2);
                }
            }
            listDataModels.add(temp);

        }

        //paintPolyline();
        //Log.d(Main_Activity,"List Models: "+listDataModels.size());
    }

    private void paintPolyline(){

        for(int i = 0; i < listDataModels.size(); i++){
            int color = PolylineManager.getColors(i);
            List<LatLng> latLngs = new ArrayList<>();
            for (DataModel item1: listDataModels.get(i)){
                latLngs.add(new LatLng(item1.getLocation().get(0),item1.getLocation().get(1)));
                //Log.d(Main_Activity,"XXXxxxx: "+item1.getNode());
            }

            Polyline polyline = mMap.addPolyline(
                    new PolylineOptions()
                            .addAll(latLngs)
                            .width(20)
                            .visible(true)
                            .color(color)
            );

            listPolyline.add(polyline);
        }

        if(onPut){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listLatLng.get(listLatLng.size()-1), 30));
        }else {
            if(listLatLng.size() > 0 && moveCamera){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listLatLng.get(listLatLng.size()-1), 30));
                moveCamera = false;
            }
        }

    }

    // Get position
    private void getPosition(int selectNode){

        APIGPSGroupInterface.Factory.getInstance().getPosition(String.valueOf(selectNode)).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.isSuccessful()){
                    ResponseModel responseModel = response.body();
                    dataModels.addAll(responseModel.getData());
                    processListDataModel();
                    if(dataModels.size() > 0){
                        paintMarker();
                    }else {
                        Toast.makeText(
                                context,
                                "Waiting for location tracking",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }else {
                    Toast.makeText(
                            context,
                            "Get node error: "+response.errorBody(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(
                        context,
                        "Get node error: "+t.getLocalizedMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void paintMarker(){
        removeAllMakers();
        removeAllPolylines();

        for (DataModel item : dataModels){
            LatLng latLng = new LatLng(item.getLocation().get(0),item.getLocation().get(1));
            listLatLng.add(latLng);
            if(latLng != null ){
                int nodeId = Integer.parseInt(item.getNode());
                Marker marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .title("Time stamp: "+ DateTimeManager.getTimeFromTimeStamp(item.getTimestamp())+" Node: "+item.getNode())
                                .icon(BitmapDescriptorFactory.fromResource(MarkerManager.getImages(nodeId)))
                );
                marker.setTag(item.getId());
                listMaker.add(marker);

            }
        }
        int color = PolylineManager.getColors(selectNode);
        Polyline polyline = mMap.addPolyline(
                new PolylineOptions()
                        .addAll(listLatLng)
                        .width(20)
                        .visible(true)
                        .color(color)
        );

        listPolyline.add(polyline);

        if(onPut){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listLatLng.get(listLatLng.size()-1), 30));
        }else {
            if(listLatLng.size() > 0 && moveCamera){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listLatLng.get(listLatLng.size()-1), 30));
                moveCamera = false;
            }
        }

        removeRefuses();
    }

    public void removeRefuses(){
        dataModels.clear();
        listDataModels.clear();
        listLatLng.clear();
        listIdNode.clear();
    }

    // Remove maker, clear list
    public void removeAllMakers(){
        if(listMaker.size() > 0){
            for(Marker item: listMaker){
                item.remove();
            }

        }
    }

    public void removeAllPolylines(){
        if(listPolyline.size() > 0){
            for(Polyline item: listPolyline){
                item.remove();
            }

        }
    }

    private void processListDataModel(){
        for(int i = 0; i < dataModels.size(); i++){
            if(dataModels.get(i).getLocation().get(0) == 1000 && dataModels.get(i).getLocation().get(1) == 1000){
                dataModels.remove(i);
            }
            if(dataModels.get(i).getLocation().get(0) == 0 && dataModels.get(i).getLocation().get(1) == 0){
                dataModels.remove(i);
            }
        }
    }

  @Override
    public void onPoiClick(PointOfInterest pointOfInterest) {
        Toast.makeText(getActivity(), "Clicked: " +
                        pointOfInterest.name +
                        "\nPlace ID:" + pointOfInterest.placeId +
                        "\nLatitude:" + pointOfInterest.latLng.latitude +
                        "\nLongitude:" + pointOfInterest.latLng.longitude,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void setOnPut(boolean value){
       this.onPut = value;
    }

    public void setSelectNode(int selectNode){
        this.selectNode = selectNode;
        moveCamera = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        initMapsFragment();
    }

}
