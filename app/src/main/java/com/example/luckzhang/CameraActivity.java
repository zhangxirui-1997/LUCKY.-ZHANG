package com.example.luckzhang;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ExifInterface;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.litepal.LitePal;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import Data_Class.Report_item;

import static java.lang.Thread.sleep;

public class CameraActivity extends AppCompatActivity implements SensorEventListener {
    private Intent intent;
    private int judge=0;

    private static final String TAG = CameraActivity.class.getName();
    private TextureView mTextureView;    //注意使用TextureView需要开启硬件加速,开启方法很简单在AndroidManifest.xml 清单文件里,你需要使用TextureView的activity添加android:hardwareAccelerated="true"
    private Button mBtnPhotograph;
    private HandlerThread mHandlerThread;
    private Handler mChildHandler = null;
    private CameraManager mCameraManager;                                //相机管理类,用于检测系统相机获取相机id
    private CameraDevice mCameraDevice;                                        //Camera设备类
    private CameraCaptureSession.StateCallback mSessionStateCallback;        //获取的会话类状态回调
    private CameraCaptureSession.CaptureCallback mSessionCaptureCallback;    //获取会话类的获取数据回调
    private CaptureRequest.Builder mCaptureRequest;                            //获取数据请求配置类
    private CameraDevice.StateCallback mStateCallback;                        //摄像头状态回调
    private CameraCaptureSession mCameraCaptureSession;                    //获取数据会话类
    private ImageReader mImageReader;                                        //照片读取器
    private Surface mSurface;
    private SurfaceTexture mSurfaceTexture;
    private String mCurrentCameraId;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private int mSensorOrientation;
    private CameraCharacteristics cameraCharacteristics;

