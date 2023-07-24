package pers.shawxingkwok.androidutil

internal inline fun foo(block: () -> Unit){
    KLog.d("", "(Inline.kt)")
    block()
}

internal inline fun bar(crossinline block: () -> Unit){
    object {
        init {
            block()
        }
    }
}