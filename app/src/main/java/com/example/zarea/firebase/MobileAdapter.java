package com.example.zarea.firebase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Zarea on 27/09/2017.
 */

public class MobileAdapter extends RecyclerView.Adapter<MobileAdapter.MobileHolder> {
    private Context mContext;
    private List<Mobile> names;

    public MobileAdapter(Context mContext, List<Mobile> names) {
        this.mContext = mContext;
        this.names = names;
    }

    @Override
    public MobileAdapter.MobileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mobile, parent, false);

        return new MobileHolder(view);
    }

    @Override
    public void onBindViewHolder(MobileAdapter.MobileHolder holder, final int position) {

        holder.textView.setText(names.get(position).getImage_title());

        Picasso.with(mContext)
                .load(names.get(position).getImage_url())
                .error(R.drawable.photo)
                .transform(new CircleTransform())
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class MobileHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView img;
        LinearLayout layout;

        public MobileHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.root);
            textView = (TextView) itemView.findViewById(R.id.version_name);
            img = (ImageView) itemView.findViewById(R.id.version_picture);
        }
    }
}
