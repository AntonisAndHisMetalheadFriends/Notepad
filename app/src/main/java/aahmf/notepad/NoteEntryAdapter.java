package aahmf.notepad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    static  String Title;
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
        NoteEntry noteEntry = noteEntryList1.get(position);
        holder.textView.setText(noteEntry.getTitle());
        SharedPreferences mSharedPref = mCtx.getSharedPreferences("NoteColor", MODE_PRIVATE);
        int bgColor =mSharedPref.getInt(noteEntry.getTitle(),mCtx.getResources().getColor(R.color.colorWhite));
        holder.cardView.setCardBackgroundColor(bgColor);

    }

    @Override
    public int getItemCount() {
        return noteEntryList1.size();
    }



    class NoteEntryViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        CardView cardView;
        int position;

     public NoteEntryViewHolder(View itemView) {
         super(itemView);
         textView = itemView.findViewById(R.id.textView);
         cardView = itemView.findViewById(R.id.card_view);
         Button EditNote = itemView.findViewById(R.id.EditButton);


         EditNote.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 main.LoadFiles(path,noteEntryList1);
                 position = getAdapterPosition();
                 Title=main.findNoteTitle(position,noteEntryList1);
                 mCtx.startActivity(new Intent(MainMenuActivity.class.cast(mCtx),EditNoteActivity.class));
             }
         });
         cardView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 main.LoadFiles(path,noteEntryList1);
                 position = getAdapterPosition();
                 Title=main.findNoteTitle(position,noteEntryList1);
                 mCtx.startActivity(new Intent(MainMenuActivity.class.cast(mCtx),ViewNoteActivity.class));



             }
         });

     }

 }


    public void filterList(ArrayList<NoteEntry> filteredList) {
        noteEntryList1 = filteredList;
        notifyDataSetChanged();
    }

    public static String getTitle() {
        return Title;
    }

}
