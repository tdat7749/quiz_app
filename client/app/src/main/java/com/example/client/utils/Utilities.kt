package com.example.client.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import androidx.annotation.RequiresApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class Utilities {
    companion object {
        private const val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        fun isValidEmail(email:String):Boolean {
            return email.matches(emailRegex.toRegex())
        }

        fun createPartFromString(value: String): RequestBody {
            return value.toRequestBody("text/plain".toMediaTypeOrNull())
        }

        fun createFilePart(fieldName: String, filePath: String): MultipartBody.Part {
            val file = File(filePath)
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData(fieldName, file.name, requestBody)
        }

        fun createPartFromInt(value: Int): RequestBody {
            val valueString = value.toString()
            return valueString.toRequestBody("text/plain".toMediaTypeOrNull())
        }

        fun slugify(string: String): String {
            val a = "àáäâãåăæąçćčđďèéěėëêęğǵḧìíïîįłḿǹńňñòóöôœøṕŕřßşśšșťțùúüûǘůűūųẃẍÿýźžż"
            val b = "aaaaaaaaacccddeeeeeeegghiiiiilmnnnnooooooprrsssssttuuuuuuuuuwxyyzzz"
            val p = Regex(a.toCharArray().joinToString("|"))
            return string.toLowerCase(Locale.ROOT)
                .replace(Regex("[áàảạãăắằẳẵặâấầẩẫậ]"), "a")
                .replace(Regex("[éèẻẽẹêếềểễệ]"), "e")
                .replace(Regex("[íìỉĩị]"), "i")
                .replace(Regex("[óòỏõọôốồổỗộơớờởỡợ]"), "o")
                .replace(Regex("[úùủũụưứừửữự]"), "u")
                .replace(Regex("[ýỳỷỹỵ]"), "y")
                .replace("đ", "d")
                .replace(Regex("\\s+"), "-")
                .replace(p) { match -> b[a.indexOf(match.value)].toString() }
                .replace("&", "-and-")
                .replace(Regex("[^\\w\\-]+"), "")
                .replace(Regex("\\-+"), "-")
                .removePrefix("-")
                .removeSuffix("-")
        }

        fun formatDate(date: Date): String {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            return dateFormat.format(date)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun formatDateInvert(date:String?): String {
            if(date == null){
                return ""
            }
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
            val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

            val dateTime: LocalDateTime = LocalDateTime.parse(date,inputFormatter)
            return dateTime.format(outputFormatter)
        }

        fun getRealPathFromURI(uri: Uri, context: Context): String? {
            val returnCursor = context.contentResolver.query(uri, null, null, null, null)
            val nameIndex =  returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
            returnCursor.moveToFirst()
            val name = returnCursor.getString(nameIndex)
            val file = File(context.filesDir, name)
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val outputStream = FileOutputStream(file)
                var read = 0
                val maxBufferSize = 1 * 1024 * 1024
                val bytesAvailable: Int = inputStream?.available() ?: 0
                //int bufferSize = 1024;
                val bufferSize = Math.min(bytesAvailable, maxBufferSize)
                val buffers = ByteArray(bufferSize)
                while (inputStream?.read(buffers).also {
                        if (it != null) {
                            read = it
                        }
                    } != -1) {
                    outputStream.write(buffers, 0, read)
                }
                inputStream?.close()
                outputStream.close()

            } catch (e: java.lang.Exception) {
                Log.e("Utilities.getRealPathFromURI", e.message!!)
            }
            return file.path
        }
    }
}