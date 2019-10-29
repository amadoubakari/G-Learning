package com.flys.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flys.R;
import com.flys.dao.entities.Alphabet;

import java.util.List;

public class AlphabetAdapter extends RecyclerView.Adapter<AlphabetAdapter.Holderview> {

    private List<Alphabet> alphabets;
    private Context context;

    public AlphabetAdapter(List<Alphabet> alphabets, Context context) {
        this.alphabets = alphabets;
        this.context = context;
    }

    @NonNull
    @Override
    public Holderview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.alphabet_adapter_item, parent, false);

        return new Holderview(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull Holderview holder, int position) {
        Alphabet alphabet = alphabets.get(position);
        holder.name.setText(alphabet.getName());
    }

    @Override
    public int getItemCount() {
        if (!alphabets.isEmpty()) {
            return alphabets.size();
        }
        return 0;
    }


    class Holderview extends RecyclerView.ViewHolder {

        TextView name;

        public Holderview(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.alphabet_name);
        }
    }

}
