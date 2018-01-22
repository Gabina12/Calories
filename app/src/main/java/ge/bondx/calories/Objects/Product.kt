package ge.bondx.calories.Objects

/**
 * Created by Lasha.Gabinashvili on 1/17/2018.
 */
class Product {

    companion object Factory {
        fun create(): Product = Product()
    }

    var name: String? = null
    var calory: Number? = null
    var category: String? = null
    var isHeader: Boolean = false

}