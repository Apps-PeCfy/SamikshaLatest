package com.samiksha.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.InvalidParameterSpecException
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec

object DownloadUtils {
    fun getFileNameFromUrl(path: String?): String {
        var fileName = ""
        if (path != null) {
            val downloadString = path.split("/").toTypedArray()
            fileName = downloadString[downloadString.size - 1]
        }
        fileName = fileName.replace(" ", "_")
        return fileName
    }

    fun isFileDownloaded(
        context: Context?,
        model: SkillDetailsResponsePOJO.Data,
        url: String,
    ): Boolean {
        var filePath: String
        if (url.contains("http")){
            filePath = fetchStoragePath(context!!) + "${Constants.FOLDER_NAME}/${SessionManager.getInstance(context).getUserModel()?.id}/" + model.name + "/" + getFileNameFromUrl(url)
        }else{
            filePath = fetchStoragePath(context!!) + "${Constants.FOLDER_NAME}/${SessionManager.getInstance(context).getUserModel()?.id}/" + model.name + "/" + url
        }


        val topicFile = File(filePath)
        return topicFile.exists()
    }

    fun getExternalSdCardPath(): String? {
        var removableStoragePath: String? = null
        val fileList = File("/storage/").listFiles()!!
        for (file in fileList) {
            if (!file.absolutePath.equals(
                    Environment.getExternalStorageDirectory().absolutePath,
                    ignoreCase = true
                ) && file.isDirectory && file.canRead()
            ) removableStoragePath = file.absolutePath
        }
        return removableStoragePath
    }

    fun fetchStoragePath(context: Context): String? {
        var path: String? = null
        path = if (false) {
            getExternalSdCardPath() + File.separator
        } else {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                Environment.getExternalStorageDirectory().toString() + File.separator
            } else {
                context.getExternalFilesDir(null).toString() + File.separator
            }
        }
        return path
    }


    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun generateKey(): SecretKey? {
        return SecretKeySpec(Constants.CIPHER_PASSWORD.toByteArray(), "AES")
    }

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidParameterSpecException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class,
        UnsupportedEncodingException::class
    )
    fun encryptFile(filePath: String, fileName: String) {
        val inputFile = File("$filePath/$fileName")
        val fis = FileInputStream(inputFile)
        val outfile = File("$filePath/Enc_$fileName")
        if (!outfile.exists()) outfile.createNewFile()
        var read: Int
        val fos = FileOutputStream(outfile)

        var cipher: Cipher? = null
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, generateKey())
        val cis = CipherInputStream(fis, cipher)
        while (cis.read().also { read = it } != -1) {
            fos.write(read)
            fos.flush()
        }
        fos.close()
        if (inputFile.exists()){
            inputFile.delete()
        }
    }
}