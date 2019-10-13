package `in`.adityaitsolutions.gst.Helpers

import android.content.Context


class SharedPrefManager{
    private var mInstance: SharedPrefManager? = null
    private lateinit var mCtx: Context

    private val SHARED_PREF_NAME = "jitokop"

    private val KEY_NAME = "keyname"
    private val KEY_MOBILE = "keymobile"

    constructor(context: Context) {
        mCtx = context
    }

    @Synchronized
    fun getInstance(context: Context): SharedPrefManager {
        if (mInstance == null) {
            mInstance = SharedPrefManager(context)
        }
        return mInstance as SharedPrefManager
    }

    fun userLogin(name:String,mobile:String): Boolean {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_NAME, name)
        editor.putString(KEY_MOBILE, mobile)
        editor.apply()
        return true
    }
    fun getName(): String{
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_NAME, null)
    }

    fun getMobile(): String{
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_MOBILE, null)
    }


    fun isLoggedIn(): Boolean {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return if (sharedPreferences.getString(KEY_NAME, null) != null) true else false
    }

    fun logout(): Boolean {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        return true
    }
}