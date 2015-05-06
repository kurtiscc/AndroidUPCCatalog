package com.example.kurtiscc.upccatalog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public ViewPager mViewPager;
    public static String productName;
    public static String image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return GetUPCFragment.newInstance(position + 1, MainActivity.this);
                case 1:
                    return ViewUPCFragment.newInstance(position + 1, MainActivity.this);

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);

            }
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        Log.v("ScanResult: ", scanResult.toString());
        if (scanResult != null) {
            String contents = scanResult.getContents();

            GetUPCFragment.displayUPC(contents.toString());

            Log.v("UPC Code", contents.toString());
            GetUPCFragment.select(contents.toString());
        }

    }

    public static String tryHttpGet(String url)
    {
        BufferedReader in = null;
        String page = "";
        try
        {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            // String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null)
            {
                // sb.append(line + NL);
                sb.append(line);
            }
            in.close();
            page = sb.toString();
        }
        catch (Exception e)
        {
            page = e.toString();
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return page;
    }

    public static class jsonTask extends AsyncTask<String, Integer, String>
    {
        protected String doInBackground(String... urls) {
            // urls.length
            return tryHttpGet(urls[0]);
        }

        protected void onPostExecute(String result) {
            GetUPCFragment.parseJSON(result);
        }
    }
/*
    adapter = new CatalogListAdapter(context, R.layout.row_product, dataProducts);
    catalogList.setAdapter(adapter);
    registerForContextMenu(catalogList);
}
}

@Override
public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, R.id.view_details, Menu.NONE, "View Details");
        menu.add(Menu.NONE, R.id.delete, Menu.NONE, "Delete");
        }


@Override
public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Product product = dataProducts.get(menuInfo.position);

        switch(item.getItemId()) {
        case R.id.view_details:
        showDetailsDialog(product);
        return true;
        case R.id.delete:
        // Deletes the correct product
        adapter.remove(adapter.getItem(menuInfo.position));
        adapter.notifyDataSetChanged();
        db.delete(product);
        return true;
default:
        return super.onContextItemSelected(item);
        }

*/

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class GetUPCFragment extends Fragment {
        private Button btn, btn2;
        private DBHelper mydb;
        private UPCData upcdata;
        static Context context;
        static View rootView;
        static TextView UPCTV;
        static TextView ProductTV;
        static ImageView ImageIV;
        String prod, upccod, imag;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static GetUPCFragment newInstance(int sectionNumber, Context context) {
            GetUPCFragment fragment = new GetUPCFragment(context);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public GetUPCFragment() {}

        public GetUPCFragment(Context context) {
            this.context = context;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
             rootView = inflater.inflate(R.layout.fragment_getupc, container, false);

            UPCTV = (TextView)rootView.findViewById(R.id.UPCTV);
            ProductTV = (TextView)rootView.findViewById(R.id.ProductTV);
            ImageIV  = (ImageView)rootView.findViewById(R.id.iconIV);
            upcdata = new UPCData();
            mydb = new DBHelper(context);

            btn = (Button) rootView.findViewById(R.id.go_button);
            btn2 = (Button) rootView.findViewById(R.id.save_button);

            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentIntegrator integrator = new IntentIntegrator(getActivity());
                    integrator.addExtra("SCAN_WIDTH", 640);
                    integrator.addExtra("SCAN_HEIGHT", 480);
                    integrator.addExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
                    //customize the prompt message before scanning
                    integrator.addExtra("PROMPT_MESSAGE", "Scanner Start!");
                    integrator.initiateScan(IntentIntegrator.PRODUCT_CODE_TYPES);
                }
            });

            btn2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    save();
                }
            });

            return rootView;
        }

        public void save() {
            upcdata.setProductname(ProductTV.getText().toString());
            upcdata.setUpccode(UPCTV.getText().toString());
            upcdata.setImage(ImageIV.getDrawable().toString());

            mydb.insert(upcdata);

            UPCTV.setText("");
            ProductTV.setText("");

            Log.v("Test", mydb.getAllUPCData().toString());
        }
        static void displayUPC(String contents){

            UPCTV.setText(contents.toString());
            Log.v("Sneech", contents.toString());

        }

        static void displayImage() {

            ProductTV.setText(productName);
            Uri uri = Uri.parse(image);

            Picasso.with(context)
                    .load(uri)
                    .resize(200,200)
                    .centerCrop()
                    .into(ImageIV);
        }

        static void select(String contents) {

            String url = "http://www.searchupc.com/handlers/upcsearch.ashx?request_type=3&access_token=DA6BF397-284A-4B11-AE6C-4560F99F47EA&upc=" + contents;
            new jsonTask().execute(url);
        }
        static void parseJSON(String s) {

          try
          {
              Log.v("My String", s);
              JSONObject jsonRoot = new JSONObject(s);
              JSONObject jsonResponse = jsonRoot.getJSONObject("0");

              productName = jsonResponse.get("productname").toString();
              image = jsonResponse.get("imageurl").toString();

              displayImage();



          } catch (Exception e)
          {
              e.printStackTrace();
          }

        }
    }

    public static class ViewUPCFragment extends Fragment {
        private ListView historyList;
        private ArrayList<UPCData> arrayList = new ArrayList<UPCData>();
        private List<UPCData> UPCList;
        private UPCCatalogAdapter adapter;

        private DBHelper mydb;
        private Context context;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ViewUPCFragment newInstance(int sectionNumber, Context context) {
            ViewUPCFragment fragment = new ViewUPCFragment(context);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public ViewUPCFragment() {

        }

        public ViewUPCFragment(Context context) {
            this.context = context;        }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, R.id.view_details, Menu.NONE, "View Details");
        menu.add(Menu.NONE, R.id.delete, Menu.NONE, "Delete");
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        UPCData product = arrayList.get(menuInfo.position);

        switch (item.getItemId()) {
            case R.id.view_details:
                //showDetailsDialog(product);
                return true;
            case R.id.delete:
                // Deletes the correct product
                adapter.remove(adapter.getItem(menuInfo.position));
                adapter.notifyDataSetChanged();
                mydb.delete(product);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
       /*
        public void showDetailsDialog(UPCData product) {
            AlertDialog.Builder productDialog = new AlertDialog.Builder(context);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.product_dialog, null);
            productDialog.setView(convertView);
            productDialog.setTitle(product.getDescription());

            final AlertDialog productsDialog = productDialog.create();

            TextView upc = (TextView) convertView.findViewById(R.id.code_dialog);
            TextView website = (TextView) convertView.findViewById(R.id.webpage_dialog);
            TextView usage = (TextView) convertView.findViewById(R.id.usage_dialog);
            ImageView image = (ImageView) convertView.findViewById(R.id.image_dialog);

            upc.setText(product.getCode());
            website.setText(product.getWebpage());
            usage.setText(product.getUsage());

            Uri uri1 =  Uri.parse(product.getImage());

            Picasso.with(activity)
                    .load(uri1)
                    .resize(300,300)
                    .centerCrop()
                    .into(image);


            productsDialog.show();

        }
        */

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_viewupc, container, false);
            historyList = (ListView) rootView.findViewById(R.id.catalog_list);
            UPCList = new ArrayList<UPCData>();
            mydb = new DBHelper(context);
            return rootView;
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if(isVisibleToUser)
            {
                UPCList.clear();
                arrayList.clear();

                UPCList = mydb.getAllUPCData();

                for(int i = 0; i < UPCList.size(); i++) {
                    arrayList.add(UPCList.get(i));
                }

                adapter = new UPCCatalogAdapter(context, R.layout.fragment_rows_catalog, arrayList);
                historyList.setAdapter(adapter);
                registerForContextMenu(historyList);
            }
        }
    }
}







