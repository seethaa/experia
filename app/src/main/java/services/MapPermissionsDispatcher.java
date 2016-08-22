package services;

import android.support.v4.app.ActivityCompat;

import fragments.LocationSettingsFragment;
import permissions.dispatcher.PermissionUtils;

/**
 * Created by Sam on 8/22/16.
 */
final public class MapPermissionsDispatcher {
    private static final int REQUEST_GETMYLOCATION = 0;

    private static final String[] PERMISSION_GETMYLOCATION = new String[] {"android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_COARSE_LOCATION"};

    private MapPermissionsDispatcher() {
    }

    static public void getMyLocationWithCheck(LocationSettingsFragment target) {
        if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_GETMYLOCATION)) {
            target.getMyLocation();
        } else {
            ActivityCompat.requestPermissions(target.getActivity(), PERMISSION_GETMYLOCATION, REQUEST_GETMYLOCATION);
        }
    }

    static public void onRequestPermissionsResult(LocationSettingsFragment target, int requestCode, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_GETMYLOCATION:
                if (PermissionUtils.getTargetSdkVersion(target.getActivity()) < 23 && !PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_GETMYLOCATION)) {
                    return;
                }
                if (PermissionUtils.verifyPermissions(grantResults)) {
                    target.getMyLocation();
                }
                break;
            default:
                break;
        }
    }
}
