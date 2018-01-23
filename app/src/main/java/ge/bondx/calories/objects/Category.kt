package ge.bondx.calories.objects

/**
 * Created by Lasha.Gabinashvili on 1/17/2018.
 */
class ListItem {
    var name: String
    var products: List<Product>

    constructor(name: String, products: List<Product>){
        this.products = products
        this.name  = name
    }

}