    static {// /为了使照片竖直显示
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private Button button_ok;
    private Button button_notok;
    private ImageView imageView_luokuo;
    private SensorManager sensorManager;
    private Sensor magnticSensor;
    private Sensor accelerometerSensor;
    private SensorEventListener listener;
    float[] gravity;
    float[] r;
    float[] geomagnetic;
    float[] values;
    private ImageView imageView;
    private TextView textView;
    private void initSensor(){
        sensorManager=(SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        magnticSensor=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometerSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gravity=new float[3];//用来保存加速度传感器的值
        r=new float[9];
        geomagnetic=new float[3];//用来保存地磁传感器的值
        values=new float[3];//用来保存最终的结果
        //sensorManager.registerListener((SensorEventListener) this,magnticSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener((SensorEventListener) this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
        SensorManager.getRotationMatrix(r,null,gravity,geomagnetic);
        SensorManager.getOrientation(r,values);
        imageView=findViewById(R.id.imageView10);
        textView=findViewById(R.id.test);
    }

    //第四步：必须重写的两个方法：onAccuracyChanged，onSensorChanged
    /**
     * 传感器精度发生改变的回调接口
     */
    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        //TODO 在传感器精度发生改变时做些操作，accuracy为当前传感器精度
    }
    /**
     * 传感器事件值改变时的回调接口：执行此方法的频率与注册传感器时的频率有关
     */
    @Override
    public final void onSensorChanged(SensorEvent event) {
        // 大部分传感器会返回三个轴方向x,y,x的event值，值的意义因传感器而异
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        int xint=Math.round(x);
        imageView.setRotation(xint);
        if(xint!=0){
            textView.setText("手机倾斜！请保持水平！");
        }else{
            textView.setText("当前保持水平状态");
        }
        Log.d("111111","1111111111"+x+" "+y+" "+z);
        //TODO 利用获得的三个float传感器值做些操作
    }
    /**
     * 第三步：在获得焦点时注册传感器并让本类实现SensorEventListener接口
     */
    @Override
    protected void onResume() {
        super.onResume();
        /*
         *第一个参数：SensorEventListener接口的实例对象
         *第二个参数：需要注册的传感器实例
         *第三个参数：传感器获取传感器事件event值频率：
         *              SensorManager.SENSOR_DELAY_FASTEST = 0：对应0微秒的更新间隔，最快，1微秒 = 1 % 1000000秒
         *              SensorManager.SENSOR_DELAY_GAME = 1：对应20000微秒的更新间隔，游戏中常用
         *              SensorManager.SENSOR_DELAY_UI = 2：对应60000微秒的更新间隔
         *              SensorManager.SENSOR_DELAY_NORMAL = 3：对应200000微秒的更新间隔
         *              键入自定义的int值x时：对应x微秒的更新间隔
         *
         */

    }
    /**
     * 第五步：在失去焦点时注销传感器
     */
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    /**
     * 获取手机旋转角度
     */
    /*public void getOritation() {
        // r从这里返回
        SensorManager.getRotationMatrix(r, null, gravity, geomagnetic);
        //values从这里返回
        SensorManager.getOrientation(r, values);
        //提取数据
        double degreeZ = Math.toDegrees(values[0]);
        double degreeX = Math.toDegrees(values[1]);
        double degreeY = Math.toDegrees(values[2]);
        if(listener != null){
            listener.onOrientationChange(degreeX, degreeY, degreeZ);
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initSensor();
        mTextureView = findViewById(R.id.textureView);
        mBtnPhotograph = findViewById(R.id.button4);
        imageView_luokuo=findViewById(R.id.imageView11);
        mBtnPhotograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mCameraCaptureSession.stopRepeating();//停止重复   取消任何正在进行的重复捕获集 在这里就是停止画面预览

                    /*  mCameraCaptureSession.abortCaptures(); //终止获取   尽可能快地放弃当前挂起和正在进行的所有捕获。
                     * 这里有一个坑,其实这个并不能随便调用(我是看到别的demo这么使用,但是其实是错误的,所以就在这里备注这个坑).
                     * 最好只在Activity里的onDestroy调用它,终止获取是耗时操作,需要一定时间重新打开会话通道.
                     * 在这个demo里我并没有恢复预览,如果你调用了这个方法关闭了会话又拍照后恢复图像预览,会话就会频繁的开关,
                     * 导致拍照图片在处理耗时缓存时你又关闭了会话.导致照片缓存不完整并且失败.
                     * 所以切记不要随便使用这个方法,会话开启后并不需要关闭刷新.后续其他拍照/预览/录制视频直接操作这个会话即可
                     */

                    takePicture();//拍照
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                button_ok.setVisibility(View.VISIBLE);
                button_notok.setVisibility(View.VISIBLE);
                mBtnPhotograph.setVisibility(View.GONE);
            }
        });

        initjudge();

        initChildThread();
        initImageReader();
        initTextureView();
    }

    private void initTextureView() {
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                Log.d(TAG, "1111111111TextureView 启用成功");
                initCameraManager();
                initCameraCallback();
                initCameraCaptureSessionStateCallback();
                initCameraCaptureSessionCaptureCallback();
                selectCamera();
                openCamera();

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                Log.d(TAG, "1111111111111SurfaceTexture 变化");

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                Log.d(TAG, "111111111111SurfaceTexture 的销毁");
                //这里返回true则是交由系统执行释放，如果是false则需要自己调用surface.release();
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }

    /**
     * 初始化子线程
     */
    private void initChildThread() {
        mHandlerThread = new HandlerThread("camera2");
        mHandlerThread.start();
        mChildHandler = new Handler(mHandlerThread.getLooper());
    }

    /**
     * 初始化相机管理
     */
    private void initCameraManager() {
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

    }

    /**
     * 获取匹配的大小
     *
     * @return
     */
    private Size getMatchingSize() {

        Size selectSize = null;
        float selectProportion = 0;
        try {
            float viewProportion = (float) mTextureView.getWidth() / (float) mTextureView.getHeight();
            cameraCharacteristics = mCameraManager.getCameraCharacteristics(mCurrentCameraId);
            StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Size[] sizes = streamConfigurationMap.getOutputSizes(ImageFormat.JPEG);
            for (int i = 0; i < sizes.length; i++) {
                Size itemSize = sizes[i];
                float itemSizeProportion = (float) itemSize.getHeight() / (float) itemSize.getWidth();
                float differenceProportion = Math.abs(viewProportion - itemSizeProportion);
                Log.d(TAG, "111111111相减差值比例=" + differenceProportion);
                if (i == 0) {
                    selectSize = itemSize;
                    selectProportion = differenceProportion;
                    continue;
                }
                if (differenceProportion <= selectProportion) {
                    if (differenceProportion == selectProportion) {
                        if (selectSize.getWidth() + selectSize.getHeight() < itemSize.getWidth() + itemSize.getHeight()) {
                            selectSize = itemSize;
                            selectProportion = differenceProportion;
                        }

                    } else {
                        selectSize = itemSize;
                        selectProportion = differenceProportion;
                    }
                }
            }
            mSensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "111111111getMatchingSize: 选择的比例是=" + selectProportion);
        Log.d(TAG, "1111111111getMatchingSize: 选择的尺寸是 宽度="+ selectSize.getWidth() + "高度=" + selectSize.getHeight());
        return selectSize;
    }

    /**
     * 选择摄像头
     */
    private void selectCamera() {
        try {
            String[] cameraIdList = mCameraManager.getCameraIdList();//获取摄像头id列表
            if (cameraIdList.length == 0) {
                return;
            }

            for (String cameraId : cameraIdList) {
                Log.d(TAG, "1111111111selectCamera: cameraId=" + cameraId);
                //获取相机特征,包含前后摄像头信息，分辨率等
                CameraCharacteristics cameraCharacteristics = mCameraManager.getCameraCharacteristics(cameraId);
                Integer facing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);//获取这个摄像头的面向
                //CameraCharacteristics.LENS_FACING_BACK 后摄像头
                //CameraCharacteristics.LENS_FACING_FRONT 前摄像头
                //CameraCharacteristics.LENS_FACING_EXTERNAL 外部摄像头,比如OTG插入的摄像头
                if (facing == CameraCharacteristics.LENS_FACING_BACK) {
                    mCurrentCameraId = cameraId;

                }

            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化摄像头状态回调
     */
    private void initCameraCallback() {
        mStateCallback = new CameraDevice.StateCallback() {
            /**
             * 摄像头打开时
             * @param camera
             */
            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                Log.d(TAG, "11111111相机开启");
                mCameraDevice = camera;

                try {
                    mSurfaceTexture = mTextureView.getSurfaceTexture();        //surfaceTexture    需要手动释放
                    Size matchingSize = getMatchingSize();
                    mSurfaceTexture.setDefaultBufferSize(matchingSize.getWidth(), matchingSize.getHeight());//设置预览的图像尺寸
                    mSurface = new Surface(mSurfaceTexture);//surface最好在销毁的时候要释放,surface.release();
//                        CaptureRequest可以完全自定义拍摄参数,但是需要配置的参数太多了,所以Camera2提供了一些快速配置的参数,如下:
// 　　　　　　　　　      TEMPLATE_PREVIEW ：预览
//                        TEMPLATE_RECORD：拍摄视频
//                        TEMPLATE_STILL_CAPTURE：拍照
//                        TEMPLATE_VIDEO_SNAPSHOT：创建视视频录制时截屏的请求
//                        TEMPLATE_ZERO_SHUTTER_LAG：创建一个适用于零快门延迟的请求。在不影响预览帧率的情况下最大化图像质量。
//                        TEMPLATE_MANUAL：创建一个基本捕获请求，这种请求中所有的自动控制都是禁用的(自动曝光，自动白平衡、自动焦点)。
                    mCaptureRequest = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);//创建预览请求
                    mCaptureRequest.addTarget(mSurface); //添加surface   实际使用中这个surface最好是全局变量 在onDestroy的时候mCaptureRequest.removeTarget(mSurface);清除,否则会内存泄露
                    mCaptureRequest.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);//自动对焦
                    /**
                     * 创建获取会话
                     * 这里会有一个容易忘记的坑,那就是Arrays.asList(surface, mImageReader.getSurface())这个方法
                         * 这个方法需要你导入后面需要操作功能的所有surface,比如预览/拍照如果你2个都要操作那就要导入2个
                     * 否则后续操作没有添加的那个功能就报错surface没有准备好,这也是我为什么先初始化ImageReader的原因,因为在这里就可以拿到ImageReader的surface了
                     */
                    mCameraDevice.createCaptureSession(Arrays.asList(mSurface, mImageReader.getSurface()), mSessionStateCallback, mChildHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

            }

            /**
             *摄像头断开时
             * @param camera
             */
            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {

            }

            /**
             * 出现异常情况时
             * @param camera
             * @param error
             */
            @Override
            public void onError(@NonNull CameraDevice camera, int error) {

            }

            /**
             * 摄像头关闭时
             * @param camera
             */
            @Override
            public void onClosed(@NonNull CameraDevice camera) {
                super.onClosed(camera);
            }
        };
    }

    /**
     * 摄像头获取会话状态回调
     */
    private void initCameraCaptureSessionStateCallback() {
        mSessionStateCallback = new CameraCaptureSession.StateCallback() {

            //摄像头完成配置，可以处理Capture请求了。
            @Override
            public void onConfigured(@NonNull CameraCaptureSession session) {
                try {
                    mCameraCaptureSession = session;
                    //注意这里使用的是 setRepeatingRequest() 请求通过此捕获会话无休止地重复捕获图像。用它来一直请求预览图像
                    mCameraCaptureSession.setRepeatingRequest(mCaptureRequest.build(), mSessionCaptureCallback, mChildHandler);


//                    mCameraCaptureSession.stopRepeating();//停止重复   取消任何正在进行的重复捕获集
//                    mCameraCaptureSession.abortCaptures();//终止获取   尽可能快地放弃当前挂起和正在进行的所有捕获。请只在销毁activity的时候调用它
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

            }

            //摄像头配置失败
            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession session) {

            }
        };
    }

    /**
     * 摄像头获取会话数据回调
     */
    private void initCameraCaptureSessionCaptureCallback() {
        mSessionCaptureCallback = new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                super.onCaptureStarted(session, request, timestamp, frameNumber);
            }

            @Override
            public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
                super.onCaptureProgressed(session, request, partialResult);
            }

            @Override
            public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                super.onCaptureCompleted(session, request, result);
