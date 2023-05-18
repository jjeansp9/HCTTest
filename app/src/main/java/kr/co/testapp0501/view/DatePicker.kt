package kr.co.testapp0501.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import kr.co.testapp0501.view.activity.ProfileUpdateActivity
import kr.co.testapp0501.view.activity.SignUpActivity
import kr.co.testapp0501.view.activity.SignUpSnsActivity
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {

        if (activity is SignUpActivity){
            val activitys = activity as SignUpActivity?
            activitys?.processDatePickerResult(year,month,day)

        }else if (activity is SignUpSnsActivity){
            val activitys = activity as SignUpSnsActivity?
            activitys?.processDatePickerResult(year,month,day)

        }else if (activity is ProfileUpdateActivity){
            val activitys = activity as ProfileUpdateActivity?
            activitys?.processDatePickerResult(year,month,day)
        }
    }
}