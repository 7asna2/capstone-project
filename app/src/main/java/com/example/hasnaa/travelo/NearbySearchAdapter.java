package com.example.hasnaa.travelo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hasnaa.travelo.DataRequest.PhotoTask;
import com.example.hasnaa.travelo.data.Contract;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hasnaa on 5/29/2017.
 */
public class NearbySearchAdapter extends RecyclerView.Adapter<NearbySearchAdapter.ViewHolder> {

        private final String TAG = this.getClass().getSimpleName();
        final private OnClickHandler onClickHandler;
        private ArrayList<String> places;
        private Context context;
        private GoogleApiClient mGoogleApiClient;

        LatLng pLatLng;




    public interface OnClickHandler {
            void onClick(String  placeId);
        }

        public NearbySearchAdapter(Context context, ArrayList<String> places,
                                   OnClickHandler onClickHandler , GoogleApiClient mGoogleApiClient) {
            this.context = context;
            this.places = places;
            this.onClickHandler = onClickHandler;
            this.mGoogleApiClient=mGoogleApiClient;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.place_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            String id = places.get(position);
            Places.GeoDataApi.getPlaceById(mGoogleApiClient, id)
                    .setResultCallback(new ResultCallback<PlaceBuffer>() {
                        @Override
                        public void onResult(PlaceBuffer places) {
                            if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                Place place = places.get(0);
                                pLatLng = place.getLatLng();
                                holder.titleView.setText(place.getName());
                                holder.subtitleView.setText(place.getRating()+"");
                                new PhotoTask(500, 500, mGoogleApiClient) {
                                    @Override
                                    protected void onPreExecute() {
                                    }

                                    @Override
                                    protected void onPostExecute(AttributedPhoto attributedPhoto) {
                                        if (attributedPhoto != null) {
                                            holder.thumbnailView.setImageBitmap(attributedPhoto.bitmap);
                                        }
                                    }
                                }.execute(place.getId());
                                Log.i(TAG, "Place found: " + places.get(0).getName());
                            } else {
                                Log.e(TAG, "Place not found");
                            }
                            places.release();
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return places.size();
        }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        //        @BindView(R.id.thumbnail)  DynamicHeightNetworkImageView thumbnailView;
        @BindView(R.id.thumbnail)
        ImageView thumbnailView;

        @BindView(R.id.article_title)
        public TextView titleView;

        @BindView(R.id.article_subtitle)
        public TextView subtitleView;

        @BindView(R.id.map)
        ImageButton mapButton;
        @BindView(R.id.share)
        ImageButton shareButton;

        public int position;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double lat = pLatLng.latitude;
                    double lng = pLatLng.longitude;
                    String uri = String.format("geo:%f,%f",lat, lng);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    context.startActivity(intent);
                }
            });

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double lat = pLatLng.latitude;
                    double lng = pLatLng.longitude;
                    String URL ="https://maps.google.com/?cid="+places.get(getAdapterPosition());
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, URL);
                    context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            });
        }


        @Override
        public void onClick(View v) {
            onClickHandler.onClick(places.get(getAdapterPosition()));
        }
    }
    }

