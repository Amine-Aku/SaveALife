package com.impression.savealife.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.impression.savealife.R
import com.impression.savealife.models.Cst

class EditDialog : AppCompatDialogFragment(), AdapterView.OnItemSelectedListener {

    private val TAG = "EditDialog"

    private lateinit var listener: EditDialogClickListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d(TAG, "onCreateDialog: Creating...")
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_edit, null)

        val citySpinner = view.findViewById<Spinner>(R.id.edit_city_spinner)
        val bloodTypeSpinner = view.findViewById<Spinner>(R.id.edit_bloodType_spinner)

        val dialogBuilder = AlertDialog.Builder(activity!!)
            .setView(view)
            .setTitle(getString(R.string.dialog_edit_title))
            .setNegativeButton(getText(R.string.cancel)){ dialog, which -> }
            .setPositiveButton(getText(R.string.edit)){ dialog, which ->
                listener.onDialogEditClick(citySpinner.selectedItem.toString(), bloodTypeSpinner.selectedItem.toString())
            }

        Log.d(TAG, "onCreateDialog: Setting Spinner")
        setSpinner(citySpinner, Cst.CITY_NAMES_LIST(), Cst.currentUser!!.city!!)
        setSpinner(bloodTypeSpinner, Cst.BLOOD_TYPE_LIST, Cst.currentUser!!.bloodType!!)

        Log.d(TAG, "onCreateDialog: Dialog Created")
        return dialogBuilder.create()
    }

    private fun setSpinner(spinner: Spinner, list: List<String>, motif: String) {
        val adapter = ArrayAdapter(context!!.applicationContext, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
        var pos: Int
        list.forEachIndexed { index, s ->
            if(s == motif) {
                spinner.setSelection(index)
                return@forEachIndexed
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.d(TAG, "onNothingSelected: ")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val itemSelected = parent!!.getItemAtPosition(position).toString()
        Log.d(TAG, "onItemSelected: Item ($itemSelected) is selected in the spinner")
    }

    interface EditDialogClickListener{
        fun onDialogEditClick(city: String, bloodType: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as EditDialogClickListener
        }catch (e: ClassCastException){
            throw java.lang.ClassCastException("$context must implement EditDialogClickListener")
        }
    }


}
