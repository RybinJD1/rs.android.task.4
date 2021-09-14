package com.example.android.carmarket.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.carmarket.database.*

@Entity(tableName = "cars")
data class Car(
    @ColumnInfo(name = BRAND) var brand: String?,
    @ColumnInfo(name = INFO) var info: String?,
    @ColumnInfo(name = CATEGORY) var category: String?,
    @ColumnInfo(name = KM) var km: Double?,
    @ColumnInfo(name = PRICE) var price: Double?,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(brand)
        parcel.writeString(info)
        parcel.writeString(category)
        parcel.writeValue(km)
        parcel.writeValue(price)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Car> {
        override fun createFromParcel(parcel: Parcel): Car {
            return Car(parcel)
        }

        override fun newArray(size: Int): Array<Car?> {
            return arrayOfNulls(size)
        }
    }
}