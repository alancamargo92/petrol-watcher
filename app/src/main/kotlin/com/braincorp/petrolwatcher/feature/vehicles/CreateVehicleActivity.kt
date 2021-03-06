package com.braincorp.petrolwatcher.feature.vehicles

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import com.braincorp.petrolwatcher.DependencyInjection
import com.braincorp.petrolwatcher.R
import com.braincorp.petrolwatcher.feature.vehicles.adapter.VehicleDetailsAdapter
import com.braincorp.petrolwatcher.feature.vehicles.api.VehicleApi
import com.braincorp.petrolwatcher.feature.vehicles.contract.CreateVehicleActivityContract
import com.braincorp.petrolwatcher.feature.vehicles.model.Vehicle
import com.braincorp.petrolwatcher.feature.vehicles.presenter.CreateVehicleActivityPresenter
import com.braincorp.petrolwatcher.utils.populateSpinner
import com.braincorp.petrolwatcher.utils.setEditTextValue
import com.braincorp.petrolwatcher.utils.startVehicleListActivity
import com.braincorp.petrolwatcher.utils.toRange
import kotlinx.android.synthetic.main.activity_create_vehicle.*
import kotlinx.android.synthetic.main.content_create_vehicle.*

/**
 * The activity where vehicles are created
 */
