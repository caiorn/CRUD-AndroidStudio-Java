package com.example.agendacontatos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Activity activity;
    ArrayList<String> rowid, nomes, telefones;
    int position;

    public CustomAdapter(Activity activity, ArrayList rowid, ArrayList nomes, ArrayList telefones){
        this.activity = activity;
        this.rowid = rowid;
        this.nomes = nomes;
        this.telefones = telefones;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.linha_lista, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        this.position = position;
        holder.txtId.setText(String.valueOf(rowid.get(position)));
        holder.txtNome.setText(String.valueOf(nomes.get(position)));
        holder.txtTelefone.setText(String.valueOf(telefones.get(position)));
        holder.rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ActivityCadastro.class);
                intent.putExtra("nome", String.valueOf(nomes.get(position)));
                intent.putExtra("tele", String.valueOf(telefones.get(position)));

                activity.startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nomes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtId, txtNome, txtTelefone;
        LinearLayout rowLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtId = itemView.findViewById(R.id.txtId);
            txtNome = itemView.findViewById(R.id.txtNome);
            txtTelefone = itemView.findViewById(R.id.txtTelefone);
            rowLayout = itemView.findViewById(R.id.rowLayout);
        }
    }
}
