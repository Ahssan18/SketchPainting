package com.example.sketchpainting.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sketchpainting.Activities.SketchFillActivity;
import com.example.sketchpainting.Activities.SketchesDataActivity;
import com.example.sketchpainting.Activities.TouchFillActivity;
import com.example.sketchpainting.Models.ModelSketch;
import com.example.sketchpainting.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterSketches extends RecyclerView.Adapter<AdapterSketches.SketchView> {
    private Context context;
    public List<ModelSketch> sketchList;

    public AdapterSketches(Context context, List<ModelSketch> modelSketchList) {
        this.context = context;
        this.sketchList = modelSketchList;
    }

    @NonNull
    @Override
    public SketchView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_sketches,parent,false);
        return new SketchView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SketchView holder, final int position) {

        holder.iv_sketches.setImageResource(sketchList.get(position).getImage());
        holder.linear_sketch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(SketchesDataActivity.ref.equalsIgnoreCase("touchfill"))
                {
                    Intent intent=new Intent(context, TouchFillActivity.class);
                    intent.putExtra("pos",position);
                    intent.putExtra("method","floodfill");
                    context.startActivity(intent);
                }else
                {
                    Intent intent=new Intent(context, SketchFillActivity.class);
                    intent.putExtra("pos",position);
                    intent.putExtra("method","sketchfill");
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return sketchList.size();
    }

    public class SketchView extends RecyclerView.ViewHolder{

        private ImageView iv_sketches;
        private LinearLayout linear_sketch;
        public SketchView(@NonNull View itemView) {
            super(itemView);
            iv_sketches=itemView.findViewById(R.id.iv_sketch);
            linear_sketch=itemView.findViewById(R.id.linear_sketch);
        }
    }
}
