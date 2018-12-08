package aahmf.notepad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/*
*RecyclerView.Adapter
* RecyclerView.ViewHolder
 */
public class NoteEntryAdapter extends RecyclerView.Adapter<NoteEntryAdapter.NoteEntryViewHolder> {

    private Context mCtx;
    private List<NoteEntry> noteEntryList1;
    private static boolean isSelected=false;
    protected static List<NoteEntry> deleteList1 = new ArrayList<>();
    private List<Integer> positionList = new ArrayList<>();
    private static String date,keywords;

    static  String Title;
    private static int id;
    private List<CheckBox> checkBox2 = new ArrayList<>();
    private List<Button> Edit2 = new ArrayList<>();

    MainMenuActivity main = new MainMenuActivity();
    String path = main.getPath();



    public NoteEntryAdapter(Context mCtx, List<NoteEntry> noteEntryList) {
        this.mCtx = mCtx;
        this.noteEntryList1 = noteEntryList;
    }

    @Override
    public NoteEntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.main_menu_layout, null);
        return new NoteEntryViewHolder(view);
    }





    @Override
    public void onBindViewHolder(NoteEntryViewHolder holder, int position) {
        final NoteEntry noteEntry = noteEntryList1.get(position);
        holder.Keywords.setText(noteEntry.getKwords());
        holder.Location.setText(noteEntry.getLocation());
        holder.Date.setText(noteEntry.getDate());
        holder.textView.setText(noteEntry.getTitle());
        noteEntry.setCheckBox(holder.selectedNote);
        holder.selectedNote=noteEntry.getCheckBox();
        holder.selectedNote.setOnCheckedChangeListener(null);
        holder.selectedNote.setChecked(noteEntry.isSelected());
        holder.selectedNote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                noteEntry.setSelected(isChecked);
            }
        });
        noteEntry.setEditButton(holder.EditNote);
        SharedPreferences mSharedPref = mCtx.getSharedPreferences("NoteColor", MODE_PRIVATE);
        int bgColor =mSharedPref.getInt(noteEntry.getTitle(),mCtx.getResources().getColor(R.color.colorWhite));
        holder.Color.setBackgroundColor(bgColor);



    }

    @Override
    public int getItemCount() {
        return noteEntryList1.size();
    }





     class NoteEntryViewHolder extends RecyclerView.ViewHolder{
        TextView textView,Date,Keywords, Color,Location;
        CardView cardView;
         CheckBox selectedNote;
         Button EditNote;
        MainMenuActivity main1 = new MainMenuActivity();
        int position;





     public NoteEntryViewHolder(View itemView) {
         super(itemView);


         textView = itemView.findViewById(R.id.textView);
         Date = itemView.findViewById(R.id.Date);
         Location = itemView.findViewById(R.id.Location_Text);
         Keywords = itemView.findViewById(R.id.Keywords);
         cardView = itemView.findViewById(R.id.card_view);
         Color = itemView.findViewById(R.id.tvColor);

           EditNote = itemView.findViewById(R.id.EditButton);

         selectedNote = itemView.findViewById(R.id.selelctedNote);




                     selectedNote.setChecked(false);
                     selectedNote.setVisibility(View.INVISIBLE);
                     EditNote.setVisibility(View.VISIBLE);








         EditNote.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //main.LoadFiles(path,noteEntryList1);
                 position = getAdapterPosition();
                 NoteEntry NE = noteEntryList1.get(position);
                 date = NE.getDate();
                 keywords = NE.getKwords();
                 Title=main.findNoteTitle(position,noteEntryList1);
                 GalleryEdit.ImagePaths2.clear();
                 mCtx.startActivity(new Intent(MainMenuActivity.class.cast(mCtx),EditNoteActivity.class));
             }
         });
         cardView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(isSelected==true)
                 {
                     position = getAdapterPosition();
                     positionList.add(position);
                     NoteEntry NE = noteEntryList1.get(position);
                     deleteList1.add(NE);
                     main1.setDeleteList(deleteList1);
                     main.SetCheckBoxtrue(isSelected);
                     selectedNote.setChecked(true);
                     selectedNote.setVisibility(View.VISIBLE);
                     EditNote.setVisibility(View.INVISIBLE);
                     checkBox2.add(selectedNote);
                     Edit2.add(EditNote);
                 }
                 else {
                     position = getAdapterPosition();
                     NoteEntry NE = noteEntryList1.get(position);
                     id = NE.getId();
                     Title = main.findNoteTitle(position, noteEntryList1);
                     mCtx.startActivity(new Intent(MainMenuActivity.class.cast(mCtx), ViewNoteActivity.class));
                 }



             }
         });




         cardView.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View v) {

                 position = getAdapterPosition();
                 positionList.add(position);
                 NoteEntry NE = noteEntryList1.get(position);
                 deleteList1.add(NE);
                 main1.setDeleteList(deleteList1);
                     isSelected = true;
                     selectedNote.setChecked(true);
                     selectedNote.setVisibility(View.VISIBLE);
                     main.SetCheckBoxtrue(isSelected);
                     EditNote.setVisibility(View.INVISIBLE);
                     checkBox2.add(selectedNote);
                     Edit2.add(EditNote);
                     return true;
             }
         });

     }



     }



    public void filterList(ArrayList<NoteEntry> filteredList) {
        noteEntryList1 = filteredList;
        notifyDataSetChanged();
    }
    public static boolean getSelected()
    {
        return isSelected;
    }

    public static void setIsSelected(boolean isSelected) {
        NoteEntryAdapter.isSelected = isSelected;
    }

    public static String getTitle() {
        return Title;
    }
    public static void setTitle(String title)
    {
        Title = title;
    }

    public static int getId() {
        return id;
    }

    public static String getDate() {
        return date;
    }

    public static void setDate(String date) {
        NoteEntryAdapter.date = date;
    }

    public static String getKeywords() {
        return keywords;
    }

    public static void setKeywords(String keywords) {
        NoteEntryAdapter.keywords = keywords;
    }
    public List<Integer> getPositionList()
    {
        return positionList;
    }

    public List<CheckBox> getCheck()
    {
        return checkBox2;
    }

    public List<Button> getEdit()
    {
        return Edit2;
    }


}
