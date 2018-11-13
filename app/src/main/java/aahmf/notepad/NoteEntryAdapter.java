package aahmf.notepad;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private List<NoteEntry> noteEntryList;



    public NoteEntryAdapter(Context mCtx, List<NoteEntry> noteEntryList) {
        this.mCtx = mCtx;
        this.noteEntryList = noteEntryList;
    }

    @Override
    public NoteEntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.main_menu_layout, null);
        return new NoteEntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteEntryViewHolder holder, int position) {
        NoteEntry noteEntry = noteEntryList.get(position);
        holder.textView.setText(noteEntry.getTitle());
        SharedPreferences mSharedPref = mCtx.getSharedPreferences("Format", MODE_PRIVATE);
        int txtColor =mSharedPref.getInt("Fcolor",mCtx.getResources().getColor(R.color.colorPrimary));
        String fontFam = mSharedPref.getString("font_path","fallingSkyOne.otf");
        Typeface face = Typeface.createFromAsset(mCtx.getAssets(),fontFam);
        int bgColor =mSharedPref.getInt("Bgcolor",mCtx.getColor(R.color.colorWhite));
        holder.cardView.setCardBackgroundColor(bgColor);
        holder.textView.setTextColor(txtColor);
        holder.textView.setTypeface(face);
        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(noteEntry.getImage()));
    }

    @Override
    public int getItemCount() {
        return noteEntryList.size();
    }

    public void  filterList(ArrayList<NoteEntry> filteredList){
        noteEntryList=filteredList;
        notifyDataSetChanged();
    }

    class NoteEntryViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        CardView cardView;

     public NoteEntryViewHolder(View itemView) {
         super(itemView);
         imageView = itemView.findViewById((R.id.imageView));
         textView = itemView.findViewById(R.id.textView);
         cardView = itemView.findViewById(R.id.card_view);
     }
 }



}