class CreateVehicleActivity : AppCompatActivity(), CreateVehicleActivityContract.View,
        AdapterView.OnItemSelectedListener, View.OnClickListener {

    companion object {
        private const val KEY_INPUT_TYPE = "input_type"
        private const val KEY_YEAR_RANGE = "year_range"
        private const val KEY_MANUFACTURERS = "manufacturers"
        private const val KEY_MODELS = "models"
        private const val KEY_DETAILS = "details"
        private const val KEY_SELECTED_YEAR = "selected_year"
        private const val KEY_SELECTED_MANUFACTURER = "selected_manufacturer"
        private const val KEY_SELECTED_MODEL = "selected_model"
        private const val KEY_SELECTED_DETAILS = "selected_details"
        private const val KEY_DISPLAYED_INFO = "displayed_info"
    }

    override lateinit var presenter: CreateVehicleActivityContract.Presenter

    // region private fields
    private var yearRange = IntRange.EMPTY
    private var selectedYear = 0
    private var manufacturers = ArrayList<String>()
    private var selectedManufacturer = ""
    private var models = ArrayList<String>()
    private var selectedModel = ""
    private var detailsList = ArrayList<Vehicle.Details>()
    private var selectedDetails = Vehicle.Details()

    private var yearSelectedCount = 0
    private var manufacturerSelectedCount = 0
    private var modelSelectedCount = 0

    private var inputType = InputType.AUTO

    private var displayedInfo = false
    // endregion

    // region activity functions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_vehicle)
        setupToolbar()
        fab.setOnClickListener(this)
        val baseUrl = DependencyInjection.vehicleApiBaseUrl
        presenter = CreateVehicleActivityPresenter(VehicleApi.getApi(baseUrl), view = this)
        if (savedInstanceState != null)
            restoreInstanceState(savedInstanceState)

        if (!displayedInfo)
            displayInfo()

        if (yearRange.isEmpty())
            presenter.getYearRange()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_create_vehicle, menu)
        menu?.let {
            if (inputType == InputType.AUTO)
                it.findItem(R.id.item_auto_input).isChecked = true
            else
                it.findItem(R.id.item_manual_input).isChecked = true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.isChecked = true

        when (item?.itemId) {
            R.id.item_auto_input -> setupAutoInput()
            R.id.item_manual_input -> setupManualInput()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (outState == null) return

        with (outState) {
            putSerializable(KEY_INPUT_TYPE, inputType)
            putBoolean(KEY_DISPLAYED_INFO, displayedInfo)

            yearRange.let {
                if (!it.isEmpty())
                    putIntArray(KEY_YEAR_RANGE, it.toList().toIntArray())
            }

            manufacturers.let {
                if (it.isNotEmpty())
                    putStringArrayList(KEY_MANUFACTURERS, it)
            }

            models.let {
                if (it.isNotEmpty())
                    putStringArrayList(KEY_MODELS, it)
            }

            detailsList.let {
                if (it.isNotEmpty())
                    putParcelableArrayList(KEY_DETAILS, it)
            }

            selectedYear.let {
                if (it != 0)
                    putInt(KEY_SELECTED_YEAR, it)
            }

            selectedManufacturer.let {
                if (it != "")
                    putString(KEY_SELECTED_MANUFACTURER, it)
            }

            selectedModel.let {
                if (it != "")
                    putString(KEY_SELECTED_MODEL, it)
            }

            putParcelable(KEY_SELECTED_DETAILS, selectedDetails)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null)
            restoreInstanceState(savedInstanceState)
    }
    // endregion

    // region view functions
    override fun onClick(v: View) {
        if (v.id == R.id.fab) {
            val vehicle = Vehicle(manufacturer = selectedManufacturer,
                    model = selectedModel,
                    year = selectedYear,
                    details = selectedDetails)
            presenter.saveVehicle(vehicle)
        }
    }
    // endregion

    // region spinner functions
    override fun onItemSelected(spinner: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (spinner?.id) {
            R.id.spn_year -> {
                if (++yearSelectedCount > 1) {
                    selectedYear = yearRange.toList()[position]
                    presenter.getManufacturers(selectedYear)
                }
            }

            R.id.spn_manufacturer -> {
                if (++manufacturerSelectedCount > 1) {
                    selectedManufacturer = manufacturers[position]
                    presenter.getModels(selectedYear, selectedManufacturer)
                }
            }

            R.id.spn_model -> {
                if (++modelSelectedCount > 1) {
                    selectedModel = models[position]
                    presenter.getDetails(selectedYear, selectedManufacturer, selectedModel)
                }
            }

            R.id.spn_details -> {
                selectedDetails = detailsList[position]
            }
        }
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) { }
    // endregion

    // region contract functions
    /**
     * Sets the year range
     *
     * @param range the range
     */
    override fun setYearRange(range: IntRange) {
        this.yearRange = range
        populateSpinner(spn_year, range, this)
    }

    /**
     * Sets the manufacturer list
     *
     * @param manufacturers the list
     */
    override fun setManufacturerList(manufacturers: ArrayList<String>) {
        this.manufacturers = manufacturers
        populateSpinner(spn_manufacturer, manufacturers, this)
    }

    /**
     * Sets the models list
     *
     * @param models the list
     */
    override fun setModelsList(models: ArrayList<String>) {
        this.models = models
        populateSpinner(spn_model, models, this)
    }

    /**
     * Sets the vehicle details list
     *
     * @param detailsList the list
     */
    override fun setDetailsList(detailsList: ArrayList<Vehicle.Details>) {
        this.detailsList = detailsList
        with (spn_details) {
            adapter = null
            adapter = VehicleDetailsAdapter(this@CreateVehicleActivity, detailsList)
            onItemSelectedListener = this@CreateVehicleActivity
        }
    }

    /**
     * Shows the vehicle list
     */
    override fun showVehicleList() {
        startVehicleListActivity(finishCurrent = true)
    }

    /**
     * Shows an invalid vehicle dialogue
     */
    override fun showInvalidVehicleDialogue() {
        AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setMessage(R.string.invalid_vehicle_data)
                .setIcon(R.drawable.ic_error)
                .setNeutralButton(R.string.ok, null)
                .show()
    }
    // endregion

    // region private functions
    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupAutoInput() {
        inputType = InputType.AUTO

        group_manual_input.visibility = GONE
        group_auto_input.visibility = VISIBLE

        if (yearRange == IntRange.EMPTY)
            presenter.getYearRange()
    }

    private fun setupManualInput() {
        inputType = InputType.MANUAL

        group_auto_input.visibility = GONE
        group_manual_input.visibility = VISIBLE

        setEditTextValue(edt_year, selectedYear)
        setEditTextValue(edt_manufacturer, selectedManufacturer)
        setEditTextValue(edt_model, selectedModel)
        setEditTextValue(edt_trim_level, selectedDetails.trimLevel)
        setEditTextValue(edt_capacity, selectedDetails.fuelCapacity)
        setEditTextValue(edt_avg_consumption_city, selectedDetails.avgConsumptionCity)
        setEditTextValue(edt_avg_consumption_motorway, selectedDetails.avgConsumptionMotorway)
    }

    private fun restoreInstanceState(savedInstanceState: Bundle) {
        with (savedInstanceState) {
            displayedInfo = getBoolean(KEY_DISPLAYED_INFO)

            if (containsKey(KEY_INPUT_TYPE))
                inputType = getSerializable(KEY_INPUT_TYPE) as InputType

            if (containsKey(KEY_YEAR_RANGE)) {
                yearRange = getIntArray(KEY_YEAR_RANGE)!!.toRange()
                setYearRange(yearRange)
            }

            if (containsKey(KEY_MANUFACTURERS)) {
                manufacturers = getStringArrayList(KEY_MANUFACTURERS)!!
                setManufacturerList(manufacturers)
            }

            if (containsKey(KEY_MODELS)) {
                models = getStringArrayList(KEY_MODELS)!!
                setModelsList(models)
            }

            if (containsKey(KEY_DETAILS)) {
                val list = getParcelableArrayList<Vehicle.Details>(KEY_DETAILS)
                if (list != null) {
                    detailsList = list
                    setDetailsList(detailsList)
                }
            }

            if (containsKey(KEY_SELECTED_YEAR)) {
                selectedYear = getInt(KEY_SELECTED_YEAR)
                if (inputType == InputType.AUTO)
                    spn_year.setSelection(yearRange.toList().indexOf(selectedYear))
                else
                    edt_year.setText(selectedYear.toString())
            }

            if (containsKey(KEY_SELECTED_MANUFACTURER)) {
                selectedManufacturer = getString(KEY_SELECTED_MANUFACTURER)!!
                if (inputType == InputType.AUTO)
                    spn_manufacturer.setSelection(manufacturers.indexOf(selectedManufacturer))
                else
                    edt_manufacturer.setText(selectedManufacturer)
            }

            if (containsKey(KEY_SELECTED_MODEL)) {
                selectedModel = getString(KEY_SELECTED_MODEL)!!
                if (inputType == InputType.AUTO)
                    spn_model.setSelection(models.indexOf(selectedModel))
                else
                    edt_model.setText(selectedModel)
            }

            if (containsKey(KEY_SELECTED_DETAILS)) {
                selectedDetails = getParcelable(KEY_SELECTED_DETAILS)!!
                if (inputType == InputType.AUTO)
                    spn_details.setSelection(detailsList.indexOf(selectedDetails))
                else {
                    setEditTextValue(edt_trim_level, selectedDetails.trimLevel)
                    setEditTextValue(edt_capacity, selectedDetails.fuelCapacity)
                    setEditTextValue(edt_avg_consumption_city, selectedDetails.avgConsumptionCity)
                    setEditTextValue(edt_avg_consumption_motorway, selectedDetails.avgConsumptionMotorway)
                }
            }

            setupInputType()
        }
    }

    private fun setupInputType() {
        if (inputType == InputType.AUTO)
            setupAutoInput()
        else
            setupManualInput()
    }

    private fun displayInfo() {
        AlertDialog.Builder(this)
                .setTitle(R.string.info)
                .setMessage(R.string.vehicle_information_might_not_be_accurate)
                .setIcon(R.drawable.ic_info)
                .setNeutralButton(R.string.ok, null)
                .show()
        displayedInfo = true
    }
    // endregion

    private enum class InputType {
        AUTO,
        MANUAL
    }

}