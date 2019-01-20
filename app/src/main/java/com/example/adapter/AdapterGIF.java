package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.item.ItemGIF;
import com.example.util.Constant;
import com.example.util.DBHelper;
import com.example.util.JsonUtils;
import com.squareup.picasso.Picasso;
import com.googgler.wallpaper.R;
import com.googgler.wallpaper.SlideImageActivityGIF;

import java.util.ArrayList;


public class AdapterGIF extends RecyclerView.Adapter{

    private DBHelper dbHelper;
    private ArrayList<ItemGIF> list;
    private Context context;
    private JsonUtils utils;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView_fav;
        private ImageView imageView_gif;
        private TextView textView_totviews;

        private MyViewHolder(View view) {
            super(view);
            imageView_gif = (ImageView) view.findViewById(R.id.imageView_gifitem);
            textView_totviews = (TextView) view.findViewById(R.id.textView_totviews);
            imageView_fav = (ImageView) view.findViewById(R.id.imageView_favourite);
        }
    }

//    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
//        private static ProgressBar progressBar;
//
//        private ProgressViewHolder(View v) {
//            super(v);
//            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
//        }
//    }

    public AdapterGIF(Context context, ArrayList<ItemGIF> list) {
        this.list = list;
        this.context = context;
        dbHelper = new DBHelper(context);

        utils = new JsonUtils(context);

        Resources r = context.getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,Constant.GRID_PADDING, r.getDisplayMetrics());
        Constant.columnWidth = (int) ((utils.getScreenWidth() - ((Constant.NUM_OF_COLUMNS + 1) * padding)) / Constant.NUM_OF_COLUMNS);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_gif, parent, false);

            return new MyViewHolder(itemView);
//        } else {
//            View v = LayoutInflater.from(parent.getContext()).inflate(
//                    R.layout.layout_progressbar, parent, false);
//
//            return new ProgressViewHolder(v);
//        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyViewHolder) {
            FirstFav(position, ((MyViewHolder) holder).imageView_fav);

            ((MyViewHolder) holder).imageView_gif.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ((MyViewHolder) holder).imageView_gif.setLayoutParams(new RelativeLayout.LayoutParams(Constant.columnWidth, Constant.columnWidth));
            ((MyViewHolder) holder).textView_totviews.setText(list.get(position).getTotalViews());

            Picasso.with(context)
                    .load(list.get(position).getImage().replace(" ", "%20"))
                    .placeholder(R.mipmap.placeholder)
                    .into(((MyViewHolder) holder).imageView_gif);


            ((MyViewHolder) holder).imageView_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String image_id = list.get(position).getId();

                    ArrayList<ItemGIF> pojolist = dbHelper.getFavRowGIF(image_id);
                    if (pojolist.size() == 0) {
                        AddtoFav(position);//if size is zero i.e means that record not in database show add to favorite
                        ((MyViewHolder) holder).imageView_fav.setImageDrawable(context.getResources().getDrawable(R.mipmap.fav_ind_active));
                    } else {
                        if (pojolist.get(0).getId().equals(image_id)) {
                            RemoveFav(position);
                            ((MyViewHolder) holder).imageView_fav.setImageDrawable(context.getResources().getDrawable(R.mipmap.fav_ind));
                        }
                    }
                }
            });


            ((MyViewHolder) holder).imageView_gif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Constant.arrayListGIF.clear();
                    Constant.arrayListGIF.addAll(list);
                    Intent intslider = new Intent(context, SlideImageActivityGIF.class);
                    intslider.putExtra("POSITION_ID", position);

                    context.startActivity(intslider);
                }
            });
        }
//        else {
//            if(getItemCount()==1) {
//                ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
//                ((ProgressViewHolder) holder).progressBar.setVisibility(View.GONE);
//            }
//        }
    }

    @Override
    public int getItemCount() {
//        if(Constant.isFav) {
            return list.size();
//        } else {
//            return list.size() + 1;
//        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void FirstFav(int pos, ImageView imageView)
    {
        ArrayList<ItemGIF> pojolist = dbHelper.getFavRowGIF(list.get(pos).getId());
        if(pojolist.size()==0) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.fav_ind));
        }
        else {
            imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.fav_ind_active));

        }
    }

    public void RemoveFav(int position)
    {
        dbHelper.removeFavGIF(list.get(position).getId());
        Toast.makeText(context, context.getResources().getString(R.string.removed_fav), Toast.LENGTH_SHORT).show();
    }

    public void AddtoFav(int position)
    {
        dbHelper.addtoFavoriteGIF(list.get(position));
        Toast.makeText(context, context.getResources().getString(R.string.added_fav), Toast.LENGTH_SHORT).show();
    }

//    public void hideHeader() {
//        ProgressViewHolder.progressBar.setVisibility(View.GONE);
//    }

    public boolean isHeader(int position) {
        return position == list.size();
    }

    @Override
    public int getItemViewType(int position) {
//        return isHeader(position) ? VIEW_PROG : VIEW_ITEM;
        return VIEW_ITEM;
    }
}