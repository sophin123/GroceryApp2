import android.provider.BaseColumns

class GroceryContract private constructor() {
    object GroceryEntry : BaseColumns {
        const val MAIN_ID = "ID"
        const val TABLE_NAME = "groceryList"
        const val COLUMN_NAME = "name"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_TIMESTAMP = "timestamp"
    }
}