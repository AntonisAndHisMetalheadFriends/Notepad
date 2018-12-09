package aahmf.notepad;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GeoLocationOfNotesActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseUser user = LogInActivity.getUser();
    private String  uid = user.getUid();
    private List<Double> longi,lat;
    private List<String> Title,Content;
    private DatabaseReference mNotes = FirebaseDatabase.getInstance().getReference("Notes");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_location_of_notes);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        longi = new ArrayList<>();
        lat = new ArrayList<>();
        Title = new ArrayList<>();
        Content = new ArrayList<>();
        GetNotes(uid);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        // Add a marker in Sydney and move the camera


    }

    public void GetNotes(final String uid)
    {
        Query query = mNotes.orderByChild("UserId").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot child:dataSnapshot.getChildren())
                    {
                        lat.add(Double.parseDouble(child.child("GeoLocLan").getValue().toString()));
                        longi.add(Double.parseDouble(child.child("GeoLocLon").getValue().toString()));
                        Title.add(child.child("NoteTitle").getValue().toString());
                        Content.add(child.child("Content").getValue().toString());

                    }

                    for(int i=0;i<longi.size();i++)
                    {
                        LatLng positionOfNote = new LatLng(lat.get(i),longi.get(i));
                      Marker Note = mMap.addMarker(new MarkerOptions().position(positionOfNote).title(Title.get(i)).snippet("Content :"+Content.get(i)));
                      Note.setTag(0);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(positionOfNote));
                    }
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            NoteEntryAdapter.setTitle(marker.getTitle());
                            startActivity(new Intent(GeoLocationOfNotesActivity.this,ViewNoteActivity.class));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        GetNotes(uid);
    }


}
