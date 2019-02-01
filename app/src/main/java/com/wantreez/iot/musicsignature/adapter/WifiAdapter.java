//package com.wantreez.iot.musicsignature.adapter;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.SharedPreferences;
//import android.net.wifi.WifiManager;
//import android.preference.PreferenceManager;
//import android.support.v7.app.AlertDialog;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
////import com.warntreez.demo.MainActivity;
////import com.warntreez.demo.R;
////import com.warntreez.demo.helper.SignalColor;
////import com.warntreez.demo.model.WirelessNetwork;
//
//import com.github.lzyzsd.circleprogress.DonutProgress;
//import com.wantreez.iot.musicsignature.MainActivity;
//import com.wantreez.iot.musicsignature.R;
//import com.wantreez.iot.musicsignature.helper.SignalColor;
//import com.wantreez.iot.musicsignature.model.WirelessNetwork;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//
//public class WifiAdapter extends BaseAdapter {
//
//    private List<WirelessNetwork> networkList = new ArrayList<>();
//    private SharedPreferences sharedPreferences;
//    private Set<String> pinnedNetworks;
//
//    public List<WirelessNetwork> getNetworkList() {
//        return networkList;
//    }
//
//    public void setNetworkList(List<WirelessNetwork> networkList) {
//        this.networkList = networkList;
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getCount() {
//        return networkList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return networkList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(parent.getContext());
//        pinnedNetworks = sharedPreferences.getStringSet("pinned_networks", new HashSet<String>());
//
//        LayoutInflater inflater = (LayoutInflater)
//                parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        if(convertView == null) {
//            convertView = inflater.inflate(R.layout.wifi_row, null);
//        }
//
//        TextView ssid               = (TextView) convertView.findViewById(R.id.network_ssid);
//        TextView bssid              = (TextView) convertView.findViewById(R.id.network_bssid);
//        TextView signal             = (TextView) convertView.findViewById(R.id.network_signal);
//        TextView channel            = (TextView) convertView.findViewById(R.id.network_channel);
//        ImageView cap_badge_ess     = (ImageView) convertView.findViewById(R.id.cap_badge_ess);
//        ImageView cap_badge_crypto  = (ImageView) convertView.findViewById(R.id.cap_badge_crypto);
//        ImageView cap_badge_wps     = (ImageView) convertView.findViewById(R.id.cap_badge_wps);
//        final Button btn_stick       = (Button) convertView.findViewById(R.id.network_btn_stick);
//        DonutProgress signalDonutProgress   = (DonutProgress)
//                convertView.findViewById(R.id.network_donut_progress);
//
//
//        final WirelessNetwork network = networkList.get(position);
//        ssid.setText(network.getSsid());
//        bssid.setText(network.getBssid());
//        signal.setText(network.getSignal() + " dBm");
//        signal.setTextColor(SignalColor.getColor(network.getSignal()));
//        String channelTranslation = convertView.getResources().getString(R.string.network_channel);
//        channel.setText(channelTranslation + ": " + network.getChannel());
//
//        /* Set donut circle signal strength */
//        if(network.getSignal() != 0) {
//            signalDonutProgress.setProgress(WifiManager.calculateSignalLevel(network.getSignal(), 100) + 1);
//            signalDonutProgress.setTextColor(signalDonutProgress.getFinishedStrokeColor());
//        } else {
//            signalDonutProgress.setProgress(0);
//            signalDonutProgress.setTextColor(signalDonutProgress.getUnfinishedStrokeColor());
//        }
//
//        /* Check ESS */
//        if(network.getSecurity().contains("ESS")) {
//            cap_badge_ess.setVisibility(View.VISIBLE);
//        } else {
//            cap_badge_ess.setVisibility(View.INVISIBLE);
//        }
//
//        /* Check cryptography */
//        if(network.getSecurity().contains("WPA2-")) {
//            cap_badge_crypto.setImageResource(R.mipmap.cap_badge_wpa2);
//        } else if (network.getSecurity().contains("WPA-")) {
//            cap_badge_crypto.setImageResource(R.mipmap.cap_badge_wpa);
//        } else if(network.getSecurity().contains("WEP")) {
//            cap_badge_crypto.setImageResource(R.mipmap.cap_badge_wep);
//        } else {
//            cap_badge_crypto.setImageResource(R.mipmap.cap_badge_open);
//        }
//
//        /* Check WPS */
//        if(network.getSecurity().contains("WPS")) {
//            cap_badge_wps.setVisibility(View.VISIBLE);
//        } else {
//            cap_badge_wps.setVisibility(View.INVISIBLE);
//        }
//
////        /* Update pinned/unpinned graphic */
////        if(pinnedNetworks.contains(network.toString())) {
////            btn_stick.setImageResource(R.drawable.ic_pin_pinned);
////        } else {
////            btn_stick.setImageResource(R.drawable.ic_pin_unpinned);
////        }
//
//
//        btn_stick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick( View v) {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//
//
//               // if (network.getSecurity().contains("WPA")) {
////                    Toast.makeText(v.getContext()," Connecting "+ network.getSsid(),Toast.LENGTH_SHORT).show();
//                if(network.getSecurity().contains("WPA2-") || (network.getSecurity().contains("WPA-")) || (network.getSecurity().contains("WEP")) ) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                    builder.setTitle("");
//                    View viewInflated = LayoutInflater.from(v.getContext()).inflate(R.layout.text_input, (ViewGroup) v.getParent(), false);
//                    final EditText input = (EditText) viewInflated.findViewById(R.id.input);
//                    builder.setView(viewInflated);
//
//                    // Set up the buttons
//                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
////                        m_Text = input.getText().toString();
//                            if(  network.getSecurity().contains("WPA2-")|| (network.getSecurity().contains("WPA-"))   )
//                                MainActivity.bt.send("wifi_connect:" + network.getSsid() + ":WPA:" + input.getText(), true);
//                            else  if( (network.getSecurity().contains("WEP")) )
//                                MainActivity.bt.send("wifi_connect:" + network.getSsid() + ":WEP:" + input.getText(), true);
//                        }
//                    });
//
//                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//
//                    builder.show();
//                } else {
//                    MainActivity.bt.send("wifi_connect:" + network.getSsid() + ":OPEN:" + "", true);
//                }
//            }
//        });
//        return convertView;
//    }
//}
