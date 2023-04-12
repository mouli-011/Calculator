package com.example.calculator

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import com.example.calculator.databinding.FragmentMainBinding
import constants.Constants

class MainFragment : Fragment() {
    private var viewChanged = false
    private lateinit var binding: FragmentMainBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        if (savedInstanceState != null) {
            binding.resultTextView.text =
                savedInstanceState.getString(Constants.RESULT.name).toString()
            viewChanged = savedInstanceState.getBoolean(Constants.VIEWCHANGED.name)
        }
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewChanged) {
                    startingScreen()
                } else {
                    activity?.finish()
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
        val fragmentManager = parentFragmentManager
        fragmentManager.setFragmentResultListener(
            Constants.RESULT.name,
            this
        ) { _, fragmentResult ->
            displayResult(fragmentResult.getString(Constants.RESULT.name).toString())
        }
        if (!viewChanged)
            startingScreen()
        else
            displayResult(binding.resultTextView.text.toString())
        return binding.root
    }

    private fun startingScreen() {
        viewChanged = false
        with(binding) {
            operationLayout.visibility = View.VISIBLE
            resultLayout.visibility = View.GONE
        }
        setListeners()
    }

    private fun setListeners() {
        val listener = OnClickListener {
            val operationSelected = Bundle()
            when (it) {
                binding.addButton -> operationSelected.putString(
                    Constants.OPERATION.name,
                    getString(R.string.add)
                )
                binding.subButton -> operationSelected.putString(
                    Constants.OPERATION.name,
                    getString(R.string.sub)
                )
                binding.mulButton -> operationSelected.putString(
                    Constants.OPERATION.name,
                    getString(R.string.mul)
                )
                binding.divButton -> operationSelected.putString(
                    Constants.OPERATION.name,
                    getString(R.string.div)
                )
            }
            replaceFragment(operationSelected, R.id.fragment_b_container, OperationFragment())
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                activity?.let {
                    val fragmentBContainer: FrameLayout =
                        activity?.findViewById(R.id.fragment_b_container)!!
                    val fragmentBLayoutParams =
                        fragmentBContainer.layoutParams as LinearLayout.LayoutParams
                    val fragmentAContainer: FrameLayout =
                        activity?.findViewById(R.id.fragment_a_container)!!
                    val fragmentALayoutParams =
                        fragmentAContainer.layoutParams as LinearLayout.LayoutParams
                    fragmentALayoutParams.weight = 0f
                    fragmentBLayoutParams.weight = 1f
                    fragmentAContainer.layoutParams = fragmentALayoutParams
                    fragmentBContainer.layoutParams = fragmentBLayoutParams
                }
            }
        }
        with(binding) {
            addButton.setOnClickListener(listener)
            subButton.setOnClickListener(listener)
            mulButton.setOnClickListener(listener)
            divButton.setOnClickListener(listener)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(Constants.VIEWCHANGED.name, viewChanged)
        if (this::binding.isInitialized) {
            outState.putString(Constants.RESULT.name, binding.resultTextView.text.toString())
        }
    }

    private fun replaceFragment(operationSelected: Bundle, container: Int, fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragment.arguments = operationSelected
        fragmentTransaction.replace(container, fragment)
        fragmentTransaction.commit()
    }

    private fun displayResult(resultString: String) {
        viewChanged = true
        with(binding) {
            operationLayout.visibility = View.GONE
            resultLayout.visibility = View.VISIBLE
            with(resultTextView) {
                text = resultString
            }
            resetButton.setOnClickListener {
                startingScreen()
            }
        }
    }
}