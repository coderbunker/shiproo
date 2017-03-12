package com.coderbunker.hyperledger.route;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coderbunker.hyperledger.App;
import com.coderbunker.hyperledger.R;

import java.util.ArrayList;
import java.util.List;


public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    private List<Route> model = new ArrayList<>();

    public interface IListener {
        void onClick(Route route);
    }

    private IListener listener;

    public void setListener(IListener listener) {
        this.listener = listener;
    }

    public void updateModel(List<Route> data) {
        Log.d(App.TAG, "Update model");
        data.add(new Route()); // add one more item to be able to see the last item
        this.model = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Route route = model.get(position);
        holder.cost.setText(route.getCost());
        holder.time.setText(route.getTime());
        holder.route.setText(RouteService.getRoute(route.getCheckpoints()));
        Log.d(App.TAG, "onBindViewHolder call");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) {
                    return;
                }

                listener.onClick(route);
            }
        });
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView route;
        private TextView time;
        private TextView cost;

        public ViewHolder(View itemView) {
            super(itemView);
            route = (TextView) itemView.findViewById(R.id.route_path);
            time = (TextView) itemView.findViewById(R.id.time);
            cost = (TextView) itemView.findViewById(R.id.cost);
        }
    }

}
