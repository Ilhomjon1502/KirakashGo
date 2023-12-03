package uz.ilhomjon.kirakashgo.presentation.screens.onboarding

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.ilhomjon.kirakashgo.R
import uz.ilhomjon.kirakashgo.data.local.sharedpref.MySharedPreference
import uz.ilhomjon.kirakashgo.databinding.FragmentOnboardingBinding
import uz.ilhomjon.kirakashgo.presentation.viewmodel.DriverProfileViewModel
import uz.ilhomjon.kirakashgo.presentation.viewmodel.DriverViewModel
import uz.ilhomjon.kirakashgo.presentation.viewmodel.utils.Status
import kotlin.coroutines.CoroutineContext

private const val TAG = "OnboardingFragment"

@AndroidEntryPoint
class OnboardingFragment : Fragment(), CoroutineScope {

    private val binding by lazy { FragmentOnboardingBinding.inflate(layoutInflater) }
    private val viewModel: DriverProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        launch {
//            driverViewModel.getDriverToken("911701078").collectLatest {
//                Log.d("Test", "onCreateView: $it")
//            }
//        }

        MySharedPreference.init(binding.root.context)
        val token = MySharedPreference.token
        val order = MySharedPreference.oder
        Log.d("KeshOrder", "onCreateView: $order")
        if (order != null) {
//            findNavController().navigate(R.id.orderActionFragment)
        }

        Log.d("Test", "onCreateView: $token")

        val navOption = NavOptions.Builder()
        navOption.setEnterAnim(R.anim.ochilish_1)
        navOption.setPopEnterAnim(R.anim.ochilish_2)
        navOption.setExitAnim(R.anim.yopilish_2)
        navOption.setPopExitAnim(R.anim.yopilish_1)

        binding.nextBtn.setOnClickListener {
            if (token.access == null) {
                findNavController().popBackStack()
                findNavController().navigate(
                    R.id.registerFragment,
                    bundleOf("1" to 1),
                    navOption.build()
                )
            } else {
                launch(Dispatchers.Main) {
                    try {
                        coroutineScope {
                            viewModel.getDriverProfile(apiKey = token.access).collectLatest {
                                Log.d("MenuFragmentDriverData", "onCreateView: $it")
                                when (it?.status) {
                                    Status.LOADING -> {
                                        binding.nextBtn.isEnabled = false
                                        binding.nextBtn.text = it.message
                                        binding.progressBar.visibility = View.VISIBLE
                                    }

                                    Status.ERROR -> {
                                        binding.nextBtn.isEnabled = true
                                        binding.nextBtn.text = "Boshlash"
                                        binding.progressBar.visibility = View.INVISIBLE
                                        val dialog = AlertDialog.Builder(binding.root.context)
                                        dialog.setTitle("Xatolik")
                                        dialog.setMessage("${it.message}")
                                        dialog.show()
                                    }

                                    Status.SUCCESS -> {
                                        binding.nextBtn.isEnabled = true
                                        binding.nextBtn.text = "Boshlash"
                                        binding.progressBar.visibility = View.INVISIBLE
                                        findNavController().popBackStack()
                                        findNavController().navigate(
                                            R.id.homeFragment,
                                            bundleOf("1" to 1),
                                            navOption.build()
                                        )
                                    }

                                    else -> {
                                        Toast.makeText(
                                            context,
                                            "Xatolik yuz berdi, nimada ekanlgini topa olmadik\n Iltimos qayta dasturga kiring",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.d(TAG, "onCreateView: ${e.message}")
                        Toast.makeText(context, "Internet bilan muammo", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return binding.root
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}