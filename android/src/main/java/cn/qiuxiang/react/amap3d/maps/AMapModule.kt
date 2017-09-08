package cn.qiuxiang.react.amap3d

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import cn.qiuxiang.react.amap3d.maps.AMapView
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.NativeViewHierarchyManager
import com.facebook.react.uimanager.UIBlock
import com.facebook.react.uimanager.UIManagerModule
import java.io.*

class AMapModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "AMapModule"
    }


    val activity: Activity?
        get() = currentActivity

    @ReactMethod
    fun takeSnapshot(tag: Int, options: ReadableMap, promise: Promise) {

        // Parse and verity options
        val context = reactApplicationContext
        val format = if (options.hasKey("format")) options.getString("format") else "png"
        val compressFormat = if (format == SNAPSHOT_FORMAT_PNG)
            Bitmap.CompressFormat.PNG
        else if (format == SNAPSHOT_FORMAT_JPG) Bitmap.CompressFormat.JPEG else null
        val quality = if (options.hasKey("quality")) options.getDouble("quality") else 1.0
        val displayMetrics = context.resources.displayMetrics
        val width = if (options.hasKey("width")) (displayMetrics.density * options.getDouble("width")).toInt() else 0
        val height = if (options.hasKey("height")) (displayMetrics.density * options.getDouble("height")).toInt() else 0
        val result = if (options.hasKey("result")) options.getString("result") else "file"

        // Add UI-block so we can get a valid reference to the map-view
        val uiManager = context.getNativeModule(UIManagerModule::class.java)
        uiManager.addUIBlock(UIBlock { nvhm: NativeViewHierarchyManager ->
            val view = nvhm.resolveView(tag) as AMapView
            if (view == null) {
                promise.reject("AMapView not found")
                return@UIBlock
            }

            if (view.map == null) {
                promise.reject("AirMapView.map is not valid");
                return@UIBlock;
            }

            view.map.getMapScreenShot(object : AMap.OnMapScreenShotListener {
                override fun onMapScreenShot(bitmap: Bitmap) {

                }

                override fun onMapScreenShot(bitmap: Bitmap?, status: Int) {
                    var bitmap = bitmap

                    // Convert image to requested width/height if necessary
                    if (bitmap == null) {
                        promise.reject("Failed to generate bitmap, snapshot = null")
                        return
                    }
                    if (width !== 0 && height !== 0 &&
                            (width !== bitmap.width || height !== bitmap.height)) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
                    }

                    // Save the snapshot to disk
                    if (result == SNAPSHOT_RESULT_FILE) {

                        val tempFile: File
                        val outputStream: FileOutputStream
                        try {
                            tempFile = File.createTempFile("AMapSnapshot", "." + format, context.cacheDir)
                            outputStream = FileOutputStream(tempFile)
                        } catch (e: Exception) {
                            promise.reject(e)
                            return
                        }

                        bitmap!!.compress(compressFormat, (100.0 * quality).toInt(), outputStream)
                        closeQuietly(outputStream)
                        val uri = Uri.fromFile(tempFile).toString()
                        promise.resolve(uri)
                    } else if (result == SNAPSHOT_RESULT_BASE64) {
                        val outputStream = ByteArrayOutputStream()
                        bitmap!!.compress(compressFormat, (100.0 * quality).toInt(), outputStream)
                        closeQuietly(outputStream)
                        val bytes = outputStream.toByteArray()
                        val data = Base64.encodeToString(bytes, Base64.NO_WRAP)
                        promise.resolve(data)
                    }

                }
            })
        })

    }

    @ReactMethod
    fun calculateLineDistance(start: ReadableMap, end: ReadableMap, promise: Promise){

        var result =  AMapUtils.calculateLineDistance(
                LatLng(start.getDouble("latitude"), start.getDouble("longitude")),
                LatLng(end.getDouble("latitude"),end.getDouble("longitude") )
        )

        promise.resolve(result);
    }

    companion object {

        private val SNAPSHOT_RESULT_FILE = "file"
        private val SNAPSHOT_RESULT_BASE64 = "base64"
        private val SNAPSHOT_FORMAT_PNG = "png"
        private val SNAPSHOT_FORMAT_JPG = "jpg"

        fun closeQuietly(closeable: Closeable?) {
            if (closeable == null) return
            try {
                closeable.close()
            } catch (ignored: IOException) {
            }

        }
    }
}
