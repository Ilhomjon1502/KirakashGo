package uz.ilhomjon.kirakashgo.data.remote.dto

class CheckSmsCodeResponse {
    var detail: String? = null
    var success: Boolean? = null

    constructor(detail: String?, success: Boolean?) {
        this.detail = detail
        this.success = success
    }

    override fun toString(): String {
        return "CheckSmsCodeResponse(detail=$detail, success=$success)"
    }


}