package com.example.physicalfitnessexamination.bean

import android.os.Parcel
import android.os.Parcelable
import com.example.physicalfitnessexamination.common.annotation.AllOpenAndNoArgAnnotation

/**
 * Created by chenzhiyuan On 2020/4/13
 */
@AllOpenAndNoArgAnnotation
data class PersonBean(
        var AGE: Int?,
        var ID: String?,
        var J_TYPE: String?,
        var NAME: String?,
        var ORG_ID: String?,
        var ORG_NAME: String?,
        var SEX: String?,
        var TYPE: String?,
        var ZW: String?
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(AGE)
        writeString(ID)
        writeString(J_TYPE)
        writeString(NAME)
        writeString(ORG_ID)
        writeString(ORG_NAME)
        writeString(SEX)
        writeString(TYPE)
        writeString(ZW)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PersonBean> = object : Parcelable.Creator<PersonBean> {
            override fun createFromParcel(source: Parcel): PersonBean = PersonBean(source)
            override fun newArray(size: Int): Array<PersonBean?> = arrayOfNulls(size)
        }
    }
}