package com.finwingway.digicob;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.finwingway.digicob.leopard.Act_Main;
import com.finwingway.digicob.loanModule.LoanMainFragment;
import com.finwingway.digicob.loanModule.jlgWebView.CustomTabActivityHelper;
import com.finwingway.digicob.loanModule.jlgWebView.LoanMainFragmentWebviewFallback;
import com.finwingway.digicob.sil.BluetoothChat;
import com.finwingway.digicob.support.UI_validation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;
    TextView agent_name_text_view, agent_id_text_view;
    Toolbar toolbar, quick_toolbar;
    FragmentManager fragmentManager;
    NavigationView navigationView;
    FrameLayout frameLayout;
    ImageButton daily_depo_image_btn, new_acc_image_btn, balance__image_btn, recharge_image_btn, daily_report_image_btn;
    Intent serviceIntent;
    ImageView connectivity, navImage;
    BroadcastReceiver receiver;
    public static boolean connectionStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
//        } else {
//        }


        fragmentManager = getSupportFragmentManager();
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        connectivity = findViewById(R.id.toolbar_connectivity_status);
        connectivity.setImageResource(R.drawable.bt_disconnected_icon);
        connectivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, Act_Main.class));
                Intent intent = new Intent(getApplicationContext(), BluetoothChat.class);
                startActivity(intent);
            }
        });
        agent_name_text_view = (TextView) header.findViewById(R.id.agent_name_text_view);
        agent_id_text_view = (TextView) header.findViewById(R.id.agent_id_text_view);
        agent_name_text_view.setText(login.agent_name);
        agent_id_text_view.setText(login.agent_code);
        try {
            navImage = findViewById(R.id.nav_image);
            navImage.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    Log.e("onLongClick: ", "--------");
                    Context context = getApplicationContext();
                    CharSequence text = login.ip_global;
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return true;
                }

            });
        } catch (Exception ignored) {
        }
        setupView();
        if (savedInstanceState == null)
            showHome();

        serviceIntent = new Intent(this, Bluetooth_Service.class);
        startService(serviceIntent);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra(Bluetooth_Service.COPA_MESSAGE);
                //Toast.makeText(context, s+"Mainactivity", Toast.LENGTH_SHORT).show();
                if (s.equals("disconnected")) {
                    connectionStatus = false;
                    setbtImage("dis");
//                    mGP.closeConn();
                    //Toast.makeText(context, "if condition disconnected", Toast.LENGTH_SHORT).show();
                }
                if (s.equals("connected")) {
                    connectionStatus = true;
                    setbtImage("con");
                    //Toast.makeText(context, "if condition connected", Toast.LENGTH_SHORT).show();
                }
            }
        };

        if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
            Log.e("trueee: ", "checkAndRequestPermissions");
        }

        //----------------------------------------------------------------------
        setMenuItems();
        //----------------------------------------------------------------------

    }

    private void setbtImage(String status) {
        //============================================================================
//        if (status.equals("dis")) {
//            connectivity.setVisibility(View.VISIBLE);
//            connectivity.setImageResource(R.drawable.bt_disconnected_icon);
//        }
//        if (status.equals("con")) {
//            connectivity.setVisibility(GONE);
//        }
//        //============================================================================
    }

    private void setupView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        quick_toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        daily_depo_image_btn = (ImageButton) findViewById(R.id.toolbar_daily_depo_btn);
        new_acc_image_btn = (ImageButton) findViewById(R.id.toolbar_new_acc_btn);
        balance__image_btn = (ImageButton) findViewById(R.id.toolbar_balance_enquiry_btn);
        recharge_image_btn = (ImageButton) findViewById(R.id.toolbar_recharge_btn);
        daily_report_image_btn = (ImageButton) findViewById(R.id.toolbar_daily_report_btn);

        if (UI_validation.COLLECTION.equals(UI_validation.getUiType())) {
            new_acc_image_btn.setVisibility(View.INVISIBLE);
            recharge_image_btn.setVisibility(View.INVISIBLE);
        }

        daily_depo_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Transactions_Daily_Deposit transactions_cashDepositCustomer = new Transactions_Daily_Deposit();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.content_frame, transactions_cashDepositCustomer);
                ft.commit();

            }
        });
        new_acc_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Accounts_Sub llf = new Accounts_Sub();
                ft.replace(R.id.content_frame, llf);
                ft.commit();
            }
        });
        balance__image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Enquiry_Balance_Enquiry enquiry_balance_enquiry = new Enquiry_Balance_Enquiry();
                ft.replace(R.id.content_frame, enquiry_balance_enquiry);
                ft.commit();
            }
        });
        recharge_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Recharge_Activity enquiry_balance_enquiry = new Recharge_Activity();
                ft.replace(R.id.content_frame, enquiry_balance_enquiry);
                ft.commit();
            }
        });
        daily_report_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                BC_Report_Daily_Report bc_report_daily_report = new BC_Report_Daily_Report();
                ft.replace(R.id.content_frame, bc_report_daily_report);
                ft.commit();
            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // MenuItem item=(MenuItem)findViewById(R.id.action_logout);
        //item.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
    }

    private void showHome() {
        selectDrawerItem(navigationView.getMenu().getItem(0));
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void setMenuItems() {
        Menu menu = navigationView.getMenu();
        if (UI_validation.COLLECTION.equals(UI_validation.getUiType())) {
            MenuItem Mloans = menu.findItem(R.id.loans);
            Mloans.setVisible(false);
            MenuItem Mloansweb = menu.findItem(R.id.loansweb);
            Mloansweb.setVisible(false);
            MenuItem Maccounts = menu.findItem(R.id.accounts);
            Maccounts.setVisible(false);
            MenuItem Mrecharge = menu.findItem(R.id.recharge);
            Mrecharge.setVisible(false);
        }
    }

    private void selectDrawerItem(MenuItem menuItem) {
        //----------------------------------------------------------------------
        setMenuItems();
        //----------------------------------------------------------------------

        boolean specialToolbarBehaviour = false;
        Class fragmentClass = null;

        switch (menuItem.getItemId()) {
            case R.id.transactions:
                fragmentClass = Transactions.class;
                break;

            case R.id.loans:
                fragmentClass = LoanMainFragment.class;
                break;

            case R.id.loansweb:
//                String url = "http://209.126.76.226:5454/Home";
//                String url = "http://35.196.223.10:160";

                String url = "http://www.finwintechnologies.com:160";

//                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
//                CustomTabActivityHelper.openCustomTab(this, customTabsIntent, Uri.parse(url), new LoanMainFragmentWebviewFallback());

                CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
                intentBuilder.setToolbarColor(Color.parseColor("#428BCA"));
                intentBuilder.setSecondaryToolbarColor(Color.parseColor("#428BCA"));
                intentBuilder.enableUrlBarHiding();
                CustomTabActivityHelper.openCustomTab(this, intentBuilder.build(), Uri.parse(url), new LoanMainFragmentWebviewFallback());
                break;

            case R.id.accounts:
                fragmentClass = Accounts.class;
                specialToolbarBehaviour = true;
                break;

            case R.id.enquiry:
                fragmentClass = Enquiry.class;
                break;

            case R.id.bc_report:
                fragmentClass = BC_Report.class;
                break;
            case R.id.password:
                fragmentClass = Agent_Management.class;
                //startActivity(new Intent(this,));
                break;

            case R.id.recharge:
                fragmentClass = Recharge_Activity.class;
                //startActivity(new Intent(this,));
                break;

            default:
                fragmentClass = TabFragment.class;
                break;
        }

        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //setToolbarElevation(specialToolbarBehaviour);
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
    }

    /*@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setToolbarElevation(boolean specialToolbarBehaviour) {
        if (specialToolbarBehaviour) {
            toolbar.setElevation(0.0f);
            frameLayout.setElevation(getResources().getDimension(R.dimen.elevation_toolbar));
        } else {
            toolbar.setElevation(getResources().getDimension(R.dimen.elevation_toolbar));
            frameLayout.setElevation(0.0f);
        }
    }*/

   /* public void showSnackbarMessage(View v) {
        EditText et_snackbar = (EditText) findViewById(R.id.et_snackbar);
        TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.textInputLayout);
        View view = findViewById(R.id.coordinator_layout);
        if (et_snackbar.getText().toString().isEmpty()) {
            textInputLayout.setError(getString(R.string.alert_text));
        } else {
            textInputLayout.setErrorEnabled(false);
            et_snackbar.onEditorAction(EditorInfo.IME_ACTION_DONE);
            Snackbar.make(view, et_snackbar.getText().toString(), Snackbar.LENGTH_LONG)
                    .setAction(getResources().getString(android.R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Do nothing
                        }
                    })
                    .show();
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {

            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.logout_rounded)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to Logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            try {
                                /*mGP.closeConn();
                                stopService(serviceIntent);
                                mBT.disable();*/
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //login.connectionStatusLogin=false;
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();

        }
        if (id == R.id.connect_bt_device_menu) {
            if (!connectionStatus) {
                startActivity(new Intent(MainActivity.this, Act_Main.class));
            } else {
                Toast.makeText(this, "Already Connected :)", Toast.LENGTH_SHORT).show();
            }

        }

        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        drawerToggle.syncState();
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        //int count = getSupportFragmentManager().getBackStackEntryCount();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            overridePendingTransition(0, 0);
            super.onBackPressed();
            getFragmentManager().popBackStack();
            imageAdapter.photograph_image_string = "null";
            imageAdapter.id_image_string = "null";
            imageAdapter.address_image_string = "null";
            imageAdapter.signature_image_string = "null";
            imageAdapter.biometric_image_string = "null";
            imageAdapter.biometric_image_string2 = "null";
            imageAdapter.biometric_image_string3 = "null";
            Accounts_QuickOpening.is_biometric_submitted = "0";
            Accounts_QuickOpening.is_id_proof_submitted = "0";
            Accounts_QuickOpening.is_address_proof_submitted = "0";
            Accounts_QuickOpening.is_photograph_submitted = "0";
            Accounts_QuickOpening.is_signature_submitted = "0";
            Accounts_Tab_Fragment_KYC.is_biometric_submitted = "0";
            Accounts_Tab_Fragment_KYC.is_id_proof_submitted = "0";
            Accounts_Tab_Fragment_KYC.is_address_proof_submitted = "0";
            Accounts_Tab_Fragment_KYC.is_photograph_submitted = "0";
            Accounts_Tab_Fragment_KYC.is_signature_submitted = "0";
            Accounts_KYCUpdate.is_biometric_submitted = "0";
            Accounts_KYCUpdate.is_id_proof_submitted = "0";
            Accounts_KYCUpdate.is_address_proof_submitted = "0";
            Accounts_KYCUpdate.is_photograph_submitted = "0";
            Accounts_KYCUpdate.is_signature_submitted = "0";
            //additional code

        } else {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.shutdown)
                    .setTitle("Exit")
                    .setMessage("Are you sure you want to exit DigiCoB?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            clearApplicationData();
                            clearApplicationDataExternal();
                            try {
                                //stopService(serviceIntent);
                                /*mGP.closeConn();
                                mBT.disable();*/
                                //login.connectionStatusLogin=false;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    @Override
    protected void onStart() {
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(Bluetooth_Service.COPA_RESULT));
        super.onStart();
    }

    @Override
    public void onStop() {
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
        /*int secondsDelayed = 10;
        handler.postDelayed(runnable=new Runnable()
         {
            public void run() {l
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
            }
        }, secondsDelayed * 1000);*/
    }

    @Override
    protected void onRestart() {
//        try {
//            showHome();
//        } catch (Exception e) {
//            Log.e("onResume Exce", String.valueOf(e));
//        }
        super.onRestart();
    }

    @Override
    protected void onResume() {
//        try {
//            showHome();
//        } catch (Exception e) {
//            Log.e("onResume Exce", String.valueOf(e));
//        }
        super.onResume();
        //============================================================================
//        if (connectionStatus) {
//            connectivity.setVisibility(GONE);
//        } else {
//            connectivity.setVisibility(View.VISIBLE);
//        }
//        //============================================================================
        //registerReceiver(mServiceUpdatesReceiver, new IntentFilter("blah"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            //clearApplicationData();
            clearApplicationDataExternal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        deleteDir(cache);
        /*File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.e("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }*/
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public void clearApplicationDataExternal() {
        File cache = getExternalCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDirExternal(new File(appDir, s));
                    Log.e("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDirExternal(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirExternal(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 1: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                } else {
//                    Toast.makeText(getBaseContext(), "Camera Permission Denied", Toast.LENGTH_SHORT).show();
//                }
//                return;
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("onRequestPer", "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("onRequestPer", "Camera and Storage permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("onRequestPer", "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                            showDialogOK("Camera and Storage Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            new AlertDialog.Builder(this)
                                    .setMessage("Need Camera and Storage permission, Go to settings and enable it!")
                                    .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                                            intent.setData(uri);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .create()
                                    .show();
//                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
//                            //                            //proceed with logic by disabling the related features or quit the app.

                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

}
