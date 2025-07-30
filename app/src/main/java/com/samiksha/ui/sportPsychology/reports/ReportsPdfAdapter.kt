package com.samiksha.ui.sportPsychology.reports

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.samiksha.BuildConfig
import com.samiksha.R
import com.samiksha.ui.sportPsychology.reports.pojo.AssesmentScoreResponsePOJO
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ReportsPdfAdapter(
    var context: FragmentActivity?,
    var assesmentScoreList: List<AssesmentScoreResponsePOJO.DataItem>?
) :
    RecyclerView.Adapter<ReportsPdfAdapter.MyViewHolder>() {

    private val PERMISSION_REQUEST_CODE = 200


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.templete_pdf, viewGroup, false)
        return MyViewHolder(v)
    }


    override fun getItemCount(): Int {
        return assesmentScoreList!!.size
    }

    @RequiresApi(30)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        Glide.with(context!!)
            .load(assesmentScoreList!!.get(position).iconUrl)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
            ).into(holder.imgnext)


        holder.tvPdfDate.text = assesmentScoreList!!.get(position).craetedAt
        holder.llPdf.setOnClickListener(View.OnClickListener {

            if (checkPermission()) {
                //  Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                requestPermission()
            }

            var path: String = ""
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                path = Environment.getExternalStorageDirectory()
                    .toString() + File.separator + "Samiksha"
            } else {
                path = context!!.getExternalFilesDir(null).toString() + File.separator + "Samiksha"
            }

            val dir = File(path)
            try {
                if (dir.mkdir()) {
                    Log.d("Directory created", "Yes")
                } else {
                    Log.d("Directory created", "No")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val pdfName = assesmentScoreList!![position].craetedAt
            var downloadPath = path + "/$pdfName.pdf"
            try {

                val dwldsPath = File(downloadPath)
                val pdfAsBytes = Base64.decode(assesmentScoreList!![position].pdf, 0)
                val os: FileOutputStream
                os = FileOutputStream(dwldsPath, false)
                os.write(pdfAsBytes)
                os.flush()
                os.close()
            } catch (e: IOException) {
                Log.d("", "File.toByteArray() error")
                e.printStackTrace()
            }


            displaypdf(downloadPath)
        })


    }


    fun displaypdf(pdfPath: String) {

        val pdfFile = File(pdfPath) //File path

        if (pdfFile.exists()) //Checking if the file exists or not
        {
            val path = Uri.fromFile(pdfFile)
            val objIntent = Intent(Intent.ACTION_VIEW)

            val path1 = FileProvider.getUriForFile(
                context!!,
                BuildConfig.APPLICATION_ID + ".provider", pdfFile
            )

            objIntent.setDataAndType(path1, "application/pdf")
            objIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            objIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            context!!.startActivity(objIntent) //Starting the pdf viewer
        } else {
            Toast.makeText(context, "The file not exists! ", Toast.LENGTH_SHORT).show()
        }
    }


    private fun checkPermission(): Boolean {
        // checking of permissions.
        val permission1: Int =
            ContextCompat.checkSelfPermission(context!!, WRITE_EXTERNAL_STORAGE)
        val permission2: Int =
            ContextCompat.checkSelfPermission(context!!, READ_EXTERNAL_STORAGE)
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(
            context!!,
            arrayOf(
                WRITE_EXTERNAL_STORAGE,
                READ_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                val writeStorage =
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readStorage =
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (writeStorage && readStorage) {
                    //  Toast.makeText(context!!, "Permission Granted..", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context!!, "Permission Denined.", Toast.LENGTH_SHORT).show()
                    context!!.finish()
                }
            }
        }
    }


    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val imgnext: ImageView = itemView.findViewById(R.id.img_learning_next)
        val llPdf: LinearLayout = itemView.findViewById(R.id.llPdf)
        val tvPdfDate: TextView = itemView.findViewById(R.id.tvPdfDate)

    }


}