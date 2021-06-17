package com.finwingway.digicob;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.finwingway.digicob.leopard.Act_Main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;
import static com.finwingway.digicob.MainActivity.connectionStatus;

/**
 * Created by Anvin on 12/26/2016.
 */
@SuppressLint("ValidFragment")
public class Accounts_Tab_Fragment_KYC extends Fragment {
    ViewPager viewPager = Accounts_Sub.viewPager;
    private int mPosition;
    ImageButton profImage, signatureImage, biometricImage, proofIDimage, AddressProofImage;
    Uri idProof, addressProof;
    LinearLayout layout;
    String stringUri, stringAddressUri;
    Intent cameraIntent;
    Camera camera = null;
    Bitmap compressedImageBitmap, ompressedImageFile;
    EditText encodedtextView;
    JSONParser jsonParser;
    public static String ip = login.ip_global;
    String encoded, encoded2, encoded3, refId;
    File file;
    SweetAlertDialog alert;
    public static String is_id_proof_submitted = "0", is_address_proof_submitted = "0", is_photograph_submitted = "0",
            is_signature_submitted = "0", is_biometric_submitted = "0";

    public Accounts_Tab_Fragment_KYC(int position) {
        mPosition = position;
    }

    public Accounts_Tab_Fragment_KYC() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View defaultView = inflater.inflate(R.layout.fragment_tab_default, container, false);
//        layout.setOrientation(LinearLayout.VERTICAL);  //Can also be done in xml by android:orientation="vertical"
        if (mPosition == 22) {
            View rootView = inflater.inflate(R.layout.fragment_accountsub_kyc_layout, container, false);
            jsonParser = new JSONParser();
            profImage = (ImageButton) rootView.findViewById(R.id.kyc_applicatsPhotoImagebtn);
            signatureImage = (ImageButton) rootView.findViewById(R.id.kyc_imagebtn_sign);
            biometricImage = (ImageButton) rootView.findViewById(R.id.kyc_imagebtn_biometric);
            proofIDimage = (ImageButton) rootView.findViewById(R.id.kyc_proofIdImagebtn);
            AddressProofImage = (ImageButton) rootView.findViewById(R.id.kyc_proofAddressImagebtn);
            Button next = (Button) rootView.findViewById(R.id.kyc_nextbtn);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(3, true);
                }
            });
            Button prev = (Button) rootView.findViewById(R.id.kyc_prevbtn);
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }
            });

            profImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkAndRequestPermissions()) {
                        if (is_photograph_submitted.equals("1")) {
                            Intent intent = new Intent(getActivity(), Change_kyc_image.class);
                            intent.putExtra("data", "photograph");
                            startActivityForResult(intent, 1001);
                        } else {
                            getPhotograph();
                        }
                    }
                }
            });
            proofIDimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkAndRequestPermissions()) {
                        if (is_id_proof_submitted.equals("1")) {
                            Intent intent = new Intent(getActivity(), Change_kyc_image.class);
                            intent.putExtra("data", "id_proof");
                            startActivityForResult(intent, 1002);
                        } else {
                            getIdProof();
                        }
                    }
                }
            });

            AddressProofImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkAndRequestPermissions()) {
                        if (is_address_proof_submitted.equals("1")) {
                            Intent intent = new Intent(getActivity(), Change_kyc_image.class);
                            intent.putExtra("data", "address_proof");
                            startActivityForResult(intent, 1003);
                        } else {
                            getAddressProof();
                        }
                    }
                }
            });

            signatureImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkAndRequestPermissions()) {
                        if (is_signature_submitted.equals("1")) {
                            Intent intent = new Intent(getActivity(), Change_kyc_image.class);
                            intent.putExtra("data", "signature");
                            startActivityForResult(intent, 1004);
                        } else {
                            getSignature();
                        }
                    }
                }
            });
            biometricImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkAndRequestPermissions()) {
                        if (is_biometric_submitted.equals("1")) {
                            Intent intent = new Intent(getActivity(), Change_kyc_image.class);
                            intent.putExtra("data", "biometric");
                            startActivityForResult(intent, 1005);
                        } else {
                            getBiometric();
                        }
                    }
                }
            });

            //   ActivityCompat.requestPermissions(getActivity(),
            //           new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
            //           1);

            //   ActivityCompat.requestPermissions(getActivity(),
            //           new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
            //           1);

            if (checkAndRequestPermissions()) {
                // carry on the normal flow, as the case of  permissions  granted.
                Log.e("trueee: ", "checkAndRequestPermissions");
            }

            return rootView;
        }
        return defaultView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            try {
                stringUri = data.getStringExtra("cropUriPhotograph");
                idProof = Uri.parse(stringUri);
                file = new File(idProof.getPath());
                compressedImageBitmap = Compressor.getDefault(getContext()).compressToBitmap(file);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byte[] array = byteArrayOutputStream.toByteArray();
                encoded = Base64.encodeToString(array, Base64.DEFAULT);
                imageAdapter.setPhotograph_image_string(encoded);
                Log.e("encoded", Base64.encodeToString(array, Base64.DEFAULT));
                is_photograph_submitted = "1";
                profImage.setImageBitmap(compressedImageBitmap);
            } catch (Exception e) {

            }
        }

        if (requestCode == 200) {
            try {
                stringUri = data.getStringExtra("cropUri");
                idProof = Uri.parse(stringUri);
                file = new File(idProof.getPath());
                compressedImageBitmap = Compressor.getDefault(getContext()).compressToBitmap(file);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byte[] array = byteArrayOutputStream.toByteArray();
                encoded = Base64.encodeToString(array, Base64.DEFAULT);
                imageAdapter.setId_image_string(encoded);
                Log.e("encoded", Base64.encodeToString(array, Base64.DEFAULT));
                is_id_proof_submitted = "1";
                proofIDimage.setImageBitmap(compressedImageBitmap);
            } catch (Exception e) {

            }
        }
        if (requestCode == 201) {
            try {
                stringAddressUri = data.getStringExtra("cropUriAddress");
                Toast.makeText(getActivity(), stringAddressUri, Toast.LENGTH_LONG).show();
                addressProof = Uri.parse(stringAddressUri);
                file = new File(addressProof.getPath());
                compressedImageBitmap = Compressor.getDefault(getContext()).compressToBitmap(file);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byte[] array = byteArrayOutputStream.toByteArray();
                encoded = Base64.encodeToString(array, Base64.DEFAULT);
                imageAdapter.setAddress_image_string(encoded);
                is_address_proof_submitted = "1";
                AddressProofImage.setImageBitmap(compressedImageBitmap);
            } catch (Exception e) {

            }
        }
        if (requestCode == 1) {
            try {
                byte[] byteArray = data.getByteArrayExtra("BMP");
                File f = File.createTempFile("Signature", ".jpg", getContext().getCacheDir());
                f.createNewFile();
                FileOutputStream stream = new FileOutputStream(f.getPath());
                stream.write(byteArray);

                compressedImageBitmap = new Compressor.Builder(getContext())
                        .setMaxWidth(1024)
                        .setMaxHeight(600)
                        .setQuality(70)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .build()
                        .compressToBitmap(f);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] array = byteArrayOutputStream.toByteArray();
                encoded = Base64.encodeToString(array, Base64.DEFAULT);
                imageAdapter.setSignature_image_string(encoded);
                is_signature_submitted = "1";
                signatureImage.setImageBitmap(compressedImageBitmap);
            } catch (Exception e) {

            }
        }
        if (requestCode == 104 && resultCode == RESULT_OK) {
            if (imageAdapter.getFingerBItmap() != null) {
                try {
                    byte[] array = data.getByteArrayExtra("biometric_byte_array");
                    is_biometric_submitted = "1";
                    biometricImage.setImageResource(R.drawable.biometric_updated);
                    encoded = Base64.encodeToString(array, Base64.DEFAULT);
                    imageAdapter.setBiometric_image_string(encoded);
                    if (imageAdapter.getBiometric_image_string() != null) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    byte[] array2 = data.getByteArrayExtra("biometric_byte_array2");
                    is_biometric_submitted = "1";
                    biometricImage.setImageResource(R.drawable.biometric_updated);
                    encoded2 = Base64.encodeToString(array2, Base64.DEFAULT);
                    imageAdapter.setBiometric_image_string2(encoded2);
                    if (imageAdapter.getBiometric_image_string() != null) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    byte[] array3 = data.getByteArrayExtra("biometric_byte_array3");
                    is_biometric_submitted = "1";
                    biometricImage.setImageResource(R.drawable.biometric_updated);
                    encoded3 = Base64.encodeToString(array3, Base64.DEFAULT);
                    imageAdapter.setBiometric_image_string3(encoded3);
                    if (imageAdapter.getBiometric_image_string() != null) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                //Toast.makeText(getContext(), "Please Capture Your Finger Print", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "NO Fingerprint Captured", Toast.LENGTH_SHORT).show();
        }
        if (resultCode == RESULT_OK && requestCode == 1001) {
            getPhotograph();
        }
        if (resultCode == RESULT_OK && requestCode == 1002) {
            getIdProof();
        }
        if (resultCode == RESULT_OK && requestCode == 1003) {
            getAddressProof();
        }
        if (resultCode == RESULT_OK && requestCode == 1004) {
            getSignature();
        }
        if (resultCode == RESULT_OK && requestCode == 1005) {
            getBiometric();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }

    private void getPhotograph() {
        Intent id = new Intent(getActivity(), Accounts_Tab_Fragment_PhotographActivity.class);
        startActivityForResult(id, 0);
    }

    private void getSignature() {
        Intent intent = new Intent(getActivity(), Signature_activity.class);
        startActivityForResult(intent, 1);
    }

    private void getIdProof() {
        Intent id = new Intent(getActivity(), Accounts_Tab_Fragment_IDActivity.class);
        startActivityForResult(id, 200);
    }

    private void getAddressProof() {
        Intent id = new Intent(getActivity(), Accounts_Tab_Fragment_AddressActivity.class);
        startActivityForResult(id, 201);
    }

    private void getBiometric() {
        if (connectionStatus) {
            Intent intent = new Intent(getContext(), Biometric_Leopard.class);
            startActivityForResult(intent, 104);
        } else {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("BT Device Not Connected")
                    .setContentText("please connect the device")
                    .setCancelText("NO")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            startActivity(new Intent(getContext(), Act_Main.class));
                            sweetAlertDialog.dismiss();
                        }
                    }).show();
        }
    }

