package ge.bondx.calories.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ge.bondx.calories.objects.Product


class MyDBHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "productDB.db"
        const val TABLE_PRODUCTS = "products"

        const val COLUMN_ID = "_id"
        const val COLUMN_KEY = "key"
        const val COLUMN_NAME = "name"
        const val COLUMN_CALORY = "calory"
        const val COLUMN_CATEGORY = "category"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_PRODUCTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME + " TEXT," +
                COLUMN_CATEGORY + " TEXT," +
                COLUMN_KEY + " TEXT,"
                + COLUMN_CALORY + " INTEGER" + ")")
        db.execSQL(PRODUCTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS)
        onCreate(db)
    }

    fun addProduct(product: Product){
        val values = ContentValues()
        values.put(COLUMN_NAME, product.name)
        values.put(COLUMN_CATEGORY, product.category)
        values.put(COLUMN_CALORY, product.calory!!.toDouble())
        values.put(COLUMN_KEY, product.key)

        val db = this.writableDatabase

        db.insert(TABLE_PRODUCTS, null, values)
        db.close()
    }

    fun findProduct(key: String): Product? {
        val query =
                "SELECT * FROM $TABLE_PRODUCTS WHERE $COLUMN_KEY =  \"$key\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        var product: Product? = null

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            val id = Integer.parseInt(cursor.getString(0))
            val name = cursor.getString(1)
            val category = cursor.getString(2)
            val key = cursor.getString(3)
            val calory = cursor.getDouble(4)

            product = Product.create()
            product.key = key
            product.name = name
            product.category = category
            product.calory = calory

            cursor.close()
        }

        db.close()
        return product
    }

    fun getProducts(): List<Product>? {
        val query =
                "SELECT * FROM $TABLE_PRODUCTS Order by $COLUMN_CATEGORY"

        val db = this.writableDatabase

        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS)
        //onCreate(db)

        val cursor = db.rawQuery(query, null)

        var products: MutableList<Product> = mutableListOf()
        var prevCategory: String? = ""

        while (cursor.moveToNext()){

            val id = Integer.parseInt(cursor.getString(0))
            val name = cursor.getString(1)
            val category = cursor.getString(2)
            val key = cursor.getString(3)
            val calory = cursor.getDouble(4)

            val product = Product.create()
            product.key = key
            product.name = name
            product.category = category
            product.calory = calory
            product.isChecked = true


            if(prevCategory!!.trim() != product.category!!.trim()){
                prevCategory = product.category!!.trim()
                var pheader = Product.create()
                pheader.name = prevCategory
                pheader.isHeader = true
                pheader.calory = 0
                products!!.add(pheader)
            }

            products.add(product)
        }

        db.close()
        return products
    }

    fun deleteProduct(key: String): Boolean{
        var result = false

        val query =
                "SELECT * FROM $TABLE_PRODUCTS WHERE $COLUMN_KEY = \"$key\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val id = Integer.parseInt(cursor.getString(0))
            db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?",
                    arrayOf(id.toString()))
            cursor.close()
            result = true
        }
        db.close()
        return result
    }
}