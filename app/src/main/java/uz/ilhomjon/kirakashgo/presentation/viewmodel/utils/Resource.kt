package uz.ilhomjon.kirakashgo.presentation.viewmodel.utils

data class MyResource<out T>(val status: Status, val data:T?, val message:String?){

    companion object{
        fun <T>success(data:T):MyResource<T>{
            return MyResource(Status.SUCCESS, data, null)
        }
        fun <T>error(message: String?):MyResource<T>{
            return MyResource(Status.ERROR, null, message)
        }
        fun <T>loading(message: String?):MyResource<T>{
            return MyResource(Status.LOADING, null, message)
        }
    }
}