//                Log.d(TAG, "111111111onCaptureCompleted: 触发接收数据");
//                Size size = request.get(CaptureRequest.JPEG_THUMBNAIL_SIZE);

            }

            @Override
            public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
                super.onCaptureFailed(session, request, failure);
            }

            @Override
            public void onCaptureSequenceCompleted(@NonNull CameraCaptureSession session, int sequenceId, long frameNumber) {
                super.onCaptureSequenceCompleted(session, sequenceId, frameNumber);
            }

            @Override
            public void onCaptureSequenceAborted(@NonNull CameraCaptureSession session, int sequenceId) {
                super.onCaptureSequenceAborted(session, sequenceId);
            }

            @Override
            public void onCaptureBufferLost(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull Surface target, long frameNumber) {
                super.onCaptureBufferLost(session, request, target, frameNumber);
            }
        };
    }

    /**
     * 打开摄像头
     */
    private void openCamera() {
        try {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                mCameraManager.openCamera(mCurrentCameraId, mStateCallback, mChildHandler);
                return;
            }
            Toast.makeText(this, "没有授权", Toast.LENGTH_SHORT).show();

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化图片读取器
     */
    private void initImageReader() {
        //创建图片读取器,参数为分辨率宽度和高度/图片格式/需要缓存几张图片,我这里写的2意思是获取2张照片
        mImageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 1);
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onImageAvailable(ImageReader reader) {
//        image.acquireLatestImage();//从ImageReader的队列中获取最新的image,删除旧的
//        image.acquireNextImage();//从ImageReader的队列中获取下一个图像,如果返回null没有新图像可用

                //CameraActivity.this.getExternalCacheDir().getPath()
                //上面一行是原来的地址
                Image image = reader.acquireNextImage();
                try {
                    File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/aphysique/data/temppicture");
                    if (!path.exists()) {
                        Log.d(TAG, "1111111111onImageAvailable: 路径不存在");
                        path.mkdirs();
                    } else {
                        Log.d(TAG, "11111111111onImageAvailable: 路径存在");
                        Log.d(TAG,"1111111111路径为"+CameraActivity.this.getExternalCacheDir().getPath());
                    }
                    File file = null;
                    int i= LitePal.count(Report_item.class);
                    if(judge==0){
                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/aphysique/data/temppicture/temp_zheng_picture"+i+".jpg");
                    }else if(judge==1){
                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/aphysique/data/temppicture/temp_ce_picture"+i+".jpg");
                    }

                    FileOutputStream fileOutputStream = new FileOutputStream(file);

//        这里的image.getPlanes()[0]其实是图层的意思,因为我的图片格式是JPEG只有一层所以是geiPlanes()[0],如果你是其他格式(例如png)的图片会有多个图层,就可以获取指定图层的图像数据　　　　　　　
                    ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);

                    fileOutputStream.write(bytes);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    image.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }, mChildHandler);
    }

    private void takePicture() {
        CaptureRequest.Builder captureRequestBuilder = null;
        try {
            captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);//自动对焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);//自动爆光
//            // 获取手机方向,如果你的app有提供横屏和竖屏,那么就需要下面的方法来控制照片为竖立状态
//            int rotation = getWindowManager().getDefaultDisplay().getRotation();
//            Log.d(TAG, "11111111111takePicture: 手机方向="+rotation);
//            Log.d(TAG, "1111111111takePicture: 照片方向="+ORIENTATIONS.get(rotation));
            int rotation =CameraActivity.this.getWindowManager().getDefaultDisplay().getRotation();
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));//项目不需要,直接写死90度 将照片竖立
            Surface surface = mImageReader.getSurface();
            captureRequestBuilder.addTarget(surface);
            CaptureRequest request = captureRequestBuilder.build();
            mCameraCaptureSession.capture(request, null, mChildHandler); //获取拍照
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private int getOrientation(int rotation) {
        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
        // We have to take that into account and rotate JPEG properly.
        // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCaptureRequest != null) {
            mCaptureRequest.removeTarget(mSurface);
            mCaptureRequest = null;
        }
        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }
        if (mSurfaceTexture != null){
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        if (mCameraCaptureSession != null) {
            try {
                mCameraCaptureSession.stopRepeating();
                mCameraCaptureSession.abortCaptures();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            mCameraCaptureSession = null;
        }

        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if (mChildHandler != null) {
            mChildHandler.removeCallbacksAndMessages(null);
            mChildHandler = null;
        }
        if (mHandlerThread != null) {
            mHandlerThread.quitSafely();
            mHandlerThread = null;
        }

        mCameraManager = null;
        mSessionStateCallback = null;
        mSessionCaptureCallback = null;
        mStateCallback = null;

    }

    private void initjudge(){
        intent=getIntent();
        judge=Integer.parseInt(intent.getStringExtra("judge"));
        Log.d(TAG,"1111111111judge:"+judge);
        if(judge==0){
            imageView_luokuo.setImageDrawable(getDrawable(R.mipmap.zheng1));
        }else if(judge==1){
            imageView_luokuo.setImageDrawable(getDrawable(R.mipmap.ce1));
        }
        button_ok=findViewById(R.id.button6);
        button_notok=findViewById(R.id.button5);
        button_ok.setVisibility(View.GONE);
        button_notok.setVisibility(View.GONE);

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button_notok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
    }
}
