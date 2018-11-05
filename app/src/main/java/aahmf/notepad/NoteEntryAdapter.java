package aahmf.notepad;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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
        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(noteEntry.getImage()));
    }

    @Override
    public int getItemCount() {
        return noteEntryList.size();
    }

    class NoteEntryViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

     public NoteEntryViewHolder(View itemView) {
         super(itemView);
         imageView = itemView.findViewById((R.id.imageView));
         textView = itemView.findViewById(R.id.textView);
     }
 }



}
