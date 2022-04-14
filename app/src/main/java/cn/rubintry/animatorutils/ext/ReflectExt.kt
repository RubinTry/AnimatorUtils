package cn.rubintry.animatorutils.ext


/**
 * 获取对象中的某字段
 *
 * @param T
 * @param fieldName
 * @return
 */
inline fun <reified T> Any.getField(fieldName: String) : T? {
    val clazz = this.javaClass
    return getFieldFromClass(clazz , fieldName , this) as? T
}


/**
 * 递归获取声明的字段
 *
 * @param clazz 类
 * @param fieldName 字段名
 * @param obj 字段所在的对象实例
 * @return
 */
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