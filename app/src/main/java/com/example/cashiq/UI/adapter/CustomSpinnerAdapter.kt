// CustomSpinnerAdapter.kt
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.cashiq.R
import com.example.cashiq.UI.CategoryItem

class CustomSpinnerAdapter(
    context: Context,
    private val items: List<CategoryItem>
) : ArrayAdapter<CategoryItem>(context, R.layout.spinner_item, items) {

    // Inflate the custom layout for the spinner items
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    // Inflate the custom layout for the dropdown items
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    // Create the view for each item in the spinner
    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val rowView = inflater.inflate(R.layout.spinner_item, parent, false)

        val icon = rowView.findViewById<ImageView>(R.id.icon)
        val categoryName = rowView.findViewById<TextView>(R.id.category_name)

        // Get the current category item
        val item = items[position]
        icon.setImageResource(item.iconResId) // Set the icon
        categoryName.text = item.name // Set the category name

        return rowView
    }
}