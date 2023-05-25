package com.example.calculator

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import com.example.calculator.databinding.FragmentMainBinding
import com.google.accompanist.themeadapter.material.MdcTheme
import constants.Constants

class MainFragment : Fragment() {
    private lateinit var inResultScreen: MutableState<Boolean>
    lateinit var dataSet: MutableState<List<String>>
    private var resultString = R.string.empty_string.toString()
    private var viewChanged = false
    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (savedInstanceState != null) {
            resultString = savedInstanceState.getString(Constants.Result.message).toString()
            viewChanged = savedInstanceState.getBoolean(Constants.ViewChanged.message)
        }
        binding = DataBindingUtil.inflate<FragmentMainBinding>(
            inflater, R.layout.fragment_main, container, false
        ).apply {
            lazyColumnComposeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            lazyColumnComposeView.setContent {
                MdcTheme {
                    inResultScreen = rememberSaveable { mutableStateOf(viewChanged) }
                    dataSet =
                        rememberSaveable { mutableStateOf(listOf(getString(R.string.add), getString(R.string.sub), getString(R.string.mul), getString(R.string.div))) }
                    LazyColumnComposable()
                }
            }
        }
        val fragmentManager = parentFragmentManager
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (this.isHidden) {
                fragmentManager
                    .beginTransaction()
                    .show(this@MainFragment)
                    .commit()
            }
        }
        fragmentManager.setFragmentResultListener(
            Constants.Result.message,
            this
        ) { _, fragmentResult ->
            viewChanged = true
            resultString = fragmentResult.getString(Constants.Result.message).toString()
            inResultScreen.value = true
            dataSet.value = listOf(resultString, getString(R.string.reset))
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(Constants.ViewChanged.message, viewChanged)
        outState.putString(Constants.Result.message, resultString)
    }

    private fun replaceFragment(operationSelected: Bundle, container: Int, fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragment.arguments = operationSelected
        fragmentTransaction.replace(container, fragment)
        fragmentTransaction.commit()
    }

    fun getViewChanged(): Boolean {
        return viewChanged
    }

    @Composable
    fun LazyColumnComposable() {
        val fragmentManager = parentFragmentManager
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(dataSet.value) { index, string ->
                if ((index == 0) && (dataSet.value.size == 2)) {
                    Text(string)
                    Spacer(modifier = Modifier.height(16.dp))
                } else {
                    Button(
                        onClick = {
                            if (inResultScreen.value) {
                                inResultScreen.value = false
                                dataSet.value = listOf(getString(R.string.add), getString(R.string.sub), getString(R.string.mul), getString(R.string.div))
                                viewChanged = false
                            } else {
                                val operationSelected = Bundle()
                                operationSelected.putString(
                                    Constants.Operation.message,
                                    dataSet.value[index]
                                )
                                replaceFragment(
                                    operationSelected,
                                    R.id.fragment_b_container,
                                    OperationFragment()
                                )
                                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    activity?.let {
                                        val fragmentTransaction =
                                            fragmentManager.beginTransaction()
                                        val operationFragment =
                                            fragmentManager.findFragmentById(R.id.fragment_b_container)
                                        operationFragment?.let {
                                            fragmentTransaction.show(operationFragment)
                                        }
                                        fragmentTransaction.hide(this@MainFragment)
                                        fragmentTransaction.commit()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .width(110.dp)
                            .height(60.dp)
                            .clip(RoundedCornerShape(10.dp))

                    ) {
                        Text(text = string)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }

}
