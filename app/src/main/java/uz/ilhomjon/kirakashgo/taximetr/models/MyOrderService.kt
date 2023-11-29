package uz.ilhomjon.kirakashgo.taximetr.models

import android.location.Location

class MyOrderService {
    var statusi:Int = 0 //0 -accept, 1-start, 2-finish
    var umumiyNarx:Int = 0
    var kutishVaqti:Int = 0
    var id:Int? = null
    var lastLatLng:Location? = null
    var yurganMasofasi:Double = 0.0

    constructor(
        statusi: Int,
        umumiyNarx: Int,
        kutishVaqti: Int,
        id: Int?,
        lastLatLng: Location?,
        yurganMasofasi: Double
    ) {
        this.statusi = statusi
        this.umumiyNarx = umumiyNarx
        this.kutishVaqti = kutishVaqti
        this.id = id
        this.lastLatLng = lastLatLng
        this.yurganMasofasi = yurganMasofasi
    }

    constructor()

    override fun toString(): String {
        return "MyOrderService(statusi=$statusi, umumiyNarx=$umumiyNarx, kutishVaqti=$kutishVaqti, id=$id, lastLatLng=$lastLatLng, yurganMasofasi=$yurganMasofasi)"
    }
}