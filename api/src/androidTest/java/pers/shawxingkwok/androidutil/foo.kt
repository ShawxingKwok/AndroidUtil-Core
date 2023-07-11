package pers.shawxingkwok.androidutil

inline fun foo(crossinline block: ()->Unit){
    object {
        init {
            block()
        }
    }
}