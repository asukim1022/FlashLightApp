package com.asukim.flashlight;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private boolean mFlashOn;

    private CameraManager mCameraManager;
    private String mCameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mCameraManager : CameraManager 플래쉬 사용하기 위해 선언
        mCameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

        final Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //flashlight : 플래쉬 on/off
                flashlight();
                if (mFlashOn == true) {
                    btn1.setText("flash light on");
                } else {
                    btn1.setText("flash light off");
                }
            }
        });
    }

    /** @brief flashlight : 플래시 on/off
     *  @date 2020.02.19
     *  @detail mCameraManager를 사용하여 플래시 상태와, on/off 관리
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void flashlight() {
        if (mCameraId == null) {
            try {
                for (String id : mCameraManager.getCameraIdList()) {

                    //CameraCharacteristics : CameraManager의 getCameraCharacteristics() 함수를 호출하면 각 카메라의 정보를 담고 있는 CameraCharacteristics 객체를 얻을 수 있음
                    CameraCharacteristics c = mCameraManager.getCameraCharacteristics(id);

                    Boolean flashAvailable = c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);

                    //CameraCharacteristics.LENS_FACING : characteristics.LENS_FACING은 카메라의 렌즈 방향을 얻을 때 사용하는 키로 다음의 상숫값으로 데이터를 얻는다.
                    Integer lensFacing = c.get(CameraCharacteristics.LENS_FACING);

                    if (flashAvailable != null && flashAvailable && lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                        mCameraId = id;
                        break;
                    }
                }
            } catch (CameraAccessException e) {
                mCameraId = null;
                e.printStackTrace();
                return;
            }
        }

        //mFlashOn : 토글
        mFlashOn = !mFlashOn;

        try {
            mCameraManager.setTorchMode(mCameraId, mFlashOn);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}
