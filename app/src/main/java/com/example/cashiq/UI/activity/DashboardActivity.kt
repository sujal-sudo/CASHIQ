import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cashiq.R
import com.example.cashiq.UI.fragment.ProfileFragment
import com.example.cashiq.databinding.ActivityDashboardBinding
//import com.github.mikephil.charting.data.Entry
//import com.github.mikephil.charting.data.LineData
//import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.math.abs

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
//    private lateinit var lineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setupChart()
        setupBottomNav()
//        setupFab()
        setupTransactionsList()
    }

     // line chart funtion

//    private fun setupChart() {
//        lineChart = binding.spendFrequencyChart
//
//        // Configure chart appearance
//        lineChart.apply {
//            description.isEnabled = false
//            setTouchEnabled(true)
//            setDrawGridBackground(false)
//            legend.isEnabled = false
//
//            // Configure X axis
//            xAxis.apply {
//                position = XAxis.XAxisPosition.BOTTOM
//                setDrawGridLines(false)
//                setDrawAxisLine(true)
//            }
//
//            // Configure Y axis
//            axisLeft.apply {
//                setDrawGridLines(true)
//                setDrawAxisLine(true)
//            }
//            axisRight.isEnabled = false
//        }
//
//        // Create sample data
//        val entries = ArrayList<Entry>()
//        entries.add(Entry(0f, 1000f))
//        entries.add(Entry(1f, 2000f))
//        entries.add(Entry(2f, 1500f))
//        entries.add(Entry(3f, 2500f))
//        entries.add(Entry(4f, 2000f))
//        entries.add(Entry(5f, 3000f))
//
//        val dataSet = LineDataSet(entries, "Spend Frequency").apply {
//            color = getColor(R.color.yellow)
//            setDrawFilled(true)
//            fillColor = getColor(R.color.yellow_transparent)
//            setDrawCircles(false)
//            mode = LineDataSet.Mode.CUBIC_BEZIER
//        }
//
//        lineChart.data = LineData(dataSet)
//        lineChart.invalidate()
//    }

    private fun setupBottomNav() {
        binding.bottomNav.apply {
            inflateMenu(R.menu.navbar)
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navHome -> true


                    R.id.NavProfile -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, ProfileFragment())
                            .commit()
                        true
                    }
                    else -> false
                }
            }
        }
    }

// plus button to nav to other page income and expence

//    private fun setupFab() {
//        binding.addTransactionFab.setOnClickListener {
//            showTransactionOptions()
//        }
//    }
//
//    private fun showTransactionOptions() {
//        val dialog = BottomSheetDialog(this)
//        val view = layoutInflater.inflate(R.layout.bottom_sheet_add_transaction, null)
//
//        view.findViewById<View>(R.id.incomeOption).setOnClickListener {
//            startActivity(Intent(this, AddIncomeActivity::class.java))
//            dialog.dismiss()
//        }
//
//        view.findViewById<View>(R.id.expenseOption).setOnClickListener {
//            startActivity(Intent(this, AddExpenseActivity::class.java))
//            dialog.dismiss()
//        }
//
//        dialog.setContentView(view)
//        dialog.show()
//    }

    private fun setupTransactionsList() {
        binding.transactionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity)
            adapter = TransactionsAdapter(getSampleTransactions())
        }

        binding.seeAllButton.setOnClickListener {
            // Handle see all click
        }
    }

    private fun setupTimePeriodSelection() {
        val dialog = Dialog(this).apply {
            setContentView(R.layout.time_selector_dialog)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        binding.timeSelector.setOnClickListener {
            dialog.show()
        }

        dialog.findViewById<Button>(R.id.applyButton).setOnClickListener {
            val selectedPeriod = when (dialog.findViewById<RadioGroup>(R.id.timeRadioGroup).checkedRadioButtonId) {
                R.id.todayRadio -> "Today"
                R.id.weekRadio -> "This Week"
                R.id.monthRadio -> "This Month"
                R.id.yearRadio -> "This Year"
                else -> "Today"
            }
            // Update transactions for selected period
            dialog.dismiss()
        }
    }

    private fun getSampleTransactions(): List<Transaction> {
        return listOf(
            Transaction("Item 1", -500),
            Transaction("Item 2", 1000),
            Transaction("Item 3", -750),
            Transaction("Item 4", -250),
            Transaction("Item 5", 2000)
        )
    }
}

data class Transaction(
    val title: String,
    val amount: Int
)

class TransactionsAdapter(
    private val transactions: List<Transaction>
) : RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.transactionTitle)
        private val amountText: TextView = itemView.findViewById(R.id.transactionAmount)

        fun bind(transaction: Transaction) {
            titleText.text = transaction.title
            amountText.apply {
                text = "NPR ${abs(transaction.amount)}"
                setTextColor(
                    ContextCompat.getColor(context,
                    if (transaction.amount >= 0) R.color.green else R.color.red))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount() = transactions.size
}