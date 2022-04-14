package cn.rubintry.animatorutils.ext



inline fun <reified T> Any.getField(fieldName: String) : T? {
    val clazz = this.javaClass
    return getFieldFromClass(clazz , fieldName , this) as? T
}

fun getFieldFromClass(clazz: Class<*> , fieldName: String , obj: Any) : Any?{
    return try {
        val field = clazz.getDeclaredField(fieldName)
        field.isAccessible = true
        field.get(obj)
    }catch (e: NoSuchFieldException){
        if(null != clazz.superclass){
            getFieldFromClass(clazz.superclass , fieldName , obj)
        }else{
            null
        }
    }
}