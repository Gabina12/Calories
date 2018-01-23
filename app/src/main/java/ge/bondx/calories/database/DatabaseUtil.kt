package ge.bondx.calories.database

import android.util.Log
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Lasha.Gabinashvili on 1/23/2018.
 */
class DatabaseUtil {
    private var mDatabase: FirebaseDatabase? = null

    val database: FirebaseDatabase
        get() {
            if (mDatabase == null) {
                mDatabase = FirebaseDatabase.getInstance()
            }
            return mDatabase!!
        }

}

