package aahmf.notepad;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NewNoteActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 177, XML_BROWSER=43;
    private ImageButton ExportNote;
    private ImageButton ImportNote;
    private ImageButton CancelNote;
    private Spinner noteClr;
    private static final String[] coloursTwo = {"White", "Green", "Yellow", "Red"};
    int bgColor;
    private EditText WriteNote, Title, Keywords;
    private int id = 1, imageid = 1, fileid = 1;
    private double longitude,latitude;
    private Address currentAdress;


    private String NoteTitle, Date;
    private List<Uri> filePaths;
    private final int File_Request_Code = 2;

    private DatabaseReference mNotes = FirebaseDatabase.getInstance().getReference("Notes");
    private DatabaseReference mImages = FirebaseDatabase.getInstance().getReference("Images");
    private DatabaseReference mFiles = FirebaseDatabase.getInstance().getReference("Files");
    private FirebaseUser user = LogInActivity.getUser();
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mLastLocation;
    private boolean mLocationPermissionGranted =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        filePaths = new ArrayList<>();
        Gallery.ImagePaths = new ArrayList<>();
        ExportNote = findViewById(R.id.btnExport);
        ImportNote = findViewById(R.id.btnImport);
        CancelNote = findViewById(R.id.btnCancelNewNote);
        noteClr = findViewById(R.id.spNoteClr);
        noteClr.setPrompt("Priority");
        WriteNote = findViewById(R.id.etWriteNote);
        Keywords = findViewById(R.id.etKeywords);
        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date = df.format(calendar.getTime());
        if(mGoogleApiClient==null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if(mGoogleApiClient!=null) {
            mGoogleApiClient.connect();
        }
        mLastLocation = LocationServices.getFusedLocationProviderClient(this);
        incrementCounterNotes();
        incrementCounterImages();
        incrementCounterFiles();

        ImportNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch();
            }
        });



        CancelNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewNoteActivity.this, MainMenuActivity.class));
            }
        });


        ArrayAdapter<String> adapterOne = new ArrayAdapter<>(NewNoteActivity.this, android.R.layout.
                simple_spinner_item, coloursTwo);
        adapterOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        noteClr.setAdapter(adapterOne);
        noteClr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //white
                        bgColor = getResources().getColor(R.color.colorWhite);
                        break;
                    case 1:
                        //green
                        bgColor = getResources().getColor(R.color.colorGreen);
                        break;
                    case 2:
                        //yellow
                        bgColor = getResources().getColor(R.color.colorYellow);
                        break;
                    case 3:
                        //red
                        bgColor = getResources().getColor(R.color.colorRed);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                bgColor = getResources().getColor(R.color.colorWhite);
            }
        });
    }

    private void startSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
               intent.setType("text/xml");
               intent.addCategory(Intent.CATEGORY_OPENABLE);
               startActivityForResult(intent.createChooser(intent, "Select XML File"), XML_BROWSER);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_newnote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (WriteNote.getText().toString().matches("")) {
                    Toast.makeText(NewNoteActivity.this, "Write Something Before Saving", Toast.LENGTH_LONG).show();
                } else {
                    //OFFLINE===================================================================================================
                    AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(NewNoteActivity.this);

                    DialogBuilder.setTitle("Give Note Title");


                    DialogBuilder.setMessage("Give the note title to the textbox to save the note to your phone");
                    Title = new EditText(NewNoteActivity.this);
                    Title.setId(R.integer.title_id);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    Title.setLayoutParams(lp);
                    DialogBuilder.setView(Title);


                    DialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NoteTitle = Title.getText().toString();
                            WriteXml(NoteTitle);
                            //ONLLINE=======================================================================
                            //primary key


                            DatabaseReference key = mNotes.child(String.valueOf(id));
                            DatabaseReference title = key.child("NoteTitle");
                            // DatabaseReference Images = key.child("Images");
                            DatabaseReference Files = key.child("Files");
                            DatabaseReference Kwords = key.child("Keywords");
                            DatabaseReference NoteDate = key.child("DateOfCreation");
                            DatabaseReference Content = key.child("Content");
                            DatabaseReference priority = key.child("Priority");
                            DatabaseReference userid = key.child("UserId");
                            DatabaseReference geolocLan = key.child("GeoLocLan");
                            DatabaseReference geoLocLon = key.child("GeoLocLon");
                            DatabaseReference geoLocAddr = key.child("Address");
                            DatabaseReference useridAndTitle = key.child("UserId_Title");


                            Kwords.setValue(Keywords.getText().toString());
                            NoteDate.setValue(Date);
                            Content.setValue(WriteNote.getText().toString());
                            priority.setValue(bgColor);
                            userid.setValue(user.getUid());
                            title.setValue(NoteTitle);
                            geolocLan.setValue(latitude);
                            geoLocLon.setValue(longitude);
                            geoLocAddr.setValue(currentAdress.getAddressLine(0));
                            useridAndTitle.setValue(user.getUid() + "_" + NoteTitle);


                            for (int i = 0; i < Gallery.ImagePaths.size(); i++) {

                                DatabaseReference imagekey = mImages.child(String.valueOf(imageid));
                                DatabaseReference path = imagekey.child("path");
                                DatabaseReference NoteId = imagekey.child("note_id");
                                path.setValue(Gallery.ImagePaths.get(i).toString());
                                NoteId.setValue(id);
                                imageid++;


                            }
                            for (int i = 0; i < filePaths.size(); i++) {

                                DatabaseReference filekey = mFiles.child(String.valueOf(fileid));
                                DatabaseReference path = filekey.child("path");
                                DatabaseReference NoteId = filekey.child("note_id");
                                path.setValue(filePaths.get(i).toString());
                                NoteId.setValue(id);
                                fileid++;
                            }

                            //==============================================================================
                            Gallery.ImagePaths = new ArrayList<>();
                        }

                    });

                    DialogBuilder.create();
                    DialogBuilder.show();
                    //==================================================================================

                }
                break;

            case R.id.addfile:
                Intent intent = new Intent(NewNoteActivity.this, FileGallery.class);
                for (int z = 0; z < filePaths.size(); z++) {
                    intent.putExtra("File" + z, filePaths.get(z).toString());
                }
                startActivityForResult(intent, File_Request_Code);
                break;
            case R.id.image:
                startActivity(new Intent(NewNoteActivity.this, Gallery.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getDate() {
        return Date;
    }

    public void WriteXml(String xmlFile) {
        String NoteText = WriteNote.getText().toString();

        try {
            FileOutputStream fileos = getApplicationContext().openFileOutput(xmlFile, Context.MODE_PRIVATE);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "userData");
            xmlSerializer.startTag(null, "Text");
            xmlSerializer.text(NoteText);
            xmlSerializer.endTag(null, "Text");
            for (int i = 0; i < Gallery.ImagePaths.size(); i++) {
                xmlSerializer.startTag(null, "Image" + i);
                xmlSerializer.text(Gallery.ImagePaths.get(i).toString());
                xmlSerializer.endTag(null, "Image" + i);
            }
            for (int i = 0; i < filePaths.size(); i++) {
                xmlSerializer.startTag(null, "File" + i);
                xmlSerializer.text(filePaths.get(i).toString());
                xmlSerializer.endTag(null, "File" + i);
            }
            xmlSerializer.startTag(null, "keywords");
            String kwords = Keywords.getText().toString();
            xmlSerializer.text(kwords);
            xmlSerializer.endTag(null, "keywords");
            xmlSerializer.startTag(null, "Date");
            xmlSerializer.text(Date);
            xmlSerializer.endTag(null, "Date");
            xmlSerializer.endTag(null, "userData");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileos.write(dataWrite.getBytes());
            fileos.close();
            SharedPreferences mSharedPref = getSharedPreferences("NoteColor", MODE_PRIVATE);
            SharedPreferences.Editor mEditor = mSharedPref.edit();
            mEditor.putInt(xmlFile, bgColor);
            mEditor.apply();
            Toast.makeText(NewNoteActivity.this, "Note Saved In your phone", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(NewNoteActivity.this, MainMenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == File_Request_Code) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                int z = 0;
                Uri temp;
                Bundle extras = data.getExtras();
                if (extras != null) {
                    while (extras.containsKey("File" + z)) {
                        temp = Uri.parse(extras.getString("File" + z));
                        if (!filePaths.contains(temp)) {
                            filePaths.add(temp);
                        }
                        z = z + 1;
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void incrementCounterNotes() {

        mNotes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int size = (int) dataSnapshot.getChildrenCount();
                for (int i = 1; i <= size; i++) {

                    if (dataSnapshot.hasChild(String.valueOf(id))) {
                        id++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void incrementCounterImages() {


        Query query = mImages.orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        imageid = Integer.parseInt(child.getKey());
                        imageid++;
                    }
                } else return;
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void incrementCounterFiles() {

        Query query = mFiles.orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        fileid = Integer.parseInt(child.getKey());
                        fileid++;
                    }
                } else return;
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void getLoc() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(mGoogleApiClient, builder.build());

        getLocationPermission();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task deviceloc = mLastLocation.getLastLocation();
        deviceloc.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
              if(task.isSuccessful())
              {
                  Location location = (Location) task.getResult();
                  latitude = location.getLatitude();
                  longitude = location.getLongitude();
                  currentAdress = getAddress(latitude,longitude);
              }
            }
        });



    }

    public Address getAddress(double latitude, double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getLoc();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
}