//-------------------------------------------------------------------------
//    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
//
//    private void checkAndroidVersion() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            checkPermission();
//        } else {
//            // write your logic here
//            Log.e("checkAndroidVersion: ", "SDK_INT > ");
//        }
//    }
//
//    private void checkPermission() {
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) +
//                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ||
//                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
//
//                Log.e("checkPermission: ", "shouldShowRequestPermissionRationale");
//
//                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("Permission Request")
//                        .setContentText("Please Grant Permissions to take photo")
//                        .setCancelText("NO")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                requestPermissions(
//                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
//                                                Manifest.permission.CAMERA},
//                                        PERMISSIONS_MULTIPLE_REQUEST);
//                            }
//                        }).show();
//
////                Snackbar.make(getActivity().findViewById(android.R.id.content),
////                        "Please Grant Permissions to take photo",
////                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
////                        new View.OnClickListener() {
////                            @Override
////                            public void onClick(View v) {
////                                requestPermissions(
////                                        new String[]{Manifest.permission
////                                                .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
////                                        PERMISSIONS_MULTIPLE_REQUEST);
////                            }
////                        }).show();
//            } else {
//
//                Log.e("checkPermission: ", "else else else");
//
//                requestPermissions(
//                        new String[]{Manifest.permission
//                                .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
//                        PERMISSIONS_MULTIPLE_REQUEST);
//            }
//        } else {
//            // write your logic code if permission already granted
//            Log.e("checkPermission: ", "permission already granted");
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
////            case 1: {
////                // If request is cancelled, the result arrays are empty.
////                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////
////                    // permission was granted, yay! Do the
////                    // contacts-related task you need to do.
////                } else {
////
////                    // permission denied, boo! Disable the
////                    // functionality that depends on this permission.
////                    Toast.makeText(getContext(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
////                }
////                return;
////            }
////
////            // other 'case' lines to check for other
////            // permissions this app might request
//
//            case PERMISSIONS_MULTIPLE_REQUEST:
//                if (grantResults.length > 0) {
//                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//
//                    if (cameraPermission && readExternalFile) {
//                        // write your logic here
//                    } else {
//
//                        Log.e("else: ", "onRequestPermissionsResult");
//
////                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
////                                .setTitleText("Permission Request")
////                                .setContentText("Please Grant Permissions to take photo")
////                                .setCancelText("NO")
////                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
////                                    @Override
////                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
////                                        requestPermissions(
////                                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
////                                                        Manifest.permission.CAMERA},
////                                                PERMISSIONS_MULTIPLE_REQUEST);
////                                    }
////                                }).show();
//                    }
//                }
//                break;
//        }
//    }

    //-------------------------------------------------------------------------
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
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
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
                            new AlertDialog.Builder(getActivity())
                                    .setMessage("Needed Camera and Storage permission, Go to settings and enable it!")
                                    .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
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
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(),
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


}