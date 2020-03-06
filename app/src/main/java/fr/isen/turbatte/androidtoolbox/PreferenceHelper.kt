package fr.isen.turbatte.androidtoolbox
import android.content.Context

class PreferenceHelper (context: Context) {
    val PREFERENCE_NAME = "SharedPreferenceExample"
    val PREFERENCE_LOGIN_COUNT = "LoginCount"
    val app_prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)


    fun getLogin(): Int{
        return app_prefs.getInt(PREFERENCE_LOGIN_COUNT, 0)
    }

    fun setLogin(count:Int){
        val editor = app_prefs.edit()
        editor.putInt(PREFERENCE_LOGIN_COUNT, count)
        editor.apply()
    }
}